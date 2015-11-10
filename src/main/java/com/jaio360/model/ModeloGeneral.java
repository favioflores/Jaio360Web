package com.jaio360.model;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import java.util.Locale;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

public class ModeloGeneral {

    public static final ReportTemplateBuilder reportTemplate;
    
    //public static final ComponentBuilder<?, ?> reportHeader;
    //public static final ComponentBuilder<?, ?> reportFooter;
    
    public static final StyleBuilder styleTituloPrincipal;
    public static final StyleBuilder styleTituloSecundario;
    public static final StyleBuilder styleSubtituloCab;
    public static final StyleBuilder styleColumnaSubtitulo;
    public static final StyleBuilder styleContenidoDatos;
    public static final StyleBuilder styleNegrita;
    public static final StyleBuilder styleRptaManualMedio;
    public static final StyleBuilder styleRptaManual;
    
    public static final StyleBuilder styleTercerTitulo;
    public static final StyleBuilder styleComentarios;

    static {

        styleTituloPrincipal = stl.style()
                                .setFontSize(14)
                                .setBold(Boolean.TRUE)
                                .setHorizontalAlignment(HorizontalAlignment.LEFT);

        styleTituloSecundario = stl.style()
                                .setFontSize(12)
                                .setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        styleColumnaSubtitulo = stl.style()
                                .setFontSize(10)
                                .setBold(Boolean.TRUE)
                                .setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        styleSubtituloCab = stl.style()
                                .setFontSize(12)
                                .setBold(Boolean.TRUE)
                                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        styleContenidoDatos = stl.style()
                              .setFontSize(8)
                              .setHorizontalAlignment(HorizontalAlignment.LEFT);

        styleTercerTitulo    = stl.style()
                              .setFontSize(8)
                              .setBold(Boolean.TRUE)
                              .setHorizontalAlignment(HorizontalAlignment.LEFT);
        styleComentarios     = stl.style()
                              .setFontSize(8)
                              .setItalic(Boolean.TRUE)
                              .setHorizontalAlignment(HorizontalAlignment.LEFT);
                        
        styleNegrita = stl.style().bold();
        
        styleRptaManualMedio = stl.style()
                              .setFontSize(6)
                              .bold()
                              .setHorizontalAlignment(HorizontalAlignment.CENTER);

        styleRptaManual      = stl.style()
                              .setFontSize(6)
                              .setHorizontalAlignment(HorizontalAlignment.CENTER);

        reportTemplate = template().setLocale(Locale.ENGLISH).setPageMargin(margin(40));
        
/*
        reportHeader = cmp.horizontalList().add(cmp.horizontalList(
                                                    cmp.image(ModeloNormal.class.getResource("images/Logo_grises-01.png")).setFixedDimension(140, 60),
                                                    cmp.verticalList(
                                                        cmp.text("").setStyle(styleHeaderParamTitle).setHorizontalAlignment(HorizontalAlignment.LEFT),
                                                        cmp.text("").setStyle(styleHeaderParamSubTitle)//.setHyperLink(link)
                                                        )
                                                    ).setWidth(150),
                                                cmp.verticalList(
                                                    cmp.horizontalList(
                                                        cmp.text("Fecha ").setStyle(styleHeaderRightBottomParam).setWidth(10),
                                                        cmp.text(": 01/07/2014").setStyle(styleHeaderLeftBottomParam).setWidth(20)
                                                        ),
                                                    cmp.horizontalList(
                                                        cmp.text("Hora ").setStyle(styleHeaderRightTopParam).setWidth(10),
                                                        cmp.text(": 20.21.00").setStyle(styleHeaderLeftTopParam).setWidth(20)
                                                        )
                                                    )
                                                )
                                            .newRow()
                                            .add(cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID)))
                                            .newRow()
                                            .add(cmp.image(ModeloNormal.class.getResource("images/linea.png")).setFixedDimension(330, 20))
                                            .newRow()
                                            .add(cmp.verticalGap(15));
            
        reportFooter = cmp.horizontalList().add(cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID)))
                                            .newRow()
                                            .add(cmp.verticalGap(5))
                                            .newRow()
                                            .add(cmp.horizontalList(   
                                                    cmp.verticalList(
                                                        cmp.text("EVALUACION DE JEFATURAS").setStyle(styleFooterLeftBottomParam),
                                                        cmp.text("PROYECTO EVALUACIONES GERENCIALES MARZO PARA EL AÑO 2014").setStyle(styleFooterLeftTopParam)
                                                        ).setWidth(460),
                                                    cmp.verticalList(
                                                        cmp.pageNumber().setStyle(styleFooterRightBottomParam)
                                                        )
                                                    )
                                                );
*/
    }
}
