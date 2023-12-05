/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.initial;

import com.jaio360.application.DummyDatabase;
import java.io.Serializable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
public class DummyDatabaseInit extends HttpServlet implements Serializable {
    
    private static Logger log = Logger.getLogger(DummyDatabaseInit.class);

    /**
     * @param config
     * @throws ServletException
     *
     *
     */
    @Override
    public void init(ServletConfig config) throws ServletException {

        // Iniciar Thread
        try {
            
            DummyDatabase dummy = new DummyDatabase();
            dummy.start();
            
        } catch (Exception e) {
            log.error(e);
        }
        
    }
    
}
