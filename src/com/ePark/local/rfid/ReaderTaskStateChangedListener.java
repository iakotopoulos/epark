/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import CSLibrary.Constants.RFState;
import CSLibrary.Constants.Result;
import CSLibrary.Events.StateChangedEventArgs;
import CSLibrary.Events.StateChangedEventListener;

/**
 *
 * @author I-A
 */
public class ReaderTaskStateChangedListener implements StateChangedEventListener {

    private final ReaderThread readerThread;
    private Thread reset;
    private String ip;

    public ReaderTaskStateChangedListener(ReaderThread rmgr, String ip) {
        this.readerThread = rmgr;
        this.ip = ip;     
    }

    @Override
    public void StateChangedEvent(StateChangedEventArgs ev) {
        System.out.println("Started " + ip + " " + ev.state);
        switch (ev.state) {
            case RFState.IDLE:
                //Check whether fail
                if (readerThread.getReaderXP().LastResultCode() == Result.NETWORK_RESET) {
                    //Use other thread to create progress
                    reset = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if ((readerThread.getReaderXP().Reconnect(10)) == Result.OK) {
                                //Start inventory
                                readerThread.startReader();
                            } else {
                                System.out.println("Error!");
                                //Fail
                            }
                        }
                    });
                    reset.start();
                }
                break;
            case RFState.BUSY:
                break;
            case RFState.STOPPING:
                break;
            case RFState.NOT_INITIALIZED:
                break;
        }
    }
}
