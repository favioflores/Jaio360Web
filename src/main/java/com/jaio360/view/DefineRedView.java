/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RedEvaluacionDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.domain.EvaluadorRelacion;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Participante;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 *
 * @author Favio
 */
@ManagedBean(name = "defineRedView")
@ViewScoped
public class DefineRedView extends BaseView implements Serializable{
    
    private static final Log log = LogFactory.getLog(DefineRedView.class);
    
    private static final long serialVersionUID = -1L;
    
    private List<EvaluadorRelacion> lstRed;
    
    private List<EvaluadorRelacion> filtroRed;
    
    private List<Relacion> lstRelaciones;
    
    private boolean isArtificio;

    public boolean isIsArtificio() {
        return isArtificio;
    }

    public void setIsArtificio(boolean isArtificio) {
        this.isArtificio = isArtificio;
    }

    public List<Relacion> getLstRelaciones() {
        return lstRelaciones;
    }

    public void setLstRelaciones(List<Relacion> lstRelaciones) {
        this.lstRelaciones = lstRelaciones;
    }

    public List<EvaluadorRelacion> getLstRed() {
        return lstRed;
    }

    public void setLstRed(List<EvaluadorRelacion> lstRed) {
        this.lstRed = lstRed;
    }

    public List<EvaluadorRelacion> getFiltroRed() {
        return filtroRed;
    }

    public void setFiltroRed(List<EvaluadorRelacion> filtroRed) {
        this.filtroRed = filtroRed;
    }
    
