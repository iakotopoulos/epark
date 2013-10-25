/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 *
 * @author pkaramol
 */
public class TestResponseMain {

    private static HttpPoster httpPost;

    public static void main(String[] args) {

        httpPost = new HttpPoster();
        JSONObject jsonArrival_1 = new JSONObject();
        JSONObject jsonArrival_2 = new JSONObject();
        JSONObject jsonDeparture_1 = new JSONObject();
        JSONObject jsonDeparture_2 = new JSONObject();

        // car wih tag id 1234567890 comes in
        jsonArrival_1.accumulate("message_type", "IN");
        jsonArrival_1.accumulate("version", 1);
        jsonArrival_1.accumulate("parking_code", "PK001");
        jsonArrival_1.accumulate("tag_identifier", 1234567890);
        jsonArrival_1.accumulate("time_in", "20130923210100");
        jsonArrival_1.accumulate("reader_code", 123);

        // car wih tag id 1234567891 comes in
        jsonArrival_2.accumulate("message_type", "IN");
        jsonArrival_2.accumulate("version", 1);
        jsonArrival_2.accumulate("parking_code", "PK001");
        jsonArrival_2.accumulate("tag_identifier", 1234567891);
        jsonArrival_2.accumulate("time_in", "20130923210100");
        jsonArrival_2.accumulate("reader_code", 123);

        // car with tag id 1234567891 departs
        jsonDeparture_2.accumulate("message_type", "OUT");
        jsonDeparture_2.accumulate("version", 1);
        jsonDeparture_2.accumulate("ticket_number", 0);
        jsonDeparture_2.accumulate("parking_code", "PK001");
        jsonDeparture_2.accumulate("tag_data", 123);
        jsonDeparture_2.accumulate("time_out", "20130923210100");
        jsonDeparture_2.accumulate("reader_code", 123);
        jsonDeparture_2.accumulate("tag_identifier", 1234567891);

        // car with tag id 1234567890 departs
        jsonDeparture_1.accumulate("message_type", "OUT");
        jsonDeparture_1.accumulate("version", 1);
        jsonDeparture_1.accumulate("ticket_number", 0);
        jsonDeparture_1.accumulate("parking_code", "PK001");
        jsonDeparture_1.accumulate("tag_data", 123);
        jsonDeparture_1.accumulate("time_out", "20130923210100");
        jsonDeparture_1.accumulate("reader_code", 123);
        jsonDeparture_1.accumulate("tag_identifier", 1234567890);


        try {

            JSONObject jsonResponseArrival_1 = httpPost.postEvent(jsonArrival_1);
            System.out.println("Json Arrival Response: " + jsonResponseArrival_1.toString());
            JSONObject jsonResponseArrival_2 = httpPost.postEvent(jsonArrival_2);
            System.out.println("Json Arrival Response: " + jsonResponseArrival_2.toString());
            JSONObject jsonResponseDeparture_2 = httpPost.postEvent(jsonDeparture_2);
            System.out.println("Json Departure Response: " + jsonResponseDeparture_2.toString());

            JSONObject jsonResponseDeparture_1 = httpPost.postEvent(jsonDeparture_1);
            System.out.println("Json Departure Response: " + jsonResponseDeparture_1.toString());
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error in submitting event");
        }

    }
}
