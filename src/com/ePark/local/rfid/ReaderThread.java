/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import CSLibrary.Constants.Operation;
import CSLibrary.Constants.RFState;
import CSLibrary.Constants.RadioOperationMode;
import CSLibrary.Constants.SelectFlags;
import CSLibrary.HighLevelInterface;

/**
 *
 * @author I-A
 */
public class ReaderThread implements Runnable {

    private HighLevelInterface ReaderXP;
    //  private ReaderManager rmgr;
    private String ip;

    public ReaderThread(String ip) {
        // rmgr = mg;
        this.ip = (ip == null ? "192.168.25.203" : ip);

        ReaderXP = new HighLevelInterface();

    }

    @Override
    public void run() {
        System.out.println("begin connecting " + ip);


        int r = ReaderXP.Connect(ip, 30000);
        if (r != CSLibrary.Constants.Result.OK) {
            System.out.println("Connection failed " + r + " with reader (ip=" + ip + ")");
            ReaderXP.Disconnect();
            Thread.currentThread().interrupt();

        } else {
            System.out.println("Connection established with reader (ip=" + ip + ")");
            ReaderXP.addStateChangedEventListener(new ReaderTaskStateChangedListener(this, ip));
            ReaderXP.addAsyncCallbackEventListener(new ReaderCallbackListener(null, ip));
            startReader();
        }


    }

    public void startReader() {
        if (ReaderXP.GetState() == RFState.IDLE) {
            ReaderXP.SetOperationMode(Settings.custInventory_continuous ? RadioOperationMode.CONTINUOUS : RadioOperationMode.NONCONTINUOUS);
            ReaderXP.SetTagGroup(Settings.tagGroup);
            ReaderXP.SetSingulationAlgorithmParms(Settings.singulation, Settings.GetSingulationAlg());
            ReaderXP.GetOptions().TagInventory.flags = SelectFlags.ZERO;
            ReaderXP.StartOperation(Operation.TAG_RANGING, Settings.custInventory_blocking_mode);
        }
    }

    public HighLevelInterface getReaderXP() {
        return ReaderXP;
    }
}
