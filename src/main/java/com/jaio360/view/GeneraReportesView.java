package com.jaio360.view;

import com.jaio360.application.GenericReports;
import com.jaio360.component.ExecutorBalanceMovement;
import com.jaio360.dao.AnalisisParticipanteDAO;
import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParametroDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.ReporteGeneradoDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.AnalisisParticipante;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.ReferenciaMovimiento;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import com.jaio360.orm.UsuarioSaldo;
import com.jaio360.report.ElementoGrupalUtiles;
import com.jaio360.report.ReporteGrupalCaratula;
import com.jaio360.report.ReporteGrupalNivelParticipacion;
import com.jaio360.report.ReporteGrupalSumarioCategoriaGeneral;
import com.jaio360.report.ReporteIndividualCaratula;
import com.jaio360.report.ReporteIndividualPreguntasAbiertas;
import com.jaio360.report.ReporteIndividualCalificacionXCategoria;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismo;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismoWeighted;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaWeighted;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacion;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionMismo;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionWeighted;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacion;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacionWeighted;
import com.jaio360.report.ReporteIndividualSumarioCategoria;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoRelacion;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoRelacionWeighted;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosProm;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosPromWeighted;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosPromRequired;
import com.jaio360.report.ReporteIndividualSumarioCategoriaWeighted;
import com.jaio360.report.ReporteTodasRespuestas;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
import com.jaio360.utils.ReportSort;
import com.jaio360.utils.Utilitarios;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.axes.radial.RadialScales;
import org.primefaces.model.charts.axes.radial.linear.RadialLinearAngleLines;
import org.primefaces.model.charts.axes.radial.linear.RadialLinearPointLabels;
import org.primefaces.model.charts.axes.radial.linear.RadialLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.FileUtils;

import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;
import org.primefaces.util.IOUtils;
import org.primefaces.util.LangUtils;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "generaReportesView")
@ViewScoped

public class GeneraReportesView extends BaseView implements Serializable {

    private static final Logger log = Logger.getLogger(GeneraReportesView.class);

    private List<SelectItem> lstContenidoIndividual;
    private List<SelectItem> lstContenidoGrupal;
    private List<Integer> lstSeleccionadosIndividual;
    private List<Integer> lstSeleccionadosGrupal;

    private List<Evaluado> lstEvaluados;
    private List<Evaluado> lstEvaluadosFiltrado;
    private List<ReporteGenerado> lstReporteGenerado;

    public List<ReporteGenerado> getLstReporteGenerado() {
        return lstReporteGenerado;
    }

    public void setLstReporteGenerado(List<ReporteGenerado> lstReporteGenerado) {
        this.lstReporteGenerado = lstReporteGenerado;
    }
    private List<Cuestionario> lstCuestionarios;
    private List<Cuestionario> lstCuestionariosSeleccionados;
    private List<Evaluado> lstEvaluadosSeleccionados;
    private List<Cuestionario> lstFiltroCuestionarios;
    private List<Participante> lstFiltroEvaluados;
    private ReporteGeneradoDAO objReporteGeneradoDAO = new ReporteGeneradoDAO();
    private AnalisisParticipanteDAO objAnalisisParticipanteDAO = new AnalisisParticipanteDAO();

    private String[] strDescripcionesIndividual = new String[15];
    private String[] strDescripcionesGrupal = new String[15];

    private StreamedContent fileGrupal;
    private StreamedContent fileIndividual;
    private StreamedContent planAccion;

    private Integer intLicenciasIndividuales;
    private Integer intLicenciasMasivas;
    private Integer intLicenciasIndividualesRequerido;
    private Integer intLicenciasMasivasRequerido;

    private Integer intEvaluadoSeleccionado;
    private List<Componente> lstCategorias;
    private List<Integer> lstCategoriasSeleccionadas;
    private Integer intType;
    private Integer intTypeReports;
    private Integer intTypeReportsGrupal;
    private Boolean blWeightedAvailable;

    private RadarChartModel chartRadar;
    private BarChartModel barModel;

    public List<SelectItem> getLstContenidoIndividual() {
        return lstContenidoIndividual;
    }

