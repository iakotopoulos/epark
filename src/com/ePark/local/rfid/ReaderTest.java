/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a low level connection tester class. It was created in order to test the 
 * direct connection with a reader with the use of a socket. The high level API is recommended
 * @author I-A
 */
public class ReaderTest {

    public static void main(String[] args) {
        Socket TCPCtrlSocket, TCPDataSocket = null;
        DataOutputStream TCPCtrlOut, TCPDataOut = null;
        DataInputStream TCPCtrlIn, TCPDataIn = null;
        DatagramSocket clientSocket = null;
        DatagramPacket sendPacket = null;
        DatagramPacket receivePacket = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        Scanner in;
        String IPAddress, cmdString;
        int power;
        InetAddress IPInet;
        boolean inventoryStopped;
        int port = 0;
        byte[] receiveData;
        byte[] inData = new byte[1024];
        int len = 0;


        try {
            //Read and set IP address
            System.out.printf("Please enter the IP address of CS203: ");
            in = new Scanner(System.in);
            IPAddress = in.nextLine();
            IPInet = InetAddress.getByName(IPAddress);

            //Set power
            power = 300;

            //Set port
            port = 0; //1515

            //open cport (1516)
            TCPCtrlSocket = new Socket(IPAddress, 1516);
            TCPCtrlOut = new DataOutputStream(TCPCtrlSocket.getOutputStream());
            TCPCtrlIn = new DataInputStream(new BufferedInputStream(TCPCtrlSocket.getInputStream()));

            //open iport (1515)
            TCPDataSocket = new Socket(IPAddress, 1515);
            TCPDataOut = new DataOutputStream(TCPDataSocket.getOutputStream());
            TCPDataIn = new DataInputStream(new BufferedInputStream(TCPDataSocket.getInputStream()));
            //open uport (3041)
            clientSocket = new DatagramSocket();

           
            //open cport (1516)
            TCPCtrlSocket = new Socket(IPAddress, 1516);
            TCPCtrlOut = new DataOutputStream(TCPCtrlSocket.getOutputStream());
            TCPCtrlIn = new DataInputStream(new BufferedInputStream(TCPCtrlSocket.getInputStream()));
            //open iport (1515)
            TCPDataSocket = new Socket(IPAddress, 1515);
            TCPDataOut = new DataOutputStream(TCPDataSocket.getOutputStream());
            TCPDataIn = new DataInputStream(new BufferedInputStream(TCPDataSocket.getInputStream()));
            //open uport (3041)
            clientSocket = new DatagramSocket();

            //#################################################################################
            // Power up RFID module
            TCPCtrlOut.write(hexStringToByteArray("80000001"));

            while (true) {
                if (TCPCtrlIn.available() != 0) {
                    len = TCPCtrlIn.read(inData);
                    break;
                }
            }
            
            Thread.sleep(2000);

            //Enable TCP Notifications
            TCPCtrlOut.write(hexStringToByteArray("8000011701"));

            while (true) {
                if (TCPCtrlIn.available() != 0) {
                    len = TCPCtrlIn.read(inData);
                    break;
                }
            }
            
            //Send Abort command
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("4003000000000000"));

            while (true) {
                if (TCPDataIn.available() != 0) {
                    len = TCPDataIn.read(inData);
                    break;
                }
            }
            

            ///////////////////////////////////////////////////////////////////////

            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("7001010700000000"));
            System.out.println("Send ANT_PORT_SEL command 0x7001010700000000");
            Thread.sleep(100);

