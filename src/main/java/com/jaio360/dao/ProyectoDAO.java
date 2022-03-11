package com.jaio360.dao;

import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.CuestionarioEvaluado;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProyectoDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    private Log log = LogFactory.getLog(ProyectoDAO.class);

    public long guardaProyecto(Proyecto proyecto) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(proyecto)) ; 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        }  

        return id; 
    }  

    public void actualizaProyecto(Proyecto proyecto) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(proyecto); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public void eliminaProyecto(Integer intIdProyecto) throws HibernateException { 
        
        try{ 
            iniciaOperacion(); 
             /*
            Query query1 = sesion.createSQLQuery("delete from detalle_metrica where ME_ID_METRICA_FK in ( select ME_ID_METRICA_PK from metrica where PO_ID_PROYECTO_FK = ? ) ");
            Query query2 = sesion.createSQLQuery("delete from metrica where PO_ID_PROYECTO_FK = ? ");
            Query query3 = sesion.createSQLQuery("delete from cuestionario_evaluado where PO_ID_PROYECTO_FK = ? ");
            Query query4 = sesion.createSQLQuery("delete from relacion_participante where PA_ID_PARTICIPANTE_FK in (select PA_ID_PARTICIPANTE_PK from participante where PO_ID_PROYECTO_FK = ? ) ");
            Query query5 = sesion.createSQLQuery("delete from participante where PO_ID_PROYECTO_FK = ? "); 
            Query query6 = sesion.createSQLQuery("delete from red_evaluacion where PO_ID_PROYECTO_FK = ? ");
            Query query7 = sesion.createSQLQuery("delete from participante where PO_ID_PROYECTO_FK = ? ");
            Query query8 = sesion.createSQLQuery("delete from mensaje where PO_ID_PROYECTO_FK = ? ");
            Query query9 = sesion.createSQLQuery("delete from componente where CU_ID_CUESTIONARIO_FK in (select CU_ID_CUESTIONARIO_PK from cuestionario where PO_ID_PROYECTO_FK = ? ) ");
            Query query10 = sesion.createSQLQuery("delete from cuestionario where PO_ID_PROYECTO_FK = ? ");
            Query query11 = sesion.createSQLQuery("delete from relacion where PO_ID_PROYECTO_FK = ? ");
            Query query12 = sesion.createSQLQuery("delete from parametro where PO_ID_PROYECTO_FK = ? ");
            */
            Query query13 = sesion.createSQLQuery("update proyecto set PO_ID_ESTADO = 201 where PO_ID_PROYECTO_PK = ? ");
            
            /*
            query1.setInteger(0, intIdProyecto);
            query2.setInteger(0, intIdProyecto);
            query3.setInteger(0, intIdProyecto);
            query4.setInteger(0, intIdProyecto);
            query5.setInteger(0, intIdProyecto);
            query6.setInteger(0, intIdProyecto);
            query7.setInteger(0, intIdProyecto);
            query8.setInteger(0, intIdProyecto);
            query9.setInteger(0, intIdProyecto);
            query10.setInteger(0, intIdProyecto);
            query11.setInteger(0, intIdProyecto);
            query12.setInteger(0, intIdProyecto);
            */
            query13.setInteger(0, intIdProyecto);
            
            /*query1.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            query4.executeUpdate();
            query5.executeUpdate();
            query6.executeUpdate();
            query7.executeUpdate();
            query8.executeUpdate();
            query9.executeUpdate();
            query10.executeUpdate();
            query11.executeUpdate();
            query12.executeUpdate();
            */
            query13.executeUpdate();
            
            tx.commit(); 
            //tx.rollback();
        } catch (HibernateException he){ 

            manejaExcepcion(he); 
            throw he; 
        } finally { 
            sesion.close(); 
        } 
    }  

    public Proyecto obtenProyecto(long idProyecto) throws HibernateException 
    { 
        Proyecto proyecto = null;  
        try 
        { 
            iniciaOperacion(); 
            proyecto = (Proyecto) sesion.get(Proyecto.class, (int)idProyecto); 
        } finally 
        { 
            sesion.close(); 
        }  

        return proyecto; 
    }  

    public List<Proyecto> obtenListaProyecto() throws HibernateException 
    { 
        List<Proyecto> listaProyecto = null;  

        try 
        { 
            iniciaOperacion(); 
            listaProyecto = sesion.createQuery("from Proyecto").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaProyecto; 
    }  

    public List<Proyecto> obtenListaProyectosPorUsuario(Integer intIdUsuario, 
            Integer intIdProyecto, 
            Boolean blocultos,
            String txtDescripcion,
            Integer idTipoProyecto,
            Integer idEstadoProyecto,
            Date txtFechaRegistroInicial,
            Date txtFechaRegistroFinal,
            Date txtFechaEjecucionInicial,
            Date txtFechaEjecucionFinal
            ) throws HibernateException { 
        
        //(objUsuarioInfo.getIntUsuarioPk(), null, blOcultos, txtDescripcion,idTipoProyecto,idEstadoProyecto,txtFechaEjecucionInicial,txtFechaEjecucionInicial,txtFechaRegistroFinal,txtFechaRegistroInicial);
        
        List<Proyecto> lstProyectos = null;  

        try {
            
            iniciaOperacion();
            
            String strCadena = " select p "
                             + " from Proyecto p "
                             + " where p.usuario.usIdCuentaPk = ? "
                             + " and p.poIdEstado != ? ";
                                
                            if(intIdProyecto != null) strCadena += " and poIdProyectoPk = ? ";
                            
                            if(Utilitarios.noEsNuloOVacio(blocultos)){ 
                                strCadena += " and poInOculto = "+blocultos;
                            }
                            if(Utilitarios.noEsNuloOVacio(txtDescripcion)){ 
                                strCadena += " and poTxDescripcion like ? ";
                            }
                            if(Utilitarios.noEsNuloOVacio(idTipoProyecto) && idTipoProyecto > 0){ 
                                strCadena += " and poIdMetodologia = ? ";
                            }
                            if(Utilitarios.noEsNuloOVacio(idEstadoProyecto) && idEstadoProyecto > 0){ 
                                strCadena += " and poIdEstado = ? ";
                            }
                            if(Utilitarios.noEsNuloOVacio(txtFechaRegistroInicial)){ 
                                strCadena += " and poFeRegistro >= ? ";
                            }
                            if(Utilitarios.noEsNuloOVacio(txtFechaRegistroFinal)){ 
                                strCadena += " and poFeRegistro <= ? ";
                            }
                            
//                            if(Utilitarios.noEsNuloOVacio(txtFechaEjecucionInicial)){ 
//                                strCadena += " and poFeEjecucion >= ? ";
//                            }
//                            if(Utilitarios.noEsNuloOVacio(txtFechaEjecucionFinal)){ 
//                                strCadena += " and poFeEjecucion <= ? ";
//                            }

                            
                  strCadena += " order by p.poIdProyectoPk desc ";
                    
            Query query = sesion.createQuery(strCadena);
            int param = 0;
            query.setInteger(param++, intIdUsuario);
            query.setInteger(param++, Constantes.INT_ET_ESTADO_PROYECTO_ELIMINADO);
            
            if(intIdProyecto != null)
                query.setInteger(param++, intIdProyecto);
            if(Utilitarios.noEsNuloOVacio(txtDescripcion))
                query.setString(param++, txtDescripcion);
            if(Utilitarios.noEsNuloOVacio(idTipoProyecto) && idTipoProyecto > 0)
                query.setInteger(param++, idTipoProyecto);
            if(Utilitarios.noEsNuloOVacio(idEstadoProyecto) && idEstadoProyecto > 0)
                query.setInteger(param++, idEstadoProyecto);
            if(Utilitarios.noEsNuloOVacio(txtFechaRegistroInicial))
                query.setDate(param++, txtFechaRegistroInicial);
            if(Utilitarios.noEsNuloOVacio(txtFechaRegistroFinal))
                query.setDate(param++, txtFechaRegistroFinal);
            /*
            if(Utilitarios.noEsNuloOVacio(txtFechaEjecucionInicial))
                query.setDate(param++, txtFechaEjecucionInicial);
            if(Utilitarios.noEsNuloOVacio(txtFechaEjecucionFinal))
                query.setDate(param++, txtFechaEjecucionFinal);
            */
            
            return query.list();
            
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        return lstProyectos;
        
    }  

    public List<Proyecto> obtenListaRedesPorUsuario(String strCorreoUsuario) throws HibernateException { 
        
        List<Proyecto> lstProyectos = null;  

        try {
            
            iniciaOperacion();
            Query query = sesion.createQuery("select po from Proyecto as po inner join po.participantes as pa where pa.paTxCorreo = ? and pa.paInRedCargada = ? and pa.paIdEstado = ? and po.poIdEstado not in (?) order by po.poIdProyectoPk desc ");
            
            query.setString(0, strCorreoUsuario);
            query.setBoolean(1, false);
            query.setInteger(2, Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);
            //query.setInteger(2, Constantes.INT_ET_ESTADO_PROYECTO_REGISTRADO);
            //query.setInteger(3, Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION);
            query.setInteger(3, Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO); 
            
            return query.list();
            
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        return lstProyectos;
        
    } 
    
        public List<Proyecto> obtenListaEvaluacionesPorUsuario(String strCorreoUsuario) throws HibernateException { 
        
        List<Proyecto> lstEvaluacion = null;

        try {
            
            iniciaOperacion();
            
            Query query = sesion.createSQLQuery("select distinct * from (                                          "+
                                                " select po.PO_ID_PROYECTO_PK, " +
                                                " po.US_ID_CUENTA_FK, " +
                                                " po.PO_TX_DESCRIPCION, " +
                                                " po.PO_ID_ESTADO, " +
                                                " po.PO_FE_REGISTRO, " +
                                                " po.PO_FE_EJECUCION, " +
                                                " po.PO_ID_METODOLOGIA, " +
                                                " po.PO_TX_MOTIVO, pa.PA_TX_DESCRIPCION, cu.CU_TX_DESCRIPCION, cu.CU_ID_CUESTIONARIO_PK, pa.PA_ID_PARTICIPANTE_PK, pa.PA_TX_CORREO, RE_TX_DESCRIPCION, RE_ID_RELACION_FK "+
                                                "   from red_evaluacion re,                                        "+
                                                " 	   participante pa,                                            "+
                                                " 	   relacion_participante rp,                                   "+
                                                " 	   proyecto po,                                                "+
                                                "	   cuestionario_evaluado ce,                                   "+
                                                "       cuestionario cu                                            "+
                                                "  where rp.RE_ID_PARTICIPANTE_FK = re.RE_ID_PARTICIPANTE_PK       "+
                                                "    and pa.PA_ID_PARTICIPANTE_PK = rp.PA_ID_PARTICIPANTE_FK       "+
                                                "    and ce.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK       "+
                                                "    and cu.CU_ID_CUESTIONARIO_PK = ce.CU_ID_CUESTIONARIO_FK       "+
                                                "    and cu.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK               "+
                                                "    and cu.CU_ID_ESTADO = ?                                      "+
                                                "    and ce.CE_ID_ESTADO = ?                                      "+
                                                "    and pa.PA_IN_RED_CARGADA = ?                               "+
                                                "    and pa.PA_IN_RED_VERIFICADA = ?                            "+
                                                "    and po.PO_ID_PROYECTO_PK = pa.PO_ID_PROYECTO_FK               "+
                                                "    and po.PO_ID_ESTADO = ?                                      "+
                                                "    and re.RE_TX_CORREO = ?               "+                   
                                                "    and rp.RP_ID_ESTADO = 79 "+
                                                "    and re.RE_ID_ESTADO = ?       " +
                                                "  union all                                                       "+
                                                " select po.PO_ID_PROYECTO_PK, " +
                                                " po.US_ID_CUENTA_FK, " +
                                                " po.PO_TX_DESCRIPCION, " +
                                                " po.PO_ID_ESTADO, " +
                                                " po.PO_FE_REGISTRO, " +
                                                " po.PO_FE_EJECUCION, " +
                                                " po.PO_ID_METODOLOGIA, " +
                                                " po.PO_TX_MOTIVO, pa.PA_TX_DESCRIPCION, cu.CU_TX_DESCRIPCION, cu.CU_ID_CUESTIONARIO_PK, pa.PA_ID_PARTICIPANTE_PK, pa.PA_TX_CORREO, PA_TX_DESCRIPCION, NULL "+
                                                "   from participante pa,                                          "+
                                                " 	   proyecto po,                                                "+
                                                "	   cuestionario_evaluado ce,                                   "+
                                                "       cuestionario cu                                            "+
                                                "  where ce.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK       "+
                                                "    and cu.CU_ID_CUESTIONARIO_PK = ce.CU_ID_CUESTIONARIO_FK       "+
                                                "    and cu.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK               "+
                                                "	and cu.CU_ID_ESTADO = ?                                       "+
                                                "    and ce.CE_ID_ESTADO = ?                                      "+
                                                "	and pa.PA_IN_RED_CARGADA = ?                                "+
                                                "    and pa.PA_IN_RED_VERIFICADA = ?                            "+
                                                "    and pa.PA_IN_AUTOEVALUAR = ?                               "+
                                                "    and pa.PA_TX_CORREO = ?               "+                   
                                                "    and po.PO_ID_PROYECTO_PK = pa.PO_ID_PROYECTO_FK               "+
                                                "    and po.PO_ID_ESTADO = ? " +
                                                "    and pa.PA_ID_ESTADO = ? ) d                                  ");

            query.setInteger(0, Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
            query.setInteger(1, Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION);
            query.setBoolean(2, true);
            query.setBoolean(3, true);
            query.setInteger(4, Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION);
            query.setString(5, strCorreoUsuario);
            query.setInteger(6, Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION);
            query.setInteger(7, Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
            query.setInteger(8, Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION);
            query.setBoolean(9, true);
            query.setBoolean(10, true);
            query.setBoolean(11, true);
            query.setString(12, strCorreoUsuario);
            query.setInteger(13, Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION);
            query.setInteger(14, Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION);
            
            
            lstEvaluacion = query.list();
                        
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        return lstEvaluacion;
        
    }
    
    private void iniciaOperacion() throws HibernateException 
    { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  

    private void manejaExcepcion(HibernateException he) throws HibernateException 
    { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 

    public List iniciarProyecto(List<Participante> lstParticipante, List<RedEvaluacion> lstRedEvaluacion, List<Cuestionario> lstCuestionario, List<CuestionarioEvaluado> lstCuestionarioEvaluado, List<Relacion> lstRelacion, List<RelacionParticipante> lstRelacionParticipante) {
        
        List<String[]> lstNotificacion = new ArrayList();
        
        try { 
            
            Map cuestionarios = cargaCuestionario(lstCuestionario);
            Map evaluadosCuestionario = cargaEvaluadosCuestionario(lstParticipante, lstCuestionarioEvaluado, cuestionarios);
            Map evaluadorEvaluado = cargaEvaluadorEvaluado(lstRelacionParticipante, evaluadosCuestionario);
            Map relacionEvaluado = cargaRelacionEvaluado(lstRelacion, lstRelacionParticipante, evaluadosCuestionario);
            Map cuestionarioEvaluado = cargaCuestionarioEvaluado(lstCuestionarioEvaluado, evaluadosCuestionario);
            
            iniciaOperacion(); 
            
            /* ACTUALIZO EL ESTADO A LOS EVALUADOS */
            for(Participante objParticipante : lstParticipante){
                if(objParticipante.getPaInRedCargada().equals(Boolean.TRUE) &&
                   objParticipante.getPaInRedVerificada().equals(Boolean.TRUE) &&
                   objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)){
                    if(evaluadosCuestionario.containsKey(objParticipante.getPaIdParticipantePk())){
                        
                        //if(objParticipante.getPaInAutoevaluar().equals(Boolean.TRUE)){
                            objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION);
                        //}else{
                        //    objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
                        //}
                        sesion.update(objParticipante);
                        if(objParticipante.getPaInAutoevaluar().equals(Boolean.TRUE)){
                            String[] str = new String[2];
                            str[0]=objParticipante.getPaTxCorreo();
                            str[1]=objParticipante.getPaTxDescripcion();
                            lstNotificacion.add(str);
                        }
                    }
                }
            }
        
            /* ACTUALIZO EL ESTADO A LOS EVALUADORES */
            for(RedEvaluacion objRedEvaluacion : lstRedEvaluacion){
                if(objRedEvaluacion.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_EN_PARAMETRIZACION)){ 
                    if(evaluadorEvaluado.containsKey(objRedEvaluacion.getReIdParticipantePk())){
                        objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION);
                        sesion.update(objRedEvaluacion);
                            String[] str = new String[2];
                            str[0]=objRedEvaluacion.getReTxCorreo();
                            str[1]=objRedEvaluacion.getReTxDescripcion();
                        lstNotificacion.add(str);
                    }
                }
            }

            /* ACTUALIZO EL ESTADO A LAS RELACIONES */
            for(Relacion objRelacion : lstRelacion){
                if(objRelacion.getReIdEstado().equals(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO)){
                    if(relacionEvaluado.containsKey(objRelacion.getReIdRelacionPk())){
                        objRelacion.setReIdEstado(Constantes.INT_ET_ESTADO_RELACION_EN_EJECUCION);
                        sesion.update(objRelacion);
                    }
                }
            }

            /* ACTUALIZO CUESTIONARIO/EVALUADO */
            for(CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado){
                Cuestionario objCuestionario = (Cuestionario) cuestionarios.get(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk());
                if(objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO) ||
                   objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION)){
                    objCuestionarioEvaluado.setCeIdEstado(Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION); 
                    sesion.update(objCuestionarioEvaluado);
                }
            }
            
            /* ACTUALIZO EL ESTADO A LOS CUESTIONARIOS */
            for(Cuestionario objCuestionario : lstCuestionario){
                if(objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO)){
                    if(cuestionarioEvaluado.containsKey(objCuestionario.getCuIdCuestionarioPk())){
                        objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION);
                        sesion.update(objCuestionario);
                    }
                }
            }
            
            
            /* CREA CUENTAS EN LA TABLA USUARIOS */
            
            //List<Usuario> lstUsuariosCreados = new ArrayList<>();
            UsuarioDAO objUsuarioDAO = new UsuarioDAO();
            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
            Usuario obtenUsuario = objUsuarioDAO.obtenUsuario(objUsuarioInfo.getIntUsuarioPk(), sesion);

            List<Usuario> lstUsuarios = objUsuarioDAO.obtenListaUsuario(sesion);

            Map hUsuarios = new HashMap();

            for (Usuario objUsuario : lstUsuarios){
                hUsuarios.put(objUsuario.getUsIdMail(), objUsuario);
            }

            /* VERIFICAMOS SI ALGUNO TIENE CUENTAS CREADAS */
            Iterator it = lstNotificacion.iterator();

            EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

            Map hUsuariosCreados = new HashMap();
            
            while(it.hasNext()){

                String[] str = (String[]) it.next();

                if(hUsuarios.containsKey(str[0])){ //DEBE SER ACTUALIZADO SI ESTA ANULADO

                    Usuario objUsuarioRepetido = (Usuario) hUsuarios.get(str[0]);

                    if(!objUsuarioRepetido.getUsIdEstado().equals(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO)){
                        objUsuarioRepetido.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);

                        sesion.update(objUsuarioRepetido);

                        
                    }
                    //lstUsuariosCreados.add(objUsuarioRepetido);
                }else{ //ES UN NUEVO USUARIO

                    if(!hUsuariosCreados.containsKey(str[0])){
                        Usuario objUsuarioNuevo = new Usuario();

                        objUsuarioNuevo.setUsBlImagenEmpresa(obtenUsuario.getUsBlImagenEmpresa());
                        objUsuarioNuevo.setUsTxDescripcionEmpresa(obtenUsuario.getUsTxDescripcionEmpresa());
                        objUsuarioNuevo.setUbigeo(obtenUsuario.getUbigeo());
                        objUsuarioNuevo.setUsIdMail(str[0]);
                        objUsuarioNuevo.setUsTxNombreRazonsocial(str[1]);
                        objUsuarioNuevo.setUsTxContrasenia(objEncryptDecrypt.encrypt(Utilitarios.generarClave()));
                        objUsuarioNuevo.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);
                        objUsuarioNuevo.setUsIdTipoCuenta(Constantes.INT_ET_TIPO_USUARIO_USUARIO);
                        objUsuarioNuevo.setUsFeNacimiento(null);
                        objUsuarioNuevo.setUsTxDocumento(null);
                        objUsuarioNuevo.setUsIdTipoDocumento(null);
                        objUsuarioNuevo.setUsFeRegistro(new Date());

                        sesion.save(objUsuarioNuevo); 

                        hUsuariosCreados.put(str[0], objUsuarioNuevo);
                        //lstUsuariosCreados.add(objUsuarioNuevo);
                    }
                }

            }
            
            
            //NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
            //objNotificacionesDAO.guardaNotificacionesEvaluadores(lstUsuariosCreados, sesion);
            
            tx.commit(); 
            
        } catch (HibernateException he){ 
            log.error(he);
            manejaExcepcion(he); 
            lstNotificacion.clear();
        } catch (Exception ex){ 
            log.error(ex);
            tx.rollback();
            lstNotificacion.clear();
        } finally { 
            sesion.close(); 
        } 
        
        return lstNotificacion;

    }

    private Map cargaEvaluadosCuestionario(List<Participante> lstParticipante, List<CuestionarioEvaluado> lstCuestionarioEvaluado, Map cuestionarios) {
        
        Map map = new HashMap();
        
        for(Participante objParticipante : lstParticipante){
        
            if( objParticipante.getPaInRedCargada().equals(Boolean.TRUE) &&
                objParticipante.getPaInRedVerificada().equals(Boolean.TRUE) &&
                objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)){
            
                for(CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado){
                
                    if(objCuestionarioEvaluado.getId().getPaIdParticipanteFk().equals(objParticipante.getPaIdParticipantePk())){
                        
                        Cuestionario objCuestionario = (Cuestionario) cuestionarios.get(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk());
                        
                        if(objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO) || 
                           objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION)){
                            map.put(objParticipante.getPaIdParticipantePk(), objParticipante);
                        }
                    }
                    
                }
                
            }
            
        }
    
        return map;
    
    }
    
    private Map cargaCuestionario(List<Cuestionario> lstCuestionario) {
        
        Map map = new HashMap();
        
        for(Cuestionario objCuestionario : lstCuestionario){
            Cuestionario objCuestionarioN = objCuestionario; 
            map.put(objCuestionario.getCuIdCuestionarioPk(), objCuestionarioN);
        }
    
        return map;
    }

    private Map cargaRelacionEvaluado(List<Relacion> lstRelacion, List<RelacionParticipante> lstRelacionParticipante, Map evaluadosCuestionario) {

        Map map = new HashMap();
        
        for(Relacion objRelacion : lstRelacion){
            for(RelacionParticipante objRelacionParticipante : lstRelacionParticipante){
                if(objRelacionParticipante.getId().getReIdRelacionFk().equals(objRelacion.getReIdRelacionPk())){
                    if(evaluadosCuestionario.containsKey(objRelacionParticipante.getId().getPaIdParticipanteFk())){
                        if(!map.containsKey(objRelacion.getReIdRelacionPk())){
                            map.put(objRelacion.getReIdRelacionPk(), objRelacion);
                        }
                    }
                }
            }
        }
    
        return map;
        
    }

    private Map cargaEvaluadorEvaluado(List<RelacionParticipante> lstRelacionParticipante, Map evaluadosCuestionario) {

        Map map = new HashMap();
        
        for(RelacionParticipante objRelacionParticipante : lstRelacionParticipante){
            if(evaluadosCuestionario.containsKey(objRelacionParticipante.getId().getPaIdParticipanteFk())){
                if(!map.containsKey(objRelacionParticipante.getId().getReIdParticipanteFk())){
                    map.put(objRelacionParticipante.getId().getReIdParticipanteFk(), null);
                }
            }
        }
    
        return map;
        
    }

    private Map cargaCuestionarioEvaluado(List<CuestionarioEvaluado> lstCuestionarioEvaluado, Map evaluadosCuestionario) {
        
        Map map = new HashMap();
        
        for(CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado){
            if(evaluadosCuestionario.containsKey(objCuestionarioEvaluado.getId().getPaIdParticipanteFk())){
                if(!map.containsKey(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk())){
                    map.put(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk(), null);
                }
            }
        }
    
        return map;
    }
    
    public boolean terminarProyecto(Integer intIdProyecto){
    
        boolean correcto = true;

        try {
            
            iniciaOperacion();
            
            //Query query1 = sesion.createSQLQuery("update relacion_participante set RP_ID_ESTADO = 80 where PA_ID_PARTICIPANTE_FK in (select PA_ID_PARTICIPANTE_PK from participante where PO_ID_PROYECTO_FK = ? ) ");
            //Query query5 = sesion.createSQLQuery("update cuestionario_evaluado set CE_ID_ESTADO = 76 where PO_ID_PROYECTO_FK = ? ");
            Query query2 = sesion.createSQLQuery("update red_evaluacion set re_id_estado = 76 where PO_ID_PROYECTO_FK = ? ");
            Query query3 = sesion.createSQLQuery("update participante set pa_id_Estado = 73 where PO_ID_PROYECTO_FK = ? ");
            Query query4 = sesion.createSQLQuery("update proyecto set PO_ID_ESTADO = 37 where PO_ID_PROYECTO_PK = ? ");
            
/*select * from cuestionario_evaluado; */
/*select * from cuestionario;*/
/*select * from relacion;*/

            //query1.setInteger(0, intIdProyecto);
            query2.setInteger(0, intIdProyecto);
            query3.setInteger(0, intIdProyecto);
            query4.setInteger(0, intIdProyecto);
            
            //query1.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            query4.executeUpdate();
            
            tx.commit(); 
            
        } catch (Exception e){
            log.error(e);
            tx.rollback();
            correcto = false;
        } finally { 
            sesion.close(); 
        }  
        return correcto;
    
    
    
    }   
    
}