/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.events;

import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;

/**
 *
 * @author I-A
 */
public interface DeviceListener {
    
    public void readerNotification(TagEvent ev);
    public void waspNotification(String pos);
        
}
