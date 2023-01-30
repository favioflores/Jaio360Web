package com.jaio360.report;

import com.jaio360.dao.ComponenteDAO;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.utils.Constantes;
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
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReporteIndividualItemsBajaCalificacion implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteIndividualItemsBajaCalificacion.class);

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFIle) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFIle + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(generaCabecera(map))
                    .summary(generaConenido(intEvaluadoPk))
                    .pageFooter(generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        }
        return strNombreReporte;
    }

    private MultiPageListBuilder generaConenido(Integer intEvaluadoPk) {

        List lstItems = resultadoDAO.listaItemsBajaPromedio(intEvaluadoPk);

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

                if ((bdPromedio.compareTo(bdPunto) == Constantes.FIRST_EQUAL_SECOND || bdPromedio.compareTo(bdPunto) == Constantes.FIRST_GREATER) && i <= 10) {

                    multiPageList.add(cmp.horizontalList(
                            cmp.verticalList(
                                    //cmp.text(i+". " +obj[2].toString()),
                                    cmp.text(obj[2].toString()),
                                    cmp.text("\t" + obj[1].toString()).setStyle(ModeloGeneral.styleNegrita)
                            ).setWidth(400),
                            cmp.text(bdPunto.doubleValue()).setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    ).newRow().add(cmp.verticalGap(5))
                    );
                }

                i++;

            }

        } else {
            multiPageList.add(cmp.text("No existen datos a procesar"));
        }

        multiPageList.newPage();

        return multiPageList;

    }

    private ComponentBuilder<?, ?> generaCabecera(Map map) throws FileNotFoundException {
        //InputStream medida = new FileInputStream(map.get(Constantes.INT_PARAM_GRAF_MEDIDA) + Constantes.STR_EXTENSION_PNG);

        return cmp.verticalList(
                cmp.verticalGap(5),//SALTO DE LINEA

                 cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloPrincipal)
                ) //,//SALTO DE LINEA
                //cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID))
                ,//SALTO DE LINEA
                 cmp.verticalGap(10), //SALTO DE LINEA
                 cmp.horizontalList(
                        //cmp.text("Medida").setStyle(styleColumnaSubtitulo).setWidth(350), 
                        //cmp.image(medida).setFixedDimension(250, 20),
                        cmp.horizontalGap(490),
                        //cmp.text("Rel").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(140),
                        cmp.text("Frec").setStyle(ModeloGeneral.styleColumnaSubtitulo)//.setWidth(120),
                //cmp.text("N").setStyle(ModeloGeneral.styleColumnaSubtitulo).setWidth(65)
                ),
                cmp.verticalGap(5)
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
