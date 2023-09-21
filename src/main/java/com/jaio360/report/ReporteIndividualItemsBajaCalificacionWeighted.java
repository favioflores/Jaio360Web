package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
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
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReporteIndividualItemsBajaCalificacionWeighted implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteIndividualItemsBajaCalificacionWeighted.class);

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intEvaluadoPk, String strNameFIle) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFIle + Constantes.STR_EXTENSION_PDF;
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    .summary(generaContenido(intEvaluadoPk))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            log.error(ex);
        }
        return strNombreReporte;
    }

    private MultiPageListBuilder generaContenido(Integer intEvaluadoPk) {

        List lstItems = resultadoDAO.listaItemsBajaPromedioWeighted(intEvaluadoPk);

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
                                    cmp.text(obj[2].toString()).setStyle(ModeloGeneral.styleTextoRegular),
                                    cmp.text("\t" + obj[1].toString().toUpperCase()).setStyle(ModeloGeneral.styleTextoRegular)
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

}
