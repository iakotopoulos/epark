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
public class DepartureResponse extends EventResponse {

    private double fee_amount;
    private int duration_minutes;

    public DepartureResponse(JSONObject jsonIn) {
        super(jsonIn);
        fee_amount = jsonIn.getDouble("fee_amount");
        duration_minutes = jsonIn.getInt("duration_minutes");
    }
    
    public double getFeeAmount(){
        return fee_amount;
    }
    
    public int getDurationMinutes(){
        return duration_minutes;
    }
}
