package com.jaio360.view;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.Categorias;
import com.jaio360.domain.Evaluado;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.elements.Elements;
import org.primefaces.model.charts.optionconfig.elements.ElementsLine;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;

/**
 *
 * @author Favio
 */
@Named(value = "dashboardDetailView")
@ViewScoped
public class DashboardDetailView extends BaseView implements Serializable {

    private static final Logger log = Logger.getLogger(DashboardDetailView.class);

    private List<Cuestionario> lstCuestionarios;
    private Cuestionario objCuestionario;
    private Participante objParticipante;

    private List<Evaluado> lstEvaluadosSeleccionados;
    private StreamedContent planAccion;

    private List<Componente> lstCategorias;
    private Integer intCategoriaOrder;
    private List<Integer> lstCategoriasSeleccionadas;
    private Integer intOrderAscDesc;

    //LVL 1
    private List<Evaluado> lstEvaluados;
    private HorizontalBarChartModel barModelPrincipal;

    //LVL 2
    private RadarChartModel chartRadarPrincipal;

    //VISTAS
    private Boolean isCategoryViewActive;

    public Boolean getIsCategoryViewActive() {
        return isCategoryViewActive;
    }

    public Integer getIntCategoriaOrder() {
        return intCategoriaOrder;
    }

    public void setIntCategoriaOrder(Integer intCategoriaOrder) {
        this.intCategoriaOrder = intCategoriaOrder;
    }

    public Integer getIntOrderAscDesc() {
        return intOrderAscDesc;
    }

    public void setIntOrderAscDesc(Integer intOrderAscDesc) {
        this.intOrderAscDesc = intOrderAscDesc;
    }

    public void setIsCategoryViewActive(Boolean isCategoryViewActive) {
        this.isCategoryViewActive = isCategoryViewActive;
    }

    public Participante getObjParticipante() {
        return objParticipante;
    }

    public void setObjParticipante(Participante objParticipante) {
        this.objParticipante = objParticipante;
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

    public List<Evaluado> getLstEvaluadosSeleccionados() {
        return lstEvaluadosSeleccionados;
    }

    public Cuestionario getObjCuestionario() {
        return objCuestionario;
    }

    public void setObjCuestionario(Cuestionario objCuestionario) {
        this.objCuestionario = objCuestionario;
    }

    public void setLstEvaluadosSeleccionados(List<Evaluado> lstEvaluadosSeleccionados) {
        this.lstEvaluadosSeleccionados = lstEvaluadosSeleccionados;
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

    public HorizontalBarChartModel getBarModelPrincipal() {
        return barModelPrincipal;
    }

    public void setBarModelPrincipal(HorizontalBarChartModel barModelPrincipal) {
        this.barModelPrincipal = barModelPrincipal;
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

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String idParticipant = params.get("idEvaluated");

        if (Utilitarios.noEsNuloOVacio(idParticipant)) {

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

            this.objParticipante = objParticipanteDAO.obtenParticipante(Integer.parseInt(idParticipant));
            this.objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk(), Utilitarios.obtenerProyecto().getIntIdProyecto());
            loadCategories();
            cargarRadarPrincipal(this.objParticipante);
            cargarBarrasSumarioXCategorias(this.objParticipante);

        } else {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getExternalContext().redirect("dashboard.jsf");
            } catch (Exception ex) {
                mostrarError(log, ex);
            }
        }

    }

