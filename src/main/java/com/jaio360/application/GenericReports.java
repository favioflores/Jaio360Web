/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.application;

import com.jaio360.dao.AnalisisParticipanteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.ReporteGeneradoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.Evaluado;
import com.jaio360.orm.AnalisisParticipante;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.report.ElementoGrupalUtiles;
import com.jaio360.report.ReporteGrupalCaratula;
import com.jaio360.report.ReporteGrupalNivelParticipacion;
import com.jaio360.report.ReporteGrupalSumarioCategoriaGeneral;
import com.jaio360.report.ReporteGrupalSumarioCategoriaGeneralWeighted;
import com.jaio360.report.ReporteIndividualCalificacionXCategoria;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismo;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismoWeighted;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaWeighted;
import com.jaio360.report.ReporteIndividualCaratula;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacion;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionMismo;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacionWeighted;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacion;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacionWeighted;
import com.jaio360.report.ReporteIndividualPreguntasAbiertas;
import com.jaio360.report.ReporteIndividualSumarioCategoria;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoRelacion;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoRelacionWeighted;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosProm;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismoVsOtrosPromWeighted;
import com.jaio360.report.ReporteIndividualSumarioCategoriaRelacion;
import com.jaio360.report.ReporteIndividualSumarioCategoriaWeighted;
import com.jaio360.report.ReporteTodasRespuestas;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.primefaces.util.IOUtils;

/**
 *
 * @author Favio
 */
public class GenericReports extends Thread implements Serializable {

    private Logger log = Logger.getLogger(GenericReports.class);

    private Integer idTipoAnalisis;
    private ReporteGeneradoDAO objReporteGeneradoDAO;
    private AnalisisParticipanteDAO objAnalisisParticipanteDAO;
    private ReporteGenerado objReporteGenerado;
    private List<Evaluado> lstEvaluadosSeleccionados;
    private List<Cuestionario> lstCuestionariosSeleccionados;
    private List<Integer> lstSeleccionadosIndividual;
    private List<Integer> lstSeleccionadosGrupal;

    public GenericReports() {
        objReporteGeneradoDAO = new ReporteGeneradoDAO();
        objAnalisisParticipanteDAO = new AnalisisParticipanteDAO();
    }

    public GenericReports(Integer idTipoAnalisis, ReporteGenerado objReporteGenerado,
            List<Evaluado> lstEvaluadosSeleccionados,
            List<Cuestionario> lstCuestionariosSeleccionados,
            List<Integer> lstSeleccionadosIndividual,
            List<Integer> lstSeleccionadosGrupal) {
        this.idTipoAnalisis = idTipoAnalisis;
        this.objReporteGeneradoDAO = new ReporteGeneradoDAO();
        this.objAnalisisParticipanteDAO = new AnalisisParticipanteDAO();
        this.objReporteGenerado = objReporteGenerado;
        this.lstEvaluadosSeleccionados = lstEvaluadosSeleccionados;
        this.lstCuestionariosSeleccionados = lstCuestionariosSeleccionados;
        this.lstSeleccionadosIndividual = lstSeleccionadosIndividual;
        this.lstSeleccionadosGrupal = lstSeleccionadosGrupal;
    }

    @Override
    public void run() {

        if (this.idTipoAnalisis.equals(Constantes.INT_REPORTE_GRUPAL)) {
            generaReporteGrupal();
        } else {
            generaReporteIndividual();
        }

    }

    public void generarAnalisis(Integer idTipoAnalisis, ReporteGenerado objReporteGenerado,
            List<Evaluado> lstEvaluadosSeleccionados,
            List<Cuestionario> lstCuestionariosSeleccionados,
            List<Integer> lstSeleccionadosIndividual,
            List<Integer> lstSeleccionadosGrupal) {
        GenericReports thread = new GenericReports(idTipoAnalisis, objReporteGenerado, lstEvaluadosSeleccionados, lstCuestionariosSeleccionados, lstSeleccionadosIndividual, lstSeleccionadosGrupal);
        thread.start();
    }

