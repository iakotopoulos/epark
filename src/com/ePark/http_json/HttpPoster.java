/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

//import net.sf.json.JSONException;
/**
 *
 * @author pantelis
 */
public class HttpPoster {


    private String url = "";
    private String urlParameters;
    private int responseCode = 0;
    private HttpURLConnection con = null;
    private int connectTimeout = Config.connectTimeout;
    private int readTimeout = Config.readTimeout;



    public HttpPoster() {
        System.out.println("Creating HttpPoster object");
        System.out.println("Timeout is: " + connectTimeout);
    }


    private void formatUrl(JSONObject jsonIn) throws MessageTypeException  {

        StringBuilder paramsBuild = new StringBuilder();
        switch (jsonIn.get("message_type").toString()) {
            case "IN":
                paramsBuild.append("message_type=IN");
                paramsBuild.append("&version=" + jsonIn.get("version"));
                paramsBuild.append("&parking_code=" + jsonIn.get("parking_code"));
                paramsBuild.append("&tag_identifier=" + jsonIn.get("tag_identifier"));
                paramsBuild.append("&time_in=" + jsonIn.get("time_in"));
                paramsBuild.append("&reader_code=" + jsonIn.get("reader_code"));
                break;
            case "OUT":
                paramsBuild.append("message_type=OUT");
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
        urlParameters = paramsBuild.toString();
    }


    public JSONObject postEvent(JSONObject jsonIn) throws java.net.SocketTimeoutException, MessageTypeException,
            java.net.MalformedURLException, IOException {

        formatUrl(jsonIn);
        url = Config.url;
        switch (jsonIn.get("message_type").toString()) {
            case "IN":
                url = url + Config.arrivalOperation;
                break;
            case "OUT":
                url = url + Config.departureOperation;
                break;
            default:
                throw new MessageTypeException("Malformed message type");
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
        System.out.println("Post parameters : " + urlParameters);
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
        return jsonResponse;
    }
}
