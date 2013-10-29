/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.EparkIO;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;

/**
 *
 * @author I-A
 */
public class EventManager implements DeviceListener {

    ReaderManager rManager;

    public EventManager() {

        rManager = new ReaderManager();
        //Add Listener
        rManager.addListener(this);

    }

    public void Start() {
        rManager.Start();//This is used to start ReaderManager. It actually attempts connection with IPs loaded from the cofiguration file
    }

    @Override
    public void readerNotification(TagEvent ev) {
        //Temporarly use this as an entrance event notification. next step is to use a magnetic notification
        if (ev.getTheReader().isEntrance()) {
            EparkIO.storeArrival(ev);
        }
    }

    @Override
    public void magneticNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
