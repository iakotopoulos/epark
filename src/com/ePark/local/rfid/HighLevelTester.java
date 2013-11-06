/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import com.ePark.local.EventManager;
import java.util.logging.Level;
import java.util.logging.Logger; 

/**
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
