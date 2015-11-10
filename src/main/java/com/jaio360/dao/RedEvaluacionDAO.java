package com.jaio360.dao;

import com.jaio360.domain.Evaluador;
import com.jaio360.domain.EvaluadorRelacion;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RedEvaluacionDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  
    private static Log log = LogFactory.getLog(RedEvaluacion.class);
   

    public RedEvaluacionDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }
    
    public Integer guardaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        Integer id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = (Integer)sesion.save(redEvaluacion); 
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
    
    public boolean guardaRedEvaluacion(List<Evaluador> lstEvaluados, Integer intIdProyecto) throws HibernateException { 
        
        boolean correcto = true;
                
        try { 
            
            iniciaOperacion(); 
            
            /* BORRA LOS PARTICIPANTES EN ESTADO REGISTRADO Y SUS RELACIONES */
            Query query = sesion.createQuery("delete from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? and r.reIdEstado = ? ");
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
            query.executeUpdate();
            
            Proyecto objProyecto = new Proyecto();
            objProyecto.setPoIdProyectoPk(intIdProyecto);

            RedEvaluacion objRedEvaluacion;
            
            for (Evaluador obj : lstEvaluados){  
                
                if(obj.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)){
                    
                    objRedEvaluacion = new RedEvaluacion();

                    objRedEvaluacion.setReIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADO);
                    objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
                    objRedEvaluacion.setReTxCorreo(obj.getReTxCorreo());
                    objRedEvaluacion.setReTxDescripcion(obj.getReTxDescripcion());
                    objRedEvaluacion.setReTxSexo(obj.getReTxSexo());
                    objRedEvaluacion.setReNrEdad(obj.getReNrEdad());
                    objRedEvaluacion.setReNrTiempoTrabajo(obj.getReNrTiempoTrabajo());
                    objRedEvaluacion.setReTxOcupacion(obj.getReTxOcupacion());
                    objRedEvaluacion.setReTxAreaNegocio(obj.getReTxAreaNegocio());
                    objRedEvaluacion.setProyecto(objProyecto);

                    sesion.save(objRedEvaluacion);
                    
                }
                
            }

            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            log.error(he);
            correcto = false;
        } finally { 
            sesion.close(); 
        }  

        return correcto; 
    }  

    public void actualizaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(redEvaluacion); 
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

    public void eliminaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(redEvaluacion); 
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

    public RedEvaluacion obtenRedEvaluacion(long idRedEvaluacion) throws HibernateException 
    { 
        RedEvaluacion redEvaluacion = null;  
        try 
        { 
            iniciaOperacion(); 
            redEvaluacion = (RedEvaluacion) sesion.get(RedEvaluacion.class, (int)idRedEvaluacion); 
        } finally 
        { 
            sesion.close(); 
        }  

        return redEvaluacion; 
    }  

    public List<RedEvaluacion> obtenListaRedEvaluacion(Integer intProyectoPk) throws HibernateException 
    { 
        List<RedEvaluacion> listaRedEvaluacion = null;  

        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? "); 
            
            query.setInteger(0, intProyectoPk);
            //query.setBoolean(1, true);
            //query.setBoolean(2, true);
            
            listaRedEvaluacion = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
    }  

    public List<RedEvaluacion> obtenListaRedEvaluacionConfirmados(Integer intProyectoPk) throws HibernateException { 
        
        List<RedEvaluacion> listaRedEvaluacion = null;  

        try { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? and r.reIdEstado = ? "); 
            
            query.setInteger(0, intProyectoPk);
            query.setInteger(1, Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION);
            
            listaRedEvaluacion = query.list();
            
        } finally { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
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
    
    
    public List<RedEvaluacion> obtenerListaEvaluadores(Integer intProyectoPk) throws HibernateException {
        
        List<RedEvaluacion> listaRedEvaluacion = null;  

        try{
            
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? order by r.reIdParticipantePk "); 
            
            query.setInteger(0, intProyectoPk);;
            
            listaRedEvaluacion = query.list();
            
        }catch (Exception e){
            log.error(e);
        }finally { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
    }
    
    public List obtenerRelacionParticipanteAnterior(Integer intProyectoPk, String strCorreoUsuario) throws HibernateException {
        
        List lista = null;  

        try{
            
            iniciaOperacion(); 
            Query query = sesion.createQuery("select rp.redEvaluacion.reIdParticipantePk, rp.relacion.reIdRelacionPk " +
                                             "  from RelacionParticipante rp " +
                                             " where rp.participante.paTxCorreo = ? " +
                                             "   and rp.participante.proyecto.poIdProyectoPk = ? "); 
            
            query.setString(0, strCorreoUsuario);
            query.setInteger(1, intProyectoPk);
            
            lista = query.list();
            
        }catch (Exception e){
            log.error(e);
        }finally { 
            sesion.close(); 
        }  

        return lista; 
    }
    
    public boolean guardaRelacionParticipanteEvaluado(List<EvaluadorRelacion> lstRed, Participante objParticipante) throws HibernateException{ 

        boolean flag = true;
        try{ 
            
            iniciaOperacion(); 
            
            /* Borramos datos anteriores */
            
            Query query = sesion.createQuery("delete from RelacionParticipante r where r.participante.paIdParticipantePk = ? ");
            query.setInteger(0, objParticipante.getPaIdParticipantePk());
            query.executeUpdate();
            
            /* Insertamos los datos nuevos */
            
            RelacionParticipante objRelacionParticipante;
            RedEvaluacion objRedEvaluacion;
            Relacion objRelacion;
            RelacionParticipanteId objRelacionParticipanteId;
            
            for (EvaluadorRelacion objEvaluadorRelacion:lstRed){
                
                if(objEvaluadorRelacion.getIntIdRelacion()!=null && 
                   objEvaluadorRelacion.getIntIdRelacion()!=0){
                
                    objRedEvaluacion = new RedEvaluacion();
                    objRedEvaluacion.setReIdParticipantePk(objEvaluadorRelacion.getIntIdEvaluador());

                    objRelacion = new Relacion();
                    objRelacion.setReIdRelacionPk(objEvaluadorRelacion.getIntIdRelacion());

                    objRelacionParticipanteId = new RelacionParticipanteId();
                    objRelacionParticipanteId.setPaIdParticipanteFk(objParticipante.getPaIdParticipantePk());
                    objRelacionParticipanteId.setReIdParticipanteFk(objEvaluadorRelacion.getIntIdEvaluador());
                    objRelacionParticipanteId.setReIdRelacionFk(objEvaluadorRelacion.getIntIdRelacion());

                    objRelacionParticipante = new RelacionParticipante();
                    objRelacionParticipante.setParticipante(objParticipante);
                    objRelacionParticipante.setRedEvaluacion(objRedEvaluacion);
                    objRelacionParticipante.setRelacion(objRelacion);
                    objRelacionParticipante.setId(objRelacionParticipanteId);
                    objRelacionParticipante.setRpIdEstado(Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_REGISTRADO);

                    sesion.save(objRelacionParticipante);
                }
            }

            tx.commit(); 
            
        } catch (HibernateException he){ 
            log.error(he);
            manejaExcepcion(he); 
            flag = false;
        } finally { 
            sesion.close(); 
        }  

        return flag; 
    } 

    
    public List<RedEvaluacion> obtenListaRedEvaluacionXEstado(Integer intIdProyecto, Integer intEstadoEvaluador) {

        List<RedEvaluacion> listaRedEvaluacion = null;  

        try { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? and r.reIdEstado = ? "); 
            
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, intEstadoEvaluador);
            
            listaRedEvaluacion = query.list();
            
        } finally { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
    }  

        
    public boolean actualizarRedEvaluacionRegistrados(Integer intIdProyecto, List<RedEvaluacion> lstRedEvaluacion) {
        
        boolean correcto = true;
        
        try{ 

            iniciaOperacion(); 
            
            for (RedEvaluacion objRedEvaluacion : lstRedEvaluacion){
            
                objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_EN_PARAMETRIZACION);
                sesion.update(objRedEvaluacion);
            
            }
            
            tx.commit();  
            
        } catch (HibernateException he){ 
            manejaExcepcion(he); 
            correcto = false;
        } finally { 
            sesion.close(); 
        } 
        
        return correcto;
    }
    
}
