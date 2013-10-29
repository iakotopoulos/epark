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
 *
 * @author I-A
 */
public class ReaderProcess implements Runnable {

    private ReaderManager theManager;
    private String ip;

    public ReaderProcess(String ip, ReaderManager tm) {
        this.ip = ip;
        this.theManager = tm;
    }

    @Override
    public void run() {
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "EparkTask.jar", "-ip", ip);

        try {
            if (theManager.getLastProcessId() > ReaderManager.MAX_PROCESS) {
                System.out.println("Error: Exceed the maximum number of allowable process.");
                return;
            }
            theManager.setLastProcess(builder.start());
            writeProcessOutput(theManager.getLastProcess());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void writeProcessOutput(Process process) throws Exception {
        InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(process.getInputStream()));
        BufferedReader reader = new BufferedReader(tempReader);
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }            
            if (line.startsWith("event")) {
                StringTokenizer stok = new StringTokenizer(line, ";");
                //System.out.print("#" +  stok.nextElement());
                //System.out.println("|" +  stok.nextElement());
                theManager.newTagEvent(theManager.gerReader(ip), stok.nextElement().toString(), stok.nextElement().toString());
            }
        }
        System.out.println("Process terminated!");
    }
}
