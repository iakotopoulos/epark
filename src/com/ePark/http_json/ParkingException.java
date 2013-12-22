/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

/**
 * Custom exception thrown by HttpPoster class when the web service returns a response_code other than 1 (which indicates success)
 * @author Pantelis Karamolegkos
 */
public class ParkingException extends Exception {

    public ParkingException(String message) {
        super(message);
    }
}
