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
import com.ePark.local.task.ReaderProcess;
import java.io.PrintWriter;
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
public class ReaderManagerP {

    public static final int MAX_PROCESS = 5;
    private Thread[] m_run_process;
    public static Process[] m_javap;
    public static int lastProcessId;
    static PrintWriter outputCommand;
    private LinkedHashMap<String, TagEvent> tagList;
    private LinkedHashMap<String, HighLevelInterface> readerList;

    public ReaderManagerP() {
        readerList = new LinkedHashMap<>();
        tagList = new LinkedHashMap<>();
        lastProcessId = 0;
        m_run_process = new Thread[MAX_PROCESS];
        m_javap = new Process[MAX_PROCESS];
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
        ip = (ip == null ? "192.168.25.203" : ip);

        m_run_process[lastProcessId] = new Thread(new ReaderProcess(ip));        
        m_run_process[lastProcessId].start();
        m_run_process[lastProcessId].join(500);

        ++lastProcessId;


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

        System.out.println(tagList.get(tagid));
        System.out.println("--------------------------------------------------------------------------");
    }
}
