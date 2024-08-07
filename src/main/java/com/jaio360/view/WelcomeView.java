package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "welcomeView")
@ViewScoped
public class WelcomeView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(WelcomeView.class);

    private Integer intEvaluationPreferenceView;
    private List<ProyectoInfo> lstEvaluaciones;
    private int cantidadLstEvaluaciones;
    private String strFeedback;

    public String getStrFeedback() {
        return strFeedback;
    }

    public void setStrFeedback(String strFeedback) {
        this.strFeedback = strFeedback;
    }

    public Integer getIntEvaluationPreferenceView() {
        return intEvaluationPreferenceView;
    }

    public void setIntEvaluationPreferenceView(Integer intEvaluationPreferenceView) {
        this.intEvaluationPreferenceView = intEvaluationPreferenceView;
    }

    public List<ProyectoInfo> getLstEvaluaciones() {
        return lstEvaluaciones;
    }

    public void setLstEvaluaciones(List<ProyectoInfo> lstEvaluaciones) {
        this.lstEvaluaciones = lstEvaluaciones;
    }

    public int getCantidadLstEvaluaciones() {
        return cantidadLstEvaluaciones;
    }

    public void setCantidadLstEvaluaciones(int cantidadLstEvaluaciones) {
        this.cantidadLstEvaluaciones = cantidadLstEvaluaciones;
    }

    @PostConstruct
    public void init() {

        this.lstEvaluaciones = new ArrayList();

        this.intEvaluationPreferenceView = Utilitarios.obtenerUsuario().getIntEvaluationPreferenceView();

        this.lstEvaluaciones = Utilitarios.poblarListaEvaluaciones(Utilitarios.obtenerUsuario(), this.lstEvaluaciones, this.intEvaluationPreferenceView);

        this.cantidadLstEvaluaciones = this.lstEvaluaciones.size();

    }

    public void changeEvaluationPreferenceView() {
        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
        objUsuarioInfo.setIntEvaluationPreferenceView(this.intEvaluationPreferenceView);
        ses.setAttribute("usuarioInfo", objUsuarioInfo);

        this.lstEvaluaciones = Utilitarios.poblarListaEvaluaciones(Utilitarios.obtenerUsuario(), this.lstEvaluaciones, this.intEvaluationPreferenceView);

    }

    public void ingresaSistema() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("admProjects.jsf");
        } catch (IOException ex) {
            mostrarError(log, ex);
        }
    }

    public void cerrarSistema() {

        try {
            Utilitarios.cerrarSesion(Utilitarios.obtenerUsuario());
        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }

    public void irEvaluacion(ProyectoInfo objProyectoInfo) {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");
            session.setAttribute("evalInfo", objProyectoInfo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("makeAssessment.jsf");

        } catch (IOException ex) {
            log.error(ex);
        }

    }

    public void sendFeedBack() {

        try {
            
            this.strFeedback = "";

            mostrarAlertaInfo("feedback.enviado");

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

}
