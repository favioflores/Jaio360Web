package com.jaio360.dao;

import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DestinatariosDAO implements Serializable{  
    
    private static Log log = LogFactory.getLog(DestinatariosDAO.class);
    private Session sesion; 
    private Transaction tx;  

    private void iniciaOperacion() throws HibernateException { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  

    private void manejaExcepcion(HibernateException he) throws HibernateException { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 
    
    public List obtieneDestinatarios(Integer intNotificacionPk, Session sesion) throws HibernateException { 
        
        List <Destinatarios> lstDestinatarios;

        Query query = sesion.createQuery("select de from Destinatarios de where de.notificaciones.noIdNotificacionPk = ? ");

        query.setInteger(0, intNotificacionPk);
        
        lstDestinatarios = query.list();
                
        return lstDestinatarios; 
    }
    
    public Integer guardaDestinatarios(Destinatarios objDestinatarios) throws HibernateException { 
        
        Integer id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = (Integer)sesion.save(objDestinatarios); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        }  

        return id; 
    }  
}