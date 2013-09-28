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
import java.sql.Timestamp;
import java.util.LinkedHashMap;

/**
 * This is the main class used to manage a reader's connections and events. This class
 * will either be refactored to conform a list of readers or another class should be added to 
 * handle all the local readers
 * @author I-A
 */
public class ReaderManager {

    private HighLevelInterface ReaderXP;
    private LinkedHashMap<String, TagEvent> tagList;

    public ReaderManager() {
        ReaderXP = new HighLevelInterface();
        ReaderXP.addStateChangedEventListener(new ReaderStateChabgedListener(this));
        ReaderXP.addAsyncCallbackEventListener(new ReaderCallbackListener(this));
        
        tagList = new LinkedHashMap<>();
    }

    /**
     * The method is used to directly connect with a reader at the specified IP address
     * @param ip the ip of the target reader. The IP must be known or a default IP will be used
     * @throws Exception in case a connection was not possible
     */
    public void connect(String ip) throws Exception {
        ip = (ip == null ? "192.168.25.203" : ip);
        int r = ReaderXP.Connect(ip, 15000);
        if (r != CSLibrary.Constants.Result.OK) {
            throw new Exception("Connection failed");
        } else {
            System.out.println("Connection established with reader (ip=" + ip + ")");
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

    /**
     * The method adds the new event. It is responsible for searching if the event's tagid
     * already exists. If the tagid already exists only the timestamp of the event is added. If it 
     * is the first time a new event is added with the current timestamp
     * @param tagid the tagid of the current event
     */
    void newTagEvent(String tagid) {
        if (tagList.containsKey(tagid)) {
            tagList.get(tagid).setEventStamp(new Timestamp(System.currentTimeMillis()));
        } else {
            tagList.put(tagid, new TagEvent(tagid, new Timestamp(System.currentTimeMillis())));
        }
        
        System.out.println(tagList.get(tagid));
        System.out.println("--------------------------------------------------------------------------");
    }
}
