/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local.rfid;

import CSLibrary.Constants.CallbackType;
import CSLibrary.Events.AsyncCallbackEventArgs;
import CSLibrary.Events.AsyncCallbackEventListener;
import CSLibrary.Structures.TagCallbackInfo;

/**
 *
 * @author I-A
 */
public class ReaderCallbackListener implements AsyncCallbackEventListener {

    private final ReaderManager the_Manager;

    public ReaderCallbackListener(ReaderManager rmgr) {
        the_Manager = rmgr;
    }

    @Override
    public void AsyncCallbackEvent(AsyncCallbackEventArgs ev) {

        if (ev.type == CallbackType.TAG_RANGING) {
            TagCallbackInfo record = ev.info;
           // Object[] entry = new Object[]{new Integer(record.index), record.pc.ToString(), record.epc.ToString(), new Float(record.rssi), new Integer(record.count)};
           // System.out.println("TagID " + record.epc.ToString());
            
            the_Manager.newTagEvent(record.epc.ToString());

        }
    }
}
