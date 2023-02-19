package com.jaio360.dao;

import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Log log = LogFactory.getLog(UsuarioDAO.class);

    public Integer guardaUsuario(Usuario usuario) throws HibernateException {
        Integer id = 0;

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

    public long guardaCliente(ManageUserRelation manageUserRelation) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(manageUserRelation));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public boolean actualizaUsuario(Usuario usuario) throws HibernateException {
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

    public void eliminaUsuario(Usuario usuario) throws HibernateException {
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

    public Usuario obtenUsuario(long idUsuario) throws HibernateException {
        Usuario usuario = null;
        try {
            iniciaOperacion();
            usuario = (Usuario) sesion.get(Usuario.class, (int) idUsuario);
        } finally {
            sesion.close();
        }

        return usuario;
    }

    public Usuario obtenUsuario(long idUsuario, Session sesion) throws HibernateException {
        Usuario usuario = null;
        try {
            usuario = (Usuario) sesion.get(Usuario.class, (int) idUsuario);
        } catch (HibernateException ex) {
            log.error(ex);
        }

        return usuario;
    }

    public Usuario obtenUsuarioByEmail(String strEmail) throws HibernateException {
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Usuario where us_id_mail = ?");

            query.setString(0, strEmail);

            return (Usuario) query.uniqueResult();
        } catch (HibernateException ex) {
            log.error(ex);
        } finally {
            sesion.close();
        }
        return null;

    }

    public Object obtenUsuario(String strEmail, Session sesion) throws HibernateException {
        try {
            Query query = sesion.createQuery("from Usuario where us_id_mail = ?");

            query.setString(0, strEmail);

            return query.uniqueResult();
        } catch (HibernateException ex) {
            log.error(ex);
        }
        return null;

    }

    public List<Usuario> obtenListaUsuario() throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            iniciaOperacion();
            listaUsuario = sesion.createQuery("from Usuario").list();
        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List obtenListaClientes(Integer idUserManager) throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Usuario u join u.manageUserRelations m where m.usIdCuentaManagerPk = ? ");
            query.setInteger(0, idUserManager);

            listaUsuario = query.list();

        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaUsuario(List lstCorreos) throws HibernateException {
        List<Usuario> listaUsuario = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Usuario u where u.usIdMail in ( :lstCorreos ) ");

            query.setParameterList("lstCorreos", lstCorreos);

            listaUsuario = query.list();

        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaUsuario(List lstCorreos, Session objSession) throws HibernateException {
        List<Usuario> listaUsuario = null;

        try {

            Query query = objSession.createQuery("from Usuario u where u.usIdMail in ( :lstCorreos ) ");

            query.setParameterList("lstCorreos", lstCorreos);

            listaUsuario = query.list();

        } catch (HibernateException e) {
            log.error(e);
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaUsuarioPorPerfil(Integer intPerfil) throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Usuario u where u.usIdTipoCuenta = ? and u.usIdEstado != ? order by usTxNombreRazonsocial ");
            query.setInteger(0, intPerfil);
            query.setInteger(1, Constantes.INT_ET_ESTADO_USUARIO_BLOQUEADO);
            listaUsuario = query.list();
        } catch (HibernateException ex) {
            log.error(ex);
        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaClientesPorPerfil(Integer intPerfil, Integer intManager) throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("select u from Usuario u join u.manageUserRelations m where m.usIdCuentaManagerPk = ? and u.usIdTipoCuenta in (?) and u.usIdEstado = ? order by u.usTxNombreRazonsocial ");

            query.setInteger(0, intManager);
            query.setInteger(1, intPerfil);
            query.setInteger(2, Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);
            
            listaUsuario = query.list();
        } catch (HibernateException ex) {
            log.error(ex);
        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaUsuarioPorPerfil(Integer intPerfil1, Integer intPerfil2, Integer intPerfil3) throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Usuario u where u.usIdTipoCuenta in (?,?,?) and u.usIdEstado = ? order by usTxNombreRazonsocial ");
            query.setInteger(0, intPerfil1);
            query.setInteger(1, intPerfil2);
            query.setInteger(2, intPerfil3);
            query.setInteger(3, Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);
            listaUsuario = query.list();
        } catch (HibernateException ex) {
            log.error(ex);
        } finally {
            sesion.close();
        }

        return listaUsuario;
    }

    public List<Usuario> obtenListaUsuario(Session sesion) throws HibernateException {
        List<Usuario> listaUsuario = null;
        try {
            listaUsuario = sesion.createQuery("from Usuario").list();
        } catch (HibernateException ex) {
            log.error(ex);
        }

        return listaUsuario;
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

    public Usuario iniciaSesion(String strEmail) {

        iniciaOperacion();

        Usuario objUsuario = null;

        try {

            List<Usuario> lstUsuarios;

            Query query = sesion.createQuery("from Usuario where us_id_mail = ?");

            query.setString(0, strEmail);

            lstUsuarios = query.list();

            if (lstUsuarios.isEmpty()) {
                log.debug("Usuario no encontrado");
            } else if (lstUsuarios.size() > 1) {
                log.debug("Se encontraron 2 o mas usuarios con el mismo email");
            } else {
                log.debug("Inicio de sesion exitoso");
                objUsuario = lstUsuarios.get(0);
            }

        } catch (HibernateException ex) {
            log.error(ex);
        } finally {
            sesion.close();
        }

        return objUsuario;

    }

    public List<Usuario> crearCuentasParaProyecto(Map hEvaluados, Map hUsuarios) {

        List<Usuario> LstUsuariosGrabados = new ArrayList();

        try {

            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
            Usuario obtenUsuario = obtenUsuario(objUsuarioInfo.getIntUsuarioPk());

            /* VERIFICAMOS SI ALGUNO TIENE CUENTAS CREADAS */
            Iterator it = hEvaluados.entrySet().iterator();

            EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

            iniciaOperacion();

            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();

                if (hUsuarios.containsKey(entry.getKey())) { //DEBE SER ACTUALIZADO SI ESTA ANULADO

                    Usuario objUsuarioRepetido = (Usuario) hUsuarios.get(entry.getKey());

                    if (!objUsuarioRepetido.getUsIdEstado().equals(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO)) {
                        objUsuarioRepetido.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);

                        sesion.update(objUsuarioRepetido);

                    }

                    LstUsuariosGrabados.add(objUsuarioRepetido);

                } else { //ES UN NUEVO USUARIO

                    Usuario objUsuarioNuevo = new Usuario();

                    objUsuarioNuevo.setUsBlImagenEmpresa(obtenUsuario.getUsBlImagenEmpresa());
                    objUsuarioNuevo.setUsTxDescripcionEmpresa(obtenUsuario.getUsTxDescripcionEmpresa());
                    objUsuarioNuevo.setUbigeo(obtenUsuario.getUbigeo());
                    objUsuarioNuevo.setUsIdMail(entry.getKey().toString());
                    objUsuarioNuevo.setUsTxNombreRazonsocial(entry.getValue().toString());
                    objUsuarioNuevo.setUsTxContrasenia(objEncryptDecrypt.encrypt(Utilitarios.generarClave()));
                    objUsuarioNuevo.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);
                    objUsuarioNuevo.setUsIdTipoCuenta(Constantes.INT_ET_TIPO_USUARIO_EVALUATED_EVALUATOR);
                    objUsuarioNuevo.setUsFeNacimiento(null);
                    objUsuarioNuevo.setUsTxDocumento(null);
                    objUsuarioNuevo.setUsIdTipoDocumento(null);
                    objUsuarioNuevo.setUsFeRegistro(new Date());

                    sesion.save(objUsuarioNuevo);

                    LstUsuariosGrabados.add(objUsuarioNuevo);

                }

            }

            tx.commit();

        } catch (HibernateException e) {
            log.error(e);
            manejaExcepcion(e);
        } catch (Exception e) {
            log.error(e);
            manejaExcepcion();
        } finally {
            sesion.close();
        }

        return LstUsuariosGrabados;
    }

}
