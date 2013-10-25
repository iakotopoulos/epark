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

            ReaderManagerP readerManager = new ReaderManagerP();
            
            //Add Listener
            readerManager.addListener(eventManager);

            //    testManager.connect("192.168.25.203");         
            //    testManager.connect("192.168.25.111");         
            readerManager.Start(); //This is used to start ReaderManager. It actually attempts connection with IPs loaded from the cofiguration file


        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
