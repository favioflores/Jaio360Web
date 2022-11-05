package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "iniciarProyectoView")
@ViewScoped
public class IniciarProyectoView extends BaseView implements Serializable{
    
    private static final long serialVersionUID = -1L;
    private static Log log = LogFactory.getLog(IniciarProyectoView.class);
    
    private boolean blConvocatoria = false;
    private boolean blInstrucciones = false;
    private boolean blAgradecimiento = false;
    private boolean blProyectoEjecutado = false;

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

    public boolean isBlProyectoEjecutado() {
        return blProyectoEjecutado;
    }

    public void setBlProyectoEjecutado(boolean blProyectoEjecutado) {
        this.blProyectoEjecutado = blProyectoEjecutado;
    }

   @PostConstruct
    public void init() {
       
        ProyectoInfo objProyectInfo = Utilitarios.obtenerProyecto();
        
        if(objProyectInfo.getIntIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION)){
            blProyectoEjecutado = true;
        }else{
            blProyectoEjecutado = false;
        }
        
        blConvocatoria = objProyectInfo.isBlConvocatoria();
        blInstrucciones = objProyectInfo.isBlInstrucciones();
        blAgradecimiento = objProyectInfo.isBlAgradecimiento();
        
    }
    
}

