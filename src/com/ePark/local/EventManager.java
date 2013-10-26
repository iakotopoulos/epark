/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.data.io.EparkIO;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import java.util.ArrayList;

/**
 *
 * @author I-A
 */
public class EventManager implements DeviceListener {

    public EventManager() {
    }

    @Override
    public void readerNotification(TagEvent ev) {
        //Temporarly use this as an entrance event notification. next step is to use a magnetic notification
        EparkIO.storeArrival(ev);
    }

    @Override
    public void magneticNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
