package com.jaio360.model;

import com.jaio360.domain.DatosReporte;
import static com.jaio360.model.ModeloCaratula.reportTemplate;
import com.jaio360.orm.Componente;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import java.util.Locale;
import java.util.Map;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

public class ModeloGeneral implements Serializable {

    public static final ReportTemplateBuilder reportTemplate;

    //public static final ComponentBuilder<?, ?> reportHeader;
    //public static final ComponentBuilder<?, ?> reportFooter;
    public static final StyleBuilder styleTituloReporte;
    public static final StyleBuilder styleHeaderColumnas;
    public static final StyleBuilder styleTextoRegular;
    public static final StyleBuilder styleTextoPreguntaRegular;
    public static final StyleBuilder styleComentarios;
    public static final StyleBuilder styleTercerTitulo; // Subtitulos de los comentarios

    public static final Color colorJAIOYellow;
    public static final Color colorJAIOBlue;
    public static final Color colorJAIOGray;

    public static final StyleBuilder styleTituloPrincipal;
    public static final StyleBuilder styleTituloSecundario;
    public static final StyleBuilder styleSubtituloCab;
    public static final StyleBuilder styleColumnaSubtitulo;
    public static final StyleBuilder styleContenidoDatos;
    public static final StyleBuilder styleNegrita;
    public static final StyleBuilder styleRptaManualMedio;
    public static final StyleBuilder styleRptaManual;

    static {

        colorJAIOYellow = new Color(255, 192, 16);
        colorJAIOBlue = new Color(0, 117, 189);
        colorJAIOGray = new Color(73, 77, 79);

        styleTituloReporte = stl.style().setFontSize(20)
                .setBold(Boolean.TRUE)
                .setForegroundColor(colorJAIOBlue)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(0);

        styleHeaderColumnas = stl.style().setFontSize(10)
                .setBold(Boolean.TRUE)
                .setForegroundColor(colorJAIOBlue)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(0) //.setBorder(stl.pen1Point())
                ;

        styleTextoRegular = stl.style().setFontSize(10)
                .setBold(Boolean.FALSE)
                .setForegroundColor(colorJAIOGray)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                //.setBorder(stl.pen1Point())
                .setPadding(0);

        styleTextoPreguntaRegular = stl.style().setFontSize(10)
                .setBold(Boolean.TRUE)
                .setForegroundColor(colorJAIOGray)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                //.setBorder(stl.pen1Point())
                .setPadding(0);

        styleContenidoDatos = stl.style()
                .setFontSize(10)
                .setBold(Boolean.FALSE)
                .setForegroundColor(colorJAIOGray)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setPadding(0) //.setBorder(stl.pen1Point())
                ;

        styleComentarios = stl.style()
                .setFontSize(10)
                .setItalic(Boolean.TRUE)
                .setAlignment(HorizontalAlignment.JUSTIFIED, VerticalAlignment.MIDDLE)
                .setForegroundColor(colorJAIOGray)
                .setHorizontalAlignment(HorizontalAlignment.LEFT);

        styleSubtituloCab = stl.style()
                .setFontSize(15)
                .setBold(Boolean.TRUE)
                .setForegroundColor(colorJAIOGray)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        styleTercerTitulo = stl.style()
                .setFontSize(10)
                .setBold(Boolean.TRUE)
                .setForegroundColor(colorJAIOGray)
                .setHorizontalAlignment(HorizontalAlignment.LEFT);

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

        styleNegrita = stl.style().bold();

        styleRptaManualMedio = stl.style()
                .setFontSize(6)
                .bold()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        styleRptaManual = stl.style()
                .setFontSize(6)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        MarginBuilder margenCaratula = margin().setLeft(50).setRight(50).setTop(60).setBottom(60);

        reportTemplate = template().setLocale(Locale.ENGLISH).setPageMargin(margenCaratula);

    }

    public static ComponentBuilder<?, ?> generaCabeceraConMetricasConComponentes(Map map, Componente objComponente, InputStream medida, DatosReporte objDatosReporte) throws FileNotFoundException {

        return cmp.verticalList(
                cmp.verticalGap(5),//SALTO DE LINEA

                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion().trim()).setStyle(ModeloGeneral.styleTituloReporte)
                ),
                cmp.verticalGap(10),
                cmp.horizontalList(
                        cmp.text(objComponente.getCoTxDescripcion().trim()).setStyle(ModeloGeneral.styleSubtituloCab)
                ),
                cmp.verticalGap(15),
                cmp.horizontalList(
                        cmp.image(medida).setFixedDimension(260, 30),
                        cmp.horizontalGap(20),
                        cmp.text("REL").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(140),
                        cmp.text("FREC").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(120),
                        cmp.text("N").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(65)
                )
        );
    }

    public static ComponentBuilder<?, ?> generaCabeceraSinMetricas(Map map, DatosReporte objDatosReporte) throws FileNotFoundException {

        return cmp.verticalList(
                cmp.verticalGap(5),
                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloReporte)
                ),
                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrCuestionario()).setStyle(ModeloGeneral.styleSubtituloCab)
                ),
                cmp.verticalGap(20)
        );

    }

    public static ComponentBuilder<?, ?> generaCabeceraConMetricas(Map map, InputStream medida, DatosReporte objDatosReporte) throws FileNotFoundException {

        return cmp.verticalList(
                cmp.verticalGap(5),
                cmp.horizontalList(
                        cmp.text(objDatosReporte.getStrDescripcion()).setStyle(ModeloGeneral.styleTituloReporte)
                ),
                cmp.verticalGap(20),
                cmp.horizontalList(
                        cmp.image(medida).setFixedDimension(260, 30),
                        cmp.horizontalGap(20),
                        cmp.text("REL").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(140),
                        cmp.text("FREC").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(120),
                        cmp.text("N").setStyle(ModeloGeneral.styleHeaderColumnas).setWidth(65)
                )
        );
    }

    public static ComponentBuilder<?, ?> generaPie(Map map) {

        String strLogo = "https://www.jaio360-app.com/images/logoJaio360.jpg";

        return cmp.verticalList().setHeight(45)
                .add(
                        cmp.horizontalList().add(
                                cmp.line().setPen(stl.pen(new Float("0.25"), LineStyle.SOLID).setLineColor(ModeloGeneral.colorJAIOYellow))),
                        cmp.verticalGap(8),
                        cmp.horizontalList(
                                cmp.horizontalGap(420),
                                cmp.image(strLogo).setFixedDimension(75, 35))
                );
    }

}
