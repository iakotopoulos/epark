/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package epark.local.rfid;

import CSLibrary.Constants.CallbackType;
import CSLibrary.Constants.Operation;
import CSLibrary.Constants.RFState;
import CSLibrary.Constants.RadioOperationMode;
import CSLibrary.Constants.Result;
import CSLibrary.Constants.SelectFlags;
import CSLibrary.Events.AsyncCallbackEventArgs;
import CSLibrary.Events.AsyncCallbackEventListener;
import CSLibrary.Events.StateChangedEventArgs;
import CSLibrary.Events.StateChangedEventListener;
import CSLibrary.HighLevelInterface;
import CSLibrary.Structures.TagCallbackInfo;
import CSLibrary.Tools.Sound;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author I-A
 */
public class ReaderHighLevelTest {

    static HighLevelInterface ReaderXP = new HighLevelInterface();
    private static Thread reset;
    private static java.util.Timer timer = null;

    public static void main(String[] args) {


        System.out.printf("Please enter the IP address of CS203: ");
        Scanner in = new Scanner(System.in);


        int r = ReaderXP.Connect(in.nextLine().length() < 2 ? "192.168.25.203" : in.nextLine(), 15000);

        System.out.println("Connection response: " + r);
        if (r != CSLibrary.Constants.Result.OK) {
            System.out.println("RFID Connection Fail");
            ReaderXP.Disconnect();
            System.exit(0);
        } else {
            System.out.println("Connection Established");
        }


        ReaderXP.addStateChangedEventListener(new StateChangedEventListener() {
            @Override
            public void StateChangedEvent(StateChangedEventArgs ev) {
                switch (ev.state) {
                    case RFState.IDLE:

                        //Check whether fail
                        if (ReaderXP.LastResultCode() == Result.NETWORK_RESET) {
                            //Use other thread to create progress
                            reset = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if ((ReaderXP.Reconnect()) == Result.OK) {
                                        //Start inventory
                                        Start();
                                    } else {
                                        //Fail
                                    }
                                }
                            });
                            reset.start();
                        }
                        break;
                    case RFState.BUSY:
                        break;
                    case RFState.STOPPING:
                        break;
                    case RFState.NOT_INITIALIZED:
                        break;
                }
            }
        });

        ReaderXP.addAsyncCallbackEventListener(new AsyncCallbackEventListener() {
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
                        Sound.Beep(2000, 10);
                    }
                }
            }
        });

        Start();

    }

    private static void Start() {
        if (ReaderXP.GetState() == RFState.IDLE) {
            ReaderXP.SetOperationMode(Settings.custInventory_continuous ? RadioOperationMode.CONTINUOUS : RadioOperationMode.NONCONTINUOUS);
            ReaderXP.SetTagGroup(Settings.tagGroup);
            ReaderXP.SetSingulationAlgorithmParms(Settings.singulation, Settings.GetSingulationAlg());

            ReaderXP.GetOptions().TagInventory.flags = SelectFlags.ZERO;
            ReaderXP.StartOperation(Operation.TAG_RANGING, Settings.custInventory_blocking_mode);
        }
    }
}
