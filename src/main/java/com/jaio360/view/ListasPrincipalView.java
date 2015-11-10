/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Proyecto;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "listasPrincipalView")
@ViewScoped
public class ListasPrincipalView implements Serializable{
    
    private static Log log = LogFactory.getLog(ListasPrincipalView.class);
    
    private static final long serialVersionUID = -1L;
    
    private List<ProyectoInfo> lstProyectos;
    
    private List<ProyectoInfo> filtroProyectos;
    
    private List<ProyectoInfo> lstEvaluaciones;
    
    private List<ProyectoInfo> lstRedes;
    
    private int cantidadLstProyectos;
    private int cantidadLstEvaluaciones;
    private int cantidadLstRedes;
    
    private ProyectoInfo proyectoSeleccionado;
    private ProyectoInfo redSeleccionada;
    private ProyectoInfo evaluacionSeleccionada;

    public ProyectoInfo getRedSeleccionada() {
        return redSeleccionada;
    }

    public void setRedSeleccionada(ProyectoInfo redSeleccionada) {
        this.redSeleccionada = redSeleccionada;
    }

    public ProyectoInfo getEvaluacionSeleccionada() {
        return evaluacionSeleccionada;
    }

    public void setEvaluacionSeleccionada(ProyectoInfo evaluacionSeleccionada) {
        this.evaluacionSeleccionada = evaluacionSeleccionada;
    }

    public ListasPrincipalView() {
        this.lstProyectos = new ArrayList<>();
        this.lstRedes = new ArrayList<>();
        this.lstEvaluaciones = new ArrayList<>();
    }

    public List<ProyectoInfo> getLstProyectos() {
        return lstProyectos;
    }

    public void setLstProyectos(List<ProyectoInfo> lstProyectos) {
        this.lstProyectos = lstProyectos;
    }

    public int getCantidadLstProyectos() {
        return cantidadLstProyectos;
    }

    public void setCantidadLstProyectos(int cantidadLstProyectos) {
        this.cantidadLstProyectos = cantidadLstProyectos;
    }

    public int getCantidadLstEvaluaciones() {
        return cantidadLstEvaluaciones;
    }

    public void setCantidadLstEvaluaciones(int cantidadLstEvaluaciones) {
        this.cantidadLstEvaluaciones = cantidadLstEvaluaciones;
    }

    public int getCantidadLstRedes() {
        return cantidadLstRedes;
    }

    public void setCantidadLstRedes(int cantidadLstRedes) {
        this.cantidadLstRedes = cantidadLstRedes;
    }

