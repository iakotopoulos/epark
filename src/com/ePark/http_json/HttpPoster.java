package com.ePark.http_json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;

    
/**
 * HttpPoster is class that undertakes http posting of epark events.
 * 
 * Incorporates methods for posting arrival, departure, as also questions on
 * availability updates.
 * Uses configuration settings from the Config class
 * @author Pantelis Karamolegkos
 * @see Config
 */

public class HttpPoster {

    private String url = "";
    private String urlParameters;
    private int responseCode = 0;
    private HttpURLConnection con = null;
    private final int connectTimeout = Config.connectTimeout;
    private final int readTimeout = Config.readTimeout;


    /**
     * Formats the url to make the http post by parsing the fields of the JSONObject passed in and the request type.
     * 
     * Example urls produced:
     * <li> for arrival: http://parking.itenasolutions.com:8080/json/reply/VehicleArrival?message_type=IN&version=1&parking_code=PK001&tag_identifier=1234567890&time_in=20130923210100&reader_code=123
     * <li> for departure: http://parking.itenasolutions.com:8080/json/reply/VehicleDeparture?message_type=OUT&version=1&parking_code=PK001&tag_identifier=1234567890&time_out=20130923210100&reader_code=123&tag_data=123&ticket_number=0
     * <li> for availability update: http://parking.itenasolutions.com:8080/json/reply/AvailabilityUpdate?version=1&parking_code=PK001&add_incoming=1&add_outgoing=0&reader_code=123
     * @param request_type Should be either "event" (from which point we distinguish on arrival or departure events based on the message_type field of the JSONObject
     * or "au" for availability updates
     * @param jsonIn The JSONObject used create the request
     * @throws MessageTypeException 
     * @see net.sf.json.JSONObject
     */
    private void formatUrl(String request_type, JSONObject jsonIn) throws MessageTypeException {

        StringBuilder paramsBuild = new StringBuilder();
        if (request_type.equals("event")) {
            switch (jsonIn.get("message_type").toString()) {
                case "IN":
                case "INOFFLINE":
                    paramsBuild.append("message_type=" + jsonIn.get("message_type"));
                    paramsBuild.append("&version=" + jsonIn.get("version"));
                    paramsBuild.append("&parking_code=" + jsonIn.get("parking_code"));
                    paramsBuild.append("&tag_identifier=" + jsonIn.get("tag_identifier"));
                    paramsBuild.append("&time_in=" + jsonIn.get("time_in"));
                    paramsBuild.append("&reader_code=" + jsonIn.get("reader_code"));
                    break;
                case "OUT":
                case "OUTOFFLINE":
                    paramsBuild.append("message_type=" + jsonIn.get("message_type"));
                    paramsBuild.append("&version=" + jsonIn.get("version"));
                    paramsBuild.append("&ticket_number=" + jsonIn.get("ticket_number"));
                    paramsBuild.append("&parking_code=" + jsonIn.get("parking_code"));
                    paramsBuild.append("&tag_data=" + jsonIn.get("tag_data"));
                    paramsBuild.append("&time_out=" + jsonIn.get("time_out"));
                    paramsBuild.append("&reader_code=" + jsonIn.get("reader_code"));
                    paramsBuild.append("&tag_identifier=" + jsonIn.get("tag_identifier"));
                    break;
                default:
                    throw new MessageTypeException("Malformed message type");

            }
        } else if (request_type.equals("au")) {
            // AvailabilityUpdate ws version from Config.java
            paramsBuild.append("version=" + Config.availabilityUpdateVersion);
            paramsBuild.append("&parking_code=" + jsonIn.getString("parking_code"));
            paramsBuild.append("&add_incoming=" + jsonIn.getString("add_incoming"));
            paramsBuild.append("&add_outgoing=" + jsonIn.getString("add_outgoing"));
            paramsBuild.append("&reader_code=" + jsonIn.getString("reader_code"));

        }
        urlParameters = paramsBuild.toString();
    }

