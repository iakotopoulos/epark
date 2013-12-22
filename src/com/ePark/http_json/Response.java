/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 * This is abstract class all response objects derive from.
 * All classes of the hierarchy are actually wrapper classes around JSONObject class for convenience in terms of accessing the relevant fields (type conversions etc)
 * Constructor uses a JSONObject to make field assignments.
 * @see JSONObject
 * 
 * @author Pantelis Karamolegkos
 */
public abstract class Response {
    
    private final JSONObject jsonResponse;
    private final int response_code;
    private final String response_message;
    
    public Response(JSONObject jsonIn){
        jsonResponse = jsonIn;
        response_code = jsonIn.getInt("response_code");
        response_message = jsonIn.getString("response_message");
    }
    
    /**
     * Getter method for the response code. 
     * 
     * @return The Response Code (1 for success)
     */
    public int getResponseCode() {
        return response_code;
    }
    
    /**
     * Getter method for the response message.
     * 
     * @return String indicating the transaction result
     */
    public String getResponseMessage(){
        return response_message;
    }
    
    @Override
    public String toString(){
        return jsonResponse.toString();
    }
    
}
