/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jaio360.exception;

import java.io.Serializable;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author favio.flores
 */
public class ShowcaseExceptionHandlerFactory extends ExceptionHandlerFactory implements Serializable{

    private ExceptionHandlerFactory base;
    
    private ShowcaseExceptionHandler cached;
    
    public ShowcaseExceptionHandlerFactory(ExceptionHandlerFactory base) {
        this.base = base;
    }
    
    @Override
    public ExceptionHandler getExceptionHandler() {
        if(cached == null) {
            cached = new ShowcaseExceptionHandler(base.getExceptionHandler());
        }
        
        return cached;
    }
}