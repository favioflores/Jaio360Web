package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Ubigeo;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UbigeoDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  
    
    public UbigeoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaUbigeo(Ubigeo ubigeo) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(ubigeo)) ; 
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

    public void actualizaUbigeo(Ubigeo ubigeo) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(ubigeo); 
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

    public void eliminaUbigeo(Ubigeo ubigeo) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(ubigeo); 
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

    public Ubigeo obtenUbigeo(long idUbigeo) throws HibernateException 
    { 
        Ubigeo ubigeo = null;  
        try 
        { 
            iniciaOperacion(); 
            ubigeo = (Ubigeo) sesion.get(Ubigeo.class, (int)idUbigeo); 
        } finally 
        { 
            sesion.close(); 
        }  

        return ubigeo; 
    }  

    public List<Ubigeo> obtenListaUbigeo(Integer intIdTipo, Integer intIdRef) throws HibernateException 
    { 
        List<Ubigeo> listaUbigeo = null;  

        try 
        { 
            iniciaOperacion(); 
            
            String strQuery = "from Ubigeo u where u.ubIdTipoUbigeo = ? ";
            
            if(Utilitarios.noEsNuloOVacio(intIdRef)){
                strQuery +=" and u.ubigeo.ubIdUbigeoPk = ? ";
            }
            
            strQuery +=" order by ubTxDescripcion ";
            
            Query query = sesion.createQuery(strQuery);
            query.setInteger(0, intIdTipo);
            if(Utilitarios.noEsNuloOVacio(intIdRef)){
                query.setInteger(1, intIdRef);
            }
            
            listaUbigeo = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaUbigeo; 
    }  
    
    public Integer obtenPais(Integer intCiudad) throws HibernateException{ 
        
        try { 
        
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("select u.ubigeo.ubIdUbigeoPk from Ubigeo u where u.ubIdUbigeoPk = ? "); 
            
            query.setInteger(0, intCiudad);  
            
            return (Integer) query.uniqueResult(); 
        
        } finally { 
            sesion.close(); 
        }  

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