    @PostConstruct
    public void init() {
        
        ProyectoInfo objProyectoInfo = Utilitarios.obtenerRed();
        
        isArtificio = objProyectoInfo.isBoDefineArtificio();
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

        Participante objParticipante;

        if(isArtificio){
            objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado());
        }else{
            objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), Utilitarios.obtenerUsuario().getStrEmail());
        }
                
        lstRed = new ArrayList<>();
        lstRelaciones = new ArrayList<>(); 
                        
        //if(!objParticipante.getPaInRedCargada().equals(Boolean.TRUE)){
        
            RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();

            List<RedEvaluacion> lstRedEvaluacion = objRedEvaluacionDAO.obtenerListaEvaluadores(objProyectoInfo.getIntIdProyecto());

            if(!lstRedEvaluacion.isEmpty()){

                /* Busca relaciones grabadas anterioremente */
                HashMap map = obtenerRelacionesAnteriores(objRedEvaluacionDAO, objProyectoInfo);

                /* Muestra lista de evaluados */

                EvaluadorRelacion objEvaluadorRelacion;

                for (RedEvaluacion objRedEvaluacion:lstRedEvaluacion){

                    if(!objRedEvaluacion.getReTxCorreo().equals(objParticipante.getPaTxCorreo())){

                        objEvaluadorRelacion = new EvaluadorRelacion();

                        objEvaluadorRelacion.setIntIdEvaluador(objRedEvaluacion.getReIdParticipantePk());
                        objEvaluadorRelacion.setStrDescNombre(objRedEvaluacion.getReTxDescripcion());
                        objEvaluadorRelacion.setStrCorreo(objRedEvaluacion.getReTxCorreo());
                        objEvaluadorRelacion.setStrCargo(objRedEvaluacion.getReTxNombreCargo());

                        if(!map.isEmpty()){
                            if(map.containsKey(objRedEvaluacion.getReIdParticipantePk())){
                                objEvaluadorRelacion.setIntIdRelacion((Integer) map.get(objRedEvaluacion.getReIdParticipantePk()));
                            }
                        }

                        lstRed.add(objEvaluadorRelacion);

                    }
                }

                /* Carga las relaciones del proyecto */

                RelacionDAO objRelacionDAO = new RelacionDAO();

                lstRelaciones.add(new Relacion(0,"Seleccione un valor"));

                lstRelaciones.addAll(objRelacionDAO.obtenListaRelacionPorProyecto(objProyectoInfo.getIntIdProyecto()));

            }
            
        //}
        
    }

    private HashMap obtenerRelacionesAnteriores(RedEvaluacionDAO objRedEvaluacionDAO, ProyectoInfo objProyectoInfo) {
        
        HashMap map = new HashMap();
        
        List lstRelacionAnt;
        
        if(objProyectoInfo.isBoDefineArtificio()){
            lstRelacionAnt = objRedEvaluacionDAO.obtenerRelacionParticipanteAnterior(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado());
        }else{
            lstRelacionAnt = objRedEvaluacionDAO.obtenerRelacionParticipanteAnterior(objProyectoInfo.getIntIdProyecto(), Utilitarios.obtenerUsuario().getStrEmail());
        }
        
        if(!lstRelacionAnt.isEmpty()){
        
            Iterator itLstRelacionAnt = lstRelacionAnt.iterator();
            
            while(itLstRelacionAnt.hasNext()){
            
                Object[] obj = (Object[]) itLstRelacionAnt.next();
                map.put(obj[0], obj[1]);
            
            }
            
        }
        
        return map;
    }
    
    public void guardarRedSeleccionada(){
        
        FacesMessage message = null;
        
        try {

            boolean flagGuardado;

            if(realizoSeleccion(lstRed)){

                RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
                ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

                Participante objParticipante;
                
                ProyectoInfo objProyectoInfo = Utilitarios.obtenerRed();
                
                if(objProyectoInfo.isBoDefineArtificio()){
                    objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado());
                }else{
                    objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), Utilitarios.obtenerUsuario().getStrEmail());
                }

                objParticipante.setPaInRedCargada(Boolean.TRUE);
                        
                flagGuardado = objRedEvaluacionDAO.guardaRelacionParticipanteEvaluado(lstRed,objParticipante);

                if(flagGuardado){
                    if(objProyectoInfo.isBoDefineArtificio()){
                        terminarCargaDeRed();
                        regresarControlRedes();
                    }else{
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje al guardar relaciones", "Relaciones guardadas exitosamente");
                    }
                }else{
                    message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Mensaje al guardar relaciones", "Ocurrio un grabe error al guardar");
                }

            }else{
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar relaciones", "Debe seleccionar al menos una relacion");
            }

        } catch (Exception e) {
            log.error(e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar relaciones", "Ocurrio un grabe error al guardar");
        }
        
        if(message!=null){
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
    }
    
    private boolean realizoSeleccion(List<EvaluadorRelacion> lstRed){
    
        boolean flagSeleccion = false;

        for (EvaluadorRelacion objEvaluadorRelacion:lstRed){
            if(objEvaluadorRelacion.getIntIdRelacion() != null && objEvaluadorRelacion.getIntIdRelacion() > 0){
                flagSeleccion = true;
            }
        }
            
        return flagSeleccion;
    }
    
    public void terminarCargaDeRed(){
        
        FacesMessage message;
                
        if(realizoSeleccion(lstRed)){
            
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            
            Participante objParticipante;

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerRed();
            
            if(objProyectoInfo.isBoDefineArtificio()){
                objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado());
            }else{
                objParticipante = objParticipanteDAO.obtenParticipantePorProyecto(objProyectoInfo.getIntIdProyecto(), Utilitarios.obtenerUsuario().getStrEmail());
            }            

            objParticipante.setPaInRedCargada(Boolean.TRUE);
            
            boolean correcto = objParticipanteDAO.actualizaParticipante(objParticipante);
            
            if(correcto){
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje al guardar relaciones", "Carga terminada exitosamente");
                if(!objProyectoInfo.isBoDefineArtificio()){
                    try{
            
                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                    session.removeAttribute("redInfo");
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");

                    } catch (IOException ex) {
                        log.debug(ex);
                    }
                }
            }else{
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Mensaje al guardar relaciones", "Ocurrio un error en el proceso");
            }
            
        }else{
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje al guardar relaciones", "Debe seleccionar al menos una relación");
        }
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }

    public void regresarControlRedes(){
    
        try{
            
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("redInfo");
        FacesContext.getCurrentInstance().getExternalContext().redirect("seguimientoRed.jsf");
        
        } catch (IOException ex) {
            log.debug(ex);
        }
    
    }
    
    public void regresarBienvenida(){
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
        } catch (IOException ex) {
            log.debug(ex);
        }
    }
    
}
