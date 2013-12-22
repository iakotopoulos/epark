/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 * Response object return upon http posting of availability updates.
 * 
 * This is the object type returned by the postAvailabilityUpdate method of HttpPoster class.
 * @see HttpPoster
 * @author Pantelis Karamolegkos
 */
public class AUResponse extends Response {
    
    private int availability;
    private int availability_disabled;
    
     /**
     * Constructor for initializing an ArrivalResponse through a JSONObject.
     * 
     * @param jsonIn The JSONObject used for initialization
     */
    public AUResponse(JSONObject jsonIn) {
        super(jsonIn);
        availability = jsonIn.getInt("availability");
        availability_disabled = jsonIn.getInt("availability_disabled");
    }
    
    public int getAvailability(){
        return availability;
    }
    
     public int getAvailabilityDisabled(){
        return availability_disabled;
    }
    
}
