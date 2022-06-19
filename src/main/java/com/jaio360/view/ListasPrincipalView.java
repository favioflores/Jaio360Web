/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "listasPrincipalView")
@ViewScoped
public class ListasPrincipalView implements Serializable {

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
    private boolean blOcultos = false;
    private LinkedHashMap<String, String> mapRelacion;

    private int intLicenciasRequeridas;
    private int intLicenciasDisponibles;
    private int intLicenciasReservadas;
    private boolean blLicenciasReservadas;

    public boolean isBlLicenciasReservadas() {
        return blLicenciasReservadas;
    }

    public void setBlLicenciasReservadas(boolean blLicenciasReservadas) {
        this.blLicenciasReservadas = blLicenciasReservadas;
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

    public List<SelectItem> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<SelectItem> lstEstados) {
        this.lstEstados = lstEstados;
    }

    public List<SelectItem> getLstMetodologias() {
        return lstMetodologias;
    }

    public void setLstMetodologias(List<SelectItem> lstMetodologias) {
        this.lstMetodologias = lstMetodologias;
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

    public boolean isBlOcultos() {
        return blOcultos;
    }

    public void setBlOcultos(boolean blOcultos) {
        this.blOcultos = blOcultos;
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

        limpiarFiltro();

        lstProyectos = new ArrayList<>();
        lstRedes = new ArrayList<>();
        lstEvaluaciones = new ArrayList<>();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("proyectoInfo");

        UsuarioSesion objUsuarioSesion = new UsuarioSesion();

        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();

        buscarProyectos();
        poblarListaRedes(objUsuarioInfo);
        poblarListaEvaluaciones(objUsuarioInfo);

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

    public void cargarProyecto(ProyectoInfo obj) {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("proyectoInfo");

            if (proyectoSeleccionado == null) {
                session.setAttribute("proyectoInfo", obj);
            } else {
                session.setAttribute("proyectoInfo", proyectoSeleccionado);
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("stepOne.jsf");

        } catch (IOException ex) {
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

                lstProyectos.add(setearProyecto(objProyecto, objProyectoInfo));
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

            //EHCacheManager objEHCacheManager = new EHCacheManager();
            while (itLstRedes.hasNext()) {

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

        if (!lstEvaluacion.isEmpty()) {

            List<ProyectoInfo> lstEvaluaciones = new ArrayList<>();

            Iterator itLstEvaluaciones = lstEvaluacion.iterator();

            ProyectoInfo objProyectoInfo;

            RelacionDAO objRelacionDAO = new RelacionDAO();

            while (itLstEvaluaciones.hasNext()) {

                Object[] objProyecto = (Object[]) itLstEvaluaciones.next();
                objProyectoInfo = new ProyectoInfo();

                objProyectoInfo.setIntIdProyecto((Integer) objProyecto[0]);
                objProyectoInfo.setStrDescNombre((String) objProyecto[2]);
                objProyectoInfo.setStrDescEvaluado((String) objProyecto[8]);
                objProyectoInfo.setStrDescCuestionario((String) objProyecto[9]);
                objProyectoInfo.setIntIdCuestionario((Integer) objProyecto[10]);
                objProyectoInfo.setIntIdEvaluado((Integer) objProyecto[11]);
                objProyectoInfo.setStrCorreoEvaluado((String) objProyecto[12]);
                objProyectoInfo.setStrNombreEvaluador((String) objProyecto[13]);
                objProyectoInfo.setStrNombreEvaluado((String) objProyecto[8]);

                if (Utilitarios.noEsNuloOVacio(objProyecto[14])) {

                    Relacion objRelacion = objRelacionDAO.obtenRelacion(Integer.parseInt(objProyecto[14].toString()));
                    objProyectoInfo.setStrRelacion(objRelacion.getReTxNombre());
                    objProyectoInfo.setStrRelacionColor(objRelacion.getReColor());
                } else {
                    objProyectoInfo.setStrRelacion("AUTOEVALUACIÓN");
                }

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

    public static ProyectoInfo setearProyecto(Proyecto objProyecto, ProyectoInfo objProyectoInfo) {

        objProyectoInfo = new ProyectoInfo();

        objProyectoInfo.setIntIdProyecto(objProyecto.getPoIdProyectoPk());
        objProyectoInfo.setStrDescNombre(objProyecto.getPoTxDescripcion());
        objProyectoInfo.setIntIdMetodologia(objProyecto.getPoIdMetodologia());
        objProyectoInfo.setStrDescMetodologia(EHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdMetodologia()));
        objProyectoInfo.setIntIdEstado(objProyecto.getPoIdEstado());
        objProyectoInfo.setStrDescEstado(EHCacheManager.obtenerDescripcionElemento(objProyecto.getPoIdEstado()));
        objProyectoInfo.setDtFechaCreacion(Utilitarios.convertDateToLocalDate(objProyecto.getPoFeRegistro()));
        objProyectoInfo.setDtFechaEjecucion(objProyecto.getPoFeEjecucion());
        objProyectoInfo.setStrMotivo(objProyecto.getPoTxMotivo());
        objProyectoInfo.setStrMotivo(objProyecto.getPoTxMotivo());
        objProyectoInfo.setBoOculto(objProyecto.getPoInOculto());

        return objProyectoInfo;

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

    public void borrarProyecto(Integer intIdProyecto) {

        try {

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            objProyectoDAO.eliminaProyecto(intIdProyecto);

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "El proyecto se eliminó correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, message);

            buscarProyectos();

        } catch (Exception ex) {
            log.error(ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error al realizar esta accion. Por favor comunicate con el administrador", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void actualizarProyecto(CellEditEvent event) {

        try {

            if (Utilitarios.noEsNuloOVacio(event.getNewValue())) {
                ProyectoInfo objProyectoInfo = this.lstProyectos.get(event.getRowIndex());

                ProyectoDAO objProyectoDAO = new ProyectoDAO();

                Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

                objProyecto.setPoTxDescripcion(event.getNewValue().toString());
                //objProyecto.setPoIdMetodologia(objProyectoInfo.getIntIdMetodologia());
                //objProyecto.setPoTxMotivo(objProyectoInfo.getStrMotivo());

                objProyectoDAO.actualizaProyecto(objProyecto);

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó el proyecto correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                init();
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un valor", null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            log.error(e);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error en la actualización", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void ocultarProyecto(ProyectoInfo objProyectoInfo) {
        try {

            ProyectoDAO objProyectoDAO = new ProyectoDAO();
            Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

            //if (objProyectoInfo.isBoOculto()) {
            objProyecto.setPoInOculto(Boolean.TRUE);
            objProyectoInfo.setBoOculto(true);
            lstProyectos.remove(objProyectoInfo);

            objProyectoDAO.actualizaProyecto(objProyecto);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "El proyecto se ocultó correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            /*
            } else {

                objProyecto.setPoInOculto(Boolean.FALSE);
                objProyectoInfo.setBoOculto(false);
                lstProyectos.remove(objProyectoInfo);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificación de proyecto", "El proyecto se habilitó correctamente");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }*/

        } catch (Exception e) {
            log.error(e);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificación de proyecto", "Ocurrio un error en la actualización");
            FacesContext.getCurrentInstance().addMessage(null, msg);
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

                lstProyectos.add(setearProyecto(objProyecto, objProyectoInfo));
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

        Date fecha = Utilitarios.obtenerFechaHoraSistema();
        String dias = EHCacheManager.obtenerValor1Elemento(Constantes.ET_DIAS_BUSQUEDAS);

        if (Utilitarios.noEsNuloOVacio(dias) && Utilitarios.isNumber(dias, true)) {
            txtFechaRegistroInicial = Utilitarios.sumarRestarDiasFecha(fecha, Utilitarios.aNumero(dias));
            txtFechaEjecucionInicial = Utilitarios.sumarRestarDiasFecha(fecha, Utilitarios.aNumero(dias));
        } else {
            int diasTemp = -100;
            txtFechaRegistroInicial = Utilitarios.sumarRestarDiasFecha(fecha, diasTemp);
            txtFechaEjecucionInicial = Utilitarios.sumarRestarDiasFecha(fecha, diasTemp);
        }

        txtFechaRegistroFinal = fecha;
        txtFechaEjecucionFinal = fecha;

        blOcultos = false;

    }
}
