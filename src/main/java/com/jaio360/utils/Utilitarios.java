/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Usuario;
import com.jaio360.view.BaseView;
import com.jaio360.view.ListasPrincipalView;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Favio
 */
public class Utilitarios extends BaseView implements Serializable{

    private static Log log = LogFactory.getLog(Utilitarios.class);

    public static Date obtenerFechaHoraSistema() {
        return new Date();
    }

    public static boolean esNuloOVacio(Object obj) {
        return !noEsNuloOVacio(obj);
    }

    public static boolean noEsNuloOVacio(Object obj) {
        if (obj != null && obj.toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    public static String formatearFecha(Date dtFecha, String strFormato) {

        SimpleDateFormat sdfFormato = new SimpleDateFormat(strFormato);
        return sdfFormato.format(dtFecha);

    }

    public static byte[] toByteArray(Blob fromBlob) {
        byte[] resultBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            resultBytes = fromBlob.getBytes(1L, (int) fromBlob.length());
            if (resultBytes == null) {
                resultBytes = toByteArrayImpl(fromBlob, baos);
            }
            return resultBytes;
        } catch (Throwable e) {
            throw new RuntimeException("toByteArray", e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private static byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws Exception {
        byte[] buf = new byte[4000];
        InputStream is = fromBlob.getBinaryStream();
        try {
            for (;;) {
                int dataSize = is.read(buf);

                if (dataSize == -1) {
                    break;
                }
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
        return baos.toByteArray();
    }

    public static UsuarioInfo obtenerUsuario() {

        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (UsuarioInfo) ses.getAttribute("usuarioInfo");

    }

    public static ProyectoInfo obtenerProyecto() {

        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (ProyectoInfo) ses.getAttribute("proyectoInfo");

    }

    public static boolean refrescarProyecto(Integer intIdProyecto) {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("proyectoInfo");

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        List lstProyecto = objProyectoDAO.obtenListaProyectosPorUsuario(obtenerUsuario().getIntUsuarioPk(), intIdProyecto, null,
                null, null, null, null, null, null, null);

        ProyectoInfo objProyectoInfo = new ProyectoInfo();

        if (!lstProyecto.isEmpty()) {

            Iterator itLstProyectos = lstProyecto.iterator();

            Proyecto objProyecto = (Proyecto) itLstProyectos.next();

            objProyectoInfo = ListasPrincipalView.setearProyecto(objProyecto, objProyectoInfo);

        } else {
            return false;
        }

        session.setAttribute("proyectoInfo", objProyectoInfo);

        return true;

    }

    public static ProyectoInfo obtenerRed() {

        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (ProyectoInfo) ses.getAttribute("redInfo");

    }

    public static String generarClave() {

        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@!#$";

        String contrasena = Constantes.strVacio;
        int longitud = base.length();

        for (int i = 0; i < Constantes.INT_LONGITUD_CLAVE_DEFECTO_ACTIVO; i++) {
            int numero = (int) (Math.random() * (longitud));
            String caracter = base.substring(numero, numero + 1);
            contrasena = contrasena + caracter;
        }

        return contrasena;

    }

    public static String generaIDReporte() {

        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String contrasena = Constantes.strVacio;
        int longitud = base.length();

        for (int i = 0; i < Constantes.INT_LONGITUD_ARCHIVOS; i++) {
            int numero = (int) (Math.random() * (longitud));
            String caracter = base.substring(numero, numero + 1);
            contrasena = contrasena + caracter;
        }

        return contrasena;

    }

    public static ProyectoInfo obtenerEvaluacion() {

        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (ProyectoInfo) ses.getAttribute("evalInfo");

    }

    public static String retirarEspacios(String strCadena) {

        if (strCadena != null) {
            while (strCadena.contains(Constantes.strEspacio)) {
                strCadena = strCadena.replaceAll(Constantes.strEspacio, Constantes.strVacio);
            }
        }

        return strCadena;

    }

    public static String limpiarTexto(String strCadena) {

        if (noEsNuloOVacio(strCadena)) {

            while (strCadena.contains(Constantes.strDobleEspacio)) {
                strCadena = strCadena.replaceAll(Constantes.strDobleEspacio, Constantes.strEspacio);
            }

            return strCadena.toUpperCase().trim();

        } else {
            return Constantes.strVacio;
        }

    }

    public static Color convertColorHexToRgb(String colorStr) {

        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static String combinaReportesTemporalesPDF(List<String> list) {

        String IdReporteSalida = Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_PDF;

        try {

            Document document = new Document();

            OutputStream outputStream;

            outputStream = new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + IdReporteSalida);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            for (String strId : list) {
                InputStream in = new FileInputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + strId);
                PdfReader reader = new PdfReader(in);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    document.newPage();
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    cb.addTemplate(page, 0, 0);
                }
                reader.close();
                in.close();
            }

            outputStream.flush();
            document.close();
            writer.close();
            outputStream.close();

        } catch (FileNotFoundException ex) {
            log.error(ex);
            IdReporteSalida = null;
        } catch (IOException | DocumentException ex) {
            log.error(ex);
            IdReporteSalida = null;
        }

        return IdReporteSalida;

    }

    public static String combinaReportesPDFDefinitivos(List<DatosReporte> list, DatosReporte objDatosReporteOriginal) {

        String IdReporteSalida = objDatosReporteOriginal.getStrID() + Constantes.STR_EXTENSION_PDF;

        try {

            Document document = new Document();

            OutputStream outputStream;

            outputStream = new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + IdReporteSalida);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            for (DatosReporte objDatosReporte : list) {
                if (objDatosReporte.isBlDefinitivo()) {
                    InputStream in = new FileInputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + objDatosReporte.getStrID());
                    PdfReader reader = new PdfReader(in);
                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        document.newPage();
                        PdfImportedPage page = writer.getImportedPage(reader, i);
                        cb.addTemplate(page, 0, 0);
                    }
                    reader.close();
                    in.close();
                    objDatosReporte.setBlDefinitivo(false);
                }
            }

            outputStream.flush();
            document.close();
            writer.close();
            outputStream.close();

        } catch (FileNotFoundException ex) {
            log.error(ex);
            IdReporteSalida = null;
        } catch (IOException | DocumentException ex) {
            log.error(ex);
            IdReporteSalida = null;
        }

        return IdReporteSalida;

    }

    public static String putPageNumber(String IdReporteSalida) {

        try {

            Document document = new Document();

            OutputStream outputStream = new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + "Final_" + IdReporteSalida);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            PdfReader reader = new PdfReader(Constantes.STR_INBOX_PRELIMINAR + File.separator + IdReporteSalida);
            reader.makeRemoteNamedDestinationsLocal();
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + "Final " + IdReporteSalida));
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                cb = stamper.getOverContent(i);
                float x = cb.getPdfDocument().getPageSize().getWidth() / 2;

