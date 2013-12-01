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
            ArrivalResponse arrival3 = httpPost.postArrival("IN", "1", "PK001", "1234567893", "123", "20130923210100", "123");
            System.out.println("ARRIVAL 3: " + arrival3.toString());
            System.out.println("Ticket Number is: " + arrival3.getTicketNumber());
            System.out.println("Auth Amount is: " + arrival3.getAuthAmount());
            System.out.println("Card Name is: " + arrival3.getCardName());
            System.out.println("Card Balance is: " + arrival3.getCardBalance());
            System.out.println("Account Name is: " + arrival3.getAccountName());
            System.out.println("Account Balance is: " + arrival3.getAccountBalance());
            System.out.println("Car No is: " + arrival3.getCarNo());
            System.out.println("Display Message is: " + arrival3.getDisplayMessage());
            System.out.println("Response Code is: " + arrival3.getResponseCode());
            System.out.println("Response Message is: " + arrival3.getResponseMessage());
            System.out.println();
            // vehichle with tag_identifier 1234567893 arrives
            //JSONObject arrival2 = httpPost.postArrival("IN", 1, "PK001", 1234567893, "123", "20130923210100", 123);
            //System.out.println("ARRIVAL 2: " + arrival2.toString());
               
            /**
             *** TESTING DEPARTURES
             */
            /* DEPARTURES OF VEHICLES WITH TAG IDs 1234567892 and 1234567893 */
            // vehichle with tag_identifier 1234567893 departs
            DepartureResponse departure3 = httpPost.postDeparture("OUT", "1", "PK001", "1234567893", "123", "20130923210100", "123", "0");
            System.out.println("DEPARTURE 3: " + departure3.toString());
            System.out.println("Ticket Number is: " + departure3.getTicketNumber());
            System.out.println("Fee Amount is: " + departure3.getFeeAmount());
            System.out.println("Card Name is: " + departure3.getCardName());
            System.out.println("Card Balance is: " + departure3.getCardBalance());
            System.out.println("Account Name is: " + departure3.getAccountName());
            System.out.println("Account Balance is: " + departure3.getAccountBalance());
            System.out.println("Car No is: " + departure3.getCarNo());
            System.out.println("Duration (min) is: " + departure3.getDurationMinutes());
            System.out.println("Display Message is: " + departure3.getDisplayMessage());
            System.out.println("Response Code is: " + departure3.getResponseCode());
            System.out.println("Response Message is: " + departure3.getResponseMessage());
            System.out.println();
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
            
            AUResponse au_test = httpPost.postAvailabilityUpdate("PK001", "0", "10", "123");
            System.out.println(au_test.toString());
            System.out.println("Response Code is: " + au_test.getResponseCode());
            System.out.println("Response Message is: " + au_test.getResponseMessage());
            System.out.println("Availability is: " + au_test.getAvailability());
            System.out.println("Availability Disabled is: " + au_test.getAvailabilityDisabled());
            System.out.println();
            
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