    public Integer getIntType() {
        return intType;
    }

    public void setIntType(Integer intType) {
        this.intType = intType;
    }

    public List<Evaluado> getLstEvaluadosFiltrado() {
        return lstEvaluadosFiltrado;
    }

    public void setLstEvaluadosFiltrado(List<Evaluado> lstEvaluadosFiltrado) {
        this.lstEvaluadosFiltrado = lstEvaluadosFiltrado;
    }

    public Boolean getBlWeightedAvailable() {
        return blWeightedAvailable;
    }

    public Integer getIntTypeReportsGrupal() {
        return intTypeReportsGrupal;
    }

    public void setIntTypeReportsGrupal(Integer intTypeReportsGrupal) {
        this.intTypeReportsGrupal = intTypeReportsGrupal;
    }

    public void setBlWeightedAvailable(Boolean blWeightedAvailable) {
        this.blWeightedAvailable = blWeightedAvailable;
    }

    public void setLstContenidoIndividual(List<SelectItem> lstContenidoIndividual) {
        this.lstContenidoIndividual = lstContenidoIndividual;
    }

    public List<SelectItem> getLstContenidoGrupal() {
        return lstContenidoGrupal;
    }

    public void setLstContenidoGrupal(List<SelectItem> lstContenidoGrupal) {
        this.lstContenidoGrupal = lstContenidoGrupal;
    }

    public List<Integer> getLstSeleccionadosIndividual() {
        return lstSeleccionadosIndividual;
    }

    public void setLstSeleccionadosIndividual(List<Integer> lstSeleccionadosIndividual) {
        this.lstSeleccionadosIndividual = lstSeleccionadosIndividual;
    }

    public List<Integer> getLstSeleccionadosGrupal() {
        return lstSeleccionadosGrupal;
    }

    public void setLstSeleccionadosGrupal(List<Integer> lstSeleccionadosGrupal) {
        this.lstSeleccionadosGrupal = lstSeleccionadosGrupal;
    }

    public List<Evaluado> getLstEvaluados() {
        return lstEvaluados;
    }

    public void setLstEvaluados(List<Evaluado> lstEvaluados) {
        this.lstEvaluados = lstEvaluados;
    }

    public List<Cuestionario> getLstCuestionarios() {
        return lstCuestionarios;
    }

    public Integer getIntTypeReports() {
        return intTypeReports;
    }

    public void setIntTypeReports(Integer intTypeReports) {
        this.intTypeReports = intTypeReports;
    }

    public void setLstCuestionarios(List<Cuestionario> lstCuestionarios) {
        this.lstCuestionarios = lstCuestionarios;
    }

    public List<Cuestionario> getLstCuestionariosSeleccionados() {
        return lstCuestionariosSeleccionados;
    }

    public void setLstCuestionariosSeleccionados(List<Cuestionario> lstCuestionariosSeleccionados) {
        this.lstCuestionariosSeleccionados = lstCuestionariosSeleccionados;
    }

    public List<Evaluado> getLstEvaluadosSeleccionados() {
        return lstEvaluadosSeleccionados;
    }

    public void setLstEvaluadosSeleccionados(List<Evaluado> lstEvaluadosSeleccionados) {
        this.lstEvaluadosSeleccionados = lstEvaluadosSeleccionados;
    }

    public List<Cuestionario> getLstFiltroCuestionarios() {
        return lstFiltroCuestionarios;
    }

    public void setLstFiltroCuestionarios(List<Cuestionario> lstFiltroCuestionarios) {
        this.lstFiltroCuestionarios = lstFiltroCuestionarios;
    }

    public List<Participante> getLstFiltroEvaluados() {
        return lstFiltroEvaluados;
    }

    public void setLstFiltroEvaluados(List<Participante> lstFiltroEvaluados) {
        this.lstFiltroEvaluados = lstFiltroEvaluados;
    }

    public String[] getStrDescripcionesIndividual() {
        return strDescripcionesIndividual;
    }

    public void setStrDescripcionesIndividual(String[] strDescripcionesIndividual) {
        this.strDescripcionesIndividual = strDescripcionesIndividual;
    }

