package com.jaio360.dao;

import com.jaio360.domain.RelacionBean;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RelacionDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    private static Logger log = Logger.getLogger(RelacionDAO.class);

    public RelacionDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public boolean borraRelacion(Integer intIdRelacion) throws HibernateException{ 
        
        boolean flag = true;
        
        try{ 
            
            iniciaOperacion(); 
            
            Query query2 = sesion.createSQLQuery("delete from relacion_participante where RE_ID_RELACION_FK = ? ");
            query2.setInteger(0, intIdRelacion);
            query2.executeUpdate();
            
            Query query1 = sesion.createSQLQuery("delete from relacion where RE_ID_RELACION_PK = ? ");
            query1.setInteger(0, intIdRelacion);
            query1.executeUpdate();
            
            tx.commit(); 

            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            log.error(he);
            flag = false;
        } finally { 
            sesion.close(); 
        }  

        return flag;
        
    }  
        
    public boolean guardaRelaciones(List<RelacionBean> lstRelaciones, Integer intIdProyecto) throws HibernateException{ 
        
        try{ 
            
            iniciaOperacion(); 
            /* BORRAR LOS HIJOS RELACION_PARTICIPANTE */
            
            /* BORRAR SOLO AQUELLOS QUE NO ESTAN EN EJECUCION */
            Query query = sesion.createQuery("delete from Relacion r where r.proyecto.poIdProyectoPk = ? and r.reIdEstado != ? ");
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_RELACION_EN_EJECUCION);
            query.executeUpdate();
            
            Proyecto proyecto = new Proyecto();
            proyecto.setPoIdProyectoPk(intIdProyecto);

            Relacion objRelacion;
            
            /* INSERTAR SOLO AQUELLOS QUE ESTAN REGISTRADOS */
            for (RelacionBean strRel : lstRelaciones){ 
                
                if(strRel.getIntIdEstado().equals(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO)){
                    
                    objRelacion = new Relacion();
                    objRelacion.setReTxNombre(strRel.getStrNombre());
                    objRelacion.setReTxAbreviatura(strRel.getStrAbreviatura());
                    objRelacion.setReTxDescripcion(strRel.getStrDescripcion());
                    objRelacion.setReColor(strRel.getStrColor());
                    objRelacion.setReIdEstado(strRel.getIntIdEstado());
                    objRelacion.setProyecto(proyecto);

                    sesion.save(objRelacion);
                    
                }
                
            }
            
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            log.error(he);
            return false;
        } finally { 
            sesion.close(); 
        }  

        return true; 
    }  

    public void actualizaRelacion(Relacion relacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(relacion); 
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

    public void eliminaRelacion(Relacion relacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(relacion); 
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

    public Relacion obtenRelacion(Integer idRelacion) throws HibernateException 
    { 
        Relacion relacion = null;  
        try 
        { 
            iniciaOperacion(); 
            relacion = (Relacion) sesion.get(Relacion.class, (int)idRelacion); 
        } finally 
        { 
            sesion.close(); 
        }  

        return relacion; 
    }  

    public List<Relacion> obtenListaRelacion() throws HibernateException 
    { 
        List<Relacion> listaRelacion = null;  

        try 
        { 
            iniciaOperacion(); 
            listaRelacion = sesion.createQuery("from Relacion").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRelacion; 
    }  
    
    public List<Relacion> obtenListaRelacionPorProyecto(Proyecto proyecto) throws HibernateException 
    { 
        List<Relacion> listaRelacion = null;  
        try 
        { 
            iniciaOperacion();             
            Query query = sesion.createQuery("from Relacion as re where re.proyecto.poIdProyectoPk = :idProyecto"); 
            query.setParameter("idProyecto", proyecto.getPoIdProyectoPk());
            
            listaRelacion = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRelacion; 
    }
    
    public List<Relacion> obtenListaRelacionPorEvaluado(Integer intIdProyecto, Integer intIdEvaluado) throws HibernateException 
    { 
        List<Relacion> listaRelacion = null;  
        try 
        { 
            iniciaOperacion();             
            Query query = sesion.createQuery("select distinct re from Relacion re join re.relacionParticipantes rp where re.proyecto.poIdProyectoPk = :idProyecto and rp.participante.paIdParticipantePk = :idEvaluado "); 
            query.setInteger("idProyecto", intIdProyecto);
            query.setInteger("idEvaluado", intIdEvaluado);
            
            listaRelacion = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRelacion; 
    }

    public List<Relacion> obtenListaRelacionPorProyecto(Integer intIdProyecto) throws HibernateException 
    { 
        List<Relacion> listaRelacion = null;  
        try 
        { 
            iniciaOperacion();             
            Query query = sesion.createQuery("from Relacion as re where re.proyecto.poIdProyectoPk = :idProyecto "); 
            query.setInteger("idProyecto", intIdProyecto);
            
            listaRelacion = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRelacion; 
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
    
    public Integer guardaRelacion(Relacion objRelacion) throws HibernateException 
    { 
        Integer id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = (Integer)sesion.save(objRelacion);
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
