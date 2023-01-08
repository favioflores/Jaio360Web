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

                if (objUsuarioSaldo == null) {
                    objUsuarioSaldo = new UsuarioSaldo();
                    objUsuarioSaldo.setUsIdCuentaPk(objUser.getUsIdCuentaPk());
                    objUsuarioSaldoDAO.guarda(objUsuarioSaldo, objSession);
                }

                for (Movimiento objMovimiento : lstMovements) {

                    strResult = executeMovement(objMovimiento, objUsuarioSaldo, objSession);

                    if (strResult != null) {
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
                //RESTA RESERVA Y SUMA USADO
                if (valAmount(objUsuarioSaldo.getUsNrUsadoIndividual(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoIndividual(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrUsadoIndividual(objUsuarioSaldo.getUsNrUsadoIndividual() + (intQuantity));
                    objUsuarioSaldo.setUsNrReservadoIndividual(objUsuarioSaldo.getUsNrReservadoIndividual() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_EJECUTA_LICENCIA_MASIVA)) {
                //RESTA RESERVA Y SUMA USADO
                if (valAmount(objUsuarioSaldo.getUsNrUsadoMasivo(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoMasivo(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrUsadoMasivo(objUsuarioSaldo.getUsNrUsadoMasivo() + (intQuantity));
                    objUsuarioSaldo.setUsNrReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_LIBERAR_LICENCIA_INDIVIDUAL)) {
                //RESTA RESERVA Y SUMA DISPONIBLE
                if (valAmount(objUsuarioSaldo.getUsNrDisponibleIndividual(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoMasivo(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrDisponibleIndividual(objUsuarioSaldo.getUsNrDisponibleIndividual() + (intQuantity));
                    objUsuarioSaldo.setUsNrReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            } else if (objTipoMovimiento.getTmIdTipoMovPk().equals(Movimientos.MOV_LIBERAR_LICENCIA_MASIVA)) {
                //RESTA RESERVA Y SUMA DISPONIBLE
                if (valAmount(objUsuarioSaldo.getUsNrDisponibleMasivo(), intQuantity)
                        && valAmount(objUsuarioSaldo.getUsNrReservadoMasivo(), intQuantity * SUBTRACTION)) {
                    objUsuarioSaldo.setUsNrDisponibleMasivo(objUsuarioSaldo.getUsNrDisponibleMasivo() + (intQuantity));
                    objUsuarioSaldo.setUsNrReservadoMasivo(objUsuarioSaldo.getUsNrReservadoMasivo() + (intQuantity * SUBTRACTION));
                } else {
                    blCorrect = false;
                }
            }

            if (!blCorrect) {
                throw new ExecutorMovementException("Error en la ejecución de movimientos.");
            } else {
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

}
