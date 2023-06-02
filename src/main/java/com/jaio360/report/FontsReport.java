/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jaio360.report;

import com.jaio360.utils.Constantes;
import java.io.File;
import javax.xml.transform.Templates;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.exception.DRException;

/**
 *
 * @author favio.flores
 */
public class FontsReport {

    public FontsReport() {

        build();

    }

    private void build() {

        FontBuilder defaultFont = stl.font().setFontName("Arial");
        StyleBuilder plainStyle = stl.style().setFontName("Arial");
        StyleBuilder boldStyle = stl.style(plainStyle)
                .bold();
        StyleBuilder italicStyle = stl.style(plainStyle)
                .italic();
        StyleBuilder boldItalicStyle = stl.style(plainStyle)
                .boldItalic();

        StyleBuilder plainStyleb = stl.style().setFontName("DejaVu Sans");
        StyleBuilder boldStyleb = stl.style(plainStyleb)
                .bold();
        StyleBuilder italicStyleb = stl.style(plainStyleb)
                .italic();
        StyleBuilder boldItalicStyleb = stl.style(plainStyleb)
                .boldItalic();

        try {

            JasperPdfExporterBuilder pdfExporter = export.pdfExporter("C:\\Users\\favio.flores\\Downloads\\Ejemplo.pdf")
                    .setEncrypted(Boolean.FALSE);

            report().setDefaultFont(defaultFont)
                    .title(
                            cmp.text("SansSerif font - plain").setStyle(plainStyle),
                            cmp.text("SansSerif font - bold").setStyle(boldStyle),
                            cmp.text("SansSerif font - italic").setStyle(italicStyle),
                            cmp.text("SansSerif font - bolditalic").setStyle(boldItalicStyle),
                            cmp.text("Monospaced font - plain").setStyle(plainStyleb),
                            cmp.text("Monospaced font - bold").setStyle(boldStyleb),
                            cmp.text("Monospaced font - italic").setStyle(italicStyleb),
                            cmp.text("Monospaced font - bolditalic").setStyle(boldItalicStyleb)
                    )
                    .toPdf(pdfExporter);

        } catch (DRException e) {

            e.printStackTrace();

        }

    }

}
