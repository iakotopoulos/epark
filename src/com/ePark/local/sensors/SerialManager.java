/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.sensors;

import com.ePark.local.events.DeviceListener;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 *
 * @author I-A
 */
public class SerialManager implements SerialPortEventListener {

    SerialPort serialPort;
    private ArrayList<DeviceListener> listeners;
    private int packetCheck;
    /**
     * The port name examples... Replaced this with a parameter in order to be
     * easily configured
     */
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyUSB0", // Linux
        "COM13", // Windows
    };
    private String port_name;
    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 38400;

    public SerialManager(String port) {
        port_name = port;
        PORT_NAMES[2] = port_name;
        listeners = new ArrayList<>();
    }

    public void Start() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            serialPort.enableReceiveTimeout(1000);
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                //System.out.println(inputLine);
                String pos = null;
                if (inputLine != null && inputLine.indexOf("EXIT") > 0) {
                    pos = "EX";
                    System.out.println("Exit");
                } else if (inputLine != null && inputLine.indexOf("ENTRANCE") > 0) {
                    pos = "IN";
                    System.out.println("Entrance");
                }/*else if(inputLine.indexOf("Entered")>0){
                 System.out.println("communicating");
                 }&& EX1*/

                notifyListeners(pos);
                
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public void addListener(DeviceListener toAdd) {
        listeners.add(toAdd);
    }

    private void notifyListeners(String pos) {
        for (DeviceListener dl : listeners) {
            dl.waspNotification(pos);
        }
    }

    public static void main(String[] args) throws Exception {
        /*    SerialTest main = new SerialTest();
         main.Start();
         /*    Thread t = new Thread() {
         public void run() {
         //the following line will keep this app alive for 1000 seconds,
         //waiting for events to occur and responding to them (printing incoming messages to console).
         try {
         Thread.sleep(100000);
         } catch (InterruptedException ie) {
         }
         }
         };
         t.start();*/
        System.out.println("Started");
    }
}
