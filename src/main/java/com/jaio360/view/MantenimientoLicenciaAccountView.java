package com.jaio360.view;

import com.jaio360.component.ExecutorBalanceMovement;
import com.jaio360.dao.ContratoDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.MovimientoDAO;
import com.jaio360.dao.TarifaDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.domain.MovimientoBean;
import com.jaio360.domain.UsuarioSaldoBean;
import com.jaio360.orm.Contrato;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.Tarifa;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import com.jaio360.orm.UsuarioSaldo;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

@ManagedBean(name = "mantenimientoLicenciaAccountView")
@ViewScoped
public class MantenimientoLicenciaAccountView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(MantenimientoLicenciaAccountView.class);

    private static final long serialVersionUID = -1L;

    private List<SelectItem> lstTarifas;
    private List<SelectItem> lstUsuarios;

    /* FORM AGREGAR LICENCIAS */
    private Integer idUsuario;
    private Integer intCantidadLicencias;
    private Integer idTarifa;
    private String strMontoBruto;
    private Integer intMaxQuantity;

    List<MovimientoBean> lstMovimientos;
    List<UsuarioSaldoBean> lstUsuarioSaldo;

    public Integer getIntMaxQuantity() {
        return intMaxQuantity;
    }

    public void setIntMaxQuantity(Integer intMaxQuantity) {
        this.intMaxQuantity = intMaxQuantity;
    }

    public List<UsuarioSaldoBean> getLstUsuarioSaldo() {
        return lstUsuarioSaldo;
    }

    public void setLstUsuarioSaldo(List<UsuarioSaldoBean> lstUsuarioSaldo) {
        this.lstUsuarioSaldo = lstUsuarioSaldo;
    }

    public List<MovimientoBean> getLstMovimientos() {
        return lstMovimientos;
    }

    public void setLstMovimientos(List<MovimientoBean> lstMovimientos) {
        this.lstMovimientos = lstMovimientos;
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

        limpiarFormulario();
        poblarUsuarios();
        verLicenciasAsignadas();
        //poblarTarifas();
        //maxQuantity();
        //calcularMontoBruto();

    }

    private void maxQuantity() {

        try {

            intMaxQuantity = 100;

            ElementoDAO objElementoDAO = new ElementoDAO();

            Elemento objElemento = objElementoDAO.obtenElemento(Constantes.INT_ET_MAX_QUANTITY);

            intMaxQuantity = Integer.parseInt(objElemento.getElTxValor1());

        } catch (NumberFormatException | HibernateException ex) {
            mostrarError(log, ex);
        }

    }

    public void limpiarFormulario() {
        this.idTarifa = null;
        this.intCantidadLicencias = 1;
        this.strMontoBruto = "0.00";
    }

    public void calcularMontoBruto() {

        try {

            if (this.idTarifa != null) {
                TarifaDAO objTarifaDAO = new TarifaDAO();
                Tarifa objTarifa = objTarifaDAO.obtenTarifa(this.idTarifa);

                if (this.intCantidadLicencias > this.intMaxQuantity) {
                    this.intCantidadLicencias = this.intMaxQuantity;
                }
                BigDecimal bdCantidadLicencias = new BigDecimal(this.intCantidadLicencias.doubleValue());
                this.strMontoBruto = objTarifa.getTaTxPrefijo() + objTarifa.getTaDePrecio().multiply(bdCantidadLicencias).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            } else {
                this.strMontoBruto = "0.00";
            }

        } catch (HibernateException ex) {
            mostrarError(log, ex);
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

                TarifaDAO objTarifaDAO = new TarifaDAO();
                Tarifa objTarifa = objTarifaDAO.obtenTarifa(idTarifa);
                objContrato.setTarifa(objTarifa);

                Usuario objUsuario = new Usuario();
                objUsuario.setUsIdCuentaPk(idUsuario);
                objContrato.setUsuario(objUsuario);

                ContratoDAO objContratoDAO = new ContratoDAO();
                objContratoDAO.guardaContrato(objContrato);

                List<Movimiento> lstMovements = new ArrayList<>();

                Movimiento objMovimiento = new Movimiento();
                objMovimiento.setMoFeCreacion(new Date());
                objMovimiento.setMoInCantidad(intCantidadLicencias);

                if (objTarifa.getTaIdTipoTarifa() == 202) {
                    objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_COMPRA_LICENCIA_INDIVIDUAL));
                } else if (objTarifa.getTaIdTipoTarifa() == 203) {
                    objMovimiento.setTipoMovimiento(new TipoMovimiento(Movimientos.MOV_COMPRA_LICENCIA_MASIVA));
                }
                lstMovements.add(objMovimiento);

                String strResult = ExecutorBalanceMovement.getInstance().execute(lstMovements, objUsuario);

                if (strResult == null) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Licencia asignada correctamente", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);

                    limpiarFormulario();
                    verLicenciasAsignadas();
                } else {
                    mostrarAlertaError("Ocurrió un error al asignar las licencias");
                }
            }

        } catch (Exception e) {
            log.error(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error inesperado. Por favor volver a intentar", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void verLicenciasAsignadas() {

        try {

            lstMovimientos = new ArrayList<>();
            lstUsuarioSaldo = new ArrayList<>();

            UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();
            UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(idUsuario);
            UsuarioSaldoBean objUsuarioSaldoBean = new UsuarioSaldoBean();
            
            if (objUsuarioSaldo == null) {
                objUsuarioSaldo = new UsuarioSaldo();
                objUsuarioSaldo.setUsNrTotalIndividual(0);
                objUsuarioSaldo.setUsNrTotalMasivo(0);
                objUsuarioSaldo.setUsNrDisponibleIndividual(0);
                objUsuarioSaldo.setUsNrDisponibleMasivo(0);
                objUsuarioSaldo.setUsNrReservadoIndividual(0);
                objUsuarioSaldo.setUsNrReservadoMasivo(0);
                objUsuarioSaldo.setUsNrUsadoIndividual(0);
                objUsuarioSaldo.setUsNrUsadoMasivo(0);
                objUsuarioSaldo.setUsIdCuentaPk(idUsuario);

                objUsuarioSaldoDAO.guardar(objUsuarioSaldo);

            }
            
            objUsuarioSaldoBean.setIntTotalIndividual(objUsuarioSaldo.getUsNrTotalIndividual());
            objUsuarioSaldoBean.setIntTotalMasivo(objUsuarioSaldo.getUsNrTotalMasivo());
            objUsuarioSaldoBean.setIntDisponibleIndividual(objUsuarioSaldo.getUsNrDisponibleIndividual());
            objUsuarioSaldoBean.setIntDisponibleMasivo(objUsuarioSaldo.getUsNrDisponibleMasivo());
            objUsuarioSaldoBean.setIntReservadoIndividual(objUsuarioSaldo.getUsNrReservadoIndividual());
            objUsuarioSaldoBean.setIntReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo());
            objUsuarioSaldoBean.setIntUtilizadoIndividual(objUsuarioSaldo.getUsNrUsadoIndividual());
            objUsuarioSaldoBean.setIntUtilizadoMasivo(objUsuarioSaldo.getUsNrUsadoMasivo());
            objUsuarioSaldoBean.setIntUsuario(idUsuario);

            lstUsuarioSaldo.add(objUsuarioSaldoBean);

            MovimientoDAO objMovimientoDAO = new MovimientoDAO();
            List<Movimiento> lstMovimiento = objMovimientoDAO.obtenListaMovimientos(idUsuario);

            if (lstMovimiento.isEmpty()) {
                mostrarAlertaWarning("licencias.no.encontradas");
            } else {

                MovimientoBean objMovimientoBean;

                for (Movimiento objMovimiento : lstMovimiento) {

                    objMovimientoBean = new MovimientoBean();

                    objMovimientoBean.setIntIdMovimiento(objMovimiento.getMoIdMovimientoPk());
                    objMovimientoBean.setIntCantidad(objMovimiento.getMoInCantidad());
                    objMovimientoBean.setDtCreacion(objMovimiento.getMoFeCreacion());
                    objMovimientoBean.setStrDescMovimiento(msg(objMovimiento.getTipoMovimiento().getTmIdTipoMovPk().toString()));

                    lstMovimientos.add(objMovimientoBean);
                }

                mostrarAlertaInfo("licencias.encontradas");
            }

        } catch (HibernateException e) {
            mostrarError(log, e);
        }

    }

    private void poblarTarifas() {

        try {

            lstTarifas = new ArrayList<>();

            TarifaDAO tarifaDAO = new TarifaDAO();

            List<Tarifa> lstTarifa = tarifaDAO.obtenListaTarifaParaClientes();

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

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(Utilitarios.obtenerUsuario().getIntUsuarioPk());
            objSelectItem.setLabel(Utilitarios.obtenerUsuario().getStrDescripcion() + " - " + Utilitarios.obtenerUsuario().getStrEmail());

            this.idUsuario = Utilitarios.obtenerUsuario().getIntUsuarioPk();

            lstUsuarios.add(objSelectItem);

        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }

}