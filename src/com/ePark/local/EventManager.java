/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.AppConfiguration;
import com.ePark.data.io.EparkIO;
import com.ePark.http_json.AUResponse;
import com.ePark.http_json.ArrivalResponse;
import com.ePark.http_json.DepartureResponse;
import com.ePark.http_json.HttpPoster;
import com.ePark.http_json.MessageTypeException;
import com.ePark.http_json.ParkingException;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.sensors.SerialManager;
import com.ePark.local.rfid.data.TagEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class encapsulating the business logic of the local system.
 * Its members are the the managers of the different devices and it is the only
 * class that has access to their state and data. Crucial for the whole workflow
 * is the implementation of the DeviceListener interface as everything is event
 * based. The class is also responsible for starting all the managers and
 * properly shutting down the system.
 *
 * @author I-A
 */
public class EventManager implements DeviceListener {

    private ReaderManager readerManager;
    private SerialManager waspManager;
    private HttpPoster httpPost;

    /**
     * It is the only constructor for the EventManager. It creates an instance
     * for each manager and adds them a listener. It also created an instance of
     * the HttpPoster class which is used for the communication with the main
     * system.
     */
    public EventManager() {
        httpPost = new HttpPoster();

        readerManager = new ReaderManager();
        readerManager.addListener(this);

        waspManager = new SerialManager(AppConfiguration.getProperty("serial_port"));
        waspManager.addListener(this);
    }

    /**
     * Starts all the needed managers. In this version we have ReaderManager for
     * the RFID readers and SerialManager for the waspmote gateway.
     */
    public void Start() {
        readerManager.Start();
        waspManager.Start();
    }

    /**
     * The method is called upon the shutdown of the system. It only makes sure
     * that the communication with the serial port is closed and the port
     * released. The reader are handled by the
     * {@link com.ePark.local.rfid.ReaderManager} himself for the shutdown
     */
    @Override
    public void shutdown() {
        System.out.println("Closing ports");
        waspManager.close();
    }

    /**
     * This is the method implementing the reader handler listening for events
     * from the {@link com.ePark.local.rfid.ReaderManager}. In the Nicosia demo
     * it was ignored as the EventManager was interested only for waspmote
     * events. Every other data handling regarding the RFID readers was done by
     * the {@link com.ePark.local.rfid.ReaderManager} himself.
     *
     * @param ev the {@link com.ePark.local.rfid.data.TagEvent} that has to be
     * handled.
     */
    @Override
    public void readerNotification(TagEvent ev) {
        //In the current implementation the event is not important for the event
        //manager. A magnetic notification is needed for the confirmation
        //########################### TESTING ONLY

        /* if (ev.getTheReader().isEntrance()) {
         waspNotification("IN");
         } else {
         waspNotification("OUT");
         }*/
    }

    /**
     * This is the method implementing the waspmote handler listening for events
     * from the serial port. It is actually listening for events from the
     * waspmote magnetic sensors. It is the critical handler implementing the
     * logic of the local system. It is also responsible for communicating with
     * the central system.
     *
     * @param pos it is a String 'IN' || 'OUT' passed from the
     * {@link com.ePark.local.sensors.SerialManager} and indicating the position
     * (i.e. entrance or exit) of the triggering sensor
     */
    @Override
    public void waspNotification(String pos) {
        System.out.println("calling " + pos);

        if (pos != null && pos.equals("IN")) {
            TagEvent theTagEvent = readerManager.getLastINEvent();
            if (theTagEvent != null) {
                processArrival(theTagEvent);
            }
            sendAvailabilityUpdate(pos, theTagEvent);

        } else if (pos != null) {
            TagEvent theTagEvent = readerManager.getLastOUTEvent();
            processDeparture(theTagEvent);
            sendAvailabilityUpdate(pos, theTagEvent);
        }

    }

