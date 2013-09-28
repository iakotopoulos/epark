/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epark.local.rfid;

import CSLibrary.Constants.RFState;
import CSLibrary.Constants.Result;
import CSLibrary.Events.StateChangedEventArgs;
import CSLibrary.Events.StateChangedEventListener;
import static epark.local.rfid.ReaderHighLevelTest.ReaderXP;

/**
 *
 * @author I-A
 */
public class ReaderStateChabgedListener implements StateChangedEventListener {

    private final ReaderManager the_Manager;
    private Thread reset;

    public ReaderStateChabgedListener(ReaderManager rmgr) {
        the_Manager = rmgr;
    }

    @Override
    public void StateChangedEvent(StateChangedEventArgs ev) {
        System.out.println("StateChanged");
        switch (ev.state) {
            case RFState.IDLE:
                //Check whether fail
                if (ReaderXP.LastResultCode() == Result.NETWORK_RESET) {
                    //Use other thread to create progress
                    reset = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if ((ReaderXP.Reconnect()) == Result.OK) {
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
