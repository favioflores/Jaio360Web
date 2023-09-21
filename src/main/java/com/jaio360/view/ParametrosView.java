package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.ParametroDAO;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "parametrosView")
@ViewScoped

public class ParametrosView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(ParametrosView.class);

    private Boolean blSexo = false;
    private Boolean blRangoEdad = false;
    private Boolean blTiempoEmpresa = false;
    private Boolean blNivelOcupacional = false;
    private Boolean blAreaNegocio = false;

    private Boolean blPonderacionHabilitado = false;

    private String strSex;
    private String strRangoEdad;
    private String strRangoTiempo;

    private List<String> job;
    private List<String> area;
    private String[] selectedJob;
    private String[] selectedAreaNegocio;

    private ParametroDAO objParametroDAO = new ParametroDAO();
    private ElementoDAO objElementoDAO = new ElementoDAO();

    public Boolean getBlPonderacionHabilitado() {
        return blPonderacionHabilitado;
    }

    public void setBlPonderacionHabilitado(Boolean blPonderacionHabilitado) {
        this.blPonderacionHabilitado = blPonderacionHabilitado;
    }

    public Boolean getBlSexo() {
        return blSexo;
    }

    public void setBlSexo(Boolean blSexo) {
        this.blSexo = blSexo;
    }

    public Boolean getBlRangoEdad() {
        return blRangoEdad;
    }

    public void setBlRangoEdad(Boolean blRangoEdad) {
        this.blRangoEdad = blRangoEdad;
    }

    public Boolean getBlTiempoEmpresa() {
        return blTiempoEmpresa;
    }

    public void setBlTiempoEmpresa(Boolean blTiempoEmpresa) {
        this.blTiempoEmpresa = blTiempoEmpresa;
    }

    public Boolean getBlNivelOcupacional() {
        return blNivelOcupacional;
    }

    public void setBlNivelOcupacional(Boolean blNivelOcupacional) {
        this.blNivelOcupacional = blNivelOcupacional;
    }

    public Boolean getBlAreaNegocio() {
        return blAreaNegocio;
    }

    public void setBlAreaNegocio(Boolean blAreaNegocio) {
        this.blAreaNegocio = blAreaNegocio;
    }

    public String getStrSex() {
        return strSex;
    }

    public void setStrSex(String strSex) {
        this.strSex = strSex;
    }

    public String getStrRangoEdad() {
        return strRangoEdad;
    }

    public void setStrRangoEdad(String strRangoEdad) {
        this.strRangoEdad = strRangoEdad;
    }

    public String getStrRangoTiempo() {
        return strRangoTiempo;
    }

    public void setStrRangoTiempo(String strRangoTiempo) {
        this.strRangoTiempo = strRangoTiempo;
    }

    public List<String> getJob() {
        return job;
    }

    public void setJob(List<String> job) {
        this.job = job;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String[] getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(String[] selectedJob) {
        this.selectedJob = selectedJob;
    }

    public String[] getSelectedAreaNegocio() {
        return selectedAreaNegocio;
    }

    public void setSelectedAreaNegocio(String[] selectedAreaNegocio) {
        this.selectedAreaNegocio = selectedAreaNegocio;
    }

    public ParametroDAO getObjParametroDAO() {
        return objParametroDAO;
    }

    public void setObjParametroDAO(ParametroDAO objParametroDAO) {
        this.objParametroDAO = objParametroDAO;
    }

    public ElementoDAO getObjElementoDAO() {
        return objElementoDAO;
    }

    public void setObjElementoDAO(ElementoDAO objElementoDAO) {
        this.objElementoDAO = objElementoDAO;
    }

    public ParametrosView() {
        cargarJobs();
        cargarAreas();
        cargarDatosExistentes();
    }

    public void activarParametro(Integer intIdTipoParametro) {

        try {

            String strPatron;

            Parametro objParametro = objParametroDAO.obtenParametro(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdTipoParametro);

            if (objParametro == null) {

                Proyecto objProyecto = new Proyecto();

                objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());

                objParametro = new Parametro();
                objParametro.setPaIdTipoParametro(intIdTipoParametro);
                objParametro.setProyecto(objProyecto);

                if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
                    if (blSexo) {
                        strPatron = msg("step1.sex.option");//"FEMENINO,MASCULINO";
                        objParametro.setPaTxPatron(strPatron.getBytes());
                        objParametroDAO.guardaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {
                    if (blRangoEdad) {
                        strPatron = "20,30,40,50,60,70,80,90";
                        objParametro.setPaTxPatron(strPatron.getBytes());
                        objParametroDAO.guardaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {
                    if (blTiempoEmpresa) {
                        strPatron = "1,3,5,10,20,50";
                        objParametro.setPaTxPatron(strPatron.getBytes());
                        objParametroDAO.guardaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
                    if (blNivelOcupacional) {
                        strPatron = job.toString();
                        strPatron = strPatron.replace("[", "");
                        strPatron = strPatron.replace("]", "");
                        strPatron = strPatron.replace(", ", ",");
                        objParametro.setPaTxPatron(strPatron.getBytes());
                        objParametroDAO.guardaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
                    if (blAreaNegocio) {
                        strPatron = area.toString();
                        strPatron = strPatron.replace("[", "");
                        strPatron = strPatron.replace("]", "");
                        strPatron = strPatron.replace(", ", ",");
                        objParametro.setPaTxPatron(strPatron.getBytes());
                        objParametroDAO.guardaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES)) {
                    if (blPonderacionHabilitado) {
                        objParametro.setPaInHabilitado(blPonderacionHabilitado);
                        objParametroDAO.guardaParametro(objParametro);
                    }
                }

                mostrarAlertaInfo("enabled");
                cargarDatosExistentes();

            } else {

                if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
                    if (!blSexo) {
                        strSex = "";
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {
                    if (!blRangoEdad) {
                        strRangoEdad = "";
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {
                    if (!blTiempoEmpresa) {
                        strRangoTiempo = "";
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
                    if (!blNivelOcupacional) {
                        selectedJob = null;
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
                    if (!blAreaNegocio) {
                        selectedAreaNegocio = null;
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES)) {
                    if (!blPonderacionHabilitado) {
                        objParametroDAO.eliminaParametro(objParametro);
                    }
                }

                mostrarAlertaInfo("disabled");

            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    private void cargarJobs() {
        job = new ArrayList();
        List<Elemento> lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_ROLES);

        for (Elemento objElemento : lstElementos) {
            job.add(objElemento.getElTxDescripcion());
        }
    }

    private void cargarAreas() {
        area = new ArrayList();

        List<Elemento> lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_AREAS);

        for (Elemento objElemento : lstElementos) {
            area.add(objElemento.getElTxDescripcion());
        }
    }

    private void cargarDatosExistentes() {

        List<Parametro> lstParametros = objParametroDAO.obtenListaParametros(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Parametro objParametro : lstParametros) {
            if (Utilitarios.noEsNuloOVacio(objParametro.getPaTxPatron()) || objParametro.getPaInHabilitado()) {

                if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
                    blSexo = true;
                    strSex = new String(objParametro.getPaTxPatron());
                } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {
                    blRangoEdad = true;
                    strRangoEdad = new String(objParametro.getPaTxPatron());
                } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {
                    blTiempoEmpresa = true;
                    strRangoTiempo = new String(objParametro.getPaTxPatron());
                } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
                    blAreaNegocio = true;
                    String temp = new String(objParametro.getPaTxPatron());
                    selectedAreaNegocio = temp.split(",");
                } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
                    blNivelOcupacional = true;
                    String temp = new String(objParametro.getPaTxPatron());
                    selectedJob = temp.split(",");
                } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES)) {
                    blPonderacionHabilitado = objParametro.getPaInHabilitado();
                }

            }
        }
    }

    public void guardarPreferencia(Integer intIdTipoParametro) {

        String strPatron = "";

        Parametro objParametro = objParametroDAO.obtenParametro(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdTipoParametro);

        if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {

            strPatron = strRangoEdad;
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);

        } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
            strPatron = strSex;
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);
        } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
            int i = 0;
            while (i < selectedAreaNegocio.length) {
                if (i == 0) {
                    strPatron = selectedAreaNegocio[i];
                } else {
                    strPatron += "," + selectedAreaNegocio[i];
                }
                i++;
            }
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);

        } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
            int i = 0;
            while (i < selectedJob.length) {
                if (i == 0) {
                    strPatron = selectedJob[i];
                } else {
                    strPatron += "," + selectedJob[i];
                }
                i++;
            }
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);

        } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {

            strPatron = strRangoTiempo;
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);

        } else if (intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_PONDERACION_RELACIONES)) {

            objParametro.setPaInHabilitado(blPonderacionHabilitado);

            objParametroDAO.actualizaParametro(objParametro);

        }

        cargarDatosExistentes();

        mostrarAlertaInfo("success");

    }

}
