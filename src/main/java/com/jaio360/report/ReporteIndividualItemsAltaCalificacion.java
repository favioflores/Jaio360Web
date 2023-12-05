package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.log4j.Logger;

public class ReporteIndividualItemsAltaCalificacion implements Serializable {

    private static final Logger log = Logger.getLogger(ReporteIndividualItemsAltaCalificacion.class);

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    .summary(generaContenido(intEvaluadoPk, objReporteGenerado))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        }

        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) {

        List lstItems = resultadoDAO.listaItemsAltoPromedio(intEvaluadoPk, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        if (!lstItems.isEmpty()) {

            Iterator itLstItems = lstItems.iterator();

            /* OBTIENE PROMEDIO */
            BigDecimal bdPromedio = new BigDecimal(BigInteger.ZERO);

            while (itLstItems.hasNext()) {

                Object obj[] = (Object[]) itLstItems.next();
                bdPromedio = bdPromedio.add(new BigDecimal(obj[3].toString()));

            }

            bdPromedio = bdPromedio.divide(new BigDecimal(lstItems.size()), 2, RoundingMode.FLOOR);

            /* ESCRIBE ITEMS */
            itLstItems = lstItems.iterator();

            int i = 1;

            while (itLstItems.hasNext()) {

                Object obj[] = (Object[]) itLstItems.next();
                BigDecimal bdPunto = new BigDecimal(obj[3].toString()).setScale(2, RoundingMode.FLOOR);

                if ((bdPromedio.compareTo(bdPunto) == Constantes.FIRST_EQUAL_SECOND || bdPromedio.compareTo(bdPunto) == Constantes.SECOND_GREATER) && i <= 10) {

                    multiPageList.add(cmp.horizontalList(
                            cmp.verticalList(
                                    //cmp.text(i+". " +obj[2].toString()),
                                    cmp.text(obj[2].toString()).setStyle(ModeloGeneral.styleTextoRegular),
                                    cmp.text("\t" + obj[1].toString()).setStyle(ModeloGeneral.styleTextoRegular)
                            ).setWidth(360),
                            cmp.text(Utilitarios.truncateTheDecimal(bdPunto, 2)).setStyle(ModeloGeneral.styleContenidoDatos).setWidth(120)
                    ).newRow().add(cmp.verticalGap(10))
                    );
                }

                i++;

            }

        } else {
            multiPageList.add(cmp.text("No existen datos a procesar").setStyle(ModeloGeneral.styleTextoRegular));
        }

        multiPageList.newPage();

        return multiPageList;

    }

    private ComponentBuilder<?, ?> generaCabecera(Map map) throws FileNotFoundException {
        //InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

        return cmp.verticalList(
                cmp.verticalGap(5),//SALTO DE LINEA

                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloReporte)
                ) //,//SALTO DE LINEA
                //cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                ,//SALTO DE LINEA
                 cmp.verticalGap(10), //SALTO DE LINEA
                cmp.horizontalList(
                        cmp.horizontalGap(290),
                        cmp.text("FREC").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(120)
                ),
                cmp.verticalGap(5)
        );
    }

    private ComponentBuilder<?, ?> generaPie(Map map) throws FileNotFoundException {

        return cmp.horizontalList().add(cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID).setLineColor(ModeloGeneral.colorJAIOYellow)));
        /*
                .newRow()
                .add(cmp.verticalGap(5))
                .newRow()
                .add(cmp.horizontalList(
                        cmp.verticalList(
                                cmp.text(objDatosReporte.getStrNombreEvaluado()).setStyle(ModeloNormal.styleFooterLeftBottomParam),
                                cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloNormal.styleFooterLeftTopParam)
                        ).setWidth(400),
                        cmp.verticalList(
                                )
                )
                );*/
    }

}
