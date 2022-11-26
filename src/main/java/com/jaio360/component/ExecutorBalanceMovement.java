package com.jaio360.component;

import com.jaio360.dao.MovimientoDAO;
import com.jaio360.dao.TipoMovimientoDAO;
import com.jaio360.dao.UsuarioSaldoDAO;
import com.jaio360.exception.ExecutorMovementException;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import com.jaio360.orm.UsuarioSaldo;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Movimientos;
import com.jaio360.view.BaseView;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author favio.flores
 */
public class ExecutorBalanceMovement extends BaseView {

    private static final Log log = LogFactory.getLog(ExecutorBalanceMovement.class);

    private static final Integer SUBTRACTION = -1;

    private static ExecutorBalanceMovement instance = null;

    private static UsuarioSaldoDAO objUsuarioSaldoDAO = new UsuarioSaldoDAO();

    public static ExecutorBalanceMovement getInstance() {
        if (instance == null) {
            instance = new ExecutorBalanceMovement();
        }
        return instance;

    }

    public synchronized String execute(List<Movimiento> lstMovements, Usuario objUser) {

        Session objSession = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = objSession.beginTransaction();

        try {

            String strResult = Constantes.strVacio;

            if (!lstMovements.isEmpty()) {

                UsuarioSaldo objUsuarioSaldo = objUsuarioSaldoDAO.obtenUsuarioSaldo(objUser.getUsIdCuentaPk(), objSession);
                
                if(objUsuarioSaldo==null){
                    objUsuarioSaldo = new UsuarioSaldo();
                    objUsuarioSaldo.setUsIdCuentaPk(objUser.getUsIdCuentaPk());
                    objUsuarioSaldoDAO.guarda(objUsuarioSaldo, objSession);
                }

                for (Movimiento objMovimiento : lstMovements) {

                    strResult = executeMovement(objMovimiento, objUsuarioSaldo, objSession);

                    if (strResult!=null) {
                        tx.rollback();
                        return strResult;
                    }

                }
                
                //objUsuarioSaldoDAO.actualizar(objUsuarioSaldo, objSession);
            }

            tx.commit();

            return strResult;

        } catch (Exception e) {
            mostrarError(log, e);
            tx.rollback();
            return msg("error.was.occurred");
        } finally {
            objSession.close();
        }

    }

    private String executeMovement(Movimiento objMovimiento, UsuarioSaldo objUsuarioSaldo, Session objSession) {

        try {

            TipoMovimiento objTipoMovimiento = objMovimiento.getTipoMovimiento();

            Integer intQuantity = objMovimiento.getMoInCantidad();
            
            objMovimiento.setMoFeCreacion(new Date());

            boolean blCorrect = true;

            if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_COMPRA_LICENCIA_INDIVIDUAL)) {
                //SUMA TOTAL Y SUMA DISPONIBLE
                if (valAmount(objUsuarioSaldo.getUsNrTotalIndividual(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrDisponibleIndividual(), intQuantity)) {
                    objUsuarioSaldo.setUsNrTotalIndividual(objUsuarioSaldo.getUsNrTotalIndividual() + intQuantity);
                    objUsuarioSaldo.setUsNrDisponibleIndividual(objUsuarioSaldo.getUsNrDisponibleIndividual() + intQuantity);
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_COMPRA_LICENCIA_MASIVA)) {
                //SUMA TOTAL Y SUMA DISPONIBLE
                if (valAmount(objUsuarioSaldo.getUsNrTotalMasivo(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrDisponibleMasivo(), intQuantity)) {
                    objUsuarioSaldo.setUsNrTotalMasivo(objUsuarioSaldo.getUsNrTotalMasivo() + intQuantity);
                    objUsuarioSaldo.setUsNrDisponibleMasivo(objUsuarioSaldo.getUsNrDisponibleMasivo() + intQuantity);
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_RESERVA_LICENCIA_INDIVIDUAL)) {
                //RESTA DISPONIBLE Y SUMA RESERVA
                if (valAmount(objUsuarioSaldo.getUsNrReservadoIndividual(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrDisponibleIndividual(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrReservadoIndividual(objUsuarioSaldo.getUsNrReservadoIndividual() + intQuantity);
                    objUsuarioSaldo.setUsNrDisponibleIndividual(objUsuarioSaldo.getUsNrDisponibleIndividual() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_RESERVA_LICENCIA_MASIVA)) {
                //RESTA DISPONIBLE Y SUMA RESERVA
                if (valAmount(objUsuarioSaldo.getUsNrReservadoMasivo(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrDisponibleMasivo(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo() + intQuantity);
                    objUsuarioSaldo.setUsNrDisponibleMasivo(objUsuarioSaldo.getUsNrDisponibleMasivo() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_EJECUTA_LICENCIA_INDIVIDUAL)) {
                //RESTA TOTAL Y RESTA RESERVA
                if (valAmount(objUsuarioSaldo.getUsNrTotalIndividual(), intQuantity * SUBTRACTION)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoIndividual(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrTotalIndividual(objUsuarioSaldo.getUsNrTotalIndividual() + (intQuantity * SUBTRACTION));
                    objUsuarioSaldo.setUsNrReservadoIndividual(objUsuarioSaldo.getUsNrReservadoIndividual() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_EJECUTA_LICENCIA_MASIVA)) {
                //RESTA TOTAL Y RESTA RESERVA
                if (valAmount(objUsuarioSaldo.getUsNrTotalMasivo(), intQuantity * SUBTRACTION)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoMasivo(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrTotalMasivo(objUsuarioSaldo.getUsNrTotalMasivo() + (intQuantity * SUBTRACTION));
                    objUsuarioSaldo.setUsNrReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            }

            if (!blCorrect) {
                throw new ExecutorMovementException("Student failed because he has scored less than 35.");
            }else{
                MovimientoDAO objMovimientoDAO = new MovimientoDAO();
                Usuario objUsuario = new Usuario();
                objUsuario.setUsIdCuentaPk(objUsuarioSaldo.getUsIdCuentaPk());
                objMovimiento.setUsuario(objUsuario);
                objMovimientoDAO.guarda(objMovimiento, objSession);
            }

        } catch (Exception e) {
            mostrarError(log, e);
            return msg("error.was.occurred");
        }

        return null;
    }

    private static boolean valAmount(Integer intOrigin, Integer IntQuantity) {

        try {
            return (intOrigin + IntQuantity) >= 0;
        } catch (Exception e) {
            mostrarError(log, e);
        }
        return false;
    }
/*
    private static void applyChangeInAmount(Integer intOrigin, Integer IntQuantity) {

        try {
            intOrigin = intOrigin + IntQuantity;
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    
    private static String updateBalanceUser(UsuarioSaldo objUsuarioSaldo, Integer intTypeMovement, Session objSession) {

        try {

            if (intTypeMovement.equals(Movimientos.MOV_COMPRA_LICENCIA_INDIVIDUAL)) {
                objUsuarioSaldo.
            
            
        
            }
        } catch (Exception e) {
            mostrarError(log, e);
        }

        return null;

    }
     */
}
