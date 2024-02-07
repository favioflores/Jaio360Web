package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
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
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;

public class ReporteIndividualSumarioCategoriaMismoRelacionWeighted implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteIndividualSumarioCategoriaMismoRelacionWeighted.class);

    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

        try {

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(ModeloGeneral.generaCabeceraConMetricas(map, medida, this.objDatosReporte))
                    .summary(generaContenido(intEvaluadoPk, objReporteGenerado))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        } finally {
            medida.close();
        }

        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Double> cantidad = col.column("Cantidad", "cantidad", type.doubleType());

        /* CREA BARRAS DE COLORES */
        Map<String, Color> seriesColors = new HashMap();

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            seriesColors.put(objRelacion.getReTxAbreviatura(), Utilitarios.convertColorHexToRgb(objRelacion.getReColor()));
        }

        List<Componente> lstComponente = componenteDao.listaComponenteProyectoTipoOrdenado(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null, intEvaluadoPk);

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        int i = 1;

        for (Componente objComponente : lstComponente) {

            multiPageList.add(cmp.horizontalList(cmp.verticalList(//cmp.text(i+". " +objComponente.getCoTxDescripcion()),
                    cmp.text(objComponente.getCoTxDescripcion().toUpperCase()).setStyle(ModeloGeneral.styleTextoRegular),
                    cmp.horizontalList(cht.barChart().setCategory(evaluacion)
                            .seriesColorsByName(seriesColors)
                            .series(cht.serie(cantidad).setSeries(relacion))
                            .setDataSource(createDataSourceBar(objComponente, intEvaluadoPk, objReporteGenerado))
                            .setOrientation(Orientation.HORIZONTAL)
                            .setLegendPosition(Position.RIGHT)
                            .setShowLegend(Boolean.FALSE)
                            .setShowLabels(Boolean.FALSE)
                            .setShowValues(Boolean.FALSE)
                            .setShowTickLabels(Boolean.FALSE)
                            .setHeight(10)/* es util, en relacion de 10 por 1 categoria*/
                            .setWidth(350)
                            .setShowTickMarks(Boolean.FALSE)
                            .addCustomizer(new ReporteIndividualSumarioCategoriaMismoRelacionWeighted.ChartCustomizerBar()),
                            crearDatosDelGrafico(objComponente, intEvaluadoPk, objReporteGenerado)
                    ))), cmp.verticalGap(15));

            i++;

        }

        multiPageList.newPage();

        return multiPageList;

    }

    private ComponentBuilder<?, ?> crearDatosDelGrafico(Componente objComponente, Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        VerticalListBuilder datos = cmp.verticalList();

        //List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismoRelacion(objComponente, intEvaluadoPk);
        List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismoRelacionWeighted(objComponente, intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        BigDecimal bdFrec;
        boolean blExiste;

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            blExiste = false;
            bdFrec = new BigDecimal(0);

            Iterator itLstResultadoss = lstResultadoFinal.iterator();

            while (itLstResultadoss.hasNext()) {

                Object[] obj = (Object[]) itLstResultadoss.next();

                if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
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
                        cmp.text(objRelacion.getReTxAbreviatura()).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(140),
                        cmp.text(Utilitarios.truncateTheDecimal(bdFrec, 2)).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(120),
                        cmp.text("0").setStyle(ModeloGeneral.styleContenidoDatos).setWidth(65))
                );
            }

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

    private JRDataSource createDataSourceBar(Componente objComponente, Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");

        List<ResultadoInfo> lstResultadoInfo = new ArrayList<>();

        List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismoRelacion(objComponente, intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        boolean blExiste;

        while (it.hasNext()) {

            blExiste = false;

            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            Iterator itLstResultadoss = lstResultadoFinal.iterator();

            while (itLstResultadoss.hasNext()) {

                Object[] obj = (Object[]) itLstResultadoss.next();

                if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                    ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                    objResultadoInfoTmp.setDeNomRelacion(obj[0].toString());
                    objResultadoInfoTmp.setDeNuOrden(new BigDecimal(obj[1].toString()));
                    lstResultadoInfo.add(objResultadoInfoTmp);
                    blExiste = true;
                }

            }

            if (!blExiste) {
                ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                objResultadoInfoTmp.setDeNomRelacion(objRelacion.getReTxAbreviatura());
                objResultadoInfoTmp.setDeNuOrden(new BigDecimal(0));
                lstResultadoInfo.add(objResultadoInfoTmp);
            }

        }

        for (ResultadoInfo objResultadoInfo : lstResultadoInfo) {
            dataSource.add("eva", objResultadoInfo.getDeNomRelacion(), objResultadoInfo.getDeNuOrden().doubleValue());
        }

        return dataSource;

    }

}
