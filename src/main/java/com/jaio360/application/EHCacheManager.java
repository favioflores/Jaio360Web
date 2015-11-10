/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.application;

import com.jaio360.orm.Elemento;
import com.jaio360.utils.Constantes;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
public class EHCacheManager implements Serializable{
    
    private static Log log = LogFactory.getLog(EHCacheManager.class);
    private static final CacheManager cacheManager;
    private static Ehcache elementosCache;
    
    static{

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        
        InputStream resourceAsStream = contextClassLoader.getResourceAsStream("ehcache.xml");

        cacheManager = CacheManager.create(resourceAsStream);

    }
    
    public EHCacheManager(){
        elementosCache = cacheManager.getEhcache("elementosCache");
    }

    public void agregarElemento(Elemento objElemento){
        Element element = new Element(objElemento.getElIdElementoPk(), objElemento);
        elementosCache.put(element);
    }
    
    public static String obtenerDescripcionElemento(Integer intIdElemento){
        
        try {
            Elemento objElement = (Elemento) elementosCache.get(intIdElemento).getValue();
            return objElement.getElTxDescripcion();
        } catch (Exception e) {
            log.error(e);
        }
        
        return Constantes.strVacio;
    }

    public static String obtenerValor1Elemento(Integer intIdElemento){
        
        try {
            Elemento objElement = (Elemento) elementosCache.get(intIdElemento).getValue();
            return objElement.getElTxValor1();
        } catch (Exception e) {
            log.error(e);
        }
        
        return Constantes.strVacio;
    }
    
    public static String obtenerCadenaElemento(Integer intIdElemento){
        
        try {
            Elemento objElement = (Elemento) elementosCache.get(intIdElemento).getValue();
            
            byte[] bdata = objElement.getElCadena();
            String mensaje = new String(bdata); 
            
            return mensaje;
        } catch (Exception e) {
            log.error(e);
        }
        
        return Constantes.strVacio;
    }
    
    public static List<Elemento> obtenerElementosPorDefinicion(Integer intIdDefinicion){
        
        List<Elemento> lstElementos = new ArrayList();
        try {
            
             List lst = elementosCache.getKeys();
             
             Iterator itLst = lst.iterator();
             
             while(itLst.hasNext()){
                 Elemento objElemento = (Elemento) elementosCache.get(itLst.next()).getValue();
                 
                 if(intIdDefinicion.equals(objElemento.getDefinicionTabla().getDtIdDefinicionPk())){
                     lstElementos.add(objElemento);
                 }
             }
             
             return lstElementos;
             
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        
    }
        
}
