package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.orm.Componente;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReporteTodasRespuestas extends BaseView implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteTodasRespuestas.class);
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    ResourceBundle rb = ResourceBundle.getBundle("etiquetas");

    public String build(DatosReporte objDatosReporte, Map map, Integer idCuestionario, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {
        try {

            List lstRptas = this.resultadoDAO.obtieneListaTodasLasRespuestas(idCuestionario, objReporteGenerado);

            String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_XLSX;
            objDatosReporte.setStrID(strNombreReporte);

            generaExcelRespuesta(strNombreReporte, lstRptas, objDatosReporte, objReporteGenerado);

            return strNombreReporte;

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

        return null;

    }

    public void generaExcelRespuesta(String strNombreReporte, List lstRptas, DatosReporte objDatosReporte, ReporteGenerado objReporteGenerado) {

        ComponenteDAO objComponenteDAO = new ComponenteDAO();
        List<Componente> lstComponente = objComponenteDAO.listaComponenteProyectoTipo(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA, null);

        XSSFWorkbook xlsRespuestas = new XSSFWorkbook();
        XSSFSheet hoja = xlsRespuestas.createSheet(rb.getString("answers"));

        Map mapColumnas = creaCabecera(lstComponente, hoja, xlsRespuestas);

        Iterator itLstRptas = lstRptas.iterator();

        int i = 1;

        String strKey = Constantes.strVacio;

        XSSFRow nextrow = null;

        while (itLstRptas.hasNext()) {

            Object[] obj = (Object[]) itLstRptas.next();

            if (!strKey.equals(obj[1].toString() + obj[7].toString() + obj[8].toString())) {
                strKey = obj[1].toString() + obj[7].toString() + obj[8].toString();

                nextrow = hoja.createRow(i);
                if (Utilitarios.noEsNuloOVacio(obj[1])) {
                    nextrow.createCell(0).setCellValue(obj[1].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[2])) {
                    nextrow.createCell(1).setCellValue(obj[2].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[3])) {
                    nextrow.createCell(2).setCellValue(obj[3].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[4])) {
                    nextrow.createCell(3).setCellValue(obj[4].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[5])) {
                    nextrow.createCell(4).setCellValue(obj[5].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[6])) {
                    nextrow.createCell(5).setCellValue(obj[6].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[7])) {
                    nextrow.createCell(6).setCellValue(obj[7].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[9])) {
                    nextrow.createCell(7).setCellValue(obj[9].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[10])) {
                    nextrow.createCell(8).setCellValue(obj[10].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[11])) {
                    nextrow.createCell(9).setCellValue(obj[11].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[12])) {
                    nextrow.createCell(10).setCellValue(obj[12].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[13])) {
                    nextrow.createCell(11).setCellValue(obj[13].toString());
                }
                if (Utilitarios.noEsNuloOVacio(obj[14])) {
                    nextrow.createCell(12).setCellValue(obj[14].toString());
                }

                if (Utilitarios.noEsNuloOVacio(obj[15])) {

                    if (mapColumnas.containsKey(obj[15].toString())) {
                        int pos = (int) mapColumnas.get(obj[15].toString());
                        nextrow.createCell(pos).setCellType(CellType.NUMERIC);
                        nextrow.createCell(pos).setCellValue(obj[17].toString());

                    }

                }

                i++;

            } else {

                if (Utilitarios.noEsNuloOVacio(obj[15])) {

                    if (mapColumnas.containsKey(obj[15].toString())) {
                        int pos = (int) mapColumnas.get(obj[15].toString());
                        nextrow.createCell(pos).setCellType(CellType.NUMERIC);
                        nextrow.createCell(pos).setCellValue(obj[17].toString());

                    }

                }

            }

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);
        hoja.autoSizeColumn(4);
        hoja.autoSizeColumn(5);
        hoja.autoSizeColumn(6);
        hoja.autoSizeColumn(7);
        hoja.autoSizeColumn(8);
        hoja.autoSizeColumn(9);
        hoja.autoSizeColumn(10);
        hoja.autoSizeColumn(12);

        try {

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

    private Map creaCabecera(List<Componente> lstComponente, XSSFSheet hoja, XSSFWorkbook xlsRespuestas) {

        Map mapColumnas = new HashMap();

        XSSFRow row = hoja.createRow(0);
        XSSFFont hSSFFont = xlsRespuestas.createFont();
        //hSSFFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); 
        hSSFFont.setBold(true);
        XSSFCellStyle myStyle = xlsRespuestas.createCellStyle();
        myStyle.setFont(hSSFFont);

        int i = 0;

        XSSFCell cell0 = row.createCell(i++);
        XSSFRichTextString texto0 = new XSSFRichTextString(rb.getString("evaluated"));
        cell0.setCellValue(texto0);
        cell0.setCellStyle(myStyle);

        XSSFCell cell1 = row.createCell(i++);
        XSSFRichTextString texto1 = new XSSFRichTextString(rb.getString("sex") + " (" + rb.getString("evaluated") + ")");
        cell1.setCellValue(texto1);
        cell1.setCellStyle(myStyle);

        XSSFCell cell2 = row.createCell(i++);
        XSSFRichTextString texto2 = new XSSFRichTextString(rb.getString("age") + " (" + rb.getString("evaluated") + ")");
        cell2.setCellValue(texto2);
        cell2.setCellStyle(myStyle);

        XSSFCell cell3 = row.createCell(i++);
        XSSFRichTextString texto3 = new XSSFRichTextString(rb.getString("hiring.time") + " (" + rb.getString("evaluated") + ")");
        cell3.setCellValue(texto3);
        cell3.setCellStyle(myStyle);

        XSSFCell cell4 = row.createCell(i++);
        XSSFRichTextString texto4 = new XSSFRichTextString(rb.getString("work.range") + " (" + rb.getString("evaluated") + ")");
        cell4.setCellValue(texto4);
        cell4.setCellStyle(myStyle);

        XSSFCell cell5 = row.createCell(i++);
        XSSFRichTextString texto5 = new XSSFRichTextString(rb.getString("working.area") + " (" + rb.getString("evaluated") + ")");
        cell5.setCellValue(texto5);
        cell5.setCellStyle(myStyle);

        XSSFCell cell6 = row.createCell(i++);
        XSSFRichTextString texto6 = new XSSFRichTextString(rb.getString("relationship"));
        cell6.setCellValue(texto6);
        cell6.setCellStyle(myStyle);

        XSSFCell cell7 = row.createCell(i++);
        XSSFRichTextString texto7 = new XSSFRichTextString(rb.getString("evaluator"));
        cell7.setCellValue(texto7);
        cell7.setCellStyle(myStyle);

        XSSFCell cell8 = row.createCell(i++);
        XSSFRichTextString texto8 = new XSSFRichTextString(rb.getString("sex") + " (" + rb.getString("evaluator") + ")");
        cell8.setCellValue(texto8);
        cell8.setCellStyle(myStyle);

        XSSFCell cell9 = row.createCell(i++);
        XSSFRichTextString texto9 = new XSSFRichTextString(rb.getString("age") + " (" + rb.getString("evaluator") + ")");
        cell9.setCellValue(texto9);
        cell9.setCellStyle(myStyle);

        XSSFCell cell10 = row.createCell(i++);
        XSSFRichTextString texto10 = new XSSFRichTextString(rb.getString("hiring.time") + " (" + rb.getString("evaluator") + ")");
        cell10.setCellValue(texto10);
        cell10.setCellStyle(myStyle);

        XSSFCell cell11 = row.createCell(i++);
        XSSFRichTextString texto11 = new XSSFRichTextString(rb.getString("work.range") + " (" + rb.getString("evaluator") + ")");
        cell11.setCellValue(texto11);
        cell11.setCellStyle(myStyle);

        XSSFCell cell12 = row.createCell(i++);
        XSSFRichTextString texto12 = new XSSFRichTextString(rb.getString("working.area") + " (" + rb.getString("evaluator") + ")");
        cell12.setCellValue(texto12);
        cell12.setCellStyle(myStyle);

        int c = 0;
        int p = 0;

        Integer pkCategoriaAnt = Integer.parseInt("-1");

        for (Componente objComponente : lstComponente) {

            if (!pkCategoriaAnt.equals(objComponente.getComponente().getCoIdComponentePk())) {
                c++;
                p = 1;
                pkCategoriaAnt = objComponente.getComponente().getCoIdComponentePk();
            }

            XSSFCell cell = row.createCell(i++);
            XSSFRichTextString texto = new XSSFRichTextString(c + "." + p++);
            cell.setCellValue(texto);
            cell.setCellStyle(myStyle);

            mapColumnas.put(objComponente.getCoIdComponentePk().toString(), i - 1);

        }

        return mapColumnas;

    }

}
