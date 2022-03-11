package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.ModeloContenido;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Relacion;
import com.jaio360.report.ElementoGrupalUtiles;
import com.jaio360.report.ReporteGrupalCaratula;
import com.jaio360.report.ReporteGrupalNivelParticipacion;
import com.jaio360.report.ReporteGrupalSumarioCategoriaGeneral;
import com.jaio360.report.ReporteIndividualCaratula;
import com.jaio360.report.ReporteIndividualPreguntasAbiertas;
import com.jaio360.report.ReporteIndividualCalificacionXCategoria;
import com.jaio360.report.ReporteIndividualCalificacionXCategoriaMismo;
import com.jaio360.report.ReporteIndividualItemsAltaCalificacion;
import com.jaio360.report.ReporteIndividualItemsBajaCalificacion;
import com.jaio360.report.ReporteIndividualSumarioCategoria;
import com.jaio360.report.ReporteIndividualSumarioCategoriaMismo;
import com.jaio360.report.ReporteTodasRespuestas;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.ReportSort;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "generaReportesView")
@ViewScoped
public class GeneraReportesView implements Serializable{
    
    private static final Log log = LogFactory.getLog(GeneraReportesView.class);
    
    private List<ModeloContenido> lstContenidoIndividual;
    private List<ModeloContenido> lstContenidoGrupal;
    private List<ModeloContenido> lstSeleccionadosIndividual;
    private List<ModeloContenido> lstSeleccionadosGrupal;
    
    private List<Participante> lstEvaluados;
    private List<Cuestionario> lstCuestionarios;
    private List<Cuestionario> lstCuestionariosSeleccionados;
    private List<Participante> lstEvaluadosSeleccionados;
    private List<Cuestionario> lstFiltroCuestionarios;
    private List<Participante> lstFiltroEvaluados;
    
    private String[] strDescripcionesIndividual = new String[15];
    private String[] strDescripcionesGrupal     = new String[15];
    
    private StreamedContent fileGrupal;
    private StreamedContent fileIndividual;
    private StreamedContent planAccion;

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

    public List<Participante> getLstEvaluadosSeleccionados() {
        return lstEvaluadosSeleccionados;
    }

