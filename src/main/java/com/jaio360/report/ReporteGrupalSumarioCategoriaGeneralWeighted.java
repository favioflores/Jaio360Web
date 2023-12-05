package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;

public class ReporteGrupalSumarioCategoriaGeneralWeighted extends BaseView implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteGrupalSumarioCategoriaGeneralWeighted.class);

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;
    ResourceBundle rb = ResourceBundle.getBundle("etiquetas");

    public String build(DatosReporte objDatosReporte, Map map, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_XLSX;

        try {

            this.objDatosReporte = objDatosReporte;

            objDatosReporte.setStrID(strNombreReporte);

            List lstDatos = resultadoDAO.listaGrupalSumarioCategoriaGeneralWeighted(objDatosReporte, objReporteGenerado);

            generaExcelRespuesta(strNombreReporte, lstDatos, objDatosReporte);

            /*
        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

        

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(generaCabecera(map, medida))
                    .summary(generaContenido(objReporteGenerado))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);


             */
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(ReporteGenerado objReporteGenerado) {

        List lstDatos = resultadoDAO.listaGrupalSumarioCategoriaGeneral(objDatosReporte, objReporteGenerado);

        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Double> cantidad = col.column("Cantidad", "cantidad", type.doubleType());
        Map<String, Color> seriesColors;

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        if (!lstDatos.isEmpty()) {

            Iterator itLstDatos = lstDatos.iterator();

            String keyTit = "";
            boolean primeraVez = true;
            int contCat = 0;
            int contador = 0;
            BigDecimal bdProm;

            while (itLstDatos.hasNext()) {

                seriesColors = new HashMap();
                seriesColors.put(rb.getString("prom"), Utilitarios.convertColorHexToRgb("#" + Utilitarios.generaColorHtmlPreferencial(contador)));
                contador++;

                Object obj[] = (Object[]) itLstDatos.next();

                if (!keyTit.equals(obj[2].toString())) {
                    if (!primeraVez) {
                        multiPageList.add(cmp.verticalGap(10));
                    }
                    multiPageList.add(cmp.horizontalList(
                            cmp.text(obj[1].toString()).setStyle(ModeloGeneral.styleTextoRegular)
                    )
                    );
                    keyTit = obj[2].toString();
                    primeraVez = false;
                    contCat++;
                }
                bdProm = new BigDecimal("0");
                if (Utilitarios.noEsNuloOVacio(obj[4])) {
                    bdProm = new BigDecimal(obj[4].toString()).setScale(2, RoundingMode.FLOOR);
                }

                String strDesc;
                if (Utilitarios.noEsNuloOVacio(obj[3])) {
                    strDesc = obj[3].toString();
                } else {
                    strDesc = rb.getString("empty.desc");
                }

                multiPageList.add(cmp.horizontalList(cht.barChart().setCategory(evaluacion)
                        .seriesColorsByName(seriesColors)
                        .series(cht.serie(cantidad).setSeries(relacion))
                        .setDataSource(createDataSourceBar(bdProm))
                        .setOrientation(Orientation.HORIZONTAL)
                        .setLegendPosition(Position.RIGHT)
                        .setShowLegend(Boolean.FALSE)
                        .setShowLabels(Boolean.FALSE)
                        .setShowValues(Boolean.FALSE)
                        .setShowTickLabels(Boolean.FALSE)
                        .setHeight(10)/* es util, en relacion de 10 por 1 categoria*/
                        .setWidth(115)
                        .setShowTickMarks(Boolean.FALSE)
                        .addCustomizer(new ReporteGrupalSumarioCategoriaGeneralWeighted.ChartCustomizerBar()),
                        cmp.horizontalGap(20),
                        cmp.horizontalList(
                                cmp.text(Utilitarios.truncateTheDecimal(bdProm, 2)).setStyle(ModeloGeneral.styleContenidoDatos)
                        ).setWidth(35),
                        cmp.horizontalList(
                                cmp.text(strDesc).setStyle(ModeloGeneral.styleContenidoDatos)
                        ).setWidth(107)
                )
                );
            }

        } else {
            multiPageList.add(cmp.text(rb.getString("empty.desc")));
        }

        multiPageList.newPage();

        return multiPageList;

    }

    private JRDataSource createDataSourceBar(BigDecimal bdDato) {
        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");
        dataSource.add("eva", rb.getString("prom"), bdDato.doubleValue());
        return dataSource;
    }

    private ComponentBuilder<?, ?> generaCabecera(Map map, InputStream medida) throws FileNotFoundException {

        return cmp.verticalList(
                cmp.verticalGap(5),
                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion().toUpperCase()).setStyle(ModeloGeneral.styleTituloReporte)
                ),
                cmp.verticalGap(10),
                cmp.horizontalList(
                        cmp.image(medida).setFixedDimension(225, 20),
                        cmp.horizontalGap(50),
                        cmp.text(rb.getString("prom").toUpperCase()).setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(30),
                        cmp.text(rb.getString("evaluated").toUpperCase()).setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(270)
                ),
                cmp.verticalGap(5)
        );
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

    public void generaExcelRespuesta(String strNombreReporte, List lstRptas, DatosReporte objDatosReporte) {

        try {
            //ComponenteDAO objComponenteDAO = new ComponenteDAO();
            //List<Componente> lstComponente = objComponenteDAO.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA, null);
            XSSFWorkbook xlsRespuestas = new XSSFWorkbook();
            XSSFSheet hoja = xlsRespuestas.createSheet(rb.getString("resume"));

            creaCabecera(hoja, xlsRespuestas);

            Iterator itLstRptas = lstRptas.iterator();

            int i = 1;

            XSSFRow nextrow;

            while (itLstRptas.hasNext()) {

                Object[] obj = (Object[]) itLstRptas.next();

                nextrow = hoja.createRow(i);

                if (Utilitarios.noEsNuloOVacio(obj[1])) {
                    nextrow.createCell(0).setCellValue(obj[2].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[2])) {
                    nextrow.createCell(1).setCellValue(obj[3].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[3])) {
                    nextrow.createCell(2).setCellValue(obj[5].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[4])) {
                    nextrow.createCell(3).setCellType(CellType.NUMERIC);
                    nextrow.createCell(3).setCellValue(obj[4].toString());
                }

                i++;
            }

            hoja.autoSizeColumn(
                    0);
            hoja.autoSizeColumn(
                    1);
            hoja.autoSizeColumn(
                    2);
            hoja.autoSizeColumn(
                    3);

            String rutaArchivo = Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID();
            File archivoXLS = new File(rutaArchivo);

            archivoXLS.createNewFile();

            FileOutputStream archivo = new FileOutputStream(archivoXLS);

            xlsRespuestas.write(archivo);

            archivo.flush();
            archivo.close();

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

    }

    private void creaCabecera(XSSFSheet hoja, XSSFWorkbook xlsRespuestas) {

        try {

            XSSFRow row = hoja.createRow(0);
            XSSFFont hSSFFont = xlsRespuestas.createFont();
            //hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
            hSSFFont.setBold(true);
            XSSFCellStyle myStyle = xlsRespuestas.createCellStyle();
            myStyle.setFont(hSSFFont);

            int i = 0;

            XSSFCell cell0 = row.createCell(i++);
            XSSFRichTextString texto0 = new XSSFRichTextString(rb.getString("category"));
            cell0.setCellValue(texto0);
            cell0.setCellStyle(myStyle);

            XSSFCell cell1 = row.createCell(i++);
            XSSFRichTextString texto1 = new XSSFRichTextString(rb.getString("evaluated"));
            cell1.setCellValue(texto1);
            cell1.setCellStyle(myStyle);

            XSSFCell cell2 = row.createCell(i++);
            XSSFRichTextString texto2 = new XSSFRichTextString(rb.getString("email"));
            cell2.setCellValue(texto2);
            cell2.setCellStyle(myStyle);

            XSSFCell cell3 = row.createCell(i++);
            XSSFRichTextString texto3 = new XSSFRichTextString(rb.getString("prom.weighted"));
            cell3.setCellValue(texto3);
            cell3.setCellStyle(myStyle);

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

    }

}
