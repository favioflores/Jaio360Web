/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.initial;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.orm.Elemento;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
public class EhCacheInit extends HttpServlet implements Serializable{
    
    private static Log log = LogFactory.getLog(EhCacheInit.class);
    private EHCacheManager objEHCacheManager;
    
    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException{
    
        log.debug("Cargando CACHE del sistema");
        
        ElementoDAO objElementoDAO = new ElementoDAO();
        
        List lstElementos = objElementoDAO.obtenListaElemento();
        
        if(!lstElementos.isEmpty()){
        
            Iterator itLstElementos = lstElementos.iterator();
            
            objEHCacheManager = new EHCacheManager();
            
            while(itLstElementos.hasNext()){
            
                Elemento objElemento = (Elemento) itLstElementos.next();
                
                try {
                    String strTemp = ResourceBundle.getBundle("etiquetas").getString("elemento."+objElemento.getElIdElementoPk().toString());    
                    objElemento.setElTxDescripcion(strTemp);
                } catch (Exception e) {
                    log.info(e);
                }
                
                log.debug("Agregando a Cache : CACHE" + objElemento.getElTxDescripcion());
                objEHCacheManager.agregarElemento(objElemento);
                
            }
        
        }
        
        log.debug("CACHE cargado");
    
    }
    
}
