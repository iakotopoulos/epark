/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.http_json;

/**
 * Custom exception thrown by HttpPoster class in case of unrecognized message type.
 *  
 * @author Pantelis Karamolegkos
 */
public class MessageTypeException extends Exception {

    public MessageTypeException(String message) {
        super(message);
    }
}
