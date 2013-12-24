/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.tasks;

import com.ePark.local.rfid.ReaderManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * This is a very important class used by the RFID {
 *
 * @see com.ePark.local.rfid.ReaderManager}. Instances of the class are needed
 * to start a separate thread for each reader device. It is also important as
 * you can notice in the run method that each reader device should be connected
 * in a different process.
 * @author I-A
 */
public class ReaderProcess implements Runnable {

    private ReaderManager readerManager;
    private String ip;

    /**
     * The constructor of the ReaderProcess. It is needed as a connection with
     * the main program that is responsible for invoking the threads.
     *
     * @param ip the IP of the reader device
     * @param tm an instance of the {@link com.ePark.local.rfid.ReaderManager}
     * that asked for the creation of the process
     */
    public ReaderProcess(String ip, ReaderManager tm) {
        this.ip = ip;
        this.readerManager = tm;
    }

    /**
     * IMPORTANT NOTICE: The thread, once started is responsible for invoking a
     * new process in the local system. This proved to be the only way to allow
     * multiple connections with the readers as separate threads share the same
     * resources and this caused hardware conflicts in the system's hardware
     * management.
     *
     */
    @Override
    public void run() {
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "EparkTask.jar", "-ip", ip);

        try {
            if (readerManager.getLastProcessId() > ReaderManager.MAX_PROCESS) {
                System.out.println("Error: Exceed the maximum number of allowable process.");
                return;
            }
            readerManager.setLastProcess(builder.start());
            writeProcessOutput(readerManager.getLastProcess());
        } catch (Exception ex) {
            System.out.println(ip + ex.getMessage());
        }

    }

    /**
     * This method uses a loop and acts as a listener for the started process.
     * They both use the System.out for interprocess communication. Output send
     * by the processes is parsed in order to identify the triggering event. A
     * new {@link com.ePark.local.rfid.data.TagEvent} is created and passed to
     * the {@link com.ePark.local.rfid.ReaderManager}. The method is also used
     * in order to terminate the process on system shutdown
     *
     * @param process the father process connected with this call.
     * @throws Exception an exception is declared to be thrown on an error while
     * reading from the input buffer
     */
    private void writeProcessOutput(Process process) throws Exception {
        InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(process.getInputStream()));
        BufferedReader reader = new BufferedReader(tempReader);
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            //This line prints everytime a tag is read. Use it only for testing | System.out.println(line);
            if (line.startsWith("event")) {
                StringTokenizer stok = new StringTokenizer(line, ";");
                //System.out.print("#" +  stok.nextElement());
                //System.out.println("|" +  stok.nextElement());
                stok.nextElement();
                readerManager.newTagEvent(readerManager.gerReader(ip), stok.nextElement().toString(), stok.nextElement().toString());

            }
        }
        System.out.println("Process terminated!");
    }
}
