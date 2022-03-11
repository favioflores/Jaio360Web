package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ComponenteDAO;
import com.jaio360.orm.Cuestionario;
import java.util.List;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.DetalleMetricaDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.MetricaDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.CuestionarioBean;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Componente;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.Metrica;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "crearCuestionarioView")
@ViewScoped
public class crearCuestionarioView implements Serializable {

    private String cuestionario;
    private List<CuestionarioBean> lstCuetionario;
    private List<DetalleMetrica> lstDetalleMetrica;
    CuestionarioDAO cuestionarioDAO = new CuestionarioDAO();
    ComponenteDAO componenteDao = new ComponenteDAO();
    ElementoDAO elementoDao = new ElementoDAO();
    DetalleMetricaDAO detalleMetricaDAO = new DetalleMetricaDAO();
    MetricaDAO metricaDAO = new MetricaDAO();
    ProyectoDAO proyectoDAO = new ProyectoDAO();
    private Componente componente;
    private List<Componente> lstCategoria;
    private List<Componente> lstCOmpTmpSUb;
    private List<Componente> lstCompComentarios;
    private List<Componente> lstCompAbiertas;
    private Metrica metrica;

    private Componente compToUpdate;

    private String strCuestionarioSelec;
    private String strCategoriaSelec;
    private String strPreguntaSelec;
    private String strComentarioSelec;
    private String strAbiertaSelec;
    private int intRangos;
    private int intRangoMin;
    private int intRangoMax;
    private String strRangoSelec;
    private boolean blProyectoEjecutado;
    private Integer intCantidadEjecutados;
    private Integer intTipoProyecto;
    private Integer intIdEstadoProyecto;

    private String strNuevaDescripcion;
    private String strAnteriorDescripcion;

    private String[] strRptaDesc = new String[10];

    private static final Log log = LogFactory.getLog(crearCuestionarioView.class);

    public String[] getStrRptaDesc() {
        return strRptaDesc;
    }

