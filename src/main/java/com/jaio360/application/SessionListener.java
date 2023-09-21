/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jaio360.application;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author favio.flores
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //System.out.println("com.jaio360.application.SesionListener.sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //System.out.println("com.jaio360.application.SesionListener.sessionDestroyed()");
    }
    
}