    private BigDecimal calculateProgress(int intDocumentsDone, int intTotalDocuments) throws Exception {

        return new BigDecimal(intDocumentsDone).divide(new BigDecimal(intTotalDocuments), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private void generaReporteIndividual() {

        try {

            int intTotalDocuments = lstSeleccionadosIndividual.size() * lstEvaluadosSeleccionados.size();
            int intDocumentsDone = 0;

            ResourceBundle rb = ResourceBundle.getBundle("etiquetas");

            verificaDirectorios();

            List<DatosReporte> lstTemporalesPDFxCombinar;
            List<DatosReporte> lstTemporalesOtros = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if (!lstSeleccionadosIndividual.isEmpty() && !lstEvaluadosSeleccionados.isEmpty()) {

                //Collections.sort(lstSeleccionadosIndividual, new ReportSort());
                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();
                ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(objReporteGenerado.getProyectoInfo().getIntIdProyecto());

                MensajeDAO objMensajeDAO = new MensajeDAO();
                Mensaje objMensaje = objMensajeDAO.obtenMensaje(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_CONVOCATORIA);

                String strLogoCliente = Constantes.strVacio;

                if (objMensaje != null) {
                    if (Utilitarios.noEsNuloOVacio(objMensaje.getMeTxConvocatoriaURL())) {
                        strLogoCliente = Utilitarios.decodeUTF8(objMensaje.getMeTxConvocatoriaURL());
                    }
                }

                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

                Map map = new HashMap();

                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango, lstTemporalesOtros);
                DatosReporte objUtil = new DatosReporte();
                objUtil.setStrID(utilReport);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, Utilitarios.getPathTempPreliminar() + File.separator + utilReport);

                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                int count = 0;

                objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_ENPROCESO);
                objReporteGenerado.setRgDtFechaUltMod(new Date());

                objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

                for (Evaluado objEvaluado : lstEvaluadosSeleccionados) {

                    log.info("Inicio análisis - " + count++ + " " + objEvaluado.getPaTxCorreo() + " - " + new Date());

                    lstTemporalesPDFxCombinar = new ArrayList<>();

                    Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());

                    LinkedHashMap mapRelaciones = cargarRelaciones(objParticipante);

                    Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk(), objReporteGenerado.getProyectoInfo().getIntIdProyecto());

                    boolean flag = true;

                    for (Integer objModeloContenido : lstSeleccionadosIndividual) {

                        log.info("Reporte - " + objModeloContenido + " - " + new Date());

                        DatosReporte objDatosReportePrincipal = new DatosReporte();
                        objDatosReportePrincipal.setStrNombreProyecto(objReporteGenerado.getProyectoInfo().getStrDescNombre());
                        objDatosReportePrincipal.setStrDescripcionProyecto(objReporteGenerado.getProyectoInfo().getStrMotivo());
                        objDatosReportePrincipal.setIntEvaluado(objParticipante.getPaIdParticipantePk());
                        objDatosReportePrincipal.setStrDescripcion(rb.getString("report." + objModeloContenido));
                        objDatosReportePrincipal.setMapRelaciones(mapRelaciones);
                        objDatosReportePrincipal.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReportePrincipal.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReportePrincipal.setIntMaxRango(intMaxRango);
                        objDatosReportePrincipal.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        objDatosReportePrincipal.setStrURLCliente(strLogoCliente);
                        objDatosReportePrincipal.setStrEmailEvaluado(objParticipante.getPaTxCorreo());

                        if (flag) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrID(strNameFile);
                            objDatosReporte.setStrNombreProyecto(objDatosReportePrincipal.getStrNombreProyecto());
                            objDatosReporte.setStrDescripcionProyecto(objDatosReportePrincipal.getStrDescripcionProyecto());
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setStrURLCliente(objDatosReportePrincipal.getStrURLCliente());
                            objDatosReporte.setIntEvaluado(objDatosReportePrincipal.getIntEvaluado());
                            objDatosReporte.setStrEmailEvaluado(objDatosReportePrincipal.getStrEmailEvaluado());

                            objDatosReporte.setBlWeighted(this.objReporteGenerado.getRgblWeighted());

                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporte.setStrID(objReporteC.build(objDatosReporte, strNameFile, objReporteGenerado));

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
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoria objReporteR = new ReporteIndividualSumarioCategoria();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaWeighted objReporteR = new ReporteIndividualSumarioCategoriaWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaMismoVsOtrosProm objReporteR = new ReporteIndividualSumarioCategoriaMismoVsOtrosProm();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaMismoVsOtrosPromWeighted objReporteR = new ReporteIndividualSumarioCategoriaMismoVsOtrosPromWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualCalificacionXCategoria objReporteR = new ReporteIndividualCalificacionXCategoria();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualCalificacionXCategoriaWeighted objReporteR = new ReporteIndividualCalificacionXCategoriaWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualCalificacionXCategoriaMismo objReporteR = new ReporteIndividualCalificacionXCategoriaMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualCalificacionXCategoriaMismoWeighted objReporteR = new ReporteIndividualCalificacionXCategoriaMismoWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_OTROS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsAltaCalificacion objReporteR = new ReporteIndividualItemsAltaCalificacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_OTROS_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsAltaCalificacionWeighted objReporteR = new ReporteIndividualItemsAltaCalificacionWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_OTROS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsBajaCalificacion objReporteR = new ReporteIndividualItemsBajaCalificacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_OTROS_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsBajaCalificacionWeighted objReporteR = new ReporteIndividualItemsBajaCalificacionWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsAltaCalificacionMismo objReporteR = new ReporteIndividualItemsAltaCalificacionMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_MISMO)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualItemsAltaCalificacionMismo objReporteR = new ReporteIndividualItemsAltaCalificacionMismo();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_GENERAL_X_CATEGORIA_RELACION)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaRelacion objReporteR = new ReporteIndividualSumarioCategoriaRelacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_PREGUNTAS_ABIERTAS)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario().toUpperCase());

                            ReporteIndividualPreguntasAbiertas objReporteR = new ReporteIndividualPreguntasAbiertas();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO_RELACION)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaMismoRelacion objReporteR = new ReporteIndividualSumarioCategoriaMismoRelacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO_RELACION_WEIGHTED)) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setStrNombreEvaluado(objDatosReportePrincipal.getStrNombreEvaluado());
                            objDatosReporte.setMapRelaciones(objDatosReportePrincipal.getMapRelaciones());
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion().toUpperCase());

                            ReporteIndividualSumarioCategoriaMismoRelacionWeighted objReporteR = new ReporteIndividualSumarioCategoriaMismoRelacionWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, objParticipante.getPaIdParticipantePk(), strNameFile, objReporteGenerado));

                            lstTemporalesPDFxCombinar.add(objDatosReporte);

                        }

                        intDocumentsDone++;
                        objReporteGenerado.setRgBlPorcentajeAvance(calculateProgress(intDocumentsDone, intTotalDocuments));
                        objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

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

                        AnalisisParticipante objAnalisisParticipante = new AnalisisParticipante();
                        objAnalisisParticipante.setPaIdParticipanteFk(objParticipante.getPaIdParticipantePk());
                        objAnalisisParticipante.setReporteGenerado(objReporteGenerado);
                        objAnalisisParticipante.setApFilename(objDatosReporte.getStrID());

                        File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID());

                        InputStream stream = new FileInputStream(file.getAbsolutePath());

                        objAnalisisParticipante.setApArchivo(IOUtils.toByteArray(stream));

                        objAnalisisParticipanteDAO.guardaAnalisisParticipante(objAnalisisParticipante);
                    }

                    log.info("Fin análisis - " + objEvaluado.getPaTxCorreo() + " - " + new Date());

                }

                log.info("eliminaArchivosTemporales");
                Utilitarios.eliminaArchivosTemporales(lstTemporalesOtros);

                if (!lstDefinitivos.isEmpty()) {

                    //String ZipName = rb.getString("report.individual") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    //File objFile = new File(Utilitarios.getPathTempDefinitivo() + File.separator + ZipName);
                    //FileOutputStream salida = new FileOutputStream(objFile);
                    //boolean flag = Utilitarios.zipArchivos(lstDefinitivos, salida);
                    //if (flag) {
//                        InputStream stream = new FileInputStream(objFile.getAbsolutePath());
                    /*
                        fileIndividual = DefaultStreamedContent.builder()
                                .name(ZipName)
                                .contentType("application/zip")
                                .stream(() -> stream)
                                .build();
                     */
                    //                      log.info("Archivo listo para descarga");
                    String ZipName = rb.getString("report.individual") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    objReporteGenerado.setRgTxNombreArchivo(ZipName);
                    objReporteGenerado.setRgDtFechaUltMod(new Date());
                    //                    byte[] content = IOUtils.toByteArray(stream);
                    //                   objReporteGenerado.setRgContenido(content);
                    objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_TERMINADO);
                    objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

                } else {
                    objReporteGenerado.setRgDtFechaUltMod(new Date());
                    objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_FALLIDO);
                    objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);
                }
                //}

            }

        } catch (Exception ex) {
            log.error(ex, ex);
            objReporteGenerado.setRgDtFechaUltMod(new Date());
            objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_FALLIDO);
            objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);
        }
    }

    private void verificaDirectorios() {

        File directoryInboxP = new File(Utilitarios.getPathTempPreliminar());
        File directoryInboxD = new File(Utilitarios.getPathTempDefinitivo());

        if (!directoryInboxP.exists()) {
            directoryInboxP.mkdir();
            if (!directoryInboxP.exists()) {
                log.error("Can't create directory " + Utilitarios.getPathTempPreliminar());
            }
        }

        if (!directoryInboxD.exists()) {
            directoryInboxD.mkdir();
            if (!directoryInboxD.exists()) {
                log.error("Can't create directory " + Utilitarios.getPathTempPreliminar());
            }
        }

    }

    private LinkedHashMap cargarRelaciones(Participante objParticipante) {

        DetalleMetricaDAO objDetalleMetricaDAO = new DetalleMetricaDAO();

        LinkedHashMap mapRelaciones = new LinkedHashMap();

        Integer intMaxRango = objDetalleMetricaDAO.obtenMaxMetricaProyecto(objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        if (objParticipante.getPaInAutoevaluar()) {
            Relacion objRelacion = new Relacion();
            objRelacion.setReIdRelacionPk(-1);
            objRelacion.setReNuOrden(intMaxRango + 1);
            objRelacion.setReTxAbreviatura("AUTO");
            objRelacion.setReTxDescripcion("Autoevaluación");
            objRelacion.setReTxNombre("Autoevaluación");
            objRelacion.setReColor("212324");
            objRelacion.setReDePonderacion(Constantes.PERCENT_100);
            mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        Relacion objRelacion = new Relacion();
        objRelacion.setReIdRelacionPk(-1);
        objRelacion.setReNuOrden(intMaxRango + 1);
        objRelacion.setReTxAbreviatura("PROM");
        objRelacion.setReTxDescripcion("Promedio");
        objRelacion.setReTxNombre("Promedio");
        objRelacion.setReColor("494D4F");
        objRelacion.setReDePonderacion(Constantes.PERCENT_100);
        mapRelaciones.put(objRelacion.getReTxAbreviatura(), objRelacion);

        mapRelaciones.putAll(obtieneRelaciones(objParticipante.getPaIdParticipantePk()));

        return mapRelaciones;
    }

    private Map obtieneRelaciones(Integer intIdEvaluado) {

        RelacionDAO objRelacionDAO = new RelacionDAO();

        List<Relacion> lstRelaciones = objRelacionDAO.obtenListaRelacionPorEvaluado(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), intIdEvaluado);

        Map map = new HashMap();

        for (Relacion objRelacion : lstRelaciones) {
            map.put(objRelacion.getReTxAbreviatura(), objRelacion);
        }

        return map;

    }

    private void generaReporteGrupal() {

        try {

            verificaDirectorios();

            int intTotalDocuments = lstSeleccionadosGrupal.size() * lstCuestionariosSeleccionados.size();
            int intDocumentsDone = 0;

            List<DatosReporte> lstTemporalesPDFxCombinar;
            List<DatosReporte> lstTemporalesOtros = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            ResourceBundle rb = ResourceBundle.getBundle("etiquetas");

            if (!lstSeleccionadosGrupal.isEmpty() && !lstCuestionariosSeleccionados.isEmpty()) {

                objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_ENPROCESO);
                objReporteGenerado.setRgDtFechaUltMod(new Date());

                objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

                //Collections.sort(lstSeleccionadosGrupal, new ReportSort());
                DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();

                Integer intMaxRango = detalleMetricaDAO.obtenMaxMetricaProyecto(objReporteGenerado.getProyectoInfo().getIntIdProyecto());

                Map map = new HashMap();

                /*
                ElementoGrupalUtiles objElementoGrupalUtiles = new ElementoGrupalUtiles();
                String utilReport = objElementoGrupalUtiles.build(intMaxRango, lstTemporalesOtros);
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA, Utilitarios.getPathTempPreliminar() + File.separator + utilReport);
                 */
                boolean flag = true;


                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for (Cuestionario objCuestionario : lstCuestionariosSeleccionados) {

                    lstTemporalesPDFxCombinar = new ArrayList<>();

                    for (Integer objModeloContenido : lstSeleccionadosGrupal) {

                        DatosReporte objDatosReportePrincipal = new DatosReporte();
                        objDatosReportePrincipal.setStrNombreProyecto(objReporteGenerado.getProyectoInfo().getStrDescNombre());
                        objDatosReportePrincipal.setStrDescripcionProyecto(objReporteGenerado.getProyectoInfo().getStrMotivo());
                        objDatosReportePrincipal.setStrDescripcion(rb.getString("report." + objModeloContenido));
                        objDatosReportePrincipal.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReportePrincipal.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReportePrincipal.setIntMaxRango(intMaxRango);

                        /*
                        if (flag) {

                            String strNameFile = Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();

                            DatosReporte DRCaratula = new DatosReporte();
                            DRCaratula.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());

                            DRCaratula.setStrID(objReporteC.build(objDatosReportePrincipal, strNameFile));
                            lstTemporalesPDFxCombinar.add(DRCaratula);

                            flag = false;
                        }
                         */
                        if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA)) {

                            String strNameFile = objDatosReportePrincipal.getStrCuestionario() + "_" + objDatosReportePrincipal.getStrDescripcion() + "_" + Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrNombreProyecto(objDatosReportePrincipal.getStrNombreProyecto());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteGrupalSumarioCategoriaGeneral objReporteR = new ReporteGrupalSumarioCategoriaGeneral();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, strNameFile, objReporteGenerado));

                            lstDefinitivos.add(objDatosReporte);

                            AnalisisParticipante objAnalisisParticipante = new AnalisisParticipante();
                            objAnalisisParticipante.setReporteGenerado(objReporteGenerado);
                            objAnalisisParticipante.setApFilename(objDatosReporte.getStrID());

                            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID());

                            InputStream stream = new FileInputStream(file.getAbsolutePath());

                            objAnalisisParticipante.setApArchivo(IOUtils.toByteArray(stream));

                            objAnalisisParticipanteDAO.guardaAnalisisParticipante(objAnalisisParticipante);

                            stream.close();

                        } if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA_WEIGHTED)) {

                            String strNameFile = objDatosReportePrincipal.getStrCuestionario() + "_" + objDatosReportePrincipal.getStrDescripcion() + "_" + Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setIntMaxRango(objDatosReportePrincipal.getIntMaxRango());
                            objDatosReporte.setStrNombreProyecto(objDatosReportePrincipal.getStrNombreProyecto());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteGrupalSumarioCategoriaGeneralWeighted objReporteR = new ReporteGrupalSumarioCategoriaGeneralWeighted();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, strNameFile, objReporteGenerado));

                            lstDefinitivos.add(objDatosReporte);

                            AnalisisParticipante objAnalisisParticipante = new AnalisisParticipante();
                            objAnalisisParticipante.setReporteGenerado(objReporteGenerado);
                            objAnalisisParticipante.setApFilename(objDatosReporte.getStrID());

                            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID());

                            InputStream stream = new FileInputStream(file.getAbsolutePath());

                            objAnalisisParticipante.setApArchivo(IOUtils.toByteArray(stream));

                            objAnalisisParticipanteDAO.guardaAnalisisParticipante(objAnalisisParticipante);

                            stream.close();

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_NIVEL_DE_PARTICIPACION)) {

                            String strNameFile = objDatosReportePrincipal.getStrCuestionario() + "_" + objDatosReportePrincipal.getStrDescripcion() + "_" + Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setStrNombreProyecto(objDatosReportePrincipal.getStrNombreProyecto());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteGrupalNivelParticipacion objReporteR = new ReporteGrupalNivelParticipacion();
                            objDatosReporte.setStrID(objReporteR.build(objDatosReporte, map, strNameFile, objReporteGenerado));

                            lstDefinitivos.add(objDatosReporte);

                            AnalisisParticipante objAnalisisParticipante = new AnalisisParticipante();
                            objAnalisisParticipante.setReporteGenerado(objReporteGenerado);
                            objAnalisisParticipante.setApFilename(objDatosReporte.getStrID());

                            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID());

                            InputStream stream = new FileInputStream(file.getAbsolutePath());

                            objAnalisisParticipante.setApArchivo(IOUtils.toByteArray(stream));

                            objAnalisisParticipanteDAO.guardaAnalisisParticipante(objAnalisisParticipante);

                            stream.close();

                        } else if (objModeloContenido.equals(Constantes.INT_REPORTE_GRUPAL_RESPUESTAS)) {

                            String strNameFile = objDatosReportePrincipal.getStrCuestionario() + "_" + objDatosReportePrincipal.getStrDescripcion() + "_" + Utilitarios.generaIDReporte() + "_" + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS);

                            DatosReporte objDatosReporte = new DatosReporte();
                            objDatosReporte.setIntIdCuestionario(objDatosReportePrincipal.getIntIdCuestionario());
                            objDatosReporte.setStrCuestionario(objDatosReportePrincipal.getStrCuestionario());
                            objDatosReporte.setStrNombreProyecto(objDatosReportePrincipal.getStrNombreProyecto());
                            objDatosReporte.setStrDescripcion(objDatosReportePrincipal.getStrDescripcion());

                            ReporteTodasRespuestas objReporte = new ReporteTodasRespuestas();
                            objDatosReporte.setStrID(objReporte.build(objDatosReporte, map, objCuestionario.getCuIdCuestionarioPk(), strNameFile, objReporteGenerado));
                            objDatosReporte.setBlDefinitivo(true);

                            lstDefinitivos.add(objDatosReporte);

                            AnalisisParticipante objAnalisisParticipante = new AnalisisParticipante();
                            objAnalisisParticipante.setReporteGenerado(objReporteGenerado);
                            objAnalisisParticipante.setApFilename(objDatosReporte.getStrID());

                            File file = new File(Utilitarios.getPathTempPreliminar() + File.separator + objDatosReporte.getStrID());

                            InputStream stream = new FileInputStream(file.getAbsolutePath());

                            objAnalisisParticipante.setApArchivo(IOUtils.toByteArray(stream));

                            objAnalisisParticipanteDAO.guardaAnalisisParticipante(objAnalisisParticipante);

                            stream.close();

                        }

                        intDocumentsDone++;
                        objReporteGenerado.setRgBlPorcentajeAvance(calculateProgress(intDocumentsDone, intTotalDocuments));
                        objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

                    }

                    if (!lstDefinitivos.isEmpty()) {

                        Utilitarios.eliminaArchivosTemporales(lstDefinitivos);

                    }

                }

                //Utilitarios.eliminaArchivosTemporales(lstTemporalesOtros);
                if (!lstDefinitivos.isEmpty()) {

                    //String ZipName = rb.getString("report.group") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    //File objFile = new File(Utilitarios.getPathTempDefinitivo() + File.separator + ZipName);
                    //FileOutputStream salida = new FileOutputStream(objFile);
                    //boolean flagFinal = Utilitarios.zipArchivos(lstDefinitivos, salida);
                    //if (flagFinal) {
                    //InputStream stream = new FileInputStream(objFile.getAbsolutePath());
                    /*
                        fileIndividual = DefaultStreamedContent.builder()
                                .name(ZipName)
                                .contentType("application/zip")
                                .stream(() -> stream)
                                .build();
                     */
                    //log.info("Archivo listo para descarga");
                    String ZipName = rb.getString("report.group") + " " + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    objReporteGenerado.setRgTxNombreArchivo(ZipName);
                    objReporteGenerado.setRgDtFechaUltMod(new Date());

                    //byte[] content = IOUtils.toByteArray(stream);
                    //objReporteGenerado.setRgContenido(content);
                    objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_TERMINADO);
                    objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);

                } else {
                    objReporteGenerado.setRgDtFechaUltMod(new Date());
                    objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_FALLIDO);
                    objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);
                }
                //}

            }

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            objReporteGenerado.setRgDtFechaUltMod(new Date());
            objReporteGenerado.setRgEstado(Constantes.INT_ET_ESTADO_REPORTE_GENERADO_FALLIDO);
            objReporteGeneradoDAO.actualizaReporteGenerado(objReporteGenerado);
        }

    }
}