    public void setStrRptaDesc(String[] strRptaDesc) {
        this.strRptaDesc = strRptaDesc;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public String getStrAnteriorDescripcion() {
        return strAnteriorDescripcion;
    }

    public void setStrAnteriorDescripcion(String strAnteriorDescripcion) {
        this.strAnteriorDescripcion = strAnteriorDescripcion;
    }

    public String getStrNuevaDescripcion() {
        return strNuevaDescripcion;
    }

    public void setStrNuevaDescripcion(String strNuevaDescripcion) {
        this.strNuevaDescripcion = strNuevaDescripcion;
    }

    public Integer getIntTipoProyecto() {
        return intTipoProyecto;
    }

    public Componente getCompToUpdate() {
        return compToUpdate;
    }

    public void setCompToUpdate(Componente compToUpdate) {
        this.compToUpdate = compToUpdate;
    }

    public void setIntTipoProyecto(Integer intTipoProyecto) {
        this.intTipoProyecto = intTipoProyecto;
    }

    public int getIntRangoMin() {
        return intRangoMin;
    }

    public void setIntRangoMin(int intRangoMin) {
        this.intRangoMin = intRangoMin;
    }

    private ProyectoInfo objProyectoInfo;

    public ProyectoInfo getObjProyectoInfo() {
        return objProyectoInfo;
    }

    public void setObjProyectoInfo(ProyectoInfo objProyectoInfo) {
        this.objProyectoInfo = objProyectoInfo;
    }

    public String getStrCategoriaSelec() {
        return strCategoriaSelec;
    }

    public void setStrCategoriaSelec(String strCategoriaSelec) {
        this.strCategoriaSelec = strCategoriaSelec;
    }

    public Integer getIntCantidadEjecutados() {
        return intCantidadEjecutados;
    }

    public void setIntCantidadEjecutados(Integer intCantidadEjecutados) {
        this.intCantidadEjecutados = intCantidadEjecutados;
    }

    public boolean isBlProyectoEjecutado() {
        return blProyectoEjecutado;
    }

    public void setBlProyectoEjecutado(boolean blProyectoEjecutado) {
        this.blProyectoEjecutado = blProyectoEjecutado;
    }

    public List<Componente> getLstCOmpTmpSUb() {
        return lstCOmpTmpSUb;
    }

    public void setLstCOmpTmpSUb(List<Componente> lstCOmpTmpSUb) {
        this.lstCOmpTmpSUb = lstCOmpTmpSUb;
    }

    private List<Componente> lstComponenteSub;
    public Cuestionario objCuestionario;
    public static final int idElementoCategoria = 45;
    public static final int idElementoPreguntaAbierta = 47;
    public static final int idElementoPreguntaCerrada = 46;
    public static final int idElementoComentario = 48;
    public static final int idElementoDescriptor = 49;

    private Componente compCategoria;
    private Componente compPregunta;
    private Componente compComentario;
    private Componente compAbierta;

    private DetalleMetrica detMetrica;
    private DetalleMetrica detMetricaSelected;

    public Componente getCompAbierta() {
        return compAbierta;
    }

    public void setCompAbierta(Componente compAbierta) {
        this.compAbierta = compAbierta;
    }

    public Componente getCompComentario() {
        return compComentario;
    }

    public void setCompComentario(Componente compComentario) {
        this.compComentario = compComentario;
    }

    public DetalleMetrica getDetMetrica() {
        return detMetrica;
    }

    public void setDetMetrica(DetalleMetrica detMetrica) {
        this.detMetrica = detMetrica;
    }

    public DetalleMetrica getDetMetricaSelected() {
        return detMetricaSelected;
    }

    public void setDetMetricaSelected(DetalleMetrica detMetricaSelected) {
        this.detMetricaSelected = detMetricaSelected;
    }

    public String getStrComentarioSelec() {
        return strComentarioSelec;
    }

    public void setStrComentarioSelec(String strComentarioSelec) {
        this.strComentarioSelec = strComentarioSelec;
    }

    public String getStrPreguntaSelec() {
        return strPreguntaSelec;
    }

    public void setStrPreguntaSelec(String strPreguntaSelec) {
        this.strPreguntaSelec = strPreguntaSelec;
    }

    public List<Componente> getLstComponenteSub() {
        return lstComponenteSub;
    }

    public void setLstComponenteSub(List<Componente> lstComponenteSub) {
        this.lstComponenteSub = lstComponenteSub;
    }

    public Componente getCompCategoria() {
        return compCategoria;
    }

    public void setCompCategoria(Componente compCategoria) {
        this.compCategoria = compCategoria;
    }

    public Componente getCompPregunta() {
        return compPregunta;
    }

    public void setCompPregunta(Componente compPregunta) {
        this.compPregunta = compPregunta;
    }

    public List<Componente> getLstCompComentarios() {
        return lstCompComentarios;
    }

    public void setLstCompComentarios(List<Componente> lstCompComentarios) {
        this.lstCompComentarios = lstCompComentarios;
    }

    public int getIntRangos() {
        return intRangos;
    }

    public void setIntRangos(int intRangos) {
        this.intRangos = intRangos;
    }

    public String getStrRangoSelec() {
        return strRangoSelec;
    }

    public void setStrRangoSelec(String strRangoSelec) {
        this.strRangoSelec = strRangoSelec;
    }

    public int getIntRangoMax() {
        return intRangoMax;
    }

    public void setIntRangoMax(int intRangoMax) {
        this.intRangoMax = intRangoMax;
    }

    @PostConstruct
    public void init() {

        intTipoProyecto = Utilitarios.obtenerProyecto().getIntIdMetodologia();

        compToUpdate = new Componente();
        lstCategoria = new ArrayList<>();
        lstComponenteSub = new ArrayList<>();
        lstCompComentarios = new ArrayList<>();
        compCategoria = new Componente();
        compPregunta = new Componente();
        compComentario = new Componente();
        compAbierta = new Componente();
        objCuestionario = new Cuestionario();

        lstCOmpTmpSUb = new ArrayList<>();
        lstCompAbiertas = new ArrayList<>();
        lstDetalleMetrica = new ArrayList<>();
        lstCuetionario = new ArrayList<>();
        metrica = new Metrica();
        blProyectoEjecutado = false;
        intCantidadEjecutados = 0;
        intRangoMax = 10;

        objProyectoInfo = Utilitarios.obtenerProyecto();

        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

        this.intIdEstadoProyecto = objProyecto.getPoIdEstado();

        loadCuestionarios();
        loadRango();

    }

    public List<Componente> getLstCategoria() {
        return lstCategoria;
    }

    public void setLstCategoria(List<Componente> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public Cuestionario getObjCuestionario() {
        return objCuestionario;
    }

    public void setObjCuestionario(Cuestionario objCuestionario) {
        this.objCuestionario = objCuestionario;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public String getStrCuestionarioSelec() {
        return strCuestionarioSelec;
    }

    public void setStrCuestionarioSelec(String strCuestionarioSelec) {
        this.strCuestionarioSelec = strCuestionarioSelec;
    }

    public String getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(String cuestionario) {
        this.cuestionario = cuestionario;
    }

    public List<CuestionarioBean> getLstCuetionario() {
        return lstCuetionario;
    }

    public void setLstCuetionario(List<CuestionarioBean> lstCuetionario) {
        this.lstCuetionario = lstCuetionario;
    }

    public List<Componente> getLstCompAbiertas() {
        return lstCompAbiertas;
    }

    public void setLstCompAbiertas(List<Componente> lstCompAbiertas) {
        this.lstCompAbiertas = lstCompAbiertas;
    }

    public String getStrAbiertaSelec() {
        return strAbiertaSelec;
    }

    public void setStrAbiertaSelec(String strAbiertaSelec) {
        this.strAbiertaSelec = strAbiertaSelec;
    }

    public Metrica getMetrica() {
        return metrica;
    }

    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public List<DetalleMetrica> getLstDetalleMetrica() {
        return lstDetalleMetrica;
    }

    public void setLstDetalleMetrica(List<DetalleMetrica> lstDetalleMetrica) {
        this.lstDetalleMetrica = lstDetalleMetrica;
    }

    public void loadCuestionarios() {
        lstCuetionario = new ArrayList<>();
        List<Cuestionario> lstCuestionarios = cuestionarioDAO.obtenListaCuestionario(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Cuestionario objCuestionario : lstCuestionarios) {

            CuestionarioBean objCuestionarioBean = new CuestionarioBean();

            objCuestionarioBean.setCuIdCuestionarioPk(objCuestionario.getCuIdCuestionarioPk());
            objCuestionarioBean.setCuIdEstado(objCuestionario.getCuIdEstado());
            objCuestionarioBean.setCuDescEstado(EHCacheManager.obtenerDescripcionElemento(objCuestionario.getCuIdEstado()));
            objCuestionarioBean.setCuFeRegistro(objCuestionario.getCuFeRegistro());
            objCuestionarioBean.setCuTxDescripcion(objCuestionario.getCuTxDescripcion());
            if (objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO)) {
                objCuestionarioBean.setBoConfirmado(Boolean.FALSE);
            } else {
                objCuestionarioBean.setBoConfirmado(Boolean.TRUE);
            }

            if (objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION)) {
                intCantidadEjecutados++;
            }

            Integer intC1 = 0;
            Integer intC2 = 0;
            Integer intC3 = 0;
            Integer intC4 = 0;

            List<Componente> lstComponente = componenteDao.listaComponenteXCuestionario(objCuestionario.getCuIdCuestionarioPk());

            for (Componente objComponente : lstComponente) {

                if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA)) {
                    intC1++;
                } else if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA)) {
                    intC2++;
                } else if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_COMENTARIO)) {
                    intC3++;
                } else if (objComponente.getCoIdTipoComponente().equals(Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_ABIERTA)) {
                    intC4++;
                }

            }

            objCuestionarioBean.setIntCantidadCategorias(intC1);
            objCuestionarioBean.setIntCantidadPreguntasCerradas(intC2);
            objCuestionarioBean.setIntCantidadComentarios(intC3);
            objCuestionarioBean.setIntCantidadPreguntasAbiertas(intC4);

            lstCuetionario.add(objCuestionarioBean);

        }

    }

    public void loadRango() {

        Proyecto proyecto = this.proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        this.strRptaDesc = new String[10];
        
        this.metrica = this.metricaDAO.obtenMetricaProyecto(proyecto);

        if (metrica != null) {

            List<DetalleMetrica> lstTmpDetalleMetrica = this.detalleMetricaDAO.obtenListaDetalleMetrica(metrica);

            lstTmpDetalleMetrica.forEach((DetalleMetrica varDetalleMetrica) -> {
                strRptaDesc[varDetalleMetrica.getDeNuOrden()] = varDetalleMetrica.getDeTxValor();
            });

            this.intRangos = metrica.getMeNuRango();
            this.intRangoMin = 2;

        } else {
            this.metrica = new Metrica();
            this.intRangos = 5;
            this.intRangoMin = 2;
            guardarRango();
        }
    }

    public void guardarRango() {
        Proyecto objProyecto = this.proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        Metrica objMetrica = new Metrica();
        objMetrica.setMeFeRegistro(new Date());
        objMetrica.setMeNuRango(intRangos);
        objMetrica.setProyecto(objProyecto);
        objMetrica.setMeIdTipoMetrica(Constantes.INT_ET_TIPO_METRICA_RANGO);
        metricaDAO.guardaMetrica(objMetrica);
    }

    public void loadCuestionario(Integer intIdCuestionario) {

        this.lstCategoria.clear();
        this.lstCompComentarios.clear();
        this.lstCompAbiertas.clear();

        objCuestionario = this.cuestionarioDAO.obtenCuestionario(intIdCuestionario);
        Componente compTemporal = new Componente();
        compTemporal.setCuestionario(objCuestionario);
        compTemporal.setCoIdTipoComponente(idElementoCategoria);

        List<Componente> lstTmpComponente = this.componenteDao.listaComponenteTipo(compTemporal);
        int i = 0;
        for (Componente varComponente : lstTmpComponente) {
            Componente componenteSub = varComponente;
            List<Componente> lstTmpCompSub = new ArrayList();
            List<Componente> lstTmpCompSubNuevo = new ArrayList();
            lstTmpCompSub.addAll(this.componenteDao.listaComponenteRed(varComponente));
            int h = 0;
            for (Componente varComponenteNew : lstTmpCompSub) {
                varComponenteNew.setPosicion(String.valueOf(h));
                lstTmpCompSubNuevo.add(varComponenteNew);
                h++;
            }

            componenteSub.setStrcomponentes(lstTmpCompSubNuevo);
            componenteSub.setPosicion(String.valueOf(i));
            this.lstCategoria.add(componenteSub);
            i++;
        }

        lstTmpComponente.clear();
        compTemporal.setCoIdTipoComponente(idElementoComentario);
        lstTmpComponente = this.componenteDao.listaComponenteTipo(compTemporal);
        int p = 0;
        for (Componente varComponente : lstTmpComponente) {
            varComponente.setPosicion(String.valueOf(p));
            this.lstCompComentarios.add(varComponente);
            p++;
        }

        lstTmpComponente.clear();
        compTemporal.setCoIdTipoComponente(idElementoPreguntaAbierta);
        lstTmpComponente = this.componenteDao.listaComponenteTipo(compTemporal);
        int q = 0;
        for (Componente varComponente : lstTmpComponente) {
            varComponente.setPosicion(String.valueOf(q));
            this.lstCompAbiertas.add(varComponente);
            q++;
        }

    }

    public void addMetrica(Integer idNuOrden) {

        try {

            Proyecto objProyecto = this.proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

            DetalleMetrica objDetalleMetrica = detalleMetricaDAO.obtenDetalleMetrica(objProyecto.getPoIdProyectoPk(), idNuOrden);

            if (objDetalleMetrica == null) {

                objDetalleMetrica = new DetalleMetrica();
                objDetalleMetrica.setDeNuOrden(idNuOrden);
                this.strRangoSelec = strRptaDesc[idNuOrden];
                objDetalleMetrica.setDeTxValor(this.strRangoSelec);
                objDetalleMetrica.setMetrica(this.metrica);
                objDetalleMetrica.setDeIdDetalleEscalaPk(detalleMetricaDAO.guardaDetalleMetrica(objDetalleMetrica));
                
            } else {
                
                this.strRangoSelec = strRptaDesc[idNuOrden];
                objDetalleMetrica.setDeTxValor(this.strRangoSelec);
                objDetalleMetrica.setMetrica(this.metrica);
                detalleMetricaDAO.actualizaDetalleMetrica(objDetalleMetrica);

            }
            
            loadRango();

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "La respuesta número " + idNuOrden+1 + " se agregó correctamente", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            log.error(e);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocrrió un error al grabar la respuesta número" + idNuOrden+1, null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void eliminarMetrica(DetalleMetrica objDetalleMetrica) {

        if (lstCuetionario.isEmpty() || lstDetalleMetrica.size() > 1) {
            if (detalleMetricaDAO.eliminaDetalleMetrica(objDetalleMetrica)) {
                loadRango();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar metrica", "La metrica se eliminó correctamente");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Eliminar metrica", "Ocurrio un error al eliminar la metrica");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar metrica", "Al menos debes tener una metrica si tienes cuestionarios creados");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void guardarEscala() {
        try {
            if (objProyectoInfo.getIntIdMetodologia().equals(Constantes.INT_ET_ESTADO_TIPO_PROYECTO_ESCALA)) {
                if (metricaCorrecta()) {

                    Proyecto proyecto = this.proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
                    this.metrica = this.metricaDAO.obtenMetricaProyecto(proyecto);
                    this.metrica.setMeNuRango(this.intRangos);
                    this.metricaDAO.actualizaMetricaEliminaDetalle(metrica);
                    
                    loadRango();
                    
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "La cantidad de respuestas se guardó correctamente", null);
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                } else {
                    this.intRangos = lstDetalleMetrica.size();
                }
            } else {
                if (metricaCorrecta()) {

                    Proyecto proyecto = this.proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
                    this.metrica = this.metricaDAO.obtenMetricaProyecto(proyecto);
                    this.metrica.setMeNuRango(this.intRangos);
                    this.metricaDAO.actualizaMetrica(metrica);

                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grabar metrica", "La metrica se guardó correctamente");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                }

            }

        } catch (Exception e) {
            log.error(e);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error al grabar los datos", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    private boolean metricaCorrecta() {

        objProyectoInfo = Utilitarios.obtenerProyecto();

        if (objProyectoInfo.getIntIdMetodologia().equals(Constantes.INT_ET_ESTADO_TIPO_PROYECTO_ESCALA)) {

            if (this.intRangos > this.intRangoMax || this.intRangos < this.intRangoMin) {
                return false;
            }
            if (this.intRangos == this.lstDetalleMetrica.size()) {
                return true;
            }
            if (this.intRangos < this.lstDetalleMetrica.size()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar metricas", "El rango no puede ser menor a las opción de respuesta"));
                return false;
            }

        } else {

            if (this.intRangos < 2) {
                this.intRangos = 2;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar metricas", "La cantidad de descriptotes no puede ser menor a 2"));
                return false;
            } else if (this.intRangos > 9) {
                this.intRangos = 9;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar metricas", "La cantidad de descriptotes no puede ser mayor a 9"));
                return false;
            }

        }

        return true;
    }

    public boolean guardarFormulario() {

        if (objProyectoInfo.getIntIdMetodologia().equals(Constantes.INT_ET_ESTADO_TIPO_PROYECTO_ELECCION)) {
            for (Componente objComponente : lstCategoria) {
                if (objComponente.getStrcomponentes().size() != intRangos) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabar", "La categoria " + objComponente.getCoTxDescripcion() + " no tiene " + intRangos + " descriptores"));

                    return false;
                }
            }
        }

        this.componenteDao.elminaComponenteCuestionario(objCuestionario);

        List<Componente> lstTmpComponente = new ArrayList();
        lstTmpComponente.addAll(lstCategoria);

        /* Guarda Categorias y Preguntas */
        for (Componente varComponente : lstTmpComponente) {

            List<Componente> lstTmpComponenteSub = new ArrayList();
            lstTmpComponenteSub.addAll(varComponente.getStrcomponentes());

            Componente objComponente = varComponente;
            objComponente.setCuestionario(objCuestionario);

            this.componenteDao.guardaComponente(objComponente);

            for (Componente varComponenteSub : lstTmpComponenteSub) {
                Componente objComponenteSub = varComponenteSub;
                objComponenteSub.setComponente(objComponente);
                objComponenteSub.setCoIdTipoComponente(idElementoPreguntaCerrada);
                objComponenteSub.setCuestionario(objCuestionario);
                this.componenteDao.guardaComponente(objComponenteSub);
            }
        }

        /* Guarda Comentarios */
        lstTmpComponente.clear();
        lstTmpComponente.addAll(this.lstCompComentarios);
        for (Componente varComentario : lstTmpComponente) {
            Componente objComentario = varComentario;
            objComentario.setCuestionario(objCuestionario);
            this.componenteDao.guardaComponente(objComentario);
        }

        /* Guarda Preguntas Abiertas */
        lstTmpComponente.clear();
        lstTmpComponente.addAll(this.lstCompAbiertas);
        for (Componente varComentario : lstTmpComponente) {
            Componente objComentario = varComentario;
            objComentario.setCuestionario(objCuestionario);
            this.componenteDao.guardaComponente(objComentario);
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Grabar", "El cuestionario se guardo correctamente"));
        init();
        return true;

    }

    public void addCuestionario(ActionEvent actionEvent) {

        Cuestionario objCuestionario = new Cuestionario();

        objCuestionario.setCuFeRegistro(new Date());
        objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO);
        objCuestionario.setCuTxDescripcion(strCuestionarioSelec);
        objCuestionario.setProyecto(proyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto()));

        cuestionarioDAO.guardaCuestionario(objCuestionario);

        strCuestionarioSelec = "";

        init();

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar cuestionario", "El cuestionario se agregó correctamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void addCategoria(ActionEvent actionEvent) {

        Componente objComponente = this.crearUnComponente(idElementoCategoria);
        objComponente.setCuestionario(objCuestionario);
        objComponente.setStrcomponentes(new ArrayList<Componente>());
        objComponente.setCoTxDescripcion(this.strCategoriaSelec);

        this.strCategoriaSelec = "";

        //objComponente.setPosicion(componenteDao.obtieneMaxPosicion(objCuestionario,idElementoCategoria));
        boolean proceso = componenteDao.guardaComponente(objComponente);

        if (proceso) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar categoria", "La categoria se agregó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Agregar categoria", "Ocurrio un error al agregar la categoria");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();

    }

    public void addPregunta(ActionEvent actionEvent) {

        Componente objComponente = this.crearUnComponente(idElementoPreguntaCerrada);
        objComponente.setCuestionario(objCuestionario);
        objComponente.setComponente(this.compCategoria);
        objComponente.setStrcomponentes(new ArrayList<Componente>());
        objComponente.setCoTxDescripcion(this.strPreguntaSelec);

        this.strPreguntaSelec = "";

        //objComponente.setPosicion(componenteDao.obtieneMaxPosicion(objCuestionario,idElementoPreguntaCerrada));
        boolean proceso = componenteDao.guardaComponente(objComponente);

        if (proceso) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar pregunta", "La pregunta se agregó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Agregar pregunta", "Ocurrio un error al agregar la pregunta");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void addComentario(ActionEvent actionEvent) {
        Componente objComponente = this.crearUnComponente(idElementoComentario);
        objComponente.setCuestionario(objCuestionario);
        objComponente.setStrcomponentes(new ArrayList<Componente>());
        objComponente.setCoTxDescripcion(this.strComentarioSelec);

        this.strComentarioSelec = "";

        //objComponente.setPosicion(componenteDao.obtieneMaxPosicion(objCuestionario,idElementoComentario));
        boolean proceso = componenteDao.guardaComponente(objComponente);

        if (proceso) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar comentario", "El comentario se agregó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Agregar comentario", "Ocurrio un error al agregar el comentario");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void addPreguntaAbierta(ActionEvent actionEvent) {
        Componente objComponente = this.crearUnComponente(idElementoPreguntaAbierta);
        objComponente.setCuestionario(objCuestionario);
        objComponente.setStrcomponentes(new ArrayList<Componente>());
        objComponente.setCoTxDescripcion(this.strAbiertaSelec);

        this.strAbiertaSelec = "";

        boolean proceso = componenteDao.guardaComponente(objComponente);

        if (proceso) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar pregunta abierta", "La pregunta abierta se agregó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Agregar pregunta abierta", "Ocurrio un error al agregar la pregunta abierta");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    private Componente crearUnComponente(int idElemento) {
        //Elemento objElemento = new Elemento();
        //objElemento = this.elementoDao.obtenElemento(idElemento);
        Componente objComponente = new Componente();
        objComponente.setCoIdTipoComponente(idElemento);
        objComponente.setCoTxDescripcion("");
        return objComponente;

    }

    public void eliminarCuestionario(Integer intIdCuestionario) {
        boolean correcto = cuestionarioDAO.eliminaCuestionarioById(intIdCuestionario);

        if (correcto) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar cuestionario", "El cuestionario se eliminó correctamente"));
            init();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar cuestionario", "Ocurrio un error al eliminar el cuestionario"));
        }

    }

    public void eliminarComentario(Componente compComentario) {

        if (componenteDao.eliminaComponente(compComentario)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar comentario", "Se elimino correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Eliminar comentario", "Ocurrio un error al eliminar el comentario"));
        }
        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void eliminarAbierta(Componente compAbierta) {

        if (componenteDao.eliminaComponente(compAbierta)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar pregunta abierta", "Se elimino correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Eliminar pregunta abierta", "Ocurrio un error al eliminar la pregunta abierta"));
        }
        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void eliminarCategoria(Componente compCategoria) {

        if (componenteDao.eliminaComponente(compCategoria)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar categoria", "Se elimino correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Eliminar categoria", "Ocurrio un error al eliminar la categoria"));
        }
        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void cargaItem(Componente compItem) {
        this.strNuevaDescripcion = Constantes.strVacio;
        this.strAnteriorDescripcion = compItem.getCoTxDescripcion();
        this.compToUpdate = compItem;
    }

    public void clearDialog() {
        this.strNuevaDescripcion = Constantes.strVacio;
        this.strAnteriorDescripcion = Constantes.strVacio;
        this.compToUpdate = new Componente();
        this.strCategoriaSelec = Constantes.strVacio;
        this.strCuestionarioSelec = Constantes.strVacio;
        this.strPreguntaSelec = Constantes.strVacio;
        this.strComentarioSelec = Constantes.strVacio;
        this.strAbiertaSelec = Constantes.strVacio;
    }

    public void actualizaItem() {
        this.compToUpdate.setCoTxDescripcion(strNuevaDescripcion);
        if (this.componenteDao.actualizaComponente(compToUpdate)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion de elemento", "Item actualizado correctamente"));
            this.strAnteriorDescripcion = this.strNuevaDescripcion;
            this.strNuevaDescripcion = Constantes.strVacio;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Actualizacion de elemento", "Ocurrio un error al querer actualizar este elemento"));
        }
    }

    public void eliminarCuestionarioSub(Componente compCategoria, Componente compPregunta) {

        if (componenteDao.eliminaComponente(compPregunta)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar pregunta cerrada", "Se elimino correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Eliminar pregunta cerrada", "Ocurrio un error al eliminar la pregunta cerrada"));
        }
        this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        this.loadCuestionarios();
    }

    public void reOrderRowCategoria() {

        int i = 0;

        for (Componente objComponente : lstCategoria) {
            objComponente.setPosicion(i + "");
            i++;
        }

    }

    public void reOrderRowPregunta() {

        for (Componente objComponente : lstCategoria) {

            int i = 0;

            for (Componente objSubComponente : objComponente.getStrcomponentes()) {
                objSubComponente.setPosicion(i + "");
                i++;
            }

        }

    }

    public void bajarAbiertas(Componente objComponente) {

        Integer pos = 0;

        for (Componente objPA : lstCompAbiertas) {
            if (objPA.equals(objComponente)) {
                pos = Integer.parseInt(objComponente.getPosicion());
            }
        }

        if (!pos.equals(lstCompAbiertas.size() - 1)) {

            Componente compTemp = lstCompAbiertas.get(pos + 1);
            String descNue = compTemp.getCoTxDescripcion();

            compTemp.setCoTxDescripcion(objComponente.getCoTxDescripcion());
            objComponente.setCoTxDescripcion(descNue);

            componenteDao.actualizaComponente(compTemp);
            componenteDao.actualizaComponente(objComponente);
        }
    }

    public void subirAbiertas(Componente objComponente) {

        Integer pos = 0;

        for (Componente objPA : lstCompAbiertas) {
            if (objPA.equals(objComponente)) {
                pos = Integer.parseInt(objComponente.getPosicion());
            }
        }

        if (!pos.equals(0)) {

            Componente compTemp = lstCompAbiertas.get(pos - 1);
            String descNue = compTemp.getCoTxDescripcion();

            compTemp.setCoTxDescripcion(objComponente.getCoTxDescripcion());
            objComponente.setCoTxDescripcion(descNue);

            componenteDao.actualizaComponente(compTemp);
            componenteDao.actualizaComponente(objComponente);
        }

    }

    public void bajarComentario(Componente objComponente) {

        Integer pos = 0;

        for (Componente objPA : lstCompComentarios) {
            if (objPA.equals(objComponente)) {
                pos = Integer.parseInt(objComponente.getPosicion());
            }
        }

        if (!pos.equals(lstCompComentarios.size() - 1)) {

            Componente compTemp = lstCompComentarios.get(pos + 1);
            String descNue = compTemp.getCoTxDescripcion();

            compTemp.setCoTxDescripcion(objComponente.getCoTxDescripcion());
            objComponente.setCoTxDescripcion(descNue);

            componenteDao.actualizaComponente(compTemp);
            componenteDao.actualizaComponente(objComponente);
        }
    }

    public void subirComentario(Componente objComponente) {

        Integer pos = 0;

        for (Componente objPA : lstCompComentarios) {
            if (objPA.equals(objComponente)) {
                pos = Integer.parseInt(objComponente.getPosicion());
            }
        }

        if (!pos.equals(0)) {

            Componente compTemp = lstCompComentarios.get(pos - 1);
            String descNue = compTemp.getCoTxDescripcion();

            compTemp.setCoTxDescripcion(objComponente.getCoTxDescripcion());
            objComponente.setCoTxDescripcion(descNue);

            componenteDao.actualizaComponente(compTemp);
            componenteDao.actualizaComponente(objComponente);
        }
    }

    public void bajarCategoria(Componente objComponente) {

        Componente objComponenteAnterior = null;

        Iterator itLstComponentes = lstCategoria.iterator();

        while (itLstComponentes.hasNext()) {
            Componente componenteTemp = (Componente) itLstComponentes.next();

            if (componenteTemp.equals(objComponente)) {
                if (itLstComponentes.hasNext()) {
                    objComponenteAnterior = (Componente) itLstComponentes.next();
                }
            }
        }

        if (objComponenteAnterior != null) {

            componenteDao.eliminaComponente(objComponente);
            componenteDao.eliminaComponente(objComponenteAnterior);

            for (Componente objComp : objComponente.getStrcomponentes()) {
                componenteDao.eliminaComponente(objComp);
            }

            for (Componente objComp : objComponenteAnterior.getStrcomponentes()) {
                componenteDao.eliminaComponente(objComp);
            }

            Integer ant = objComponenteAnterior.getCoIdComponentePk();
            objComponenteAnterior.setCoIdComponentePk(objComponente.getCoIdComponentePk());
            objComponente.setCoIdComponentePk(ant);

            componenteDao.guardaComponentes(objComponente);
            componenteDao.guardaComponentes(objComponenteAnterior);

            this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        }

    }

    public void subirCategoria(Componente objComponente) {

        Componente objComponenteAnterior = null;

        for (Componente objComponenteTemp : lstCategoria) {
            if (objComponenteTemp.equals(objComponente)) {
                break;
            }
            objComponenteAnterior = objComponenteTemp;
        }

        if (objComponenteAnterior != null) {

            componenteDao.eliminaComponente(objComponente);
            componenteDao.eliminaComponente(objComponenteAnterior);

            for (Componente objComp : objComponente.getStrcomponentes()) {
                componenteDao.eliminaComponente(objComp);
            }

            for (Componente objComp : objComponenteAnterior.getStrcomponentes()) {
                componenteDao.eliminaComponente(objComp);
            }

            Integer ant = objComponenteAnterior.getCoIdComponentePk();
            objComponenteAnterior.setCoIdComponentePk(objComponente.getCoIdComponentePk());
            objComponente.setCoIdComponentePk(ant);

            componenteDao.guardaComponentes(objComponente);
            componenteDao.guardaComponentes(objComponenteAnterior);

            this.loadCuestionario(objCuestionario.getCuIdCuestionarioPk());
        }
    }

    public void bajarSubCuestionario(Componente objComponenteCat, Componente objComponenteSub) {

        Componente objComponenteDespues = null;

        List<Componente> lstComponentes = objComponenteCat.getStrcomponentes();

        Iterator itLstComponentes = lstComponentes.iterator();

        while (itLstComponentes.hasNext()) {
            Componente componenteTemp = (Componente) itLstComponentes.next();

            if (componenteTemp.equals(objComponenteSub)) {
                if (itLstComponentes.hasNext()) {
                    objComponenteDespues = (Componente) itLstComponentes.next();
                }
            }
        }

        if (objComponenteDespues != null) {
            String strAnt = objComponenteDespues.getCoTxDescripcion();

            objComponenteDespues.setCoTxDescripcion(objComponenteSub.getCoTxDescripcion());
            objComponenteSub.setCoTxDescripcion(strAnt);

            componenteDao.actualizaComponente(objComponenteDespues);
            componenteDao.actualizaComponente(objComponenteSub);
        }

    }

    public void subirSubCuestionario(Componente objComponenteCat, Componente objComponenteSub) {

        Componente objComponenteAnterior = null;

        for (Componente objComponente : objComponenteCat.getStrcomponentes()) {
            if (objComponente.equals(objComponenteSub)) {
                break;
            }
            objComponenteAnterior = objComponente;
        }

        if (objComponenteAnterior != null) {
            String strAnt = objComponenteAnterior.getCoTxDescripcion();

            objComponenteAnterior.setCoTxDescripcion(objComponenteSub.getCoTxDescripcion());
            objComponenteSub.setCoTxDescripcion(strAnt);

            componenteDao.actualizaComponente(objComponenteAnterior);
            componenteDao.actualizaComponente(objComponenteSub);
        }
    }

    public void confirmaCuestionario(CuestionarioBean objCuestionarioBean) {

        Cuestionario objCuestionario = cuestionarioDAO.obtenCuestionario(objCuestionarioBean.getCuIdCuestionarioPk());

        if (objCuestionarioBean.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO)) {
            /* QUIERE CONFIRMAR */
            if (objCuestionarioBean.getIntCantidadPreguntasCerradas() == 0 || objCuestionarioBean.getIntCantidadCategorias() == 0) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirmación", "No es posible confirmar un cuestionario sin preguntas");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                if (objCuestionarioBean.isBoConfirmado()) {
                    objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO);
                } else {
                    objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO);
                }

                cuestionarioDAO.actualizaCuestionario(objCuestionario);

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Cuestionario " + objCuestionarioBean.getCuTxDescripcion() + " confirmado correctamente");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                init();

            }

        } else if (objCuestionarioBean.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO)) {
            /* QUIERE REVERTIR */
            objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO);

            cuestionarioDAO.actualizaCuestionario(objCuestionario);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Cuestionario " + objCuestionarioBean.getCuTxDescripcion() + " revertido correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            init();
        }

        loadCuestionario(objCuestionarioBean.getCuIdCuestionarioPk());

    }

    public void onRowEditCuestionario(RowEditEvent event) {

        CuestionarioBean objCuestionarioBean = (CuestionarioBean) event.getObject();

        Cuestionario objCuestionario = cuestionarioDAO.obtenCuestionario(objCuestionarioBean.getCuIdCuestionarioPk());

        objCuestionario.setCuTxDescripcion(objCuestionarioBean.getCuTxDescripcion());

        cuestionarioDAO.actualizaCuestionario(objCuestionario);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio de nombre", "Se acutalizo el nombre correctamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void subirMetrica(DetalleMetrica objDetalleMetrica) {

        Integer pos = lstDetalleMetrica.indexOf(objDetalleMetrica);

        if (!pos.equals(0)) {

            if (detalleMetricaDAO.subirMetrica(objDetalleMetrica, lstDetalleMetrica, metrica)) {
                loadRango();
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Subir metrica", "Ocurrio un error al mover la metrica");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }

    public void bajarMetrica(DetalleMetrica objDetalleMetrica) {

        Integer pos = lstDetalleMetrica.indexOf(objDetalleMetrica);

        if (!pos.equals(lstDetalleMetrica.size() - 1)) {

            if (detalleMetricaDAO.bajarMetrica(objDetalleMetrica, lstDetalleMetrica, metrica)) {
                loadRango();
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bajar metrica", "Ocurrio un error al mover la metrica");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }

    public void irImportarCuestionario(Integer idCuestionario) {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("importarCuestionario");
            session.setAttribute("importarCuestionario", idCuestionario);
            FacesContext.getCurrentInstance().getExternalContext().redirect("importarCuestionario.jsf");
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void actualizaCategoria(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Car Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