            //Select RF power 30dBm
            cmdString = String.format("70010607%02X%02X0000", power & 0xFF, ((power >> 8) & 0xFF));
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray(cmdString));

            Thread.sleep(100);
            //Set Link Profile 2
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("7001600b02000000"));

            Thread.sleep(100);
            //HST Command
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("700100f019000000"));

            Thread.sleep(100);
            //QUERY_CFG Command for continuous inventory
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("70010007ffff0000"));

            Thread.sleep(100);
            //Set DynamicQ algorithm (INV_SEL)
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("7001020903000000"));

            Thread.sleep(100);
            //Send INV_CFG
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("7001010903000000"));

            Thread.sleep(100);
            //Set dwell time
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("7001050700000000"));

            //Start inventory - send (HST_CMD)
            inventoryStopped = false;
            long timer = System.currentTimeMillis();
            clearReadBuffer(TCPDataIn);
            TCPDataOut.write(hexStringToByteArray("700100f00f000000"));
            System.out.println("Start inventory - send (HST_CMD) 700100f00f000000");
            //System.out.println("Press any key to stop...");
            while (true) {
                if (TCPDataIn.available() != 0) {
                    timer = System.currentTimeMillis();
                    //keypress detected
                    if (System.in.available() != 0 && !inventoryStopped) {
                        // Stop Inventory
                        TCPDataOut.write(hexStringToByteArray("4003000000000000"));
                        System.out.println("Abort Inventory command 0x4003000000000000");
                        inventoryStopped = true;
                    }

                    //get packet header first
                    len = TCPDataIn.read(inData, 0, 8);

                    if (len < 8) {
                        continue;
                    }

                    if (byteArrayToHexString(inData, len).startsWith("9898989898989898")) {
                        //clearReadBuffer(TCPDataIn);
                        date = new Date();
                        System.out.println(dateFormat.format(date) + " Connection is alive.");
                        continue;
                    } else if (byteArrayToHexString(inData, len).startsWith("02000780")) {
                        //clearReadBuffer(TCPDataIn);
                        date = new Date();
                        System.out.println(dateFormat.format(date) + " Antenna Cycle End Notification Received");
                        continue;
                    } else if (byteArrayToHexString(inData, len).startsWith("4003BFFCBFFCBFFC")) {
                        // Check Abort command response
                        System.out.println("All tag data has been returned");
                        break;
                    }


                    int pkt_ver = (int) (inData[0] & 0xFF);
                    int flags = (int) (inData[1] & 0xFF);
                    int pkt_type = (int) (inData[2] & 0xFF) + ((int) (inData[3] & 0xFF) << 8);
                    int pkt_len = (int) (inData[4] & 0xFF) + ((int) (inData[5] & 0xFF) << 8);
                    int datalen = pkt_len * 4;

                    if (pkt_ver != 0x01 && pkt_ver != 0x02) {
                        System.out.println("Unrecognized packet header: " + byteArrayToHexString(inData, len));
                        continue;
                    }

                    //wait until the full packet data has come in
                    while (TCPDataIn.available() < datalen) {
                    }
                    //finish reading
                    TCPDataIn.read(inData, 8, datalen);

                    if (pkt_type == 0x8001) {
                        date = new Date();
                        System.out.println(dateFormat.format(date) + " Command End Packet: " + byteArrayToHexString(inData, len + datalen));
                        continue;
                    }
                    if (pkt_type == 0x8000) {
                        date = new Date();
                        System.out.println(dateFormat.format(date) + " Command Begin Packet: " + byteArrayToHexString(inData, len + datalen));
                        continue;
                    }
                    if (pkt_type == 0x8005) {
                        byte[] EPC = new byte[64];
                        for (int cnt = 0; cnt < (datalen - 16); cnt++) {
                            EPC[cnt] = inData[22 + cnt];
                        }
                        date = new Date();
                        //System.out.println(dateFormat.format(date) + " Inventory Packet: " + byteArrayToHexString(inData,len+datalen));
                        cmdString = String.format("Tag Inventory Packet: EPC=%s, Antenna Port=%d, RSSI=%3.2f", byteArrayToHexString(EPC, datalen - 16), inData[18], inData[13] * 0.8);
                        System.out.println("Recognized tag: tagid=" + byteArrayToHexString(EPC, datalen - 16) + ", timestamp=" + date.toString());
                        //System.out.println(cmdString);
                        continue;
                    }
                } else {
                    if (System.currentTimeMillis() - timer >= 6000) {
                        System.out.println("Connection lost.  Please reconnect");
                        System.out.println("Close Connections");

                        TCPCtrlSocket.close();
                        TCPDataSocket.close();
                        clientSocket.close();
                    }
                }
            }

            //M WHILE _____________________________________

            clearReadBuffer(TCPDataIn);
            //Go back to high-level API mode
          /* cmd="80" + byteArrayToHexString(IPInet.getAddress(),IPInet.getAddress().length) + "010D";           
             sendPacket = new DatagramPacket(hexStringToByteArray(cmd), hexStringToByteArray(cmd).length,IPInet, 3041);
             receiveData = new byte[4];
             receivePacket = new DatagramPacket(receiveData, receiveData.length);
             clientSocket.send(sendPacket);
             clientSocket.receive(receivePacket);            */
            System.out.println("Close Connections");
            Thread.sleep(3000);
            TCPCtrlSocket.close();
            TCPDataSocket.close();
            clientSocket.close();



        } catch (UnknownHostException ex) {
            Logger.getLogger(ReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String charArrayToHexString(char[] a, int length) {
        String returnString = "";
        for (int j = 0; j < length; j++) {
            byte c = (byte) a[j];
            int uc = (int) (c & 0xFF);
            if (Integer.toHexString(uc).length() == 1) {
                returnString += "0";
            }
            returnString += Integer.toHexString(uc);
        }
        returnString = returnString.toUpperCase();
        return returnString;
    }

    public static String byteArrayToHexString(byte[] a, int length) {
        String returnString = "";
        for (int j = 0; j < length; j++) {
            int uc = (int) (a[j] & 0xFF);
            if (Integer.toHexString(uc).length() == 1) {
                returnString += "0";
            }
            returnString += Integer.toHexString(uc);
        }
        returnString = returnString.toUpperCase();
        return returnString;
    }

    static void clearReadBuffer(DataInputStream data) {
        byte[] inData = new byte[1024];
        int len;
        try {
            while (data.available() != 0) {
                len = data.read(inData);
            }
        } catch (IOException e) {
            System.out.println("Could not clear buffer: " + e.getMessage());
        }
    }
}
