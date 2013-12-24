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
 * This is an important class used for handling communication with the serial
 * port. It is directly used for handling input from the waspmote gateway.
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
     * IMPORTANT: this method is part of the business logic as it is responsible
     * for parsing the data passed from the waspmote sensors. For this purpose
     * it must be aware of the specific hardcoded strings included in the passed
     * data from the waspmotes.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                //System.out.println(inputLine );
                String pos = null;
                if (inputLine != null && inputLine.indexOf("EXIT13") > 0) {
                    pos = "EX";
                    System.out.println("Exit");
                } else if (inputLine != null && inputLine.indexOf("ENTRANCE13") > 0) {
                    pos = "IN";
                    System.out.println("Entrance");
                }/*else if(inputLine.indexOf("Entered")>0){
                 System.out.println("communicating");
                 }&& EX1*/

                notifyListeners(pos);

            } catch (Exception e) {
                System.out.println("my - " + e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    /**
     * This is necessary for adding a listener to the class. It is crucial as it
     * is the only way to make the connection with the
     * {@link com.ePark.local.EventManager} which acts as the listener of all
     * the events in the local system
     *
     * @param toAdd the calls acting as the listener. Here it is the main
     * {@link com.ePark.local.EventManager} Object
     */
    public void addListener(DeviceListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Invoke the listeners on an event. (Only one listener here but it is
     * implemented in a more generalized way)
     *
     * @param pos indicates if the event was triggered by an arrival or a
     * departure
     */
    private void notifyListeners(String pos) {
        for (DeviceListener dl : listeners) {
            dl.waspNotification(pos);
        }
    }

    /**
     * It was used only for testing purposes
     * @param args ..
     * @throws Exception ..
     */
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
