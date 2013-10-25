/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ePark.local;

import com.ePark.local.events.DeviceListener;
import java.util.ArrayList;

/**
 *
 * @author I-A
 */
public class EventManager implements DeviceListener{    

    public EventManager() {
        
    }

    @Override
    public void readerNotification() {
        
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void magneticNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
