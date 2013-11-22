/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.AppConfiguration;
import com.ePark.data.io.EparkIO;
import com.ePark.http_json.HttpPoster;
import com.ePark.http_json.MessageTypeException;
import com.ePark.http_json.ParkingException;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.rfid.SerialManager;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
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
    }

    /**
     * This is the magnetic sensor notification. In the current version for the
     * testing needs the manager does not recognize IN and OUT magnetic sensors
     */
    @Override
    public void waspNotification() {
        boolean messageSend = true;
        //Supposing it is a magnetic at the entrance            
        TagEvent theTagEvent = readerManager.getLastINEvent();

        try {

            //Supposing it is a magnetic at the entrance            
            JSONObject arrival1 = httpPost.postArrival("IN", "1.0", "PK001", "1234567892", "123", "20130923210100", "123");
//            System.out.println("ARRIVAL : " + arrival1.toString());


        } catch (SocketTimeoutException ex) {
            messageSend = false;
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessageTypeException ex) {
            messageSend = false;
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            messageSend = false;
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            messageSend = false;
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParkingException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            EparkIO.storeArrival(theTagEvent, messageSend);
        }
    }
}
