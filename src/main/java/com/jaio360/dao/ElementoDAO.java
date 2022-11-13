package com.jaio360.dao;

import com.jaio360.orm.Elemento;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ElementoDAO extends BaseView implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Log log = LogFactory.getLog(ElementoDAO.class);

    public ElementoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public long guardaElemento(Elemento elemento) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(elemento));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaElemento(Elemento elemento) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(elemento);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaElemento(Elemento elemento) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(elemento);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Elemento obtenElemento(long idElemento) throws HibernateException {
        Elemento elemento = null;
        try {
            iniciaOperacion();
            elemento = (Elemento) sesion.get(Elemento.class, (int) idElemento);
        } finally {
            sesion.close();
        }

        return elemento;
    }

    public List<Elemento> obtenListaElemento() throws HibernateException {
        List<Elemento> listaElemento = null;

        try {
            Query query = sesion.createQuery("from Elemento el where el.elInActivo = ? ");
            query.setInteger(0, Constantes.INT_ESTADO_ELEMENTO_ACTIVO);
            listaElemento = query.list();

            if (!listaElemento.isEmpty()) {
                for (Elemento objElemento : listaElemento) {
                    if (Utilitarios.noEsNuloOVacio(msg(objElemento.getElIdElementoPk().toString()))) {
                        objElemento.setElTxDescripcion(msg(objElemento.getElIdElementoPk().toString()));
                    }
                }
            }
            
        } catch (Exception e) {
            log.error(e);
        }

        return listaElemento;
    }

    public List<Elemento> obtenListaElementoXDefinicion(Integer intDefinicion) throws HibernateException {
        List<Elemento> listaElemento = null;

        try {
            Query query = sesion.createQuery("from Elemento el where el.definicionTabla.dtIdDefinicionPk = ? order by el.elNuOrden ");
            query.setInteger(0, intDefinicion);
            listaElemento = query.list();

            if (!listaElemento.isEmpty()) {
                for (Elemento objElemento : listaElemento) {
                    if (Utilitarios.noEsNuloOVacio(msg(objElemento.getElIdElementoPk().toString()))) {
                        objElemento.setElTxDescripcion(msg(objElemento.getElIdElementoPk().toString()));
                    }
                }
            }

        } catch (Exception e) {
            log.error(e);
        }

        return listaElemento;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }
}
