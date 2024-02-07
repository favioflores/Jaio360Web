package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.chart.DRChart;
import net.sf.dynamicreports.report.base.chart.plot.DRSpiderPlot;
import net.sf.dynamicreports.report.builder.chart.SpiderChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.ReportStyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jfree.chart.JFreeChart;
import org.apache.log4j.Logger;
import org.jfree.chart.plot.SpiderWebPlot;

public class ReporteIndividualSumarioCategoriaMismoVsOtrosPromRequired implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteIndividualSumarioCategoriaMismoVsOtrosPromRequired.class);

    ComponenteDAO componenteDao = new ComponenteDAO();
    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            TextColumnBuilder<String> categorias = col.column("Categorias", "categoria", type.stringType());

            TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
            TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
            ReportStyleBuilder styleBuilder = stl.style().setBackgroundColor(Color.GREEN);

            quantityColumn.setStyle(styleBuilder);
            TextColumnBuilder<BigDecimal> unitPriceColumn1 = col.column("Unit price1", "unitprice1", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> unitPriceColumn2 = col.column("Unit price2", "unitprice2", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> unitPriceColumn3 = col.column("Unit price3", "unitprice3", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> unitPriceColumn4 = col.column("Unit price4", "unitprice4", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> unitPriceColumn5 = col.column("Unit price5", "unitprice5", type.bigDecimalType());

            FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

            SpiderChartBuilder spider = cht.spiderChart()
                    .setTitle("Prueba")
                    .setTitleFont(boldFont)
                    //.setFixedWidth(350)
                    .setCategory(itemColumn)
                    //.addCustomizer(new ReporteIndividualSumarioCategoriaMismoVsOtrosPromRequired.ChartCustomizer())
                    .series(cht.serie(quantityColumn),
                            cht.serie(unitPriceColumn1),
                            cht.serie(unitPriceColumn2),
                            cht.serie(unitPriceColumn3),
                            cht.serie(unitPriceColumn4),
                            cht.serie(unitPriceColumn5));

            DRChart chart = spider.getChart();
            
            List<DRIChartCustomizer> lstChartCustomizers = chart.getCustomizers();
            
            DRSpiderPlot plot = (DRSpiderPlot) spider.getChart().getPlot();
            plot.setAxisLineColor(Color.ORANGE);
            plot.setWebFilled(true);
            plot.setLabelColor(Color.yellow);

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    //.pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    //.summary(generaContenido(intEvaluadoPk, categorias, objReporteGenerado))
                    .summary(spider)
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .setDataSource(createDataSource2())
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        }
        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk, TextColumnBuilder<String> categorias, ReporteGenerado objReporteGenerado) {

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        List lstItems = resultadoDAO.listaItemsBajaPromedioMismo(intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price1", "unitprice1", type.bigDecimalType());

        FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

        ReportStyleBuilder styleBuilder = stl.style().setBackgroundColor(Color.BLACK);

        quantityColumn.setStyle(styleBuilder);

        SpiderChartBuilder spider = cht.spiderChart()
                .setTitle("Prueba")
                .setTitleFont(boldFont)
                //.setCategory(categorias)
                .setCategory(itemColumn)
                //.addCustomizer(new ReporteIndividualSumarioCategoriaMismoVsOtrosPromRequired.ChartCustomizer())
                .series(cht.serie(quantityColumn), cht.serie(unitPriceColumn));

        /* RELACIONES */
        Map<String, Color> seriesColors = new HashMap();

        Iterator it = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            seriesColors.put(objRelacion.getReTxAbreviatura(), Utilitarios.convertColorHexToRgb(objRelacion.getReColor()));
            TextColumnBuilder<BigDecimal> relacion = col.column(objRelacion.getReTxNombre(), objRelacion.getReTxAbreviatura(), type.bigDecimalType());
            spider.addSerie(cht.serie(relacion));

        }

        multiPageList.add(spider);

        return multiPageList;

    }

    private JRDataSource createDataSource2() {

        DRDataSource dataSource = new DRDataSource("item", "quantity",
                "unitprice1",
                "unitprice2",
                "unitprice3",
                "unitprice4",
                "unitprice5"
        );

        dataSource.add("Tablet", 350, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Laptop1", 300, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Laptop2", 300, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Laptop3", 300, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Laptop4", 300, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Laptop5", 300, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));
        dataSource.add("Smartphone", 450, new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300), new BigDecimal((int) (Math.random() * 400) + 300));

        return dataSource;

    }

    private JRDataSource createDataSource(Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        List<Componente> lstResultadoXCategoria = componenteDao.listaComponenteProyectoTipo(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), objDatosReporte.getIntIdCuestionario(), Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA, null);

        ArrayList<String> columns = new ArrayList<>();
        columns.add("categoria");

        Iterator itMapRelaciones = objDatosReporte.getMapRelaciones().entrySet().iterator();

        while (itMapRelaciones.hasNext()) {
            Map.Entry entry = (Map.Entry) itMapRelaciones.next();
            Relacion objRelacion = (Relacion) entry.getValue();
            columns.add(objRelacion.getReTxAbreviatura());
        }

        DRDataSource dataSource = new DRDataSource(columns.stream().toArray(String[]::new));

        ArrayList<Object> metricasXRelacion;

        boolean blExiste;
        BigDecimal bdFrec;

        for (Componente objComponente : lstResultadoXCategoria) {

            List lstResultadoFinal = this.resultadoDAO.listaReporteSumarioMismo(objComponente, intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

            itMapRelaciones = objDatosReporte.getMapRelaciones().entrySet().iterator();

            metricasXRelacion = new ArrayList<>();

            metricasXRelacion.add(objComponente.getCoTxDescripcion());

            while (itMapRelaciones.hasNext()) {
                Map.Entry entry = (Map.Entry) itMapRelaciones.next();
                Relacion objRelacion = (Relacion) entry.getValue();

                blExiste = false;
                bdFrec = new BigDecimal(0);

                Iterator itLstResultadoss = lstResultadoFinal.iterator();

                while (itLstResultadoss.hasNext()) {

                    Object[] obj = (Object[]) itLstResultadoss.next();

                    if (objRelacion.getReTxAbreviatura().equals(obj[0].toString())) {
                        blExiste = true;
                        bdFrec = new BigDecimal(obj[1].toString());
                        metricasXRelacion.add(bdFrec);
                    }

                }

                if (!blExiste) {
                    metricasXRelacion.add(bdFrec);
                }

            }

            dataSource.add(metricasXRelacion.get(0),
                    metricasXRelacion.get(1),
                    metricasXRelacion.get(2),
                    metricasXRelacion.get(3));

        }

        return dataSource;

    }

}
