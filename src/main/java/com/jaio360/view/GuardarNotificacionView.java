package com.jaio360.view;

import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;


import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "guardarNotificacionView")
@ViewScoped


public class GuardarNotificacionView extends BaseView implements Serializable {
    private static final long serialVersionUID = -1L;
    
    private static Logger log = Logger.getLogger(GuardarNotificacionView.class);
    
    private String strIdNotificacion;
    private String strRecordatorio;
    private String strTipoNotificacion;
    private String[] evaluados;
    private String strDescTipoNotificacion;
    private String strAsunto;
    private String strContenido;

    public String getStrIdNotificacion() {
        return strIdNotificacion;
    }

    public void setStrIdNotificacion(String strIdNotificacion) {
        this.strIdNotificacion = strIdNotificacion;
    }

    public String getStrRecordatorio() {
        return strRecordatorio;
    }

    public void setStrRecordatorio(String strRecordatorio) {
        this.strRecordatorio = strRecordatorio;
    }

    public String getStrTipoNotificacion() {
        return strTipoNotificacion;
    }

    public void setStrTipoNotificacion(String strTipoNotificacion) {
        this.strTipoNotificacion = strTipoNotificacion;
    }

    public String[] getEvaluados() {
        return evaluados;
    }

    public void setEvaluados(String[] evaluados) {
        this.evaluados = evaluados;
    }

    public String getStrDescTipoNotificacion() {
        return strDescTipoNotificacion;
    }

    public void setStrDescTipoNotificacion(String strDescTipoNotificacion) {
        this.strDescTipoNotificacion = strDescTipoNotificacion;
    }

    public String getStrAsunto() {
        return strAsunto;
    }

    public void setStrAsunto(String strAsunto) {
        this.strAsunto = strAsunto;
    }

    public String getStrContenido() {
        return strContenido;
    }

    public void setStrContenido(String strContenido) {
        this.strContenido = strContenido;
    }
    

