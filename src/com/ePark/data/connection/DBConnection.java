package com.ePark.data.connection;

import com.ePark.data.io.AppConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for handling the connection with the local
 * database. It is common for all the other classes of the API
 *
 * @author ykoto
 */
public class DBConnection {

    private String ip;
    private String port;
    private String uname;
    private String password;
    private String dbase;

    /**
     * The constructor is used to initialize all the required parameters. The
     * values are read from the config.properties file.
     */
    public DBConnection() {

        this.ip = AppConfiguration.getProperty("db_host");
        this.port = AppConfiguration.getProperty("db_port");
        this.uname = AppConfiguration.getProperty("db_uname");
        this.password = AppConfiguration.getProperty("db_password");
        this.dbase = AppConfiguration.getProperty("db_base");

    }

    /**
     * Method used to get back a java.sql.Connection object for the specified
     * parameters
     *
     * @return a java.sql.Connection Object
     */
    public Connection getConnection() {
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

    /**
     * The method is used in order to create the database string needed for the
     * connection with the database
     *
     * @param ip the ip of the hosted mysql manager
     * @param port the port used by the manager
     * @param dbase the name of the database
     * @return a String Object with the constructed String for the Connection
     */
    private static String getMySQLDatabaseString(String ip, String port, String dbase) {
        String db = dbase + "?useUnicode=true&characterEncoding=UTF-8";
        String host = ip + ":" + port;
        return "jdbc:mysql://" + host + "/" + db;
    }
}
