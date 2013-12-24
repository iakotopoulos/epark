/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * A data holder for all the data regarding a tag detection event
 *
 * @author I-A
 */
public class TagEvent implements Comparable {

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
     * Date formatter. It is important for the call of the web service
     *
     * @return a String with the formatted date
     */
    public String getEventStampString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(eventStamp);
    }

    /**
     * Update the timestamp and increase the counter
     *
     * @param eventStamp the timestamp of the event
     */
    public void setEventStamp(Timestamp eventStamp) {
        this.eventStamp = eventStamp;
        ecount++;
    }

    @Override
    public String toString() {
        return "tagid: " + tagid + ", last occurence " + eventStamp.toString() + ", (count " + ecount + ")";
    }

    public int getEcount() {
        return ecount;
    }

    /**
     *
     * @return IN for arrival event and OUT for departure
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Reader getTheReader() {
        return theReader;
    }

    /**
     * An override of the compareTo method in order to implement a timestamp
     * comparison. It is used for time based sorting of the events
     *
     * @param o actually a TagEvent to be compared
     * @return -1, 0, 1 for an event before, equal, or after the event in the
     * parameter
     */
    @Override
    public int compareTo(Object o) {
        return this.eventStamp.compareTo(((TagEvent) o).eventStamp);
    }
}
