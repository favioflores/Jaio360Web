package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
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

public class ReporteGrupalNivelParticipacion implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteGrupalNivelParticipacion.class);

    DatosReporte objDatosReporte;
    ResourceBundle rb = ResourceBundle.getBundle("etiquetas");

    public String build(DatosReporte objDatosReporte, Map map, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_XLSX;

        try {

            this.objDatosReporte = objDatosReporte;

            objDatosReporte.setStrID(strNombreReporte);

            ResultadoDAO objResultadoDAO = new ResultadoDAO();

            List lstResultado = objResultadoDAO.listaReporteNivelParticipacion(objDatosReporte.getIntIdCuestionario(), objReporteGenerado);

            generaExcelRespuesta(strNombreReporte, lstResultado, objDatosReporte);

            /*
        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        ResultadoDAO objResultadoDAO = new ResultadoDAO();
        List lstResultado = objResultadoDAO.listaReporteNivelParticipacion(objDatosReporte.getIntIdCuestionario(), objReporteGenerado);

        TextColumnBuilder<String> nombreEvaluadoColumn = col.column("EVALUADO", "nombres", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas.setHorizontalAlignment(HorizontalAlignment.LEFT));
        TextColumnBuilder<String> personasColumn = col.column("NO. PERSONAS", "personas", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);
        TextColumnBuilder<String> respuestasColumn = col.column("NO. RESPUESTAS", "respuestas", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);
        TextColumnBuilder<String> participacionColumn = col.column("PARTICIPACIÓN", "participacion", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);

        try {

            objDatosReporte.setStrCuestionario("");

            report()
                    .setTemplate(ModeloGeneral.reportTemplate)
                    .columns(nombreEvaluadoColumn, personasColumn, respuestasColumn, participacionColumn)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .setDataSource(createDataSource(lstResultado))
                    .setSummaryStyle(ModeloGeneral.styleContenidoDatos)
                    .toPdf(pdfExporter);
             */
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

        return strNombreReporte;
    }

    private JRDataSource createDataSource(List lstResultado) {

        DRDataSource dataSource = new DRDataSource("nombres", "personas", "respuestas", "participacion");

        Iterator itLstResultado = lstResultado.iterator();

        Integer total;
        Integer resueltos;
        BigDecimal porcentaje;

        while (itLstResultado.hasNext()) {

            Object obj[] = (Object[]) itLstResultado.next();

            if (Utilitarios.esNuloOVacio(obj[2])) {
                total = 0;
            } else {
                total = new Integer(obj[2].toString());
            }

            if (Utilitarios.esNuloOVacio(obj[3])) {
                resueltos = 0;
            } else {
                resueltos = new Integer(obj[3].toString());
            }

            porcentaje = new BigDecimal((resueltos.doubleValue() / total.doubleValue()) * 100);

            dataSource.add(obj[1], total.toString(), resueltos.toString(), porcentaje.setScale(2, RoundingMode.HALF_UP) + "%");//porcentaje.multiply(new BigDecimal(100)).setScale(2)+"%");   
        }

        return dataSource;

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
                    nextrow.createCell(0).setCellValue(obj[1].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[4])) {
                    nextrow.createCell(1).setCellValue(obj[4].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[2])) {
                    nextrow.createCell(2).setCellValue(obj[2].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[3])) {
                    nextrow.createCell(3).setCellValue(obj[3].toString());
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
            XSSFRichTextString texto0 = new XSSFRichTextString(rb.getString("evaluated"));
            cell0.setCellValue(texto0);
            cell0.setCellStyle(myStyle);

            XSSFCell cell1 = row.createCell(i++);
            XSSFRichTextString texto1 = new XSSFRichTextString(rb.getString("email"));
            cell1.setCellValue(texto1);
            cell1.setCellStyle(myStyle);

            XSSFCell cell2 = row.createCell(i++);
            XSSFRichTextString texto2 = new XSSFRichTextString(rb.getString("no.personas"));
            cell2.setCellValue(texto2);
            cell2.setCellStyle(myStyle);

            XSSFCell cell3 = row.createCell(i++);
            XSSFRichTextString texto3 = new XSSFRichTextString(rb.getString("no.respuestas"));
            cell3.setCellValue(texto3);
            cell3.setCellStyle(myStyle);

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

    }

}
