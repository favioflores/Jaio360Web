package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.Categorias;
import com.jaio360.domain.CuestionarioImportado;
import com.jaio360.domain.DosDatos;
import com.jaio360.domain.ErrorBean;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.validator.validaTextoIngresado;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "importarCuestionariosView")
@ViewScoped
public class importarCuestionariosView implements Serializable{

    private static Log log = LogFactory.getLog(importarCuestionariosView.class);
    
    private Integer idCuestionario;
    private String strCuestionario;
    private List<ErrorBean> lstErrores;
    private List<CuestionarioImportado> lstCuestionariosImportados;
    private List<DosDatos> lstElementosDelCuestionarios;
    private UploadedFile inputFile;
    private boolean processOk;
    private StreamedContent fileImport;
    private Integer intIdEstadoProyecto;

    public StreamedContent getFileImport() {
        try {
            
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        
        String fullPath = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "ModeloDeImportacionCuestionario.xls");
            
        File objFile = new File(fullPath);
        InputStream stream = new FileInputStream(objFile.getAbsolutePath());
        fileImport = new DefaultStreamedContent(stream, "application/xls", "ModeloDeImportacionCuestionario.xls");
        
        } catch (FileNotFoundException ex) {
            log.error(ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Modelo de importación de cuestionario", "No existe el documento. Por favor contactese con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return fileImport;
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

    
    public boolean isProcessOk() {
        return processOk;
    }
    public void setProcessOk(boolean processOk) {
        this.processOk = processOk;
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

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        this.intIdEstadoProyecto = objProyecto.getPoIdEstado();

    }
    
    public void irCrearCuestionarios(){
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("importarCuestionario");
            FacesContext.getCurrentInstance().getExternalContext().redirect("crearCuestionarios.jsf");
        } catch (IOException ex) {
            log.error(ex);
        }
    }
    
    public void cargaCuestionarios(FileUploadEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        this.lstCuestionariosImportados = new ArrayList<>();
        this.lstElementosDelCuestionarios = new ArrayList<>();
        this.lstErrores = new ArrayList<>();
        if (event.getFile() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Carga masiva", "Archivo " + event.getFile().getFileName() + " esta vacio"));
        }else{
            
            HSSFWorkbook xlsAvanzado;
            
            try {
                xlsAvanzado = new HSSFWorkbook(event.getFile().getInputstream());

                lstErrores = validaContenido(xlsAvanzado);

                if(lstErrores.isEmpty()){
                    procesaArchivo(xlsAvanzado);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Importar cuestionario", "Archivo leido exitosamente.");
                    FacesContext.getCurrentInstance().addMessage(null, message);        
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Importar cuestionario", "No te olvides de presionar \"Guardar importación\"");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    processOk = true;
                }
                            
            } catch (IOException ex) {
                log.error(ex);
            }
            
        }

    }    
    
