/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.io;

import com.ePark.data.connection.DBConnection;
import com.ePark.local.rfid.epark.local.rfid.data.TagEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I-A
 */
public class EparkIO {

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

    public static boolean storeCompletion(TagEvent ev, boolean isSend, float fee) {

        String ic = "Insert into completed(tagid, intime, charge, send) select tagid, intime, ?, ? from arrivals WHERE tagid=?;";
        String dc = "DELETE FROM arrivals WHERE tagid=?";



        ResultSet rs = null;
        try (Connection con = new DBConnection().getConnection()) {

            try (
                    PreparedStatement pst = con.prepareStatement(ic);
                    PreparedStatement pst1 = con.prepareStatement(dc);) {
                con.setAutoCommit(false);

                pst.setFloat(1, fee);
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
