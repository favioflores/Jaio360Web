package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.utils.Utilitarios;
import static com.jaio360.view.BaseView.mostrarError;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "welcomeView")
@ViewScoped
public class WelcomeView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(WelcomeView.class);

    private static final long serialVersionUID = -1L;

    private Integer intEvaluationPreferenceView;
    private List<ProyectoInfo> lstEvaluaciones;
    private int cantidadLstEvaluaciones;

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
            FacesContext.getCurrentInstance().getExternalContext().redirect("admProyectos.jsf");
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("ejecutarEvaluacion.jsf");

        } catch (IOException ex) {
            log.error(ex);
        }

    }
    
}
