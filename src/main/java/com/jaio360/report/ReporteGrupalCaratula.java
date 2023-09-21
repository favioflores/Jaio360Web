package com.jaio360.report;

import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloCaratula;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.ReportStyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;

public class ReporteGrupalCaratula implements Serializable {

    public String build(DatosReporte objDatosReporte, String strNameFile) throws IOException {

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        try {

            report().setTemplate(ModeloCaratula.reportTemplate)
                    .summary(creaCaratula(objDatosReporte))
                    .toPdf(pdfExporter);

        } catch (DRException e) {
            return null;
        }

        return strNombreReporte;
    }

    private ComponentBuilder<?, ?> creaCaratula(DatosReporte objDatosReporte) {

        String strLogo = "https://www.jaio360-app.com/images/logoJaio360.jpg";

        if (Utilitarios.esNuloOVacio(objDatosReporte.getStrURLCliente())) {
            strLogo = objDatosReporte.getStrURLCliente();
        }

        ComponentBuilder<?, ?> caratula
                = cmp.verticalList(
                        cmp.verticalList(
                                cmp.horizontalList(
                                        cmp.horizontalGap(345),
                                        cmp.image(strLogo).setFixedWidth(120)
                                )
                        ),
                        cmp.verticalGap(60),
                        cmp.text("INFORME GRUPAL").setStyle(ModeloCaratula.styleNombreCuestionario),
                        cmp.verticalGap(20),
                        cmp.text(objDatosReporte.getStrNombreProyecto()).setStyle(ModeloCaratula.styleNombreProyecto),
                        cmp.verticalGap(20),
                        cmp.text(objDatosReporte.getStrDescripcionProyecto()).setStyle(ModeloCaratula.styleNombreParticipante),
                        cmp.verticalGap(20),
                        cmp.text(Utilitarios.obtieneFechaSistema(Constantes.FORMAT_DATE_LONG).toUpperCase()).setStyle(ModeloCaratula.styleFechaCaratula),
                        cmp.verticalGap(20),
                        cmp.line().setPen(stl.pen(new Float("3"), LineStyle.SOLID).setLineColor(new Color(255, 192, 16))).setFixedWidth(400),
                        cmp.verticalGap(20),
                        cmp.text("Evaluación: " + objDatosReporte.getStrCuestionario()).setStyle(ModeloCaratula.styleNombreParticipante),
                        cmp.verticalGap(20),
                        cmp.text("").setStyle(ModeloCaratula.styleNombreParticipante),
                        cmp.verticalGap(20),
                        cmp.text("").setStyle(ModeloCaratula.styleNombreParticipante)
                );
        return caratula;
    }

}
