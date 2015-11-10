package com.jaio360.dao;

import com.jaio360.orm.DefinicionTabla;
import com.jaio360.orm.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class DefinicionTablaDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

   

    public DefinicionTablaDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaDefinicionTabla(DefinicionTabla definicionTabla) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(definicionTabla)) ; 
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

    public void actualizaDefinicionTabla(DefinicionTabla definicionTabla) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(definicionTabla); 
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

    public void eliminaDefinicionTabla(DefinicionTabla definicionTabla) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(definicionTabla); 
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

    public DefinicionTabla obtenDefinicionTabla(long idDefinicionTabla) throws HibernateException 
    { 
        DefinicionTabla definicionTabla = null;  
        try 
        { 
            iniciaOperacion(); 
            definicionTabla = (DefinicionTabla) sesion.get(DefinicionTabla.class, (int)idDefinicionTabla); 
        } finally 
        { 
            sesion.close(); 
        }  

        return definicionTabla; 
    }  

    public List<DefinicionTabla> obtenDefinicionTabla() throws HibernateException 
    { 
        List<DefinicionTabla> listaDefinicionTabla = null;  

        try 
        { 
            iniciaOperacion(); 
            listaDefinicionTabla = sesion.createQuery("from DefinicionTabla").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaDefinicionTabla; 
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
