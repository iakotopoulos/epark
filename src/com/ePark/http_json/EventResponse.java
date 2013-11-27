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
public abstract class EventResponse extends Response {
    private int ticket_number;
    private String card_name;
    private double card_balance;
    private String account_name;
    private double account_balance;
    private String car_no;
    private String display_message;
    
    public EventResponse(JSONObject jsonIn){
        super(jsonIn);
        ticket_number = jsonIn.getInt("ticket_number");
        card_name = jsonIn.getString("card_name");
        card_balance = jsonIn.getDouble("card_balance");
        account_name = jsonIn.getString("account_name");
        account_balance = jsonIn.getDouble("account_balance");
        car_no = jsonIn.getString("car_no");
        display_message = jsonIn.getString("display_message");
        
    }
    
    public int getTicketNumber(){
        return ticket_number;
    }
    
    public String getCardName(){
        return card_name;
    }
    
    public double getCardBalance(){
        return card_balance;
    }
    
    public String getAccountName(){
        return account_name;
    }
    
    public double getAccountBalance(){
        return account_balance;
    }
    
    public String getCarNo(){
        return car_no;
    }
    
    public String getDisplayMessage(){
        return display_message;
    }
    
    
}
