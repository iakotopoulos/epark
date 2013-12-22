/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 * Response object return upon http posting of departure events.
 * 
 * This is the object type returned by the postDeparture method of the HttpPoster class.
 * @see HttpPoster
 * @author Pantelis Karamolegkos
 */
public class DepartureResponse extends EventResponse {

    private final double fee_amount;
    private final int duration_minutes;
    
    /**
     * Constructor for initializing a DepartureResponse through a JSONObject.
     * 
     * @param jsonIn - The JSONObject used for initialization
     */
    public DepartureResponse(JSONObject jsonIn) {
        super(jsonIn);
        fee_amount = jsonIn.getDouble("fee_amount");
        duration_minutes = jsonIn.getInt("duration_minutes");
    }
    
    /**
     * Getter method for the free amount.
     * 
     * @return String - The free amount left
     */
    public double getFeeAmount(){
        return fee_amount;
    }
    
    /**
     * Getter method for the duration (minutes) the car was parked.
     * 
     * @return Number of minutes the car was parked.
     */
    public int getDurationMinutes(){
        return duration_minutes;
    }
}
