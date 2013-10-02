/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I-A
 */
public class HighLevelTester {
    
    public static void main(String[] args) {
        try {
            ReaderManager testManager = new ReaderManager();
            testManager.connect(null);
            testManager.Start();
        } catch (Exception ex) {
            Logger.getLogger(HighLevelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
