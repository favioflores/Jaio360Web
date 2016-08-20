package com.jaio360.dao;

import com.jaio360.domain.RelacionEvaluadoEvaluador;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NotificacionesDAO implements Serializable{
    
    private static Log log = LogFactory.getLog(NotificacionesDAO.class);
    private Session sesion; 
    private Transaction tx;  

    private void iniciaOperacion() throws HibernateException { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  

    private void manejaExcepcion(HibernateException he) throws HibernateException { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 
    
    public List obtieneNotificaciones(Date dtFechaEntrega, Integer intLimiteNotificaciones) throws HibernateException { 
        
        iniciaOperacion();
        
        List lstMails = new ArrayList();

        Query query = sesion.createQuery("from Notificaciones where DATE_FORMAT(noFeCreacion, '%Y-%m-%d') <= DATE_FORMAT(?, '%Y-%m-%d') and noIdEstado = ? ");

        query.setDate(0, dtFechaEntrega); 
        query.setInteger(1, Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
        query.setMaxResults(intLimiteNotificaciones);
        
        List <Notificaciones> lstNotificaciones = query.list();
        
        if(!lstNotificaciones.isEmpty()){
        
            Iterator itLstNotificaciones = lstNotificaciones.iterator();
            
            DestinatariosDAO objDestinatariosDAO = new DestinatariosDAO();
            
            Notificaciones objNotificaciones;
            
            while(itLstNotificaciones.hasNext()){
            
                objNotificaciones = (Notificaciones) itLstNotificaciones.next();
                
                List<Destinatarios> lstDestinatarios = objDestinatariosDAO.obtieneDestinatarios(objNotificaciones.getNoIdNotificacionPk(), sesion);
                
                if(!lstDestinatarios.isEmpty()){
                
                    Object[] obj = {objNotificaciones, lstDestinatarios};
                    
                    lstMails.add(obj);
                    
                }
            
            }
            
        }
        
        tx.commit();
        sesion.close();
        
        return lstMails; 
    }  

    public boolean actualizaNotificacion(Notificaciones objNotificaciones) throws HibernateException { 
        
        try { 
            iniciaOperacion(); 
            sesion.update(objNotificaciones); 
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            return false;
        } finally { 
            sesion.close(); 
        } 
        
        return true;
    }  
        
    public Integer guardaNotificacion(Notificaciones objNotificaciones) throws HibernateException { 
        
        Integer id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = (Integer)sesion.save(objNotificaciones); 
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
    
    public boolean guardaNotificacionesEvaluados(List<Usuario> lstUsuariosGrabados) throws HibernateException, Exception { 
        
        Integer intIdProyecto       = Utilitarios.obtenerProyecto().getIntIdProyecto();
        Integer intIdMetodologia    = Utilitarios.obtenerProyecto().getIntIdMetodologia();
        String strDescEmpresa       = Utilitarios.obtenerUsuario().getStrDescripcion();
        String strEmailContacto     = Utilitarios.obtenerUsuario().getStrEmail();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        Mensaje objMensaje = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED);
        
        byte[] bdata = objMensaje.getMeTxCuerpo();
        String strNotificacion = Utilitarios.decodeUTF8(bdata);
        String strNotificacionPer;
        
        EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                
        try { 
            
            iniciaOperacion(); 
            
            for(Usuario objUsuario : lstUsuariosGrabados){
        
                strNotificacionPer = strNotificacion;
                strNotificacionPer = strNotificacionPer.replace("#%DATO.PROYECTO", Utilitarios.obtenerProyecto().getStrDescNombre());
                strNotificacionPer = strNotificacionPer.replace("#%DATO.MENSAJE", Utilitarios.formatearFecha(new Date(), Constantes.HH24_MI_DDMMYYYY));
                strNotificacionPer = strNotificacionPer.replace("#%USUARIO.MENSAJE", objUsuario.getUsIdMail());
                strNotificacionPer = strNotificacionPer.replace("#%CLAVE.MENSAJE", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));
                strNotificacionPer = strNotificacionPer.replace("#%DATO.CORREO", strEmailContacto);

                Notificaciones objNotificaciones = new Notificaciones();
                objNotificaciones.setNoFeCreacion(new Date());
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificaciones.setNoIdRefProceso(intIdProyecto);
                objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                objNotificaciones.setNoTxAsunto(objMensaje.getMeTxAsunto());
                objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));     
                objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));

                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail(objUsuario.getUsIdMail());
                objDestinatarios.setNotificaciones(objNotificaciones);

                sesion.save(objDestinatarios);
        
            }
        
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        }  

        return true; 
    }  
    

    public boolean guardaNotificacionesEvaluadosRecordatorio(List<Participante> lstEvaluados) throws HibernateException, Exception { 
        
        Integer intIdProyecto       = Utilitarios.obtenerProyecto().getIntIdProyecto();
        Integer intIdMetodologia    = Utilitarios.obtenerProyecto().getIntIdMetodologia();
        String strDescEmpresa       = Utilitarios.obtenerUsuario().getStrDescripcion();
        String strEmailContacto     = Utilitarios.obtenerUsuario().getStrEmail();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        Mensaje objMensaje = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED);
        
        byte[] bdata = objMensaje.getMeTxCuerpo();
        String strNotificacion = Utilitarios.decodeUTF8(bdata);
        String strNotificacionPer;
        
        EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                
        try { 
            
            iniciaOperacion(); 
            
            UsuarioDAO objUsuarioDAO = new UsuarioDAO();
                    
            for(Participante objEvaluado : lstEvaluados){
        
                if(objEvaluado.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION) && objEvaluado.getPaInRedCargada()==false){
                    Usuario objUsuario = objUsuarioDAO.iniciaSesion(objEvaluado.getPaTxCorreo());

                    strNotificacionPer = strNotificacion;
                    strNotificacionPer = strNotificacionPer.replace("#%DATO.PROYECTO", Utilitarios.obtenerProyecto().getStrDescNombre());
                    strNotificacionPer = strNotificacionPer.replace("#%DATO.MENSAJE", Utilitarios.formatearFecha(new Date(), Constantes.HH24_MI_DDMMYYYY));
                    strNotificacionPer = strNotificacionPer.replace("#%USUARIO.MENSAJE", objUsuario.getUsIdMail());
                    strNotificacionPer = strNotificacionPer.replace("#%CLAVE.MENSAJE", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));
                    strNotificacionPer = strNotificacionPer.replace("#%DATO.CORREO", strEmailContacto);

                    Notificaciones objNotificaciones = new Notificaciones();
                    objNotificaciones.setNoFeCreacion(new Date());
                    objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                    objNotificaciones.setNoIdRefProceso(intIdProyecto);
                    objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                    objNotificaciones.setNoTxAsunto(objMensaje.getMeTxAsunto());
                    objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacionPer));     
                    objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));

                    Destinatarios objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(objUsuario.getUsIdMail());
                    objDestinatarios.setNotificaciones(objNotificaciones);

                    sesion.save(objDestinatarios);
                }
        
            }
        
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        }  

        return true; 
    }  
    
    public boolean guardaNotificacionesEvaluadosRecordatorio(String[] strEvaluados) throws HibernateException, Exception { 
        
        Integer intIdProyecto       = Utilitarios.obtenerProyecto().getIntIdProyecto();
        Integer intIdMetodologia    = Utilitarios.obtenerProyecto().getIntIdMetodologia();
        String strDescEmpresa       = Utilitarios.obtenerUsuario().getStrDescripcion();
        String strEmailContacto     = Utilitarios.obtenerUsuario().getStrEmail();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        Mensaje objMensaje = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED);
        
        byte[] bdata = objMensaje.getMeTxCuerpo();
        String strNotificacion = Utilitarios.decodeUTF8(bdata);
        String strNotificacionPer;
        
        EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                
        try { 
            
            iniciaOperacion(); 
            
            UsuarioDAO objUsuarioDAO = new UsuarioDAO();
                    
            for(int i = 0; i < strEvaluados.length; i++){
        
                Usuario objUsuario = objUsuarioDAO.iniciaSesion(strEvaluados[i]);

                strNotificacionPer = strNotificacion;
                strNotificacionPer = strNotificacionPer.replace("#%DATO.PROYECTO", Utilitarios.obtenerProyecto().getStrDescNombre());
                strNotificacionPer = strNotificacionPer.replace("#%DATO.MENSAJE", Utilitarios.formatearFecha(new Date(), Constantes.HH24_MI_DDMMYYYY));
                strNotificacionPer = strNotificacionPer.replace("#%USUARIO.MENSAJE", objUsuario.getUsIdMail());
                strNotificacionPer = strNotificacionPer.replace("#%CLAVE.MENSAJE", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));
                strNotificacionPer = strNotificacionPer.replace("#%DATO.CORREO", strEmailContacto);

                Notificaciones objNotificaciones = new Notificaciones();
                objNotificaciones.setNoFeCreacion(new Date());
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificaciones.setNoIdRefProceso(intIdProyecto);
                objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                objNotificaciones.setNoTxAsunto(objMensaje.getMeTxAsunto());
                objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));     
                objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));

                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail(objUsuario.getUsIdMail());
                objDestinatarios.setNotificaciones(objNotificaciones);

                sesion.save(objDestinatarios);
        
            }
        
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        }  

        return true; 
    }  
    
    public boolean guardaNotificacionesEvaluadores(List<RelacionEvaluadoEvaluador> lstRelacionEvaluadoEvaluador) throws HibernateException, Exception { 
        
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();
        
        iniciaOperacion();
        
        boolean m = true;
        
        Integer intIdProyecto       = Utilitarios.obtenerProyecto().getIntIdProyecto();
        String strProyecto          = Utilitarios.obtenerProyecto().getStrDescNombre();
        Integer intIdMetodologia    = Utilitarios.obtenerProyecto().getIntIdMetodologia();
        String strEmailContacto     = Utilitarios.obtenerUsuario().getStrEmail();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        Mensaje objMensaje = objMensajeDAO.obtenMensaje(intIdProyecto, Constantes.INT_ET_NOTIFICACION_CONVOCATORIA, sesion); 
        
        byte[] bdata = objMensaje.getMeTxCuerpo();
        String strNotificacion = Utilitarios.decodeUTF8(bdata);
        String strNotificacionPer;
        
        EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                
        try { 
            Usuario objUsuario;
            Object obj;
                        
            for(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador){
                
                if(objRelacionEvaluadoEvaluador.getBlEnvioCorreo()){
        
                    obj = objUsuarioDAO.obtenUsuario(objRelacionEvaluadoEvaluador.getStrCorreoEvaluador(), sesion);

                    if(obj!=null){
                        objUsuario = (Usuario) obj;
                        strNotificacionPer = strNotificacion;
                        strNotificacionPer = strNotificacionPer.replace("#%DATO.PROYECTO", strProyecto);
                        strNotificacionPer = strNotificacionPer.replace("#%DATO.MENSAJE", Utilitarios.formatearFecha(new Date(), Constantes.HH24_MI_DDMMYYYY));
                        strNotificacionPer = strNotificacionPer.replace("#%USUARIO.MENSAJE", objRelacionEvaluadoEvaluador.getStrCorreoEvaluador());
                        strNotificacionPer = strNotificacionPer.replace("#%CLAVE.MENSAJE", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));
                        strNotificacionPer = strNotificacionPer.replace("#%DATO.CORREO", strEmailContacto);

                        Notificaciones objNotificaciones = new Notificaciones();
                        objNotificaciones.setNoFeCreacion(new Date());
                        objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                        objNotificaciones.setNoIdRefProceso(intIdProyecto);
                        objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                        objNotificaciones.setNoTxAsunto(objMensaje.getMeTxAsunto());
                        objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacionPer));
                        objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));

                        Destinatarios objDestinatarios = new Destinatarios();
                        objDestinatarios.setDeTxMail(objRelacionEvaluadoEvaluador.getStrCorreoEvaluador());
                        objDestinatarios.setNotificaciones(objNotificaciones);

                        sesion.save(objDestinatarios);

                    }else{
                        tx.rollback();
                        return false;
                    }
                }
            }
        
            tx.commit();
            
        } catch (HibernateException he) { 
            tx.rollback();
            log.error(he);
            m = false;
        }

        return m; 
    } 
    
    
    public boolean guardarmeComunicados(List <String> lstCorreosAdicionales) throws HibernateException, Exception { 
        
        iniciaOperacion();
                
        boolean m = true;
        
        Integer intIdProyecto       = Utilitarios.obtenerProyecto().getIntIdProyecto();
        Integer intIdMetodologia    = Utilitarios.obtenerProyecto().getIntIdMetodologia();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        try { 
                    
            Mensaje objMensaje = objMensajeDAO.obtenMensaje(intIdProyecto, Constantes.INT_ET_NOTIFICACION_CONVOCATORIA, sesion); 

            if(objMensaje!=null){
                byte[] bdata = objMensaje.getMeTxCuerpo();
                String strNotificacion = Utilitarios.decodeUTF8(bdata);
                Notificaciones objNotificaciones = new Notificaciones();
                objNotificaciones.setNoFeCreacion(new Date());
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificaciones.setNoIdRefProceso(intIdProyecto);
                objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                objNotificaciones.setNoTxAsunto(objMensaje.getMeTxAsunto());
                objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));     
                objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));
                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail(Utilitarios.obtenerUsuario().getStrEmail());
                objDestinatarios.setNotificaciones(objNotificaciones);
                sesion.save(objDestinatarios);
                
                for(String strCorreo : lstCorreosAdicionales){
                    objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(strCorreo);
                    objDestinatarios.setNotificaciones(objNotificaciones);
                    sesion.save(objDestinatarios);                    
                }
                
            }

            objMensaje = objMensajeDAO.obtenMensaje(intIdProyecto, Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES, sesion); 

            if(objMensaje!=null){
                byte[] bdata = objMensaje.getMeTxCuerpo();
                String strNotificacion = Utilitarios.decodeUTF8(bdata);
                Notificaciones objNotificaciones = new Notificaciones();
                objNotificaciones.setNoFeCreacion(new Date());
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificaciones.setNoIdRefProceso(intIdProyecto);
                objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                objNotificaciones.setNoTxAsunto("Vista previa de instrucciones");
                objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));     
                objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));
                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail(Utilitarios.obtenerUsuario().getStrEmail());
                objDestinatarios.setNotificaciones(objNotificaciones);
                sesion.save(objDestinatarios);
                
                for(String strCorreo : lstCorreosAdicionales){
                    objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(strCorreo);
                    objDestinatarios.setNotificaciones(objNotificaciones);
                    sesion.save(objDestinatarios);                    
                }
                
            }

            objMensaje = objMensajeDAO.obtenMensaje(intIdProyecto, Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO, sesion); 

            if(objMensaje!=null){
                byte[] bdata = objMensaje.getMeTxCuerpo();
                String strNotificacion = Utilitarios.decodeUTF8(bdata);
                Notificaciones objNotificaciones = new Notificaciones();
                objNotificaciones.setNoFeCreacion(new Date());
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificaciones.setNoIdRefProceso(intIdProyecto);
                objNotificaciones.setNoIdTipoProceso(intIdMetodologia);
                objNotificaciones.setNoTxAsunto("Vista previa de agradecimiento");
                objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));     
                objNotificaciones.setNoIdNotificacionPk((Integer) sesion.save(objNotificaciones));
                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail(Utilitarios.obtenerUsuario().getStrEmail());
                objDestinatarios.setNotificaciones(objNotificaciones);
                sesion.save(objDestinatarios);
                
                for(String strCorreo : lstCorreosAdicionales){
                    objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(strCorreo);
                    objDestinatarios.setNotificaciones(objNotificaciones);
                    sesion.save(objDestinatarios);                    
                }
                
            }

            tx.commit();

        } catch (HibernateException he) { 
            log.error(he);
            m = false;
        }    
        
        return m; 
    } 

}
