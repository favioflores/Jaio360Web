package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.component.ExecutorBalanceMovement;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ParametroDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RedEvaluacionDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.domain.EvaluacionesXEjecutar;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.NotificacionBean;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.RelacionEvaluadoEvaluador;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.CuestionarioEvaluado;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.ReferenciaMovimiento;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import com.jaio360.orm.UsuarioSaldo;
import com.jaio360.report.CuestionarioFisico;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.elements.*;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;

@ManagedBean(name = "seguimientoProyectoView")
@ViewScoped
public class SeguimientoProyectoView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(SeguimientoProyectoView.class);

    /**
     * ****************
     */
    /* Iniciar proceso */
    /**
     * ****************
     */
    private Integer intCantPartTodos;
    private Integer intCantPartRegistrados;
    private Integer intCantPartEjecucion;
    private Integer intCantPartParam;
    private Integer intCantPartVeri;
    private Integer intCantPartSel;
    private Integer intCantPartCuesNoEje;
    private Integer intCantPartCuesEje;
    private Integer intLicenciasIndividuales;
    private Integer intLicenciasMasivas;
    private Integer intLicenciasIndividualesRequerido;
    private Integer intLicenciasMasivasRequerido;
    private boolean blLicenciasOK;

    private RadarChartModel radarGrupo;

    private Boolean boProyectoEjecutado;
    private Integer intIdEstadoProyecto;

    private List<Participante> lstParticipante;
    private List<RedEvaluacion> lstRedEvaluacion;
    private List<Cuestionario> lstCuestionario;
    private List<CuestionarioEvaluado> lstCuestionarioEvaluado;
    private List<Relacion> lstRelacion;
    private List<RelacionParticipante> lstRelacionParticipante;
    private List<NotificacionBean> lstNotificacion;

    private Date ini;
    private Date end;

    /**
     * ************
     */
    /* SEGUIMIENTO */
    /**
     * ************
     */
    private Integer intPorcentajeGeneral;
    private List<Evaluado> lstParticipantesIniciados;
    private List<RelacionEvaluadoEvaluador> lstRelacionEvaluadoEvaluador;

    private StreamedContent fileIndividual;
    private StreamedContent fileIndividualFisico;

    private Boolean flagDescargaFisico = Boolean.FALSE;
    private Boolean flagFiltrarRed = Boolean.FALSE;
    private Boolean flagComunicar = Boolean.TRUE;

    private Integer idParametroElegido;
    private LinkedHashMap<String, String> mapItemsParametros;

    public StreamedContent getFileIndividualFisico() {
        return fileIndividualFisico;
    }

    public void setFileIndividualFisico(StreamedContent fileIndividualFisico) {
        this.fileIndividualFisico = fileIndividualFisico;
    }

    public Date getIni() {
        return ini;
    }

    public void setIni(Date ini) {
        this.ini = ini;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }



    public Integer getIdParametroElegido() {
        return idParametroElegido;
    }

    public List<NotificacionBean> getLstNotificacion() {
        return lstNotificacion;
    }

    public void setLstNotificacion(List<NotificacionBean> lstNotificacion) {
        this.lstNotificacion = lstNotificacion;
    }

    public Integer getIntLicenciasIndividualesRequerido() {
        return intLicenciasIndividualesRequerido;
    }

    public void setIntLicenciasIndividualesRequerido(Integer intLicenciasIndividualesRequerido) {
        this.intLicenciasIndividualesRequerido = intLicenciasIndividualesRequerido;
    }

    public Integer getIntLicenciasMasivasRequerido() {
        return intLicenciasMasivasRequerido;
    }

    public boolean isBlLicenciasOK() {
        return blLicenciasOK;
    }

    public void setBlLicenciasOK(boolean blLicenciasOK) {
        this.blLicenciasOK = blLicenciasOK;
    }

    public void setIntLicenciasMasivasRequerido(Integer intLicenciasMasivasRequerido) {
        this.intLicenciasMasivasRequerido = intLicenciasMasivasRequerido;
    }

    public void setIdParametroElegido(Integer idParametroElegido) {
        this.idParametroElegido = idParametroElegido;
    }

    public LinkedHashMap<String, String> getMapItemsParametros() {
        return mapItemsParametros;
    }

    public void setMapItemsParametros(LinkedHashMap<String, String> mapItemsParametros) {
        this.mapItemsParametros = mapItemsParametros;
    }

    public Integer getIntLicenciasIndividuales() {
        return intLicenciasIndividuales;
    }

    public void setIntLicenciasIndividuales(Integer intLicenciasIndividuales) {
        this.intLicenciasIndividuales = intLicenciasIndividuales;
    }

    public Integer getIntLicenciasMasivas() {
        return intLicenciasMasivas;
    }

    public void setIntLicenciasMasivas(Integer intLicenciasMasivas) {
        this.intLicenciasMasivas = intLicenciasMasivas;
    }

    public RadarChartModel getRadarGrupo() {
        return radarGrupo;
    }

    public void setRadarGrupo(RadarChartModel radarGrupo) {
        this.radarGrupo = radarGrupo;
    }

    public Boolean getFlagComunicar() {
        return flagComunicar;
    }

    public void setFlagComunicar(Boolean flagComunicar) {
        this.flagComunicar = flagComunicar;
    }

    public Boolean getFlagFiltrarRed() {
        return flagFiltrarRed;
    }

    public void setFlagFiltrarRed(Boolean flagFiltrarRed) {
        this.flagFiltrarRed = flagFiltrarRed;
    }

    public Boolean getFlagDescargaFisico() {
        return flagDescargaFisico;
    }

    public void setFlagDescargaFisico(Boolean flagDescargaFisico) {
        this.flagDescargaFisico = flagDescargaFisico;
    }

    public StreamedContent getFileIndividual() {
        return fileIndividual;
    }

    public void setFileIndividual(StreamedContent fileIndividual) {
        this.fileIndividual = fileIndividual;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public List<RelacionEvaluadoEvaluador> getLstRelacionEvaluadoEvaluador() {
        return lstRelacionEvaluadoEvaluador;
    }

    public void setLstRelacionEvaluadoEvaluador(List<RelacionEvaluadoEvaluador> lstRelacionEvaluadoEvaluador) {
        this.lstRelacionEvaluadoEvaluador = lstRelacionEvaluadoEvaluador;
    }

    public Integer getIntPorcentajeGeneral() {
        return intPorcentajeGeneral;
    }

    public void setIntPorcentajeGeneral(Integer intPorcentajeGeneral) {
        this.intPorcentajeGeneral = intPorcentajeGeneral;
    }

    public List<Evaluado> getLstParticipantesIniciados() {
        return lstParticipantesIniciados;
    }

    public void setLstParticipantesIniciados(List<Evaluado> lstParticipantesIniciados) {
        this.lstParticipantesIniciados = lstParticipantesIniciados;
    }

    public List<RelacionParticipante> getLstRelacionParticipante() {
        return lstRelacionParticipante;
    }

    public void setLstRelacionParticipante(List<RelacionParticipante> lstRelacionParticipante) {
        this.lstRelacionParticipante = lstRelacionParticipante;
    }

    public Integer getIntCantPartCuesEje() {
        return intCantPartCuesEje;
    }

    public void setIntCantPartCuesEje(Integer intCantPartCuesEje) {
        this.intCantPartCuesEje = intCantPartCuesEje;
    }

    public Integer getIntCantPartCuesNoEje() {
        return intCantPartCuesNoEje;
    }

    public void setIntCantPartCuesNoEje(Integer intCantPartCuesNoEje) {
        this.intCantPartCuesNoEje = intCantPartCuesNoEje;
    }

    public Integer getIntCantPartParam() {
        return intCantPartParam;
    }

    public void setIntCantPartParam(Integer intCantPartParam) {
        this.intCantPartParam = intCantPartParam;
    }

    public Integer getIntCantPartEjecucion() {
        return intCantPartEjecucion;
    }

    public void setIntCantPartEjecucion(Integer intCantPartEjecucion) {
        this.intCantPartEjecucion = intCantPartEjecucion;
    }

    public Integer getIntCantPartTodos() {
        return intCantPartTodos;
    }

    public void setIntCantPartTodos(Integer intCantPartTodos) {
        this.intCantPartTodos = intCantPartTodos;
    }

    public Integer getIntCantPartRegistrados() {
        return intCantPartRegistrados;
    }

    public void setIntCantPartRegistrados(Integer intCantPartRegistrados) {
        this.intCantPartRegistrados = intCantPartRegistrados;
    }

    public Integer getIntCantPartVeri() {
        return intCantPartVeri;
    }

    public void setIntCantPartVeri(Integer intCantPartVeri) {
        this.intCantPartVeri = intCantPartVeri;
    }

    public Integer getIntCantPartSel() {
        return intCantPartSel;
    }

    public void setIntCantPartSel(Integer intCantPartSel) {
        this.intCantPartSel = intCantPartSel;
    }

    public Boolean getBoProyectoEjecutado() {
        return boProyectoEjecutado;
    }

    public void setBoProyectoEjecutado(Boolean boProyectoEjecutado) {
        this.boProyectoEjecutado = boProyectoEjecutado;
    }

    public List<Participante> getLstParticipante() {
        return lstParticipante;
    }

    public void setLstParticipante(List<Participante> lstParticipante) {
        this.lstParticipante = lstParticipante;
    }

    public List<RedEvaluacion> getLstRedEvaluacion() {
        return lstRedEvaluacion;
    }

    public void setLstRedEvaluacion(List<RedEvaluacion> lstRedEvaluacion) {
        this.lstRedEvaluacion = lstRedEvaluacion;
    }

    public List<Cuestionario> getLstCuestionario() {
        return lstCuestionario;
    }

    public void setLstCuestionario(List<Cuestionario> lstCuestionario) {
        this.lstCuestionario = lstCuestionario;
    }

    public List<CuestionarioEvaluado> getLstCuestionarioEvaluado() {
        return lstCuestionarioEvaluado;
    }

    public void setLstCuestionarioEvaluado(List<CuestionarioEvaluado> lstCuestionarioEvaluado) {
        this.lstCuestionarioEvaluado = lstCuestionarioEvaluado;
    }

    public List<Relacion> getLstRelacion() {
        return lstRelacion;
    }

    public void setLstRelacion(List<Relacion> lstRelacion) {
        this.lstRelacion = lstRelacion;
    }

    @PostConstruct
    public void init() {

        intCantPartTodos = 0;
        intCantPartVeri = 0;
        intCantPartSel = 0;
        intCantPartRegistrados = 0;
        intCantPartEjecucion = 0;
        intCantPartParam = 0;
        intCantPartCuesNoEje = 0;
        intCantPartCuesEje = 0;
        intPorcentajeGeneral = 0;

        Integer idProyecto = Utilitarios.obtenerProyecto().getIntIdProyecto();

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        intIdEstadoProyecto = objProyecto.getPoIdEstado();

        if (objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION)
                || objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO)) {
            boProyectoEjecutado = Boolean.TRUE;
        } else {
            boProyectoEjecutado = Boolean.FALSE;
        }

        /* DATOS DE PARTICIPANTES */
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        lstParticipante = objParticipanteDAO.obtenListaParticipanteXProyecto(idProyecto);

        /* DATOS DE EVALUADORES */
        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
        lstRedEvaluacion = objRedEvaluacionDAO.obtenerListaEvaluadores(idProyecto);

        /* DATOS DE CUESTIONARIOS */
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        lstCuestionario = objCuestionarioDAO.obtenListaCuestionario(idProyecto);
        lstCuestionarioEvaluado = objCuestionarioDAO.obtenListaRelacionCuestionarioEvaluado(idProyecto);

        /* DATOS DE RELACIONES CON EVALUADOS */
        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        lstRelacionParticipante = objRelacionParticipanteDAO.obtenListaRelParticipanteXProyecto(idProyecto);

        /* DATOS DE RELACIONES */
        RelacionDAO objRelacionDAO = new RelacionDAO();
        lstRelacion = objRelacionDAO.obtenListaRelacionPorProyecto(idProyecto);

        if (!lstParticipante.isEmpty()) {
            cargarDatosDeParticipantes(lstParticipante, lstCuestionarioEvaluado);
        }

        if (!lstCuestionarioEvaluado.isEmpty()) {
            cargarDatosDeCuestinarioEvaluado(lstCuestionarioEvaluado);
        }

        /* POBLAR PARAMETROS */
        poblarParametros();

        calcularLicencias();

        /* SEGUIMIENTO */
        lstParticipantesIniciados = new ArrayList();

        Evaluado objEvaluado;

        for (Participante objParticipante : lstParticipante) {
            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)
                    || objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO)) {

                objEvaluado = new Evaluado();

                objEvaluado.setPaIdParticipantePk(objParticipante.getPaIdParticipantePk());
                objEvaluado.setPaIdEstado(objParticipante.getPaIdEstado());
                objEvaluado.setPaStrEstado(msg(objParticipante.getPaIdEstado().toString()));
                objEvaluado.setPaIdTipoParticipante(objParticipante.getPaIdTipoParticipante());
                objEvaluado.setPaInAutoevaluar(objParticipante.getPaInAutoevaluar());
                objEvaluado.setPaInRedCargada(objParticipante.getPaInRedCargada());
                objEvaluado.setPaInRedVerificada(objParticipante.getPaInRedVerificada());
                objEvaluado.setPaTxCorreo(objParticipante.getPaTxCorreo());
                objEvaluado.setPaTxDescripcion(objParticipante.getPaTxDescripcion());
                objEvaluado.setPaTxNombreCargo(objParticipante.getPaTxNombreCargo());
                objEvaluado.setBdPorcentajeAvance(BigDecimal.ZERO);
                objEvaluado.setBoCheckFilterSeg(Boolean.FALSE);
                objEvaluado.setPaTxImgUrl(objParticipante.getPaTxImgUrl());

                for (CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado) {
                    if (objCuestionarioEvaluado.getId().getPaIdParticipanteFk().equals(objEvaluado.getPaIdParticipantePk())) {
                        for (Cuestionario objCuestionario : lstCuestionario) {
                            if (objCuestionario.getCuIdCuestionarioPk().equals(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk())) {
                                objEvaluado.setStrDescCuestionario(objCuestionario.getCuTxDescripcion());
                            }
                        }
                    }
                }

                lstParticipantesIniciados.add(objEvaluado);
            }
        }

        Map map = buscaEvaluacionesTerminadas();
        cargarEvaluaciones(map);

        if (!lstParticipantesIniciados.isEmpty()) {
            calcularPorcentajes(map);
        }

    }

    private void poblarParametros() {

        Integer idProyecto = Utilitarios.obtenerProyecto().getIntIdProyecto();
        ParametroDAO objParametroDAO = new ParametroDAO();
        List<Parametro> lstParametros = objParametroDAO.obtenListaParametros(idProyecto);

        this.mapItemsParametros = new LinkedHashMap<>();
        this.mapItemsParametros.put("Relaciones", "0");

        for (Parametro objParametro : lstParametros) {

            String strDesc;
            if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
                this.mapItemsParametros.put("Sexo", objParametro.getPaIdParametroPk().toString());
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {
                this.mapItemsParametros.put("Edad", objParametro.getPaIdParametroPk().toString());
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {
                this.mapItemsParametros.put("Tiempo", objParametro.getPaIdParametroPk().toString());
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
                this.mapItemsParametros.put("Nivel", objParametro.getPaIdParametroPk().toString());
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
                this.mapItemsParametros.put("Area", objParametro.getPaIdParametroPk().toString());
            }

        }
    }

    private void calcularPorcentajes(Map map) {

        /* General */
        int terminados = 0;
        int total = 0;

        /* Coloca porcentajes a evaluados */
        for (Evaluado objEvaluado : lstParticipantesIniciados) {

            int i = 0;
            int c = 0;

            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                if (entry.getValue().equals(objEvaluado.getPaIdParticipantePk())) {
                    i++;
                    terminados++;
                }

            }

            for (RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador) {

                if (objRelacionEvaluadoEvaluador.getIntIdEvaluado().equals(objEvaluado.getPaIdParticipantePk())) {
                    c++;
                    total++;
                }

            }

            objEvaluado.setBdPorcentajeAvance(new BigDecimal(Math.floor(i * 100 / c)));
            this.intPorcentajeGeneral = new BigDecimal(Math.floor(terminados * 100 / total)).toBigIntegerExact().intValue();

        }

    }

    private void cargarEvaluaciones(Map map) {

        lstRelacionEvaluadoEvaluador = new ArrayList<>();

        for (RelacionParticipante objRelacionParticipante : lstRelacionParticipante) {

            boolean apto = false;
            String strDescEvaluador = null;

            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
            Cuestionario objCuestionario = null;

            for (Participante objParticipante : lstParticipante) {
                if (objParticipante.getPaIdParticipantePk().equals(objRelacionParticipante.getId().getPaIdParticipanteFk())) {
                    if (!(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)
                            || objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION))) {

                        strDescEvaluador = objParticipante.getPaTxDescripcion();

                        objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk());

                        apto = true;
                        break;
                    }
                }
            }

            if (apto) {

                RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador = new RelacionEvaluadoEvaluador();

                objRelacionEvaluadoEvaluador.setStrDescEvaluado(strDescEvaluador);

                objRelacionEvaluadoEvaluador.setIntIdEvaluado(objRelacionParticipante.getId().getPaIdParticipanteFk());
                objRelacionEvaluadoEvaluador.setIntIdEvaluador(objRelacionParticipante.getId().getReIdParticipanteFk());
                objRelacionEvaluadoEvaluador.setIntIdRelacion(objRelacionParticipante.getId().getReIdRelacionFk());
                if (objCuestionario != null) {
                    objRelacionEvaluadoEvaluador.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                }

                String strKey = objRelacionEvaluadoEvaluador.getIntIdEvaluado() + Constantes.strPipe
                        + objRelacionEvaluadoEvaluador.getIntIdEvaluador() + Constantes.strPipe
                        + objRelacionEvaluadoEvaluador.getIntIdRelacion();

                objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);

                if (map.containsKey(strKey)) {
                    objRelacionEvaluadoEvaluador.setBlEvaluacionTerminada(Boolean.TRUE);
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                }

                for (Relacion objRelacion : lstRelacion) {
                    if (objRelacion.getReIdRelacionPk().equals(objRelacionEvaluadoEvaluador.getIntIdRelacion())) {
                        objRelacionEvaluadoEvaluador.setStrDescRelacion(objRelacion.getReTxNombre());
                        objRelacionEvaluadoEvaluador.setStrColorRelacion(objRelacion.getReColor());
                    }
                }

                for (RedEvaluacion objRedEvaluacion : lstRedEvaluacion) {
                    if (objRedEvaluacion.getReIdParticipantePk().equals(objRelacionEvaluadoEvaluador.getIntIdEvaluador())) {
                        objRelacionEvaluadoEvaluador.setStrDescEvaluador(objRedEvaluacion.getReTxDescripcion());
                        objRelacionEvaluadoEvaluador.setStrCorreoEvaluador(objRedEvaluacion.getReTxCorreo());
                    }
                }

                lstRelacionEvaluadoEvaluador.add(objRelacionEvaluadoEvaluador);

            }
        }

        /* Carga autoevaluaciones */
        for (Participante objParticipante : lstParticipante) {

            if (objParticipante.getPaInRedCargada().equals(true) && objParticipante.getPaInRedVerificada().equals(true)
                    && (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)
                    || objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO))) {

                if (objParticipante.getPaInAutoevaluar().equals(true)) {

                    RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador = new RelacionEvaluadoEvaluador();

                    objRelacionEvaluadoEvaluador.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
                    objRelacionEvaluadoEvaluador.setIntIdEvaluador(null);
                    objRelacionEvaluadoEvaluador.setIntIdRelacion(null);

                    String strKey = objRelacionEvaluadoEvaluador.getIntIdEvaluado() + Constantes.strPipe
                            + objRelacionEvaluadoEvaluador.getIntIdEvaluador() + Constantes.strPipe
                            + objRelacionEvaluadoEvaluador.getIntIdRelacion();

                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);

                    if (map.containsKey(strKey)) {
                        objRelacionEvaluadoEvaluador.setBlEvaluacionTerminada(Boolean.TRUE);
                        objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                    }

                    objRelacionEvaluadoEvaluador.setStrDescRelacion(msg("autoevaluate.cap"));

                    CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
                    Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk());;

                    objRelacionEvaluadoEvaluador.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                    objRelacionEvaluadoEvaluador.setStrDescEvaluador(objParticipante.getPaTxDescripcion());
                    objRelacionEvaluadoEvaluador.setStrDescEvaluado(objParticipante.getPaTxDescripcion());
                    objRelacionEvaluadoEvaluador.setStrCorreoEvaluador(objParticipante.getPaTxCorreo());

                    lstRelacionEvaluadoEvaluador.add(objRelacionEvaluadoEvaluador);

                }

            }

        }

    }

    private void cargarDatosDeCuestinarioEvaluado(List<CuestionarioEvaluado> lstCuestionarioEvaluado) {

        for (CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado) {

            if (objCuestionarioEvaluado.getCeIdEstado().equals(Constantes.INT_ET_ESTADO_CUES_EVA_REGISTRADO)) {
                intCantPartCuesNoEje++;
            }
            if (objCuestionarioEvaluado.getCeIdEstado().equals(Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION)) {
                intCantPartCuesEje++;
            }
        }

    }

    private void cargarDatosDeParticipantes(List<Participante> lstParticipantes, List<CuestionarioEvaluado> lstCuestionarioEvaluado) {

        for (Participante objParticipante : lstParticipantes) {

            intCantPartTodos++;

            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                intCantPartRegistrados++;
            }

            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)) {
                intCantPartEjecucion++;
            }

            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)) {
                intCantPartParam++;
            }

            if (objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)) {
                intCantPartVeri++;
            }

            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)
                    && objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)) {

                for (CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado) {
                    if (objCuestionarioEvaluado.getId().getPaIdParticipanteFk().equals(objParticipante.getPaIdParticipantePk())) {
                        intCantPartSel++;
                    }
                }
            }

        }

    }

    public boolean verificaNotificaciones() {

        MensajeDAO objMensajeDAO = new MensajeDAO();
        Mensaje objMensajeA = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_CONVOCATORIA);
        Mensaje objMensajeB = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES);
        Mensaje objMensajeC = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO);

        return !(objMensajeA == null || objMensajeB == null || objMensajeC == null);

    }

    public void iniciarProceso() {

        if (verificaNotificaciones()) {

            init();

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            List lstNotificaciones = objProyectoDAO.iniciarProyecto(lstParticipante, lstRedEvaluacion, lstCuestionario, lstCuestionarioEvaluado, lstRelacion, lstRelacionParticipante);

            if (!lstNotificaciones.isEmpty()) {
                Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                if (!objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION)) {
                    objProyecto.setPoFeEjecucion(new Date());
                    objProyecto.setPoIdEstado(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION);
                    objProyectoDAO.actualizaProyecto(objProyecto);

                    mostrarAlertaInfo("step5.start.project");
                }
            } else {
                mostrarAlertaError("error.was.occurred");
            }

            init();
        } else {
            mostrarAlertaError("step5.start.project.incomplete");
        }

    }

    public void actListaEvaluadores() {

        Map map = buscaEvaluacionesTerminadas();

        cargarEvaluaciones(map);

        List<RelacionEvaluadoEvaluador> lstTemp = new ArrayList<>();

        for (RelacionEvaluadoEvaluador obj : lstRelacionEvaluadoEvaluador) {

            for (Evaluado objEvaluado : lstParticipantesIniciados) {

                if (obj.getIntIdEvaluado().equals(objEvaluado.getPaIdParticipantePk()) && objEvaluado.getBoCheckFilterSeg()) {

                    lstTemp.add(obj);

                }

            }

        }

        lstRelacionEvaluadoEvaluador = lstTemp;

    }

    private Map buscaEvaluacionesTerminadas() {

        Map map = new HashMap();

        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        List lstTerminados = objResultadoDAO.obtenListaResultadoGeneral(Utilitarios.obtenerProyecto().getIntIdProyecto());

        if (!lstTerminados.isEmpty()) {

            Iterator itLstTerminados = lstTerminados.iterator();

            String strKey;

            while (itLstTerminados.hasNext()) {

                Object[] obj = (Object[]) itLstTerminados.next();

                if (Utilitarios.noEsNuloOVacio(obj[2])) {
                    strKey = obj[0] + Constantes.strPipe + obj[1] + Constantes.strPipe + obj[2];
                } else {
                    strKey = obj[0] + Constantes.strPipe + obj[1] + Constantes.strPipe + obj[2];
                }
                map.put(strKey, obj[0]);

            }
        }

        return map;

    }

    public void descargarFisico() {

        boolean flag = false;

        List<String> lstArchivos = new ArrayList<>();

        for (Evaluado objEvaluado : lstParticipantesIniciados) {

            //if (objEvaluado.isBlManual()) {
            flag = true;
            CuestionarioFisico objCuestionarioFisico = new CuestionarioFisico();
            try {
                String archivo = objCuestionarioFisico.build(objEvaluado);
                lstArchivos.add(archivo);
            } catch (IOException ex) {
                mostrarError(log, ex);
                mostrarAlertaFatal("error.was.occurred");
            }
            //}
        }

        if (!flag) {
            mostrarAlertaError("must.select.at.least.evaluated");
        } else {

            if (!lstArchivos.isEmpty()) {
                try {
                    String ZipName = msg("step2.evaluations") + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);
                    File directory = new File(Constantes.STR_INBOX_DEFINITIVO);

                    if (!directory.exists()) {
                        directory.mkdir();
                    }

                    FileOutputStream salida;
                    salida = new FileOutputStream(objFile);
                    Utilitarios.zipArchivosCualquiera(lstArchivos, salida);
                    InputStream stream = new FileInputStream(objFile.getAbsolutePath());

                    fileIndividualFisico = DefaultStreamedContent.builder()
                            .name(ZipName)
                            .contentType("application/zip")
                            .stream(() -> stream)
                            //.stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                            .build();

                    mostrarAlertaInfo("step5.evaluation.downloaded=");
                } catch (FileNotFoundException ex) {
                    mostrarError(log, ex);
                    mostrarAlertaFatal("error.was.occurred");
                }
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }
        }

    }

    public void revertirEvaluado(Evaluado objEvaluado) {

        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();

        try {

            boolean correcto;
            /* ACTUALIZA EL ESTADO DEL CUESTIONARIO */
            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

            correcto = objCuestionarioDAO.actualizaEstadoCuestionarioXEvaluado(objEvaluado, Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_ESTADO_CUES_EVA_REGISTRADO, sesion);

            if (correcto) {
                /* ACTUALIZA EL ESTADO DEL EVALUADO */
                ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

                Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());

                objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);

                objParticipanteDAO.actualizaParticipante(objParticipante, sesion);

            }

            if (correcto) {
                tx.commit();
            } else {
                tx.rollback();
            }

        } catch (Exception e) {
            mostrarError(log, e);
            tx.rollback();
        }

        init();

    }

    public void retirarRelacion(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador) {

        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();

        RelacionParticipante objRelacionParticipante = new RelacionParticipante();

        RelacionParticipanteId objRelacionParticipanteId = new RelacionParticipanteId();

        objRelacionParticipanteId.setPaIdParticipanteFk(objRelacionEvaluadoEvaluador.getIntIdEvaluado());
        objRelacionParticipanteId.setReIdParticipanteFk(objRelacionEvaluadoEvaluador.getIntIdEvaluador());
        objRelacionParticipanteId.setReIdRelacionFk(objRelacionEvaluadoEvaluador.getIntIdRelacion());

        objRelacionParticipante.setId(objRelacionParticipanteId);

        objRelacionParticipanteDAO.eliminaRelacionParticipante(objRelacionParticipante);
        /* VERIFICA SI NO TIENE OTRAS RELACIONES, SINO CAMBIA DE ESTADO A REGISTRADO */
        //FALTA

        init();

    }

    public void terminarProceso() {

        ProyectoDAO objProyectoDAO = new ProyectoDAO();

        objProyectoDAO.terminarProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        mostrarAlertaInfo("step5.finish.project");

        init();

    }

    public void realizaEvaluacion(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador) {
        try {

            ProyectoInfo redSeleccionada = new ProyectoInfo();
            redSeleccionada.setBoDefineArtificio(Boolean.TRUE);
            redSeleccionada.setIntIdProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            Participante objParticipante = objParticipanteDAO.obtenParticipante(Long.valueOf(objRelacionEvaluadoEvaluador.getIntIdEvaluado()));

            //redSeleccionada.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
            //redSeleccionada.setStrCorreoEvaluado(objParticipante.getPaTxCorreo());
            //redSeleccionada.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
            //redSeleccionada.setStrCorreoEvaluador(objRelacionEvaluadoEvaluador.getStrCorreoEvaluador());
            //redSeleccionada.setStrNombreEvaluado(objRelacionEvaluadoEvaluador.getStrDescEvaluado());
            redSeleccionada.setStrNombreEvaluador(objRelacionEvaluadoEvaluador.getStrDescEvaluador());
            //redSeleccionada.setStrRelacion(objRelacionEvaluadoEvaluador.getStrDescRelacion());
            redSeleccionada.setIntCantidadEvaluaciones(1);
            redSeleccionada.setIntIdCuestionario(objRelacionEvaluadoEvaluador.getIntIdCuestionario());

            EvaluacionesXEjecutar objEvaluacionesXEjecutar = new EvaluacionesXEjecutar();

            objEvaluacionesXEjecutar.setIdParticipante(objParticipante.getPaIdParticipantePk());
            objEvaluacionesXEjecutar.setBlGrupal(false);
            objEvaluacionesXEjecutar.setIdProyecto(intIdEstadoProyecto);
            objEvaluacionesXEjecutar.setStrCorreoEvaluado(objParticipante.getPaTxCorreo());
            objEvaluacionesXEjecutar.setStrCorreoEvaluador(objRelacionEvaluadoEvaluador.getStrCorreoEvaluador());
            objEvaluacionesXEjecutar.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
            objEvaluacionesXEjecutar.setStrURLImagen(objParticipante.getPaTxImgUrl());

            if (objRelacionEvaluadoEvaluador.getStrCorreoEvaluador().equals(objParticipante.getPaTxCorreo())) {
                objEvaluacionesXEjecutar.setBlAutoevaluation(true);
            } else {
                objEvaluacionesXEjecutar.setBlAutoevaluation(false);
            }

            redSeleccionada.getLstEvaluacionesXEjecutar().add(objEvaluacionesXEjecutar);

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");
            session.setAttribute("evalInfo", redSeleccionada);

            FacesContext.getCurrentInstance().getExternalContext().redirect("ejecutarEvaluacion.jsf");

        } catch (IOException ex) {
            mostrarError(log, ex);
        }

    }

    public void generaExcel() {

        HSSFWorkbook xlsEvaluados = new HSSFWorkbook();

        HSSFSheet hoja = xlsEvaluados.createSheet(msg("monitoring"));

        HSSFRow row = hoja.createRow(0);

        int c = 0;

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("evaluated"));
        cell0.setCellValue(texto0);

        c++;

        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("evaluator"));
        cell1.setCellValue(texto1);

        c++;

        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("relationship"));
        cell2.setCellValue(texto2);

        c++;
        HSSFCell cell3 = row.createCell(c);
        HSSFRichTextString texto3 = new HSSFRichTextString(msg("complete"));
        cell3.setCellValue(texto3);

        int i = 1;
        for (RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador) {

            HSSFRow nextrow = hoja.createRow(i);

            int r = 0;

            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescEvaluado());
            r++;
            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescEvaluador());
            r++;
            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescRelacion());

            r++;
            if (objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada() != null) {
                if (objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada()) {
                    nextrow.createCell(r).setCellValue(msg("yes"));
                } else {
                    nextrow.createCell(r).setCellValue(msg("no"));
                }
            } else {
                nextrow.createCell(r).setCellValue(msg("no"));
            }

            i++;

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("monitoring") + ".xls\"");

            xlsEvaluados.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void enviarRecordatorio() {

        boolean validaAlMenosUno = false;
        for (RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador) {
            if (objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada() == null || objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada().equals(false)) {
                validaAlMenosUno = true;
                continue;
            }
        }

        if (validaAlMenosUno) {
            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
            try {
                if (objNotificacionesDAO.guardaNotificacionesEvaluadores(lstRelacionEvaluadoEvaluador)) {
                    MailSender objMailSender = new MailSender();
                    objMailSender.enviarListaDeNotificacionesProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
                    mostrarAlertaInfo("step5.sended.reminders.project");
                } else {
                    mostrarAlertaFatal("error.was.occurred");
                }
            } catch (HibernateException ex) {
                mostrarError(log, ex);
                mostrarAlertaFatal("error.was.occurred");
            } catch (Exception ex) {
                mostrarError(log, ex);
                mostrarAlertaFatal("error.was.occurred");
            }
        } else {
            mostrarAlertaError("must.select.at.least.evaluated");
        }

    }

    public void enviarRecordatorioPersonal(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador) {

        NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
        try {

            List<RelacionEvaluadoEvaluador> lstEvaluadoEvaluadors = new ArrayList<>();

            lstEvaluadoEvaluadors.add(objRelacionEvaluadoEvaluador);

            if (objNotificacionesDAO.guardaNotificacionesEvaluadores(lstEvaluadoEvaluadors)) {
                MailSender objMailSender = new MailSender();
                objMailSender.enviarListaDeNotificacionesProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                mostrarAlertaInfo("step5.sended.reminder.project");
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (HibernateException ex) {
            mostrarError(log, ex);
            mostrarAlertaFatal("error.was.occurred");
        } catch (Exception ex) {
            mostrarError(log, ex);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void actCheckCorreo(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador) {

        RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluadorT = lstRelacionEvaluadoEvaluador.get(lstRelacionEvaluadoEvaluador.indexOf(objRelacionEvaluadoEvaluador));

        if (objRelacionEvaluadoEvaluadorT != null) {

            objRelacionEvaluadoEvaluadorT.setBlEnvioCorreo(objRelacionEvaluadoEvaluador.getBlEnvioCorreo());

        }

    }

    public void putFlagDescargaFisico() {

        for (Evaluado objEvaluado : lstParticipantesIniciados) {
            if (flagDescargaFisico) {
                objEvaluado.setBlManual(Boolean.TRUE);
            } else {
                objEvaluado.setBlManual(Boolean.FALSE);
            }

        }

    }

    public void onRowSelect(SelectEvent<Evaluado> event) {

        lstParticipantesIniciados.forEach(objEvaluado -> {
            objEvaluado.setBoCheckFilterSeg(Boolean.FALSE);
        });

        event.getObject().setBoCheckFilterSeg(Boolean.TRUE);

        actListaEvaluadores();

        mostrarAlertaInfo("step5.filter");

    }

    public void onRowUnselect(UnselectEvent<Evaluado> event) {

        lstParticipantesIniciados.forEach(objEvaluado -> {
            objEvaluado.setBoCheckFilterSeg(Boolean.TRUE);
        });

        actListaEvaluadores();

        mostrarAlertaInfo("step5.filter.retired");

    }

    public void putFlagFiltrarRed() {

        for (Evaluado objEvaluado : lstParticipantesIniciados) {
            if (flagFiltrarRed) {
                objEvaluado.setBoCheckFilterSeg(Boolean.TRUE);
            } else {
                objEvaluado.setBoCheckFilterSeg(Boolean.FALSE);
            }

        }

        actListaEvaluadores();

    }

    public void putFlagComunicar() {

        for (RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador) {
            if (objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada() == null || objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada().equals(Boolean.FALSE)) {
                if (flagComunicar) {
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);
                } else {
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                }
            }

        }

    }

    public void calcularLicencias() {
        try {
            this.intLicenciasIndividuales = 0;
            this.intLicenciasMasivas = 0;
            this.intLicenciasIndividualesRequerido = 0;
            this.intLicenciasMasivasRequerido = 0;

            Integer intIndividualesRequerido = 0;
            Integer intMasivasRequerido = 0;
            this.blLicenciasOK = false;

            //Integer idTipoTarifaProyecto = null;
            //Calcular licencias requeridas para el proyecto
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

            //Participantes
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            List lstParticipanteRedEvaluacion = objParticipanteDAO.obtenerNroParticipantes(objProyectoInfo.getIntIdProyecto());
            Iterator itLstParticipanteRedEvaluacion = lstParticipanteRedEvaluacion.iterator();

            while (itLstParticipanteRedEvaluacion.hasNext()) {

                Object[] obj = (Object[]) itLstParticipanteRedEvaluacion.next();

                Integer nroEvaluadores = Integer.parseInt(obj[1].toString());

                if (nroEvaluadores > 20) {
                    intMasivasRequerido++;
                } else {
                    intIndividualesRequerido++;
                }

            }

            this.intLicenciasIndividualesRequerido = intIndividualesRequerido;
            this.intLicenciasMasivasRequerido = intMasivasRequerido;

            //Obtener licencias disponibles
            UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();

            UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(Utilitarios.obtenerUsuario().getIntUsuarioPk());

            if (objUsuarioSaldo == null) {
                this.intLicenciasIndividuales = 0;
                this.intLicenciasMasivas = 0;
            } else {
                this.intLicenciasIndividuales = objUsuarioSaldo.getUsNrDisponibleIndividual();
                this.intLicenciasMasivas = objUsuarioSaldo.getUsNrDisponibleMasivo();
            }

            //Validar si las licencias estan ok
            blLicenciasOK = this.intLicenciasIndividuales >= this.intLicenciasIndividualesRequerido
                    && this.intLicenciasMasivas >= this.intLicenciasMasivasRequerido;

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void reservarLicencias() {
        try {
            calcularLicencias();
            boolean blLicenciasOk = true;

            UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();
            UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(Utilitarios.obtenerUsuario().getIntUsuarioPk());

            if (this.intLicenciasIndividualesRequerido <= objUsuarioSaldo.getUsNrDisponibleIndividual()
                    && this.intLicenciasMasivasRequerido <= objUsuarioSaldo.getUsNrDisponibleMasivo()) {

                List<Movimiento> lstMovements = new ArrayList<>();

                ReferenciaMovimiento objReferenciaMovimiento = new ReferenciaMovimiento();

                objReferenciaMovimiento.setPoIdProyectoFk(Utilitarios.obtenerProyecto().getIntIdProyecto());

                if (intLicenciasIndividualesRequerido > 0) {
                    Movimiento objMovimiento = new Movimiento();
                    objMovimiento.setMoInCantidad(intLicenciasIndividualesRequerido);
                    objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_RESERVA_LICENCIA_INDIVIDUAL));
                    objReferenciaMovimiento.setMovimiento(objMovimiento);
                    objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                    lstMovements.add(objMovimiento);
                }

                if (intLicenciasMasivasRequerido > 0) {
                    Movimiento objMovimiento = new Movimiento();
                    objMovimiento.setMoInCantidad(intLicenciasMasivasRequerido);
                    objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_RESERVA_LICENCIA_MASIVA));
                    objReferenciaMovimiento.setMovimiento(objMovimiento);
                    objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                    lstMovements.add(objMovimiento);
                }

                Usuario objUsuario = new Usuario();
                objUsuario.setUsIdCuentaPk(Utilitarios.obtenerUsuario().getIntUsuarioPk());
                String strResult = ExecutorBalanceMovement.getInstance().execute(lstMovements, objUsuario);

                if (strResult == null) {
                    iniciarProceso();
                }

            } else {
                mostrarAlertaError("step5.insufficient.licenses");
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void verLogMail() {
        try {
            this.lstNotificacion = new ArrayList<>();
            this.ini = null;
            this.end = null;
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void buscarEmails() {
        try {

            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

            List lstNotificaciones = objNotificacionesDAO.obtieneNotificacionesForLog(Utilitarios.obtenerProyecto().getIntIdProyecto(), this.ini, this.end );

            Iterator itLstNotificaciones = lstNotificaciones.iterator();

            Notificaciones objNotificaciones;
            NotificacionBean objNotificacionBean;
            List lstTempDestinatarios;

            while (itLstNotificaciones.hasNext()) {

                Object[] obj = (Object[]) itLstNotificaciones.next();
                objNotificaciones = (Notificaciones) obj[0];
                List<Destinatarios> lstDestinatarios = (List<Destinatarios>) obj[1];

                objNotificacionBean = new NotificacionBean();

                objNotificacionBean.setNoFeCreacion(objNotificaciones.getNoFeCreacion());
                objNotificacionBean.setNoFeEnvio(objNotificaciones.getNoFeEnvio());
                objNotificacionBean.setNoTxAsunto(objNotificaciones.getNoTxAsunto());
                objNotificacionBean.setNoFeCreacion(objNotificaciones.getNoFeCreacion());
                objNotificacionBean.setNoTxEstado(msg(objNotificaciones.getNoIdEstado() + ""));
                objNotificacionBean.setNoTxError(objNotificaciones.getNoTxError());

                lstTempDestinatarios = new ArrayList();

                for (Destinatarios objDestinatarios : lstDestinatarios) {
                    lstTempDestinatarios.add(objDestinatarios.getDeTxMail());
                }

                objNotificacionBean.setLstDestinatarios(lstTempDestinatarios);

                this.lstNotificacion.add(objNotificacionBean);

            }

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

}
