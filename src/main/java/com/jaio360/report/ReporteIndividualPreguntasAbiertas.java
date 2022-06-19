package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.constant.LineStyle;

import net.sf.dynamicreports.report.exception.DRException;

public class ReporteIndividualPreguntasAbiertas implements Serializable {

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;
    

    public String build(DatosReporte objDatosReporte, Map map, Integer intIdEvaluado) throws IOException {
    
        this.objDatosReporte = objDatosReporte;
        List lstRptaAbiertas = this.resultadoDAO.obtieneListaResultadoPreguntasAbiertas(intIdEvaluado);
        
        String strNombreReporte = objDatosReporte.getStrID() + Constantes.STR_EXTENSION_PDF; 
        objDatosReporte.setStrID(strNombreReporte);
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                                                     .setEncrypted(Boolean.FALSE);

        try {
            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(generaCabecera(map, objDatosReporte))
                    .summary(generaConenido(lstRptaAbiertas))
                    .pageFooter(generaPie(map))
                    .toPdf(pdfExporter);
        } catch (DRException ex) {
            Logger.getLogger(ReporteIndividualPreguntasAbiertas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return strNombreReporte;
    }

    private ComponentBuilder<?, ?> generaCabecera(Map map, DatosReporte objDatosReporte) throws FileNotFoundException{
        //InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);
        
        return  cmp.verticalList(
                    cmp.verticalGap(5)
                    ,//SALTO DE LINEA
                    cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloGeneral.styleTituloPrincipal).setStyle(ModeloGeneral.styleNegrita)
                    )
                    ,//SALTO DE LINEA
                    cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloSecundario)
                    )
                    ,//SALTO DE LINEA
                    cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                    ,//SALTO DE LINEA
                    cmp.verticalGap(20)
                );
    }
    
    
    
    
    private ComponentBuilder<?, ?> generaConenido(List lstRptaAbiertas){
    
        MultiPageListBuilder multiPageList = cmp.multiPageList();
        
        if(lstRptaAbiertas.isEmpty()){
            multiPageList.add(cmp.horizontalList(cmp.text("No hay respuestas para este cuestionario")));
        }else{
            
            Iterator itLstRptaAbiertas = lstRptaAbiertas.iterator();
            int i = 1;
            
            String strPreguntaTemp = "";
            
            while(itLstRptaAbiertas.hasNext()){
                
                Object obj[] = (Object[]) itLstRptaAbiertas.next();
                /*
                if(Utilitarios.esNuloOVacio(strPreguntaTemp)){
                    //multiPageList.add(cmp.horizontalList(cmp.text(i + ". " + obj[0].toString()).setStyle(ModeloGeneral.styleNegrita)));
                    multiPageList.add(cmp.horizontalList().newRow(10));
                    multiPageList.add(cmp.horizontalList(cmp.text(obj[0].toString()).setStyle(ModeloGeneral.styleNegrita)));
                    i++;
                }else */
                if(!strPreguntaTemp.equals(obj[0].toString().trim())){
                    multiPageList.add(cmp.horizontalList().newRow(10));
                    //multiPageList.add(cmp.horizontalList(cmp.text(i + ". " + obj[0].toString()).setStyle(ModeloGeneral.styleNegrita)));
                    multiPageList.add(cmp.horizontalList(cmp.text(obj[0].toString().trim()).setStyle(ModeloGeneral.styleNegrita)));
                    i++;
                }
                
                strPreguntaTemp = obj[0].toString().trim();
                
                multiPageList.add(cmp.horizontalList().newRow(10));
                if(Utilitarios.esNuloOVacio(obj[1])){
                    multiPageList.add(cmp.horizontalList(cmp.text("\t- " + "Sin comentarios.")));
                }else{
                    multiPageList.add(cmp.horizontalList(cmp.text("\t- " + obj[1].toString().trim())));
                }
            
            }
            
        }

        multiPageList.newPage();
        
        return multiPageList;
    
    }

    private ComponentBuilder<?, ?> generaPie(Map map) throws FileNotFoundException{
        
        return  cmp.horizontalList().add(cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID)))
                                            .newRow()
                                            .add(cmp.verticalGap(5))
                                            .newRow()
                                            .add(cmp.horizontalList(   
                                                    cmp.verticalList(
                                                        cmp.text(objDatosReporte.getStrNombreEvaluado()).setStyle(ModeloNormal.styleFooterLeftBottomParam),
                                                        cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloNormal.styleFooterLeftTopParam)
                                                        ).setWidth(400),
                                                    cmp.verticalList(
                                                        //cmp.pageNumber().setStyle(ModeloNormal.styleFooterRightBottomParam)
                                                        )
                                                    )
                                                );
    }
    
}
