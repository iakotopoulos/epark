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
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This is the main class used to manage a reader's connections and events. This
 * class will either be refactored to conform a list of readers or another class
 * should be added to handle all the local readers
 *
 * @author I-A
 */
public class ReaderManager {

   // private HighLevelInterface ReaderXP1;
    private LinkedHashMap<String, TagEvent> tagList;
    private LinkedHashMap<String, HighLevelInterface> readerList;

    public ReaderManager() {
        readerList = new LinkedHashMap<>();
        tagList = new LinkedHashMap<>();

    }

    /**
     * The method is used to directly connect with a reader at the specified IP
     * address
     *
     * @param ip the ip of the target reader. The IP must be known or a default
     * IP will be used
     * @throws Exception in case a connection was not possible
     */
    public void connect(String ip) throws Exception {
        HighLevelInterface newReaderXP = new HighLevelInterface();
        ip = (ip == null ? "192.168.25.203" : ip);
        
        newReaderXP.addStateChangedEventListener(new ReaderStateChabgedListener(this, ip));
        newReaderXP.addAsyncCallbackEventListener(new ReaderCallbackListener(this, ip));
        
        int r = newReaderXP.Connect(ip, 15000);
        if (r != CSLibrary.Constants.Result.OK) {
            throw new Exception("Connection failed");
        } else {
            System.out.println("Connection established with reader (ip=" + ip + ")");
            readerList.put(ip, newReaderXP);
        }
    }

    public HighLevelInterface getReaderXP(String ip) {
        return readerList.get(ip);
    }

    public void Start() {
        for(HighLevelInterface r: readerList.values()){
            startReader(r);
        }
    }

    public void startReader(HighLevelInterface rfReader) {
        if (rfReader.GetState() == RFState.IDLE) {
            rfReader.SetOperationMode(Settings.custInventory_continuous ? RadioOperationMode.CONTINUOUS : RadioOperationMode.NONCONTINUOUS);
            rfReader.SetTagGroup(Settings.tagGroup);
            rfReader.SetSingulationAlgorithmParms(Settings.singulation, Settings.GetSingulationAlg());

            rfReader.GetOptions().TagInventory.flags = SelectFlags.ZERO;
            rfReader.StartOperation(Operation.TAG_RANGING, Settings.custInventory_blocking_mode);
        }
    }

    /**
     * The method adds the new event. It is responsible for searching if the
     * event's tagid already exists. If the tagid already exists only the
     * timestamp of the event is added. If it is the first time a new event is
     * added with the current timestamp
     *
     * @param tagid the tagid of the current event
     */
    void newTagEvent(String tagid) {
        if (tagList.containsKey(tagid)) {
            tagList.get(tagid).setEventStamp(new Timestamp(System.currentTimeMillis()));
        } else {
            tagList.put(tagid, new TagEvent(tagid, new Timestamp(System.currentTimeMillis())));
        }
        if (tagList.get(tagid).getEcount() % 20 == 0) {
            //System.out.println(tagList.get(tagid));
            System.out.println("--------------------------------------------------------------------------");
        }
    }
    
    public TagEvent getEvent(String id){
        return tagList.get(id);        
    }
}