    private void getAssessments() {
        try {
            this.isCategoryViewActive = false;
            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
            lstCuestionarios = objCuestionarioDAO.obtenListaCuestionarioXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
        } catch (Exception e) {
            mostrarError(log, e);
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

    public void cargarDashboard() {
        //cargarRadarPrincipal();
        //cargarBarrasSumarioXCategorias();
    }

    private void cargarRadarPrincipal(Participante objParticipante) {

        chartRadarPrincipal = new RadarChartModel();

        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        //this.lstCategoriasSeleccionadas;
        LinkedHashMap mapRelaciones = cargarRelaciones(objParticipante);

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        ChartData data = new ChartData();

        List<List<String>> labels = new ArrayList<>();

        for (Componente objComponente : lstCategorias) {
            labels.add(new ArrayList(Arrays.asList(objComponente.getCoTxDescripcion())));
        }

        data.setLabels(labels);

        Iterator itMapRelaciones = mapRelaciones.entrySet().iterator();

        List lstResultadoFinal = objResultadoDAO.resultadoPorRelacionYCategoria(objParticipante.getPaIdParticipantePk());

        while (itMapRelaciones.hasNext()) {

            Map.Entry entry = (Map.Entry) itMapRelaciones.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            RadarChartDataSet radarDataSet = new RadarChartDataSet();
            radarDataSet.setLabel(objRelacion.getReTxDescripcion());
            radarDataSet.setTension(0.1);
            radarDataSet.setFill(false);
            radarDataSet.setBackgroundColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA(objRelacion.getReColor()));
            radarDataSet.setBorderColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(objRelacion.getReColor()));
            radarDataSet.setPointBackgroundColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(objRelacion.getReColor()));
            radarDataSet.setPointBorderColor("#fff");
            radarDataSet.setPointHoverRadius(4);
            radarDataSet.setPointRadius(2.5);
            radarDataSet.setBorderWidth(1);
            radarDataSet.setPointHoverBackgroundColor("#fff");
            radarDataSet.setPointHoverBorderColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(objRelacion.getReColor()));

            List<Number> dataVal = new ArrayList<>();

            for (Componente objComponente : lstCategorias) {

                Iterator itLstResultados = lstResultadoFinal.iterator();

                if (objRelacion.getReTxAbreviatura().equals("REQ(*)")) {

                    dataVal.add(objComponente.getCoNrPuntajeRequerido());

                } else {

                    boolean blResultadoEncontrado = false;

                    while (itLstResultados.hasNext()) {

                        Object[] obj = (Object[]) itLstResultados.next();

                        if (objComponente.getCoIdComponentePk().toString().equals(obj[1].toString())
                                && objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                            dataVal.add(new BigDecimal(obj[2].toString()));
                            blResultadoEncontrado = true;
                            break;
                        }

                    }

                    if (!blResultadoEncontrado) {
                        dataVal.add(BigDecimal.ZERO);
                    }

                }

            }

            radarDataSet.setData(dataVal);
            data.addChartDataSet(radarDataSet);
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
        //ticks.setMaxTicksLimit(intMaxRango);
        //ticks.setMin(0);
        ticks.setMax(intMaxRango);
        ticks.setStepSize(1);
        ticks.setDisplay(true);
        ticks.setFontSize(12);
        ticks.setFontStyle("300");
        ticks.setFontFamily("Lato, sans-serif");

        rScales.setStartAngle(0);//
        rScales.setTicks(ticks);
        rScales.setPointLabels(pointLabels);

        /*
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Sumario de Categoria");
        options.setTitle(title);
         */
        Elements elements = new Elements();
        ElementsLine elementsLine = new ElementsLine();
        elementsLine.setTension(0);
        elementsLine.setBorderWidth(3);
        elements.setLine(elementsLine);
        options.setElements(elements);

        options.setMaintainAspectRatio(true);
        options.setResponsive(true);
        options.setScales(rScales);

