/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import com.ePark.local.EventManager;
import com.ePark.local.tasks.ResendTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main class of the application :) Everything is handled by the
 * {@link com.ePark.local.EventManager} so an instance is created and the start
 * method is called to initialize connections etc
 *
 * @author I-A
 */
public class HighLevelTester {

    public static void main(String[] args) {
        try {

            EventManager evManager = new EventManager();
            evManager.Start();           

        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
