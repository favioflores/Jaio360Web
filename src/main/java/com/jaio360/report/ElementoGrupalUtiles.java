package com.jaio360.report;

import com.jaio360.domain.DatosReporte;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;

public class ElementoGrupalUtiles implements Serializable {

    private static String strNombreReporte = Utilitarios.generaIDReporte();
    private static Integer intMaxRango;

    public String build(Integer intMaxRango, List lstFilesTemp) throws IOException {

        this.intMaxRango = intMaxRango;

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PDF).setEncrypted(Boolean.FALSE);

        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Integer> cantidad = col.column("Cantidad", "cantidad", type.integerType());

        Map<String, Color> seriesColors = new HashMap<>();
        seriesColors.put("Cabecera", Color.BLACK);
        try {
            /* GENERA LINEA DE MEDIDA */
            report().summary(cht.barChart()
                    .setCategory(evaluacion)
                    .seriesColorsByName(seriesColors)
                    .series(cht.serie(cantidad)
                            .setSeries(relacion))
                    .setDataSource(createDataSourceHeader())
                    .setOrientation(Orientation.HORIZONTAL)
                    .setLegendPosition(Position.RIGHT)
                    .setShowLegend(Boolean.FALSE)
                    .setShowLabels(Boolean.FALSE)
                    .setShowValues(Boolean.FALSE)
                    //.setShowTickLabels(Boolean.FALSE)
                    .setHeight(8)
                    //.setShowTickMarks(Boolean.FALSE)
                    .setCustomizer(new ChartCustomizerHeader())
                    .setWidth(110)).toPdf(pdfExporter);

            DatosReporte temppdf = new DatosReporte();
            temppdf.setStrID(strNombreReporte + Constantes.STR_EXTENSION_PDF);
            lstFilesTemp.add(temppdf);

            File file = new File(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PNG);
            if (file.exists()) {
                DatosReporte temp = new DatosReporte();
                temp.setStrID(strNombreReporte + Constantes.STR_EXTENSION_PNG);
                lstFilesTemp.add(temp);
            }

        } catch (DRException ex) {
            Logger.getLogger(ElementoGrupalUtiles.class.getName()).log(Level.SEVERE, null, ex);
        }

        return strNombreReporte;
    }

    private JRDataSource createDataSourceHeader() {
        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");
        dataSource.add("Ev1", "Cabecera", intMaxRango);
        return dataSource;
    }

    public class ChartCustomizerHeader implements DRIChartCustomizer, Serializable {

        public static final long serialVersionUID = 1L;

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {

            BarRenderer barRenderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            barRenderer.setBaseSeriesVisible(false);
            barRenderer.setBaseSeriesVisibleInLegend(false);
            barRenderer.setDrawBarOutline(false);
            barRenderer.setItemMargin(0);

            CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
            domainAxis.setUpperMargin(0);
            domainAxis.setLowerMargin(0);
            domainAxis.setCategoryMargin(0);
            domainAxis.setAxisLineVisible(false);//este es util
            domainAxis.setTickMarksVisible(false);
            domainAxis.setTickLabelsVisible(false);

            Plot plot = chart.getPlot();
            plot.setOutlineVisible(false);
            plot.setInsets(new RectangleInsets(0, 0, 0, 0));//este sera util

            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
            categoryPlot.setRangeGridlinePaint(Color.WHITE);
            categoryPlot.setDomainGridlinesVisible(false);
            categoryPlot.setRangeGridlinesVisible(false);
            categoryPlot.setBackgroundPaint(Color.white);
            categoryPlot.setOutlineVisible(false);
            categoryPlot.setDomainGridlinesVisible(false);
            categoryPlot.setDomainCrosshairVisible(false);

            ValueAxis valueAsix = categoryPlot.getRangeAxis();
            valueAsix.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//este es muy util

            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

            File file = new File(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PNG);

            try {
                ChartUtilities.saveChartAsPNG(file, chart, 426, 17, info);
            } catch (IOException ex) {
                Logger.getLogger(ElementoGrupalUtiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