    public void setLstEvaluadosSeleccionados(List<Participante> lstEvaluadosSeleccionados) {
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

    public List<Participante> getLstEvaluados() {
        return lstEvaluados;
    }

    public void setLstEvaluados(List<Participante> lstEvaluados) {
        this.lstEvaluados = lstEvaluados;
    }

    public List<ModeloContenido> getLstContenidoIndividual() {
        return lstContenidoIndividual;
    }

    public void setLstContenidoIndividual(List<ModeloContenido> lstContenidoIndividual) {
        this.lstContenidoIndividual = lstContenidoIndividual;
    }

    public List<ModeloContenido> getLstContenidoGrupal() {
        return lstContenidoGrupal;
    }

    public void setLstContenidoGrupal(List<ModeloContenido> lstContenidoGrupal) {
        this.lstContenidoGrupal = lstContenidoGrupal;
    }

    public List<ModeloContenido> getLstSeleccionadosIndividual() {
        return lstSeleccionadosIndividual;
    }

    public void setLstSeleccionadosIndividual(List<ModeloContenido> lstSeleccionadosIndividual) {
        this.lstSeleccionadosIndividual = lstSeleccionadosIndividual;
    }

    public List<ModeloContenido> getLstSeleccionadosGrupal() {
        return lstSeleccionadosGrupal;
    }

    public void setLstSeleccionadosGrupal(List<ModeloContenido> lstSeleccionadosGrupal) {
        this.lstSeleccionadosGrupal = lstSeleccionadosGrupal;
    }

    public List<Cuestionario> getLstCuestionarios() {
        return lstCuestionarios;
    }

    public void setLstCuestionarios(List<Cuestionario> lstCuestionarios) {
        this.lstCuestionarios = lstCuestionarios;
    }
    

    @PostConstruct
    public void init(){
        
        lstContenidoIndividual = new ArrayList<>();
        lstContenidoGrupal     = new ArrayList<>();
        
        agregarDescripciones();
        
        lstContenidoIndividual.add(new ModeloContenido(1,"Sumario de categoria","",strDescripcionesIndividual[1],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(2,"Sumario de categoria - Mismo/Otro","",strDescripcionesIndividual[2],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(3,"Calificaciones por item - Por categoria (Desglozado)","",strDescripcionesIndividual[3],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(4,"Calificaciones por item - Promedio","",strDescripcionesIndividual[4],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(5,"Items con más alta calificación","",strDescripcionesIndividual[5],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(6,"Items con más baja calificación","",strDescripcionesIndividual[6],"PDF"));
        lstContenidoIndividual.add(new ModeloContenido(7,"Preguntas abiertas","",strDescripcionesIndividual[7],"PDF"));
        
        lstContenidoGrupal.add(new ModeloContenido(8,"Todas las respuestas","",strDescripcionesGrupal[8],"EXCEL"));
        lstContenidoGrupal.add(new ModeloContenido(9,"Sumario por categorias","",strDescripcionesGrupal[0],"PDF"));
        lstContenidoGrupal.add(new ModeloContenido(10,"Nivel de participación","",strDescripcionesGrupal[0],"PDF"));
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
        
        lstEvaluados = objParticipanteDAO.obtenListaParticipanteXEstado(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
        
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
    
    public void generarReporteIndividual(){
        
        if(lstSeleccionadosIndividual.isEmpty()){
        
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un modelo de los reportes");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }else if (lstEvaluadosSeleccionados.isEmpty()){
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un evaluado");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }else{
        
            /* Genera reportes */
            generaReporteIndividual();
        
        }
        
    }
    
    public void generarReporteGrupal(){
        
        if(lstSeleccionadosGrupal.isEmpty()){
        
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un modelo de los reportes grupales");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }else if (lstCuestionariosSeleccionados.isEmpty()){
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Generar reportes", "Debe elegir al menos un cuestionario");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }else{
        
            /* Genera reportes */
            generaReporteGrupal();
        
        }
        
    }

    private void generaReporteGrupal() {

        try {
            
            verificaDirectorios();

            List<DatosReporte> lstTemporales = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if(!lstSeleccionadosGrupal.isEmpty() && !lstCuestionariosSeleccionados.isEmpty()){

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
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA,utilReport);
                        
                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for(Cuestionario objCuestionario : lstCuestionariosSeleccionados){

                    for(ModeloContenido objModeloContenido : lstSeleccionadosGrupal){

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrNombre(objModeloContenido.getIntModeloPk().toString());
                        objDatosReporte.setStrDescripcion(objModeloContenido.getStrDescModelo());
                        //objDatosReporte.setMapRelaciones(mapRelaciones);
                        objDatosReporte.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReporte.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReporte.setIntMaxRango(intMaxRango);

                        String strNombreTemp = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion()," ","_");
                        String strFirma = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                          Utilitarios.generateRandom(strNombreTemp);

                        objDatosReporte.setStrID(strNombreTemp+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFirma);
                        objDatosReporte.setStrNombreEvaluado(objCuestionario.getCuTxDescripcion());

                        log.debug("Genera documento - " + objDatosReporte.getStrID());

                        if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA)){

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);

                            ReporteGrupalSumarioCategoriaGeneral objReporteR = new ReporteGrupalSumarioCategoriaGeneral();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_GRUPAL_NIVEL_DE_PARTICIPACION)){

                            List<DatosReporte> lstTemp = new ArrayList();

                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);

                            ReporteGrupalCaratula objReporteC = new ReporteGrupalCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);

                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objCuestionario.getCuTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS);

                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);

                            ReporteGrupalNivelParticipacion objReporteR = new ReporteGrupalNivelParticipacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);

                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);

                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_GRUPAL_RESPUESTAS)){
                            log.debug("Hace Excel");
                            ReporteTodasRespuestas objReporte = new ReporteTodasRespuestas();
                            objDatosReporte.setStrID(objReporte.build(objDatosReporte, map, objCuestionario.getCuIdCuestionarioPk()));
                            objDatosReporte.setBlDefinitivo(true); 
                            lstDefinitivos.add(objDatosReporte);
                        }
                    
                    }
                    
                    if(!lstTemporales.isEmpty()){

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrID(objCuestionario.getCuTxDescripcion()+"_"+ Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS));
                        objDatosReporte.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporales, objDatosReporte));
                        objDatosReporte.setStrID(Utilitarios.putPageNumber(objDatosReporte.getStrID()));
                        objDatosReporte.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporte);

                        Utilitarios.eliminaArchivosTemporales(lstTemporales);
                        
                    }

                }

                if(!lstDefinitivos.isEmpty()){

                    log.debug("Inicia creación de ZIP");

                    String ZipName = "Reporte_grupal_" + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);

                    log.debug("Se creará archivo " + objFile.getAbsolutePath());

                    FileOutputStream salida = new FileOutputStream(objFile);

                    log.debug("Zipea archivos");
                    boolean flag = Utilitarios.zipArchivos(lstDefinitivos, salida);

                    if(flag){
                        log.debug("Archivo zipeado correctamente");
                        //InputStream stream = new FileInputStream(objFile.getAbsolutePath());
                        //fileGrupal = new DefaultStreamedContent(stream, "application/zip", ZipName);

                        fileGrupal = DefaultStreamedContent.builder()
                        .name(ZipName)
                        .contentType("application/zip")
                        .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                        .build();

                    }else{
                        log.debug("Error al zipear archivo");
                    }
                }
                
                                    
            }

        } catch (IOException ex) {
            log.error(ex);
        }
        
    }

    private void generaReporteIndividual() {

        try {
            
            verificaDirectorios();

            List<DatosReporte> lstTemporales = new ArrayList<>();
            List<DatosReporte> lstDefinitivos = new ArrayList<>();

            if(!lstSeleccionadosIndividual.isEmpty() && !lstEvaluadosSeleccionados.isEmpty()){

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
                map.put(Constantes.INT_PARAM_GRAF_MEDIDA,utilReport);
                        
                /* INVOCA CLASES QUE GENEREN LOS TEMPORALES */
                for(Participante objParticipante : lstEvaluadosSeleccionados){

                    Map mapRelaciones = obtieneRelaciones(objParticipante.getPaIdParticipantePk());
                    
                    Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objParticipante.getPaIdParticipantePk());
                    
                    if(objParticipante.getPaInAutoevaluar()){
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
                    
                    for(ModeloContenido objModeloContenido : lstSeleccionadosIndividual){

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrNombre(objModeloContenido.getIntModeloPk().toString());
                        objDatosReporte.setStrDescripcion(objModeloContenido.getStrDescModelo());
                        objDatosReporte.setMapRelaciones(mapRelaciones);
                        objDatosReporte.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                        objDatosReporte.setStrCuestionario(objCuestionario.getCuTxDescripcion());
                        objDatosReporte.setIntMaxRango(intMaxRango);
                        
                        String strNombreTemp = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","_");
                        String strFirma = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                          Utilitarios.generateRandom(strNombreTemp);
                        
                        objDatosReporte.setStrID(strNombreTemp+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFirma);
                        objDatosReporte.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                        log.debug("Genera documento - " + objDatosReporte.getStrID());
 
                        if(flag){
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);

                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(true);
                            lstTemporales.add(objDatosReporteC);
                            flag = false;
                        }

                        if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA)){

                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
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

                            
                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO)){
                            
                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
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

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA)){
                            
                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
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

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO)){
                            
                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
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

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION)){
                            
                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            
                            ReporteIndividualItemsAltaCalificacion objReporteR = new ReporteIndividualItemsAltaCalificacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);
                            
                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);
                            
