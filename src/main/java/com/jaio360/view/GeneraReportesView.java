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
import java.io.FileNotFoundException;
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
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

        try {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            String fullPath = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "PlanDeAccion.doc");

            File objFile = new File(fullPath);
            InputStream stream = new FileInputStream(objFile.getAbsolutePath());
            //planAccion = new DefaultStreamedContent(stream, "application/doc", "PlanDeAccion.doc");

            planAccion = DefaultStreamedContent.builder()
                    .name("PlanDeAccion.doc")
                    .contentType("application/doc")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                    .build();

        } catch (FileNotFoundException ex) {
            log.error(ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Plan de accion", "No existe el documento. Por favor contactese con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

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

    private void agregarDescripciones() {

        strDescripcionesIndividual[0] = "Individual 0";
        strDescripcionesIndividual[1] = "Individual 1";
        strDescripcionesIndividual[2] = "Individual 2";
        strDescripcionesIndividual[3] = "Individual 3";
        strDescripcionesIndividual[4] = "Individual 4";
        strDescripcionesIndividual[5] = "Individual 5";
        strDescripcionesIndividual[6] = "Individual 6";
        strDescripcionesIndividual[7] = "Individual 7";
        strDescripcionesIndividual[8] = "Individual 8";

        strDescripcionesGrupal[0] = "Grupal 0";

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

            List<DatosReporte> lstTemporales = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if (!lstSeleccionadosGrupal.isEmpty() && !lstCuestionariosSeleccionados.isEmpty()) {

                Collections.sort(lstSeleccionadosGrupal, new ReportSort());

                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                //CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
                Map map = new HashMap();

                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango);
                DatosReporte objUtil = new DatosReporte();
                objUtil.setStrID(utilReport);
                objUtil.setBlDefinitivo(false);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, utilReport);

                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for (Cuestionario objCuestionario : lstCuestionariosSeleccionados) {

                    for (Integer objModeloContenido : lstSeleccionadosGrupal) {

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrNombre(msg("report." + objModeloContenido));
                        objDatosReporte.setStrDescripcion(msg("report." + objModeloContenido));
                        //objDatosReporte.setMapRelaciones(mapRelaciones);
                        objDatosReporte.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReporte.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReporte.setIntMaxRango(intMaxRango);

                        String strNombreTemp = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion(), " ", "_");
                        String strFirma = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                + Utilitarios.generateRandom(strNombreTemp);

                        objDatosReporte.setStrID(strNombreTemp + "_" + msg("report." + objModeloContenido) + "_" + strFirma);
                        objDatosReporte.setStrNombreEvaluado(objCuestionario.getCuTxDescripcion());

                        log.debug("Genera documento - " + objDatosReporte.getStrID());

                        if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion(), " ", "C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteC.setStrID(strNTempC + "_" + msg("report." + objModeloContenido) + "_" + strFC);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);

                            ReporteGrupalSumarioCategoriaGeneral objReporteR = new ReporteGrupalSumarioCategoriaGeneral();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_NIVEL_DE_PARTICIPACION)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion(), " ", "C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteC.setStrID(strNTempC + "_" + msg("report." + objModeloContenido) + "_" + strFC);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);

                            ReporteGrupalNivelParticipacion objReporteR = new ReporteGrupalNivelParticipacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_RESPUESTAS)) {
                            log.debug("Hace Excel");
                            ReporteTodasRespuestas objReporte = new ReporteTodasRespuestas();
                            objDatosReporte.setStrID(objReporte.build(objDatosReporte, map, objCuestionario.getCuIdCuestionarioPk()));
                            objDatosReporte.setBlDefinitivo(true);
                            lstDefinitivos.add(objDatosReporte);
                        }

                    }

                    if (!lstTemporales.isEmpty()) {

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrID(objCuestionario.getCuTxDescripcion() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS));
                        objDatosReporte.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporales, objDatosReporte));
                        objDatosReporte.setStrID(Utilitarios.putPageNumber(objDatosReporte.getStrID()));
                        objDatosReporte.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporte);

                        Utilitarios.eliminaArchivosTemporales(lstTemporales);

                    }

                }

                if (!lstDefinitivos.isEmpty()) {

                    String ZipName = msg("report.group") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);

                    FileOutputStream salida = new FileOutputStream(objFile);

                    boolean flag = Utilitarios.zipArchivos(lstDefinitivos, salida);

                    if (flag) {
                        InputStream stream = new FileInputStream(objFile.getAbsolutePath());

                        fileGrupal = DefaultStreamedContent.builder()
                                .name(ZipName)
                                .contentType("application/zip")
                                .stream(() -> stream)
                                .build();

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

            List<DatosReporte> lstTemporales = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if (!lstSeleccionadosIndividual.isEmpty() && !lstEvaluadosSeleccionados.isEmpty()) {

                Collections.sort(lstSeleccionadosIndividual, new ReportSort());

                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

                Map map = new HashMap();

                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango);
                DatosReporte objUtil = new DatosReporte();
                objUtil.setStrID(utilReport);
                objUtil.setBlDefinitivo(false);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, utilReport);

                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for (Evaluado objParticipante : lstEvaluadosSeleccionados) {

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

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrNombre(msg("report." + objModeloContenido));
                        objDatosReporte.setStrDescripcion(msg("report." + objModeloContenido));
                        objDatosReporte.setMapRelaciones(mapRelaciones);
                        objDatosReporte.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReporte.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReporte.setIntMaxRango(intMaxRango);

                        String strNombreTemp = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "_");
                        String strFirma = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                + Utilitarios.generateRandom(strNombreTemp);

                        objDatosReporte.setStrID(strNombreTemp + "_" + msg("report." + objModeloContenido) + "_" + strFirma);
                        objDatosReporte.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                        if (flag) {
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempC);

                            objDatosReporteC.setStrID(strNTempC + "_" + msg("report." + objModeloContenido) + "_" + strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(true);
                            lstTemporales.add(objDatosReporteC);
                            flag = false;
                        }

                        if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA)) {

                            List<DatosReporte> lstTemp = new ArrayList();
                            /*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+msg("report."+objModeloContenido) +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
                             */
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualSumarioCategoria objReporteR = new ReporteIndividualSumarioCategoria();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO)) {

                            List<DatosReporte> lstTemp = new ArrayList();
                            /*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+msg("report."+objModeloContenido) +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
                             */
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualSumarioCategoriaMismo objReporteR = new ReporteIndividualSumarioCategoriaMismo();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA)) {

                            List<DatosReporte> lstTemp = new ArrayList();
                            /*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+msg("report."+objModeloContenido) +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
                             */
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualCalificacionXCategoria objReporteR = new ReporteIndividualCalificacionXCategoria();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO)) {

                            List<DatosReporte> lstTemp = new ArrayList();
                            /*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+msg("report."+objModeloContenido) +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
                             */
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualCalificacionXCategoriaMismo objReporteR = new ReporteIndividualCalificacionXCategoriaMismo();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_OTROS)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualItemsAltaCalificacion objReporteR = new ReporteIndividualItemsAltaCalificacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_OTROS)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualItemsBajaCalificacion objReporteR = new ReporteIndividualItemsBajaCalificacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);
                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_MISMO)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualItemsAltaCalificacionMismo objReporteR = new ReporteIndividualItemsAltaCalificacionMismo();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_MISMO)) {

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualItemsBajaCalificacionMismo objReporteR = new ReporteIndividualItemsBajaCalificacionMismo();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_PREGUNTAS_ABIERTAS)) {

                            List<DatosReporte> lstTemp = new ArrayList();
                            /*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+msg("report."+objModeloContenido) +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
                             */
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion(), " ", "R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS) + "_"
                                    + Utilitarios.generateRandom(strNTempR);

                            objDatosReporteR.setStrID(strNTempR + "_" + msg("report." + objModeloContenido) + "_" + strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualPreguntasAbiertas objReporteR = new ReporteIndividualPreguntasAbiertas();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            //                          lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        }

                    }

                    if (!lstTemporales.isEmpty()) {

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrID(objParticipante.getPaTxDescripcion() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS));
                        objDatosReporte.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporales, objDatosReporte));
                        objDatosReporte.setStrID(Utilitarios.putPageNumber(objDatosReporte.getStrID()));
                        objDatosReporte.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporte);

                        Utilitarios.eliminaArchivosTemporales(lstTemporales);

                    }

                }

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

        } catch (Exception e) {
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
                        objMovimiento.setMoInCantidad(objEvaluado.getIntNumberEvaluators());
                        objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_EJECUTA_LICENCIA_INDIVIDUAL));
                        objReferenciaMovimiento.setMovimiento(objMovimiento);
                        objMovimiento.getReferenciaMovimientos().add(objReferenciaMovimiento);
                        lstMovements.add(objMovimiento);
                    } else {
                        Movimiento objMovimiento = new Movimiento();
                        objMovimiento.setMoInCantidad(objEvaluado.getIntNumberEvaluators());
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
                generaReporteIndividual();
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

}
