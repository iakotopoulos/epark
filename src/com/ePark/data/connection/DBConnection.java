/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.data.connection;

import com.ePark.data.io.AppConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ykoto
 */
public class DBConnection {

    // private Connection con=null;
    private String ip;
    private String port;
    private String uname;
    private String password;
    private String dbase;

    public DBConnection() {

        this.ip = AppConfiguration.getProperty("db_host");
        this.port = AppConfiguration.getProperty("db_port");
        this.uname = AppConfiguration.getProperty("db_uname");
        this.password = AppConfiguration.getProperty("db_password");
        this.dbase = AppConfiguration.getProperty("db_base");

    }

    public Connection getConnection()  {
        String database = "";
        try {

            Class.forName("com.mysql.jdbc.Driver");
            database = getMySQLDatabaseString(ip, port, dbase);


            return DriverManager.getConnection(database, uname, password);

        } catch (SQLException | ClassNotFoundException ex) {

            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String getMySQLDatabaseString(String ip, String port, String dbase) {
        String db = dbase + "?useUnicode=true&characterEncoding=UTF-8";
        String host = ip + ":" + port;
        return "jdbc:mysql://" + host + "/" + db;
    }
}