                if (cb.getPdfDocument().getPageSize().equals(PageSize.A4.rotate())) {
                    x = x;
                } else if (cb.getPdfDocument().getPageSize().equals(PageSize.A4)) {
                    if (reader.getPageRotation(i) == 90) {
                        x = cb.getPdfDocument().getPageSize().getHeight() / 2;
                    }
                }
                cb.beginText();
                cb.setFontAndSize(bf, 10);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, i + " de " + n, x, 5, 0);
                cb.endText();
            }
            stamper.close();
            reader.close();

            outputStream.flush();
            //document.close();
            //writer.close();
            outputStream.close();

            IdReporteSalida = "Final " + IdReporteSalida;

        } catch (FileNotFoundException ex) {
            log.error(ex);
            IdReporteSalida = null;
        } catch (IOException | DocumentException ex) {
            log.error(ex);
            IdReporteSalida = null;
        }

        return IdReporteSalida;

    }

    public static void putFooterNumberPAge() throws IOException, DocumentException {

        Document document = new Document();

        OutputStream outputStream;

        outputStream = new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator);

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();

        PdfContentByte cb = writer.getDirectContent();

        PdfReader reader = new PdfReader("C:\\InboxDefinitivo\\FAFO.pdf");
        reader.makeRemoteNamedDestinationsLocal();;
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C:\\InboxDefinitivo\\FAFO3.pdf"));
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            cb = stamper.getOverContent(i);
            float x = cb.getPdfDocument().getPageSize().getWidth() / 2;

            if (cb.getPdfDocument().getPageSize().equals(PageSize.A4.rotate())) {
                x = x;
            } else if (cb.getPdfDocument().getPageSize().equals(PageSize.A4)) {
                if (reader.getPageRotation(i) == 90) {
                    x = cb.getPdfDocument().getPageSize().getHeight() / 2;
                } else {
                    x = x;
                }
            }
            cb.beginText();
            cb.setFontAndSize(bf, 10);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, i
                    + " de " + n, x, 5, 0);
            cb.endText();
        }
        stamper.close();
        reader.close();

    }

    public static String combinaReportesPDF(List<DatosReporte> list, DatosReporte objDatosReporteOriginal) {

        String IdReporteSalida = objDatosReporteOriginal.getStrID() + Constantes.STR_EXTENSION_PDF;

        try {

            Document document = new Document();

            OutputStream outputStream;

            outputStream = new FileOutputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + IdReporteSalida);

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            for (DatosReporte objDatosReporte : list) {
                InputStream in = new FileInputStream(Constantes.STR_INBOX_PRELIMINAR + File.separator + objDatosReporte.getStrID());
                PdfReader reader = new PdfReader(in);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    document.newPage();
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    cb.addTemplate(page, 0, 0);
                }
                reader.close();
                in.close();
            }

            outputStream.flush();
            document.close();
            writer.close();
            outputStream.close();

        } catch (FileNotFoundException ex) {
            log.error(ex);
            IdReporteSalida = null;
        } catch (IOException | DocumentException ex) {
            log.error(ex);
            IdReporteSalida = null;
        }

        return IdReporteSalida;

    }

    public static String obtieneFechaSistema(int type) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        DateFormat dfDateFull = DateFormat.getDateInstance(type, bundle.getLocale());
        Date fecha = new Date();
        return dfDateFull.format(fecha);
    }

    public static Usuario obtenerUsuarioSelecionado() {

        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (Usuario) ses.getAttribute("usuarioSeleccionado");

    }

    public static boolean eliminaArchivosTemporales(List<DatosReporte> lstReportes) {

        boolean blSave = false;

        log.debug("Inicia zipArchivos");

        try {

            for (DatosReporte objDatosReporte : lstReportes) {

                File fl = new File(Constantes.STR_INBOX_PRELIMINAR + File.separator + objDatosReporte.getStrID());

                try {
                    Files.delete(fl.toPath());
                } catch (NoSuchFileException x) {
                    log.error("%s: no such" + " file or directory%n");
                } catch (DirectoryNotEmptyException x) {
                    log.error("%s not empty%n");
                } catch (IOException x) {
                    log.error(x);
                }

            }

            blSave = true;

        } catch (Exception e) {

            blSave = false;
            log.error(e);

        }

        return blSave;
    }

    public static boolean zipArchivos(List<DatosReporte> lstReportes, FileOutputStream outPut) {

        boolean blSave = false;

        try {

            byte[] BUFFER = new byte[1024];

            ZipOutputStream out = new ZipOutputStream(outPut);

            for (DatosReporte objDatosReporte : lstReportes) {

                File fl = new File(Constantes.STR_INBOX_PRELIMINAR + File.separator + objDatosReporte.getStrID());

                if (objDatosReporte.isBlDefinitivo()) {

                    FileInputStream fi = new FileInputStream(fl);

                    ZipEntry entry = new ZipEntry(objDatosReporte.getStrID());

                    out.putNextEntry(entry);

                    int count;

                    while ((count = fi.read(BUFFER)) > 0) {
                        out.write(BUFFER, 0, count);
                    }

                    fi.close();

                }

                try {
                    Files.delete(fl.toPath());
                } catch (NoSuchFileException x) {
                    mostrarError(log, x);
                } catch (DirectoryNotEmptyException x) {
                    mostrarError(log, x);
                } catch (IOException x) {
                    // File permission problems are caught here.
                    mostrarError(log, x);
                }

            }

            out.closeEntry();
            out.close();
            blSave = true;

        } catch (Exception e) {

            blSave = false;
            mostrarError(log, e);

        }

        return blSave;
    }

    public static boolean zipArchivosCualquiera(List<String> lstArchivos, FileOutputStream outPut) {

        boolean blSave = false;

        log.debug("Inicia zipArchivos");

        try {

            byte[] BUFFER = new byte[1024];

            ZipOutputStream out = new ZipOutputStream(outPut);

            for (String strArchivo : lstArchivos) {

                File fl = new File(Constantes.STR_INBOX_PRELIMINAR + File.separator + strArchivo);

                FileInputStream fi = new FileInputStream(fl);

                ZipEntry entry = new ZipEntry(strArchivo);

                out.putNextEntry(entry);

                int count;

                while ((count = fi.read(BUFFER)) > 0) {
                    out.write(BUFFER, 0, count);
                }

                fi.close();

                try {
                    Files.delete(fl.toPath());
                } catch (NoSuchFileException x) {
                    log.error("%s: no such" + " file or directory%n");
                } catch (DirectoryNotEmptyException x) {
                    log.error("%s not empty%n");
                } catch (IOException x) {
                    // File permission problems are caught here.
                    log.error(x);
                }

            }

            out.closeEntry();
            out.close();
            blSave = true;

        } catch (Exception e) {

            blSave = false;
            log.error(e);

        }

        log.debug("Termina zipArchivos");

        return blSave;
    }

    public static String reemplazar(String cadena, String busqueda, String reemplazo) {
        return cadena.replaceAll(busqueda, reemplazo);
    }

    public static String generateRandom(String strNum) {

        StringBuffer strBufRandom = new StringBuffer(strNum);

        String strReal = Constantes.strVacio;

        char chInitalCharacter = strBufRandom.charAt(0);

        char chMiddleCharecter = strBufRandom.charAt(1);

        char chFinalCharecter = strBufRandom.charAt(2);

        if (!Character.isDigit(chFinalCharecter)) {

            // its character
            if (chFinalCharecter == Constantes.CH_RANDOM_Z) {

                if (!Character.isDigit(chMiddleCharecter)) {

                    if (chMiddleCharecter == Constantes.CH_RANDOM_Z) {

                        if (!Character.isDigit(chInitalCharacter)) {

                            if (chInitalCharacter == Constantes.CH_RANDOM_Z) {

                                return null;

                            }

                            chMiddleCharecter = Constantes.CH_RANDOM_A;

                            chFinalCharecter = Constantes.CH_RANDOM_A;

                            strReal = ++chInitalCharacter + Constantes.strVacio + chMiddleCharecter
                                    + Constantes.strVacio + chFinalCharecter;

                        } else {

                            // digit
                            chInitalCharacter = Constantes.CH_RANDOM_A;

                            chMiddleCharecter = Constantes.CH_RANDOM_A;

                            chFinalCharecter = Constantes.CH_RANDOM_A;

                            strReal = chInitalCharacter + Constantes.strVacio + chMiddleCharecter
                                    + Constantes.strVacio + chFinalCharecter;

                        }

                    } else {

                        chFinalCharecter = Constantes.CH_RANDOM_A;

                        strReal = chInitalCharacter + Constantes.strVacio + ++chMiddleCharecter + Constantes.strVacio
                                + chFinalCharecter;

                    }

                } else {

                    chMiddleCharecter = Constantes.CH_RANDOM_A;

                    chFinalCharecter = Constantes.CH_RANDOM_A;

                    strReal = chInitalCharacter + Constantes.strVacio + chMiddleCharecter + Constantes.strVacio
                            + chFinalCharecter;

                }

            } else {

                strReal = chInitalCharacter + Constantes.strVacio + chMiddleCharecter + Constantes.strVacio
                        + ++chFinalCharecter;

            }

        } else {

            chFinalCharecter = Constantes.CH_RANDOM_A;

            strReal = chInitalCharacter + Constantes.strVacio + chMiddleCharecter + Constantes.strVacio
                    + chFinalCharecter;

        }

        return strReal;

    }

    public static Date getCurrentDate() {

        return Calendar.getInstance().getTime();

    }

    public static int getCurrentHour() {

        JaioCalendar objCavCal = new JaioCalendar();

        int CURRENT_HOUR = objCavCal.getInHourOfDay();

        return CURRENT_HOUR;

    }

    public static int getCurrentMinutes() {

        JaioCalendar objCavCal = new JaioCalendar();
        int CURRENT_MINUTES = objCavCal.getInMinute();
        return CURRENT_MINUTES;

    }

    public static String truncateTheDecimal(BigDecimal bdData, int iScale) {

        String strData = bdData.toPlainString();

        String strEntera = null;

        String strDecimal = null;

        int inPos = strData.indexOf(Constantes.STR_POINT);

        if (inPos > 0) {

            strEntera = strData.substring(Constantes.INT_PARAMETER_ZERO_POS, inPos);

            strDecimal = strData.substring(inPos + Constantes.INT_PARAMETER_ONE_POS);

            if (strDecimal.length() > iScale) {

                strDecimal = strDecimal.substring(Constantes.INT_PARAMETER_ZERO_POS, iScale);

            }

        } else {

            strEntera = strData;

            strDecimal = Constantes.ZERO;

        }

        return strEntera + Constantes.STR_POINT + strDecimal;

    }

    public static boolean isNumber(String strNum, boolean blWholeNumber) {
        String strRegExpWholeNumber = "[\\-\\+]?[0-9]*";
        String strRegExpDecimalNumber = "[\\-\\+]?[0-9]*\\.?[0-9]*";
        String strRegExpNotNumber = "[\\-\\+]?[0-9]*\\.+";
        if (strNum != null) {
            if (strNum.indexOf("E") != -1 || strNum.indexOf("e") != -1) {
                Double dblNum = null;
                try {
                    dblNum = new Double(strNum);
                } catch (NumberFormatException nfe) {
                    // is not a number
                    return false;
                }
                if (dblNum.isNaN()) {
                    return false;
                }
                return true;
            }
            if (blWholeNumber) {
                if (strNum.trim().length() > 0 && strNum.matches(strRegExpWholeNumber) && !strNum.matches(strRegExpNotNumber)) {
                    return true;
                }
            } else {
                if (strNum.trim().length() > 0 && strNum.matches(strRegExpDecimalNumber) && !strNum.matches(strRegExpNotNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String generaColorHtml() {

        String code = "";
        String[] letters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        for (int i = 0; i < 6; i++) {
            code += letters[(int) Math.round(Math.random() * 15)];
        }
        return code;
    }

    public static String generaColorHtmlPreferencial(int i) {

        String code = "";
        String[] colors = {"046BBB",
            "DDE4EB",
            "F3C305",
            "545454",
            "57D9DE",
            "A490BE",
            "5A89C9",
            "1F9EE4",
            "2C76BC",
            "47586D",
            "36A4CD",
            "366494"};

        if (colors.length > i) {
            return colors[i];
        } else {
            return generaColorHtml();
        }

    }

    public static String columnExcel(int number) {
        StringBuilder sb = new StringBuilder();
        while (number-- > 0) {
            sb.append((char) ('A' + (number % 26)));
            number /= 26;
        }
        return sb.reverse().toString();
    }

    public static String obtieneDatoCelda(Row row, int c) {
        String strTemp = Constantes.strVacio;
        try {
            if (row.getCell(c).getCellType() == CellType.STRING) {
                strTemp = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            } else if (row.getCell(c).getCellType() == CellType.NUMERIC) {
                strTemp = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue() + "";
            }
        } catch (NoSuchElementException | NullPointerException e) {
            strTemp = Constantes.strVacio;
        }
        return strTemp;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos	
    }

    public static String decodeUTF8(byte[] bytes) {
        return new String(bytes, Constantes.UTF8_CHARSET);
    }

    public static byte[] encodeUTF8(String string) {
        return string.getBytes(Constantes.UTF8_CHARSET);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        final int currentPageNumber = writer.getCurrentPageNumber();

        if (currentPageNumber == 1) {
            return;
        }

        try {
            final Rectangle pageSize = document.getPageSize();
            final PdfContentByte directContent = writer.getDirectContent();

            directContent.setColorFill(BaseColor.GRAY);
            directContent.setFontAndSize(BaseFont.createFont(), 10);

            directContent.setTextMatrix(pageSize.getRight(40), pageSize.getBottom(30));
            directContent.showText(String.valueOf(currentPageNumber));

        } catch (DocumentException | IOException e) {
            log.error("PDF generation error", e);
        }
    }

    public static int aNumero(String strCadena) {

        try {
            return Integer.parseInt(strCadena);
        } catch (Exception e) {
            return Constantes.ZERO_INTEGER;
        }

    }

    public static List poblarCombo(Integer DT) {

        List lstMetodologias = new ArrayList();

        ElementoDAO objElementoDAO = new ElementoDAO();

        List<Elemento> lstElementos = objElementoDAO.obtenListaElementoXDefinicion(DT);

        Iterator itLstElementos = lstElementos.iterator();

        SelectItem objSelectItem = new SelectItem();
        objSelectItem.setValue("");
        objSelectItem.setLabel("---- Todos ----");
        lstMetodologias.add(objSelectItem);

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstMetodologias.add(objSelectItem);
        }

        return lstMetodologias;

    }

    public static Date convertStringToDate(String dateString, String formato) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate;
        try {
            startDate = df.parse(dateString);

            return startDate;
        } catch (ParseException ex) {
            log.error(ex);
        }
        return getCurrentDate();

    }

    public static LocalDate convertDateToLocalDate(java.util.Date suspectDate) {

        try {
            java.util.Date safeDate = new Date(suspectDate.getTime());

            return safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        } catch (UnsupportedOperationException e) {
            // BOOM!!
        }

        return null;

    }

}
