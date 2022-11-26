package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Movimiento;
import com.jaio360.orm.TipoMovimiento;
import com.jaio360.orm.Usuario;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MovimientoDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Log log = LogFactory.getLog(MovimientoDAO.class);

    public Integer guarda(Movimiento usuario, Session objSession) throws HibernateException {
        Integer id = null;

        try {
            
            id = (Integer) objSession.save(usuario);

        } catch (HibernateException he) {
            log.error(he);
        } 

        return id;
    }

    public boolean actualizar(Movimiento usuario) throws HibernateException {
        boolean correcto = true;
        try {
            iniciaOperacion();
            sesion.update(usuario);
            tx.commit();
        } catch (HibernateException he) {
            correcto = false;
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
        return correcto;
    }

    public void eliminar(Movimiento usuario) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(usuario);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Movimiento obtenMovimiento(long idUsuario) throws HibernateException {
        Movimiento usuario = null;
        try {
            iniciaOperacion();
            usuario = (Movimiento) sesion.get(MovimientoDAO.class, (int) idUsuario);
        } finally {
            sesion.close();
        }

        return usuario;
    }

    public Movimiento obtenTipoMovimiento(long idUsuario, Session sesion) throws HibernateException {
        Movimiento usuario = null;
        try {
            usuario = (Movimiento) sesion.get(MovimientoDAO.class, (int) idUsuario);
        } catch (HibernateException ex) {
            log.error(ex);
        }

        return usuario;
    }
    
    public List<Movimiento> obtenListaMovimientos(Integer idUsuario) throws HibernateException {
        
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("select m from Movimiento m where m.usuario.usIdCuentaPk = ? order by m.moIdMovimientoPk ");
            
            query.setInteger(0, idUsuario);
            
            return query.list();
            
        } finally {
            sesion.close();
        }

    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        log.error(he);
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    private void manejaExcepcion() throws HibernateException {
        tx.rollback();
    }

}
