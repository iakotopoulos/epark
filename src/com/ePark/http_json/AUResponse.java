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
public class AUResponse extends Response {
    
    private int availability;
    private int availability_disabled;
        
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
