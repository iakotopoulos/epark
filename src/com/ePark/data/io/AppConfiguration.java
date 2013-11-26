/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.io;

import com.ePark.local.EventManager;
import com.ePark.local.rfid.epark.local.rfid.data.Reader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ykoto
 */
public class AppConfiguration {

    private static HashMap<String, String> conf;

    public static HashMap<String, String> loadConfiguration() {
        conf = new HashMap<>();

        Properties ep = new Properties();

        try {
            //   ep.load(ReaderManagerP.class.getResourceAsStream("../../../../../config/config.properties"));
            
            //ep.load(EventManager.class.getResourceAsStream("/config/config.properties"));
            ep.load(new FileInputStream(new java.io.File(".").getCanonicalPath() + "/config/config.properties"));
            conf.put("rfid_readers_in", ep.getProperty("rfid_readers_in"));
            conf.put("rfid_readers_out", ep.getProperty("rfid_readers_out"));
            conf.put("db_host", ep.getProperty("db_host"));
            conf.put("db_uname", ep.getProperty("db_uname"));
            conf.put("db_password", ep.getProperty("db_password"));
            conf.put("db_port", ep.getProperty("db_port"));
            conf.put("db_base", ep.getProperty("db_base"));
            conf.put("serial_port", ep.getProperty("serial_port"));
            conf.put("parking_name", ep.getProperty("parking_name"));

        } catch (IOException ex) {
            Logger.getLogger(AppConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conf;
    }

    /*
     * Return the list of connected readers. The list is hashed on the reader ip
     */
    public static LinkedHashMap<String, Reader> getReaders() {
        LinkedHashMap<String, Reader> iplist = new LinkedHashMap<>();
    
   
        if (conf != null) {
            String ipString = conf.get("rfid_readers_in");
            StringTokenizer stok = new StringTokenizer(ipString, ";");
            while (stok.hasMoreTokens()) {
                String nip = stok.nextToken();
                iplist.put(nip, new Reader(nip, "in"));
            }            

            ipString = conf.get("rfid_readers_out");
            stok = new StringTokenizer(ipString, ";");
            while (stok.hasMoreTokens()) {
                String nip = stok.nextToken();
                iplist.put(nip, new Reader(nip, "out"));
            }

        } else {
            System.out.println("No configuration loaded");
        }

        return iplist;
    }
    
    public static String getProperty(String pname) {
        return conf.get(pname);
    }
}
