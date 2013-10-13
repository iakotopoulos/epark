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

    public static boolean storeArrival(TagEvent ev) {
        String iQuery = "INSERT INTO arrivals(tagid, intime) VALUES(?, CURRENT_TIMESTAMP);";



        ResultSet rs = null;
        try (Connection con = new DBConnection().getConnection();
                PreparedStatement pst = con.prepareStatement(iQuery);) {
            
            pst.setString(1, ev.getTagid());
            
            if(pst.executeUpdate()>0)
                return true;
            return false;

        } catch (SQLException ex) {
            Logger.getLogger(EparkIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


    }
}
