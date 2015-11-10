package com.jaio360.dao;

import com.jaio360.orm.Contrato;
import com.jaio360.orm.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ContratoDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  
 

    public ContratoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaContrato(Contrato contrato) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(contrato)) ; 
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

    public void actualizaContrato(Contrato contrato) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(contrato); 
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

    public void eliminaContrato(Contrato contrato) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(contrato); 
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

    public Contrato obtenContrato(long idContrato) throws HibernateException 
    { 
        Contrato contrato = null;  
        try 
        { 
            iniciaOperacion(); 
            contrato = (Contrato) sesion.get(Contrato.class, (int)idContrato); 
        } finally 
        { 
            sesion.close(); 
            
        }  

        return contrato; 
    }  

    public List<Contrato> obtenListaContrato() throws HibernateException 
    { 
        List<Contrato> listaContrato = null;  

        try 
        { 
            iniciaOperacion(); 
            listaContrato = sesion.createQuery("from Contrato").list(); 
        } finally 
        { 
            sesion.close(); 
             System.out.println("finalizo");
            
        }  

        return listaContrato; 
    } 
    
    public List<Contrato> obtenListaContratoPorUsuario(Integer intIdUsuario) throws HibernateException 
    { 
        List<Contrato> listaContrato = null;  

        try 
        { 
            iniciaOperacion(); 
            Query sql  = sesion.createQuery("from Contrato co where co.usuario.usIdCuentaPk = :idUsuario order by co.coIdContratoPk"); 
            sql.setInteger("idUsuario", intIdUsuario);
            listaContrato = sql.list();
        } finally 
        { 
            sesion.close(); 
             System.out.println("finalizo");
            
        }  

        return listaContrato; 
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