//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION)){
                            
                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
                            objDatosReporteR.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());

                            
                            ReporteIndividualItemsBajaCalificacion objReporteR = new ReporteIndividualItemsBajaCalificacion();
                            objDatosReporteR.setStrID(objReporteR.build(objDatosReporteR, map, objParticipante.getPaIdParticipantePk()));
                            objDatosReporteR.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteR);
                            
                            objDatosReporte.setStrID(Utilitarios.combinaReportesPDF(lstTemp, objDatosReporte));
                            objDatosReporte.setBlDefinitivo(true);
                            
//                            lstTemporales.add(objDatosReporteC);
                            lstTemporales.add(objDatosReporteR);
                            lstTemporales.add(objDatosReporte);

                        }else if(objModeloContenido.getIntModeloPk().equals(Constantes.INT_REPORTE_INDIVIDUAL_PREGUNTAS_ABIERTAS)){

                            List<DatosReporte> lstTemp = new ArrayList();
/*                            
                            DatosReporte objDatosReporteC = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempC = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","C_");
                            String strFC = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempC);
                        
                            objDatosReporteC.setStrID(strNTempC+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFC);
                            objDatosReporteC.setStrNombreEvaluado(objParticipante.getPaTxDescripcion());
                        
                            ReporteIndividualCaratula objReporteC = new ReporteIndividualCaratula();
                            objDatosReporteC.setStrID(objReporteC.build(objDatosReporteC));
                            objDatosReporteC.setBlDefinitivo(false);
                            lstTemp.add(objDatosReporteC);
*/
                            DatosReporte objDatosReporteR = (DatosReporte) SerializationUtils.clone(objDatosReporte);

                            String strNTempR = Utilitarios.reemplazar(objParticipante.getPaTxDescripcion()," ","R_");
                            String strFR = Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS)+"_" +
                                           Utilitarios.generateRandom(strNTempR);
                        
                            objDatosReporteR.setStrID(strNTempR+"_"+objModeloContenido.getIntModeloPk() +"_"+ strFR);
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
                    
                    
                    if(!lstTemporales.isEmpty()){

                        DatosReporte objDatosReporte = new DatosReporte();
                        objDatosReporte.setStrID(objParticipante.getPaTxDescripcion()+"_"+ Utilitarios.formatearFecha(Utilitarios.getCurrentDate(),Constantes.DDMMYYYYHH24MISS));
                        objDatosReporte.setStrID(Utilitarios.combinaReportesPDFDefinitivos(lstTemporales, objDatosReporte));
                        objDatosReporte.setStrID(Utilitarios.putPageNumber(objDatosReporte.getStrID()));
                        objDatosReporte.setBlDefinitivo(true);
                        lstDefinitivos.add(objDatosReporte);

                        Utilitarios.eliminaArchivosTemporales(lstTemporales);
                        
                    }

                }

                if(!lstDefinitivos.isEmpty()){

                    log.debug("Inicia creación de ZIP");

                    String ZipName = "Reporte_individual_" + Utilitarios.generaIDReporte() + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);

                    log.debug("Se creará archivo " + objFile.getAbsolutePath());

                    FileOutputStream salida = new FileOutputStream(objFile);

                    log.debug("Zipea archivos");
                    boolean flag = Utilitarios.zipArchivos(lstDefinitivos, salida);

                    if(flag){
                        log.debug("Archivo zipeado correctamente");
                        //InputStream stream = new FileInputStream(objFile.getAbsolutePath());
                        //fileIndividual = new DefaultStreamedContent().getStream().stream, "application/zip");
                        
                        fileIndividual = DefaultStreamedContent.builder()
                        .name(ZipName)
                        .contentType("application/zip")
                        .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(objFile.getAbsolutePath()))
                        .build();

                    }else{
                        log.debug("Error al zipear archivo");
                    }
                }
                
                                    
            }

        } catch (IOException ex) {
            log.error(ex);
        }

    }
    
    private void verificaDirectorios(){
        
        log.debug("Inicia verificaDirectorios");
        
        File directoryInboxP = new File(Constantes.STR_INBOX_PRELIMINAR);
        File directoryInboxD = new File(Constantes.STR_INBOX_DEFINITIVO);
        
        log.debug("Verifica directoryInboxP");
        if(!directoryInboxP.exists()){
            directoryInboxP.mkdir();
            log.debug("Crea directoryInboxP");
            if(!directoryInboxP.exists()){
                log.debug("No pudo crear directoryInboxP");
            }
        }
        
        log.debug("Verifica directoryInboxD");
        if(!directoryInboxD.exists()){
            directoryInboxD.mkdir();
            log.debug("Crea directoryInboxD");
            if(!directoryInboxD.exists()){
                log.debug("No pudo crear directoryInboxD");
            }
        }
        
        log.debug("Termina verificaDirectorios");
    
    }
    
    private Map obtieneRelaciones(Integer intIdEvaluado){
    
        RelacionDAO objRelacionDAO = new RelacionDAO();
        
        List<Relacion> lstRelaciones = objRelacionDAO.obtenListaRelacionPorEvaluado(Utilitarios.obtenerProyecto().getIntIdProyecto(),intIdEvaluado);
        
        Map map = new HashMap();
        
        for(Relacion objRelacion : lstRelaciones) map.put(objRelacion.getReTxAbreviatura(), objRelacion);
        
        return map;
        
    }
    
    
}



