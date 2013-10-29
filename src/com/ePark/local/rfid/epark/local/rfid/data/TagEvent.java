/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid.epark.local.rfid.data;

import java.sql.Timestamp;

/**
 *
 * @author I-A
 */
public class TagEvent {

    private Reader theReader;
    private String tagid;
    private Timestamp eventStamp;
    private int ecount;
    private String type; //IN|OUT

    public TagEvent(String tagid, Timestamp eventStamp, Reader r) {
        this.tagid = tagid;
        this.eventStamp = eventStamp;
        theReader = r;
        ecount = 1;
    }

    public TagEvent(String tagid, Timestamp eventStamp, String type) {
        this.tagid = tagid;
        this.eventStamp = eventStamp;
        ecount = 1;
        this.type = type;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public Timestamp getEventStamp() {
        return eventStamp;
    }

    /**
     * Update the timestamp and increase the counter
     * @param eventStamp the timestamp of the event
     */
    public void setEventStamp(Timestamp eventStamp) {
        this.eventStamp = eventStamp;
        ecount++;
    }

    public String toString() {
        return "tagid: " + tagid + ", last occurence " + eventStamp.toString() + ", (count " + ecount + ")";
    }

    public int getEcount() {
        return ecount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Reader getTheReader() {
        return theReader;
    }
    
    
}