        chartRadarPrincipal.setOptions(options);
        chartRadarPrincipal.setData(data);
        chartRadarPrincipal.setExtender("skinRadarChart");
    }

    public RadarChartModel getChartRadarPrincipal() {
        return chartRadarPrincipal;
    }

    public void setChartRadarPrincipal(RadarChartModel chartRadarPrincipal) {
        this.chartRadarPrincipal = chartRadarPrincipal;
    }

    public void loadCategories() {
        if (objCuestionario != null) {
            this.lstCategorias = new ArrayList<>();
            this.intCategoriaOrder = -1;
            ComponenteDAO objComponenteDAO = new ComponenteDAO();
            /*
            Componente objComponente = new Componente();
            objComponente.setCoIdComponentePk(-1);
            objComponente.setCoTxDescripcion("General");
            this.lstCategorias.add(objComponente);
            */
            this.lstCategorias.addAll(objComponenteDAO.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objCuestionario.getCuIdCuestionarioPk(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null));
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
            objRelacion.setReTxDescripcion("AUTOEVALUACIÓN");
            objRelacion.setReTxNombre("AUTOEVALUACIÓN");
            objRelacion.setReColor("#212324");
            mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        Relacion objRelacion = new Relacion();
        objRelacion.setReIdRelacionPk(-1);
        objRelacion.setReNuOrden(intMaxRango + 1);
        objRelacion.setReTxAbreviatura("PROM");
        objRelacion.setReTxDescripcion("PROMEDIO");
        objRelacion.setReTxNombre("PROMEDIO");
        objRelacion.setReColor("#909090");
        mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);

        objRelacion = new Relacion();
        objRelacion.setReIdRelacionPk(-1);
        objRelacion.setReNuOrden(intMaxRango + 1);
        objRelacion.setReTxAbreviatura("REQ(*)");
        objRelacion.setReTxDescripcion("REQUERIDO");
        objRelacion.setReTxNombre("REQUERIDO");
        objRelacion.setReColor("#ff0000");
        mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);

        mapRelaciones.putAll(obtieneRelaciones(objParticipante.getPaIdParticipantePk()));

        return mapRelaciones;
    }

    public void cargarBarrasSumarioXCategorias(Participante objParticipante) {

        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        barModelPrincipal = new HorizontalBarChartModel();

        ChartData data = new ChartData();

        LinkedHashMap mapRelaciones = cargarRelaciones(objParticipante);

        Iterator itMapRelaciones = mapRelaciones.entrySet().iterator();

        List lstResultadoFinal = objResultadoDAO.resultadoPorRelacionYCategoria(objParticipante.getPaIdParticipantePk());

        while (itMapRelaciones.hasNext()) {

            Map.Entry entry = (Map.Entry) itMapRelaciones.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            BarChartDataSet barDataSet = new HorizontalBarChartDataSet();
            barDataSet.setLabel(objRelacion.getReTxNombre());
            barDataSet.setBorderWidth(0.5);
            barDataSet.setBackgroundColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA(objRelacion.getReColor()));
            barDataSet.setBorderColor(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(objRelacion.getReColor()));

            List<Number> values = new ArrayList<>();

            for (Componente objComponente : lstCategorias) {

                if (objRelacion.getReTxAbreviatura().equals("REQ(*)")) {

                    values.add(objComponente.getCoNrPuntajeRequerido());

                } else {
                    List lstResultadoXCategoria = objResultadoDAO.getResultRelacionByCategoria(objComponente.getCoIdComponentePk(), objParticipante.getPaIdParticipantePk());

                    //List<String> bgColor = new ArrayList<>();
                    //List<String> borderColor = new ArrayList<>();
                    //bgColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGBA(strColorPreferencial));
                    //borderColor.add(Utilitarios.convertColorHexToRgbChartPrimefacesRGB(strColorPreferencial));
                    if (!lstResultadoXCategoria.isEmpty()) {

                        Iterator itLstResultadoXCategoria = lstResultadoXCategoria.iterator();

                        BigDecimal bdScore = BigDecimal.ZERO;

                        while (itLstResultadoXCategoria.hasNext()) {
                            Object obj[] = (Object[]) itLstResultadoXCategoria.next();

                            if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                                bdScore = new BigDecimal(obj[1].toString());
                                break;
                            }

                        }

                        values.add(bdScore.doubleValue());

                    } else {
                        values.add(0);
                    }
                }

            }

            barDataSet.setData(values);
            data.addChartDataSet(barDataSet);

        }

        List<String> labels = new ArrayList<>();

        for (Componente objComponente : lstCategorias) {
            labels.add(objComponente.getCoTxDescripcion());
        }

        data.setLabels(labels);

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        barModelPrincipal.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        options.setMaintainAspectRatio(false);

        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        linearAxes.setMax(intMaxRango);
        linearAxes.setMin(0);
        //linearAxes.setStacked(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        //ticks.setMaxTicksLimit(1);
        ticks.setStepSize(1);
        ticks.setLabelOffset(1);
        linearAxes.setTicks(ticks);

        CartesianScales cScales = new CartesianScales();
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
        legendLabels.setFontSize(12);
        legend.setLabels(legendLabels);
        options.setLegend(legend);
        //barModelPrincipal.setExtender("barExtender");

        // disable animation
        /*
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);
         */
        barModelPrincipal.setOptions(options);
    }

    public void onRowSelectAssessment(SelectEvent<Cuestionario> event) {
        try {
            this.isCategoryViewActive = true;
            this.intOrderAscDesc = 0;

            loadCategories();
            loadEvaluators();
            orderLstEvaluados(0, false);
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    private void loadEvaluators() {
        try {
            this.lstEvaluados = new ArrayList<>();

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            ComponenteDAO objComponenteDAO = new ComponenteDAO();

            List<Componente> lstResultByEvaluator;
            Evaluado objEvaluado;

            List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);

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
                        objEvaluado.setInAnalizado(1);
                    } else {
                        objEvaluado.setInAnalizado(0);
                    }
                } else {
                    objEvaluado.setBlAnalizado(false);
                    objEvaluado.setInAnalizado(0);
                }

                lstResultByEvaluator = objComponenteDAO.listaComponenteProyectoTipoOrdenadoForDashboard(
                        Utilitarios.obtenerProyecto().getIntIdProyecto(),
                        this.objCuestionario.getCuIdCuestionarioPk(),
                        Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null, objParticipante.getPaIdParticipantePk());

                List<Categorias> lstCategoriesByEvaluated = new ArrayList<>();

                for (Componente objComponente : lstResultByEvaluator) {
                    Categorias objCategorias = new Categorias(
                            objComponente.getCoTxDescripcion(),
                            objComponente.getCoIdComponentePk(),
                            null,
                            objComponente.getCoNrPuntajeRequerido().setScale(2, RoundingMode.HALF_DOWN),
                            objComponente.getCoNrPuntajeMinimoRequerido().setScale(2, RoundingMode.HALF_DOWN),
                            objComponente.getCoNrPuntajeObtenido().setScale(2, RoundingMode.HALF_DOWN)
                    );
                    lstCategoriesByEvaluated.add(objCategorias);
                }

                objEvaluado.setLstCategoryResult(lstCategoriesByEvaluated);

                lstEvaluados.add(objEvaluado);

            }

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void onRowUnselectAssessment(UnselectEvent<Cuestionario> event) {
        try {
            this.isCategoryViewActive = false;
            blankDashboard();
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    private void blankDashboard() {
        try {
            this.lstCategorias.clear();
            this.lstCategoriasSeleccionadas.clear();
            this.lstEvaluados.clear();
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    private void orderLstEvaluados(Integer columnIndex, Boolean isAsc) {

        try {

            if (isAsc) {

                Collections.sort(lstEvaluados,
                        Comparator.comparing((Evaluado evaluado) -> evaluado.getLstCategoryResult().get(columnIndex).getBdScoreResult())
                );

            } else {

                Collections.sort(lstEvaluados,
                        Comparator.comparing((Evaluado evaluado) -> evaluado.getLstCategoryResult().get(columnIndex).getBdScoreResult()).reversed()
                );

            }
        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void updateOrder() {
        try {

            boolean blOrderAsc = true;

            Integer indexColumn = 0;

            for (Componente objComponente : lstCategorias) {
                if (objComponente.getCoIdComponentePk().equals(this.intCategoriaOrder)) {
                    break;
                }
                indexColumn++;
            }

            if (intOrderAscDesc.equals(1)) {
                blOrderAsc = false;
            }
            orderLstEvaluados(indexColumn, blOrderAsc);

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void goToDetailView() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboardDetail.jsf");
        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }
}
