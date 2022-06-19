package com.jaio360.view;

import com.jaio360.dao.ContratoDAO;
import com.jaio360.dao.TarifaDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.orm.Contrato;
import com.jaio360.orm.Tarifa;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "mantenimientoLicenciaView")
@ViewScoped
public class MantenimientoLicenciaView implements Serializable {

    private static Log log = LogFactory.getLog(MantenimientoLicenciaView.class);

    private static final long serialVersionUID = -1L;

    private List<SelectItem> lstTarifas;
    private List<SelectItem> lstUsuarios;

    /* FORM AGREGAR LICENCIAS */
    private Integer idUsuario;
    private Integer intCantidadLicencias;
    private Integer idTarifa;
    private String strMontoBruto;

    List<Contrato> lstContratos;

    public List<Contrato> getLstContratos() {
        return lstContratos;
    }

    public void setLstContratos(List<Contrato> lstContratos) {
        this.lstContratos = lstContratos;
    }

    public Integer getIntCantidadLicencias() {
        return intCantidadLicencias;
    }

    public void setIntCantidadLicencias(Integer intCantidadLicencias) {
        this.intCantidadLicencias = intCantidadLicencias;
    }

    public String getStrMontoBruto() {
        return strMontoBruto;
    }

    public void setStrMontoBruto(String strMontoBruto) {
        this.strMontoBruto = strMontoBruto;
    }

    private boolean isEdit;

    public List<SelectItem> getLstTarifas() {
        return lstTarifas;
    }

    public void setLstTarifas(List<SelectItem> lstTarifas) {
        this.lstTarifas = lstTarifas;
    }

    public List<SelectItem> getLstUsuarios() {
        return lstUsuarios;
    }

    public void setLstUsuarios(List<SelectItem> lstUsuarios) {
        this.lstUsuarios = lstUsuarios;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @PostConstruct
    public void init() {

        poblarUsuarios();
        poblarTarifas();
        calcularMontoBruto();
        limpiarFormulario();        

    }

    public void limpiarFormulario() {
        this.idTarifa = null;
        this.intCantidadLicencias = 0;
        this.strMontoBruto = "0.00";
    }

    public void calcularMontoBruto() {

        try {

            if (this.idTarifa != null) {
                TarifaDAO objTarifaDAO = new TarifaDAO();
                Tarifa objTarifa = objTarifaDAO.obtenTarifa(this.idTarifa);

                BigDecimal bdCantidadLicencias = new BigDecimal(this.intCantidadLicencias.doubleValue());
                this.strMontoBruto = objTarifa.getTaTxPrefijo() + objTarifa.getTaDePrecio().multiply(bdCantidadLicencias).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Monto bruto calculado", null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                this.strMontoBruto = "0.00";
            }

        } catch (Exception ex) {
            log.error(ex);
        }

    }

    public void asignarLicencias() {

        try {

            if (Utilitarios.esNuloOVacio(this.idUsuario)
                    || Utilitarios.esNuloOVacio(this.intCantidadLicencias)
                    || Utilitarios.esNuloOVacio(this.idTarifa)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos para asignar las licencias. Por favor ingresar todos los datos solicitados", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                Contrato objContrato = new Contrato();
                objContrato.setCoFeCreacion(new Date());
                objContrato.setCoIdEstado(Constantes.INT_ET_ESTADO_CONTRATO_BLOQUEADO);
                objContrato.setCoNuLicenciaTotal(intCantidadLicencias);
                objContrato.setCoNuLicenciaDisponible(intCantidadLicencias);
                objContrato.setCoNuLicenciaReservada(0);
                Tarifa objTarifa = new Tarifa();
                objTarifa.setTaIdTarifaPk(idTarifa);
                objContrato.setTarifa(objTarifa);
                Usuario objUsuario = new Usuario();
                objUsuario.setUsIdCuentaPk(idUsuario);
                objContrato.setUsuario(objUsuario);

                ContratoDAO objContratoDAO = new ContratoDAO();
                objContratoDAO.guardaContrato(objContrato);

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Licencia asignada correctamente", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                
                limpiarFormulario();
                verLicenciasAsignadas();
            }

        } catch (Exception e) {
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error inesperado. Por favor volver a intentar", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void verLicenciasAsignadas() {

        try {

            lstContratos = new ArrayList<>();

            if (Utilitarios.noEsNuloOVacio(idUsuario)) {

                ContratoDAO objContratoDAO = new ContratoDAO();
                lstContratos = objContratoDAO.obtenListaContratoPorUsuario(idUsuario);

                if (lstContratos.isEmpty()) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No se encontraron licencias asignadas", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se encontraron licencias asignadas", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }

        } catch (Exception e) {
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error inesperado. Por favor volver a intentar", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    private void poblarTarifas() {

        try {

            lstTarifas = new ArrayList<>();

            TarifaDAO tarifaDAO = new TarifaDAO();

            List<Tarifa> lstTarifa = tarifaDAO.obtenListaTarifa();

            for (Tarifa objTarifa : lstTarifa) {

                SelectItem objSelectItem = new SelectItem();
                objSelectItem.setValue(objTarifa.getTaIdTarifaPk());
                objSelectItem.setLabel(objTarifa.getTaTxDescripcion() + " - " + objTarifa.getTaTxPrefijo() + objTarifa.getTaDePrecio().doubleValue());

                lstTarifas.add(objSelectItem);
            }
        } catch (Exception ex) {
            log.error(ex);
        }

    }

    private void poblarUsuarios() {

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            lstUsuarios = new ArrayList<>();

            List<Usuario> lstUsers = usuarioDAO.obtenListaUsuarioPorPerfil(Constantes.INT_ET_TIPO_USUARIO_USUARIO_MAESTRO);

            for (Usuario objUsuario : lstUsers) {

                SelectItem objSelectItem = new SelectItem();
                objSelectItem.setValue(objUsuario.getUsIdCuentaPk());
                objSelectItem.setLabel(objUsuario.getUsTxNombreRazonsocial() + " - " + objUsuario.getUsIdMail());

                lstUsuarios.add(objSelectItem);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

}
