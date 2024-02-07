package com.jaio360.dao;

import com.jaio360.domain.DatosReporte;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Componente;
import com.jaio360.orm.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComponenteDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    public ComponenteDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public boolean guardaComponente(Componente componente) throws HibernateException {
        boolean proceso = false;

        try {
            iniciaOperacion();
            sesion.save(componente);
            tx.commit();
            proceso = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return proceso;
    }

    public boolean guardaComponentes(Componente componente) throws HibernateException {
        boolean proceso = false;

        try {
            iniciaOperacion();

            Query query = sesion.createSQLQuery("INSERT INTO componente ("
                    + "CO_ID_COMPONENTE_PK, "
                    + "CU_ID_CUESTIONARIO_FK, "
                    + "CO_ID_COMPONENTE_REF_FK,"
                    + "CO_ID_TIPO_COMPONENTE,"
                    + "CO_TX_DESCRIPCION) "
                    + "VALUES ("
                    + "?,?,null,?,?) ");
            query.setInteger(0, componente.getCoIdComponentePk());
            query.setInteger(1, componente.getCuestionario().getCuIdCuestionarioPk());
            query.setInteger(2, componente.getCoIdTipoComponente());
            query.setString(3, componente.getCoTxDescripcion());
            query.executeUpdate();

            for (Componente objComponente : componente.getStrcomponentes()) {

                query = sesion.createSQLQuery("INSERT INTO componente ("
                        + "CO_ID_COMPONENTE_PK, "
                        + "CU_ID_CUESTIONARIO_FK, "
                        + "CO_ID_COMPONENTE_REF_FK,"
                        + "CO_ID_TIPO_COMPONENTE,"
                        + "CO_TX_DESCRIPCION) "
                        + "VALUES ("
                        + "?,?,?,?,?) ");
                query.setInteger(0, objComponente.getCoIdComponentePk());
                query.setInteger(1, objComponente.getCuestionario().getCuIdCuestionarioPk());
                query.setInteger(2, componente.getCoIdComponentePk());
                query.setInteger(3, objComponente.getCoIdTipoComponente());
                query.setString(4, objComponente.getCoTxDescripcion());
                query.executeUpdate();

            }

            tx.commit();
            proceso = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return proceso;
    }

    public boolean guardaComponente2(Componente componente) throws HibernateException {
        boolean proceso = false;

        try {
            iniciaOperacion();
            sesion.saveOrUpdate(componente);
            tx.commit();
            proceso = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return proceso;
    }

    public boolean actualizaComponente(Componente componente) throws HibernateException {

        boolean flag = false;
        try {
            iniciaOperacion();
            sesion.update(componente);
            tx.commit();
            flag = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }
        return flag;
    }

    public boolean eliminaComponente(Componente componente) throws HibernateException {
        boolean proceso = false;
        try {
            iniciaOperacion();
            sesion.delete(componente);
            tx.commit();
            proceso = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
        return proceso;
    }

    public String obtieneMaxPosicion(Cuestionario cuestionario, Integer tipoComponente) throws HibernateException {

        try {
            iniciaOperacion();
            org.hibernate.Query qsql = sesion.createQuery("select max(co.posicion) from Componente co where co.cuestionario.cuIdCuestionarioPk = :idCuestionario and co.coIdTipoComponente = :tipoComponente ");
            qsql.setInteger("idCuestionario", cuestionario.getCuIdCuestionarioPk());
            qsql.setInteger("tipoComponente", tipoComponente);

            Object obj = qsql.uniqueResult();

            if (obj != null) {
                return obj.toString();
            } else {
                return "0";
            }

        } finally {
            sesion.close();
        }

    }

    public Componente elminaComponenteCuestionario(Cuestionario cuestionario) throws HibernateException {
        Componente componente = null;
        try {
            iniciaOperacion();
            org.hibernate.Query qsql = sesion.createQuery("delete from Componente co where co.cuestionario.cuIdCuestionarioPk = :idCuestionario ");
            qsql.setInteger("idCuestionario", cuestionario.getCuIdCuestionarioPk());
            qsql.executeUpdate();
            tx.commit();
        } finally {
            sesion.close();
        }

        return componente;
    }

    public List<Componente> obtenListaComponente() throws HibernateException {
        List<Componente> listaComponente = null;

        try {
            iniciaOperacion();
            listaComponente = sesion.createQuery("from Componente").list();
        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<Componente> listaComponenteTipo(Componente componente) throws HibernateException {
        List<Componente> listaComponente = null;

        try {
            iniciaOperacion();
            org.hibernate.Query sql = sesion.createQuery("from Componente co where co.cuestionario.cuIdCuestionarioPk = :idCuestionario and co.coIdTipoComponente= :idTipo order by co.coIdComponentePk");
            sql.setInteger("idCuestionario", componente.getCuestionario().getCuIdCuestionarioPk());
            sql.setInteger("idTipo", componente.getCoIdTipoComponente());
            listaComponente = sql.list();
        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<Componente> listaComponenteXCuestionario(Integer intIdCuestionario) throws HibernateException {
        List<Componente> listaComponente = null;

        try {
            iniciaOperacion();
            org.hibernate.Query sql = sesion.createQuery("from Componente co where co.cuestionario.cuIdCuestionarioPk = ?");
            sql.setInteger(0, intIdCuestionario);

            listaComponente = sql.list();
        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<Componente> listaComponenteRed(Componente componente) throws HibernateException {
        List<Componente> listaComponente = null;

        try {
            iniciaOperacion();
            org.hibernate.Query sql = sesion.createQuery("from Componente co where co.componente.coIdComponentePk = :idComponente order by co.coIdComponentePk");
            sql.setInteger("idComponente", componente.getCoIdComponentePk());

            listaComponente = sql.list();
        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    public List<Componente> listaComponenteProyectoTipoOrdenado(Integer intIdProyecto, Integer idCuestionario, Integer INT_ET_TIPO_COMPONENTE, Componente objCategoria, Integer intEvaluadoPk) {
        List<Componente> listaComponente = new ArrayList<>();
        List lista = new ArrayList();

        try {
            iniciaOperacion();

            String strQuery
                    = " SELECT COMPONENTE_ID,                                                                          "
                    + "        TABLA.RELACION,                                                                         "
                    + "        DESCRIPCION,                                                                            "
                    + "        ifnull(AVG(TABLA.MEDIDA),0),                                                                      "
                    + "        COUNT(DISTINCT TABLA.EVALUADOR)                                                         "
                    + "        FROM                                                                                    "
                    + "       (select cat.CO_ID_COMPONENTE_PK AS COMPONENTE_ID,                                        "
                    + "			   cat.CO_TX_DESCRIPCION as DESCRIPCION,                                             "
                    + "               'PROM' AS RELACION,                                                              "
                    + "               ifnull(res.RE_ID_PARTICIPANTE_FK,res.PA_ID_PARTICIPANTE_FK||'EVA') AS EVALUADOR, "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                                    "
                    + "               ifnull(RE_ID_PARTICIPANTE_FK,res.PA_ID_PARTICIPANTE_FK||'EVA') AS CANTIDAD       "
                    + "          from proyecto po,                                                                     "
                    + "               resultado res,                                                                   "
                    + "               metrica me, "
                    + "               detalle_metrica dm,                                                              "
                    + "               componente cop,                                                                  "
                    + "			   componente cat                                                        "
                    + "         where po.PO_ID_PROYECTO_pK = :idProyecto                                                       "
                    + "           and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK					 "
                    + "           and me.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK "
                    + "           and dm.ME_ID_METRICA_FK = me.ME_ID_METRICA_PK "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                         "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                                "
                    + "	    and cat.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                     "
                    + //" and res.RE_ID_PARTICIPANTE_FK is not null " +
                    "	    and cat.CO_ID_TIPO_COMPONENTE = :idTipo                                            "
                    + "           and cat.CU_ID_CUESTIONARIO_FK = :idCuestionario                                      "
                    + "           and res.PA_ID_PARTICIPANTE_FK = :idParticipant ";

            if (objCategoria != null) {
                strQuery += " and cat.CO_ID_COMPONENTE_PK = :idCategoria ";
            }

            strQuery
                    += " ) TABLA GROUP BY COMPONENTE_ID,DESCRIPCION, TABLA.RELACION  order by 4 desc                    ";

            Query sql = sesion.createSQLQuery(strQuery);

            sql.setInteger("idProyecto", intIdProyecto);
            sql.setInteger("idCuestionario", idCuestionario);
            sql.setInteger("idTipo", INT_ET_TIPO_COMPONENTE);
            sql.setInteger("idParticipant", intEvaluadoPk);

            if (objCategoria != null) {
                sql.setInteger("idCategoria", objCategoria.getCoIdComponentePk());
            }

            lista = sql.list();

            if (!lista.isEmpty()) {
                Iterator itLista = lista.iterator();
                Componente objComponente;
                while (itLista.hasNext()) {
                    Object obj[] = (Object[]) itLista.next();
                    objComponente = new Componente();
                    objComponente.setCoIdComponentePk(Integer.parseInt(obj[0].toString()));
                    objComponente.setCoTxDescripcion(obj[2].toString());
                    objComponente.setCoNrPuntajeObtenido(new BigDecimal(obj[3].toString()));
                    listaComponente.add(objComponente);
                }
            }

            lista.clear();

        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<Componente> listaComponenteProyectoTipoOrdenadoForDashboard(Integer intIdProyecto, Integer idCuestionario, Integer INT_ET_TIPO_COMPONENTE, Componente objCategoria, Integer intEvaluadoPk) {
        List<Componente> listaComponente = new ArrayList<>();
        List lista = new ArrayList();

        try {
            iniciaOperacion();

            String strQuery
                    = "select "
                    + "	COMPONENTE_ID, "
                    + "	TABLA.RELACION, "
                    + "	DESCRIPCION, "
                    + "	IFNULL(AVG(TABLA.MEDIDA), 0), "
                    + "	CAT_PUN_REQ, "
                    + "	CAT_PUN_MIN "
                    + "from "
                    + "	( "
                    + "	select "
                    + "		CAT.CO_ID_COMPONENTE_PK as COMPONENTE_ID, "
                    + "		CAT.CO_TX_DESCRIPCION as DESCRIPCION, "
                    + "		'PROM' as RELACION, "
                    + "		IFNULL(RES.RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as EVALUADOR, "
                    + "		DM.DE_NU_ORDEN + 1 as MEDIDA, "
                    + "		IFNULL(RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as CANTIDAD, "
                    + "		CAT.CO_NR_PUNTAJE_REQUERIDO as CAT_PUN_REQ, "
                    + "		CAT.CO_NR_PUNTAJE_MINIMO_REQUERIDO as CAT_PUN_MIN, "
                    + "		COP.CO_NR_PUNTAJE_REQUERIDO as PRE_PUN_REQ, "
                    + "		COP.CO_NR_PUNTAJE_MINIMO_REQUERIDO as PRE_PUN_MIN "
                    + "	from "
                    + "		proyecto PO, "
                    + "		resultado RES, "
                    + "		metrica ME, "
                    + "		detalle_metrica DM, "
                    + "		componente COP, "
                    + "		componente CAT "
                    + "	where "
                    + "		PO.PO_ID_PROYECTO_PK = :idProyecto "
                    + "		and RES.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and ME.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and DM.ME_ID_METRICA_FK = ME.ME_ID_METRICA_PK "
                    + "		and DM.DE_ID_DETALLE_ESCALA_PK = RES.DE_ID_DETALLE_ESCALA_FK "
                    + "		and COP.CO_ID_COMPONENTE_PK = RES.CO_ID_COMPONENTE_FK "
                    + "		and CAT.CO_ID_COMPONENTE_PK = COP.CO_ID_COMPONENTE_REF_FK "
                    + "		and RES.RE_ID_PARTICIPANTE_FK is not null "
                    + "		and CAT.CO_ID_TIPO_COMPONENTE = :idTipo "
                    + "		and RES.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + "union all "
                    + "	select "
                    + "		-1 as COMPONENTE_ID, "
                    + "		'General' as DESCRIPCION, "
                    + "		'General' as RELACION, "
                    + "		IFNULL(RES.RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as EVALUADOR, "
                    + "		AVG(DM.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		IFNULL(RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as CANTIDAD, "
                    + "		-1 as CAT_PUN_REQ, "
                    + "		-1 as CAT_PUN_MIN, "
                    + "		-1 as PRE_PUN_REQ, "
                    + "		-1 as PRE_PUN_MIN "
                    + "	from "
                    + "		proyecto PO, "
                    + "		resultado RES, "
                    + "		metrica ME, "
                    + "		detalle_metrica DM, "
                    + "		componente COP, "
                    + "		componente CAT "
                    + "	where "
                    + "		PO.PO_ID_PROYECTO_PK = :idProyecto "
                    + "		and RES.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and ME.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and DM.ME_ID_METRICA_FK = ME.ME_ID_METRICA_PK "
                    + "		and DM.DE_ID_DETALLE_ESCALA_PK = RES.DE_ID_DETALLE_ESCALA_FK "
                    + "		and COP.CO_ID_COMPONENTE_PK = RES.CO_ID_COMPONENTE_FK "
                    + "		and CAT.CO_ID_COMPONENTE_PK = COP.CO_ID_COMPONENTE_REF_FK "
                    + "		and RES.RE_ID_PARTICIPANTE_FK is not null "
                    + "		and CAT.CO_ID_TIPO_COMPONENTE = :idTipo "
                    + "		and RES.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + "	group by "
                    + "		COMPONENTE_ID, "
                    + "		DESCRIPCION	 "
                    + "		) TABLA "
                    + "group by "
                    + "	COMPONENTE_ID, "
                    + "	DESCRIPCION, "
                    + "	TABLA.RELACION "
                    + "order by "
                    + "	COMPONENTE_ID";

            Query sql = sesion.createSQLQuery(strQuery);

            sql.setInteger("idProyecto", intIdProyecto);
            //sql.setInteger("idCuestionario", idCuestionario);
            sql.setInteger("idTipo", INT_ET_TIPO_COMPONENTE);
            sql.setInteger("idParticipant", intEvaluadoPk);

            if (objCategoria != null) {
                sql.setInteger("idCategoria", objCategoria.getCoIdComponentePk());
            }

            lista = sql.list();

            if (!lista.isEmpty()) {
                Iterator itLista = lista.iterator();
                Componente objComponente;
                while (itLista.hasNext()) {
                    Object obj[] = (Object[]) itLista.next();
                    objComponente = new Componente();
                    objComponente.setCoIdComponentePk(Integer.parseInt(obj[0].toString()));
                    objComponente.setCoTxDescripcion(obj[2].toString());
                    objComponente.setCoNrPuntajeObtenido(new BigDecimal(obj[3].toString()));
                    objComponente.setCoNrPuntajeMinimoRequerido(new BigDecimal(obj[5].toString()));
                    objComponente.setCoNrPuntajeRequerido(new BigDecimal(obj[4].toString()));
                    listaComponente.add(objComponente);
                }
            }

            lista.clear();

        } finally {
            sesion.close();
        }

        return listaComponente;

    }

    public List<Componente> listaComponenteByProjectOrdered(Integer intIdProyecto, Integer idCuestionario, Integer INT_ET_TIPO_COMPONENTE, Componente objCategoria, Integer intEvaluadoPk) {
        List<Componente> listaComponente = new ArrayList<>();
        List lista = new ArrayList();

        try {
            iniciaOperacion();

            String strQuery
                    = "select "
                    + "	COMPONENTE_ID, "
                    + "	TABLA.RELACION, "
                    + "	DESCRIPCION, "
                    + "	IFNULL(AVG(TABLA.MEDIDA), 0), "
                    + "	CAT_PUN_REQ, "
                    + "	CAT_PUN_MIN "
                    + "from "
                    + "	( "
                    + "	select "
                    + "		-1 as COMPONENTE_ID, "
                    + "		'General' as DESCRIPCION, "
                    + "		'General' as RELACION, "
                    + "		IFNULL(RES.RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as EVALUADOR, "
                    + "		AVG(DM.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		IFNULL(RE_ID_PARTICIPANTE_FK, RES.PA_ID_PARTICIPANTE_FK || 'EVA') as CANTIDAD, "
                    + "		-1 as CAT_PUN_REQ, "
                    + "		-1 as CAT_PUN_MIN, "
                    + "		-1 as PRE_PUN_REQ, "
                    + "		-1 as PRE_PUN_MIN "
                    + "	from "
                    + "		proyecto PO, "
                    + "		resultado RES, "
                    + "		metrica ME, "
                    + "		detalle_metrica DM, "
                    + "		componente COP, "
                    + "		componente CAT "
                    + "	where "
                    + "		PO.PO_ID_PROYECTO_PK = :idProyecto "
                    + "		and RES.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and ME.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "		and DM.ME_ID_METRICA_FK = ME.ME_ID_METRICA_PK "
                    + "		and DM.DE_ID_DETALLE_ESCALA_PK = RES.DE_ID_DETALLE_ESCALA_FK "
                    + "		and COP.CO_ID_COMPONENTE_PK = RES.CO_ID_COMPONENTE_FK "
                    + "		and CAT.CO_ID_COMPONENTE_PK = COP.CO_ID_COMPONENTE_REF_FK "
                    + "		and RES.RE_ID_PARTICIPANTE_FK is not null "
                    + "		and CAT.CO_ID_TIPO_COMPONENTE = :idTipo "
                    + "		and RES.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + "	group by "
                    + "		COMPONENTE_ID, "
                    + "		DESCRIPCION	 "
                    + "		) TABLA "
                    + "group by "
                    + "	COMPONENTE_ID, "
                    + "	DESCRIPCION, "
                    + "	TABLA.RELACION "
                    + "order by "
                    + "	COMPONENTE_ID";

            Query sql = sesion.createSQLQuery(strQuery);

            sql.setInteger("idProyecto", intIdProyecto);
            //sql.setInteger("idCuestionario", idCuestionario);
            sql.setInteger("idTipo", INT_ET_TIPO_COMPONENTE);
            sql.setInteger("idParticipant", intEvaluadoPk);

            if (objCategoria != null) {
                sql.setInteger("idCategoria", objCategoria.getCoIdComponentePk());
            }

            lista = sql.list();

            if (!lista.isEmpty()) {
                Iterator itLista = lista.iterator();
                Componente objComponente;
                while (itLista.hasNext()) {
                    Object obj[] = (Object[]) itLista.next();
                    objComponente = new Componente();
                    objComponente.setCoIdComponentePk(Integer.parseInt(obj[0].toString()));
                    objComponente.setCoTxDescripcion(obj[2].toString());
                    objComponente.setCoNrPuntajeObtenido(new BigDecimal(obj[3].toString()));
                    objComponente.setCoNrPuntajeMinimoRequerido(new BigDecimal(obj[5].toString()));
                    objComponente.setCoNrPuntajeRequerido(new BigDecimal(obj[4].toString()));
                    listaComponente.add(objComponente);
                }
            }

            lista.clear();

        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<Componente> listaComponenteProyectoTipo(Integer intIdProyecto, Integer intIdCuestionario, Integer INT_ET_TIPO_COMPONENTE, Componente objCategoria) {
        List<Componente> listaComponente = null;

        try {
            iniciaOperacion();

            String strQuery = "from Componente co where co.cuestionario.proyecto.poIdProyectoPk = :idProyecto and co.cuestionario.cuIdCuestionarioPk = :idCuestionario and co.coIdTipoComponente= :idTipo ";

            if (objCategoria != null) {
                strQuery += " and co.componente.coIdComponentePk = :idCategoria ";
            }

            strQuery += " order by co.coIdComponentePk ";

            Query sql = sesion.createQuery(strQuery);

            sql.setInteger("idProyecto", intIdProyecto);
            sql.setInteger("idCuestionario", intIdCuestionario);
            sql.setInteger("idTipo", INT_ET_TIPO_COMPONENTE);

            if (objCategoria != null) {
                sql.setInteger("idCategoria", objCategoria.getCoIdComponentePk());
            }

            listaComponente = sql.list();
        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public Componente obtenComponente(long idComponente) throws HibernateException {
        Componente componente = null;
        try {
            iniciaOperacion();
            componente = (Componente) sesion.get(Componente.class, (int) idComponente);
        } finally {
            sesion.close();
        }

        return componente;
    }

}
