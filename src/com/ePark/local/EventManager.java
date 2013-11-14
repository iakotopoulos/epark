/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.AppConfiguration;
import com.ePark.data.io.EparkIO;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.rfid.SerialManager;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;

/**
 *
 * @author I-A
 */
public class EventManager implements DeviceListener {

    ReaderManager readerManager;
    SerialManager waspManager;

    public EventManager() {
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

    @Override
    public void waspNotification() {
        //    EparkIO.storeArrival(ev);
    }
}
