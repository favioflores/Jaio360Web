package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "pasosView")
@ViewScoped
public class PasosView implements Serializable{
    
    private static Log log = LogFactory.getLog(PasosView.class);
    
    private static final long serialVersionUID = -1L;
    
    private String opcion;

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
    
    public void navegacionPasos(String opcion){
        
        try {
            
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

            if(opcion.equals(Constantes.PASO_0)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");

            }else if(opcion.equals(Constantes.PASO_1) && condicionesOkPaso1(objProyectoInfo)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("relaciones.jsf");
        
            }else if(opcion.equals(Constantes.PASO_2) && condicionesOkPaso2(objProyectoInfo)){
                
                FacesContext.getCurrentInstance().getExternalContext().redirect("seguimientoRed.jsf");
        
            }else if(opcion.equals(Constantes.PASO_3) && condicionesOkPaso3(objProyectoInfo)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("crearCuestionarios.jsf");
        
            }else if(opcion.equals(Constantes.PASO_4) && condicionesOkPaso4(objProyectoInfo)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("defineCuestionarioEvaluado.jsf");
        
            }else if(opcion.equals(Constantes.PASO_5) && condicionesOkPaso5(objProyectoInfo)){
                
                FacesContext.getCurrentInstance().getExternalContext().redirect("redactarMensajes.jsf");
        
            }else if(opcion.equals(Constantes.PASO_6) && condicionesOkPaso6(objProyectoInfo)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("seguimientoProyecto.jsf");
        
            }else if(opcion.equals(Constantes.PASO_7) && condicionesOkPaso6(objProyectoInfo)){

                FacesContext.getCurrentInstance().getExternalContext().redirect("generaReportes.jsf");
            }else{
                
                FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");
                
            }

        } catch (IOException ex) {
            log.error(ex);
        }
        
    }
    
    private boolean condicionesOkPaso1(ProyectoInfo objProyectoInfo){
        /* No tiene condiciones */
        return true;
    }

    private boolean condicionesOkPaso2(ProyectoInfo objProyectoInfo){
        /* Si tiene relaciones */
        /* Si tiene evaluados */
        /* Si tiene evaluadores */
        return true;
    }
    
    private boolean condicionesOkPaso3(ProyectoInfo objProyectoInfo){
        /* No tiene condiciones */
        return true;
    }
    
    private boolean condicionesOkPaso4(ProyectoInfo objProyectoInfo){
        /* Si tiene relaciones */
        /* Si tiene evaluados y su red esta cargada y revisada */
        /* Si tiene custionarios confirmados */
        return true;
    }
    
    private boolean condicionesOkPaso5(ProyectoInfo objProyectoInfo){
        /* Si el proyecto no esta en registrado */
        return true;
    }
    
    private boolean condicionesOkPaso6(ProyectoInfo objProyectoInfo){
        /* Si el proyecto esta en Terminado */
        
        //if(objProyectoInfo.getIntIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO)){
            return true;
        //}
        
        //return false;
        
    }
}
