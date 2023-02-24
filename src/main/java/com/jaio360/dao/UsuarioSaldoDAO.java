package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.UsuarioSaldo;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioSaldoDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Log log = LogFactory.getLog(UsuarioSaldoDAO.class);

    public Integer guardar(UsuarioSaldo usuario) throws HibernateException {
        Integer id = null;

        try {
            iniciaOperacion();
            id = (Integer) sesion.save(usuario);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public long guarda(UsuarioSaldo usuario, Session objSession) throws HibernateException {
        Integer id = null;

        try {
            id = (Integer) objSession.save(usuario);
        } catch (HibernateException he) {
            log.error(he);
        }

        return id;
    }

    public boolean actualizar(UsuarioSaldo usuario, Session objSession) throws HibernateException {
        boolean correcto = true;
        try {
            sesion.update(usuario);
        } catch (HibernateException he) {
            log.error(he);
            return false;
        }
        return correcto;
    }

    public void eliminar(UsuarioSaldo usuario) throws HibernateException {
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

    public UsuarioSaldo obtenUsuarioSaldo(long idUsuario) throws HibernateException {
        UsuarioSaldo usuario = null;
        try {
            iniciaOperacion();
            usuario = (UsuarioSaldo) sesion.get(UsuarioSaldo.class, (int) idUsuario);
        } finally {
            sesion.close();
        }

        return usuario;
    }

    public UsuarioSaldo obtenUsuarioSaldo(long idUsuario, Session sesion) throws HibernateException {
        UsuarioSaldo usuario = null;
        try {
            usuario = (UsuarioSaldo) sesion.get(UsuarioSaldo.class, (int) idUsuario);
        } catch (HibernateException ex) {
            log.error(ex);
        }

        return usuario;
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
