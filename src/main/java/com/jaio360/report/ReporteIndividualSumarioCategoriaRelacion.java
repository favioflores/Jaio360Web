package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.chart.SpiderChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;

public class ReporteIndividualSumarioCategoriaRelacion implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteIndividualSumarioCategoriaRelacion.class);

    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            TextColumnBuilder<String> categorias = col.column("Categorias", "categoria", type.stringType());

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    .summary(generaContenido(intEvaluadoPk, categorias, objReporteGenerado))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        }
        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk, TextColumnBuilder<String> categorias, ReporteGenerado objReporteGenerado) {

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        List lstItems = resultadoDAO.listaItemsBajaPromedioMismo(intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        SpiderChartBuilder spider = cht.spiderChart()
                .setCategory(categorias)
                //.addCustomizer(new ReporteIndividualSumarioCategoriaRelacion.ChartCustomizer())
                .setDataSource(createDataSource(intEvaluadoPk, objReporteGenerado)
                );

        /* RELACIONES */
        Map<String, Color> seriesColors = new HashMap();

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            seriesColors.put(objRelacion.getReTxAbreviatura(), Utilitarios.convertColorHexToRgb("#" + objRelacion.getReColor()));
            TextColumnBuilder<BigDecimal> relacion = col.column(objRelacion.getReTxNombre(), objRelacion.getReTxAbreviatura(), type.bigDecimalType());
            spider.addSerie(cht.serie(relacion));

        }

        multiPageList.add(spider);

        return multiPageList;

    }

    private JRDataSource createDataSource(Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        List<Componente> lstResultadoXCategoria = componenteDao.listaComponenteProyectoTipo(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null);

        ArrayList<String> columns = new ArrayList<>();
        columns.add("categoria");

        Iterator itMapRelaciones = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (itMapRelaciones.hasNext()) {
            Map.Entry entry = (Map.Entry) itMapRelaciones.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            columns.add(objRelacion.getReTxAbreviatura());
        }

        DRDataSource dataSource = new DRDataSource(columns.stream().toArray(String[]::new));

        ArrayList<Object> metricasXRelacion;

        boolean blExiste;
        BigDecimal bdFrec;

        for (Componente objComponente : lstResultadoXCategoria) {

            List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismo(objComponente, intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

            itMapRelaciones = objDatosReporte.getMapRelaciones().entrySet().iterator();

            metricasXRelacion = new ArrayList<>();

            metricasXRelacion.add(objComponente.getCoTxDescripcion());

            while (itMapRelaciones.hasNext()) {
                Map.Entry entry = (Map.Entry) itMapRelaciones.next();
                Relacion objRelacion = (Relacion) entry.getValue();

                blExiste = false;
                bdFrec = new BigDecimal(0);

                Iterator itLstResultadoss = lstResultadoFinal.iterator();

                while (itLstResultadoss.hasNext()) {

                    Object[] obj = (Object[]) itLstResultadoss.next();

                    if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                        blExiste = true;
                        bdFrec = new BigDecimal(obj[1].toString());
                        metricasXRelacion.add(bdFrec);
                    }

                }

                if (!blExiste) {
                    metricasXRelacion.add(bdFrec);
                }

            }

            dataSource.add(metricasXRelacion.get(0),
                    metricasXRelacion.get(1),
                    metricasXRelacion.get(2),
                    metricasXRelacion.get(3));

        }

        return dataSource;

    }

    public class ChartCustomizer implements DRIChartCustomizer, Serializable {

        public void customize(JFreeChart chart, ReportParameters reportParameters) {

            chart.getTitle();
            //CategoryPlot categoryPlot = chart.getCategoryPlot();

            //BarRenderer barRenderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            //barRenderer.setBaseSeriesVisible(false);//QUITA LAS BARRAS
            //barRenderer.setBaseSeriesVisibleInLegend(false);
            //barRenderer.setDrawBarOutline(false);
            //barRenderer.setShadowVisible(false);
            //barRenderer.setBarPainter(new CustomBarPainter());
            //barRenderer.setItemMargin(0);//QUITA ESPACIOS ENTRE BARRA Y BARRA
            //CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
            //domainAxis.setUpperMargin(0);
            //domainAxis.setLowerMargin(0);
            //domainAxis.setCategoryMargin(0);
            //domainAxis.setAxisLineVisible(false);//este es util
            //Plot plot = chart.getPlot();
            //plot.setOutlineVisible(false);
            //plot.setInsets(new RectangleInsets(0, 0, 0, 0));//este sera util
            //CategoryPlot categoryPlot = chart.getCategoryPlot();
            //categoryPlot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
            //categoryPlot.setDomainGridlinesVisible(false);
            //categoryPlot.setRangeGridlinesVisible(true);// Muestra las lineas punteadas entre las barras
            //categoryPlot.setRangeGridlinePaint(ModeloGeneral.colorJAIOYellow);
            //categoryPlot.setBackgroundPaint(Color.white);
            //categoryPlot.setOutlineVisible(false);
            //ValueAxis valueAsix = categoryPlot.getRangeAxis();
            //valueAsix.setAxisLineVisible(false);//este es muy util
            //valueAsix.setRange(0, objDatosReporte.getIntMaxRango());
            //valueAsix.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//este es muy util
        }
    }
}
