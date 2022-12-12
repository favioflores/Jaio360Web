package com.jaio360.dao;

import com.jaio360.domain.EvaluadorRelacion;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RelacionParticipanteDAO implements Serializable{
    private Session sesion; 
    private Transaction tx;  

   

    public RelacionParticipanteDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public long guardaRelacionParticipante(RelacionParticipante reParticipante) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(reParticipante)) ; 
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

    public void actualizaRelacionParticipante(RelacionParticipante reParticipante) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(reParticipante); 
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

    public void eliminaRelacionParticipante(RelacionParticipante reParticipante) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(reParticipante); 
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
    
    public RelacionParticipante obtenRelacionParticipante(RelacionParticipanteId objRelacionParticipanteId) throws HibernateException 
    { 
        RelacionParticipante objRelacionParticipante = null;  
        try 
        { 
            iniciaOperacion(); 
            objRelacionParticipante = (RelacionParticipante) sesion.get(RelacionParticipante.class, objRelacionParticipanteId); 
        } finally 
        { 
            sesion.close(); 
        }  

        return objRelacionParticipante; 
    }  
    
    public List<RelacionParticipante> obtenListaRelParticipante(Integer intParticipantePk) throws HibernateException 
    { 
        List<RelacionParticipante> listaRelacionParticipante = null;  

        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RelacionParticipante r where  r.participante.paIdParticipantePk= ? "); 
            
            query.setInteger(0, intParticipantePk);
            //query.setBoolean(1, true);
            //query.setBoolean(2, true);
            
            listaRelacionParticipante = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRelacionParticipante; 
    }  
    
    public List<EvaluadorRelacion> obtenListaEvaluadorRelacion(Integer intParticipantePk) throws HibernateException 
    { 
        List<EvaluadorRelacion> listaEvaluadorRelacion = new ArrayList<>();  

        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createSQLQuery( " select repa.RE_ID_PARTICIPANTE_FK intIdEvaluador,                  " +
                                                "         re.RE_ID_RELACION_PK intIdRelacion,                         " +
                                                "         reev.RE_TX_DESCRIPCION strDescNombre,                       " +
                                                "         reev.RE_TX_CORREO strCorreo,                                " +
                                                "         re.RE_TX_NOMBRE strDescRelacion                           " +
                                                "    from relacion_participante repa                                  " +
                                                " 	  inner join red_evaluacion reev                              " +
                                                " 	  on repa.RE_ID_PARTICIPANTE_FK = reev.RE_ID_PARTICIPANTE_PK  " +
                                                " 	  inner join relacion re                                      " +
                                                " 	  on repa.RE_ID_RELACION_FK = re.RE_ID_RELACION_PK            " +
                                                "   where repa.PA_ID_PARTICIPANTE_FK = ?                              ");
       
            
            query.setInteger(0, intParticipantePk);
            
            //listaEvaluadorRelacion = (List<EvaluadorRelacion>)query.list();
            Iterator iterator= query.list().iterator();
            while(iterator.hasNext()){
                Object[] tuple= (Object[]) iterator.next();
                
                EvaluadorRelacion eval= new EvaluadorRelacion();
                eval.setIntIdEvaluador((Integer)tuple[0]);
                eval.setIntIdRelacion((Integer)tuple[1]);
                eval.setStrDescNombre((String)tuple[2]);
                eval.setStrCorreo((String)tuple[3]);
                eval.setStrDescRelacion((String)tuple[4]);
                listaEvaluadorRelacion.add(eval);
                
            }
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaEvaluadorRelacion; 
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

    public boolean existeRelacionConEvaluado(Integer intRedEvaluacionPk) throws HibernateException { 
        
        boolean correcto = false;

        try { 
            iniciaOperacion(); 
            Query query = sesion.createSQLQuery( " select count(*)  " +
                                                "    from relacion_participante repa                                  " +
                                                " 	  inner join red_evaluacion reev                              " +
                                                " 	  on repa.RE_ID_PARTICIPANTE_FK = reev.RE_ID_PARTICIPANTE_PK  " +
                                                "   where reev.RE_ID_PARTICIPANTE_PK = ?                              ");
       
            
            query.setInteger(0, intRedEvaluacionPk);
            
            int i = (int) query.uniqueResult();
            
            if(i>0){
                return true;
            }
            
        } finally { 
            sesion.close(); 
        }  

        return correcto; 
    } 
    
    public int existeRelacionesXRelacion(Integer intRelacionPk) throws HibernateException { 
        
        BigInteger i;

        try { 
            iniciaOperacion(); 
            Query query = sesion.createSQLQuery( " select count(*)  " +
                                                "    from relacion_participante rp " +
                                                "   where rp.RE_ID_RELACION_FK = ? ");
       
            
            query.setInteger(0, intRelacionPk);
            
            i = (BigInteger) query.uniqueResult();
            
        } finally { 
            sesion.close(); 
        }  

        return i.intValue(); 
    } 
    
    
    public List<RelacionParticipante> obtenListaRelParticipanteXProyecto(Integer intIdProyecto) throws HibernateException
    { 
        List<RelacionParticipante> listaRelacionParticipante = null;  

        try { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("select r from RelacionParticipante r where  r.participante.proyecto.poIdProyectoPk = ? "); 
            
            query.setInteger(0, intIdProyecto);
            
            listaRelacionParticipante = query.list();
            
        } finally { 
            sesion.close(); 
        }  

        return listaRelacionParticipante; 
    }  
    
    public RelacionParticipanteId obtenRelacionParticipanteId(ProyectoInfo objProyectoInfo, String reTxCorreo, Integer intIdEvaluado) throws HibernateException 
    { 
        RelacionParticipanteId relacionParticipanteId = null;  
        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createSQLQuery(
                " select rp.PA_ID_PARTICIPANTE_FK,                                " +
                "        rp.RE_ID_PARTICIPANTE_FK,                                " +
                "        rp.RE_ID_RELACION_FK                                     " +
                "   from relacion_participante rp,                                " +
                "        participante pa,                                         " +
                "        red_evaluacion re,                                       " +
                "        cuestionario_evaluado ce,                                " +
                "        cuestionario cu                                          " +
                "  where pa.PA_ID_PARTICIPANTE_PK = rp.PA_ID_PARTICIPANTE_FK      " +
                "        and re.RE_ID_PARTICIPANTE_PK = rp.RE_ID_PARTICIPANTE_FK  " +
                "        and ce.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK  " +
                "        and cu.CU_ID_CUESTIONARIO_PK = ce.CU_ID_CUESTIONARIO_FK  " +
                "        and pa.PO_ID_PROYECTO_FK = ?                             " +
                "        and cu.CU_ID_CUESTIONARIO_PK  = ?                        " +
                "        and pa.PA_ID_PARTICIPANTE_PK = ?                         " +
                "        and pa.PA_IN_RED_CARGADA = true                          " +
                "        and pa.PA_IN_RED_VERIFICADA = true                       " +
                //"        and pa.PA_IN_AUTOEVALUAR = false                         " +
                "        and re.RE_ID_ESTADO = ?                                  " +
                "        and pa.PA_ID_ESTADO IN (?,?)                             " +
                "        and cu.CU_ID_ESTADO = ?                                  " +
                "        and ce.CE_ID_ESTADO = ?                                  " +
                "        and re.RE_TX_CORREO = ?                                  ");
            
            query.setInteger(0, objProyectoInfo.getIntIdProyecto());
            query.setInteger(1, objProyectoInfo.getIntIdCuestionario());
            query.setInteger(2, intIdEvaluado);
            query.setInteger(3, Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION);
            query.setInteger(4, Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION);
            query.setInteger(5, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
            query.setInteger(6, Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
            query.setInteger(7, Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION);
            query.setString(8, reTxCorreo);
            
            //relacionParticipanteId = (RelacionParticipanteId)query.uniqueResult();
            
            Iterator iterator= query.list().iterator();
            while(iterator.hasNext()){
                Object[] tuple= (Object[]) iterator.next();
                
                relacionParticipanteId = new RelacionParticipanteId();
                relacionParticipanteId.setPaIdParticipanteFk((Integer)tuple[0]);
                relacionParticipanteId.setReIdParticipanteFk((Integer)tuple[1]);
                relacionParticipanteId.setReIdRelacionFk((Integer)tuple[2]);
                
            }            
            
        } finally 
        { 
            sesion.close(); 
        }  

        return relacionParticipanteId; 
    }

}

