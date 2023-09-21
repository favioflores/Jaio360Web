package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "stepTwoView")
@ViewScoped

public class stepTwoView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(stepTwoView.class);

    private static final long serialVersionUID = -1L;

    private ProyectoInfo objProyectoInfo;

    private Integer activeIndex;

    public ProyectoInfo getObjProyectoInfo() {
        return objProyectoInfo;
    }

    public void setObjProyectoInfo(ProyectoInfo objProyectoInfo) {
        this.objProyectoInfo = objProyectoInfo;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

    @PostConstruct
    public void init() {
        objProyectoInfo = Utilitarios.obtenerProyecto();

        if (Utilitarios.obtenerProyecto() != null) {
            try {
                activeIndex = 1;
            } catch (Exception ex) {
                log.error(ex);
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("admProyectos.jsf");
            } catch (Exception ex) {
                log.error(ex);
            }

        }
    }

    public void nextStep() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("stepThree.jsf");
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void backStep() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("stepOne.jsf");
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void goToStep(Integer intStep) {

        try {

            switch (intStep) {
                case 1:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepOne.jsf");
                    break;
                case 2:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepTwo.jsf");
                    break;
                case 3:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepThree.jsf");
                    break;
                case 4:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepFour.jsf");
                    break;
                case 5:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                    break;
                case 6:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepSix.jsf");
                    break;
            }

        } catch (IOException ex) {
            log.error(ex);
        }

    }

}
