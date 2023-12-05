package com.jaio360.view;

import com.jaio360.component.ExecutorBalanceMovement;
import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.Participante;
import com.jaio360.orm.ReferenciaMovimiento;
import com.jaio360.orm.Relacion;
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
import com.jaio360.report.ReporteIndividualItemsAltaCalificacion;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionMismo;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacion;
import com.jaio360.report.ReporteIndividualSumarioCategoria;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoRelacion;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosProm;
import com.jaio360.report.ReporteIndividualSumarioCategoriaRelacion;
import com.jaio360.report.ReporteTodasRespuestas;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
import com.jaio360.utils.ReportSort;
import com.jaio360.utils.Utilitarios;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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


import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "dashboardView")
@ViewScoped
public class DashboardView extends BaseView implements Serializable {

    private static final Logger log = Logger.getLogger(DashboardView.class);

    private List<SelectItem> lstContenidoIndividual;
    private List<SelectItem> lstContenidoGrupal;
    private List<Integer> lstSeleccionadosIndividual;
    private List<Integer> lstSeleccionadosGrupal;

    private List<Evaluado> lstEvaluados;
    private List<Cuestionario> lstCuestionarios;
    private List<Cuestionario> lstCuestionariosSeleccionados;
    private List<Evaluado> lstEvaluadosSeleccionados;
    private List<Cuestionario> lstFiltroCuestionarios;
    private List<Participante> lstFiltroEvaluados;

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

    private RadarChartModel chartRadar;
    private BarChartModel barModel;

    public List<SelectItem> getLstContenidoIndividual() {
        return lstContenidoIndividual;
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
        String fullPath = servletContext.getRealPath(File.separator + "resources" + File.separator + "other" + File.separator + "PlanDeAccion.docx");
        File objFile = new File(fullPath);
        planAccion = DefaultStreamedContent.builder()
                .name("PlanDeAccion.docx")
                .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                .build();

        return planAccion;
    }

    @PostConstruct
    public void init() {

        chartRadar = new RadarChartModel();
        barModel = new BarChartModel();

        cargarCategorias();

        lstContenidoIndividual = new ArrayList<>();
        lstContenidoGrupal = new ArrayList<>();

        for (int i = 1; i <= 11; i++) {
            lstContenidoIndividual.add(new SelectItem(i, msg("report." + i)));
        }

        //Collections.sort(lstContenidoIndividual, new ReportOptionsSort());
        for (int i = 100; i <= 102; i++) {
            lstContenidoGrupal.add(new SelectItem(i, msg("report." + i)));
        }

        //Collections.sort(lstContenidoGrupal, new ReportOptionsSort());
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
                init();
                
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (HibernateException e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void cargarDashboard() {
        crearRadar();
        cargarBarrasSumarioXCategorias();
    }

    private void crearRadar() {

        chartRadar = new RadarChartModel();

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        ComponenteDAO componenteDao = new ComponenteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        Participante objParticipante = objParticipanteDAO.obtenParticipante(this.intEvaluadoSeleccionado);

        //this.lstCategoriasSeleccionadas;
        LinkedHashMap mapRelaciones = cargarRelaciones(objParticipante);

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk(), Utilitarios.obtenerProyecto().getIntIdProyecto());

        List<Componente> lstResultadoXCategoria = componenteDao.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objCuestionario.getCuIdCuestionarioPk(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null);

        ChartData data = new ChartData();

        List<List<String>> labels = new ArrayList<>();

        for (Componente objComponente : lstResultadoXCategoria) {
            labels.add(new ArrayList(Arrays.asList(objComponente.getCoTxDescripcion())));
        }

        data.setLabels(labels);

        Iterator itMapRelaciones = mapRelaciones.entrySet().iterator();

        while (itMapRelaciones.hasNext()) {

            Map.Entry entry = (Map.Entry) itMapRelaciones.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            if ("PROM".equals(objRelacion.getReTxAbreviatura()) || "AUTO".equals(objRelacion.getReTxAbreviatura())) {
                continue;
            }

            RadarChartDataSet radarDataSet = new RadarChartDataSet();
            radarDataSet.setLabel(objRelacion.getReTxDescripcion());
            radarDataSet.setTension(0.1);
            radarDataSet.setBackgroundColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA("#" + objRelacion.getReColor()));
            radarDataSet.setBorderColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB("#" + objRelacion.getReColor()));
            radarDataSet.setPointBackgroundColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB("#" + objRelacion.getReColor()));
            radarDataSet.setPointBorderColor("#fff");
            radarDataSet.setPointHoverRadius(5);
            radarDataSet.setPointHoverBackgroundColor("#fff");
            radarDataSet.setPointHoverBorderColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB("#" + objRelacion.getReColor()));

            List lstResultadoFinal = objResultadoDAO.resultadoPorRelacionYCategoriaNoAutoevaluacionNoProm(objParticipante.getPaIdParticipantePk(), objRelacion.getReIdRelacionPk());

            Iterator itLstResultados = lstResultadoFinal.iterator();

            List<Number> dataVal = new ArrayList<>();

            boolean blAlMenosUno = false;

            while (itLstResultados.hasNext()) {

                Object[] obj = (Object[]) itLstResultados.next();

                if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                    dataVal.add(new BigDecimal(obj[2].toString()));
                    blAlMenosUno = true;
                }

            }

            if (blAlMenosUno) {
                radarDataSet.setData(dataVal);
                data.addChartDataSet(radarDataSet);
            }
        }

        /* Options */
        RadarChartOptions options = new RadarChartOptions();
        RadialScales rScales = new RadialScales();

        RadialLinearAngleLines angleLines = new RadialLinearAngleLines();
        angleLines.setDisplay(true);
        angleLines.setLineWidth(0.5);
        angleLines.setColor("rgba(128, 128, 128, 0.2)");
        rScales.setAngleLines(angleLines);

        RadialLinearPointLabels pointLabels = new RadialLinearPointLabels();
        pointLabels.setFontSize(14);
        pointLabels.setFontStyle("300");
        pointLabels.setFontColor("rgba(204, 204, 204, 1)");
        pointLabels.setFontFamily("Lato, sans-serif");

        RadialLinearTicks ticks = new RadialLinearTicks();
        ticks.setBeginAtZero(true);
        ticks.setMaxTicksLimit(intMaxRango + 1);
        ticks.setMin(0);
        ticks.setMax(intMaxRango + 1);
        //ticks.setStepSize(0.5); 
        ticks.setDisplay(true);
        ticks.setFontSize(14);
        ticks.setFontStyle("300");
        ticks.setFontFamily("Lato, sans-serif");

        rScales.setTicks(ticks);
        rScales.setPointLabels(pointLabels);

        /*
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Sumario de Categoria");
        options.setTitle(title);
         */
        options.setScales(rScales);

        chartRadar.setOptions(options);
        chartRadar.setData(data);
        chartRadar.setExtender("skinRadarChart");
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
            mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        Relacion objRelacion = new Relacion();
        objRelacion.setReIdRelacionPk(-1);
        objRelacion.setReNuOrden(intMaxRango + 1);
        objRelacion.setReTxAbreviatura("PROM");
        objRelacion.setReTxDescripcion("Promedio");
        objRelacion.setReTxNombre("Promedio");
        objRelacion.setReColor("494D4F");
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

            bgColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA("#" + strColorPreferencial));
            borderColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGB("#" + strColorPreferencial));

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
}