    /**
     * It is a private method used by the EventManager for processing an arrival
     * event. The method must: communicate the event to the central server.
     * Store the event to the local database.
     *
     * @param te the {@link com.ePark.local.rfid.data.TagEvent} to be processed
     */
    private void processArrival(TagEvent te) {
        ArrivalResponse response = null;
        try {            // 
            System.out.println("Store");
            response = sendArrival("IN", te);

        } catch (ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Store arr");
            if (response != null) {
                EparkIO.storeArrival(te, true);
            } else {
                EparkIO.storeArrival(te, false);
                //no demo new Thread(new ResendTask(this, te)).start();
            }
            readerManager.removeTag(te.getTagid());
        }
    }

    /**
     * It is a private method used by the EventManager for processing a
     * departure event. The method must: communicate the event to the central
     * server. Update the local database.
     *
     * @param te the {@link com.ePark.local.rfid.data.TagEvent} to be processed
     */
    private void processDeparture(TagEvent te) {
        DepartureResponse response = null;

        try {            // 

            response = sendDeparture("OUT", te);

        } catch (ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Store dep");
            if (response != null) {
                Double fee = response.getFeeAmount();
                EparkIO.storeCompletion(te, true, fee);
            } else {
                EparkIO.storeCompletion(te, false, -1d);
                //no demo new Thread(new ResendTask(this, te)).start();
            }
            readerManager.removeTag(te.getTagid());
        }

    }

    /**
     * The method is used used to send data regarding a departure to the central
     * server. The respective web service is used.
     *
     * @param mtype the message type OUT|OUTOFFLINE. You can find more details
     * in {
     * @see com.ePark.http_json.HttpPoster}
     * @param theTagEvent the actual event that triggered the handler and has to
     * be send
     * @return the {@link com.ePark.http_json.DepartureResponse} as a wrapper of
     * the actual response of the web service call
     * @throws ParkingException an exception regarding the status of the
     * outgoing vehicle/tag. It is thrown in order to be propagated. Any other
     * exception is caught within the method.
     */
    public DepartureResponse sendDeparture(String mtype, TagEvent theTagEvent) throws ParkingException {
        DepartureResponse response = null;
        try {

            response = httpPost.postDeparture(mtype, "1.0", AppConfiguration.getProperty("parking_name"), theTagEvent.getTagid(), "123", theTagEvent.getEventStampString(), theTagEvent.getTheReader().getIp(), "0");
            return response;

        } catch (SocketTimeoutException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MessageTypeException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * The method is a wrapper to the web service used for querying or updating
     * the availability of the parking slots. Here it is used in order to either
     * increase or decrease the availability by 1 after detecting an arrival or
     * a departure.
     *
     * @param type the type of the event IN|EX for an arrival or a departure
     * @param theTagEvent the event that triggered this call.
     */
    public void sendAvailabilityUpdate(String type, TagEvent theTagEvent) {

        int in = 0;
        int out = 0;

        in = (type.equals("IN")) ? 1 : 0;
        out = (type.equals("EX")) ? 1 : 0;
        try {
            AUResponse au_test = httpPost.postAvailabilityUpdate(AppConfiguration.getProperty("parking_name"), in + "", out + "", theTagEvent.getTheReader().getIp());

        } catch (SocketTimeoutException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessageTypeException | ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The method is used used to send data regarding an arrival to the central
     * server. The respective web service is used.
     *
     * @param mtype the message type IN|INOFFLINE. You can find more details in
     * {
     * @see com.ePark.http_json.HttpPoster}
     * @param theTagEvent the actual event that triggered the handler and has to
     * be send
     * @return the {@link com.ePark.http_json.ArrivalResponse} as a wrapper of
     * the actual response of the web service call
     * @throws ParkingException an exception regarding the status of the
     * incoming vehicle/tag. It is thrown in order to be propagated. Any other
     * exception is caught within the method.
     */
    public ArrivalResponse sendArrival(String mtype, TagEvent theTagEvent) throws ParkingException {
        ArrivalResponse response;

        try {

            response = httpPost.postArrival(mtype, "1.0", AppConfiguration.getProperty("parking_name"), theTagEvent.getTagid(), "123", theTagEvent.getEventStampString(), theTagEvent.getTheReader().getIp());
            return response;

        } catch (SocketTimeoutException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MessageTypeException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
