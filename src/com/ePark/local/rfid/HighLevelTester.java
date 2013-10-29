/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import com.ePark.local.EventManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I-A
 */
public class HighLevelTester {

    public static void main(String[] args) {
        try {
            EventManager eventManager = new EventManager();


            eventManager.Start(); //This is used to start EventManager. He is responsible for starting any other managers (readers, amagnetic etc)


        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
