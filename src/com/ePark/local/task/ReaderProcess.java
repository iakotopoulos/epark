/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.task;

import com.ePark.local.rfid.ReaderManagerP;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author I-A
 */
public class ReaderProcess implements Runnable {

    private String ip;

    public ReaderProcess(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "EparkTask.jar", "-ip", ip);

        try {
            if (ReaderManagerP.lastProcessId > ReaderManagerP.MAX_PROCESS) {
                System.out.println("Error: Exceed the maximum number of allowable process.");
                return;
            }
            ReaderManagerP.m_javap[ReaderManagerP.lastProcessId] = builder.start();
            writeProcessOutput(ReaderManagerP.m_javap[ReaderManagerP.lastProcessId]);
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
            System.out.println("Father " + line);
        }
        System.out.println("Process terminated!");
    }
}
