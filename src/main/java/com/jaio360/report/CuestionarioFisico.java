package com.jaio360.report;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.EjecutarEvaluacionDAO;
import com.jaio360.dao.MetricaDAO;
import com.jaio360.domain.Evaluado;
import com.jaio360.model.ModeloCaratula;
import com.jaio360.model.ModeloGeneral;
import static com.jaio360.model.ModeloGeneral.styleContenidoDatos;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.Metrica;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import static com.jaio360.view.EjecutarEvaluacionView.TIPO_COMPONENTE_ABIERTA;
import static com.jaio360.view.EjecutarEvaluacionView.TIPO_COMPONENTE_CERRADA;
import static com.jaio360.view.EjecutarEvaluacionView.TIPO_COMPONENTE_COMENTARIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

public class CuestionarioFisico implements Serializable {

    private static Logger log = Logger.getLogger(CuestionarioFisico.class);

    public String build(Evaluado objEvaluado) throws IOException {

        String strNombreReporte = Utilitarios.reemplazar(objEvaluado.getPaTxDescripcion(), " ", "_") + "_"
                + Utilitarios.formatearFecha(Utilitarios.getCurrentDate(), Constantes.DDMMYYYYHH24MISS)
                + "_" + Constantes.STR_EXTENSION_PDF;

        File directory = new File(Utilitarios.getPathTempPreliminar());

        if (!directory.exists()) {
            directory.mkdir();
        }

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
            Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objEvaluado.getPaIdParticipantePk(), Utilitarios.obtenerProyecto().getIntIdProyecto());

            if (objCuestionario != null) {
                report().setTemplate(ModeloCaratula.reportTemplateManual)
                        .setSummaryWithPageHeaderAndFooter(Boolean.TRUE)
                        .pageHeader(creaCabecera(objCuestionario.getCuTxDescripcion(), objEvaluado.getPaTxDescripcion()))
                        .summary(creaContenido(objEvaluado))
                        .toPdf(pdfExporter);
            }

        } catch (DRException e) {
            log.error(e);
            return null;
        }

        return strNombreReporte;
    }

    private MultiPageListBuilder creaContenido(Evaluado objEvaluado) {

        EjecutarEvaluacionDAO objEjecutarEvaluacionDAO = new EjecutarEvaluacionDAO();
        MetricaDAO objMetricaDAO = new MetricaDAO();

        List<Componente> lstCompCerrada = objEjecutarEvaluacionDAO.obtenerComponenteTipoXEmail(Utilitarios.obtenerProyecto().getIntIdProyecto(), objEvaluado.getPaTxCorreo(), TIPO_COMPONENTE_CERRADA);
        Metrica objMetrica = objMetricaDAO.obtenMetricaProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        List<DetalleMetrica> lstDetalleMetrica = objEjecutarEvaluacionDAO.obtenerDetalleMetrica(Utilitarios.obtenerProyecto().getIntIdProyecto());
        List<Componente> lstCompComentario = objEjecutarEvaluacionDAO.obtenerComponenteTipoXEmail(Utilitarios.obtenerProyecto().getIntIdProyecto(), objEvaluado.getPaTxCorreo(), TIPO_COMPONENTE_COMENTARIO);
        List<Componente> lstCompAbierta = objEjecutarEvaluacionDAO.obtenerComponenteTipoXEmail(Utilitarios.obtenerProyecto().getIntIdProyecto(), objEvaluado.getPaTxCorreo(), TIPO_COMPONENTE_ABIERTA);

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        /**
         * *******************
         */
        /* PREGUNTAS CERRADAS */
        /**
         * *******************
         */
        if (!lstCompCerrada.isEmpty()) {

            HorizontalListBuilder horResp = cmp.horizontalList();
            horResp.add(cmp.verticalGap(5));
            for (int i = 0; i < objMetrica.getMeNuRango(); i++) {
                horResp.add(cmp.text("(" + (i + 1) + ")").setStyle(ModeloGeneral.styleRptaManualMedio));
            }

            horResp.add(cmp.verticalGap(5));
            horResp.newRow();

            for (DetalleMetrica objDetalleMetrica : lstDetalleMetrica) {
                horResp.add(cmp.text(objDetalleMetrica.getDeTxValor()).setStyle(ModeloGeneral.styleRptaManual).setStyle(ModeloGeneral.styleNegrita));
            }

            int i = 1;

            for (Componente objComponente : lstCompCerrada) {

                VerticalListBuilder objVL = cmp.verticalList();

                objVL.add(cmp.text(objComponente.getCoTxDescripcion()), horResp);

                for (Componente objComponenteC : lstCompComentario) {
                    objVL.add(cmp.verticalGap(5));
                    objVL.add(cmp.text(objComponenteC.getCoTxDescripcion()).setStyle(ModeloGeneral.styleContenidoDatos));
                    objVL.add(cmp.text(Constantes.UNDERLINE_COMMENT));
                    objVL.add(cmp.verticalGap(5));
                }

                multiPageList.add(objVL);

                i++;

            }

        }

        multiPageList.newPage();

        /**
         * *******************
         */
        /* PREGUNTAS ABIERTAS */
        /**
         * *******************
         */
        int i = 0;

        VerticalListBuilder objVL = cmp.verticalList();

        objVL.add(cmp.text("PREGUNTAS ABIERTAS").setStyle(ModeloGeneral.styleTercerTitulo));
        objVL.add(cmp.verticalGap(5));

        for (Componente objComponente : lstCompAbierta) {
            objVL.add(cmp.text(objComponente.getCoTxDescripcion()));
            objVL.add(cmp.text(Constantes.UNDERLINE_COMMENT));
            i++;
        }

        multiPageList.add(objVL);

        return multiPageList;

    }

    private ComponentBuilder<?, ?> creaCabecera(String strCuestionario, String strEvaluado) {

        return cmp.verticalList(
                cmp.verticalGap(5),
                 cmp.text(strCuestionario).setStyle(ModeloGeneral.styleTituloPrincipal),
                 cmp.text(strEvaluado).setStyle(ModeloGeneral.styleTituloSecundario),
                 cmp.verticalGap(5),
                 cmp.line().setPen(stl.pen(new Float("0.1"), LineStyle.SOLID)),
                 cmp.verticalGap(15)
        );
    }
}
