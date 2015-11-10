package com.jaio360.view;

import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.EstadoPasosProyecto;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "resumenProyectoView")
@ViewScoped
public class ResumenProyectoView implements Serializable{
    
    private static Log log = LogFactory.getLog(ResumenProyectoView.class);
    
    private static final long serialVersionUID = -1L;
    
    private ProyectoInfo objProyectoInfo;

    public ProyectoInfo getObjProyectoInfo() {
        return objProyectoInfo;
    }

    public void setObjProyectoInfo(ProyectoInfo objProyectoInfo) {
        this.objProyectoInfo = objProyectoInfo;
    }
    
    @PostConstruct
    public void init() {
        objProyectoInfo = Utilitarios.obtenerProyecto();
        if(Utilitarios.obtenerProyecto()==null){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");
            } catch (IOException ex) {
                log.error(ex);
            }
        }
    }
    
    public void navegacion(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("resumenProyecto.jsf");
        } catch (IOException ex) {
            log.error(ex);
        }
    }
    
}
