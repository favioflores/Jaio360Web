package com.jaio360.report;

import com.jaio360.dao.ParametroDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloCaratula;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;

public class ReporteIndividualCaratula extends BaseView implements Serializable {

    public String build(DatosReporte objDatosReporte, String strNameFile, ReporteGenerado objReporteGenerado) throws IOException {

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {
            //ReportStyleBuilder background = stl.style().setBackgroundColor(new Color(0, 117, 189));
            //ReportStyleBuilder background = stl.style().setBackgroundColor(Color.white);

            report().setTemplate(ModeloCaratula.reportTemplate)
                    .summary(creaCaratula(objDatosReporte, objReporteGenerado))
                    .toPdf(pdfExporter);

        } catch (DRException ex) {
            Logger.getLogger(ReporteIndividualCaratula.class.getName()).log(Level.SEVERE, null, ex);
        }

        return strNombreReporte;
    }

    private ComponentBuilder<?, ?> creaCaratula(DatosReporte objDatosReporte, ReporteGenerado objReporteGenerado) {

        String strLogo = "https://www.jaio360-app.com/images/logoJaio360.jpg";

        if (Utilitarios.esNuloOVacio(objDatosReporte.getStrURLCliente())) {
            strLogo = objDatosReporte.getStrURLCliente();
        }

        MultiPageListBuilder multiPageList = cmp.multiPageList();

        multiPageList.add(cmp.verticalList(
                cmp.verticalList(
                        cmp.horizontalList(
                                cmp.horizontalGap(345),
                                cmp.image(strLogo).setFixedWidth(120)
                        )
                ),
                cmp.verticalGap(60),
                cmp.text("INFORME EJECUTIVO").setStyle(ModeloCaratula.styleNombreCuestionario),
                cmp.verticalGap(20),
                cmp.text(objDatosReporte.getStrNombreProyecto()).setStyle(ModeloCaratula.styleNombreProyecto),
                cmp.verticalGap(20),
                cmp.text(objDatosReporte.getStrDescripcionProyecto()).setStyle(ModeloCaratula.styleNombreParticipante),
                cmp.verticalGap(20),
                cmp.text(Utilitarios.obtieneFechaSistema(Constantes.FORMAT_DATE_LONG).toUpperCase()).setStyle(ModeloCaratula.styleFechaCaratula),
                cmp.verticalGap(20),
                cmp.line().setPen(stl.pen(new Float("3"), LineStyle.SOLID).setLineColor(new Color(255, 192, 16))).setFixedWidth(400),
                cmp.verticalGap(20),
                cmp.text("Participante: " + objDatosReporte.getStrNombreEvaluado()).setStyle(ModeloCaratula.styleNombreParticipante),
                cmp.verticalGap(20),
                cmp.text("Correo electrónico: " + objDatosReporte.getStrEmailEvaluado()).setStyle(ModeloCaratula.styleNombreParticipante),
                cmp.verticalGap(20),
                cmp.text("Evaluación: " + objDatosReporte.getStrCuestionario()).setStyle(ModeloCaratula.styleNombreParticipante)
        ));

        if (objDatosReporte.getBlWeighted()) {
            
            ResourceBundle rb = ResourceBundle.getBundle("etiquetas");
            
            ParametroDAO objParametroDAO = new ParametroDAO();

            Parametro objParametro = objParametroDAO.obtenParametro(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES);

            multiPageList.newPage();

            RelacionDAO objRelacionDAO = new RelacionDAO();

            List<Relacion> lstRelaciones = objRelacionDAO.obtenListaRelacionPorEvaluado(objReporteGenerado.getProyectoInfo().getIntIdProyecto(), objDatosReporte.getIntEvaluado());

            multiPageList.add(cmp.verticalList(
                    cmp.text(rb.getString("instr.1"))
            ));
            
            multiPageList.add(cmp.verticalList(
                    cmp.text("")
            ));

            for (Relacion objRelacion : lstRelaciones) {

                multiPageList.add(cmp.verticalList(
                        cmp.text("- " + objRelacion.getReTxAbreviatura() + " (" + objRelacion.getReTxNombre() + ") : " + Utilitarios.truncateTheDecimal(objRelacion.getReDePonderacion(), 2) + "%")
                ));

            }

        }
        return multiPageList;
    }
}
