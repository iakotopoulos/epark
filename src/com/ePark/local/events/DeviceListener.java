/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.events;

import com.ePark.local.rfid.data.TagEvent;

/**
 * A generalized interface to be implemented for all kind of device listeners.
 * It also contains a shutdown method for each device
 *
 * @author I-A
 */
public interface DeviceListener {

    /**
     * An event listener for the {@link com.ePark.local.rfid.ReaderManager}
     * @param ev the triggering event
     */
    public void readerNotification(TagEvent ev);

    /**
     * An event listener for the {@link com.ePark.local.sensors.SerialManager}
     * @param pos the position (entrance|exit) of the triggering sensor
     */
    public void waspNotification(String pos);

    /**
     * Implement a proper shutdown for each device manager
     */
    public void shutdown();
}