    private List<ErrorBean> validaContenido(HSSFWorkbook xlsAvanzado) {
    
        validaTextoIngresado PatterTexto = new validaTextoIngresado();
                
        HSSFSheet sheet = xlsAvanzado.getSheetAt(0);
        Iterator<Row> rowElementos = sheet.iterator();

        int contador = 0;
        
        boolean cuestionarioEncontrado = false;
        
        boolean categoriaEncontrada = false;
        boolean primeraCategoria = false;
        
        String strCuestTemp;
        
        while (rowElementos.hasNext()) {
            Row row = rowElementos.next();
            
            String strTipo = Utilitarios.obtieneDatoCelda(row, 0);
            String strElemento = Utilitarios.obtieneDatoCelda(row, 1);
            
            if(Utilitarios.noEsNuloOVacio(strTipo) && Utilitarios.noEsNuloOVacio(strElemento)){
                if(contador==0){
                    cuestionarioEncontrado = true;
                }
                
                if(strTipo.equals(Constantes.LINEA_CUESTIONARIO)){
                    primeraCategoria = true;
                }
                
                if(primeraCategoria == true){
                
                    if(strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA)){
                        lstErrores.add(new ErrorBean((row.getRowNum()+1), "Esta pregunta cerrada no esta dentro de una categoria", null, (row.getRowNum()+1)+""));
                    }else{
                        primeraCategoria = false;
                    }
                    
                }

                if(strTipo.equals(Constantes.LINEA_CUESTIONARIO) ||
                   strTipo.equals(Constantes.LINEA_CATEGORIA) ||
                   strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA) ||
                   strTipo.equals(Constantes.LINEA_COMENTARIO) ||
                   strTipo.equals(Constantes.LINEA_PREGUNTA_ABIERTA)){
                
                }else{

                    lstErrores.add(new ErrorBean((row.getRowNum()+1), "Tipo desconocido", null, (row.getRowNum()+1)+""));

                }

                String validaDescripcion = PatterTexto.validar(strElemento);

                if(!Utilitarios.esNuloOVacio(validaDescripcion)){
                    lstErrores.add(new ErrorBean((row.getRowNum()+1) , "Descripcion de elemento inválido, asegurate de que no sea vacio o tenga algún digito especial", null, (row.getRowNum()+1)+""));
                } 

                if( strTipo.equals(Constantes.LINEA_CATEGORIA)||
                    strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA)||
                    strTipo.equals(Constantes.LINEA_COMENTARIO)||
                    strTipo.equals(Constantes.LINEA_PREGUNTA_ABIERTA)){

                    if(cuestionarioEncontrado==false){
                        cuestionarioEncontrado = true;
                        lstErrores.add(new ErrorBean((row.getRowNum()+1), "Debes colocar al menos un cuestionario en la primera fila", null, "1"));
                    }
                }
                

                
                
            }else{
            
                if(Utilitarios.noEsNuloOVacio(strTipo) || Utilitarios.noEsNuloOVacio(strElemento)){
                    
                    if(Utilitarios.noEsNuloOVacio(strTipo)){
                        lstErrores.add(new ErrorBean((row.getRowNum()+1), "No se especificó el tipo del elemento", null, (row.getRowNum()+1) + ""));
                    }else{
                        lstErrores.add(new ErrorBean((row.getRowNum()+1), "No se especificó una descripción para el elemento del cuestionario", null, (row.getRowNum()+1) + ""));
                    }
                
                }
            
            }
                
            contador++;
        }
        return lstErrores;
    }

    private void procesaArchivo(HSSFWorkbook xlsAvanzado) {
        
        try{
            
            HSSFSheet sheet = xlsAvanzado.getSheetAt(0);
            Iterator<Row> rowElementos = sheet.iterator();
            String key = null;
            CuestionarioImportado objCuestionarioImportado = null;
            Categorias objCategorias = null;
            Integer i = 0;

            while (rowElementos.hasNext()) {

                Row row = rowElementos.next();

                String strTipo = Utilitarios.obtieneDatoCelda(row, 0);
                String strElemento = Utilitarios.obtieneDatoCelda(row, 1);

                if(Utilitarios.noEsNuloOVacio(strTipo) && Utilitarios.noEsNuloOVacio(strElemento)){

                    if(strTipo.equals(Constantes.LINEA_CUESTIONARIO)){
                        if(objCuestionarioImportado!=null){
                            lstCuestionariosImportados.add(objCuestionarioImportado);
                        }
                        objCuestionarioImportado = new CuestionarioImportado();
                        objCuestionarioImportado.setIdCuestionario(i);
                        objCuestionarioImportado.setStrDescCuestionario(strElemento);
                        objCuestionarioImportado.setLstCategorias(new ArrayList<Categorias>());
                        objCuestionarioImportado.setLstComentarios(new ArrayList<String>());
                        objCuestionarioImportado.setLstPreguntasAbiertas(new ArrayList<String>());
                        i++;

                    }

                    if(strTipo.equals(Constantes.LINEA_COMENTARIO)){
                        objCuestionarioImportado.getLstComentarios().add(strElemento);
                    }

                    if(strTipo.equals(Constantes.LINEA_PREGUNTA_ABIERTA)){
                        objCuestionarioImportado.getLstPreguntasAbiertas().add(strElemento);
                    }

                    if(strTipo.equals(Constantes.LINEA_CATEGORIA)){
                        objCuestionarioImportado.getLstCategorias().add(new Categorias(strElemento,new ArrayList<String>()));
                        objCategorias = objCuestionarioImportado.getLstCategorias().get(objCuestionarioImportado.getLstCategorias().size()-1);
                    }

                    if(strTipo.equals(Constantes.LINEA_PREGUNTA_CERRADA)){
                        objCategorias.getLstPreguntasCerradas().add(strElemento);
                    }

                }

            }

            if(objCuestionarioImportado!=null){
                lstCuestionariosImportados.add(objCuestionarioImportado);
            }
        
        }catch(Exception e){
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Importar cuestionario", "Ocurrio un error al procesar el archivo");
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }
        
    }
    
    
    public void verElementos(Integer idCuestionario){

        this.idCuestionario = idCuestionario;
        
        this.lstElementosDelCuestionarios = new ArrayList<>();

        CuestionarioImportado objElementos = lstCuestionariosImportados.get(idCuestionario);
            
        this.strCuestionario = objElementos.getStrDescCuestionario();
        
        try{
            
            //lstElementosDelCuestionarios.add(new DosDatos("Cuestionario",objElementos.getStrDescCuestionario()));

            Iterator itCategoria = objElementos.getLstCategorias().iterator();

            while(itCategoria.hasNext()){

                Categorias objCat = (Categorias) itCategoria.next();
                lstElementosDelCuestionarios.add(new DosDatos("Categoria",objCat.getStrCategoria()));

                Iterator itPreguntasC = objCat.getLstPreguntasCerradas().iterator();

                while (itPreguntasC.hasNext()) {

                    String strPreguntaC = (String) itPreguntasC.next();
                    lstElementosDelCuestionarios.add(new DosDatos("Pregunta cerrada",strPreguntaC));
                }

            }

            Iterator itComentarios = objElementos.getLstComentarios().iterator();

            while(itComentarios.hasNext()){

                String strComment = (String) itComentarios.next();
                lstElementosDelCuestionarios.add(new DosDatos("Comentario",strComment));

            }

            Iterator itPreguntasA = objElementos.getLstPreguntasAbiertas().iterator();

            while(itPreguntasA.hasNext()){

                String strPreguntaA = (String) itPreguntasA.next();
                lstElementosDelCuestionarios.add(new DosDatos("Pregunta abierta",strPreguntaA));

            }

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Importar cuestionario", "Se seleccionó el cuestionario " + objElementos.getStrDescCuestionario());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }catch(Exception e){
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Importar cuestionario", "Ocurrio un error al ver los elementos del cuestionario " + objElementos.getStrDescCuestionario());
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }
    }

    
    public void guardarImportacion(){
    
        try{
            
            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
            boolean correcto = objCuestionarioDAO.guardaImportacionCuestionario(lstCuestionariosImportados);
            if(correcto){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardar cuestionarios", "Cuestionarios cargados exitosamente");
                FacesContext.getCurrentInstance().addMessage(null, message);        
            }else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Guardar cuestionarios", "Ocurrio un error al guardar los cuestionarios");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }catch(Exception e){
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Guardar cuestionarios", "Ocurrio un error al guardar los cuestionarios");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        init();
        
    }
    
}
