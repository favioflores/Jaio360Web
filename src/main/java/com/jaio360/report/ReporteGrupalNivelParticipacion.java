package com.jaio360.report;

import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.DatosReporte;
import com.jaio360.model.ModeloGeneral;
import com.jaio360.model.ModeloNormal;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizer;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReporteGrupalNivelParticipacion implements Serializable {

    private static final Log log = LogFactory.getLog(ReporteGrupalNivelParticipacion.class);

    DatosReporte objDatosReporte;

    public String build(DatosReporte objDatosReporte, Map map, String strNameFile) throws IOException {

        this.objDatosReporte = objDatosReporte;

        String strNombreReporte = strNameFile + Constantes.STR_EXTENSION_PDF;

        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(Utilitarios.getPathTempPreliminar() + File.separator + strNombreReporte)
                .setEncrypted(Boolean.FALSE);

        ResultadoDAO objResultadoDAO = new ResultadoDAO();
        List lstResultado = objResultadoDAO.listaReporteNivelParticipacion(objDatosReporte.getIntIdCuestionario());

        TextColumnBuilder<String> nombreEvaluadoColumn = col.column("EVALUADO", "nombres", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas.setHorizontalAlignment(HorizontalAlignment.LEFT));
        TextColumnBuilder<String> personasColumn = col.column("NO. PERSONAS", "personas", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);
        TextColumnBuilder<String> respuestasColumn = col.column("NO. RESPUESTAS", "respuestas", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);
        TextColumnBuilder<String> participacionColumn = col.column("PARTICIPACIÓN", "participacion", type.stringType()).setTitleStyle(ModeloGeneral.styleHeaderColumnas);

        try {
            
            objDatosReporte.setStrCuestionario("");

            report()
                    .setTemplate(ModeloGeneral.reportTemplate)
                    .columns(nombreEvaluadoColumn, personasColumn, respuestasColumn, participacionColumn)
                    .pageHeader(ModeloGeneral.generaCabeceraSinMetricas(map, this.objDatosReporte))
                    .pageFooter(ModeloGeneral.generaPie(map))
                    .setDataSource(createDataSource(lstResultado))
                    .setSummaryStyle(ModeloGeneral.styleContenidoDatos)
                    .toPdf(pdfExporter);

        } catch (DRException e) {
            log.error(e);
        }

        return strNombreReporte;
    }

    private JRDataSource createDataSource(List lstResultado) {

        DRDataSource dataSource = new DRDataSource("nombres", "personas", "respuestas", "participacion");

        Iterator itLstResultado = lstResultado.iterator();

        Integer total;
        Integer resueltos;
        BigDecimal porcentaje;

        while (itLstResultado.hasNext()) {

            Object obj[] = (Object[]) itLstResultado.next();

            if (Utilitarios.esNuloOVacio(obj[2])) {
                total = 0;
            } else {
                total = new Integer(obj[2].toString());
            }

            if (Utilitarios.esNuloOVacio(obj[3])) {
                resueltos = 0;
            } else {
                resueltos = new Integer(obj[3].toString());
            }

            porcentaje = new BigDecimal((resueltos.doubleValue() / total.doubleValue()) * 100);

            dataSource.add(obj[1], total.toString(), resueltos.toString(), porcentaje.setScale(2, RoundingMode.HALF_UP) + "%");//porcentaje.multiply(new BigDecimal(100)).setScale(2)+"%");   
        }

        return dataSource;

    }

}
