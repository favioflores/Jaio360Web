package com.jaio360.view;

import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "redactarMensajesView")
@ViewScoped
public class RedactarMensajesView implements Serializable{
    private static final long serialVersionUID = -1L;
    
    private static Log log = LogFactory.getLog(RedactarMensajesView.class);
    
    private boolean blConvocatoria = false;
    private boolean blInstrucciones = false;
    private boolean blAgradecimiento = false;
    private boolean blProyectoTerminado = false;
    private Integer intIdEstadoProyecto;
    private String strPreview;
    private String correoExtra;
    private List<String> lstCorreosExtra;

    public String getCorreoExtra() {
        return correoExtra;
    }

    public void setCorreoExtra(String correoExtra) {
        this.correoExtra = correoExtra;
    }

    public List<String> getLstCorreosExtra() {
        return lstCorreosExtra;
    }

    public void setLstCorreosExtra(List<String> lstCorreosExtra) {
        this.lstCorreosExtra = lstCorreosExtra;
    }

    
    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }
    
    public String getStrPreview() {
        return strPreview;
    }

    public void setStrPreview(String strPreview) {
        this.strPreview = strPreview;
    }

    public boolean isBlProyectoTerminado() {
        return blProyectoTerminado;
    }

    public void setBlProyectoTerminado(boolean blProyectoTerminado) {
        this.blProyectoTerminado = blProyectoTerminado;
    }

    
    public boolean isBlConvocatoria() {
        return blConvocatoria;
    }

    public void setBlConvocatoria(boolean blConvocatoria) {
        this.blConvocatoria = blConvocatoria;
    }

    public boolean isBlInstrucciones() {
        return blInstrucciones;
    }

    public void setBlInstrucciones(boolean blInstrucciones) {
        this.blInstrucciones = blInstrucciones;
    }

    public boolean isBlAgradecimiento() {
        return blAgradecimiento;
    }

    public void setBlAgradecimiento(boolean blAgradecimiento) {
        this.blAgradecimiento = blAgradecimiento;
    }

        
    public void notiticacionCreada() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Creación de notificacion", "La notificación se guardo exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void abrirPanelComunicados(Integer intIdTipoNotificacion, Integer recordatorio) {
        
        Map<String,Object> options = new HashMap();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentWidth", 600);
        //options.put("style", "width: auto !important");
        
        Map<String,List<String>> params = new HashMap();
        List<String> list1 = new ArrayList();
        list1.add(intIdTipoNotificacion.toString());
        List<String> list2 = new ArrayList();
        list2.add(recordatorio.toString());
        params.put("strTipoNotificacion", list1);
        params.put("strRecordatorio", list2);
            
        RequestContext.getCurrentInstance().openDialog("crearComunicacion", options, params);
    }
    
    @PostConstruct
    public void init() {
        
        lstCorreosExtra = new ArrayList<String>();
        strPreview = Constantes.strVacio;
        
        Integer intIdProyecto = Utilitarios.obtenerProyecto().getIntIdProyecto();
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        List<Mensaje> lstMensajes = objMensajeDAO.obtenListaMensaje(intIdProyecto);
                
        for(Mensaje objMensaje : lstMensajes){
            if(objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA)){
                blConvocatoria = true;
            }else if(objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES)){
                blInstrucciones = true;
            }else if(objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO)){
                blAgradecimiento = true;
            }
        }
        
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(intIdProyecto);
        
        if(objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO)){
            blProyectoTerminado = true;
        }
        
        intIdEstadoProyecto = objProyecto.getPoIdEstado();

    }
    
    public void preview(Integer intTipoNotificacion){
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        
        List<Mensaje> lstMensajes = objMensajeDAO.obtenListaMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto());
                
        for(Mensaje objMensaje : lstMensajes){
            if(objMensaje.getMeIdTipoMensaje().equals(intTipoNotificacion)){
                byte[] bdata = objMensaje.getMeTxCuerpo();
                strPreview = Utilitarios.decodeUTF8(bdata); 
            }
        }

    }
    
    public void enviarmeMensajeria(){

        try{
        
            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
            if(objNotificacionesDAO.guardarmeComunicados(lstCorreosExtra)){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Envio de mensajes", "Se enviaron las notificaciones al correo "+Utilitarios.obtenerUsuario().getStrEmail());
                FacesContext.getCurrentInstance().addMessage(null, message);    
            }else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Envio de mensajes", "No esta disponible el envio de correos. Por favor comunicate con el administrador");
                FacesContext.getCurrentInstance().addMessage(null, message);    
            }
        }catch(Exception e){
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Envio de mensajes", "Ocurrio un error inesperado. Por favor comunicate con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, message);    
        }
        
    }

    
    public void agregaCorreoExtra(){
        correoExtra = correoExtra.toLowerCase();
        if(!lstCorreosExtra.contains(correoExtra)){
            lstCorreosExtra.add(correoExtra);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar correo", "Correo agregado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);   
            correoExtra = "";
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Agregar correo", "El correo ingresado ya existe en la lista");
            FacesContext.getCurrentInstance().addMessage(null, message);                
        }
    }
    
    public void eliminarCorreoExtra(String correoExtra){
        lstCorreosExtra.remove(correoExtra);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar correo", "Correo se eliminó correctamente");
        FacesContext.getCurrentInstance().addMessage(null, message);            
    }
}

