/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.io;

import com.ePark.data.connection.DBConnection;
import com.ePark.local.rfid.data.TagEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a utility class with a number of static methods used for the update
 * of the database
 *
 * @author I-A
 */
public class EparkIO {

    /**
     * The method is used for storing an arrival event to the local database. It
     * is used by the EventManager.
     *
     * @param ev the TagEvent with all the needed information for the arrival
     * @param isSend it is used to indicate if the event was successfully
     * transmitted to the central server. True if the event was successfully
     * transmitted and false otherwise.
     * @return true if the update of the database was successful, false
     * otherwise
     */
    public static boolean storeArrival(TagEvent ev, boolean isSend) {
        String iQuery = "INSERT INTO arrivals(tagid, intime, send) VALUES(?, ?, ?);";



        ResultSet rs = null;
        try (Connection con = new DBConnection().getConnection();
                PreparedStatement pst = con.prepareStatement(iQuery);) {

            pst.setString(1, ev.getTagid());
            pst.setTimestamp(2, ev.getEventStamp());
            pst.setString(3, (isSend) ? "yes" : "no");

            if (pst.executeUpdate() > 0) {
                return true;
            }
            return false;

        } catch (SQLException ex) {
            Logger.getLogger(EparkIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * The method is used for storing a departure event to the local database.
     * It is used by the EventManager and is responsible for updating both the
     * arrivals and the departures table
     *
     * @param ev the TagEvent with all the needed information abou the event
     * @param isSend it is used to indicate if the event was successfully
     * @param fee the calculated fee for the specific tag and duration. The
     * amount is returned by the central system. In case of any failure during
     * the connection with the central system the fee is set to -1.
     * @return true if the update of the database was successful, false
     * otherwise
     */
    public static boolean storeCompletion(TagEvent ev, boolean isSend, Double fee) {

        String ic = "Insert into completed(tagid, intime, charge, send) select tagid, intime, ?, ? from arrivals WHERE tagid=?;";
        String dc = "DELETE FROM arrivals WHERE tagid=?";



        ResultSet rs = null;
        try (Connection con = new DBConnection().getConnection()) {

            try (
                    PreparedStatement pst = con.prepareStatement(ic);
                    PreparedStatement pst1 = con.prepareStatement(dc);) {
                con.setAutoCommit(false);

                pst.setDouble(1, fee);
                pst.setString(2, (isSend) ? "yes" : "no");
                pst.setString(3, ev.getTagid());

                //Insert into completed
                pst.executeUpdate();

                pst1.setString(1, ev.getTagid());
                //Empty arrivals
                pst1.executeUpdate();

                con.commit();
                return true;

            } catch (SQLException ex) {
                con.rollback();
                con.setAutoCommit(true);
                Logger.getLogger(EparkIO.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EparkIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
