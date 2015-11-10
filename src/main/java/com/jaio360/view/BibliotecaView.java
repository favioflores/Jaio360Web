package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.domain.BibliotecaInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Elemento;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

@ManagedBean(name = "bibliotecaView")
@SessionScoped
public class BibliotecaView implements Serializable {
    
    private String metodologia;  
    private String estadoProyecto;
    private List<BibliotecaInfo> filtroElementos;
    
    private int cantidadElementos;
    
    private  Map<String,String> metodologias;
    private  Map<String,String> estados;
    
    private List<BibliotecaInfo> lstElementos;

    public BibliotecaView() {
        metodologia = "";
        estadoProyecto = "";
        metodologias = new HashMap<String, String>();
        estados = new HashMap<String, String>();
        //filtroElementos = new ArrayList<BibliotecaInfo>();
        //lstElementos = new ArrayList<BibliotecaInfo>();
        //cantidadElementos = -1;
    }

    public List<BibliotecaInfo> getLstElementos() {
        return lstElementos;
    }

    public List<BibliotecaInfo> getFiltroElementos() {
        return filtroElementos;
    }

    public void setFiltroElementos(List<BibliotecaInfo> filtroElementos) {
        this.filtroElementos = filtroElementos;
    }

    public void setLstElementos(List<BibliotecaInfo> lstElementos) {
        this.lstElementos = lstElementos;
    }
    
    public int getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(int cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(String estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    public Map<String, String> getMetodologias() {
        return metodologias;
    }

    public void setMetodologias(Map<String, String> metodologias) {
        this.metodologias = metodologias;
    }

    public Map<String, String> getEstados() {
        return estados;
    }

    public void setEstados(Map<String, String> estados) {
        this.estados = estados;
    }
    
    @PostConstruct
    public void init() {

        EHCacheManager objEHCacheManager = new EHCacheManager();

        List lstMetodologias = objEHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_METODOLOGIAS);
        
        List lstEstadosProyecto = objEHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_ESTADOS_PROYECTO);
    
        Iterator itLstMetodologia = lstMetodologias.iterator();
        metodologias = new HashMap<String, String>();
        
        while(itLstMetodologia.hasNext()){
            Elemento objElemento = (Elemento) itLstMetodologia.next();
            metodologias.put(objElemento.getElIdElementoPk().toString(), objElemento.getElTxDescripcion());
        }   
        
        Iterator itLstEstadosProyecto = lstEstadosProyecto.iterator();
        estados = new HashMap<String, String>();
        
        while (itLstEstadosProyecto.hasNext()) {
            Elemento objElemento = (Elemento) itLstEstadosProyecto.next();
            estados.put(objElemento.getElIdElementoPk().toString(), objElemento.getElTxDescripcion());
        }
         
    }
    
    public void buscarBiblioteca(){
    
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        
        UsuarioSesion objUsuarioSesion = new UsuarioSesion();
        
        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();
        
        List lstItemsTemp = objCuestionarioDAO.obtenItemsBiblioteca(objUsuarioInfo.getStrEmail(), metodologia, estadoProyecto);
        
        Iterator itLstItems = lstItemsTemp.iterator();
        
        int i = 0;
        
        List<BibliotecaInfo> lstItems = new ArrayList<BibliotecaInfo>();
        
        EHCacheManager objEHCacheManager = new EHCacheManager();
        
        while (itLstItems.hasNext()) {
            
            Object[] object = (Object[]) itLstItems.next();
            
            BibliotecaInfo objBibliotecaInfo = new BibliotecaInfo();
            
            objBibliotecaInfo.setStrDescMetodologia(objEHCacheManager.obtenerDescripcionElemento((Integer)object[0]));
            objBibliotecaInfo.setStrProyectos(object[2].toString());
            objBibliotecaInfo.setStrFechaProyecto(object[3].toString());
            objBibliotecaInfo.setStrCuestionarios(object[5].toString());
            objBibliotecaInfo.setStrTipoElemento(objEHCacheManager.obtenerDescripcionElemento((Integer)object[7]));
            objBibliotecaInfo.setStrDescElemento(object[8].toString());
            
            if(Utilitarios.noEsNuloOVacio(object[9])){
                objBibliotecaInfo.setStrExtension(object[9].toString());
            }else{
                objBibliotecaInfo.setStrExtension("---");
            }
            //objBibliotecaInfo.setStrExtension(metodologia);
            objBibliotecaInfo.setStrNroElemento(i++ + "");

            lstItems.add(objBibliotecaInfo);
        }
        cantidadElementos = lstItems.size();
        lstElementos = lstItems;
        
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
 
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(cell.getStringCellValue().toUpperCase());
                cell.setCellStyle(style);
            }
        }
    }
}
