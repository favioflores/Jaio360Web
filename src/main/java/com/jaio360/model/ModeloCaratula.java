package com.jaio360.model;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.util.Locale;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

public class ModeloCaratula {
    
    public static final StyleBuilder tituloReporte1;
    public static final StyleBuilder tituloReporte2;
    public static final StyleBuilder tituloReporte3;
    public static final StyleBuilder fechaReporte;
    public static final StyleBuilder proyectoTitulo;
    public static final ReportTemplateBuilder reportTemplate;
    public static final ReportTemplateBuilder reportTemplateManual;

    static {
        
        tituloReporte1 = stl.style().setFontSize(24)
                                    .setBold(Boolean.TRUE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
        tituloReporte2 = stl.style().setFontSize(22)
                                    .setBold(Boolean.TRUE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
        tituloReporte3 = stl.style().setFontSize(20)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
        fechaReporte = stl.style().setFontSize(18)
                                    .setBold(Boolean.TRUE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
        proyectoTitulo = stl.style().setFontSize(16)
                                    .setBold(Boolean.FALSE)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

        reportTemplate = template().setLocale(Locale.ENGLISH).setPageMargin(margin(20));
        reportTemplateManual = template().setLocale(Locale.ENGLISH).setPageMargin(margin(30));

    }

}