/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ykoto
 */
public class DBUtil {

    public static void closeStatement(Statement s) {
        try {
            if (s != null) //Should be null in order not to close the Statement twice
            {
                s.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            s = null;
        }
    }

    public static void closePreparedStatement(PreparedStatement ps) {
        try {
            if (ps != null) //Should be null in order not to close the PStatement twice
            {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) //Should be null in order not to close the ResultSet twice
            {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
