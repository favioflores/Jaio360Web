package com.jaio360.view;

import com.jaio360.component.ExecutorBalanceMovement;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.Participante;
import com.jaio360.orm.ReferenciaMovimiento;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import com.jaio360.orm.UsuarioSaldo;
import com.jaio360.report.ElementoGrupalUtiles;
import com.jaio360.report.ReporteGrupalCaratula;
import com.jaio360.report.ReporteGrupalNivelParticipacion;
import com.jaio360.report.ReporteGrupalSumarioCategoriaGeneral;
import com.jaio360.report.ReporteIndividualCaratula;
import com.jaio360.report.ReporteIndividualPreguntasAbiertas;
import com.jaio360.report.ReporteIndividualCalificacionXCategoria;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismo;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacion;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionMismo;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacion;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacionMismo;
import com.jaio360.report.ReporteIndividualSumarioCategoria;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismo;
import com.jaio360.report.ReporteTodasRespuestas;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
import com.jaio360.utils.ReportSort;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "generaReportesView")
@ViewScoped
public class GeneraReportesView extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(GeneraReportesView.class);

    private List<SelectItem> lstContenidoIndividual;
    private List<SelectItem> lstContenidoGrupal;
    private List<Integer> lstSeleccionadosIndividual;
    private List<Integer> lstSeleccionadosGrupal;

    private List<Evaluado> lstEvaluados;
    private List<Cuestionario> lstCuestionarios;
    private List<Cuestionario> lstCuestionariosSeleccionados;
    private List<Evaluado> lstEvaluadosSeleccionados;
    private List<Cuestionario> lstFiltroCuestionarios;
    private List<Participante> lstFiltroEvaluados;

    private String[] strDescripcionesIndividual = new String[15];
    private String[] strDescripcionesGrupal = new String[15];

    private StreamedContent fileGrupal;
    private StreamedContent fileIndividual;
    private StreamedContent planAccion;

    private Integer intLicenciasIndividuales;
    private Integer intLicenciasMasivas;
    private Integer intLicenciasIndividualesRequerido;
    private Integer intLicenciasMasivasRequerido;

    public Integer getIntLicenciasIndividuales() {
        return intLicenciasIndividuales;
    }

    public void setIntLicenciasIndividuales(Integer intLicenciasIndividuales) {
        this.intLicenciasIndividuales = intLicenciasIndividuales;
    }

    public Integer getIntLicenciasMasivas() {
        return intLicenciasMasivas;
    }

    public void setIntLicenciasMasivas(Integer intLicenciasMasivas) {
        this.intLicenciasMasivas = intLicenciasMasivas;
    }

    public Integer getIntLicenciasIndividualesRequerido() {
        return intLicenciasIndividualesRequerido;
    }

    public void setIntLicenciasIndividualesRequerido(Integer intLicenciasIndividualesRequerido) {
        this.intLicenciasIndividualesRequerido = intLicenciasIndividualesRequerido;
    }

    public Integer getIntLicenciasMasivasRequerido() {
        return intLicenciasMasivasRequerido;
    }

    public void setIntLicenciasMasivasRequerido(Integer intLicenciasMasivasRequerido) {
        this.intLicenciasMasivasRequerido = intLicenciasMasivasRequerido;
    }

    public StreamedContent getPlanAccion() {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String fullPath = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "PlanDeAccion.docx");
        File objFile = new File(fullPath);
        planAccion = DefaultStreamedContent.builder()
                .name("PlanDeAccion.docx")
                .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                .build();

        return planAccion;
    }

    public StreamedContent getFileIndividual() {
        return fileIndividual;
    }

    public List<Cuestionario> getLstCuestionariosSeleccionados() {
        return lstCuestionariosSeleccionados;
    }

    public void setLstCuestionariosSeleccionados(List<Cuestionario> lstCuestionariosSeleccionados) {
        this.lstCuestionariosSeleccionados = lstCuestionariosSeleccionados;
    }

    public List<Cuestionario> getLstFiltroCuestionarios() {
        return lstFiltroCuestionarios;
    }

    public void setLstFiltroCuestionarios(List<Cuestionario> lstFiltroCuestionarios) {
        this.lstFiltroCuestionarios = lstFiltroCuestionarios;
    }

    public void setFileIndividual(StreamedContent fileIndividual) {
        this.fileIndividual = fileIndividual;
    }

    public List<Evaluado> getLstEvaluadosSeleccionados() {
        return lstEvaluadosSeleccionados;
    }

    public void setLstEvaluadosSeleccionados(List<Evaluado> lstEvaluadosSeleccionados) {
        this.lstEvaluadosSeleccionados = lstEvaluadosSeleccionados;
    }

    public String[] getStrDescripcionesIndividual() {
        return strDescripcionesIndividual;
    }

    public void setStrDescripcionesIndividual(String[] strDescripcionesIndividual) {
        this.strDescripcionesIndividual = strDescripcionesIndividual;
    }

    public String[] getStrDescripcionesGrupal() {
        return strDescripcionesGrupal;
    }

    public void setStrDescripcionesGrupal(String[] strDescripcionesGrupal) {
        this.strDescripcionesGrupal = strDescripcionesGrupal;
    }

    public StreamedContent getFileGrupal() {
        return fileGrupal;
    }

    public void setFileGrupal(StreamedContent fileGrupal) {
        this.fileGrupal = fileGrupal;
    }

    public List<Participante> getLstFiltroEvaluados() {
        return lstFiltroEvaluados;
    }

    public void setLstFiltroEvaluados(List<Participante> lstFiltroEvaluados) {
        this.lstFiltroEvaluados = lstFiltroEvaluados;
    }

    public List<Evaluado> getLstEvaluados() {
        return lstEvaluados;
    }

    public void setLstEvaluados(List<Evaluado> lstEvaluados) {
        this.lstEvaluados = lstEvaluados;
    }

    public List<Cuestionario> getLstCuestionarios() {
        return lstCuestionarios;
    }

    public void setLstCuestionarios(List<Cuestionario> lstCuestionarios) {
        this.lstCuestionarios = lstCuestionarios;
    }

    public List<SelectItem> getLstContenidoIndividual() {
        return lstContenidoIndividual;
    }

    public void setLstContenidoIndividual(List<SelectItem> lstContenidoIndividual) {
        this.lstContenidoIndividual = lstContenidoIndividual;
    }

    public List<SelectItem> getLstContenidoGrupal() {
        return lstContenidoGrupal;
    }

    public void setLstContenidoGrupal(List<SelectItem> lstContenidoGrupal) {
        this.lstContenidoGrupal = lstContenidoGrupal;
    }

    public List<Integer> getLstSeleccionadosIndividual() {
        return lstSeleccionadosIndividual;
    }

    public void setLstSeleccionadosIndividual(List<Integer> lstSeleccionadosIndividual) {
        this.lstSeleccionadosIndividual = lstSeleccionadosIndividual;
    }

    public List<Integer> getLstSeleccionadosGrupal() {
        return lstSeleccionadosGrupal;
    }

    public void setLstSeleccionadosGrupal(List<Integer> lstSeleccionadosGrupal) {
        this.lstSeleccionadosGrupal = lstSeleccionadosGrupal;
    }

    @PostConstruct
    public void init() {

        lstContenidoIndividual = new ArrayList<>();
        lstContenidoGrupal = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            lstContenidoIndividual.add(new SelectItem(i, msg("report." + i)));
        }

        for (int i = 100; i <= 102; i++) {
            lstContenidoGrupal.add(new SelectItem(i, msg("report." + i)));
        }

        //Resultados Generales - Competencias y preguntas
        /*
        lstContenidoGrupal.add(new ModeloContenido(11,"Promedio general por competencia","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(12,"Promedio general por pregunta","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(13,"Promedio por preguntas ordenado de forma descendente","",strDescripcionesGrupal[0],"PDF"));
        //Resultados Generales - Por personas
        lstContenidoGrupal.add(new ModeloContenido(14,"Personas con mejor puntaje general","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(15,"Personas con menor puntaje general","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(16,"Resumen de promedio","",strDescripcionesGrupal[0],"PDF"));
        //RESULTADOS POR VARIABLE
        lstContenidoGrupal.add(new ModeloContenido(17,"Promedio de competencias por sexo","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(18,"Promedio de competencias por edad","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(19,"Promedio de competencias por relaciones","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(20,"Promedio de competencias por tiempo en la empresa","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(21,"Promedio de competencias por área","",strDescripcionesGrupal[0],"PDF"));
        //RESUMEN DE EVALUADOS POR RELACIONES
        lstContenidoGrupal.add(new ModeloContenido(22,"Resumen de evaluados por relaciones","",strDescripcionesGrupal[0],"EXCEL"));
         */
        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        ResultadoDAO objResultadoDAO = new ResultadoDAO();

        /**
         * OBTIENE NRO DE EVALUADORES POR PARTICIPANTE DEL PROYECTO
         */
        List lstParticipanteRedEvaluacion = objParticipanteDAO.obtenerNroParticipantesEnEjecucion(objProyectoInfo.getIntIdProyecto());
        Iterator itLstParticipanteRedEvaluacion = lstParticipanteRedEvaluacion.iterator();

        Map mapEvaluados = new HashMap();

        while (itLstParticipanteRedEvaluacion.hasNext()) {

            Object[] obj = (Object[]) itLstParticipanteRedEvaluacion.next();

            if (!mapEvaluados.containsKey(obj[0])) {
                mapEvaluados.put(obj[0], obj[1]);
            }

        }

        /**
         * OBTIENE NRO DE EVALUACIONES TERMINADAS PR PARTICIPANTE
         */
        List lstEvaluacionesTerminadas = objResultadoDAO.obtenListaTotalTerminadosXparticipante(objProyectoInfo.getIntIdProyecto());
        Iterator itLstEvaluacionesTerminadas = lstEvaluacionesTerminadas.iterator();

        Map mapEvaluacionesTerminadas = new HashMap();

        while (itLstEvaluacionesTerminadas.hasNext()) {

            Object[] obj = (Object[]) itLstEvaluacionesTerminadas.next();

            if (!mapEvaluacionesTerminadas.containsKey(obj[0])) {
                mapEvaluacionesTerminadas.put(obj[0], obj[1]);
            }

        }

        /**
         * OBTIENE PARTICIPANTES DEL PROYECTO
         */
        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXEstado(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);

        lstEvaluados = new ArrayList<>();

        Evaluado objEvaluado;

        for (Participante objParticipante : lstParticipantes) {

            objEvaluado = new Evaluado();

            objEvaluado.setPaIdParticipantePk(objParticipante.getPaIdParticipantePk());
            objEvaluado.setPaTxDescripcion(objParticipante.getPaTxDescripcion());
            objEvaluado.setPaTxNombreCargo(objParticipante.getPaTxNombreCargo());
            objEvaluado.setPaTxCorreo(objParticipante.getPaTxCorreo());
            objEvaluado.setPaInAutoevaluar(objParticipante.getPaInAutoevaluar());
            objEvaluado.setPaTxImgUrl(objParticipante.getPaTxImgUrl());

            if (objParticipante.getPaInAnalizado() != null) {
                objEvaluado.setBlAnalizado(objParticipante.getPaInAnalizado());
                if (objParticipante.getPaInAnalizado()) {
                    objEvaluado.setInAnalizado(111);
                } else {
                    objEvaluado.setInAnalizado(0);
                }
            } else {
                objEvaluado.setBlAnalizado(false);
                objEvaluado.setInAnalizado(0);
            }

            if (mapEvaluados.containsKey(objParticipante.getPaIdParticipantePk())) {
                BigDecimal bdTemp = (BigDecimal) mapEvaluados.get(objParticipante.getPaIdParticipantePk());
                objEvaluado.setIntNumberEvaluators(bdTemp.intValue());
            } else {
                objEvaluado.setIntNumberEvaluators(0);
            }
            if (!mapEvaluacionesTerminadas.isEmpty() && mapEvaluacionesTerminadas.containsKey(objParticipante.getPaIdParticipantePk())) {
                BigInteger bdTemp = (BigInteger) mapEvaluacionesTerminadas.get(objParticipante.getPaIdParticipantePk());
                objEvaluado.setIntNumberEvaluationFinished(bdTemp.intValue());
            } else {
                objEvaluado.setIntNumberEvaluationFinished(0);
            }
            lstEvaluados.add(objEvaluado);

        }

        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        lstCuestionarios = objCuestionarioDAO.obtenListaCuestionarioXEstado(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
    }

    public void generarReporteGrupal() {

        if (lstSeleccionadosGrupal.isEmpty()) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un modelo de los reportes grupales");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else if (lstCuestionariosSeleccionados.isEmpty()) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un cuestionario");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {

            /* Genera reportes */
            generaReporteGrupal();

        }

    }

    private void generaReporteGrupal() {

        try {

            verificaDirectorios();

            List<DatosReporte> lstTemporalesPDFxCombinar = new ArrayList<>();
            List<DatosReporte> lstTemporalesOtros = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if (!lstSeleccionadosGrupal.isEmpty() && !lstCuestionariosSeleccionados.isEmpty()) {

                Collections.sort(lstSeleccionadosGrupal, new ReportSort());

                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                Map map = new HashMap();

                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango, lstTemporalesOtros);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, Constantes.STR_INBOX_PRELIMINAR + File.separator + utilReport);

                boolean flag = true;


                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for (Cuestionario objCuestionario : lstCuestionariosSeleccionados) {

                    for (Integer objModeloContenido : lstSeleccionadosGrupal) {

                        DatosReporte objDatosReportePrincipal = new DatosReporte();
                        objDatosReportePrincipal.setStrNombre(msg("report." + objModeloContenido));
                        objDatosReportePrincipal.setStrDescripcion(msg("report." + objModeloContenido));
                        objDatosReportePrincipal.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReportePrincipal.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReportePrincipal.setIntMaxRango(intMaxRango);

                        if (flag) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();

                            DatosReporte DRCaratula = new DatosReporte();
                            DRCaratula.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());

                            DRCaratula.setStrID(objReporteC.build(objDatosReportePrincipal, strNameFile));
                            lstTemporalesPDFxCombinar.add(DRCaratula);

                            flag = false;
                        }

                        if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrNombre(objDatosReportePrincipal.getStrNombre());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteGrupalSumarioCategoriaGeneral objReporteR = new ReporteGrupalSumarioCategoriaGeneral();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_NIVEL_DE_PARTICIPACION)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setStrNombre(objDatosReportePrincipal.getStrNombre());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteGrupalNivelParticipacion objReporteR = new ReporteGrupalNivelParticipacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_RESPUESTAS)) {

                            String strNameFile = "Final_" + objDatosReportePrincipal.getStrCuestionario() + "_" + Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setStrNombre(objDatosReportePrincipal.getStrNombre());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteTodasRespuestas objReporte = new ReporteTodasRespuestas();
                            objDatosReporte.setStrID(objReporte.build(objDatosReporte, map, objCuestionario.getCuIdCuestionarioPk(), strNameFile));
                            objDatosReporte.setBlDefinitivo(true);

                            lstDefinitivos.add(objDatosReporte);

                        }

                    }

                    if (!lstTemporalesPDFxCombinar.isEmpty()) {

                        DatosReporte objDatosReporteCombine = new DatosReporte();
                        objDatosReporteCombine.setStrID(objCuestionario.getCuTxDescripcion() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS));
                        objDatosReporteCombine.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporalesPDFxCombinar, objDatosReporteCombine));
                        lstTemporalesPDFxCombinar.add(objDatosReporteCombine);

                        DatosReporte objDatosReporteFinal = new DatosReporte();
                        objDatosReporteFinal.setStrID(Utilitarios.putPageNumber(objDatosReporteCombine.getStrID()));
                        objDatosReporteFinal.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporteFinal);

                        Utilitarios.eliminaArchivosTemporales(lstTemporalesPDFxCombinar);

                    }

                }

                Utilitarios.eliminaArchivosTemporales(lstTemporalesOtros);

                if (!lstDefinitivos.isEmpty()) {

                    String ZipName = msg("report.group") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);

                    FileOutputStream salida = new FileOutputStream(objFile);

                    boolean flagFinal = Utilitarios.zipArchivos(lstDefinitivos, salida);

                    if (flagFinal) {

                        InputStream stream = new FileInputStream(objFile.getAbsolutePath());

                        fileGrupal = DefaultStreamedContent.builder()
                                .name(ZipName)
                                .contentType("application/zip")
                                .stream(() -> stream)
                                .build();

                        mostrarAlertaInfo("step6.reports.generated.success");

                    } else {
                        mostrarAlertaError("error.was.occurred");
                    }
                }

            }

        } catch (IOException ex) {
            mostrarError(log, ex);
        }

    }

    private void generaReporteIndividual() {

        try {

            verificaDirectorios();

            List<DatosReporte> lstTemporalesPDFxCombinar;
            List<DatosReporte> lstTemporalesOtros = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if (!lstSeleccionadosIndividual.isEmpty() && !lstEvaluadosSeleccionados.isEmpty()) {

                Collections.sort(lstSeleccionadosIndividual, new ReportSort());

                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

                Map map = new HashMap();

                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango, lstTemporalesOtros);
                DatosReporte objUtil = new DatosReporte();
                objUtil.setStrID(utilReport);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, Constantes.STR_INBOX_PRELIMINAR + File.separator + utilReport);

                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for (Evaluado objParticipante : lstEvaluadosSeleccionados) {

                    lstTemporalesPDFxCombinar = new ArrayList<>();

                    Map mapRelaciones = obtieneRelaciones(objParticipante.getPaIdParticipantePk());

                    Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk());

                    if (objParticipante.isPaInAutoevaluar()) {
                        Relacion objRelacion = new Relacion();
                        objRelacion.setReIdRelacionPk(-1);
                        objRelacion.setReNuOrden(intMaxRango + 1);
                        objRelacion.setReTxAbreviatura("AUTO");
                        objRelacion.setReColor("000000");
                        mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);
                    }

                    Relacion objRelacion = new Relacion();
                    objRelacion.setReIdRelacionPk(-1);
                    objRelacion.setReNuOrden(intMaxRango + 1);
                    objRelacion.setReTxAbreviatura("PROM");
                    objRelacion.setReColor("585858");
                    mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);

                    boolean flag = true;

                    for (Integer objModeloContenido : lstSeleccionadosIndividual) {

                        DatosReporte objDatosReportePrincipal = new DatosReporte();
                        objDatosReportePrincipal.setStrNombre(msg("report." + objModeloContenido));
                        objDatosReportePrincipal.setStrDescripcion(msg("report." + objModeloContenido));
                        objDatosReportePrincipal.setMapRelaciones(mapRelaciones);
                        objDatosReportePrincipal.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReportePrincipal.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReportePrincipal.setIntMaxRango(intMaxRango);
                        objDatosReportePrincipal.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                        if (flag) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrID(strNameFile);
                            objDatosReporte.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporte.setStrID(objReporteC.build(objDatosReporte, strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                            flag = false;

                        }

                        if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualSumarioCategoria objReporteR = new ReporteIndividualSumarioCategoria();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualSumarioCategoriaMismo objReporteR = new ReporteIndividualSumarioCategoriaMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualCalificacionXCategoria objReporteR = new ReporteIndividualCalificacionXCategoria();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualCalificacionXCategoriaMismo objReporteR = new ReporteIndividualCalificacionXCategoriaMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_OTROS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualItemsAltaCalificacion objReporteR = new ReporteIndividualItemsAltaCalificacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_OTROS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualItemsBajaCalificacion objReporteR = new ReporteIndividualItemsBajaCalificacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualItemsAltaCalificacionMismo objReporteR = new ReporteIndividualItemsAltaCalificacionMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualItemsBajaCalificacionMismo objReporteR = new ReporteIndividualItemsBajaCalificacionMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_PREGUNTAS_ABIERTAS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteIndividualPreguntasAbiertas objReporteR = new ReporteIndividualPreguntasAbiertas();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        }

                    }

                    if (!lstTemporalesPDFxCombinar.isEmpty()) {

                        DatosReporte objDatosReporteCombine = new DatosReporte();
                        objDatosReporteCombine.setStrID(objParticipante.getPaTxDescripcion() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS));
                        objDatosReporteCombine.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporalesPDFxCombinar, objDatosReporteCombine));
                        lstTemporalesPDFxCombinar.add(objDatosReporteCombine);

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrID(objParticipante.getPaTxDescripcion() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS));
                        objDatosReporte.setStrID(Utilitarios.putPageNumber(objDatosReporteCombine.getStrID()));
                        objDatosReporte.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporte);

                        Utilitarios.eliminaArchivosTemporales(lstTemporalesPDFxCombinar);

                    }

                }

                Utilitarios.eliminaArchivosTemporales(lstTemporalesOtros);

                if (!lstDefinitivos.isEmpty()) {

                    String ZipName = msg("report.individual") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);

                    FileOutputStream salida = new FileOutputStream(objFile);

                    boolean flag = Utilitarios.zipArchivos(lstDefinitivos, salida);

                    if (flag) {
                        InputStream stream = new FileInputStream(objFile.getAbsolutePath());

                        fileIndividual = DefaultStreamedContent.builder()
                                .name(ZipName)
                                .contentType("application/zip")
                                .stream(() -> stream)
                                .build();

                        mostrarAlertaInfo("step6.reports.generated.success");

                    } else {
                        mostrarAlertaFatal("error.was.occurred");
                    }
                }

            }

        } catch (IOException ex) {
            mostrarError(log, ex);
        }

    }

    private void verificaDirectorios() {

        File directoryInboxP = new File(Constantes.STR_INBOX_PRELIMINAR);
        File directoryInboxD = new File(Constantes.STR_INBOX_DEFINITIVO);

        if (!directoryInboxP.exists()) {
            directoryInboxP.mkdir();
            if (!directoryInboxP.exists()) {
                log.error("Can't create directory " + Constantes.STR_INBOX_PRELIMINAR);
            }
        }

        if (!directoryInboxD.exists()) {
            directoryInboxD.mkdir();
            if (!directoryInboxD.exists()) {
                log.error("Can't create directory " + Constantes.STR_INBOX_PRELIMINAR);
            }
        }

    }

    private Map obtieneRelaciones(Integer intIdEvaluado) {

        RelacionDAO objRelacionDAO = new RelacionDAO();

        List<Relacion> lstRelaciones = objRelacionDAO.obtenListaRelacionPorEvaluado(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdEvaluado);

        Map map = new HashMap();

        for (Relacion objRelacion : lstRelaciones) {
            map.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        return map;

    }

    public void generarReporteIndividual() {

        if (lstSeleccionadosIndividual.isEmpty()) {

            mostrarAlertaError("must.select.at.least.one.report");

        } else if (lstEvaluadosSeleccionados.isEmpty()) {

            mostrarAlertaError("must.select.at.least.evaluated");

        } else {

            calcularLicenciasEvaluadosSeleccionados();

            /*
            if (this.intLicenciasIndividualesRequerido > 0 || this.intLicenciasMasivasRequerido > 0) {

                Map<String, Object> options = new HashMap<>();

                options.put(
                        "modal", true);
                options.put(
                        "draggable", false);
                options.put(
                        "closable", true);
                options.put(
                        "resizable", false);
                options.put(
                        "showEffect", "drop");
                options.put(
                        "includeViewParams", true);

                //generarReporteIndividual();
                PrimeFaces.current().dialog().openDynamic("useLicenses", options, null);

            }
             */
        }
    }

    public void closeLicenses() {
        PrimeFaces.current().dialog().closeDynamic("useLicenses");
    }

    public void calcularLicenciasEvaluadosSeleccionados() {

        try {

            this.intLicenciasIndividualesRequerido = 0;
            this.intLicenciasMasivasRequerido = 0;

            Integer intLicenciasIndividualesReservadas = 0;
            Integer intLicenciasMasivasReservadas = 0;

            UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();
            UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(Utilitarios.obtenerUsuario().getIntUsuarioPk());

            if (objUsuarioSaldo != null) {
                this.intLicenciasIndividuales = objUsuarioSaldo.getUsNrReservadoIndividual();
                this.intLicenciasMasivas = objUsuarioSaldo.getUsNrReservadoMasivo();
            } else {
                this.intLicenciasIndividuales = 0;
                this.intLicenciasMasivas = 0;
            }

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

            //Participantes
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

            Integer intNroEvaluadores = 0;

            for (Evaluado objParticipante : lstEvaluadosSeleccionados) {

                if (!objParticipante.isBlAnalizado()) {
                    Object obj = objParticipanteDAO.obtenerNroEvaluadoresXEvaluado(objProyectoInfo.getIntIdProyecto(), objParticipante.getPaIdParticipantePk());
                    if (obj != null) {
                        intNroEvaluadores = ((BigDecimal) obj).intValue();
                    }

                } else {
                    continue;
                }

                if (intNroEvaluadores > 20) {
                    intLicenciasMasivasReservadas++;
                } else {
                    intLicenciasIndividualesReservadas++;
                }

            }

            this.intLicenciasIndividualesRequerido = intLicenciasIndividualesReservadas;

            this.intLicenciasMasivasRequerido = intLicenciasMasivasReservadas;

            confirmaGeneracionReporteIndividual();

        } catch (HibernateException e) {
            mostrarError(log, e);
        }
    }

    public void confirmaGeneracionReporteIndividual() {

        try {
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            Participante objParticipante;
            boolean ok = true;

            for (Evaluado objEvaluado : this.lstEvaluadosSeleccionados) {

                if (!objEvaluado.isBlAnalizado()) {

                    objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());
                    objParticipante.setPaInAnalizado(Boolean.TRUE);

                    List<Movimiento> lstMovements = new ArrayList<>();

                    ReferenciaMovimiento objReferenciaMovimiento = new ReferenciaMovimiento();

                    objReferenciaMovimiento.setPoIdProyectoFk(Utilitarios.obtenerProyecto().getIntIdProyecto());

                    if (objEvaluado.getIntNumberEvaluators() <= 20) {
                        Movimiento objMovimiento = new Movimiento();
                        objMovimiento.setMoInCantidad(1);
                        objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_EJECUTA_LICENCIA_INDIVIDUAL));
                        objReferenciaMovimiento.setMovimiento(objMovimiento);
                        objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                        lstMovements.add(objMovimiento);
                    } else {
                        Movimiento objMovimiento = new Movimiento();
                        objMovimiento.setMoInCantidad(1);
                        objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_EJECUTA_LICENCIA_MASIVA));
                        objReferenciaMovimiento.setMovimiento(objMovimiento);
                        objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                        lstMovements.add(objMovimiento);
                    }

                    Usuario objUsuario = new Usuario();
                    objUsuario.setUsIdCuentaPk(Utilitarios.obtenerUsuario().getIntUsuarioPk());
                    String strResult = ExecutorBalanceMovement.getInstance().execute(lstMovements, objUsuario);

                    if (strResult != null) {
                        ok = false;
                    }

                    objParticipanteDAO.actualizaParticipante(objParticipante);

                }
            }

            if (ok) {
                init();
                generaReporteIndividual();
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (HibernateException e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

}
