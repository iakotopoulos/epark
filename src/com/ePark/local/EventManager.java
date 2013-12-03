/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.AppConfiguration;
import com.ePark.data.io.EparkIO;
import com.ePark.http_json.ArrivalResponse;
import com.ePark.http_json.DepartureResponse;
import com.ePark.http_json.HttpPoster;
import com.ePark.http_json.MessageTypeException;
import com.ePark.http_json.ParkingException;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.sensors.SerialManager;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import com.ePark.local.tasks.ResendTask;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 *
 * @author I-A
 */
public class EventManager implements DeviceListener {

    private ReaderManager readerManager;
    private SerialManager waspManager;
    private HttpPoster httpPost;

    public EventManager() {
        httpPost = new HttpPoster();

        readerManager = new ReaderManager();
        readerManager.addListener(this);

        waspManager = new SerialManager(AppConfiguration.getProperty("serial_port"));
        waspManager.addListener(this);
    }

    /**
     * Start managers. For now only Readers
     */
    public void Start() {
        readerManager.Start();
        waspManager.Start();
    }

    @Override
    public void readerNotification(TagEvent ev) {
        //In the current implementation the event is not important for the event
        //manager. A magnetic notification is needed for the confirmation

        //########################### TESTING ONLY
        waspNotification(null);
    }

    /**
     * This is the magnetic sensor notification. In the current version for the
     * testing needs the manager does not recognize IN and OUT magnetic sensors
     */
    @Override
    public void waspNotification(String pos) {

        //Supposing it is a magnetic at the entrance            
        TagEvent theTagEvent = readerManager.getLastINEvent();

   //     processDeparture(theTagEvent);


    }

    private void processArrival(TagEvent te, boolean send) {
        ArrivalResponse response = null;
        try {            // 

            response = sendArrival("IN", te);

        } catch (ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (response != null) {
                EparkIO.storeArrival(te, true);
            } else {
                EparkIO.storeArrival(te, false);
                //no demo new Thread(new ResendTask(this, te)).start();
            }
            readerManager.removeTag(te.getTagid());
        }
    }

    private void processDeparture(TagEvent te) {
        DepartureResponse response = null;
        try {            // 

            response = sendDeparture("OUT", te);

        } catch (ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

    public DepartureResponse sendDeparture(String mtype, TagEvent theTagEvent) throws ParkingException {
        DepartureResponse response = null;
        try {

            response = httpPost.postDeparture(mtype, "1.0", AppConfiguration.getProperty("parking_name"), "1234567890", "123", theTagEvent.getEventStampString(), theTagEvent.getTheReader().getIp(), "0");
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

    public ArrivalResponse sendArrival(String mtype, TagEvent theTagEvent) throws ParkingException {
        ArrivalResponse response;

        try {

            response = httpPost.postArrival(mtype, "1.0", AppConfiguration.getProperty("parking_name"), "1234567890"/*theTagEvent.getTagid()*/, "123", theTagEvent.getEventStampString(), theTagEvent.getTheReader().getIp());
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