    public String[] getStrDescripcionesGrupal() {
        return strDescripcionesGrupal;
    }

    public void setStrDescripcionesGrupal(String[] strDescripcionesGrupal) {
        this.strDescripcionesGrupal = strDescripcionesGrupal;
    }

    public StreamedContent getFileGrupal() {
        return fileGrupal;
    }

    public void setFileGrupal(StreamedContent fileGrupal) {
        this.fileGrupal = fileGrupal;
    }

    public StreamedContent getFileIndividual() {
        return fileIndividual;
    }

    public void setFileIndividual(StreamedContent fileIndividual) {
        this.fileIndividual = fileIndividual;
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

    public Integer getIntLicenciasIndividualesRequerido() {
        return intLicenciasIndividualesRequerido;
    }

    public void setIntLicenciasIndividualesRequerido(Integer intLicenciasIndividualesRequerido) {
        this.intLicenciasIndividualesRequerido = intLicenciasIndividualesRequerido;
    }

    public Integer getIntLicenciasMasivasRequerido() {
        return intLicenciasMasivasRequerido;
    }

    public void setIntLicenciasMasivasRequerido(Integer intLicenciasMasivasRequerido) {
        this.intLicenciasMasivasRequerido = intLicenciasMasivasRequerido;
    }

    public Integer getIntEvaluadoSeleccionado() {
        return intEvaluadoSeleccionado;
    }

    public void setIntEvaluadoSeleccionado(Integer intEvaluadoSeleccionado) {
        this.intEvaluadoSeleccionado = intEvaluadoSeleccionado;
    }

    public List<Componente> getLstCategorias() {
        return lstCategorias;
    }

    public void setLstCategorias(List<Componente> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }

    public List<Integer> getLstCategoriasSeleccionadas() {
        return lstCategoriasSeleccionadas;
    }

    public void setLstCategoriasSeleccionadas(List<Integer> lstCategoriasSeleccionadas) {
        this.lstCategoriasSeleccionadas = lstCategoriasSeleccionadas;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public StreamedContent getPlanAccion() {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String fullPath = servletContext.getRealPath(File.separator + "resources" + File.separator + "other" + File.separator + "Formato Plan de Desarrollo Individual.xlsx");
        File objFile = new File(fullPath);
        planAccion = DefaultStreamedContent.builder()
                .name("Formato Plan de Desarrollo Individual.xlsx")
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                .build();

        return planAccion;
    }

    private void habilitarReportesNoPonderados() {
        lstContenidoIndividual = new ArrayList<>();
        lstContenidoIndividual.add(new SelectItem(1, msg("report." + 1)));
        lstContenidoIndividual.add(new SelectItem(2, msg("report." + 2)));
        lstContenidoIndividual.add(new SelectItem(3, msg("report." + 3)));
        lstContenidoIndividual.add(new SelectItem(4, msg("report." + 4)));
        lstContenidoIndividual.add(new SelectItem(5, msg("report." + 5)));
        lstContenidoIndividual.add(new SelectItem(6, msg("report." + 6)));
        lstContenidoIndividual.add(new SelectItem(7, msg("report." + 7)));
        lstContenidoIndividual.add(new SelectItem(8, msg("report." + 8)));
        lstContenidoIndividual.add(new SelectItem(9, msg("report." + 9)));
        lstContenidoIndividual.add(new SelectItem(10, msg("report." + 10)));
        //lstContenidoIndividual.add(new SelectItem(18, msg("report." + 18)));
    }

    private void habilitarReportesPonderados() {
        lstContenidoIndividual = new ArrayList<>();
        lstContenidoIndividual.add(new SelectItem(11, msg("report." + 11)));
        lstContenidoIndividual.add(new SelectItem(12, msg("report." + 12)));
        lstContenidoIndividual.add(new SelectItem(13, msg("report." + 13)));
        lstContenidoIndividual.add(new SelectItem(14, msg("report." + 14)));
        lstContenidoIndividual.add(new SelectItem(15, msg("report." + 15)));
        lstContenidoIndividual.add(new SelectItem(16, msg("report." + 16)));
        lstContenidoIndividual.add(new SelectItem(17, msg("report." + 17)));
        lstContenidoIndividual.add(new SelectItem(8, msg("report." + 8)));
        lstContenidoIndividual.add(new SelectItem(9, msg("report." + 9)));
        lstContenidoIndividual.add(new SelectItem(10, msg("report." + 10)));
    }

    private void habilitarReportesNoPonderadosGrupal() {
        lstContenidoGrupal = new ArrayList<>();
        for (int i = 100; i <= 102; i++) {
            lstContenidoGrupal.add(new SelectItem(i, msg("report." + i)));
        }
    }

    private void habilitarReportesPonderadosGrupal() {
        lstContenidoGrupal = new ArrayList<>();
        lstContenidoGrupal.add(new SelectItem(103, msg("report." + 103)));
        lstContenidoGrupal.add(new SelectItem(101, msg("report." + 101)));
        lstContenidoGrupal.add(new SelectItem(102, msg("report." + 102)));
    }

    public void habilitarReportes() {
        if (intTypeReports == 0) {
            habilitarReportesNoPonderados();
        } else {
            habilitarReportesPonderados();
        }
    }

    public void habilitarReportesGrupal() {
        if (intTypeReportsGrupal == 0) {
            habilitarReportesNoPonderadosGrupal();
        } else {
            habilitarReportesPonderadosGrupal();
        }
    }

    @PostConstruct
    public void init() {

        chartRadar = new RadarChartModel();
        barModel = new BarChartModel();

        cargarCategorias();

        this.lstContenidoIndividual = new ArrayList<>();
        this.lstContenidoGrupal = new ArrayList<>();
        this.lstReporteGenerado = new ArrayList<>();

        this.intType = 0;
        this.intTypeReports = 0;
        this.intTypeReportsGrupal = 0;
        this.blWeightedAvailable = false;

        ParametroDAO objParametroDAO = new ParametroDAO();

        Parametro objParametro = objParametroDAO.obtenParametro(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES);

        if (objParametro != null && objParametro.getPaInHabilitado()) {
            blWeightedAvailable = true;
        }

        if (intTypeReports == 0) {
            habilitarReportesNoPonderados();
        } else {
            habilitarReportesPonderados();
        }

        if (intTypeReportsGrupal == 0) {
            habilitarReportesNoPonderadosGrupal();
        } else {
            habilitarReportesPonderadosGrupal();
        }

        cargarInformacion();

    }

    private void cargarInformacion() {
        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        /**
         * OBTIENE NRO DE EVALUADORES POR PARTICIPANTE DEL PROYECTO
         */
        List lstParticipanteRedEvaluacion = objParticipanteDAO.obtenerNroParticipantesEnEjecucion(objProyectoInfo.getIntIdProyecto());
        Iterator itLstParticipanteRedEvaluacion = lstParticipanteRedEvaluacion.iterator();

        Map mapConteoEvaluados = new HashMap();

        while (itLstParticipanteRedEvaluacion.hasNext()) {

            Object[] obj = (Object[]) itLstParticipanteRedEvaluacion.next();

            if (!mapConteoEvaluados.containsKey(obj[0])) {
                mapConteoEvaluados.put(obj[0], obj[1]);
            }

        }

        /**
         * OBTIENE NRO DE EVALUACIONES TERMINADAS PR PARTICIPANTE
         */
        List lstEvaluacionesTerminadas = objResultadoDAO.obtenListaTotalTerminadosXparticipante(objProyectoInfo.getIntIdProyecto());
        Iterator itLstEvaluacionesTerminadas = lstEvaluacionesTerminadas.iterator();

        Map mapEvaluacionesTerminadas = new HashMap();

        while (itLstEvaluacionesTerminadas.hasNext()) {

            Object[] obj = (Object[]) itLstEvaluacionesTerminadas.next();

            if (!mapEvaluacionesTerminadas.containsKey(obj[0])) {
                mapEvaluacionesTerminadas.put(obj[0], obj[1]);
            }

        }

        /**
         * OBTIENE PARTICIPANTES DEL PROYECTO
         */
        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXEstado(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);

        lstEvaluados = new ArrayList<>();

        Evaluado objEvaluado;

        for (Participante objParticipante : lstParticipantes) {

            objEvaluado = new Evaluado();

            objEvaluado.setPaIdParticipantePk(objParticipante.getPaIdParticipantePk());
            objEvaluado.setPaTxDescripcion(objParticipante.getPaTxDescripcion());
            objEvaluado.setPaTxNombreCargo(objParticipante.getPaTxNombreCargo());
            objEvaluado.setPaTxCorreo(objParticipante.getPaTxCorreo());
            objEvaluado.setPaInAutoevaluar(objParticipante.getPaInAutoevaluar());
            objEvaluado.setPaTxImgUrl(objParticipante.getPaTxImgUrl());

            if (objParticipante.getPaInAnalizado() != null) {
                objEvaluado.setBlAnalizado(objParticipante.getPaInAnalizado());
                if (objParticipante.getPaInAnalizado()) {
                    objEvaluado.setInAnalizado(111);
                } else {
                    objEvaluado.setInAnalizado(0);
                }
            } else {
                objEvaluado.setBlAnalizado(false);
                objEvaluado.setInAnalizado(0);
            }

            if (mapConteoEvaluados.containsKey(objParticipante.getPaIdParticipantePk())) {
                BigDecimal bdTemp = (BigDecimal) mapConteoEvaluados.get(objParticipante.getPaIdParticipantePk());
                objEvaluado.setIntNumberEvaluators(bdTemp.intValue());
            } else {
                objEvaluado.setIntNumberEvaluators(0);
            }

            if (!mapEvaluacionesTerminadas.isEmpty() && mapEvaluacionesTerminadas.containsKey(objParticipante.getPaIdParticipantePk())) {
                BigInteger bdTemp = (BigInteger) mapEvaluacionesTerminadas.get(objParticipante.getPaIdParticipantePk());
                objEvaluado.setIntNumberEvaluationFinished(bdTemp.intValue());
            } else {
                objEvaluado.setIntNumberEvaluationFinished(0);
            }
            lstEvaluados.add(objEvaluado);

        }

        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        lstCuestionarios = objCuestionarioDAO.obtenListaCuestionarioXEstado(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
    }

    public void generarReporteGrupal() {

        if (lstSeleccionadosGrupal.isEmpty()) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un modelo de los reportes grupales");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else if (lstCuestionariosSeleccionados.isEmpty()) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un cuestionario");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {

            if (!lstSeleccionadosGrupal.isEmpty() && !lstCuestionariosSeleccionados.isEmpty()) {

                //Registrar generación de reporte
                ReporteGenerado objReporteGenerado = new ReporteGenerado();

                objReporteGenerado.setRgDtFechaRegistro(new Date());
                objReporteGenerado.setRgDtFechaUltMod(new Date());
                objReporteGenerado.setRgblWeighted((this.intTypeReportsGrupal == 1));
                objReporteGenerado.setRgBlPorcentajeAvance(BigDecimal.ZERO);
                objReporteGenerado.setRgTxNombreArchivo(msg("filename.progress"));
                objReporteGenerado.setUsuario(Utilitarios.obtenerUsuario().getUsuario());
                objReporteGenerado.setProyectoInfo(Utilitarios.obtenerProyecto());
                objReporteGenerado.setRgDtFechaExpiracion(Utilitarios.calculateExpiryDate(43200));
                Proyecto objProyecto = new Proyecto();
                objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());
                objReporteGenerado.setProyecto(objProyecto);
                objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_REGISTRADO);

                objReporteGeneradoDAO.guardaReporteGenerado(objReporteGenerado);

                //Ejecutar reporte
                GenericReports objGenericReports = new GenericReports();
                objGenericReports.generarAnalisis(Constantes.INT_REPORTE_GRUPAL,
                        objReporteGenerado,
                        this.lstEvaluadosSeleccionados,
                        this.lstCuestionariosSeleccionados,
                        this.lstSeleccionadosIndividual,
                        this.lstSeleccionadosGrupal);

                mostrarAlertaInfo("analisis.enviado");
            } else {
                mostrarAlertaError("debe.elegir.cuestionario.reporte");
            }

        }

    }

