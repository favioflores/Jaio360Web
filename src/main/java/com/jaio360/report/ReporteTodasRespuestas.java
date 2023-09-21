package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.orm.Componente;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class ReporteTodasRespuestas extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteTodasRespuestas.class);
    ResultadoDAO resultadoDAO = new ResultadoDAO();

    public String build(DatosReporte objDatosReporte, Map map, Integer idCuestionario, String strNameFile) throws IOException {

        List lstRptas = this.resultadoDAO.obtieneListaTodasLasRespuestas(idCuestionario);

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_XLS;
        objDatosReporte.setStrID(strNombreReporte);

        generaExcelRespuesta(strNombreReporte, lstRptas, objDatosReporte);

        return strNombreReporte;
    }

    public void generaExcelRespuesta(String strNombreReporte, List lstRptas, DatosReporte objDatosReporte) {

        ComponenteDAO objComponenteDAO = new ComponenteDAO();
        List<Componente> lstComponente = objComponenteDAO.listaComponenteProyectoTipo(Utilitarios.obtenerProyecto().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA, null);

        HSSFWorkbook xlsRespuestas = new HSSFWorkbook();
        HSSFSheet hoja = xlsRespuestas.createSheet(msg("answers"));

        Map mapColumnas = creaCabecera(lstComponente, hoja, xlsRespuestas);

        Iterator itLstRptas = lstRptas.iterator();

        int i = 1;

        String strKey = Constantes.strVacio;

        HSSFRow nextrow = null;

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

        } catch (IOException ex) {
            mostrarError(log, ex);
        }

    }

    private Map creaCabecera(List<Componente> lstComponente, HSSFSheet hoja, HSSFWorkbook xlsRespuestas) {

        Map mapColumnas = new HashMap();

        HSSFRow row = hoja.createRow(0);
        HSSFFont hSSFFont = xlsRespuestas.createFont();
        //hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
        hSSFFont.setBold(true);
        HSSFCellStyle myStyle = xlsRespuestas.createCellStyle();
        myStyle.setFont(hSSFFont);

        int i = 0;

        HSSFCell cell0 = row.createCell(i++);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("evaluated"));
        cell0.setCellValue(texto0);
        cell0.setCellStyle(myStyle);

        HSSFCell cell1 = row.createCell(i++);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("sex") + " (" + msg("evaluated") + ")");
        cell1.setCellValue(texto1);
        cell1.setCellStyle(myStyle);

        HSSFCell cell2 = row.createCell(i++);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("age") + " (" + msg("evaluated") + ")");
        cell2.setCellValue(texto2);
        cell2.setCellStyle(myStyle);

        HSSFCell cell3 = row.createCell(i++);
        HSSFRichTextString texto3 = new HSSFRichTextString(msg("hiring.time") + " (" + msg("evaluated") + ")");
        cell3.setCellValue(texto3);
        cell3.setCellStyle(myStyle);

        HSSFCell cell4 = row.createCell(i++);
        HSSFRichTextString texto4 = new HSSFRichTextString(msg("work.range") + " (" + msg("evaluated") + ")");
        cell4.setCellValue(texto4);
        cell4.setCellStyle(myStyle);

        HSSFCell cell5 = row.createCell(i++);
        HSSFRichTextString texto5 = new HSSFRichTextString(msg("working.area") + " (" + msg("evaluated") + ")");
        cell5.setCellValue(texto5);
        cell5.setCellStyle(myStyle);

        HSSFCell cell6 = row.createCell(i++);
        HSSFRichTextString texto6 = new HSSFRichTextString(msg("relationship"));
        cell6.setCellValue(texto6);
        cell6.setCellStyle(myStyle);

        HSSFCell cell7 = row.createCell(i++);
        HSSFRichTextString texto7 = new HSSFRichTextString(msg("evaluator"));
        cell7.setCellValue(texto7);
        cell7.setCellStyle(myStyle);

        HSSFCell cell8 = row.createCell(i++);
        HSSFRichTextString texto8 = new HSSFRichTextString(msg("sex") + " (" + msg("evaluator") + ")");
        cell8.setCellValue(texto8);
        cell8.setCellStyle(myStyle);

        HSSFCell cell9 = row.createCell(i++);
        HSSFRichTextString texto9 = new HSSFRichTextString(msg("age") + " (" + msg("evaluator") + ")");
        cell9.setCellValue(texto9);
        cell9.setCellStyle(myStyle);

        HSSFCell cell10 = row.createCell(i++);
        HSSFRichTextString texto10 = new HSSFRichTextString(msg("hiring.time") + " (" + msg("evaluator") + ")");
        cell10.setCellValue(texto10);
        cell10.setCellStyle(myStyle);

        HSSFCell cell11 = row.createCell(i++);
        HSSFRichTextString texto11 = new HSSFRichTextString(msg("work.range") + " (" + msg("evaluator") + ")");
        cell11.setCellValue(texto11);
        cell11.setCellStyle(myStyle);

        HSSFCell cell12 = row.createCell(i++);
        HSSFRichTextString texto12 = new HSSFRichTextString(msg("working.area") + " (" + msg("evaluator") + ")");
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

            HSSFCell cell = row.createCell(i++);
            HSSFRichTextString texto = new HSSFRichTextString(c + "." + p++);
            cell.setCellValue(texto);
            cell.setCellStyle(myStyle);

            mapColumnas.put(objComponente.getCoIdComponentePk().toString(), i - 1);

        }

        return mapColumnas;

    }

}
