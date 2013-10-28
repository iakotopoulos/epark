/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid.epark.local.rfid.data;

/**
 *
 * @author I-A
 */
public class Reader {

    private String ip;
    private String location;
    private String role; //in|out

    public Reader(String ip, String role) {
        this.ip = ip;
        location = "";
        this.role = role;

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
