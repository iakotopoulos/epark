/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
            ep.load(new FileInputStream(new java.io.File(".").getCanonicalPath() + "/config/config.properties"));
            conf.put("rfid_readers", ep.getProperty("rfid_readers"));
            conf.put("db_host", ep.getProperty("db_host"));
            conf.put("db_uname", ep.getProperty("db_uname"));
            conf.put("db_password", ep.getProperty("db_password"));
            conf.put("db_port", ep.getProperty("db_port"));
            conf.put("db_base", ep.getProperty("db_base"));

        } catch (IOException ex) {
            Logger.getLogger(AppConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conf;
    }

    public static ArrayList<String> getReaders() {
        ArrayList<String> iplist = new ArrayList<>();

        if (conf != null) {
            String ipString = conf.get("rfid_readers");
            StringTokenizer stok = new StringTokenizer(ipString, ";");
            while (stok.hasMoreTokens()) {
                iplist.add(stok.nextToken());
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
