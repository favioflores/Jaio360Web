package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
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

import net.sf.dynamicreports.report.exception.DRException;

public class ReporteIndividualPreguntasAbiertas implements Serializable {

    ResultadoDAO resultadoDAO = new ResultadoDAO();
    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, Integer intIdEvaluado, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        this.objDatosReporte = objDatosReporte;
        List lstRptaAbiertas = this.resultadoDAO.obtieneListaResultadoPreguntasAbiertas(intIdEvaluado, objReporteGenerado.getProyectoInfo().getIntIdProyecto());

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;
        objDatosReporte.setStrID(strNombreReporte);
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {
            report().setTemplate(ModeloGeneral.reportTemplate)
                    .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, objDatosReporte))
                    .summary(generaContenido(lstRptaAbiertas))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .toPdf(pdfExporter);
        } catch (DRException ex) {
            Logger.getLogger(ReporteIndividualPreguntasAbiertas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return strNombreReporte;
    }

    private ComponentBuilder<?, ?> generaContenido(List lstRptaAbiertas) {

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        if (lstRptaAbiertas.isEmpty()) {
            multiPageList.add(cmp.horizontalList(cmp.text("No hay respuestas para este cuestionario").setStyle(ModeloGeneral.styleTextoRegular)));
        } else {

            Iterator itLstRptaAbiertas = lstRptaAbiertas.iterator();
            int i = 1;

            String strPreguntaTemp = "";

            while (itLstRptaAbiertas.hasNext()) {

                Object obj[] = (Object[]) itLstRptaAbiertas.next();

                if (!strPreguntaTemp.equals(obj[0].toString().trim())) {
                    multiPageList.add(cmp.horizontalList().newRow(10));
                    multiPageList.add(cmp.horizontalList(cmp.text("Pregunta: " + obj[0].toString().trim()).setStyle(ModeloGeneral.styleTextoPreguntaRegular)));
                    i++;
                }

                strPreguntaTemp = obj[0].toString().trim();

                multiPageList.add(cmp.horizontalList().newRow(10));
                if (Utilitarios.esNuloOVacio(obj[1])) {
                    multiPageList.add(cmp.horizontalList(
                            cmp.horizontalGap(20),
                            cmp.text("Comentario: Ninguno.").setStyle(ModeloGeneral.styleComentarios)
                    )
                    );
                } else {
                    multiPageList.add(cmp.horizontalList(
                            cmp.horizontalGap(20),
                            cmp.text("Comentario: " + obj[1].toString().trim()).setStyle(ModeloGeneral.styleComentarios)
                    )
                    );
                }

            }

        }

        multiPageList.newPage();

        return multiPageList;

    }

}
