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

    public static boolean getCategories(TagEvent ev) {
        String iQuery = "SELECT cid, cname FROM category WHERE parentid=1;";



        ResultSet rs = null;
        try (Connection con = new DBConnection().getConnection();
                PreparedStatement pst = con.prepareStatement(iQuery);) {


            pst.executeQuery();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(EparkIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


    }
}
