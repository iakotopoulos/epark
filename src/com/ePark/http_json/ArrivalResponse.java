/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 * Response object return upon http posting of arrival events.
 * 
 * This is the object type returned by the postArrival method of the HttpPoster class.
 * @see HttpPoster
 * @author Pantelis Karamolegkos
 */
public class ArrivalResponse extends EventResponse {
    
    private double auth_amount;
    
    /**
     * Constructor for initializing an ArrivalResponse through a JSONObject.
     * 
     * @param jsonIn The JSONOBject used for initialization
     */
    public ArrivalResponse(JSONObject jsonIn){
        super(jsonIn);
        auth_amount = jsonIn.getDouble("auth_amount");
    }
    
    /**
     * Getter method for retrieving the auth amount.
     * 
     * @return  Double - The auth amount.
     */
    public double getAuthAmount(){
        return auth_amount;
    }
    
}
