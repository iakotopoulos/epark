/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I-A
 */
public class HighLevelTester {

    public static void main(String[] args) {
        try {
            ReaderManagerP testManager = new ReaderManagerP();
            testManager.connect("192.168.25.203");         
            testManager.connect("192.168.25.111");


            /*    Thread rd = new Thread(new ReaderThread("192.168.25.203"));
             rd.start();
          
             Thread rd1 = new Thread(new ReaderThread("192.168.25.111"));
             rd1.start();
             */
            //testManager.Start();
        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
