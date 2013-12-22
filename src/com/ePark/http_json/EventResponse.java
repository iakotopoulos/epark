/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

import net.sf.json.JSONObject;

/**
 * Abstract class for the two event response objects.
 * 
 * Implements getter methods for both Event Response objects (arrival / departure)
 * 
 * @author Pantelis Karamolegkos
 */
public abstract class EventResponse extends Response {
    private final int ticket_number;
    private final String card_name;
    private final double card_balance;
    private final String account_name;
    private final double account_balance;
    private final String car_no;
    private final String display_message;
    
    /**
     * Constructor for the abstract class EventResponse.
     * 
     * Uses a JSONObject as parameter.
     * @param jsonIn 
     */
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
    
    /**
     * Getter method for the ticket number of the transaction.
     * 
     * @return Integer, the ticket number
     */
    public int getTicketNumber(){
        return ticket_number;
    }
    
    /**
     * Getter method for the Card Name used in the transaction.
     * 
     * @return String - the card name
     */
    public String getCardName(){
        return card_name;
    }
    
    /**
     * Getter method for the card balance.
     * 
     * @return Double - the card balance 
     */
    public double getCardBalance(){
        return card_balance;
    }
    
    /**
     * Getter method for the account name used in the transaction.
     * 
     * @return String - The Account Name
     */
    public String getAccountName(){
        return account_name;
    }
    
    /**
     * Getter method for the Account Balance.
     * 
     * @return Double - The Account Balance
     */
    public double getAccountBalance(){
        return account_balance;
    }
    
    /**
     * Getter method for the car no (license plate no) of the car in the specific transaction.
     * 
     * @return String - The car no (license plate no)
     */
    public String getCarNo(){
        return car_no;
    }
    
    /**
     * Getter method for the display message returned by the web service
     * 
     * @return String - The display message returned by the web service
     */
    public String getDisplayMessage(){
        return display_message;
    }
    
    
}
