package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;

public class ReporteGrupalSumarioCategoriaGeneral implements Serializable  {

    private static final Log log = LogFactory.getLog(ReporteGrupalSumarioCategoriaGeneral.class);
    
    CuestionarioDAO cuestionarioDAO = new CuestionarioDAO();
    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;
    
    public String build(DatosReporte objDatosReporte, Map map) throws IOException {
        
        this.objDatosReporte = objDatosReporte;
        
        String strNombreReporte = objDatosReporte.getStrID() + Constantes.STR_EXTENSION_PDF; 
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                                                     .setEncrypted(Boolean.FALSE);

        try {
            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(generaCabecera(map))
                    .summary(generaConenido())
                    .pageFooter(generaPie(map))
                    .toPdf(pdfExporter);
        } catch (DRException ex) {
            log.error(ex);
            return null;
        }
        return strNombreReporte;
    }

   private MultiPageListBuilder generaConenido(){
        
        List lstDatos  = resultadoDAO.listaGrupalSumarioCategoriaGeneral(objDatosReporte);
        
        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Double> cantidad = col.column("Cantidad", "cantidad", type.doubleType());
        Map<String, Color> seriesColors = new HashMap();
        
        MultiPageListBuilder multiPageList = cmp.multiPageList();
        
        if(!lstDatos.isEmpty()){
        
            Iterator itLstDatos = lstDatos.iterator();

            String keyTit = "";
            boolean primeraVez = true;
            int contCat = 0;
            BigDecimal bdProm;
            
            while(itLstDatos.hasNext()){
                
                seriesColors = new HashMap();
                seriesColors.put("PROM", Utilitarios.convertColorHexToRgb("#"+Utilitarios.generaColorHtml()));
                        
                Object obj[] = (Object[]) itLstDatos.next();
            
                if(!keyTit.equals(obj[2].toString())){
                    if(!primeraVez){
                        multiPageList.add(cmp.verticalGap(10));
                    }
                    multiPageList.add(  cmp.horizontalList(
                                            //cmp.text(contCat +". " + obj[1].toString()).setStyle(ModeloGeneral.styleNegrita).setWidth(400).setHorizontalAlignment(HorizontalAlignment.LEFT)
                                            cmp.text(obj[1].toString()).setStyle(ModeloGeneral.styleNegrita).setWidth(400).setHorizontalAlignment(HorizontalAlignment.LEFT)
                                        )                        
                                    );
                    keyTit = obj[2].toString();
                    primeraVez = false;
                    contCat++;
                }
                bdProm = new BigDecimal("0");
                if(Utilitarios.noEsNuloOVacio(obj[4])){
                    bdProm = new BigDecimal(obj[4].toString()).setScale(2, RoundingMode.FLOOR);
                }
                
                String strDesc;
                if(Utilitarios.noEsNuloOVacio(obj[3])){
                    strDesc = obj[3].toString();
                }else{
                    strDesc = "Sin datos";
                }
                
                multiPageList.add(  cmp.horizontalList(
                                            cht.barChart().setCategory(evaluacion)
                                          .seriesColorsByName(seriesColors)
                                          .series(cht.serie(cantidad).setSeries(relacion))
                                          .setDataSource(createDataSourceBar(bdProm))
                                          .setOrientation(Orientation.HORIZONTAL)
                                          .setLegendPosition(Position.RIGHT)
                                          .setShowLegend(Boolean.FALSE)
                                          .setShowLabels(Boolean.FALSE)
                                          .setShowValues(Boolean.FALSE)
                                          .setShowTickLabels(Boolean.FALSE)
                                          .setHeight(5)/* es util, en relacion de 10 por 1 categoria*/
                                          .setShowTickMarks(Boolean.FALSE)
                                          .setCustomizer(new ReporteGrupalSumarioCategoriaGeneral.ChartCustomizerBar()),
                                        cmp.horizontalList(
                                            cmp.text(bdProm.doubleValue()).setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                        ).setWidth(80),
                                        cmp.horizontalList(
                                            cmp.text(strDesc).setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                        ).setWidth(200)
                                    )
                                );
                
            }
        
        }else{
            multiPageList.add(cmp.text("No existen datos a procesar"));
        }
        
        multiPageList.newPage();
        
        return multiPageList;

    }
    

    private JRDataSource createDataSourceBar(BigDecimal bdDato) {
        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");
        dataSource.add("eva", "PROM", bdDato.doubleValue());
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
                    //,//SALTO DE LINEA
                    //cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                    ,//SALTO DE LINEA
                    cmp.verticalGap(10)
                    , //SALTO DE LINEA
                    cmp.horizontalList( 
                        //cmp.text("Medida").setStyle(styleColumnaSubtitulo).setWidth(350), 
                        cmp.image(medida).setFixedDimension(225, 20),
                        cmp.horizontalGap(50),
                        //cmp.text("Rel").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(140),
                        cmp.text("Prom").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(80),
                        cmp.text("Descripcion").setStyle(ModeloGeneral.styleColumnaSubtitulo).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(220)
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
    
    
    public class ChartCustomizerBar implements DRIChartCustomizer, Serializable {

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {

            BarRenderer barRenderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            //barRenderer.setBaseSeriesVisible(false);//QUITA LAS BARRAS
            //barRenderer.setBaseSeriesVisibleInLegend(false);
            //barRenderer.setDrawBarOutline(false);
            barRenderer.setShadowVisible(false);
            barRenderer.setBarPainter(new CustomBarPainter());
            barRenderer.setItemMargin(0);//QUITA ESPACIOS ENTRE BARRA Y BARRA
            
            
            CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
            domainAxis.setUpperMargin(0);
            domainAxis.setLowerMargin(0);
            domainAxis.setCategoryMargin(0);
            domainAxis.setAxisLineVisible(false);//este es util

            Plot plot = chart.getPlot();
            //plot.setOutlineVisible(false);
            plot.setInsets(new RectangleInsets(0,0,0,0));//este sera util

            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setAxisOffset(new RectangleInsets(0,0,0,0));
            //FAFO6categoryPlot.setRangeGridlinePaint(Color.WHITE);
            categoryPlot.setDomainGridlinesVisible(false);
            //FAFO5categoryPlot.setRangeGridlinesVisible(false);
            //categoryPlot.setBackgroundPaint(Color.white);
            categoryPlot.setOutlineVisible(false);

            ValueAxis valueAsix = categoryPlot.getRangeAxis();
            valueAsix.setAxisLineVisible(false);//este es muy util
            valueAsix.setRange(0, objDatosReporte.getIntMaxRango());
            valueAsix.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//este es muy util

        }
    }
    
}
