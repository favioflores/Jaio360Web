package com.jaio360.model;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

public class ModeloInstrucciones {
    
    public static final StyleBuilder stlTituloReporte;
    public static final StyleBuilder stlSubtituloReporte;
    public static final StyleBuilder stlDescripcion;
    public static final ReportTemplateBuilder reportTemplate;

    static {

        stlTituloReporte = stl.style().setFontSize(18)
                                    .setBold(Boolean.TRUE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.TOP);
        stlSubtituloReporte = stl.style().setFontSize(15)
                                    .setItalic(Boolean.TRUE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.BOTTOM);
        stlDescripcion = stl.style().setFontSize(14)
                                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                    .setPadding(5);

        reportTemplate = ModeloGeneral.reportTemplate;
    }
    
}