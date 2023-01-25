package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizer;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReporteGrupalNivelParticipacion implements Serializable  {

    private static final Log log = LogFactory.getLog(ReporteGrupalNivelParticipacion.class);
    
    CuestionarioDAO cuestionarioDAO = new CuestionarioDAO();
    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;
    
    public String build(DatosReporte objDatosReporte, Map map, String strNameFile) throws IOException {
        
        this.objDatosReporte = objDatosReporte;
        
        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF; 
        
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                                                     .setEncrypted(Boolean.FALSE);

        ResultadoDAO objResultadoDAO = new ResultadoDAO();
        List lstResultado = objResultadoDAO.listaReporteNivelParticipacion(objDatosReporte.getIntIdCuestionario());
                
        TextColumnBuilder<String> nombreEvaluadoColumn = col.column("Nombre (Evaluado)", "nombres", type.stringType()).setTitleStyle(stl.style().setBold(Boolean.TRUE));
        TextColumnBuilder<String> personasColumn = col.column("Nro. Personas", "personas", type.stringType()).setTitleStyle(stl.style().setBold(Boolean.TRUE));
        TextColumnBuilder<String> respuestasColumn = col.column("Nro. Respuestas", "respuestas", type.stringType()).setTitleStyle(stl.style().setBold(Boolean.TRUE));
        TextColumnBuilder<String> participacionColumn = col.column("Participación", "participacion", type.stringType()).setTitleStyle(stl.style().setBold(Boolean.TRUE));


          try {

             report()
               .setTemplate(ModeloGeneral.reportTemplate)
               .columns(nombreEvaluadoColumn,personasColumn,respuestasColumn,participacionColumn)
               .pageHeader(generaCabecera(map))
               .pageFooter(generaPie(map))
               .setDataSource(createDataSource(lstResultado))
               .toPdf(pdfExporter);

           } catch (DRException e) {
             log.error(e);
           }

        return strNombreReporte;
    }

    
    private JRDataSource createDataSource(List lstResultado) {
    
        DRDataSource dataSource = new DRDataSource("nombres","personas","respuestas", "participacion");

        Iterator itLstResultado = lstResultado.iterator();
        
        Integer total;
        Integer resueltos;
        BigDecimal porcentaje;
        
        while (itLstResultado.hasNext()) {
            
            Object obj[] = (Object[]) itLstResultado.next();
            
            if(Utilitarios.esNuloOVacio(obj[2])) {
                total = 0;
            }else{
                total = new Integer(obj[2].toString());
            }
            
            if(Utilitarios.esNuloOVacio(obj[3])) {
                resueltos = 0;
            }else{
                resueltos = new Integer(obj[3].toString());
            }
            
            porcentaje = new BigDecimal((resueltos.doubleValue()/total.doubleValue())*100);
            
            dataSource.add(obj[1],total.toString(),resueltos.toString(),porcentaje.setScale(2, RoundingMode.HALF_UP)+"%");//porcentaje.multiply(new BigDecimal(100)).setScale(2)+"%");   
        }
        
        return dataSource;

    }


    
    private ComponentBuilder<?, ?> generaCabecera(Map map) throws FileNotFoundException{
        InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);
        
        return  cmp.verticalList(
                    cmp.verticalGap(5)
                    ,//SALTO DE LINEA
                    
                    cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloPrincipal)
                    )
                    ,//SALTO DE LINEA
                    cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                    ,//SALTO DE LINEA
                    cmp.verticalGap(10)
                    , //SALTO DE LINEA
                    cmp.horizontalList( 
                        //cmp.text("Medida").setStyle(styleColumnaSubtitulo).setWidth(350), 
                        //cmp.image(medida).setFixedDimension(225, 20),
                        cmp.horizontalGap(50)//,
                        //cmp.text("Rel").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(140),
                        //cmp.text("Prom").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(80),
                        //cmp.text("Descripcion").setStyle(ModeloGeneral.styleColumnaSubtitulo).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(220)
                    ),
                    cmp.verticalGap(5)
                );
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
    
    private class CustomTableOfContentsCustomizer extends TableOfContentsCustomizer {
        private static final long serialVersionUID = 1L;

        @Override
        protected ComponentBuilder<?, ?> title() {
           VerticalListBuilder verticalList = cmp.verticalList();
           verticalList.add(cmp.line());
           verticalList.add(super.title());
           verticalList.add(cmp.line());
           return verticalList;
        }



        @Override
        protected ComponentBuilder<?, ?> headingComponent(int level) {

           if (level == 0) {
              VerticalListBuilder verticalList = cmp.verticalList();
              verticalList.add(super.headingComponent(level));
              verticalList.add(cmp.line());
              return verticalList;
           }
           else {
              return super.headingComponent(level);
           }
        }
    }

}
