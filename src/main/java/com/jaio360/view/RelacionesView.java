package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.RelacionBean;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "relacionesView")
@ViewScoped
public class RelacionesView implements Serializable{
    
    private static Log log = LogFactory.getLog(RelacionesView.class); 
    
    private static final long serialVersionUID = -1L;
    
    private String strNombre;
    private String strAbreviatura;
    private String strDescripcion;
    private String strColor;
    private Integer idRelacionPk;
    
    private Integer intIdEstadoProyecto;

    private List<RelacionBean> lstRelacionBean;

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }
    
    public static Log getLog() {
        return log;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }
        
    public static void setLog(Log log) {
        RelacionesView.log = log;
    }

    public String getStrAbreviatura() {
        return strAbreviatura;
    }

    public void setStrAbreviatura(String strAbreviatura) {
        this.strAbreviatura = strAbreviatura;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    public Integer getIdRelacionPk() {
        return idRelacionPk;
    }

    public void setIdRelacionPk(Integer idRelacionPk) {
        this.idRelacionPk = idRelacionPk;
    }

    public List<RelacionBean> getLstRelacionBean() {
        return lstRelacionBean;
    }

    public void setLstRelacionBean(List<RelacionBean> lstRelacionBean) {
        this.lstRelacionBean = lstRelacionBean;
    }
           
    public RelacionesView(){
        this.lstRelacionBean = new ArrayList();
        //this.resetFail();
    }
        
    
    @PostConstruct
    public void init() {
       
        this.lstRelacionBean = new ArrayList();

        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        
        Proyecto proyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());
                
        intIdEstadoProyecto = proyecto.getPoIdEstado();

        RelacionDAO objRelacionDAO = new RelacionDAO();

        List<Relacion> lstRelacion = objRelacionDAO.obtenListaRelacionPorProyecto(proyecto);

        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        
        for (Relacion obj: lstRelacion){  

            RelacionBean objRelacionBean = new RelacionBean();
            objRelacionBean.setStrNombre(obj.getReTxNombre());
            objRelacionBean.setStrAbreviatura(obj.getReTxAbreviatura());
            objRelacionBean.setStrColor(obj.getReColor());
            objRelacionBean.setStrDescripcion(obj.getReTxDescripcion());
            objRelacionBean.setIdRelacionPk(obj.getReIdRelacionPk());
            objRelacionBean.setIntIdEstado(obj.getReIdEstado());
            objRelacionBean.setStrEstado(EHCacheManager.obtenerDescripcionElemento(obj.getReIdEstado()));
            
            objRelacionBean.setIntCantidadUso(objRelacionParticipanteDAO.existeRelacionesXRelacion(obj.getReIdRelacionPk()));

            lstRelacionBean.add(objRelacionBean);
        }

    }
    
    public FacesMessage buscarLista(String strNombre, String strAbreviatura, String strDescripcion, String strColor ){
        
        FacesMessage message = null;
        
        String strMensaje = Constantes.strVacio;
         
        String strNom = Utilitarios.retirarEspacios(strNombre);
        String strAbr = Utilitarios.retirarEspacios(strAbreviatura);
        String strDes = Utilitarios.retirarEspacios(strDescripcion);
        
        if(strNom.isEmpty() || strAbr.isEmpty() || strDes.isEmpty()){
            return new FacesMessage(FacesMessage.SEVERITY_ERROR,"Agregar relacion","No se pueden agregar valores vacios");
        }
        
        for (RelacionBean strRel : lstRelacionBean){  
            
            if(Utilitarios.retirarEspacios(strRel.getStrNombre()).toUpperCase().equals(strNom.toUpperCase())){
                strMensaje = "El nombre de la relacion esta repetido.";
                break;
            }else if(Utilitarios.retirarEspacios(strRel.getStrAbreviatura()).toUpperCase().equals(strAbr.toUpperCase())){
                strMensaje = "La abreviatura de la relacion esta repetida.";
                break;
            }else if(Utilitarios.retirarEspacios(strRel.getStrDescripcion()).toUpperCase().equals(strDes.toUpperCase())){
                strMensaje = "La descripción de la relacion esta repetida.";
                break;
            }else if(strRel.getStrColor().equals(strColor.trim())){
                strMensaje = "El color de la relacion esta repetido.";
                break;
            }
                
        }  
    
        if(!strMensaje.isEmpty()){
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fallo!",strMensaje);
        }
        
        return message;
    }
    
    
    public void eliminarLista(RelacionBean objRelacionBean){
        
        FacesMessage message;
        
        RelacionDAO objRelacionDAO = new RelacionDAO();
        
        if(objRelacionDAO.borraRelacion(objRelacionBean.getIdRelacionPk())){
            this.lstRelacionBean.remove(objRelacionBean);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Se eliminó correctamente");
        }else{
            message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Ocurrió un error al eliminar");
        }
        
        init();
        
        FacesContext.getCurrentInstance().addMessage(null,message);
                
    }
    
    public void guardarRelacion(){
        
        FacesMessage message;

        try{
            
            message = buscarLista(this.strNombre, this.strAbreviatura, this.strDescripcion, this.strColor );
        
            if(message==null){

                RelacionBean relacionBean = new RelacionBean();

                relacionBean.setStrNombre(this.strNombre.trim());
                relacionBean.setStrAbreviatura(this.strAbreviatura.trim());
                relacionBean.setStrDescripcion(this.strDescripcion.trim());
                relacionBean.setStrColor(this.strColor.trim());        
                relacionBean.setIntIdEstado(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO);
                relacionBean.setStrEstado(EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO));

                RelacionDAO objRelacionDAO = new RelacionDAO();

                Relacion objRelacion = crearRelacion(relacionBean);

                objRelacion.setReIdRelacionPk(objRelacionDAO.guardaRelacion(objRelacion));

                this.lstRelacionBean.add(relacionBean);
                
                resetFail();
                init();
                
                message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Se guardo correctamente");

            }
            
        }catch(Exception e){
            log.error(e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fallo!","Ocurrio un error al guardar la Relación");
        }   
        
        FacesContext.getCurrentInstance().addMessage(null,message);
        
    }
    
    private Relacion crearRelacion(RelacionBean relacionBean){
    
        Proyecto objProyecto = new Proyecto();
        
        objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        Relacion objRelacion = new Relacion();
        objRelacion.setReTxNombre(relacionBean.getStrNombre());
        objRelacion.setReTxAbreviatura(relacionBean.getStrAbreviatura());
        objRelacion.setReTxDescripcion(relacionBean.getStrDescripcion());
        objRelacion.setReColor(relacionBean.getStrColor());
        objRelacion.setReIdEstado(relacionBean.getIntIdEstado());
        objRelacion.setProyecto(objProyecto);
        
        return objRelacion;
    }
    
    public void resetFail() {
        this.strNombre = null;
        this.strAbreviatura = null;
        this.strDescripcion = null;
        this.strColor = null;
        this.idRelacionPk = null;
    }
    
    
}