    /**
     * This method is called by the public methods used to post the events to actually make the http post.
     * 
     * Calls formatUrl to retrieve the url that will be used to make the http post and uses java.net.HttpURLConnection for http communication with the main site.
     * Retrieves a JSONObject as a response and if the "response_code" field of the later is not equal to 1 (which indicates success) a ParkingException is thrown
     * 
     * 
     * @param request_type
     * @param jsonIn
     * @return
     * @throws java.net.SocketTimeoutException
     * @throws MessageTypeException
     * @throws java.net.MalformedURLException
     * @throws IOException
     * @throws ParkingException 
     */
    private JSONObject postEvent(String request_type, JSONObject jsonIn) throws java.net.SocketTimeoutException, MessageTypeException,
            java.net.MalformedURLException, IOException, ParkingException {

        formatUrl(request_type, jsonIn);
        url = Config.url;
        if (request_type.equals("event")) {
            switch (jsonIn.get("message_type").toString()) {
                case "IN":
                case "INOFFLINE":
                    url = url + Config.arrivalOperation;
                    break;
                case "OUT":
                case "OUTOFFLINE":
                    url = url + Config.departureOperation;
                    break;
                default:
                    throw new MessageTypeException("Malformed message type");
            }
        } else if (request_type.equals("au")) {
            url = url + Config.availabilityUpdate;
        }
        System.out.println("URL is: " + url);
        
        URL obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod("POST");
        con.setConnectTimeout(connectTimeout);
        con.setReadTimeout(readTimeout);
        //con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        responseCode = con.getResponseCode();

        System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in;
        if (responseCode == 200) {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        JSONObject jsonResponse = JSONObject.fromObject(response.toString());
        if (jsonResponse.getInt("response_code") != 1) {
            throw new ParkingException(jsonResponse.getString("response_message"));
        }
        return jsonResponse;
    }

    /**
     * The method used to post an arrival event to the main server.
     * 
     * Creates a JSONObject from the String fields passed in which is latter passed to the postEvent private method to make the actual http post.
     * 
     * @param message_type A String that should be either IN (for synchronous arrival postings) or INOFFLINE (for posting arrivals asynchronously when the server is offline)
     * @param version The Version of the communication protocol used between main site and parking sites - should be set to 1
     * @param parking_code Should be set to PK001 for Lydra's Parking / Nicosia and PK002 for Strovolou Parking
     * @param tag_identifier The ID of the tag used in the car entering the parking
     * @param tag_data Other data to pass to the server. Pass the User data written on the tag
     * @param time_in For message_type IN the server's time is used and the field is ignored. For message_type INOFFLINE we use this time expected yyyyMMddHHmmss  (24 hour basis)
     * @param reader_code Identifier of the reader used to detect the tag
     * @return Returns an ArrivalResponse object
     * @throws java.net.SocketTimeoutException
     * @throws MessageTypeException
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws ParkingException 
     * @see ArrivalResponse
     */
    public ArrivalResponse postArrival(String message_type, String version,
            String parking_code, String tag_identifier, String tag_data, String time_in, String reader_code) throws java.net.SocketTimeoutException,
            MessageTypeException, java.net.MalformedURLException, java.io.IOException, ParkingException {

        JSONObject jsonArrival = new JSONObject();
        jsonArrival.accumulate("message_type", message_type);
        jsonArrival.accumulate("version", version);
        jsonArrival.accumulate("parking_code", parking_code);
        jsonArrival.accumulate("tag_identifier", tag_identifier);
        jsonArrival.accumulate("tag_data", tag_data);
        jsonArrival.accumulate("time_in", time_in);
        jsonArrival.accumulate("reader_code", reader_code);

        JSONObject jsonResponse = postEvent("event", jsonArrival);
        return new ArrivalResponse(jsonResponse);

    }
    
    /**
     * The method used to post a departure event to the main server.
     * Creates a JSONObject from the String fields passed in which is latter passed to the postEvent private method to make the actual http post.
     * 
     * @param message_type A String that should be either OUT (for synchronous departure postings) or INOFFLINE (for posting departures asynchronously when the server is offline)
     * @param version The Version of the communication protocol used between main site and parking sites - should be set to 1
     * @param parking_code Should be set to PK001 for Lydra's Parking / Nicosia and PK002 for Strovolou Parking
     * @param tag_identifier The ID of the tag used in the car entering the parking
     * @param tag_data Other data to pass to the server. Pass the User data written on the tag
     * @param time_out For message_type OUT the server's time is used and the field is ignored. For message_type OUTOFFLINE we use this time expected yyyyMMddHHmmss  (24 hour basis)
     * @param reader_code Identifier of the reader used to detect the tag
     * @param ticket_number Unique Identifier of the transaction between main and remote sites
     * @return DepartureResponse
     * @throws java.net.SocketTimeoutException
     * @throws MessageTypeException
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws ParkingException 
     * @see Returns a DepartureReponse object
     */
    
    public DepartureResponse postDeparture(String message_type, String version,
            String parking_code, String tag_identifier, String tag_data, String time_out, String reader_code, String ticket_number) throws java.net.SocketTimeoutException,
            MessageTypeException, java.net.MalformedURLException, java.io.IOException, ParkingException {

        JSONObject jsonDeparture = new JSONObject();
        jsonDeparture.accumulate("message_type", message_type);
        jsonDeparture.accumulate("version", version);
        jsonDeparture.accumulate("parking_code", parking_code);
        jsonDeparture.accumulate("tag_identifier", tag_identifier);
        jsonDeparture.accumulate("tag_data", tag_data);
        jsonDeparture.accumulate("time_out", time_out);
        jsonDeparture.accumulate("reader_code", reader_code);
        jsonDeparture.accumulate("ticket_number", ticket_number);
        
        JSONObject jsonResponse = postEvent("event", jsonDeparture);
        return new DepartureResponse(jsonResponse);

    }
    
    /**
     * Method used to query for / update on available parking spots.
     * 
     * When add_incoming = add_outgoing = 0, just returns the number of available spots, otherwise it is used to 
     * update the main site on number of parking spots through those two parameters
     * 
     * @param parking_code Should be set to PK001 for Lydra's Parking / Nicosia and PK002 for Strovolou Parking
     * @param add_incoming The number of new arrivals when updating
     * @param add_outgoing The number of new departures when updating
     * @param reader_code Identifier of the reader used to detect the tag
     * @return Returns an AUReponse object
     * @throws java.net.SocketTimeoutException
     * @throws MessageTypeException
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws ParkingException 
     */
    public AUResponse postAvailabilityUpdate(String parking_code, String add_incoming, String add_outgoing, String reader_code) throws java.net.SocketTimeoutException,
            MessageTypeException, java.net.MalformedURLException, java.io.IOException, ParkingException {
        JSONObject jsonAvailabilityUpdate = new JSONObject();
        //jsonAvailabilityUpdate.accumulate("version", Config.availabilityUpdateVersion);
        jsonAvailabilityUpdate.accumulate("parking_code", parking_code);
        jsonAvailabilityUpdate.accumulate("add_incoming", add_incoming);
        jsonAvailabilityUpdate.accumulate("add_outgoing", add_outgoing);
        jsonAvailabilityUpdate.accumulate("reader_code", reader_code);
        
        JSONObject jsonResponse = postEvent("au", jsonAvailabilityUpdate);
        return new AUResponse(jsonResponse);

    }
}
