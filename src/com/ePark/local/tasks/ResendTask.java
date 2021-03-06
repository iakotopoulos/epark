/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.tasks;

import com.ePark.http_json.ParkingException;
import com.ePark.local.EventManager;
import com.ePark.local.rfid.data.TagEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class is created in order to allow a thread to handle the resend attempts
 * on communication failures with the main system. 
 * 
 * It was not used or tested during the demo in Nicosia.
 *
 * @author ykoto
 */
public class ResendTask implements Runnable {

    private EventManager manager;
    private TagEvent theTagEvent;

    public ResendTask(EventManager manager, TagEvent ev) {
        this.manager = manager;
        theTagEvent = ev;
    }

    @Override
    public void run() {

        if (theTagEvent.getType().equals("IN")) {
            try {
                while (manager.sendArrival("IN_OFFLINE", theTagEvent) == null) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ResendTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ParkingException ex) {
                Logger.getLogger(ResendTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                while (manager.sendDeparture("OUT_OFFLINE", theTagEvent) == null) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ResendTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ParkingException ex) {
                Logger.getLogger(ResendTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Resend was successful");

    }
}