    @PostConstruct
    public void init() {
       
        Map<String, String[]> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();

        String strRecor[] = params.get("strRecordatorio");
        String strNotif[] = params.get("strTipoNotificacion");
        String strEvalu[] = params.get("evaluados");
        
        strRecordatorio = strRecor[0];
        strTipoNotificacion = strNotif[0];
        evaluados = strEvalu;
            
        if(strTipoNotificacion!=null){
            
            strDescTipoNotificacion = msg(strTipoNotificacion);

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

            MensajeDAO objMensajeDAO = new MensajeDAO();
            Mensaje objMensaje = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Integer.parseInt(strTipoNotificacion));

            if(objMensaje != null){
                strIdNotificacion = objMensaje.getMeIdMensajePk().toString();
                strAsunto = objMensaje.getMeTxAsunto();
                byte[] bdata = objMensaje.getMeTxCuerpo();
                strContenido = Utilitarios.decodeUTF8(bdata);
            }else{
                //Obtiene el mensaje modelo
                if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA.toString())){
                    strContenido = msg(Constantes.INT_ET_MODELO_NOTIFICACION_CONVOCATORIA.toString());
                    strAsunto = Constantes.strVacio;
                    strIdNotificacion = Constantes.strVacio;
                }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED.toString())){
                    strContenido = msg(Constantes.INT_ET_MODELO_NOTIFICACION_CARGA_RED.toString());
                    strAsunto = Constantes.strVacio;
                    strIdNotificacion = Constantes.strVacio;
                }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO.toString())){
                    strContenido = Constantes.strVacio;
                    strAsunto = Constantes.strVacio;
                    strIdNotificacion = Constantes.strVacio;
                }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES.toString())){
                    strContenido = Constantes.strVacio;
                    strAsunto = Constantes.strVacio;
                    strIdNotificacion = Constantes.strVacio;
                }
                
            }

        }
        
    }
        
    public void guardarNotificacion() {
        
        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();
        
        try{
            
            if(contenidoValido()){
            
                MensajeDAO objMensajeDAO = new MensajeDAO();

                if(Utilitarios.esNuloOVacio(strIdNotificacion)){

                    //Graba la notificacion nueva
                    Proyecto objProyecto = new Proyecto();
                    objProyecto.setPoIdProyectoPk(objProyectoInfo.getIntIdProyecto());

                    Mensaje objMensaje = new Mensaje();
                    objMensaje.setMeIdTipoMensaje(Integer.parseInt(strTipoNotificacion));
                    objMensaje.setMeTxAsunto(strAsunto);
                    objMensaje.setMeTxCuerpo(Utilitarios.encodeUTF8(strContenido));
                    objMensaje.setProyecto(objProyecto);

                    objMensajeDAO.guardaMensaje(objMensaje);

                }else{

                    //Actualiza la notificacion
                    Proyecto objProyecto = new Proyecto();
                    objProyecto.setPoIdProyectoPk(objProyectoInfo.getIntIdProyecto());

                    Mensaje objMensaje = new Mensaje();
                    objMensaje.setMeIdMensajePk(Integer.parseInt(strIdNotificacion));
                    objMensaje.setMeIdTipoMensaje(Integer.parseInt(strTipoNotificacion));
                    objMensaje.setMeTxAsunto(strAsunto);
                    objMensaje.setMeTxCuerpo(Utilitarios.encodeUTF8(strContenido));
                    
                    //log.error("Prueba1"+strContenido);
                    //log.error("Prueba2"+new String(strContenido.getBytes()));
                    //String prueba = new String(objMensaje.getMeTxCuerpo());
                    //log.error("Prueba3"+prueba);
                    
                    objMensaje.setProyecto(objProyecto);

                    objMensajeDAO.actualizaMensaje(objMensaje);

                }
                
                /* Continua el proceso de envio de red o ejecución */
                
                //Obtiene el mensaje modelo
                if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED.toString())){
                    
                    if(strRecordatorio.equals("0")){
                        /*Inicia el proceso*/
                        //SeguimientoRedView objSeguimientoRedView = new SeguimientoRedView();
                        //objSeguimientoRedView.iniciarRed();
                    }else{
                        //ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
                        
                        //List<Participante> lstParticipante = objParticipanteDAO.obtenListaParticipanteXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION, null);
                        
                        NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
                        try {
                            objNotificacionesDAO.guardaNotificacionesEvaluadosRecordatorio(evaluados);
                        } catch (Exception ex) {
                            log.error(ex);
                        }
                    }
                
                }
                
                cerrarPanel();
            
            }else{
            
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al grabar mensaje", "No tiene un contenido válido");
                FacesContext.getCurrentInstance().addMessage(null, message);
            
            }
            
        }catch(NumberFormatException | HibernateException e){
            log.error(e);
        }  
        
    }
    
    public void cerrarPanel(){
        PrimeFaces.current().dialog().closeDynamic("crearNotificacion");
    }

    private Boolean  contenidoValido() {
        
        if(Utilitarios.esNuloOVacio(strContenido)){
        
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar mensaje", "El contenido no puede estar vacio");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;
        }
        
        if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA.toString())){
            
            String temp = strContenido.trim();
            
            if(!(temp.contains("#%USUARIO.MENSAJE")&&
                 temp.contains("#%CLAVE.MENSAJE"))){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar mensaje", "Debes colocar todos los TAGs en el contenido del mensaje");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return false;
            }
        }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA_RED.toString())){
            String temp = strContenido.trim();
            
            if(!(temp.contains("#%USUARIO.MENSAJE")&&
                 temp.contains("#%CLAVE.MENSAJE"))){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar mensaje", "Debes colocar todos los TAGs en el contenido del mensaje");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return false;
            }
        }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO.toString())){

        }else if(strTipoNotificacion.equals(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES.toString())){
            
        }
        
        return true;
    }
    
}