    public ProyectoInfo getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(ProyectoInfo proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public List<ProyectoInfo> getFiltroProyectos() {
        return filtroProyectos;
    }

    public void setFiltroProyectos(List<ProyectoInfo> filtroProyectos) {
        this.filtroProyectos = filtroProyectos;
    }

    public List<ProyectoInfo> getLstEvaluaciones() {
        return lstEvaluaciones;
    }

    public void setLstEvaluaciones(List<ProyectoInfo> lstEvaluaciones) {
        this.lstEvaluaciones = lstEvaluaciones;
    }

    public List<ProyectoInfo> getLstRedes() {
        return lstRedes;
    }

    public void setLstRedes(List<ProyectoInfo> lstRedes) {
        this.lstRedes = lstRedes;
    }
    
    @PostConstruct
    public void init() {
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("proyectoInfo");
        
        UsuarioSesion objUsuarioSesion = new UsuarioSesion();
        
        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();
        
        poblarListaProyectos(objUsuarioInfo);
        poblarListaRedes(objUsuarioInfo);
        poblarListaEvaluaciones(objUsuarioInfo);
        
        if(!lstProyectos.isEmpty()){
            this.cantidadLstProyectos = this.lstProyectos.size();
        }
        if(!lstRedes.isEmpty()){
            this.cantidadLstRedes = this.lstRedes.size();
        }
        if(!lstEvaluaciones.isEmpty()){
            this.cantidadLstEvaluaciones = this.lstEvaluaciones.size();
        }
    }
    
    public void cargarProyecto(ProyectoInfo obj){
    
        try {
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("proyectoInfo"); 
            
            if(proyectoSeleccionado==null){
                session.setAttribute("proyectoInfo", obj);
            }else{
                session.setAttribute("proyectoInfo", proyectoSeleccionado);
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("resumenProyecto.jsf");
            
        } catch (IOException ex) {
            log.debug(ex);
        }
        
    }

    private void poblarListaProyectos(UsuarioInfo objUsuarioInfo) {

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstProyecto = objProyectoDAO.obtenListaProyectosPorUsuario(objUsuarioInfo.getIntUsuarioPk(), null);
        
        if(!lstProyecto.isEmpty()){
            
            List<ProyectoInfo> lstProyectos = new ArrayList<>();
            
            Iterator itLstProyectos = lstProyecto.iterator();
            
            ProyectoInfo objProyectoInfo = new ProyectoInfo();
                    
            while(itLstProyectos.hasNext()){
                
                Proyecto objProyecto = (Proyecto) itLstProyectos.next();
                
                lstProyectos.add(setearProyecto(objProyecto, objProyectoInfo));
            }
            
            this.lstProyectos = lstProyectos;
        }
  
    }

    private void poblarListaRedes(UsuarioInfo objUsuarioInfo) {
        
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstRed = objProyectoDAO.obtenListaRedesPorUsuario(objUsuarioInfo.getStrEmail());
        
        if(!lstRed.isEmpty()){
            
            List<ProyectoInfo> lstRedes = new ArrayList<>();
            
            Iterator itLstRedes = lstRed.iterator();
            
            ProyectoInfo objProyectoInfo;
            
            //EHCacheManager objEHCacheManager = new EHCacheManager();
                    
            while(itLstRedes.hasNext()){
                
                Proyecto objProyecto = (Proyecto) itLstRedes.next();
                objProyectoInfo = new ProyectoInfo();
                
                objProyectoInfo.setIntIdProyecto(objProyecto.getPoIdProyectoPk());
                objProyectoInfo.setStrDescNombre(objProyecto.getPoTxDescripcion());
                objProyectoInfo.setStrMotivo(objProyecto.getPoTxMotivo());
                /*
                objProyectoInfo.setIntIdMetodologia(objProyecto.getPoIdMetodologia());
                objProyectoInfo.setStrDescMetodologia(objEHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdMetodologia()));
                objProyectoInfo.setIntIdEstado(objProyecto.getPoIdEstado());
                objProyectoInfo.setStrDescEstado(objEHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdEstado()));
                objProyectoInfo.setDtFechaCreacion(objProyecto.getPoFeRegistro());*/
                
                lstRedes.add(objProyectoInfo);
            }
            
            this.lstRedes = lstRedes;
        }
    }

    private void poblarListaEvaluaciones(UsuarioInfo objUsuarioInfo) {
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstEvaluacion = objProyectoDAO.obtenListaEvaluacionesPorUsuario(objUsuarioInfo.getStrEmail());
        
        if(!lstEvaluacion.isEmpty()){
            
            List<ProyectoInfo> lstEvaluaciones = new ArrayList<>();
            
            Iterator itLstEvaluaciones = lstEvaluacion.iterator();
            
            ProyectoInfo objProyectoInfo;
            
            //EHCacheManager objEHCacheManager = new EHCacheManager();
                    
            while(itLstEvaluaciones.hasNext()){
                
                Object[] objProyecto = (Object[]) itLstEvaluaciones.next();
                objProyectoInfo = new ProyectoInfo();
                
                objProyectoInfo.setIntIdProyecto((Integer) objProyecto[0]);
                objProyectoInfo.setStrDescNombre((String) objProyecto[2]);
                objProyectoInfo.setStrDescEvaluado((String) objProyecto[8]); 
                objProyectoInfo.setStrDescCuestionario((String) objProyecto[9]); 
                objProyectoInfo.setIntIdCuestionario((Integer)objProyecto[10]);
                objProyectoInfo.setIntIdEvaluado((Integer)objProyecto[11]); 
                objProyectoInfo.setStrCorreoEvaluado((String)objProyecto[12]);
                
                /*
                objProyectoInfo.setIntIdMetodologia(objProyecto[6]);
                objProyectoInfo.setStrDescMetodologia(objEHCacheManager.obtenerDescripcionElemento(objProyecto[6]));
                objProyectoInfo.setIntIdEstado(objProyecto[3]);
                objProyectoInfo.setStrDescEstado(objEHCacheManager.obtenerDescripcionElemento(objProyecto[3]));
                objProyectoInfo.setDtFechaCreacion(objProyecto[4]);*/
                
                lstEvaluaciones.add(objProyectoInfo);
            }
            
            this.lstEvaluaciones = lstEvaluaciones;
        }
    }
    
    public void irRed(ProyectoInfo objProyectoInfo){
        
        try {
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("redInfo");
            session.setAttribute("redInfo", objProyectoInfo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("defineRed.jsf");
            
        } catch (IOException ex) {
            log.debug(ex);
        }
        
    }
    
    public static ProyectoInfo setearProyecto(Proyecto objProyecto, ProyectoInfo objProyectoInfo){
    
        objProyectoInfo = new ProyectoInfo();
        
        objProyectoInfo.setIntIdProyecto(objProyecto.getPoIdProyectoPk());
        objProyectoInfo.setStrDescNombre(objProyecto.getPoTxDescripcion());
        objProyectoInfo.setIntIdMetodologia(objProyecto.getPoIdMetodologia());
        objProyectoInfo.setStrDescMetodologia(EHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdMetodologia()));
        objProyectoInfo.setIntIdEstado(objProyecto.getPoIdEstado());
        objProyectoInfo.setStrDescEstado(EHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdEstado()));
        objProyectoInfo.setDtFechaCreacion(objProyecto.getPoFeRegistro());
        objProyectoInfo.setDtFechaEjecucion(objProyecto.getPoFeEjecucion());
        objProyectoInfo.setStrMotivo(objProyecto.getPoTxMotivo());
        
        return objProyectoInfo;
    
    }
    
    public void irEvaluacion(ProyectoInfo objProyectoInfo){
        
        try {
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");
            session.setAttribute("evalInfo", objProyectoInfo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("ejecutarEvaluacion.jsf");
            
        } catch (IOException ex) {
            log.debug(ex);
        }
        
    }

    public void borrarProyecto(Integer intIdProyecto){
    
        try {
            
            ProyectoDAO objProyectoDAO = new ProyectoDAO();
            
            objProyectoDAO.eliminaProyecto(intIdProyecto);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Borrar proyecto", "El proyecto se eliminó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
            init();
            
        } catch (Exception ex) {
            log.error(ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Borrar proyecto", "Ocurrio un error al realizar esta accion. Por favor comunicate con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    
    
    }
    
        
    public void actualizarProyecto(RowEditEvent event) {
        
        try{
            ProyectoInfo objProyectoInfo = (ProyectoInfo) event.getObject();

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

            objProyecto.setPoTxDescripcion(objProyectoInfo.getStrDescNombre());
            objProyecto.setPoIdMetodologia(objProyectoInfo.getIntIdMetodologia());        
            objProyecto.setPoTxMotivo(objProyectoInfo.getStrMotivo());

            objProyectoDAO.actualizaProyecto(objProyecto);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificación de proyecto", objProyecto.getPoTxDescripcion() + " " + objProyecto.getPoIdMetodologia());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            init();
        }catch(Exception e){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificación de proyecto", "Ocurrio un error en la actualización");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
