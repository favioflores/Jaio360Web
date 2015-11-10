package com.jaio360.model;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ModeloNormal {
    
        public static final StyleBuilder styleHeaderParamTitle;
        public static final StyleBuilder styleHeaderParamSubTitle;
        public static final StyleBuilder styleHeaderRightBottomParam;
        public static final StyleBuilder styleHeaderRightTopParam;
        public static final StyleBuilder styleHeaderLeftBottomParam;
        public static final StyleBuilder styleHeaderLeftTopParam;
        public static final StyleBuilder styleFooterRightTopParam;
        public static final StyleBuilder styleFooterRightBottomParam;
        public static final StyleBuilder styleFooterLeftTopParam;
        public static final StyleBuilder styleFooterLeftBottomParam;
	public static final ReportTemplateBuilder reportTemplate;

	static {

            styleHeaderParamTitle = stl.style().setPadding(4)
                                                .setFontSize(13)
                                                .setForegroundColor(Color.LIGHT_GRAY)
                                                .setVerticalAlignment(VerticalAlignment.BOTTOM);

            styleHeaderParamSubTitle = stl.style().setPadding(4)
                                                    .setFontSize(10)
                                                    .setForegroundColor(Color.LIGHT_GRAY);

            styleHeaderRightBottomParam    = stl.style().setPadding(1)
                                                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                                        .setFontSize(9)
                                                        .setForegroundColor(Color.LIGHT_GRAY)
                                                        //.setBorder(stl.pen1Point())
                                                        .setVerticalAlignment(VerticalAlignment.BOTTOM);

            styleHeaderRightTopParam    = stl.style().setPadding(1)
                                                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                                        .setFontSize(9)
                                                        .setForegroundColor(Color.LIGHT_GRAY)
                                                        //.setBorder(stl.pen1Point())
                                                        .setVerticalAlignment(VerticalAlignment.TOP);;

            styleHeaderLeftBottomParam    = stl.style().setPadding(1)
                                                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                                        .setFontSize(9)
                                                        .setForegroundColor(Color.LIGHT_GRAY)
                                                        //.setBorder(stl.pen1Point())
                                                        .setVerticalAlignment(VerticalAlignment.BOTTOM);

            styleHeaderLeftTopParam        = stl.style().setPadding(1)
                                                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                                        .setFontSize(9)
                                                        .setForegroundColor(Color.LIGHT_GRAY)
                                                        //.setBorder(stl.pen1Point())
                                                        .setVerticalAlignment(VerticalAlignment.TOP);



            styleFooterRightBottomParam        = stl.style().setPadding(1)
                                                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                                    .setFontSize(9)
                                                    .setForegroundColor(Color.LIGHT_GRAY)
                                                    .setVerticalAlignment(VerticalAlignment.BOTTOM);

            styleFooterRightTopParam           = stl.style().setPadding(1)
                                                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                                    .setFontSize(9)
                                                    .setForegroundColor(Color.LIGHT_GRAY)
                                                    .setVerticalAlignment(VerticalAlignment.TOP);

            styleFooterLeftBottomParam         = stl.style().setPadding(1)
                                                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                                    .setFontSize(9)
                                                    .setForegroundColor(Color.LIGHT_GRAY)
                                                    .setItalic(Boolean.TRUE)
                                                    .setVerticalAlignment(VerticalAlignment.BOTTOM);

            styleFooterLeftTopParam            = stl.style().setPadding(1)
                                                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                                    .setFontSize(9)
                                                    .setForegroundColor(Color.LIGHT_GRAY)
                                                    .setBold(Boolean.TRUE)
                                                    .setVerticalAlignment(VerticalAlignment.TOP);


            reportTemplate = template().setLocale(Locale.ENGLISH)
                                        .highlightDetailEvenRows()
                                        .crosstabHighlightEvenRows();
                      
	}


        
}