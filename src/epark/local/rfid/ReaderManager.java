/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epark.local.rfid;

import CSLibrary.Constants.Operation;
import CSLibrary.Constants.RFState;
import CSLibrary.Constants.RadioOperationMode;
import CSLibrary.Constants.SelectFlags;
import CSLibrary.HighLevelInterface;
import epark.local.rfid.data.TagEvent;
import java.util.LinkedHashMap;

/**
 *
 * @author I-A
 */
public class ReaderManager {

    private HighLevelInterface ReaderXP; 
    private LinkedHashMap<String, TagEvent> tagList;

    public ReaderManager() {        
        ReaderXP = new HighLevelInterface();
        ReaderXP.addStateChangedEventListener(new ReaderStateChabgedListener(this));
        
        tagList = new LinkedHashMap<>();
    }

    public void connect(String ip) throws Exception {
        ip = (ip == null ? "192.168.25.203" : ip);
        int r = ReaderXP.Connect(ip, 15000);
        if (r != CSLibrary.Constants.Result.OK) {
            throw new Exception("Connection failed");
        } else {
            System.out.println("Connection established with reader (ip="+ip+")");
        }
    }

    public HighLevelInterface getReaderXP() {
        return ReaderXP;
    }
    
     public void Start() {
        if (ReaderXP.GetState() == RFState.IDLE) {
            ReaderXP.SetOperationMode(Settings.custInventory_continuous ? RadioOperationMode.CONTINUOUS : RadioOperationMode.NONCONTINUOUS);
            ReaderXP.SetTagGroup(Settings.tagGroup);
            ReaderXP.SetSingulationAlgorithmParms(Settings.singulation, Settings.GetSingulationAlg());

            ReaderXP.GetOptions().TagInventory.flags = SelectFlags.ZERO;
            ReaderXP.StartOperation(Operation.TAG_RANGING, Settings.custInventory_blocking_mode);
        }
    }
}
