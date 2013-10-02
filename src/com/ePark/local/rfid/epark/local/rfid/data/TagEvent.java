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

    private String tagid;
    private Timestamp eventStamp;
    private int ecount;

    public TagEvent(String tagid, Timestamp eventStamp) {
        this.tagid = tagid;
        this.eventStamp = eventStamp;
        ecount = 1;
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

    public void setEventStamp(Timestamp eventStamp) {
        this.eventStamp = eventStamp;
        ecount++;
    }

    public String toString() {
        return "tagid: " + tagid + ", last occurence " + eventStamp.toString() + ", (count "+ ecount +")";
    }

    public int getEcount() {
        return ecount;
    }
}
