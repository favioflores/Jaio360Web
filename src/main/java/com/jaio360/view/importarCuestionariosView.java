package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.MetricaDAO;
import com.jaio360.domain.Categorias;
import com.jaio360.domain.ComentarioBean;
import com.jaio360.domain.CuestionarioImportado;
import com.jaio360.domain.DosDatos;
import com.jaio360.domain.ElementoCuestionario;
import com.jaio360.domain.ErrorBean;
import com.jaio360.domain.PreguntaAbiertaBean;
import com.jaio360.domain.PreguntaCerradaBean;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Metrica;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.validator.validaTextoIngresado;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@ManagedBean(name = "importarCuestionariosView")
@ViewScoped
public class importarCuestionariosView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(importarCuestionariosView.class);

    private Integer idCuestionario;
    private String strCuestionario;
    private List<ErrorBean> lstErrores;
    private List<CuestionarioImportado> lstCuestionariosImportados;
    private List<DosDatos> lstElementosDelCuestionarios;
    private List<ElementoCuestionario> lstElementoCuestionario;
    private UploadedFile inputFile;
    private Boolean processOk;
    private Boolean blExistPrevImport;
    private StreamedContent fileImport;
    private Integer intIdEstadoProyecto;

    public StreamedContent getFileImport() {
        try {

            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            String fullPath = servletContext.getRealPath(File.separator + "resources" + File.separator + "other" + File.separator + "ModeloDeImportacionCuestionario.xlsx");

            InputStream stream = new FileInputStream(fullPath);

            fileImport = DefaultStreamedContent.builder()
                    .name("ModeloDeImportacionCuestionario.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> stream)
                    .build();

        } catch (Exception ex) {
            mostrarError(log, ex);
        }

        return fileImport;
    }

    public List<ElementoCuestionario> getLstElementoCuestionario() {
        return lstElementoCuestionario;
    }

    public void setLstElementoCuestionario(List<ElementoCuestionario> lstElementoCuestionario) {
        this.lstElementoCuestionario = lstElementoCuestionario;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public void setFileImport(StreamedContent fileImport) {
        this.fileImport = fileImport;
    }

    public String getStrCuestionario() {
        return strCuestionario;
    }

    public void setStrCuestionario(String strCuestionario) {
        this.strCuestionario = strCuestionario;
    }

    public Boolean getProcessOk() {
        return processOk;
    }

    public void setProcessOk(Boolean processOk) {
        this.processOk = processOk;
    }

    public Boolean getBlExistPrevImport() {
        return blExistPrevImport;
    }

    public void setBlExistPrevImport(Boolean blExistPrevImport) {
        this.blExistPrevImport = blExistPrevImport;
    }

    public UploadedFile getInputFile() {
        return inputFile;
    }

    public void setInputFile(UploadedFile inputFile) {
        this.inputFile = inputFile;
    }

    public Integer getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Integer idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public List<ErrorBean> getLstErrores() {
        return lstErrores;
    }

    public void setLstErrores(List<ErrorBean> lstErrores) {
        this.lstErrores = lstErrores;
    }

    public List<CuestionarioImportado> getLstCuestionariosImportados() {
        return lstCuestionariosImportados;
    }

    public void setLstCuestionariosImportados(List<CuestionarioImportado> lstCuestionariosImportados) {
        this.lstCuestionariosImportados = lstCuestionariosImportados;
    }

    public List<DosDatos> getLstElementosDelCuestionarios() {
        return lstElementosDelCuestionarios;
    }

    public void setLstElementosDelCuestionarios(List<DosDatos> lstElementosDelCuestionarios) {
        this.lstElementosDelCuestionarios = lstElementosDelCuestionarios;
    }

    @PostConstruct
    public void init() {
        this.lstErrores = new ArrayList<>();
        this.lstCuestionariosImportados = new ArrayList<>();
        this.lstElementosDelCuestionarios = new ArrayList<>();
        this.idCuestionario = null;
        this.strCuestionario = null;
        this.inputFile = null;
        this.processOk = false;
        this.blExistPrevImport = false;

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        this.intIdEstadoProyecto = objProyecto.getPoIdEstado();

        loadCuestionarios();

    }

    public void irCrearCuestionarios() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("importarCuestionario");
            FacesContext.getCurrentInstance().getExternalContext().redirect("crearCuestionarios.jsf");
        } catch (IOException ex) {
            mostrarError(log, ex);
        }
    }

    public void cargaCuestionarios(FileUploadEvent event) {

        this.lstCuestionariosImportados = new ArrayList<>();
        this.lstElementosDelCuestionarios = new ArrayList<>();
        this.lstElementoCuestionario = new ArrayList();
        this.lstErrores = new ArrayList<>();

        inputFile = event.getFile();

        if (inputFile == null) {
            mostrarAlertaInfo("search.file.first");
        } else {

            XSSFWorkbook xlsAvanzado;

            try {
                xlsAvanzado = new XSSFWorkbook(inputFile.getInputStream());

                lstErrores = validaContenido(xlsAvanzado);

                if (lstErrores.isEmpty()) {
                    procesaArchivo(xlsAvanzado);
                    mostrarAlertaInfo("file.uploaded.correctly");
                    processOk = true;
                } else {
                    mostrarAlertaFatal("file.has.data.errors");
                }

            } catch (Exception ex) {
                mostrarError(log, ex);
                mostrarAlertaFatal("error.was.occurred");
            }

        }

    }

    private List<ErrorBean> validaContenido(XSSFWorkbook xlsAvanzado) {

        validaTextoIngresado PatterTexto = new validaTextoIngresado();

        XSSFSheet sheet = xlsAvanzado.getSheetAt(0);
        Iterator<Row> rowElementos = sheet.iterator();

        int contador = 0;

        while (rowElementos.hasNext()) {

            Row row = rowElementos.next();

            String strTipoRegistro = Utilitarios.obtieneDatoCelda(row, 0);
            String strElemento = Utilitarios.obtieneDatoCelda(row, 1);
            String strPuntajeMinRequerido = Utilitarios.obtieneDatoCelda(row, 2);
            String strPuntajeRequerido = Utilitarios.obtieneDatoCelda(row, 3);

            if (Utilitarios.noEsNuloOVacio(strTipoRegistro)
                    && Utilitarios.noEsNuloOVacio(strElemento)
                    && Utilitarios.noEsNuloOVacio(strPuntajeMinRequerido)
                    && Utilitarios.noEsNuloOVacio(strPuntajeRequerido)) {

                //VALIDAR TIPO DE REGISTRO
                if (strTipoRegistro.equals(Constantes.LINEA_CUESTIONARIO)
                        || strTipoRegistro.equals(Constantes.LINEA_CATEGORIA)
                        || strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_CERRADA)
                        || strTipoRegistro.equals(Constantes.LINEA_COMENTARIO)
                        || strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_ABIERTA)) {

                } else {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.incorrect.type.line"), null, "A" + (row.getRowNum() + 1) + ""));
                }

                //VALIDA CONTENIDO DEL TEXTO
                String validaDescripcion = PatterTexto.validar(strElemento);

                if (!Utilitarios.esNuloOVacio(validaDescripcion)) {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.invalid.text"), null, (row.getRowNum() + 1) + ""));
                }

                //VALIDA CONTENIDO DEL PUNTAJE
                if (!Utilitarios.isNumber(strPuntajeMinRequerido, false)
                        && (strTipoRegistro.equals(Constantes.LINEA_CATEGORIA)
                        || strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_CERRADA))) {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.invalid.puntaje"), null, (row.getRowNum() + 1) + ""));
                }

                //VALIDA CONTENIDO DEL PUNTAJE
                if (!Utilitarios.isNumber(strPuntajeRequerido, false)
                        && (strTipoRegistro.equals(Constantes.LINEA_CATEGORIA)
                        || strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_CERRADA))) {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.invalid.puntaje"), null, (row.getRowNum() + 1) + ""));
                }

                //lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.closed.question.not.in.category"), null, (row.getRowNum() + 1) + ""));
                //lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.at.least.evaluation"), null, "1"));
            } else {

                if (Utilitarios.noEsNuloOVacio(strTipoRegistro)
                        || Utilitarios.noEsNuloOVacio(strElemento)
                        || Utilitarios.noEsNuloOVacio(strPuntajeMinRequerido)
                        || Utilitarios.noEsNuloOVacio(strPuntajeRequerido)) {

                    if (Utilitarios.noEsNuloOVacio(strTipoRegistro)) {
                        lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.not.element.unspecified.type"), null, "A" + (row.getRowNum() + 1) + ""));
                    } else if (Utilitarios.noEsNuloOVacio(strElemento)) {
                        lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.not.element.unspecified.description"), null, "B" + (row.getRowNum() + 1) + ""));
                    } else if (Utilitarios.noEsNuloOVacio(strPuntajeMinRequerido)) {
                        lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.not.element.unspecified.puntaje"), null, "C" + (row.getRowNum() + 1) + ""));
                    } else if (Utilitarios.noEsNuloOVacio(strPuntajeRequerido)) {
                        lstErrores.add(new ErrorBean((row.getRowNum() + 1), msg("step2.not.element.unspecified.puntaje"), null, "D" + (row.getRowNum() + 1) + ""));
                    }

                }

            }

            contador++;
        }

        //SI HAY ERRORES TIPOS DE DATOS SE DEVUELVE AL USUARIO PARA CORREGIR Y NO SE CONTINUA CON LAS VALIDACIONES DE FORMA
        if (!lstErrores.isEmpty()) {
            return lstErrores;
        }

        List<CuestionarioImportado> lstCuestionarioImportados = new ArrayList<>();
        CuestionarioImportado objCuestionarioImportado = new CuestionarioImportado(-1, "Sin descripción", null, null, null);

        boolean isNewAssessment = false;

        MetricaDAO objMetricaDAO = new MetricaDAO();
        Metrica objMetrica = objMetricaDAO.obtenMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        while (rowElementos.hasNext()) {

            Row row = rowElementos.next();

            try {

                String strTipoRegistro = Utilitarios.obtieneDatoCelda(row, 0);
                String strElemento = Utilitarios.obtieneDatoCelda(row, 1);
                String strPuntajeMinRequerido = Utilitarios.obtieneDatoCelda(row, 2);
                String strPuntajeRequerido = Utilitarios.obtieneDatoCelda(row, 3);

                if (strTipoRegistro.equals(Constantes.LINEA_CUESTIONARIO)) {
                    if (isNewAssessment) {
                        lstCuestionarioImportados.add(objCuestionarioImportado);
                    }
                    objCuestionarioImportado = new CuestionarioImportado(-1, strElemento, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    isNewAssessment = true;
                }

                if (strTipoRegistro.equals(Constantes.LINEA_CATEGORIA)) {
                    Categorias objCategorias = new Categorias(strElemento, -1, new ArrayList<>(), null, null);
                    objCuestionarioImportado.getLstCategorias().add(objCategorias);
                }

                if (strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_CERRADA)) {
                    PreguntaCerradaBean objPreguntaCerradaBean = new PreguntaCerradaBean(-1, strElemento, null, null);
                    objCuestionarioImportado.getLstCategorias().get(objCuestionarioImportado.getLstCategorias().size() - 1).getLstPreguntasCerradas().add(objPreguntaCerradaBean);
                }

                if (strTipoRegistro.equals(Constantes.LINEA_COMENTARIO)) {
                    ComentarioBean objComentarioBean = new ComentarioBean(-1, strElemento, null);
                    objCuestionarioImportado.getLstComentarios().add(objComentarioBean);
                }

                if (strTipoRegistro.equals(Constantes.LINEA_PREGUNTA_ABIERTA)) {
                    PreguntaAbiertaBean objPreguntaAbiertaBean = new PreguntaAbiertaBean(-1, strElemento, null);
                    objCuestionarioImportado.getLstPreguntasAbiertas().add(objPreguntaAbiertaBean);
                }

                BigDecimal bdPuntajeMinRequerido = new BigDecimal(strPuntajeMinRequerido);
                BigDecimal bdPuntajeRequerido = new BigDecimal(strPuntajeRequerido);

                if (bdPuntajeMinRequerido.doubleValue() >= 1 && bdPuntajeMinRequerido.doubleValue() <= objMetrica.getMeNuRango()) {

                } else {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), "Puntaje mínimo no puede ser menor a 1", null, "C" + (row.getRowNum() + 1) + ""));
                }
                if (bdPuntajeRequerido.doubleValue() >= 1 && bdPuntajeRequerido.doubleValue() <= objMetrica.getMeNuRango()) {

                } else {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), "Puntaje requerido no puede ser mayor al número de la escala: " + objMetrica.getMeNuRango(), null, "D" + (row.getRowNum() + 1) + ""));
                }

                if (bdPuntajeMinRequerido.doubleValue() > bdPuntajeRequerido.doubleValue()) {
                    lstErrores.add(new ErrorBean((row.getRowNum() + 1), "Puntaje mínimo requerido (Columna C) debe ser menor al puntaje requerido (Columna D)", null, "C o D" + (row.getRowNum() + 1) + ""));
                }

            } catch (NullPointerException e) {
                lstErrores.add(new ErrorBean((row.getRowNum() + 1), "El tipo de elemento está ubicado incorrectamente. Por favor revisar el manual para la importación de cuestionario"));
            } catch (Exception e) {
                lstErrores.add(new ErrorBean((row.getRowNum() + 1), "Ocurrió un error debido a una confiuración errada del archivo. Por favor revisar el manual para la importación de cuestionario"));
            }

        }

        //if (lstCuestionarioImportados.size() != contador) {
        //    lstErrores.add(new ErrorBean(-1, "Hubieron registros no procesados debido a una configuración errada del archivo. Por favor revisar el manual para la importación de cuestionario"));
        //}
        return lstErrores;
    }

    private void procesaArchivo(XSSFWorkbook xlsAvanzado) {

        try {

            XSSFSheet sheet = xlsAvanzado.getSheetAt(0);
            Iterator<Row> rowElementos = sheet.iterator();
            String key = null;
            CuestionarioImportado objCuestionarioImportado = null;
            Categorias objCategorias = null;
            Integer i = 0;

            while (rowElementos.hasNext()) {

                Row row = rowElementos.next();

                String strTipo = Utilitarios.obtieneDatoCelda(row, 0);
                String strElemento = Utilitarios.obtieneDatoCelda(row, 1);
                BigDecimal bdScoreRequired = BigDecimal.ZERO;
                BigDecimal bdScoreMinRequired = BigDecimal.ZERO;

                if (strTipo.equals(Constantes.LINEA_CATEGORIA)
                        || strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA)) {
                    bdScoreMinRequired = new BigDecimal(Utilitarios.obtieneDatoCelda(row, 2));
                    bdScoreRequired = new BigDecimal(Utilitarios.obtieneDatoCelda(row, 3));
                }

                if (Utilitarios.noEsNuloOVacio(strTipo) && Utilitarios.noEsNuloOVacio(strElemento)) {

                    if (strTipo.equals(Constantes.LINEA_CUESTIONARIO)) {
                        if (objCuestionarioImportado != null) {
                            lstCuestionariosImportados.add(objCuestionarioImportado);
                        }
                        objCuestionarioImportado = new CuestionarioImportado();
                        objCuestionarioImportado.setIdCuestionario(i);
                        objCuestionarioImportado.setStrDescCuestionario(strElemento);
                        objCuestionarioImportado.setLstCategorias(new ArrayList<>());
                        objCuestionarioImportado.setLstComentarios(new ArrayList<>());
                        objCuestionarioImportado.setLstPreguntasAbiertas(new ArrayList<>());
                        i++;

                    }

                    if (strTipo.equals(Constantes.LINEA_COMENTARIO)) {
                        ComentarioBean objComentarioBean = new ComentarioBean(-1, strElemento, null);
                        objCuestionarioImportado.getLstComentarios().add(objComentarioBean);
                    }

                    if (strTipo.equals(Constantes.LINEA_PREGUNTA_ABIERTA)) {
                        PreguntaAbiertaBean objPreguntaAbiertaBean = new PreguntaAbiertaBean(-1, strElemento, null);
                        objCuestionarioImportado.getLstPreguntasAbiertas().add(objPreguntaAbiertaBean);
                    }

                    if (strTipo.equals(Constantes.LINEA_CATEGORIA)) {
                        objCuestionarioImportado.getLstCategorias().add(new Categorias(strElemento, null, new ArrayList<>(), bdScoreRequired, bdScoreMinRequired));
                        objCategorias = objCuestionarioImportado.getLstCategorias().get(objCuestionarioImportado.getLstCategorias().size() - 1);
                    }

                    if (strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA)) {
                        PreguntaCerradaBean objPreguntaCerradaBean = new PreguntaCerradaBean(-1, strElemento, bdScoreRequired, bdScoreMinRequired);
                        objCategorias.getLstPreguntasCerradas().add(objPreguntaCerradaBean);
                    }

                }

            }

            if (objCuestionarioImportado != null) {
                lstCuestionariosImportados.add(objCuestionarioImportado);
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void verElementos(Integer idCuestionario) {

        this.idCuestionario = idCuestionario;

        this.lstElementosDelCuestionarios = new ArrayList<>();

        CuestionarioImportado objElementos = lstCuestionariosImportados.get(idCuestionario);

        this.strCuestionario = objElementos.getStrDescCuestionario();

        try {

            for (Categorias objCat : objElementos.getLstCategorias()) {
                lstElementosDelCuestionarios.add(new DosDatos(msg("category"), objCat.getStrCategoria(), null, objCat.getIntIdComponente(), objCat.getBdScoreRequired(), objCat.getBdScoreMinRequired()));

                for (PreguntaCerradaBean objPreguntaCerradaBean : objCat.getLstPreguntasCerradas()) {
                    lstElementosDelCuestionarios.add(new DosDatos(msg("close.question"), objPreguntaCerradaBean.getStrDescripcion(), "secondary", objPreguntaCerradaBean.getId(), objPreguntaCerradaBean.getBdScoreRequired(), objPreguntaCerradaBean.getBdScoreMinRequired()));
                }
            }

            for (ComentarioBean objComentarioBean : objElementos.getLstComentarios()) {
                lstElementosDelCuestionarios.add(new DosDatos(msg("comment"), objComentarioBean.getStrDescripcion(), "warning", objComentarioBean.getId(), null, null));
            }

            for (PreguntaAbiertaBean objPreguntaAbiertaBean : objElementos.getLstPreguntasAbiertas()) {
                lstElementosDelCuestionarios.add(new DosDatos(msg("open.question"), objPreguntaAbiertaBean.getStrDescripcion(), "success", objPreguntaAbiertaBean.getId(), null, null));
            }

            mostrarAlertaInfo(msg("step2.evaluation.selected") + " " + objElementos.getStrDescCuestionario());

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }
    }

    public void guardarImportacion() {

        try {

            this.lstElementoCuestionario = new ArrayList();

            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
            boolean correcto = objCuestionarioDAO.guardaImportacionCuestionario(lstCuestionariosImportados);
            if (correcto) {
                mostrarAlertaInfo("success");
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }
        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

        init();

    }

    public void loadCuestionarios() {

        List<Cuestionario> lstCuestionarios;

        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

        lstCuestionarios = objCuestionarioDAO.obtenListaCuestionario(Utilitarios.obtenerProyecto().getIntIdProyecto());

        CuestionarioImportado objCuestionarioImportado;

        for (Cuestionario objCuestionario : lstCuestionarios) {

            blExistPrevImport = true;

            objCuestionarioImportado = new CuestionarioImportado();

            objCuestionarioImportado.setIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
            objCuestionarioImportado.setStrDescCuestionario(objCuestionario.getCuTxDescripcion());

            ComponenteDAO objComponenteDao = new ComponenteDAO();

            List<Componente> lstComponente = objComponenteDao.listaComponenteXCuestionario(objCuestionario.getCuIdCuestionarioPk());

            for (Componente objComponente : lstComponente) {

                if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA)) {

                    Categorias objCategorias = new Categorias(objComponente.getCoIdComponentePk());
                    objCategorias.setStrCategoria(objComponente.getCoTxDescripcion());
                    objCategorias.setBdScoreMinRequired(objComponente.getCoNrPuntajeMinimoRequerido());
                    objCategorias.setBdScoreRequired(objComponente.getCoNrPuntajeRequerido());

                    for (Componente objComponentePC : objComponenteDao.listaComponenteRed(objComponente)) {
                        PreguntaCerradaBean objPreguntaCerradaBean = new PreguntaCerradaBean(objComponentePC.getCoIdComponentePk(), objComponentePC.getCoTxDescripcion(), objComponentePC.getCoNrPuntajeRequerido(), objComponentePC.getCoNrPuntajeMinimoRequerido());
                        objCategorias.getLstPreguntasCerradas().add(objPreguntaCerradaBean);
                    }

                    objCuestionarioImportado.getLstCategorias().add(objCategorias);

                } else if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_COMENTARIO)) {
                    ComentarioBean objComentarioBean = new ComentarioBean(objComponente.getCoIdComponentePk(), objComponente.getCoTxDescripcion(), null);
                    objCuestionarioImportado.getLstComentarios().add(objComentarioBean);
                } else if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_ABIERTA)) {
                    PreguntaAbiertaBean objPreguntaAbiertaBean = new PreguntaAbiertaBean(objComponente.getCoIdComponentePk(), objComponente.getCoTxDescripcion(), null);
                    objCuestionarioImportado.getLstPreguntasAbiertas().add(objPreguntaAbiertaBean);
                }

            }

            lstCuestionariosImportados.add(objCuestionarioImportado);
        }

    }

    public void eliminarCuestionario() {
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

        boolean correcto = objCuestionarioDAO.eliminaCuestionarioTotal();

        if (correcto) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cuestionarios eliminados correctamente", null));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al eliminar los cuestionarios", null));
        }

    }

    public void onRowEdit(CellEditEvent event) {

        try {

            if (Utilitarios.noEsNuloOVacio(event.getNewValue())) {

                ComponenteDAO objComponenteDao = new ComponenteDAO();

                Componente objComponente = objComponenteDao.obtenComponente(lstElementosDelCuestionarios.get(event.getRowIndex()).getIntComponente());
                objComponente.setCoTxDescripcion(event.getNewValue().toString());

                ComponenteDAO objComponenteDAO = new ComponenteDAO();
                objComponenteDAO.actualizaComponente(objComponente);

                lstElementosDelCuestionarios.get(event.getRowIndex()).setStrDato2(event.getNewValue().toString());

                mostrarAlertaInfo("profile.updated.success");

            } else {
                mostrarAlertaError(msg("adm.least.value"));
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void onRowCancel(RowEditEvent<DosDatos> event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(event.getObject()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