    private Map obtieneRelaciones(Integer intIdEvaluado) {

        RelacionDAO objRelacionDAO = new RelacionDAO();

        List<Relacion> lstRelaciones = objRelacionDAO.obtenListaRelacionPorEvaluado(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdEvaluado);

        Map map = new HashMap();

        for (Relacion objRelacion : lstRelaciones) {
            map.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        return map;

    }

    public void generarReporteIndividual() {

        if (lstSeleccionadosIndividual.isEmpty()) {

            mostrarAlertaError("must.select.at.least.one.report");

        } else if (lstEvaluadosSeleccionados.isEmpty()) {

            mostrarAlertaError("must.select.at.least.evaluated");

        } else {

            calcularLicenciasEvaluadosSeleccionados();
        }
    }

    public void closeLicenses() {
        PrimeFaces.current().dialog().closeDynamic("useLicenses");
    }

    public void calcularLicenciasEvaluadosSeleccionados() {

        try {

            this.intLicenciasIndividualesRequerido = 0;
            this.intLicenciasMasivasRequerido = 0;

            Integer intLicenciasIndividualesReservadas = 0;
            Integer intLicenciasMasivasReservadas = 0;

            UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();
            UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(Utilitarios.obtenerUsuario().getIntUsuarioPk());

            if (objUsuarioSaldo != null) {
                this.intLicenciasIndividuales = objUsuarioSaldo.getUsNrReservadoIndividual();
                this.intLicenciasMasivas = objUsuarioSaldo.getUsNrReservadoMasivo();
            } else {
                this.intLicenciasIndividuales = 0;
                this.intLicenciasMasivas = 0;
            }

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

            //Participantes
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

            Integer intNroEvaluadores = 0;

            for (Evaluado objParticipante : lstEvaluadosSeleccionados) {

                if (!objParticipante.getBlAnalizado()) {
                    Object obj = objParticipanteDAO.obtenerNroEvaluadoresXEvaluado(objProyectoInfo.getIntIdProyecto(), objParticipante.getPaIdParticipantePk());
                    if (obj != null) {
                        intNroEvaluadores = ((BigDecimal) obj).intValue();
                    }

                } else {
                    continue;
                }

                if (intNroEvaluadores > 20) {
                    intLicenciasMasivasReservadas++;
                } else {
                    intLicenciasIndividualesReservadas++;
                }

            }

            this.intLicenciasIndividualesRequerido = intLicenciasIndividualesReservadas;

            this.intLicenciasMasivasRequerido = intLicenciasMasivasReservadas;

            confirmaGeneracionReporteIndividual();

        } catch (HibernateException e) {
            mostrarError(log, e);
        }
    }

    public void confirmaGeneracionReporteIndividual() {

        try {
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            Participante objParticipante;
            boolean ok = true;

            for (Evaluado objEvaluado : this.lstEvaluadosSeleccionados) {

                if (!objEvaluado.getBlAnalizado()) {

                    objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());
                    objParticipante.setPaInAnalizado(Boolean.TRUE);

                    List<Movimiento> lstMovements = new ArrayList<>();

                    ReferenciaMovimiento objReferenciaMovimiento = new ReferenciaMovimiento();

                    objReferenciaMovimiento.setPoIdProyectoFk(Utilitarios.obtenerProyecto().getIntIdProyecto());

                    if (objEvaluado.getIntNumberEvaluators() <= 20) {
                        Movimiento objMovimiento = new Movimiento();
                        objMovimiento.setMoInCantidad(1);
                        objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_EJECUTA_LICENCIA_INDIVIDUAL));
                        objReferenciaMovimiento.setMovimiento(objMovimiento);
                        objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                        lstMovements.add(objMovimiento);
                    } else {
                        Movimiento objMovimiento = new Movimiento();
                        objMovimiento.setMoInCantidad(1);
                        objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_EJECUTA_LICENCIA_MASIVA));
                        objReferenciaMovimiento.setMovimiento(objMovimiento);
                        objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                        lstMovements.add(objMovimiento);
                    }

