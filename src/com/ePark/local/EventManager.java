/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.EparkIO;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.ReaderManager;
import com.ePark.local.rfid.SerialManager;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import com.ePark.local.serial.SerialTest;

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
        
        waspManager = new SerialManager();
        waspManager.addListener(this);
    }
    
    /**
     * Start managers. For now only Readers
     */
    public void Start(){
        readerManager.Start();
        waspManager.Start();
    }

    @Override
    public void readerNotification(TagEvent ev) {        
        //Temporarly use this as an entrance event notification. next step is to use a magnetic notification
        if (ev.getTheReader().isEntrance()) {
            EparkIO.storeArrival(ev);
        }
    }

    @Override
    public void waspNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
