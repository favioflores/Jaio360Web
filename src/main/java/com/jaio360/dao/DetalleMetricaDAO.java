package com.jaio360.dao;

import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Metrica;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DetalleMetricaDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    public Integer guardaDetalleMetrica(DetalleMetrica detalleMetrica) throws HibernateException 
    { 
        Integer id = 0;  

        try { 
            iniciaOperacion(); 
            id = (Integer)sesion.save(detalleMetrica); 
            tx.commit(); 
        } catch (HibernateException he){ 
            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        }  

        return id; 
    }  

    public void actualizaDetalleMetrica(DetalleMetrica detalleMetrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(detalleMetrica); 
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

    public boolean eliminaDetalleMetrica(DetalleMetrica detalleMetrica) throws HibernateException { 
        
        boolean correcto = true;
        
        try { 
            iniciaOperacion(); 
            
            
            /* Borra relacion */
            sesion.delete(detalleMetrica); 
            
            /* Actualiza orden */
            
            Query sql = sesion.createQuery("from DetalleMetrica dm where dm.metrica.proyecto.poIdProyectoPk = :idProyecto order by dm.deNuOrden asc ");
            sql.setInteger("idProyecto", Utilitarios.obtenerProyecto().getIntIdProyecto());
            
            List<DetalleMetrica> listaDetalleMetrica = sql.list();
            
            int count = 0;
            
            for(DetalleMetrica objDetalleMetrica : listaDetalleMetrica){
                objDetalleMetrica.setDeNuOrden(count);
                sesion.update(objDetalleMetrica);
                count++;
            }
            
            tx.commit(); 
        } catch (HibernateException he) { 
            correcto = false;
            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        } 
        
        return correcto;
    } 
    
    
    public void eliminaDetalleMetricaTotal(Metrica metrica) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("delete from DetalleMetrica dm where dm.metrica.meIdMetricaPk= :idMetrica");
            sql.setInteger("idMetrica", metrica.getMeIdMetricaPk());
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
    
    

    public DetalleMetrica obtenDetalleMetrica(long idDetalleMetrica) throws HibernateException 
    { 
        DetalleMetrica detalleMetrica = null;  
        try 
        { 
            iniciaOperacion(); 
            detalleMetrica = (DetalleMetrica) sesion.get(DetalleMetrica.class, (int)idDetalleMetrica); 
        } finally 
        { 
            sesion.close(); 
        }  

        return detalleMetrica; 
    }  

    public List<DetalleMetrica> obtenListaDetalleMetrica() throws HibernateException 
    { 
        List<DetalleMetrica> listaDetalleMetrica = null;  

        try 
        { 
            iniciaOperacion(); 
            listaDetalleMetrica = sesion.createQuery("from DetalleMetrica").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaDetalleMetrica; 
    }  
    
    public List<DetalleMetrica> obtenListaDetalleMetrica(Metrica metrica) throws HibernateException 
    { 
        List<DetalleMetrica> listaDetalleMetrica = null;  

        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("from DetalleMetrica dm where dm.metrica.meIdMetricaPk = :idMetrica order by dm.deNuOrden asc ");
            sql.setInteger("idMetrica", metrica.getMeIdMetricaPk());
            listaDetalleMetrica = sql.list();
        } finally 
        { 
            sesion.close(); 
        }  

        return listaDetalleMetrica; 
    }  
    
    
    
     public List<DetalleMetrica> obtenListaDetalleMetricaMetricaProyecto(int idProyecto) throws HibernateException 
    { 
        List<DetalleMetrica> listaDetalleMetrica = null;  

        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("from DetalleMetrica dm where dm.metrica.proyecto.poIdProyectoPk = :idProyecto");
            sql.setInteger("idProyecto", idProyecto);
            listaDetalleMetrica = sql.list();
        } finally 
        { 
            sesion.close(); 
        }  

        return listaDetalleMetrica; 
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
    
    
     public Integer obtenMaxMetricaProyecto(int idProyecto) throws HibernateException 
    { 
        Integer a = 0;

        try 
        { 
            iniciaOperacion(); 
            Query sql = sesion.createQuery("select max(dm.deNuOrden) + 1 from DetalleMetrica dm where dm.metrica.proyecto.poIdProyectoPk = :idProyecto");
            sql.setInteger("idProyecto", idProyecto);
            a = (Integer) sql.uniqueResult();
        } finally 
        { 
            sesion.close(); 
        }  

        return a; 
    } 
     
     
     
     
     
     
     
     
     
     
     
    public boolean subirMetrica(DetalleMetrica objDetalleMetrica, List<DetalleMetrica> lstDetalleMetricas, Metrica metrica) throws HibernateException { 
        
        boolean correcto = true;

        try { 
            
            iniciaOperacion();

            Integer pos = lstDetalleMetricas.indexOf(objDetalleMetrica);

            DetalleMetrica objDetalleMetricaNuev = lstDetalleMetricas.get(pos);
            DetalleMetrica objDetalleMetricaTemp = lstDetalleMetricas.get(pos - 1);

            int newPos = objDetalleMetricaTemp.getDeNuOrden();
            int lastPos = objDetalleMetrica.getDeNuOrden();

            objDetalleMetricaNuev.setDeNuOrden(newPos);
            objDetalleMetricaTemp.setDeNuOrden(lastPos);
            sesion.update(objDetalleMetricaTemp);
            sesion.update(objDetalleMetricaNuev);

            tx.commit();
            
        } catch (HibernateException he){
            manejaExcepcion(he); 
            correcto = false;
        } catch (Exception e){
            tx.rollback();
            correcto = false;
        } finally { 
            sesion.close(); 
        } 
        
        return correcto;
    } 
    
    
    public boolean bajarMetrica(DetalleMetrica objDetalleMetrica, List<DetalleMetrica> lstDetalleMetricas, Metrica metrica) throws HibernateException { 
        
        boolean correcto = true;

        try { 
            
            iniciaOperacion(); 

            Integer pos = lstDetalleMetricas.indexOf(objDetalleMetrica);

            DetalleMetrica objDetalleMetricaNuev = lstDetalleMetricas.get(pos);
            DetalleMetrica objDetalleMetricaTemp = lstDetalleMetricas.get(pos + 1);

            int newPos = objDetalleMetricaTemp.getDeNuOrden();
            int lastPos = objDetalleMetrica.getDeNuOrden();

            objDetalleMetricaNuev.setDeNuOrden(newPos);
            objDetalleMetricaTemp.setDeNuOrden(lastPos);
            sesion.update(objDetalleMetricaTemp);
            sesion.update(objDetalleMetricaNuev);

            tx.commit(); 
        } catch (HibernateException he){ 
            manejaExcepcion(he); 
            correcto = false;
        } catch (Exception e){
            tx.rollback();
            correcto = false;
        } finally { 
            sesion.close(); 
        } 
                
        return correcto;
    } 
    
    
    
    
}
