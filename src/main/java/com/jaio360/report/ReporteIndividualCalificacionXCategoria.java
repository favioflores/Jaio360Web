package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ResultadoInfo;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;

import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;

public class ReporteIndividualCalificacionXCategoria implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteIndividualCalificacionXCategoria.class);

    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;
    List lstRel = new ArrayList();

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;

        try {

            List<Componente> lstCategorias = componenteDao.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null);

            List<String> lstArchivos = new ArrayList();

            for (Componente objComponente : lstCategorias) {

                String strNombreReporteC = strNameFile + objComponente.getCoIdComponentePk() + Constantes.STR_EXTENSION_PDF;
                JasperPdfExporterBuilder pdfExporterC = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporteC)
                        .setEncrypted(Boolean.FALSE);

                InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

                report().setTemplate(ModeloGeneral.reportTemplate)
                        .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                        .pageHeader(ModeloGeneral.generaCabeceraConMetricasConComponentes(map, objComponente, medida, this.objDatosReporte))
                        .summary(generaContenido(intEvaluadoPk, objComponente))
                        .pageFooter(ModeloGeneral.generaPie(map))
                        .toPdf(pdfExporterC);

                medida.close();

                lstArchivos.add(strNombreReporteC);

            }

            strNombreReporte = Utilitarios.combinaReportesTemporalesPDF(lstArchivos);
            Utilitarios.eliminaArchivosTemporalesByPath(lstArchivos);

        } catch (DRException ex) {
            log.error(ex);
        }

        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk, Componente objCategoria) {

        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Double> cantidad = col.column("Cantidad", "cantidad", type.doubleType());

        /* CREA BARRAS DE COLORES */
        Map<String, Color> seriesColors = new HashMap();

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        lstRel = new ArrayList();

        if (objDatosReporte.getMapRelaciones().containsKey("AUTO")) {
            seriesColors.put("AUTO", Utilitarios.convertColorHexToRgb("#000000"));
            lstRel.add("AUTO");
        }

        seriesColors.put("PROM", Utilitarios.convertColorHexToRgb("#585858"));
        lstRel.add("PROM");

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            if (!objRelacion.getReTxAbreviatura().equals("PROM") && !objRelacion.getReTxAbreviatura().equals("AUTO")) {
                seriesColors.put(objRelacion.getReTxAbreviatura(), Utilitarios.convertColorHexToRgb("#" + objRelacion.getReColor()));
                lstRel.add(objRelacion.getReTxAbreviatura());
            }
        }

        List<Componente> lstComponente = componenteDao.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA, objCategoria);
        List lstComentarios = this.resultadoDAO.obtieneListaResultadoComentarios(intEvaluadoPk);

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        int i = 1;

        VerticalListBuilder vComentarios;

        for (Componente objComponente : lstComponente) {

            Iterator itComentarios = lstComentarios.iterator();

            vComentarios = cmp.verticalList();

            vComentarios.add(cmp.text("Comentarios").setStyle(ModeloGeneral.styleTercerTitulo));

            boolean commentsEmpty = true;

            while (itComentarios.hasNext()) {
                Object[] obj = (Object[]) itComentarios.next();

                if (obj[2] != null) {
                    if (obj[2].toString().equals(objComponente.getCoIdComponentePk().toString())) {
                        vComentarios.add(cmp.text("- " + obj[1].toString()).setStyle(ModeloGeneral.styleComentarios));
                        commentsEmpty = false;
                    }
                }
            }

            if (commentsEmpty) {
                vComentarios.add(cmp.text("- No tiene comentarios").setStyle(ModeloGeneral.styleComentarios));
            }

            vComentarios.add(cmp.verticalGap(15));

            multiPageList.add(
                    cmp.horizontalList(
                            cmp.verticalList(
                                    cmp.text(objComponente.getCoTxDescripcion().toUpperCase()).setStyle(ModeloGeneral.styleTextoRegular),
                                    cmp.horizontalList(
                                            cht.barChart().setCategory(evaluacion)
                                                    .seriesColorsByName(seriesColors)
                                                    .series(cht.serie(cantidad).setSeries(relacion))
                                                    .setDataSource(createDataSourceBar(objComponente, intEvaluadoPk))
                                                    .setOrientation(Orientation.HORIZONTAL)
                                                    .setLegendPosition(Position.RIGHT)
                                                    .setShowLegend(Boolean.FALSE)
                                                    .setShowLabels(Boolean.FALSE)
                                                    .setShowValues(Boolean.FALSE)
                                                    .setShowTickLabels(Boolean.FALSE)
                                                    .setHeight(10)/* es util, en relacion de 10 por 1 categoria*/
                                                    .setWidth(350)
                                                    .setShowTickMarks(Boolean.FALSE)
                                                    .addCustomizer(new ReporteIndividualCalificacionXCategoria.ChartCustomizerBar()),
                                            crearDatosDelGrafico(objComponente, intEvaluadoPk)
                                    ))), vComentarios);

            i++;

        }

        multiPageList.newPage();

        return multiPageList;

    }

    private ComponentBuilder<?, ?> crearDatosDelGrafico(Componente objComponente, Integer intEvaluadoPk) {

        VerticalListBuilder datos = cmp.verticalList();

        List lstResultadoFinal = this.resultadoDAO.listaReporteUno(objComponente, intEvaluadoPk);

        //Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();
        Iterator it = lstRel.iterator();
        Map map = objDatosReporte.getMapRelaciones();

        BigDecimal bdFrec;
        boolean blExiste;

        while (it.hasNext()) {

            blExiste = false;
            bdFrec = new BigDecimal(0);

            String rel = (String) it.next();

            //if(!objRelacion.getReTxAbreviatura().equals("PROM")){
            Iterator itLstResultadoss = lstResultadoFinal.iterator();

            while (itLstResultadoss.hasNext()) {

                Object[] obj = (Object[]) itLstResultadoss.next();

                if (rel.equals(obj[0].toString())) {
                    blExiste = true;
                    bdFrec = new BigDecimal(obj[1].toString());
                    datos.add(cmp.horizontalList(
                            cmp.horizontalGap(30),
                            cmp.text(obj[0].toString()).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(140),
                            cmp.text(Utilitarios.truncateTheDecimal(bdFrec, 2)).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(120),
                            cmp.text(obj[2].toString()).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(65))
                    );
                }
            }

            if (!blExiste) {
                datos.add(cmp.horizontalList(
                        cmp.horizontalGap(30),
                        cmp.text(rel).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(140),
                        cmp.text(Utilitarios.truncateTheDecimal(bdFrec, 2)).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(120),
                        cmp.text("0").setStyle(ModeloGeneral.styleContenidoDatos).setWidth(65))
                );
            }
            //}
        }

        return datos;
    }

    public class ChartCustomizerBar implements DRIChartCustomizer, Serializable {

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {

            BarRenderer barRenderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            //barRenderer.setBaseSeriesVisible(false);//QUITA LAS BARRAS
            //barRenderer.setBaseSeriesVisibleInLegend(false);
            //barRenderer.setDrawBarOutline(false);
            barRenderer.setShadowVisible(false);
            barRenderer.setBarPainter(new CustomBarPainter());
            barRenderer.setItemMargin(0);//QUITA ESPACIOS ENTRE BARRA Y BARRA

            CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
            domainAxis.setUpperMargin(0);
            domainAxis.setLowerMargin(0);
            domainAxis.setCategoryMargin(0);
            domainAxis.setAxisLineVisible(false);//este es util

            Plot plot = chart.getPlot();
            //plot.setOutlineVisible(false);
            plot.setInsets(new RectangleInsets(0, 0, 0, 0));//este sera util

            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
            categoryPlot.setDomainGridlinesVisible(false);
            categoryPlot.setRangeGridlinesVisible(true);// Muestra las lineas punteadas entre las barras
            categoryPlot.setRangeGridlinePaint(ModeloGeneral.colorJAIOYellow);

            //categoryPlot.setBackgroundPaint(Color.white);
            categoryPlot.setOutlineVisible(false);

            ValueAxis valueAsix = categoryPlot.getRangeAxis();
            valueAsix.setAxisLineVisible(false);//este es muy util
            valueAsix.setRange(0, objDatosReporte.getIntMaxRango());
            valueAsix.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//este es muy util

        }
    }

    private JRDataSource createDataSourceBar(Componente objComponente, Integer intEvaluadoPk) {

        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");

        List<ResultadoInfo> lstResultadoInfo = new ArrayList<>();

        List lstResultadoFinal = this.resultadoDAO.listaReporteUno(objComponente, intEvaluadoPk);

        //Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();
        Iterator it = lstRel.iterator();

        boolean blExiste;

        while (it.hasNext()) {

            blExiste = false;

            String rel = (String) it.next();

            //if(!objRelacion.getReTxAbreviatura().equals("PROM")){
            Iterator itLstResultadoss = lstResultadoFinal.iterator();

            while (itLstResultadoss.hasNext()) {

                Object[] obj = (Object[]) itLstResultadoss.next();

                if (rel.equals(obj[0].toString())) {
                    ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                    objResultadoInfoTmp.setDeNomRelacion(obj[0].toString());
                    objResultadoInfoTmp.setDeNuOrden(new BigDecimal(obj[1].toString()));
                    lstResultadoInfo.add(objResultadoInfoTmp);
                    blExiste = true;
                }

            }

            if (!blExiste) {
                ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                objResultadoInfoTmp.setDeNomRelacion(rel);
                objResultadoInfoTmp.setDeNuOrden(new BigDecimal(0));
                lstResultadoInfo.add(objResultadoInfoTmp);
            }

            //}
        }

        for (ResultadoInfo objResultadoInfo : lstResultadoInfo) {
            dataSource.add("eva", objResultadoInfo.getDeNomRelacion(), objResultadoInfo.getDeNuOrden().doubleValue());
        }

        return dataSource;
    }

}
