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


            EventManager evManager = new EventManager();
            evManager.Start();




            /*    ReaderManager testManager = new ReaderManager();
             testManager.addListener(evManager);
       
             testManager.Start(); //This is used to start ReaderManager. It actually attempts connection with IPs loaded from the cofiguration file
             */

        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
