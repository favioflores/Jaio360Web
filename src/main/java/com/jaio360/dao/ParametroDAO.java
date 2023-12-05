package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Parametro;
import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParametroDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    private static Logger log = Logger.getLogger(ParametroDAO.class);
   

    public ParametroDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaParametro(Parametro parametro) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(parametro)) ; 
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

    public void actualizaParametro(Parametro parametro) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(parametro); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public void eliminaParametro(Parametro parametro) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(parametro); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public Parametro obtenParametro(Integer intIdProyecto, Integer intTipoParametro) throws HibernateException 
{ 
        Parametro parametro = null;  
        
        try { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from Parametro p where p.proyecto.poIdProyectoPk = ?  and p.paIdTipoParametro = ? ");
            
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, intTipoParametro);
            
            parametro = (Parametro) query.uniqueResult();
            
        } catch (HibernateException e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  

        return parametro; 
    }  
    

    public List<Parametro> obtenListaParametros(Integer intIdProyecto) throws HibernateException 
    { 
        List<Parametro> listaParametros = null;  

        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from Parametro p where p.proyecto.poIdProyectoPk = ? "); 
            
            query.setInteger(0, intIdProyecto);
            
            listaParametros = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaParametros; 
    }  

    private void iniciaOperacion() throws HibernateException 
    { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  

    private void manejaExcepcion(HibernateException he) throws HibernateException 
    { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 

}
