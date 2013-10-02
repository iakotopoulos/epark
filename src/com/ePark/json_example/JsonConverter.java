/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.json_example;

import net.sf.json.JSONObject;
/**
 *
 * @author pkaramol
 */
public class JsonConverter {
    
    private JSONObject jsonobj;
    
    public JsonConverter(String message_type, int version, String parking_code, String tag_identifier, String tag_data, String time_in, int reader_code){
        
        jsonobj = new JSONObject();
        
        
        //TODO: add validation for parameter values
        jsonobj.put("message_type", message_type);
        jsonobj.put("version", version);
        jsonobj.put("parking_code", parking_code);
        jsonobj.put("tag_identifier", tag_identifier);
        jsonobj.put("tag_data", tag_data);
        jsonobj.put("time_in", time_in);
        jsonobj.put("reader_code", reader_code);
    }
    
    public JSONObject getJsonObj(){
        return this.jsonobj;
    }
    
    // return string representation of json object
    public String getJsonObjStr(int identation){
            return this.jsonobj.toString(identation);
    }
}
