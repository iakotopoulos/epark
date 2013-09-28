/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epark.local.rfid;

import CSLibrary.Constants.CallbackType;
import CSLibrary.Events.AsyncCallbackEventArgs;
import CSLibrary.Events.AsyncCallbackEventListener;
import CSLibrary.Structures.TagCallbackInfo;
import CSLibrary.Tools.Sound;
import java.util.ArrayList;
import java.util.Collections;

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
        int m_totaltag = 0;
        int m_tagCount = 0;
        ArrayList InventoryListItems = new ArrayList();

        if (ev.type == CallbackType.TAG_RANGING) {
            TagCallbackInfo record = ev.info;

            ++m_totaltag;

            int index = Collections.binarySearch(InventoryListItems, ev.info);
            if (index > -1) {
                //found a record
                TagCallbackInfo found = ((TagCallbackInfo) InventoryListItems.get(index));
                ++found.count;
                found.rssi = record.rssi;
                /*
                 int rows = inventoryTableModel.getRowCount();
                 for (int i = 0; i < rows; i++) {
                 String value = inventoryTableModel.getValueAt(i, 0).toString();
                 if (value.equals(Integer.toString(found.index))) {
                 table_inventory.setValueAt(new Float(found.rssi), i, 3);
                 table_inventory.setValueAt(new Integer(found.count), i, 4);
                 }
                 }*/
            } else {
                m_tagCount = record.index = InventoryListItems.size();
                InventoryListItems.add(record);
                Object[] entry = new Object[]{new Integer(record.index), record.pc.ToString(), record.epc.ToString(), new Float(record.rssi), new Integer(record.count)};
                System.out.println("TagID " + record.epc.ToString());
                //inventoryTableModel.addRow(entry);
                Collections.sort(InventoryListItems);                
            }
        }
    }
}
