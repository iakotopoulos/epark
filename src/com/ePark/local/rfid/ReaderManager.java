/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import com.ePark.data.io.AppConfiguration;
import com.ePark.local.events.DeviceListener;
import com.ePark.local.rfid.epark.local.rfid.data.Reader;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import com.ePark.local.tasks.ReaderProcess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class used to manage a reader's connections and events. This
 * class will either be refactored to conform a list of readers or another class
 * should be added to handle all the local readers
 *
 * @author I-A
 */
public class ReaderManager {

    public static final int MAX_PROCESS = 5;
    public final int MIN_OCCURENCE = 20;
    private Thread[] m_run_process;
    private Process[] m_javap;
    private int lastProcessId;
    private PrintWriter outputCommand;
    private LinkedHashMap<String, TagEvent> tagList;
    //The list of connected readers. The list is populated on start and is based on the reader IP
    private LinkedHashMap<String, Reader> readerList;
    private ArrayList<DeviceListener> listeners;

    public ReaderManager() {
        AppConfiguration.loadConfiguration();


        tagList = new LinkedHashMap<>();
        readerList = new LinkedHashMap<>();
        listeners = new ArrayList<>();

        lastProcessId = 0;
        m_run_process = new Thread[MAX_PROCESS];
        m_javap = new Process[MAX_PROCESS];

        //Start a thread waiting for shutdown command (Stop)
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Press Enter to stop");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String name = null;
                try {
                    while (true) {
                        name = br.readLine();
                        for (int i = 0; i < lastProcessId; i++) {
                            // Post command to each process to stop.
                            outputCommand = new PrintWriter(m_javap[i].getOutputStream());
                            outputCommand.println("Stop");
                            outputCommand.close();
                        }
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("Error!");
                    System.exit(1);
                }
            }
        }).start();
    }

    /**
     * The method is used to directly connect with a reader at the specified IP
     * address
     *
     * @param ip the ip of the target reader. The IP must be known or a default
     * IP will be used
     * @throws Exception in case a connection was not possible
     */
    public void connect(String ip) {
        try {
            ip = (ip == null ? "192.168.25.203" : ip);

            m_run_process[lastProcessId] = new Thread(new ReaderProcess(ip, this));
            m_run_process[lastProcessId].start();
            m_run_process[lastProcessId].join(500);

            ++lastProcessId;
        } catch (InterruptedException ex) {
            Logger.getLogger(ReaderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Start() {
        readerList = AppConfiguration.getReaders();
        for (String ip : readerList.keySet()) {
            connect(ip);
        }
    }

    /**
     * The method adds the new event. It is responsible for searching if the
     * event's tagid already exists. If the tagid already exists only the
     * timestamp of the event is added. If it is the first time a new event is
     * added with the current timestamp
     *
     * @param r the reader that will produce the event
     * @param s
     * @param tagid the tagid of the current event
     */
    public void newTagEvent(Reader r, String s, String tagid) {
        if (tagList.containsKey(tagid)) {
            tagList.get(tagid).setEventStamp(new Timestamp(System.currentTimeMillis()));
        } else {
            tagList.put(tagid, new TagEvent(tagid, new Timestamp(System.currentTimeMillis()), r));
        }

        //Check the threshold to decide if it is a tag passing through the reader
        if (tagList.get(tagid).getEcount() > MIN_OCCURENCE) {

            System.out.println("--------------------------Tag passing reader--------------------------");
            System.out.println(tagList.get(tagid));

            //Notify the event manager
            notifyListeners(tagList.get(tagid));
        }
    }

    public void addListener(DeviceListener toAdd) {
        listeners.add(toAdd);
    }

    private void notifyListeners(TagEvent ev) {
        for (DeviceListener dl : listeners) {
            dl.readerNotification(ev);
        }
    }

    public TagEvent getLastINEvent() {
        ArrayList<TagEvent> tempList = new ArrayList(tagList.values());
        Collections.sort(tempList);

        for (TagEvent ev : tempList) {
            if (ev.getTheReader().isEntrance()) {
                return ev;
            }
        }
        return null;
    }

    public TagEvent getLastOUTEvent() {
        ArrayList<TagEvent> tempList = new ArrayList(tagList.values());
        Collections.sort(tempList);

        for (TagEvent ev : tempList) {
            if (!ev.getTheReader().isEntrance()) {
                return ev;
            }
        }
        return null;
    }

    public Process getLastProcess() {
        return m_javap[lastProcessId];
    }

    public void setLastProcess(Process p) {
        m_javap[lastProcessId] = p;
    }

    public int getLastProcessId() {
        return lastProcessId;
    }

    public Reader gerReader(String readerIP) {
        return readerList.get(readerIP);
    }
}