                    Usuario objUsuario = new Usuario();
                    objUsuario.setUsIdCuentaPk(Utilitarios.obtenerUsuario().getIntUsuarioPk());
                    String strResult = ExecutorBalanceMovement.getInstance().execute(lstMovements, objUsuario);

                    if (strResult != null) {
                        ok = false;
                    }

                    objParticipanteDAO.actualizaParticipante(objParticipante);

                }
            }

            if (ok) {
                cargarInformacion();

                if (!lstSeleccionadosIndividual.isEmpty() && !lstEvaluadosSeleccionados.isEmpty()) {

                    //Registrar generación de reporte
                    ReporteGenerado objReporteGenerado = new ReporteGenerado();

                    objReporteGenerado.setRgDtFechaRegistro(new Date());
                    objReporteGenerado.setRgDtFechaUltMod(new Date());
                    objReporteGenerado.setRgblWeighted((this.intTypeReports == 1));
                    objReporteGenerado.setRgBlPorcentajeAvance(BigDecimal.ZERO);
                    objReporteGenerado.setRgTxNombreArchivo(msg("filename.progress"));
                    objReporteGenerado.setUsuario(Utilitarios.obtenerUsuario().getUsuario());
                    objReporteGenerado.setProyectoInfo(Utilitarios.obtenerProyecto());
                    objReporteGenerado.setRgDtFechaExpiracion(Utilitarios.calculateExpiryDate(43200));
                    Proyecto objProyecto = new Proyecto();
                    objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());
                    objReporteGenerado.setProyecto(objProyecto);
                    objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_REGISTRADO);

                    objReporteGeneradoDAO.guardaReporteGenerado(objReporteGenerado);

                    //Ejecutar reporte
                    GenericReports objGenericReports = new GenericReports();
                    objGenericReports.generarAnalisis(Constantes.INT_REPORTE_INDIVIDUAL,
                            objReporteGenerado,
                            this.lstEvaluadosSeleccionados,
                            this.lstCuestionariosSeleccionados,
                            this.lstSeleccionadosIndividual,
                            this.lstSeleccionadosGrupal);

                    mostrarAlertaInfo("analisis.enviado");

                } else {
                    mostrarAlertaError("debe.elegir.evaluado.reporte");
                }

            } else {
                mostrarAlertaError("error.was.occurred");
            }

        } catch (HibernateException e) {
            mostrarError(log, e);
        }

    }

    public RadarChartModel getChartRadar() {
        return chartRadar;
    }

    public void setChartRadar(RadarChartModel chartRadar) {
        this.chartRadar = chartRadar;
    }

    public void cargarCategorias() {

        if (Utilitarios.noEsNuloOVacio(this.intEvaluadoSeleccionado)) {
            ComponenteDAO objComponenteDAO = new ComponenteDAO();

            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

            Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(this.intEvaluadoSeleccionado, Utilitarios.obtenerProyecto().getIntIdProyecto());

            this.lstCategorias = objComponenteDAO.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objCuestionario.getCuIdCuestionarioPk(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null);
        }
    }

    private LinkedHashMap cargarRelaciones(Participante objParticipante) {

        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();

        LinkedHashMap mapRelaciones = new LinkedHashMap();

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        if (objParticipante.getPaInAutoevaluar()) {
            Relacion objRelacion = new Relacion();
            objRelacion.setReIdRelacionPk(-1);
            objRelacion.setReNuOrden(intMaxRango + 1);
            objRelacion.setReTxAbreviatura("AUTO");
            objRelacion.setReTxDescripcion("Autoevaluación");
            objRelacion.setReTxNombre("Autoevaluación");
            objRelacion.setReColor("212324");
            objRelacion.setReDePonderacion(Constantes.PERCENT_100);
            mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        Relacion objRelacion = new Relacion();
        objRelacion.setReIdRelacionPk(-1);
        objRelacion.setReNuOrden(intMaxRango + 1);
        objRelacion.setReTxAbreviatura("PROM");
        objRelacion.setReTxDescripcion("Promedio");
        objRelacion.setReTxNombre("Promedio");
        objRelacion.setReColor("494D4F");
        objRelacion.setReDePonderacion(Constantes.PERCENT_100);
        mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);

        mapRelaciones.putAll(obtieneRelaciones(objParticipante.getPaIdParticipantePk()));

        return mapRelaciones;
    }

    public void cargarBarrasSumarioXCategorias() {

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        ComponenteDAO componenteDao = new ComponenteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        barModel = new BarChartModel();

        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Categorias");
        List<Number> values = new ArrayList<>();
        List<String> bgColor = new ArrayList<>();
        List<String> borderColor = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int i = 0;
        String strColorPreferencial;

        for (Integer idCategoria : this.lstCategoriasSeleccionadas) {

            Componente objComponente = componenteDao.obtenComponente(idCategoria);
            labels.add(objComponente.getCoTxDescripcion());

            List lstResultadoXCategoria = objResultadoDAO.obtenerResultadoXCategoria(idCategoria);
            strColorPreferencial = Utilitarios.generaColorHtmlPreferencial(i);

            bgColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA(strColorPreferencial));
            borderColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(strColorPreferencial));

            if (!lstResultadoXCategoria.isEmpty()) {
                Object obj[] = (Object[]) lstResultadoXCategoria.get(0);
                values.add((Number) obj[0]);
            } else {
                values.add(0);
            }

            i++;
        }

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        barDataSet.setData(values);
        barDataSet.setBorderWidth(1);
        barDataSet.setBackgroundColor(bgColor);
        barDataSet.setBorderColor(borderColor);

        data.addChartDataSet(barDataSet);
        data.setLabels(labels);
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        linearAxes.setMax(intMaxRango + 1);
        linearAxes.setStacked(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        //ticks.setMaxTicksLimit(intMaxRango + 1);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);


        /*
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);
         */
        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("italic");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);
        barModel.setExtender("barExtender");

        // disable animation
        /*
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);
         */
        barModel.setOptions(options);
    }

    public void goToDashboard() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.jsf");
        } catch (IOException ex) {
            mostrarError(log, ex);
        }
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isBlank(filterText)) {
            return true;
        }
        Evaluado objEvaluado = (Evaluado) value;

        return objEvaluado.getPaTxDescripcion().toLowerCase().contains(filterText)
                || objEvaluado.getPaTxCorreo().toLowerCase().contains(filterText)
                || objEvaluado.getPaTxNombreCargo().toLowerCase().contains(filterText);
    }

    public void cargarAnalisisEnviados() {
        try {

            this.lstReporteGenerado = objReporteGeneradoDAO.obtenListaReporteGenerado(Utilitarios.obtenerUsuario().getUsuario().getUsIdCuentaPk(), Utilitarios.obtenerProyecto().getIntIdProyecto());

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void descargarContenido(ReporteGenerado objReporteGenerado) {
        try {

            objReporteGenerado = objReporteGeneradoDAO.obtenReporteGenerado(objReporteGenerado.getRgReportePk());

            List<AnalisisParticipante> objAnalisisParticipantes = objAnalisisParticipanteDAO.obtenListaAnalisisParticipante(objReporteGenerado.getRgReportePk());

            List<String> lstArchivosPorZipear = new ArrayList();

            for (AnalisisParticipante objAnalisisParticipante : objAnalisisParticipantes) {
                File fl = new File(Utilitarios.getPathTempPreliminar() + File.separator + objAnalisisParticipante.getApFilename());
                fl.deleteOnExit();
                FileUtils.writeByteArrayToFile(fl,objAnalisisParticipante.getApArchivo());
                lstArchivosPorZipear.add(objAnalisisParticipante.getApFilename());
            }

            File objFile = new File(Utilitarios.getPathTempDefinitivo() + File.separator + objReporteGenerado.getRgTxNombreArchivo());

            objFile.deleteOnExit();
            
            FileOutputStream objFileOutput = new FileOutputStream(objFile);

            Utilitarios.zipArchivosCualquiera(lstArchivosPorZipear, objFileOutput);

            InputStream targetStream = new FileInputStream(objFile);
            fileIndividual = DefaultStreamedContent.builder()
                    .name(objReporteGenerado.getRgTxNombreArchivo())
                    .contentType("application/zip")
                    .stream(() -> targetStream)
                    .build();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }
}
