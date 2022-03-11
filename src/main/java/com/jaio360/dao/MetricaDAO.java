package com.jaio360.dao;

import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Metrica;
import com.jaio360.orm.Proyecto;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MetricaDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

   

    public MetricaDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaMetrica(Metrica metrica) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(metrica)) ; 
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
    
    public void guardaActualizaMetrica(Metrica metrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.saveOrUpdate(metrica); 
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
    
    

    public void actualizaMetrica(Metrica metrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(metrica); 
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
    
    public void actualizaMetricaEliminaDetalle(Metrica metrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            
            sesion.update(metrica); 
            
            Query sql = sesion.createQuery("delete from DetalleMetrica dm where dm.metrica.meIdMetricaPk = :idMetrica and dm.deNuOrden > :nuRango");
            sql.setInteger("idMetrica", metrica.getMeIdMetricaPk());
            sql.setInteger("nuRango", metrica.getMeNuRango()-1);
            sql.executeUpdate();
            
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

    public void eliminaMetrica(Metrica metrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(metrica); 
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
    
    public void eliminaMetrica(Integer intIdProyecto) throws HibernateException 
    { 
        try 
        { 
           iniciaOperacion(); 
            
            /* Borramos datos anteriores */
            /* SOLO BORRA LAS SELECCIONES DE EVALUADOS QUE NO ESTAN EN EJECUCION */
           
            Query query1 = sesion.createQuery("delete from DetalleMetrica d where d.metrica.meIdMetricaPk in (select m.meIdMetricaPk from Metrica m where m.proyecto.poIdProyectoPk = ? ) "); 
            Query query2 = sesion.createQuery("delete from Metrica m where m.proyecto.poIdProyectoPk = ? "); 
             
            
            query1.setInteger(0, intIdProyecto);
            query1.executeUpdate();
            query2.setInteger(0, intIdProyecto);
            query2.executeUpdate();
 
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
    

    public Metrica obtenMetrica(long idMetrica) throws HibernateException 
    { 
        Metrica metrica = null;  
        try 
        { 
            iniciaOperacion(); 
            metrica = (Metrica) sesion.get(Metrica.class, (int)idMetrica); 
        } finally 
        { 
            sesion.close(); 
        }  

        return metrica; 
    }  
    
     public Metrica obtenMetricaProyecto(Proyecto proyecto) throws HibernateException 
    { 
        Metrica metricaReturn = null;  
        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("from Metrica me where me.proyecto.poIdProyectoPk = :idProyecto");
            sql.setInteger("idProyecto", proyecto.getPoIdProyectoPk());
            metricaReturn = (Metrica)  sql.uniqueResult();
        } finally 
        { 
            sesion.close(); 
        }  

        return metricaReturn; 
    }  
    
     public Metrica obtenMetricaProyecto(Integer idProyecto) throws HibernateException 
    { 
        Metrica metricaReturn = null;  
        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("from Metrica me where me.proyecto.poIdProyectoPk = :idProyecto");
            sql.setInteger("idProyecto", idProyecto);
            metricaReturn = (Metrica)  sql.uniqueResult();
        } finally 
        { 
            sesion.close(); 
        }  

        return metricaReturn; 
    }  

    public List<Metrica> obtenListaMetrica() throws HibernateException 
    { 
        List<Metrica> listaMetrica = null;  

        try 
        { 
            iniciaOperacion(); 
            listaMetrica = sesion.createQuery("from Metrica").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaMetrica; 
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
