package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ResultadoInfo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
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

public class ReporteIndividualSumarioCategoriaMismoVsOtrosProm implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteIndividualSumarioCategoriaMismoVsOtrosProm.class);
    
    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

        try {

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(generaCabecera(map, medida))
                    .summary(generaConenido(intEvaluadoPk))
                    .pageFooter(generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        } finally {
            medida.close();
        }
        
        return strNombreReporte;
    }

    private MultiPageListBuilder generaConenido(Integer intEvaluadoPk) {

        TextColumnBuilder<String> evaluacion = col.column("Evaluacion", "evaluacion", type.stringType());
        TextColumnBuilder<String> relacion = col.column("Relacion", "relacion", type.stringType());
        TextColumnBuilder<Double> cantidad = col.column("Cantidad", "cantidad", type.doubleType());

        /* CREA BARRAS DE COLORES */
        Map<String, Color> seriesColors = new HashMap();

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();

            Relacion objRelacion = (Relacion) entry.getValue();
            if (objRelacion.getReTxAbreviatura().equals("AUTO") || objRelacion.getReTxAbreviatura().equals("PROM")) {
                seriesColors.put(objRelacion.getReTxAbreviatura(), Utilitarios.convertColorHexToRgb("#" + objRelacion.getReColor()));
            }
        }

        List<Componente> lstComponente = componenteDao.listaComponenteProyectoTipoOrdenado(Utilitarios.obtenerProyecto().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null, intEvaluadoPk);
        //List lstComentarios = this.resultadoDAO.obtieneListaResultadoComentarios(intEvaluadoPk);

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        int i = 1;

        //VerticalListBuilder vComentarios = cmp.verticalList();
        for (Componente objComponente : lstComponente) {
            /*
            Iterator itComentarios = lstComentarios.iterator();
            
            vComentarios = cmp.verticalList();
            
            vComentarios.add(cmp.text("Comentarios").setStyle(ModeloGeneral.styleTercerTitulo));
                    
            boolean commentsEmpty = true;
            
            while(itComentarios.hasNext()){
                Object[] obj = (Object[]) itComentarios.next();
                
                if(obj[2]!=null){
                    if(obj[2].toString().equals(objComponente.getCoIdComponentePk().toString())){
                        vComentarios.add(cmp.text(" - " + obj[1].toString()).setStyle(ModeloGeneral.styleComentarios));
                        commentsEmpty = false;
                    }
                }
            }
            
            if(commentsEmpty){
                vComentarios.add(cmp.text(" - No tiene comentarios").setStyle(ModeloGeneral.styleComentarios));
            }
            
            vComentarios.add(cmp.verticalGap(15));
             */
            multiPageList.add(cmp.horizontalList(cmp.verticalList(//cmp.text(i+". " +objComponente.getCoTxDescripcion()),
                                    cmp.text(objComponente.getCoTxDescripcion()),
                                    cmp.horizontalList(cht.barChart().setCategory(evaluacion)
                                                    .seriesColorsByName(seriesColors)
                                                    .series(cht.serie(cantidad).setSeries(relacion))
                                                    .setDataSource(createDataSourceBar(objComponente, intEvaluadoPk))
                                                    .setOrientation(Orientation.HORIZONTAL)
                                                    .setLegendPosition(Position.RIGHT)
                                                    .setShowLegend(Boolean.FALSE)
                                                    .setShowLabels(Boolean.FALSE)
                                                    .setShowValues(Boolean.FALSE)
                                                    .setShowTickLabels(Boolean.FALSE)
                                                    .setHeight(10)/* es util, en relacion de 10 por 1 categoria*/
                                                    .setShowTickMarks(Boolean.FALSE)
                                                    .setCustomizer(new ReporteIndividualSumarioCategoriaMismoVsOtrosProm.ChartCustomizerBar()),
                                            crearDatosDelGrafico(objComponente, intEvaluadoPk)
                                    ))), cmp.verticalGap(15));

            //multiPageList.add(vComentarios);
            i++;

        }

        multiPageList.newPage();

        return multiPageList;

    }

    private ComponentBuilder<?, ?> crearDatosDelGrafico(Componente objComponente, Integer intEvaluadoPk) {

        VerticalListBuilder datos = cmp.verticalList();

        List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismo(objComponente, intEvaluadoPk);

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        BigDecimal bdFrec;
        boolean blExiste;

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            blExiste = false;
            bdFrec = new BigDecimal(0);

            if (objRelacion.getReTxAbreviatura().equals("PROM") || objRelacion.getReTxAbreviatura().equals("AUTO")) {

                Iterator itLstResultadoss = lstResultadoFinal.iterator();

                while (itLstResultadoss.hasNext()) {

                    Object[] obj = (Object[]) itLstResultadoss.next();

                    if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                        blExiste = true;
                        bdFrec = new BigDecimal(obj[1].toString());
                        datos.add(cmp.horizontalList(cmp.text("").setWidth(25),
                                cmp.text("").setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.LEFT)).setWidth(25),
                                cmp.text(obj[0].toString()).setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(60),
                                cmp.text(Utilitarios.truncateTheDecimal(bdFrec, 2)).setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(60),
                                cmp.text(obj[2].toString()).setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(35),
                                cmp.text("").setWidth(25)));
                    }
                }

                if (!blExiste) {
                    datos.add(cmp.horizontalList(cmp.text("").setWidth(25),
                            cmp.text("").setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.LEFT)).setWidth(25),
                            cmp.text(objRelacion.getReTxAbreviatura()).setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(60),
                            cmp.text(Utilitarios.truncateTheDecimal(bdFrec, 2)).setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(60),
                            cmp.text("0").setStyle(ModeloGeneral.styleContenidoDatos.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE)).setWidth(35),
                            cmp.text("").setWidth(25)));
                }
            }
        }

        return datos;
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
            plot.setInsets(new RectangleInsets(0, 0, 0, 0));//este sera util

            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
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

    private JRDataSource createDataSourceBar(Componente objComponente, Integer intEvaluadoPk) {

        DRDataSource dataSource = new DRDataSource("evaluacion", "relacion", "cantidad");

        List<ResultadoInfo> lstResultadoInfo = new ArrayList<>();

        List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismo(objComponente, intEvaluadoPk);

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        boolean blExiste;

        while (it.hasNext()) {

            blExiste = false;

            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();

            if (objRelacion.getReTxAbreviatura().equals("PROM") || objRelacion.getReTxAbreviatura().equals("AUTO")) {

                Iterator itLstResultadoss = lstResultadoFinal.iterator();

                while (itLstResultadoss.hasNext()) {

                    Object[] obj = (Object[]) itLstResultadoss.next();

                    if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                        ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                        objResultadoInfoTmp.setDeNomRelacion(obj[0].toString());
                        objResultadoInfoTmp.setDeNuOrden(new BigDecimal(obj[1].toString()));
                        lstResultadoInfo.add(objResultadoInfoTmp);
                        blExiste = true;
                    }

                }

                if (!blExiste) {
                    ResultadoInfo objResultadoInfoTmp = new ResultadoInfo();
                    objResultadoInfoTmp.setDeNomRelacion(objRelacion.getReTxAbreviatura());
                    objResultadoInfoTmp.setDeNuOrden(new BigDecimal(0));
                    lstResultadoInfo.add(objResultadoInfoTmp);
                }

            }
        }

        for (ResultadoInfo objResultadoInfo : lstResultadoInfo) {
            dataSource.add("eva", objResultadoInfo.getDeNomRelacion(), objResultadoInfo.getDeNuOrden().doubleValue());
        }

        return dataSource;

    }

    private ComponentBuilder<?, ?> generaCabecera(Map map, InputStream medida) throws FileNotFoundException {

        return cmp.verticalList(
                cmp.verticalGap(5),//SALTO DE LINEA

                 cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloPrincipal)
                ) //,//SALTO DE LINEA
                //cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                ,//SALTO DE LINEA
                 cmp.verticalGap(20),//SALTO DE LINEA
                 cmp.horizontalList(
                        //cmp.text("Medida").setStyle(styleColumnaSubtitulo).setWidth(350), 
                        cmp.image(medida).setFixedDimension(250, 20),
                        cmp.horizontalGap(100),
                        cmp.text("Rel").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(140),
                        cmp.text("Frec").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(120),
                        cmp.text("N").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(65)
                )
        );
    }

    private ComponentBuilder<?, ?> generaPie(Map map) throws FileNotFoundException {

        return cmp.horizontalList().add(cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID)))
                .newRow()
                .add(cmp.verticalGap(5))
                .newRow()
                .add(cmp.horizontalList(
                        cmp.verticalList(
                                cmp.text(objDatosReporte.getStrNombreEvaluado()).setStyle(ModeloNormal.styleFooterLeftBottomParam),
                                cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloNormal.styleFooterLeftTopParam)
                        ).setWidth(400),
                        cmp.verticalList( //cmp.pageNumber().setStyle(ModeloNormal.styleFooterRightBottomParam)
                                )
                )
                );
    }

}
