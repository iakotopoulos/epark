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
public abstract class Response {
    
    private JSONObject jsonResponse;
    private int response_code;
    private String response_message;
    
    public Response(JSONObject jsonIn){
        jsonResponse = jsonIn;
        response_code = jsonIn.getInt("response_code");
        response_message = jsonIn.getString("response_message");
    }
    
    public int getResponseCode() {
        return response_code;
    }
    public String getResponseMessage(){
        return response_message;
    }
    
    @Override
    public String toString(){
        return jsonResponse.toString();
    }
    
}
