/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

/**
 * Configuration class for the web service / http communication with the main site.
 * 
 * @author Pantelis Karamolegkos
 */
public class Config {

    public static final String url = "http://parking.itenasolutions.com:8080/json/reply/";
    public static final String arrivalOperation = "VehicleArrival";
    public static final String departureOperation = "VehicleDeparture";
    public static final String availabilityUpdate = "AvailabilityUpdate";
    public static final String availabilityUpdateVersion = "1";
    public static final int connectTimeout = 3000;
    public static final int readTimeout = 2000;
    
}
