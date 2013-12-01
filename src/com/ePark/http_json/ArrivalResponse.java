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
public class ArrivalResponse extends EventResponse {
    
    private double auth_amount;
    
    public ArrivalResponse(JSONObject jsonIn){
        super(jsonIn);
        auth_amount = jsonIn.getDouble("auth_amount");
    }
    
    public double getAuthAmount(){
        return auth_amount;
    }
    
}
