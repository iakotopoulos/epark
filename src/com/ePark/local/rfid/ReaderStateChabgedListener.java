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
public class ReaderStateChabgedListener implements StateChangedEventListener {

    private final ReaderManager the_Manager;
    private Thread reset;
    private String ip;

    public ReaderStateChabgedListener(ReaderManager rmgr, String ip) {
        this.the_Manager = rmgr;
        this.ip = ip;     
    }

    @Override
    public void StateChangedEvent(StateChangedEventArgs ev) {
        System.out.println("StateChanged");
        switch (ev.state) {
            case RFState.IDLE:
                //Check whether fail
                if (the_Manager.getReaderXP(ip).LastResultCode() == Result.NETWORK_RESET) {
                    //Use other thread to create progress
                    reset = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if ((the_Manager.getReaderXP(ip).Reconnect()) == Result.OK) {
                                //Start inventory
                                the_Manager.Start();
                            } else {
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
