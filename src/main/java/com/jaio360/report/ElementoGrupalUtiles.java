package com.jaio360.report;

import com.jaio360.domain.DatosReporte;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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

public class ElementoGrupalUtiles implements Serializable {

    private static String strNombreReporte = Utilitarios.generaIDReporte();
    private static Integer intMaxRango;

    public String build(Integer intMaxRango, List lstFilesTemp) throws IOException {

        this.intMaxRango = intMaxRango;
 
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PDF).setEncrypted(Boolean.FALSE);

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
                    //.setShowTickLabels(Boolean.FALSE) // OCULTA LOS NÚMEROS DE X
                    .setHeight(8)
                    //.setShowTickMarks(Boolean.FALSE) // OCULTA LAS RAYITAS DEBAJO DE LOS NÚMEROS DE X
                    .addCustomizer(new ChartCustomizerHeader())
                    .setWidth(110))         
                    .toPdf(pdfExporter);

            DatosReporte temppdf = new DatosReporte();
            temppdf.setStrID(strNombreReporte + Constantes.STR_EXTENSION_PDF);
            lstFilesTemp.add(temppdf);

            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PNG);
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
            //plot.setInsets(new RectangleInsets(0, 0, 0, 0));//este sera util

            CategoryPlot categoryPlot = chart.getCategoryPlot();
            //categoryPlot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
            categoryPlot.setRangeGridlinePaint(Color.WHITE);
            categoryPlot.setDomainGridlinesVisible(false);
            categoryPlot.setRangeGridlinesVisible(false);
            categoryPlot.setBackgroundPaint(Color.white);
            categoryPlot.setOutlineVisible(false);
            categoryPlot.setDomainCrosshairVisible(false);

            ValueAxis valueAsix = categoryPlot.getRangeAxis();
            valueAsix.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//este es muy util
            valueAsix.setTickLabelPaint(new Color(0, 117, 189)); // CAMBIA EL COLOR DE LOS VALORES DE X
            valueAsix.setTickMarkPaint(new Color(255, 192, 16)); // CAMBIA EL COLOR A LAS RAYITAS DEBAJO DE LOS VALORES DE X
            valueAsix.setTickMarkStroke(new BasicStroke(1)); // CAMBIA EL TIPO DE LINEA A LAS RAYITAS DEBAJO DE LOS VALORES DE X
            valueAsix.setAxisLinePaint(new Color(255, 192, 16)); // CAMBIA EL COLOR DE LA LINEA DEL EJE X
            valueAsix.setAxisLineStroke(new BasicStroke(1)); // CAMBIA EL TIPO DE LINEA DEL EJE X
            
            
            Font font = new Font("Arial", Font.BOLD, 17);
            valueAsix.setTickLabelFont(font); // PERMITE AJUSTA EL FONT DE LOS VALORES DE X
            
            Plot plotValueAxis = valueAsix.getPlot();
            plotValueAxis.setOutlineVisible(false);
            
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte + Constantes.STR_EXTENSION_PNG);

            try {
                ChartUtilities.saveChartAsPNG(file, chart, 430, 28, info);
            } catch (IOException ex) {
                Logger.getLogger(ElementoGrupalUtiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
