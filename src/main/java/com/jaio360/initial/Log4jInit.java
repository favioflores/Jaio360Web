package com.jaio360.initial;

import java.io.File;
import java.io.Serializable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Favio
 */
public class Log4jInit extends HttpServlet implements Serializable {
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException{
    
        System.out.println("Log4j esta inicializando");
        
        String log4jLocation = config.getInitParameter("log4j-init-file") + File.separator + "log4j.properties";
        
        ServletContext sc = config.getServletContext();
        
        if ( log4jLocation == null ){
        
                System.err.println("No Log4j Properties Location");
                BasicConfigurator.configure();
                
        } else {
        
            String log4jProp = sc.getRealPath(log4jLocation);
        
            File properties = new File(log4jProp);
            
            if(properties.exists()){
            
                System.out.println("Inicializando Log4j " + log4jProp);
                PropertyConfigurator.configure(log4jProp);
                
            }else{
                System.out.println("Configuracion basica " + log4jProp);
                BasicConfigurator.configure();
                
            }
                
        }
    
    }
    
}
