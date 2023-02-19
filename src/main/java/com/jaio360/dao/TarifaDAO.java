package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Tarifa;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TarifaDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

   

    public TarifaDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaTarifa(Tarifa tarifa) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(tarifa)) ; 
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

    public void actualizaTarifa(Tarifa tarifa) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(tarifa); 
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

    public void eliminaTarifa(Tarifa tarifa) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(tarifa); 
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

    public Tarifa obtenTarifa(long idTarifa) throws HibernateException 
    { 
        Tarifa tarifa = null;  
        try 
        { 
            iniciaOperacion(); 
            tarifa = (Tarifa) sesion.get(Tarifa.class, (int)idTarifa); 
        } finally 
        { 
            sesion.close(); 
        }  

        return tarifa; 
    }  

    public List<Tarifa> obtenListaTarifa() throws HibernateException 
    { 
        List<Tarifa> listaTarifa = null;  

        try 
        { 
            iniciaOperacion(); 
            listaTarifa = sesion.createQuery("from Tarifa").list(); 
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaTarifa; 
    }  
    
    public List<Tarifa> obtenListaTarifaParaClientes() throws HibernateException 
    { 
        List<Tarifa> listaTarifa = null;  

        try 
        { 
            iniciaOperacion(); 
            listaTarifa = sesion.createQuery("from Tarifa t where t.taDePrecio > 0 ").list(); 
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaTarifa; 
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
