package com.jaio360.report;

import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloCaratula;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.exception.DRException;

public class ReporteIndividualCaratula implements Serializable {
    
    public String build(DatosReporte objDatosReporte, String strNameFile) throws IOException {

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF; 

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Constantes.STR_INBOX_PRELIMINAR + File.separator + strNombreReporte)
                                                     .setEncrypted(Boolean.FALSE);

        try {
                report().setTemplate(ModeloCaratula.reportTemplate)
                        .summary(creaCaratula(objDatosReporte))
                        .toPdf(pdfExporter);

        } catch (DRException e) {
                e.printStackTrace();
                return null;
        }

        return strNombreReporte;
    }        
    
    private ComponentBuilder<?, ?> creaCaratula(DatosReporte objDatosReporte){
        
        ComponentBuilder<?, ?> caratula = cmp.horizontalList()
                                            .add(cmp.image(ModeloCaratula.class.getResource(File.separator + "images" + File.separator + "favicon.png")).setFixedDimension(150, 70))
                                            .newRow()
                                            .add(cmp.verticalGap(70))
                                            .newRow()
                                            .add(cmp.text(objDatosReporte.getStrNombreEvaluado()).setStyle(ModeloCaratula.tituloReporte1))
                                            .newRow()
                                            //.add(cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloCaratula.tituloReporte2))
                                            .add(cmp.text("").setStyle(ModeloCaratula.tituloReporte2))
                                            .newRow()
                                            .add(cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloCaratula.tituloReporte1))
                                            .newRow()
                                            .add(cmp.verticalGap(70))
                                            .newRow()
                                            .add(cmp.text(Utilitarios.obtieneFechaSistema(Constantes.FORMAT_DATE_LONG)).setStyle(ModeloCaratula.fechaReporte))
                                            .newRow()
                                            .add(cmp.verticalGap(10))
                                            .newRow()
                                            .add(cmp.text(Utilitarios.obtenerProyecto().getStrDescNombre()).setStyle(ModeloCaratula.proyectoTitulo))
                                            .newRow()
                                            .add(cmp.verticalGap(15))
                                            .newRow()
                                            .add(cmp.centerHorizontal(cmp.line().setPen(stl.pen(Float.MIN_VALUE, LineStyle.SOLID)).setFixedWidth(550)))
                                            .newRow()
                                            .add(cmp.verticalGap(30))
                                            .newRow()
                                            /*
                                            .add(cmp.verticalList(cmp.horizontalList(
                                                                    cmp.text("Evaluado  :").setStyle(ModeloCaratula.parametroDatos),
                                                                    cmp.text(objDatosEvaluacion.getStrDescripcionParticipante()).setStyle(ModeloCaratula.descripcionDatos)),
                                                                  cmp.horizontalList(
                                                                    cmp.text("Fecha Emisión  :").setStyle(ModeloCaratula.parametroDatos),
                                                                    cmp.text(objDatosEvaluacion.getStrFechaEmision()).setStyle(ModeloCaratula.descripcionDatos)),
                                                                  cmp.horizontalList(
                                                                    cmp.text("Usuario  :").setStyle(ModeloCaratula.parametroDatos),
                                                                    cmp.text(objDatosEvaluacion.getStrUsuario()).setStyle(ModeloCaratula.descripcionDatos))
                                                                  )
                                                       )
                                            .newRow()
                                            */
                                            .add(cmp.verticalGap(80))
                                            .newRow()
                                            .add(cmp.centerHorizontal(cmp.image("http://jaio360.com/images/Logo-01.jpg"//ModeloNormal.class.getResource("images/linea.png")
                                                            ).setFixedDimension(150,150)));
        return caratula;
    }
}
