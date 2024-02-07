package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.HistorialAcceso;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HistorialAccesoDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    private static Logger log = Logger.getLogger(HistorialAcceso.class);

    public HistorialAccesoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public Integer guardaHistorialAcceso(HistorialAcceso historialAcceso) throws HibernateException 
    { 
        Integer id = null;  

        try 
        { 
            iniciaOperacion(); 
            id = (Integer) sesion.save(historialAcceso); 
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

    public void actualizaHistorialAcceso(HistorialAcceso historialAcceso) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(historialAcceso); 
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

    public void eliminaHistorialAcceso(HistorialAcceso historialAcceso) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(historialAcceso); 
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

    public HistorialAcceso obtenHistorialAcceso(Integer idHistorialAcceso) throws HibernateException 
    { 
        HistorialAcceso historialAcceso = null;  
        try 
        { 
            iniciaOperacion(); 
            historialAcceso = (HistorialAcceso) sesion.get(HistorialAcceso.class, idHistorialAcceso); 
        } finally 
        { 
            sesion.close(); 
        }  

        return historialAcceso; 
    }  
    
    public Integer obtenIntentosFallidos(Integer idUserPk, Integer idHistPk){ 
            
        iniciaOperacion();
        
        try {
        
            String sql = "SELECT COUNT(HA.HA_ID_HISTORIAL_PK) AS CANT   " +
                        "  FROM historial_acceso HA                     " +
                        " WHERE HA.HA_ID_HISTORIAL_PK < :idHistPk       " +
                        "   AND HA.US_ID_CUENTA_FK = :idUserPk          " +
                        "   AND HA.HA_IN_ESTADO = FALSE                 " +
                        "   AND HA.HA_ID_HISTORIAL_PK >  (SELECT MAX(H.HA_ID_HISTORIAL_PK)          " +
                        "                                   FROM historial_acceso H                 " +
                        "				   WHERE H.US_ID_CUENTA_FK = :idUserPk      " +
                        "				     AND H.HA_IN_ESTADO = TRUE              " +
                        "			             AND H.HA_ID_HISTORIAL_PK < :idHistPk ) " ;

            Query query = sesion.createSQLQuery(sql).addScalar("CANT");
            
            
            query.setInteger("idUserPk", idUserPk);
            query.setInteger("idHistPk", idHistPk);

            return Integer.parseInt(query.uniqueResult().toString());
            
        } catch (Exception e) {
            log.error(e);
        }
        
        return 0;
    
    }

    public Date obtenUltimoAcceso(Integer idUserPk) throws HibernateException{ 
        
        Date dtUltimoAcceso = null;  
        
        try { 
            
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("select hist.haFeIngreso from HistorialAcceso hist join hist.usuario user where user.usIdCuentaPk = ? and hist.haInEstado = ? order by hist.haFeIngreso desc "); 
            
            query.setInteger(0, idUserPk);
            query.setBoolean(1, true);
            
            query.setMaxResults(1);
            
            List lstHistorial = query.list();
            
            if(!lstHistorial.isEmpty()){
                 dtUltimoAcceso = (Date) lstHistorial.get(0);
            }
            
            return dtUltimoAcceso;
        
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        
        return dtUltimoAcceso;

    }  

    public Date obtenUltimoAccesoByEmail(String strEmail) throws HibernateException{ 
        
        Date dtUltimoAcceso = null;  
        
        try { 
            
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("select hist.haFeIngreso from HistorialAcceso hist join hist.usuario user where user.usIdMail = ? order by hist.haFeIngreso desc "); 
            
            query.setString(0, strEmail);
            
            query.setMaxResults(1);
            
            List lstHistorial = query.list();
            
            if(!lstHistorial.isEmpty()){
                 dtUltimoAcceso = (Date) lstHistorial.get(0);
            }
            
            return dtUltimoAcceso;
        
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        
        return dtUltimoAcceso;

    }  
    
    public List<HistorialAcceso> obtenListaHistorialAcceso() throws HibernateException 
    { 
        List<HistorialAcceso> listaHistorialAcceso = null;  

        try 
        { 
            iniciaOperacion(); 
            listaHistorialAcceso = sesion.createQuery("from HistorialAcceso").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaHistorialAcceso; 
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