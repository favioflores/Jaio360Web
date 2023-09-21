/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "listasPrincipalView")
@ViewScoped


public class ListasPrincipalView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(ListasPrincipalView.class);

    private static final long serialVersionUID = -1L;

    private List<ProyectoInfo> lstProyectos;

    private List<ProyectoInfo> filtroProyectos;

    private List<ProyectoInfo> lstEvaluaciones;

    private List<ProyectoInfo> lstRedes;

    private List<SelectItem> lstMetodologias;
    private List<SelectItem> lstEstados;

    private int cantidadLstProyectos;
    private int cantidadLstEvaluaciones;
    private int cantidadLstRedes;

    private ProyectoInfo proyectoSeleccionado;
    private ProyectoInfo redSeleccionada;
    private ProyectoInfo evaluacionSeleccionada;

    private String txtDescripcion;
    private Integer idTipoProyecto;
    private Integer idEstadoProyecto;
    private Date txtFechaRegistroInicial;
    private Date txtFechaRegistroFinal;
    private Date txtFechaEjecucionInicial;
    private Date txtFechaEjecucionFinal;
    private Boolean blOcultos = false;

    private int intLicenciasRequeridas;
    private int intLicenciasDisponibles;
    private int intLicenciasReservadas;
    private Boolean blLicenciasReservadas;

    private Boolean globalFilterOnly;
    private List<ProyectoInfo> filteredProyectos;
    private String filterValue;

    public List<ProyectoInfo> getLstProyectos() {
        return lstProyectos;
    }

    public void setLstProyectos(List<ProyectoInfo> lstProyectos) {
        this.lstProyectos = lstProyectos;
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

    public List<SelectItem> getLstMetodologias() {
        return lstMetodologias;
    }

    public void setLstMetodologias(List<SelectItem> lstMetodologias) {
        this.lstMetodologias = lstMetodologias;
    }

    public List<SelectItem> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<SelectItem> lstEstados) {
        this.lstEstados = lstEstados;
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

    public String getTxtDescripcion() {
        return txtDescripcion;
    }

    public void setTxtDescripcion(String txtDescripcion) {
        this.txtDescripcion = txtDescripcion;
    }

    public Integer getIdTipoProyecto() {
        return idTipoProyecto;
    }

    public void setIdTipoProyecto(Integer idTipoProyecto) {
        this.idTipoProyecto = idTipoProyecto;
    }

    public Integer getIdEstadoProyecto() {
        return idEstadoProyecto;
    }

    public void setIdEstadoProyecto(Integer idEstadoProyecto) {
        this.idEstadoProyecto = idEstadoProyecto;
    }

    public Date getTxtFechaRegistroInicial() {
        return txtFechaRegistroInicial;
    }

    public void setTxtFechaRegistroInicial(Date txtFechaRegistroInicial) {
        this.txtFechaRegistroInicial = txtFechaRegistroInicial;
    }

    public Date getTxtFechaRegistroFinal() {
        return txtFechaRegistroFinal;
    }

    public void setTxtFechaRegistroFinal(Date txtFechaRegistroFinal) {
        this.txtFechaRegistroFinal = txtFechaRegistroFinal;
    }

    public Date getTxtFechaEjecucionInicial() {
        return txtFechaEjecucionInicial;
    }

    public void setTxtFechaEjecucionInicial(Date txtFechaEjecucionInicial) {
        this.txtFechaEjecucionInicial = txtFechaEjecucionInicial;
    }

    public Date getTxtFechaEjecucionFinal() {
        return txtFechaEjecucionFinal;
    }

    public void setTxtFechaEjecucionFinal(Date txtFechaEjecucionFinal) {
        this.txtFechaEjecucionFinal = txtFechaEjecucionFinal;
    }

    public Boolean getBlOcultos() {
        return blOcultos;
    }

    public void setBlOcultos(Boolean blOcultos) {
        this.blOcultos = blOcultos;
    }

    public int getIntLicenciasRequeridas() {
        return intLicenciasRequeridas;
    }

    public void setIntLicenciasRequeridas(int intLicenciasRequeridas) {
        this.intLicenciasRequeridas = intLicenciasRequeridas;
    }

    public int getIntLicenciasDisponibles() {
        return intLicenciasDisponibles;
    }

    public void setIntLicenciasDisponibles(int intLicenciasDisponibles) {
        this.intLicenciasDisponibles = intLicenciasDisponibles;
    }

    public int getIntLicenciasReservadas() {
        return intLicenciasReservadas;
    }

    public void setIntLicenciasReservadas(int intLicenciasReservadas) {
        this.intLicenciasReservadas = intLicenciasReservadas;
    }

    public Boolean getBlLicenciasReservadas() {
        return blLicenciasReservadas;
    }

    public void setBlLicenciasReservadas(Boolean blLicenciasReservadas) {
        this.blLicenciasReservadas = blLicenciasReservadas;
    }

    public Boolean getGlobalFilterOnly() {
        return globalFilterOnly;
    }

    public void setGlobalFilterOnly(Boolean globalFilterOnly) {
        this.globalFilterOnly = globalFilterOnly;
    }

    public List<ProyectoInfo> getFilteredProyectos() {
        return filteredProyectos;
    }

    public void setFilteredProyectos(List<ProyectoInfo> filteredProyectos) {
        this.filteredProyectos = filteredProyectos;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    
    
    public ListasPrincipalView() {
        this.lstProyectos = new ArrayList<>();
        this.lstRedes = new ArrayList<>();
        this.lstEvaluaciones = new ArrayList<>();
    }

    @PostConstruct
    public void init() {

        limpiarFiltro();

        globalFilterOnly = false;
        lstProyectos = new ArrayList<>();
        lstRedes = new ArrayList<>();
        lstEvaluaciones = new ArrayList<>();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("proyectoInfo");

        //UsuarioSesion objUsuarioSesion = new UsuarioSesion();
        //UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
        buscarProyectos();

        //Utilitarios.poblarListaEvaluaciones(objUsuarioInfo, this.lstEvaluaciones);
        if (!lstProyectos.isEmpty()) {
            this.cantidadLstProyectos = this.lstProyectos.size();
        }
        if (!lstRedes.isEmpty()) {
            this.cantidadLstRedes = this.lstRedes.size();
        }
        if (!lstEvaluaciones.isEmpty()) {
            this.cantidadLstEvaluaciones = this.lstEvaluaciones.size();
        }
    }

    public void buscarProyectos() {

        poblarListaProyectos(Utilitarios.obtenerUsuario());

        if (!lstProyectos.isEmpty()) {
            this.cantidadLstProyectos = this.lstProyectos.size();
        } else {
            this.cantidadLstProyectos = 0;
        }

    }

    public void cargarProyecto(ProyectoInfo objProyectoInfo) {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            Utilitarios.goToProject(objProyectoInfo, Utilitarios.obtenerUsuario(), null, session);

        } catch (Exception ex) {
            log.error(ex);
        }

    }

    private void poblarListaProyectos(UsuarioInfo objUsuarioInfo) {

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstProyecto = objProyectoDAO.obtenListaProyectosPorUsuario(objUsuarioInfo.getIntUsuarioPk(), null,
                blOcultos,
                txtDescripcion,
                idTipoProyecto,
                idEstadoProyecto,
                txtFechaRegistroInicial,
                txtFechaRegistroFinal,
                txtFechaEjecucionInicial,
                txtFechaEjecucionFinal
        );

        if (!lstProyecto.isEmpty()) {

            List<ProyectoInfo> lstProyectos = new ArrayList<>();

            Iterator itLstProyectos = lstProyecto.iterator();

            ProyectoInfo objProyectoInfo = new ProyectoInfo();

            while (itLstProyectos.hasNext()) {

                Proyecto objProyecto = (Proyecto) itLstProyectos.next();

                lstProyectos.add(Utilitarios.setearProyecto(objProyecto, objProyectoInfo));
            }

            this.lstProyectos = lstProyectos;
        }

    }

    private void poblarListaRedes(UsuarioInfo objUsuarioInfo) {

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstRed = objProyectoDAO.obtenListaRedesPorUsuario(objUsuarioInfo.getStrEmail());

        if (!lstRed.isEmpty()) {

            List<ProyectoInfo> lstRedes = new ArrayList<>();

            Iterator itLstRedes = lstRed.iterator();

            ProyectoInfo objProyectoInfo;

            while (itLstRedes.hasNext()) {

                Proyecto objProyecto = (Proyecto) itLstRedes.next();
                objProyectoInfo = new ProyectoInfo();

                objProyectoInfo.setIntIdProyecto(objProyecto.getPoIdProyectoPk());
                objProyectoInfo.setStrDescNombre(objProyecto.getPoTxDescripcion());
                objProyectoInfo.setStrMotivo(objProyecto.getPoTxMotivo());

                lstRedes.add(objProyectoInfo);
            }

            this.lstRedes = lstRedes;
        }
    }

    public void irRed(ProyectoInfo objProyectoInfo) {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("redInfo");
            session.setAttribute("redInfo", objProyectoInfo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("defineRed.jsf");

        } catch (IOException ex) {
            log.error(ex);
        }

    }

    public void borrarProyecto(Integer intIdProyecto) {

        try {

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            objProyectoDAO.eliminaProyecto(intIdProyecto);

            mostrarAlertaInfo("adm.project.deleted");

            buscarProyectos();

        } catch (HibernateException ex) {
            mostrarError(log, ex);
        }

    }

    public void actualizarProyecto(CellEditEvent event) {

        try {

            if (Utilitarios.noEsNuloOVacio(event.getNewValue())) {
                ProyectoInfo objProyectoInfo = this.lstProyectos.get(event.getRowIndex());

                ProyectoDAO objProyectoDAO = new ProyectoDAO();

                Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

                if (event.getColumn().getHeaderText().equals(msg("adm.nombre"))) {
                    objProyecto.setPoTxDescripcion(event.getNewValue().toString());
                }

                if (event.getColumn().getHeaderText().equals(msg("adm.descripcion"))) {
                    objProyecto.setPoTxMotivo(event.getNewValue().toString());
                }

                objProyectoDAO.actualizaProyecto(objProyecto);

                mostrarAlertaInfo("adm.project.updated");

                init();

            } else {
                mostrarAlertaError("adm.least.value");
            }
        } catch (HibernateException e) {
            mostrarError(log, e);
        }
    }

    public void ocultarProyecto(ProyectoInfo objProyectoInfo) {
        try {

            ProyectoDAO objProyectoDAO = new ProyectoDAO();
            Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

            objProyecto.setPoInOculto(Boolean.TRUE);
            objProyectoInfo.setBoOculto(true);
            lstProyectos.remove(objProyectoInfo);

            objProyectoDAO.actualizaProyecto(objProyecto);

            mostrarAlertaInfo("adm.archived.project");

        } catch (HibernateException e) {
            mostrarError(log, e);
        }
    }

    public void buscarOcultos() {

        lstProyectos = new ArrayList<>();

        ProyectoDAO objProyectoDAO = new ProyectoDAO();

        List lstProyecto = objProyectoDAO.obtenListaProyectosPorUsuario(Utilitarios.obtenerUsuario().getIntUsuarioPk(), null, blOcultos,
                null, null, null, null, null, null, null);

        if (!lstProyecto.isEmpty()) {

            List<ProyectoInfo> lstProyectos = new ArrayList<>();

            Iterator itLstProyectos = lstProyecto.iterator();

            ProyectoInfo objProyectoInfo = new ProyectoInfo();

            while (itLstProyectos.hasNext()) {

                Proyecto objProyecto = (Proyecto) itLstProyectos.next();

                lstProyectos.add(Utilitarios.setearProyecto(objProyecto, objProyectoInfo));
            }

            this.lstProyectos = lstProyectos;
        }

    }

    public void limpiarFiltro() {

        lstMetodologias = Utilitarios.poblarCombo(Constantes.INT_DT_METODOLOGIAS);
        lstEstados = Utilitarios.poblarCombo(Constantes.INT_DT_ESTADOS_PROYECTO);
        txtDescripcion = Constantes.TODO_TEXTO;
        idTipoProyecto = Constantes.ZERO_INTEGER;
        idEstadoProyecto = Constantes.ZERO_INTEGER;

        //Date fecha = Utilitarios.obtenerFechaHoraSistema();
        //String dias = EHCacheManager.obtenerValor1Elemento(Constantes.ET_DIAS_BUSQUEDAS);
        blOcultos = false;

    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (Utilitarios.esNuloOVacio(filterText)) {
            return true;
        }

        ProyectoInfo infoProyecto = (ProyectoInfo) value;
        return infoProyecto.getStrDescNombre().toLowerCase().contains(filterText)
                || infoProyecto.getStrDescEstado().toLowerCase().contains(filterText)
                || infoProyecto.getStrMotivo().toLowerCase().contains(filterText)
                || infoProyecto.getDtFechaCreacion().toString().toLowerCase().contains(filterText);
    }

}
