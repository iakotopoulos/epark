/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 *
 * @author pkaramol
 */
public class TestResponseMain {

    private static HttpPoster httpPost;

    public static void main(String[] args) {

        try {

            httpPost = new HttpPoster();


            /**
             *** TESTING ARRIVALS
             */
            // vehichle with tag_identifier 1234567891 arrives
            JSONObject arrival0 = httpPost.postArrival("IN", "1", "PK001", "1234567890", "123", "20130923210100", "123");
            System.out.println("ARRIVAL 0: " + arrival0.toString());
            // vehichle with tag_identifier 1234567893 arrives
            //JSONObject arrival2 = httpPost.postArrival("IN", 1, "PK001", 1234567893, "123", "20130923210100", 123);
            //System.out.println("ARRIVAL 2: " + arrival2.toString());
               
            /**
             *** TESTING DEPARTURES
             */
            /* DEPARTURES OF VEHICLES WITH TAG IDs 1234567892 and 1234567893 */
            // vehichle with tag_identifier 1234567893 departs
            JSONObject departure0 = httpPost.postDeparture("OUT", "1", "PK001", "1234567890", "123", "20130923210100", "123", "0");
            System.out.println("DEPARTURE 0: " + departure0.toString());
            // vehichle with tag_identifier 1234567890 departs
            //JSONObject departure3 = httpPost.postDeparture("OUT", "1", "PK001", "1234567893", "123", "20130923210100", "123", "0");
            //System.out.println("DEPARTURE 3: " + departure3.toString());
            //System.out.println(departure3.get("response_message"));

            /**
             *** OFFLINE MESSAGE TESTING *****
             */
            /* simulating offline message for arrival of 1234567890 */
            //JSONObject off_arrival = httpPost.postArrival("INOFFLINE", 1, "PK001", 1234567892, "123", "20130923210100", 123);
            //System.out.println("OFFLINE ARRIVAL: " + off_arrival.toString());
            
            /**
             * ** AVAILABILITY UPDATE TESTING *****
             * if add_incoming = add_outgoing = 0, it just returns the number of available parking spots
             */
            
            JSONObject au_test = httpPost.postAvailabilityUpdate("PK001", "0", "10", "123");
            System.out.println(au_test.toString());
            
        } catch (SocketTimeoutException ex) {
            Logger.getLogger(TestResponseMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessageTypeException ex) {
            Logger.getLogger(TestResponseMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TestResponseMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestResponseMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParkingException ex) {
            System.out.println("Parking Exception: " + ex);
        }
    }
}
