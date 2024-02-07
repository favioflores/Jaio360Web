package com.jaio360.model;

import java.awt.Color;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.util.Locale;
import net.sf.dynamicreports.report.builder.MarginBuilder;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

public class ModeloCaratula {

    public static final StyleBuilder tituloReporte1;
    public static final StyleBuilder styleNombreProyecto;
    public static final StyleBuilder styleNombreParticipante;
    public static final StyleBuilder styleFechaCaratula;
    public static final StyleBuilder styleNombreCuestionario;
    public static final StyleBuilder styleSummaryBorder;
    public static final StyleBuilder tituloReporte2;
    public static final StyleBuilder tituloReporte3;
    public static final StyleBuilder fechaReporte;
    public static final StyleBuilder proyectoTitulo;
    public static final ReportTemplateBuilder reportTemplate;
    public static final ReportTemplateBuilder reportTemplateManual;

    static {

        styleSummaryBorder = stl.style()
                .setBorder(stl.pen1Point())
                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        styleNombreProyecto = stl.style().setFontSize(45)
                .setBold(Boolean.TRUE)
                .setForegroundColor(new Color(0, 117, 189))
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(0);

        styleFechaCaratula = stl.style().setFontSize(10)
                .setBold(Boolean.FALSE)
                .setForegroundColor(new Color(0, 117, 189))
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        styleNombreCuestionario = stl.style().setFontSize(25)
                .setBold(Boolean.TRUE)
                .setForegroundColor(new Color(255, 192, 16))
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        styleNombreParticipante = stl.style().setFontSize(15)
                .setBold(Boolean.FALSE)
                .setForegroundColor(new Color(73, 77, 79))
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

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
        fechaReporte = stl.style().setFontSize(15)
                .setBold(Boolean.TRUE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        proyectoTitulo = stl.style().setFontSize(16)
                .setBold(Boolean.FALSE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        //reportTemplate = template().setLocale(Locale.ENGLISH).setPageMargin(margin(20));
        //reportTemplateManual = template().setLocale(Locale.ENGLISH).setPageMargin(margin(30));
        MarginBuilder margenCaratula = margin().setLeft(60)
                                        .setTop(60)
                                        .setBottom(60)
                                        .setRight(60);
        
        reportTemplate = template().setLocale(Locale.ENGLISH).setPageMargin(margenCaratula);
        reportTemplateManual = template().setLocale(Locale.ENGLISH).setPageMargin(margenCaratula);

    }

}
