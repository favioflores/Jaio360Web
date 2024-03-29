package com.jaio360.dao;

import com.jaio360.domain.ComentarioBean;
import com.jaio360.domain.DatosReporte;
import com.jaio360.domain.EvaluacionesXEjecutar;
import com.jaio360.domain.PreguntaAbiertaBean;
import com.jaio360.domain.PreguntaCerradaBean;
import com.jaio360.orm.Componente;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.orm.Resultado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ResultadoDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Logger log = Logger.getLogger(ResultadoDAO.class);

    public long guardaResultado(Resultado resultado) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(resultado));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaResultado(Resultado resultado) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(resultado);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaResultado(Resultado resultado) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(resultado);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Resultado obtenResultado(long idResultado) throws HibernateException {
        Resultado resultado = null;
        try {
            iniciaOperacion();
            resultado = (Resultado) sesion.get(Resultado.class, (int) idResultado);
        } finally {
            sesion.close();
        }

        return resultado;
    }

    public List<Resultado> obtenListaResultado() throws HibernateException {
        List<Resultado> listaResultado = null;

        try {
            iniciaOperacion();
            listaResultado = sesion.createQuery("from Resultado").list();
        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    public List obtenListaResultadoGeneral(Integer intidProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select distinct * from (                                                                 "
                    + " select r.PA_ID_PARTICIPANTE_FK, r.RE_ID_PARTICIPANTE_FK, r.RE_ID_RELACION_FK    "
                    + "   from resultado r                                                              "
                    + "  where r.PO_ID_PROYECTO_FK = ?                                                  "
                    + "  UNION ALL                                                                      "
                    + " select rp.PA_ID_PARTICIPANTE_FK, rp.RE_ID_PARTICIPANTE_FK, rp.RE_ID_RELACION_FK "
                    + "   from relacion_participante rp, participante pa                                "
                    + "  where rp.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK                      "
                    + "    and rp.RP_ID_ESTADO = ?                                                      "
                    + "    and pa.PO_ID_PROYECTO_FK = ?                                             "
                    + " union all "
                    + "	select 	pa.PA_ID_PARTICIPANTE_PK, null, null "
                    + "	from participante pa "
                    + "	where pa.PO_ID_PROYECTO_FK = ? "
                    + "		and pa.PA_IN_AUTOEVALUAR = true "
                    + "		and pa.PA_ID_ESTADO = ? "
                    + " and exists (select 1 "
                    + "		              from resultado rr "
                    + "				     where rr.PO_ID_PROYECTO_FK = pa.PO_ID_PROYECTO_FK "
                    + "				       and rr.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK "
                    + "				       and rr.RE_ID_RELACION_FK is NULL "
                    + "				       and rr.RE_ID_PARTICIPANTE_FK is null ) "
                    + "               ) d  ");

            /*falta agregar al participante que termina su evaluacion pero no responde nada */
            query.setInteger(0, intidProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO);
            query.setInteger(2, intidProyecto);
            query.setInteger(3, intidProyecto);
            query.setInteger(4, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List obtenListaTotalTerminadosXparticipante(Integer intidProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select g.participante, count(*) from ( "
                    + "select distinct * from (                                                                 "
                    + " select r.PA_ID_PARTICIPANTE_FK as participante, r.RE_ID_PARTICIPANTE_FK as relacion_participante, r.RE_ID_RELACION_FK as relacion "
                    + "   from resultado r                                                              "
                    + "  where r.PO_ID_PROYECTO_FK = ?                                                  "
                    + "  UNION ALL                                                                      "
                    + " select rp.PA_ID_PARTICIPANTE_FK, rp.RE_ID_PARTICIPANTE_FK, rp.RE_ID_RELACION_FK "
                    + "   from relacion_participante rp, participante pa                                "
                    + "  where rp.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK                      "
                    + "    and rp.RP_ID_ESTADO = ?                                                      "
                    + "    and pa.PO_ID_PROYECTO_FK = ?                                             "
                    + " union all "
                    + "	select 	pa.PA_ID_PARTICIPANTE_PK, null, null "
                    + "	from participante pa "
                    + "	where pa.PO_ID_PROYECTO_FK = ? "
                    + "		and pa.PA_IN_AUTOEVALUAR = true "
                    + "		and pa.PA_ID_ESTADO = ? "
                    + " and exists (select 1 "
                    + "		              from resultado rr "
                    + "				     where rr.PO_ID_PROYECTO_FK = pa.PO_ID_PROYECTO_FK "
                    + "				       and rr.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK "
                    + "				       and rr.RE_ID_RELACION_FK is NULL "
                    + "				       and rr.RE_ID_PARTICIPANTE_FK is null )"
                    + "               ) d ) g group by participante ");

            /*falta agregar al participante que termina su evaluacion pero no responde nada */
            query.setInteger(0, intidProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO);
            query.setInteger(2, intidProyecto);
            query.setInteger(3, intidProyecto);
            query.setInteger(4, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List<Resultado> obtenListaResultado(Integer intIdComponente) throws HibernateException {

        List<Resultado> listaResultado = null;

        try {

            iniciaOperacion();

            Query query = sesion.createQuery("from Resultado r where r.componente.coIdComponentePk = ? ");

            query.setInteger(0, intIdComponente);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteUno(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT                                                                                           "
                    + "  TABLA.RELACION,                                                                                 "
                    + "  AVG(TABLA.MEDIDA),                                                                              "
                    + "  SUM(TABLA.CANTIDAD)                                                                             "
                    + "  FROM                                                                                            "
                    + "  (select rel.RE_TX_ABREVIATURA AS RELACION,                                                      "
                    + "  	   res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                                                 "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,                            "
                    + "         IF(RE_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         relacion rel,                                                                            "
                    + "         resultado res,                                                                           "
                    + "         detalle_metrica dm                                                                       "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                                             "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                            "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                                 "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                                              "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                                            "
                    + "  UNION ALL                                                                                       "
                    + "  select 'AUTO' AS RELACION,                                                                      "
                    + "  	   res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,                                                 "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,                            "
                    + "         IF(PA_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         resultado res                                                                            "
                    + "         left join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                                             "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                                              "
                    + "     AND res.RE_ID_RELACION_FK is null                                                            "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                                            "
                    + "     and res.RE_ID_PARTICIPANTE_FK is null                              "
                    + "   UNION ALL                                                            "
                    + "  select 'PROM' AS RELACION,                                             "
                    + "  	   res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                         "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,    "
                    + "         IF(RE_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD              "
                    + "    from proyecto po,                                                     "
                    + "         relacion rel,                                                    "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                     "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                    "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                    "
                    + ") TABLA GROUP BY TABLA.RELACION                         ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);
            query.setInteger(6, idProyecto);
            query.setInteger(7, objComponente.getCoIdComponentePk());
            query.setInteger(8, intEvaluadoPk);
            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteUnoWeighted(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT                                                                                           "
                    + "  TABLA.RELACION,                                                                                 "
                    + "  TABLA.MEDIDA,                                                                              "
                    + "  TABLA.CANTIDAD                                                                             "
                    + "  FROM                                                                                            "
                    + " (select rel.RE_TX_ABREVIATURA AS RELACION,                                                      "
                    + "  	     AVG(IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1)) AS MEDIDA,                            "
                    + "         ifNULL(SUM(IF(RE_ID_PARTICIPANTE_FK is null, 0, 1)),0) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         relacion rel,                                                                            "
                    + "         resultado res,                                                                           "
                    + "         detalle_metrica dm                                                                       "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                                             "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                            "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                                 "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                                              "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ? GROUP BY rel.RE_TX_ABREVIATURA          "
                    + "  UNION ALL                                                                                       "
                    + "  select 'AUTO' AS RELACION,                                                                      "
                    + "  	ifNULL(AVG(IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1)),0) AS MEDIDA,                            "
                    + "         ifNULL(SUM(IF(PA_ID_PARTICIPANTE_FK is null, 0, 1)),0) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         resultado res                                                                            "
                    + "         left join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                                             "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                                              "
                    + "     AND res.RE_ID_RELACION_FK is null                                                            "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                                            "
                    + "     and res.RE_ID_PARTICIPANTE_FK is null                              "
                    + "   UNION ALL          "
                    + "   SELECT 'PROM', "
                    + "   ifNULL(SUM(PROM.MEDIDA * PROM.PONDERADO / 100),0), "
                    + "   ifNULL(SUM(PROM.CANTIDAD),0) FROM "
                    + "  (select rel.RE_ID_RELACION_PK   AS RELACION,                                             "
                    + "  		 AVG(IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1)) AS MEDIDA,    "
                    + "         ifNULL(SUM(IF(RE_ID_PARTICIPANTE_FK is null, 0, 1)),0) AS CANTIDAD,"
                    + "         rel.RE_DE_PONDERACION AS PONDERADO"
                    + "    from proyecto po,                                                     "
                    + "         relacion rel,                                                    "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                     "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                    "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ? GROUP BY rel.RE_ID_RELACION_PK ) PROM                                "
                    + ") TABLA  ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);
            query.setInteger(6, idProyecto);
            query.setInteger(7, objComponente.getCoIdComponentePk());
            query.setInteger(8, intEvaluadoPk);
            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List getResultRelacionByCategoria(Integer idCategoria, Integer idParticipant) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    "select "
                    + "	TABLA.RELACION, "
                    + "	AVG(TABLA.MEDIDA), "
                    + "	SUM(TABLA.CANTIDAD) "
                    + "from "
                    + "	( "
                    + "	select "
                    + "		rel.RE_TX_ABREVIATURA as RELACION, "
                    + "		res.RE_ID_PARTICIPANTE_FK as EVALUADOR, "
                    + "		if(dm.DE_NU_ORDEN is null, "
                    + "		0, "
                    + "		dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		if(RE_ID_PARTICIPANTE_FK is null, "
                    + "		0, "
                    + "		1) as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		relacion rel, "
                    + "		resultado res, "
                    + "		detalle_metrica dm, "
                    + "		componente c "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK "
                    + "		and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK "
                    + "		and c.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		and c.CO_ID_COMPONENTE_REF_FK = :idCategoria "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + "union all "
                    + "	select "
                    + "		'AUTO' as RELACION, "
                    + "		res.PA_ID_PARTICIPANTE_FK as EVALUADOR, "
                    + "		if(dm.DE_NU_ORDEN is null, "
                    + "		0, "
                    + "		dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		if(PA_ID_PARTICIPANTE_FK is null, "
                    + "		0, "
                    + "		1) as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		componente c, "
                    + "		resultado res "
                    + "	left join detalle_metrica dm on "
                    + "		dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK "
                    + "		and c.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and c.CO_ID_COMPONENTE_REF_FK = :idCategoria "
                    + "		and res.RE_ID_RELACION_FK is null "
                    + "		and res.RE_ID_PARTICIPANTE_FK is null "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + " union all "
                    + "	select "
                    + "		'PROM' as RELACION, "
                    + "		res.RE_ID_PARTICIPANTE_FK as EVALUADOR, "
                    + "		if(dm.DE_NU_ORDEN is null, "
                    + "		0, "
                    + "		dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		if(RE_ID_PARTICIPANTE_FK is null, "
                    + "		0, "
                    + "		1) as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		relacion rel, "
                    + "		resultado res, "
                    + "		detalle_metrica dm, "
                    + "		componente c "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK "
                    + "		and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK "
                    + "		and c.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		and c.CO_ID_COMPONENTE_REF_FK = :idCategoria "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idParticipant "
                    + "                    ) TABLA "
                    + "group by "
                    + "	TABLA.RELACION ");

            query.setInteger("idProyecto", Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger("idCategoria", idCategoria);
            query.setInteger("idParticipant", idParticipant);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List obtenerResultadoXCategoria(Integer idCategoria) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT                                                                                           "
                    + "  AVG(TABLA.MEDIDA),                                                                              "
                    + "  SUM(TABLA.CANTIDAD)                                                                             "
                    + "  FROM                                                                                            "
                    + "  (select rel.RE_TX_ABREVIATURA AS RELACION,                                                      "
                    + "  	   res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                                                 "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,                            "
                    + "         IF(RE_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         relacion rel,                                                                            "
                    + "         resultado res,                                                                           "
                    + "         detalle_metrica dm,                                                                       "
                    + "         componente c                                                                       "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                                             "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                            "
                    + "     and c.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                                            "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                                 "
                    + "     and c.CO_ID_COMPONENTE_REF_FK = ?                                                              "
                    + "  UNION ALL                                                                                       "
                    + "  select 'AUTO' AS RELACION,                                                                      "
                    + "  	   res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,                                                 "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,                            "
                    + "         IF(PA_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD                                      "
                    + "    from proyecto po,                                                                             "
                    + "         componente c,                                                                             "
                    + "         resultado res                                                                            "
                    + "         left join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                                                 "
                    + "     and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                                             "
                    + "     and c.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                                             "
                    + "     and c.CO_ID_COMPONENTE_REF_FK = ?                                                              "
                    + "     AND res.RE_ID_RELACION_FK is null                                                            "
                    + "     and res.RE_ID_PARTICIPANTE_FK is null                              "
                    + ") TABLA  ");

            query.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger(1, idCategoria);
            query.setInteger(2, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger(3, idCategoria);
            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List resultadoPorRelacionYCategoria(Integer intEvaluadoPk) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    "select "
                    + "	TABLA.RELACION, "
                    + "	TABLA.CATEGORIA, "
                    + "	AVG(TABLA.MEDIDA), "
                    + "	SUM(TABLA.CANTIDAD) "
                    + "from "
                    + "	( "
                    + "	select "
                    + "		rel.RE_TX_ABREVIATURA as RELACION, "
                    + "		cat.CO_ID_COMPONENTE_PK as CATEGORIA, "
                    + "		res.RE_ID_PARTICIPANTE_FK as EVALUADOR, "
                    + "		if(dm.DE_NU_ORDEN is null, "
                    + "		0, "
                    + "		dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		if(RE_ID_PARTICIPANTE_FK is null, "
                    + "		0, "
                    + "		1) as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		relacion rel, "
                    + "		resultado res, "
                    + "		detalle_metrica dm, "
                    + "		componente cp, "
                    + "		componente cat "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK "
                    + "		and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK "
                    + "		and cp.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and cat.CO_ID_COMPONENTE_PK = cp.CO_ID_COMPONENTE_REF_FK "
                    + "		and cat.CO_ID_TIPO_COMPONENTE = 45 "
                    + "		and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idEvaluado "
                    + "union all "
                    + "	select "
                    + "		'PROM' as RELACION, "
                    + "		cat.CO_ID_COMPONENTE_PK as CATEGORIA, "
                    + "		res.RE_ID_PARTICIPANTE_FK as EVALUADOR, "
                    + "		if(dm.DE_NU_ORDEN is null, "
                    + "		0, "
                    + "		dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		if(RE_ID_PARTICIPANTE_FK is null, "
                    + "		0, "
                    + "		1) as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		relacion rel, "
                    + "		resultado res, "
                    + "		detalle_metrica dm, "
                    + "		componente cp, "
                    + "		componente cat "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK "
                    + "		and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK "
                    + "		and cp.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and cat.CO_ID_COMPONENTE_PK = cp.CO_ID_COMPONENTE_REF_FK "
                    + "		and cat.CO_ID_TIPO_COMPONENTE = 45 "
                    + "		and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idEvaluado "
                    + "union all "
                    + "	select "
                    + "		'AUTO' as RELACION, "
                    + "		cat.CO_ID_COMPONENTE_PK as CATEGORIA, "
                    + "		null as EVALUADOR, "
                    + "		AVG(dm.DE_NU_ORDEN + 1) as MEDIDA, "
                    + "		1 as CANTIDAD "
                    + "	from "
                    + "		proyecto po, "
                    + "		resultado res, "
                    + "		detalle_metrica dm, "
                    + "		componente cp, "
                    + "		componente cat "
                    + "	where "
                    + "		po.PO_ID_PROYECTO_pK = :idProyecto "
                    + "		and res.RE_ID_RELACION_FK is null "
                    + "		and res.RE_ID_PARTICIPANTE_FK is null "
                    + "		and cp.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK "
                    + "		and cat.CO_ID_COMPONENTE_PK = cp.CO_ID_COMPONENTE_REF_FK "
                    + "		and cat.CO_ID_TIPO_COMPONENTE = 45 "
                    + "		and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		and res.PA_ID_PARTICIPANTE_FK = :idEvaluado "
                    + "	group by 		 "
                    + "		cat.CO_ID_COMPONENTE_PK "
                    + "		 "
                    + "		 "
                    + "                    ) TABLA "
                    + "group by "
                    + "	TABLA.RELACION, "
                    + "	TABLA.CATEGORIA "
                    + "order by "
                    + "	TABLA.CATEGORIA ");

            query.setInteger("idProyecto", Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger("idEvaluado", intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteCategoriaMismoWeighted(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    "  SELECT                                                                   "
                    + "  TABLA.RELACION,                                                         "
                    + "  SUM(TABLA.MEDIDA),                                                      "
                    + "  SUM(TABLA.CANTIDAD)                                                     "
                    + "  FROM                                                                    "
                    + " (select 'PROM' AS RELACION,                                             "
                    + "  	IFNULL(AVG(IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1)) * rel.RE_DE_PONDERACION / 100,0) AS MEDIDA,    "
                    + "         IFNULL(SUM(IF(RE_ID_PARTICIPANTE_FK is null, 0, 1)),0) AS CANTIDAD              "
                    + "    from proyecto po,                                                     "
                    + "         relacion rel,                                                    "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                     "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                    "
                    + "     and res.RE_ID_RELACION_FK is not null                                "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ? GROUP BY rel.RE_ID_RELACION_PK                                    "
                    + "  UNION ALL                                                               "
                    + "  select 'AUTO' AS RELACION,                                              "
                    + "  	IFNULL(AVG(IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1)),0) AS MEDIDA,    "
                    + " 	IFNULL(SUM(IF(PA_ID_PARTICIPANTE_FK is null, 0, 1)),0) AS CANTIDAD             "
                    + "    from proyecto po,                                                     "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                     "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     AND res.RE_ID_RELACION_FK is null                                    "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                    "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.RE_ID_PARTICIPANTE_FK is null) TABLA GROUP BY TABLA.RELACION ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteCategoriaMismo(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT                                                                   "
                    + "  TABLA.RELACION,                                                         "
                    + "  AVG(TABLA.MEDIDA),                                                      "
                    + "  SUM(TABLA.CANTIDAD)                                                     "
                    + "  FROM                                                                    "
                    + "  (select 'PROM' AS RELACION,                                             "
                    + "  	   res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                         "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,    "
                    + "         IF(RE_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD              "
                    + "    from proyecto po,                                                     "
                    + "         relacion rel,                                                    "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                     "
                    + "     and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                    "
                    + "     and res.RE_ID_RELACION_FK is not null                                "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                    "
                    + "  UNION ALL                                                               "
                    + "  select 'AUTO' AS RELACION,                                              "
                    + "  	   res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,                         "
                    + "  	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN + 1) AS MEDIDA,    "
                    + " 	   IF(PA_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD             "
                    + "    from proyecto po,                                                     "
                    + "         resultado res,                                                   "
                    + "         detalle_metrica dm                                               "
                    + "   where po.PO_ID_PROYECTO_pK = ?                                         "
                    + "     and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                     "
                    + "     and res.CO_ID_COMPONENTE_FK = ?                                      "
                    + "     AND res.RE_ID_RELACION_FK is null                                    "
                    + "     and res.PA_ID_PARTICIPANTE_FK = ?                                    "
                    + "     and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK         "
                    + "     and res.RE_ID_PARTICIPANTE_FK is null) TABLA GROUP BY TABLA.RELACION ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteSumario(Componente objComponente, Integer intEvaluadoPk, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT TABLA.RELACION,                                                                         "
                    + "        AVG(TABLA.MEDIDA),                                                                      "
                    + "        COUNT(DISTINCT TABLA.EVALUADOR)                                                         "
                    + "        FROM                                                                                    "
                    + "       (select 'PROM' AS RELACION,                                                              "
                    + "               ifnull(res.RE_ID_PARTICIPANTE_FK,res.PA_ID_PARTICIPANTE_FK||'EVA') AS EVALUADOR, "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                                    "
                    + "               ifnull(RE_ID_PARTICIPANTE_FK,res.PA_ID_PARTICIPANTE_FK||'EVA') AS CANTIDAD       "
                    + "          from proyecto po,                                                                     "
                    + "               resultado res,                                                                   "
                    + "               detalle_metrica dm,                                                              "
                    + "               componente cop                                                                   "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                                         "
                    + "           and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK					 "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                         "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                                "
                    + " and res.RE_ID_PARTICIPANTE_FK is not null "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                                  "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                                    "
                    + " ) TABLA GROUP BY TABLA.RELACION  order by 2 desc                                               ");

            query.setInteger(0, objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteSumarioWeighted(Componente objComponente, Integer intEvaluadoPk, Integer intProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    "  select 'PROM', SUM(PONDERADO) / 100, SUM(CANTIDAD) from ( "
                    + " SELECT INFO.RELACION, INFO.MEDIDA * re.RE_DE_PONDERACION as PONDERADO, INFO.CANTIDAD from ( "
                    + " SELECT TABLA.RELACION as RELACION,                                                                          "
                    + "        AVG(TABLA.MEDIDA) as MEDIDA,                                                                       "
                    + "        COUNT(distinct TABLA.EVALUADOR) as CANTIDAD                                                         "
                    + "        FROM                                                                                     "
                    + "       (select res.RE_ID_RELACION_FK AS RELACION,                                                               "
                    + "               res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,  "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA"
                    + "          from proyecto po,                                                                      "
                    + "               resultado res,                                                                    "
                    + "               detalle_metrica dm,                                                               "
                    + "               componente cop                                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                                          "
                    + "           and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK					  "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                          "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                                 "
                    + "		   and res.RE_ID_PARTICIPANTE_FK is not null  "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                                  "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ? ) TABLA GROUP BY TABLA.RELACION "
                    + "           ) INFO, "
                    + "           relacion re "
                    + "           where re.RE_ID_RELACION_PK = INFO.RELACION) PONDERADO order by 1 ");

            query.setInteger(0, intProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaItemsAltoPromedioWeighted(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT CUE, CUESTIONARIO, PREGUNTA, SUM(MEDIDA) FROM ("
                    + " select coc.CU_ID_CUESTIONARIO_FK AS CUE,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as CUESTIONARIO,                                     "
                    + "        cop.CO_TX_DESCRIPCION as PREGUNTA,    "
                    + "        rel.RE_ID_RELACION_PK,"
                    + "        ifNULL(AVG(dm.DE_NU_ORDEN + 1) * rel.RE_DE_PONDERACION / 100,0) AS MEDIDA                                         "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc,                                                             "
                    + "        relacion rel                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                         "
                    + "    and res.RE_ID_RELACION_FK is not null                "
                    + "    GROUP BY coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION ,                                     "
                    + "        cop.CO_TX_DESCRIPCION ,    "
                    + "        rel.RE_ID_RELACION_PK"
                    + "  ) DATA GROUP BY CUE, CUESTIONARIO, PREGUNTA ORDER BY 4 desc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaItemsAltoPromedio(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as cuestionario,                                     "
                    + "        cop.CO_TX_DESCRIPCION as pregunta,                                         "
                    + "        AVG(dm.DE_NU_ORDEN + 1)                                                    "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc,                                                             "
                    + "        relacion rel                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                         "
                    + "    and res.RE_ID_RELACION_FK is not null                                          "
                    + "  GROUP BY coc.CU_ID_CUESTIONARIO_FK ,coc.CO_TX_DESCRIPCION, cop.CO_TX_DESCRIPCION "
                    + "  ORDER BY 4 desc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaItemsAltoPromedioMismo(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as cuestionario,                                     "
                    + "        cop.CO_TX_DESCRIPCION as pregunta,                                         "
                    + "        AVG(dm.DE_NU_ORDEN + 1)                                                    "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and res.RE_ID_RELACION_FK is null                                          "
                    + "  GROUP BY coc.CU_ID_CUESTIONARIO_FK ,coc.CO_TX_DESCRIPCION, cop.CO_TX_DESCRIPCION "
                    + "  ORDER BY 4 desc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaGrupalSumarioCategoriaGeneral(DatosReporte objDatosReporte, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();

            Query query = sesion.createSQLQuery(
                    " select ca.CU_ID_CUESTIONARIO_FK,                                                                "
                    + "        ca.CO_TX_DESCRIPCION,                                                                    "
                    + "        ca.CO_ID_COMPONENTE_PK,                                                                  "
                    + "        pa.PA_TX_DESCRIPCION,                                                                    "
                    + "        IFNULL(AVG(dm.DE_NU_ORDEN + 1),0),                                                                  "
                    + "        PA_TX_CORREO"
                    + "   from cuestionario cu,                                                                         "
                    + "        componente ca,                                                                           "
                    + "        componente cp                                                                            "
                    + "        left join resultado re on re.CO_ID_COMPONENTE_FK = cp.CO_ID_COMPONENTE_PK                "
                    + "        join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = re.DE_ID_DETALLE_ESCALA_FK  "
                    + "        left join participante pa on pa.PA_ID_PARTICIPANTE_PK = re.PA_ID_PARTICIPANTE_FK         "
                    + "  where cu.PO_ID_PROYECTO_FK = ?                                                                 "
                    + "    and cu.CU_ID_CUESTIONARIO_PK = ? "
                    + "    and ca.CU_ID_CUESTIONARIO_FK = cu.CU_ID_CUESTIONARIO_PK                                      "
                    + "    and ca.CO_ID_TIPO_COMPONENTE = 45                                                            "
                    + "    and cp.CO_ID_COMPONENTE_REF_FK = ca.CO_ID_COMPONENTE_PK                                      "
                    + "    and cp.CO_ID_TIPO_COMPONENTE = 46                                                            "
                    + "  group by ca.CU_ID_CUESTIONARIO_FK,                                                             "
                    + "           ca.CO_ID_COMPONENTE_PK,                                                               "
                    + "           ca.CO_TX_DESCRIPCION,                                                                 "
                    + "           pa.PA_TX_DESCRIPCION                                                                  "
                    + "  order by ca.CU_ID_CUESTIONARIO_FK,                                                             "
                    + "           ca.CO_ID_COMPONENTE_PK,                                                               "
                    + "           ca.CO_TX_DESCRIPCION,                                                                 "
                    + "           5 desc                                                                                ");

            query.setInteger(0, objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger(1, objDatosReporte.getIntIdCuestionario());
            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaGrupalSumarioCategoriaGeneralWeighted(DatosReporte objDatosReporte, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();

            Query query = sesion.createSQLQuery(
                    " select total.CU_ID_CUESTIONARIO_FK,                                                              "
                    + "           total.CO_ID_COMPONENTE_PK,                                                                "
                    + "           total.CO_TX_DESCRIPCION,                                                                  "
                    + "           total.PA_TX_DESCRIPCION, "
                    + "           sum(total.ponderado), "
                    + "           total.PA_TX_CORREO from ( "
                    + "select datos.CU_ID_CUESTIONARIO_FK,                                                              "
                    + "           datos.CO_ID_COMPONENTE_PK,                                                                "
                    + "           datos.CO_TX_DESCRIPCION,                                                                  "
                    + "           datos.PA_TX_DESCRIPCION, "
                    + "           datos.medida * datos.RE_DE_PONDERACION / 100 as ponderado, "
                    + "           datos.PA_TX_CORREO "
                    + "           from ( "
                    + " select ca.CU_ID_CUESTIONARIO_FK,                                                                 "
                    + "        ca.CO_TX_DESCRIPCION,                                                                     "
                    + "        ca.CO_ID_COMPONENTE_PK,                                                                   "
                    + "        pa.PA_TX_DESCRIPCION,                                                                     "
                    + "		AVG(dm.DE_NU_ORDEN + 1) as medida,                                                                   "
                    + " 		PA_TX_CORREO, "
                    + " 		rel.RE_DE_PONDERACION, "
                    + " 		rel.RE_ID_RELACION_PK "
                    + "   from cuestionario cu,                                                                          "
                    + "        componente ca,                                                                            "
                    + "        componente cp                                                                             "
                    + "        left join resultado re on re.CO_ID_COMPONENTE_FK = cp.CO_ID_COMPONENTE_PK                 "
                    + "        join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = re.DE_ID_DETALLE_ESCALA_FK   "
                    + "        left join participante pa on pa.PA_ID_PARTICIPANTE_PK = re.PA_ID_PARTICIPANTE_FK  "
                    + "        join relacion_participante rp on rp.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK  "
                    + "        								and rp.RE_ID_PARTICIPANTE_FK = re.RE_ID_PARTICIPANTE_FK "
                    + "        join relacion rel on rel.RE_ID_RELACION_pK = rp.RE_ID_RELACION_FK "
                    + "  where cu.PO_ID_PROYECTO_FK = ?                                                                  "
                    + "    and cu.CU_ID_CUESTIONARIO_PK = ?  "
                    + "    and ca.CU_ID_CUESTIONARIO_FK = cu.CU_ID_CUESTIONARIO_PK                                       "
                    + "    and ca.CO_ID_TIPO_COMPONENTE = 45                                                             "
                    + "    and cp.CO_ID_COMPONENTE_REF_FK = ca.CO_ID_COMPONENTE_PK                                       "
                    + "    and cp.CO_ID_TIPO_COMPONENTE = 46                    "
                    + "    and re.RE_ID_PARTICIPANTE_FK is not null "
                    + "    group by ca.CU_ID_CUESTIONARIO_FK,                                                                 "
                    + "        ca.CO_TX_DESCRIPCION,                                                                     "
                    + "        ca.CO_ID_COMPONENTE_PK,                                                                   "
                    + "        pa.PA_TX_DESCRIPCION,                                                                   "
                    + " 		PA_TX_CORREO, "
                    + " 		rel.RE_DE_PONDERACION, "
                    + " 		rel.RE_ID_RELACION_PK "
                    + "    ) datos  "
                    + "           ) total  "
                    + "           group by total.CU_ID_CUESTIONARIO_FK,                                                              "
                    + "           total.CO_ID_COMPONENTE_PK,                                                                "
                    + "           total.CO_TX_DESCRIPCION,                                                                  "
                    + "           total.PA_TX_DESCRIPCION, "
                    + "           total.PA_TX_CORREO  "
                    + "  order by 2,total.PA_TX_CORREO,5 desc        ");

            query.setInteger(0, objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger(1, objDatosReporte.getIntIdCuestionario());
            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaItemsBajaPromedio(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as cuestionario,                                     "
                    + "        cop.CO_TX_DESCRIPCION as pregunta,                                         "
                    + "        AVG(dm.DE_NU_ORDEN + 1)                                                    "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and res.RE_ID_RELACION_FK is not null                                          "
                    + "  GROUP BY coc.CU_ID_CUESTIONARIO_FK ,coc.CO_TX_DESCRIPCION, cop.CO_TX_DESCRIPCION "
                    + "  ORDER BY 4 asc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;

    }

    public List listaItemsBajaPromedioWeighted(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT CUE, CUESTIONARIO, PREGUNTA, SUM(MEDIDA) FROM ("
                    + " select coc.CU_ID_CUESTIONARIO_FK AS CUE,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as CUESTIONARIO,                                     "
                    + "        cop.CO_TX_DESCRIPCION as PREGUNTA,    "
                    + "        rel.RE_ID_RELACION_PK,"
                    + "        ifNULL(AVG(dm.DE_NU_ORDEN + 1) * rel.RE_DE_PONDERACION / 100,0) AS MEDIDA                                         "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc,                                                             "
                    + "        relacion rel                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                                         "
                    + "    and res.RE_ID_RELACION_FK is not null                "
                    + "    GROUP BY coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION ,                                     "
                    + "        cop.CO_TX_DESCRIPCION ,    "
                    + "        rel.RE_ID_RELACION_PK"
                    + "  ) DATA GROUP BY CUE, CUESTIONARIO, PREGUNTA ORDER BY 4 asc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;

    }

    public List listaItemsBajaPromedioMismo(Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " select coc.CU_ID_CUESTIONARIO_FK,                                                 "
                    + "        coc.CO_TX_DESCRIPCION as cuestionario,                                     "
                    + "        cop.CO_TX_DESCRIPCION as pregunta,                                         "
                    + "        AVG(dm.DE_NU_ORDEN + 1)                                                    "
                    + "   from proyecto po,                                                               "
                    + "        resultado res,                                                             "
                    + "        detalle_metrica dm,                                                        "
                    + "        componente cop,                                                            "
                    + "        componente coc                                                             "
                    + "  where po.PO_ID_PROYECTO_PK = ?                                                   "
                    + "    and res.PO_ID_PROYECTO_FK = po.PO_ID_PROYECTO_PK                               "
                    + "    and res.PA_ID_PARTICIPANTE_FK = ?                                              "
                    + "    and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK                   "
                    + "    and res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK                          "
                    + "    and coc.CO_ID_COMPONENTE_PK = cop.CO_ID_COMPONENTE_REF_FK                      "
                    + "    and res.RE_ID_RELACION_FK is null                                          "
                    + "  GROUP BY coc.CU_ID_CUESTIONARIO_FK ,coc.CO_TX_DESCRIPCION, cop.CO_TX_DESCRIPCION "
                    + "  ORDER BY 4 asc ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;

    }

    public List listaReporteSumarioMismo(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT TABLA.RELACION,                                                          "
                    + "        AVG(TABLA.MEDIDA),                                                       "
                    + "        COUNT(DISTINCT TABLA.EVALUADOR)                                          "
                    + "        FROM                                                                     "
                    + "       (select 'PROM' AS RELACION,                                               "
                    + "               res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                           "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                     "
                    + "               RE_ID_PARTICIPANTE_FK AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               relacion rel,                                                     "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                      "
                    + "           and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and res.RE_ID_RELACION_FK is not null                                 "
                    + "         UNION ALL                                                               "
                    + "        select 'AUTO' AS RELACION,                                               "
                    + "               res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,                           "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                     "
                    + "               PA_ID_PARTICIPANTE_FK AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                      "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.RE_ID_RELACION_FK is null                                     "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and res.RE_ID_PARTICIPANTE_FK is null) TABLA GROUP BY TABLA.RELACION  ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteSumarioMismoWeighted(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT TABLA.RELACION,                                                          "
                    + "        IFNULL(TABLA.MEDIDA,0),                                                       "
                    + "        IFNULL(TABLA.CANTIDAD,0)                                          "
                    + "        FROM                                                                     "
                    + "       (select 'PROM' as RELACION, "
                    + "       			sum(PROMEDIO.MEDIDA * rel.RE_DE_PONDERACION / 100)  as MEDIDA, "
                    + "   				sum(PROMEDIO.CANTIDAD) as CANTIDAD "
                    + "          from (select res.RE_ID_RELACION_FK AS RELACION,                                               "
                    + "               AVG(dm.DE_NU_ORDEN + 1) AS MEDIDA,                                     "
                    + "               COUNT(DISTINCT RE_ID_PARTICIPANTE_FK) AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                       "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and res.RE_ID_RELACION_FK is not null group by res.RE_ID_RELACION_FK"
                    + "  		) PROMEDIO, relacion rel"
                    + "         where rel.RE_ID_RELACION_PK = PROMEDIO.RELACION                              "
                    + "         UNION ALL                                                               "
                    + "        select 'AUTO' AS RELACION,                                               "
                    + "               AVG(dm.DE_NU_ORDEN + 1) AS MEDIDA,                                     "
                    + "               COUNT(DISTINCT PA_ID_PARTICIPANTE_FK) AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                      "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.RE_ID_RELACION_FK is null                                     "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and res.RE_ID_PARTICIPANTE_FK is null) TABLA GROUP BY TABLA.RELACION    ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteSumarioMismoRelacion(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT TABLA.RELACION,                                                          "
                    + "        AVG(TABLA.MEDIDA),                                                       "
                    + "        COUNT(DISTINCT TABLA.EVALUADOR)                                          "
                    + "        FROM                                                                     "
                    + "       (select 'PROM' AS RELACION,                                               "
                    + "               res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                           "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                     "
                    + "               RE_ID_PARTICIPANTE_FK AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               relacion rel,                                                     "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                      "
                    + "           and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and res.RE_ID_RELACION_FK is not null                                 "
                    + "         UNION ALL                                                               "
                    + "       select rel.RE_TX_ABREVIATURA AS RELACION,                                               "
                    + "               res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,                           "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                     "
                    + "               RE_ID_PARTICIPANTE_FK AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               relacion rel,                                                     "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                      "
                    + "           and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and res.RE_ID_RELACION_FK is not null                                 "
                    + "         UNION ALL                                                               "
                    + "        select 'AUTO' AS RELACION,                                               "
                    + "               res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,                           "
                    + "               dm.DE_NU_ORDEN + 1 AS MEDIDA,                                     "
                    + "               PA_ID_PARTICIPANTE_FK AS CANTIDAD                                 "
                    + "          from proyecto po,                                                      "
                    + "               resultado res,                                                    "
                    + "               detalle_metrica dm,                                               "
                    + "               componente cop                                                    "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                          "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                      "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                 "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                   "
                    + "           and res.RE_ID_RELACION_FK is null                                     "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                     "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK          "
                    + "           and res.RE_ID_PARTICIPANTE_FK is null) TABLA GROUP BY TABLA.RELACION  ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);
            query.setInteger(6, idProyecto);
            query.setInteger(7, objComponente.getCoIdComponentePk());
            query.setInteger(8, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteSumarioMismoRelacionWeighted(Componente objComponente, Integer intEvaluadoPk, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT TABLA.RELACION,                                                           "
                    + "        IFNULL(TABLA.MEDIDA,0),                                                        "
                    + "        IFNULL(TABLA.CANTIDAD,0)                                           "
                    + "        FROM                                                                      "
                    + "       (select 'PROM' as RELACION, "
                    + "       			sum(PROMEDIO.MEDIDA ) as MEDIDA, "
                    + "       			sum(PROMEDIO.CANTIDAD) as CANTIDAD  "
                    + "          from ( "
                    + "        select res.RE_ID_RELACION_FK AS RELACION,                                                            "
                    + "               AVG(dm.DE_NU_ORDEN + 1) * rel.RE_DE_PONDERACION / 100 AS MEDIDA,                                      "
                    + "               COUNT(distinct RE_ID_PARTICIPANTE_FK) AS CANTIDAD                                  "
                    + "          from proyecto po,                                                       "
                    + "               resultado res,                                                     "
                    + "               detalle_metrica dm,                                                "
                    + "               componente cop,"
                    + "               relacion rel "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                           "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                       "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "		   and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK"
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                  "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                    "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                      "
                    + "           and res.RE_ID_RELACION_FK is not null "
                    + "		  group by res.RE_ID_RELACION_FK  "
                    + "           ) PROMEDIO"
                    + "         UNION ALL                                                                "
                    + "       select rel.RE_TX_ABREVIATURA AS RELACION,                                  "
                    + "               AVG(dm.DE_NU_ORDEN + 1) AS MEDIDA,                                 "
                    + "               COUNT(DISTINCT RE_ID_PARTICIPANTE_FK)  AS CANTIDAD                 "
                    + "          from proyecto po,                                                       "
                    + "               relacion rel,                                                      "
                    + "               resultado res,                                                     "
                    + "               detalle_metrica dm,                                                "
                    + "               componente cop                                                     "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                           "
                    + "           and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK                       "
                    + "           and rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK                      "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK           "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                  "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                    "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                      "
                    + "           and res.RE_ID_RELACION_FK is not null                                  "
                    + "         UNION ALL                                                                "
                    + "        select 'AUTO' AS RELACION,                                                "
                    + "               AVG(dm.DE_NU_ORDEN + 1) AS MEDIDA,                                 "
                    + "               COUNT(DISTINCT PA_ID_PARTICIPANTE_FK) AS CANTIDAD                  "
                    + "          from proyecto po,                                                       "
                    + "               resultado res,                                                     "
                    + "               detalle_metrica dm,                                                "
                    + "               componente cop                                                     "
                    + "         where po.PO_ID_PROYECTO_pK = ?                                           "
                    + "           and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK                       "
                    + "           and cop.CO_ID_COMPONENTE_PK = res.CO_ID_COMPONENTE_FK                  "
                    + "           and cop.CO_ID_COMPONENTE_REF_FK = ?                                    "
                    + "           and res.RE_ID_RELACION_FK is null                                      "
                    + "           and res.PA_ID_PARTICIPANTE_FK = ?                                      "
                    + "           and dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK           "
                    + "           and res.RE_ID_PARTICIPANTE_FK is null) TABLA  ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, objComponente.getCoIdComponentePk());
            query.setInteger(2, intEvaluadoPk);
            query.setInteger(3, idProyecto);
            query.setInteger(4, objComponente.getCoIdComponentePk());
            query.setInteger(5, intEvaluadoPk);
            query.setInteger(6, idProyecto);
            query.setInteger(7, objComponente.getCoIdComponentePk());
            query.setInteger(8, intEvaluadoPk);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteDos(Componente objComponente) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery("SELECT "
                    + "cate.CO_TX_DESCRIPCION, "
                    + "AVG(TABLA.MEDIDA), "
                    + "SUM(TABLA.CANTIDAD) "
                    + "FROM "
                    + "(select cop.CO_TX_DESCRIPCION AS RELACION,  "
                    + "	cop.CO_ID_COMPONENTE_PK as ID_COMPONENTE, "
                    + "    cop.co_id_componente_ref_fk as padre, "
                    + "	   res.RE_ID_PARTICIPANTE_FK AS EVALUADOR,  "
                    + "	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN) AS MEDIDA,  "
                    + "       IF(RE_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD "
                    + "  from proyecto po, "
                    + "       relacion rel "
                    + "       left join resultado res on  rel.RE_ID_RELACION_PK = res.RE_ID_RELACION_FK -- and res.CO_ID_COMPONENTE_FK=2 "
                    + "       left join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "        "
                    + "       inner join componente cop on res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK "
                    + "        "
                    + " where po.PO_ID_PROYECTO_pK = ? "
                    + "   and po.PO_ID_PROYECTO_PK = rel.PO_ID_PROYECTO_FK "
                    + "UNION ALL "
                    + "select 'AUT' AS RELACION,  "
                    + "		cop.CO_ID_COMPONENTE_PK as ID_COMPONENTE, "
                    + "        cop.co_id_componente_ref_fk as padre, "
                    + "	   res.PA_ID_PARTICIPANTE_FK AS EVALUADOR,  "
                    + "	   IF(dm.DE_NU_ORDEN is null, 0, dm.DE_NU_ORDEN) AS MEDIDA,  "
                    + "       IF(PA_ID_PARTICIPANTE_FK is null, 0, 1) AS CANTIDAD "
                    + "  from proyecto po, "
                    + "       resultado res "
                    + "       left join detalle_metrica dm on dm.DE_ID_DETALLE_ESCALA_PK = res.DE_ID_DETALLE_ESCALA_FK "
                    + "       inner join componente cop on res.CO_ID_COMPONENTE_FK = cop.CO_ID_COMPONENTE_PK "
                    + " where po.PO_ID_PROYECTO_pK = ? "
                    + "   and po.PO_ID_PROYECTO_PK = res.PO_ID_PROYECTO_FK "
                    + "   -- and res.CO_ID_COMPONENTE_FK=2  "
                    + "   AND res.RE_ID_RELACION_FK is null  "
                    + "   and res.RE_ID_PARTICIPANTE_FK is null) TABLA, componente cate   "
                    + "   where TABLA.padre = cate.CO_ID_COMPONENTE_PK and cate.CO_ID_COMPONENTE_PK=? group by cate.CO_TX_DESCRIPCION ");

            query.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger(1, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query.setInteger(2, objComponente.getCoIdComponentePk());

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List obtieneListaResultadoPreguntasAbiertas(Integer indIdEvaluado, Integer idProyecto) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(" select co.CO_TX_DESCRIPCION, re.RE_TX_COMENTARIO "
                    + "   from resultado re, "
                    + "        componente co "
                    + "  where re.PO_ID_PROYECTO_FK = ? "
                    + "    and co.CO_ID_COMPONENTE_PK = re.CO_ID_COMPONENTE_FK "
                    + "    and co.CO_ID_TIPO_COMPONENTE = ? "
                    + "    and re.PA_ID_PARTICIPANTE_FK = ? "
                    + "  order by co.CO_TX_DESCRIPCION ");

            query.setInteger(0, idProyecto);
            query.setInteger(1, Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_ABIERTA);
            query.setInteger(2, indIdEvaluado);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List obtieneListaResultadoComentarios(Integer indIdEvaluado, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(" select co.CO_TX_DESCRIPCION, re.RE_TX_COMENTARIO, re.CO_ID_COMPONENTE_REF_FK "
                    + "   from resultado re, "
                    + "        componente co "
                    + "  where re.PO_ID_PROYECTO_FK = ? "
                    + "    and co.CO_ID_COMPONENTE_PK = re.CO_ID_COMPONENTE_FK "
                    + "    and co.CO_ID_TIPO_COMPONENTE = ? "
                    + "    and re.PA_ID_PARTICIPANTE_FK = ? "
                    + "  order by co.CO_TX_DESCRIPCION ");

            query.setInteger(0, objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger(1, Constantes.INT_ET_TIPO_COMPONENTE_COMENTARIO);
            query.setInteger(2, indIdEvaluado);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public boolean guardarResultadoFinal(List<EvaluacionesXEjecutar> lstPreGrabado, Integer idProyecto, Date dtSaved) {

        boolean procesoOk = false;

        try {

            iniciaOperacion();

            for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstPreGrabado) {

                /* GUARDA RESPUESTAS PREGUNTAS CERRADAS */
                for (PreguntaCerradaBean objPreguntaCerradaBean : objEvaluacionesXEjecutar.getLstPreguntasCerradas()) {
                    if (objPreguntaCerradaBean.getBlRespondido()) {
                        guardarResultadoEval(objPreguntaCerradaBean.getId().toString(), objPreguntaCerradaBean.getIdRespuesta().toString(), null, idProyecto, objEvaluacionesXEjecutar, null, dtSaved);
                    }

                    for (ComentarioBean objComentarioBean : objPreguntaCerradaBean.getLstComentarios()) {
                        if (Utilitarios.noEsNuloOVacio(objComentarioBean.getStrRespuesta())) {
                            guardarResultadoEval(objComentarioBean.getId().toString(), null, objComentarioBean.getStrRespuesta(), idProyecto, objEvaluacionesXEjecutar, objPreguntaCerradaBean.getId().toString(), dtSaved);
                        }
                    }
                }

                /* GUARDA RESPUESTAS PREGUNTAS ABIERTAS */
                for (PreguntaAbiertaBean objPreguntaAbiertaBean : objEvaluacionesXEjecutar.getLstPreguntasAbiertas()) {
                    if (Utilitarios.noEsNuloOVacio(objPreguntaAbiertaBean.getStrRespuesta())) {
                        guardarResultadoEval(objPreguntaAbiertaBean.getId().toString(), null, objPreguntaAbiertaBean.getStrRespuesta(), idProyecto, objEvaluacionesXEjecutar, null, dtSaved);
                    }
                }

                /* ACTUALIZA EVALUACIÓN A TERMINADO */
                if (objEvaluacionesXEjecutar.getRelacionParticipanteId() != null) {

                    RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
                    RelacionParticipante objRelacionParticipante = objRelacionParticipanteDAO.obtenRelacionParticipante(objEvaluacionesXEjecutar.getRelacionParticipanteId());
                    objRelacionParticipante.setRpIdEstado(Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO);
                    //objRelacionParticipanteDAO.actualizaRelacionParticipante(objRelacionParticipante);
                    sesion.update(objRelacionParticipante);

                } else {

                    ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
                    Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluacionesXEjecutar.getIdParticipante());
                    objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
                    //objParticipanteDAO.actualizaParticipante(objParticipante);
                    sesion.update(objParticipante);
                }

            }

            tx.commit();

            procesoOk = true;

        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }

        return procesoOk;

    }

    public void guardarResultadoEval(String idComponentePk, String idDetalleMetrica, String txtComentario, Integer idProyecto, EvaluacionesXEjecutar objEvaluacionesXEjecutar, String idComponentePreguntaPk, Date dtSaved) {

        Resultado resultado = new Resultado();

        Componente componente = new Componente();
        componente.setCoIdComponentePk(Integer.parseInt(idComponentePk));

        resultado.setReDtFecAudit(dtSaved);

        DetalleMetrica detalleMetrica = new DetalleMetrica();

        if (Utilitarios.noEsNuloOVacio(idDetalleMetrica)) {
            detalleMetrica.setDeIdDetalleEscalaPk(Integer.parseInt(idDetalleMetrica));
            resultado.setDetalleMetrica(detalleMetrica);
        }

        resultado.setComponente(componente);

        if (Utilitarios.noEsNuloOVacio(txtComentario)) {
            resultado.setReTxComentario(txtComentario);
        }

        if (Utilitarios.noEsNuloOVacio(idComponentePreguntaPk)) {
            resultado.setCoIdComponenteRefFk(Integer.parseInt(idComponentePreguntaPk));
        }

        if (objEvaluacionesXEjecutar.getRelacionParticipanteId() != null) {
            resultado.setPaIdParticipanteFk(objEvaluacionesXEjecutar.getRelacionParticipanteId().getPaIdParticipanteFk());
            resultado.setReIdParticipanteFk(objEvaluacionesXEjecutar.getRelacionParticipanteId().getReIdParticipanteFk());
            resultado.setReIdRelacionFk(objEvaluacionesXEjecutar.getRelacionParticipanteId().getReIdRelacionFk());
        } else {
            resultado.setPaIdParticipanteFk(objEvaluacionesXEjecutar.getIdParticipante());
        }

        Proyecto objProyecto = new Proyecto();
        objProyecto.setPoIdProyectoPk(idProyecto);

        resultado.setProyecto(objProyecto);

        //RelacionParticipante relacionParticipante = new RelacionParticipante();
        //relacionParticipante.setId(this.relacionParticipanteId);
        //resultado.setRelacionParticipante(relacionParticipante);
        //resultadoDAO.guardaResultado(resultado);
        sesion.save(resultado);

    }

    public List obtieneListaTodasLasRespuestas(Integer idCuestionario, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " 	select                                                                           "
                    + "     pa.PA_ID_PARTICIPANTE_PK,                                                      "
                    + "     pa.PA_TX_DESCRIPCION,                                                          "
                    + "     pa.PA_TX_SEXO,                                                                 "
                    + "     pa.PA_NR_EDAD,                                                                 "
                    + "     pa.PA_NR_TIEMPO_TRABAJO,                                                       "
                    + "     pa.PA_TX_OCUPACION,                                                            "
                    + "     pa.PA_TX_AREA_NEGOCIO,                                                         "
                    + "     rl.RE_TX_ABREVIATURA,                                                          "
                    + "     red.RE_ID_PARTICIPANTE_PK,                                                     "
                    + "     red.RE_TX_DESCRIPCION,                                                         "
                    + "     red.RE_TX_SEXO,                                                                "
                    + "     red.RE_NR_EDAD,                                                                "
                    + "     red.RE_NR_TIEMPO_TRABAJO,                                                      "
                    + "     red.RE_TX_OCUPACION,                                                           "
                    + "     red.RE_TX_AREA_NEGOCIO,                                                        "
                    + "     co.CO_ID_COMPONENTE_PK,                                                        "
                    + "     co.CO_TX_DESCRIPCION,                                                          "
                    + "     dm.DE_NU_ORDEN + 1                                                             "
                    + " from                                                                               "
                    + " cuestionario_evaluado ce, "
                    + "     participante pa,                                                               "
                    + "     red_evaluacion red,                                                            "
                    + "     relacion_participante rp                                                       "
                    + "         inner join                                                                 "
                    + "     relacion rl ON rl.RE_ID_RELACION_PK = rp.RE_ID_RELACION_FK                     "
                    + "         left join                                                                  "
                    + "     resultado re ON re.PA_ID_PARTICIPANTE_FK = rp.PA_ID_PARTICIPANTE_FK            "
                    + "         and re.RE_ID_PARTICIPANTE_FK = rp.RE_ID_PARTICIPANTE_FK                    "
                    + "         and re.RE_ID_PARTICIPANTE_FK is not null                                   "
                    + "         left join                                                                  "
                    + "     componente co ON co.CO_ID_COMPONENTE_PK = re.CO_ID_COMPONENTE_FK               "
                    + "         and co.CO_ID_TIPO_COMPONENTE = :p_tipoc                                    "
                    + "         and co.CU_ID_CUESTIONARIO_FK = :p_cuestionario                             "
                    + "         left join                                                                  "
                    + "     detalle_metrica dm ON dm.DE_ID_DETALLE_ESCALA_PK = re.DE_ID_DETALLE_ESCALA_FK  "
                    + " where                                                                              "
                    + "     pa.PO_ID_PROYECTO_FK = :p_proyecto                                                     "
                    + " and ce.CU_ID_CUESTIONARIO_FK = :p_cuestionario "
                    + " and pa.PA_ID_PARTICIPANTE_PK = ce.PA_ID_PARTICIPANTE_FK "
                    + "         and rp.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK                    "
                    + "         and red.RE_ID_PARTICIPANTE_PK = rp.RE_ID_PARTICIPANTE_FK                   "
                    + " UNION ALL select                                                                   "
                    + "     pa.PA_ID_PARTICIPANTE_PK,                                                      "
                    + "     pa.PA_TX_DESCRIPCION,                                                          "
                    + "     pa.PA_TX_SEXO,                                                                 "
                    + "     pa.PA_NR_EDAD,                                                                 "
                    + "     pa.PA_NR_TIEMPO_TRABAJO,                                                       "
                    + "     pa.PA_TX_OCUPACION,                                                            "
                    + "     pa.PA_TX_AREA_NEGOCIO,                                                         "
                    + "     'MISMO',                                                                       "
                    + "     pa.PA_ID_PARTICIPANTE_PK,                                                      "
                    + "     pa.PA_TX_DESCRIPCION,                                                          "
                    + "     pa.PA_TX_SEXO,                                                                 "
                    + "     pa.PA_NR_EDAD,                                                                 "
                    + "     pa.PA_NR_TIEMPO_TRABAJO,                                                       "
                    + "     pa.PA_TX_OCUPACION,                                                            "
                    + "     pa.PA_TX_AREA_NEGOCIO,                                                         "
                    + "     temp.CO_ID_COMPONENTE_PK,                                                      "
                    + "     temp.CO_TX_DESCRIPCION,                                                        "
                    + "     temp.DE_NU_ORDEN                                                               "
                    + " from                                                                               "
                    + "     participante pa,                                                               "
                    + "     cuestionario_evaluado ce,                                                      "
                    + "     (select                                                                        "
                    + "         re.PA_ID_PARTICIPANTE_FK,                                                  "
                    + "             co.CO_ID_COMPONENTE_PK,                                                "
                    + "             co.CO_TX_DESCRIPCION,                                                  "
                    + "             dm.DE_NU_ORDEN + 1 as DE_NU_ORDEN                                      "
                    + "     from                                                                           "
                    + "         resultado re, componente co, detalle_metrica dm                            "
                    + "     where                                                                          "
                    + "         co.CO_ID_COMPONENTE_PK = re.CO_ID_COMPONENTE_FK                            "
                    + "             and dm.DE_ID_DETALLE_ESCALA_PK = re.DE_ID_DETALLE_ESCALA_FK            "
                    + "             and re.RE_ID_PARTICIPANTE_FK is null                                   "
                    + "             and re.RE_ID_RELACION_FK is null                                       "
                    + "             and co.CO_ID_TIPO_COMPONENTE = :p_tipoc                                "
                    + "             and co.CU_ID_CUESTIONARIO_FK = :p_cuestionario                         "
                    + "             and re.PO_ID_PROYECTO_FK = :p_proyecto ) temp                          "
                    + " where                                                                              "
                    + "     pa.PO_ID_PROYECTO_FK = :p_proyecto                                             "
                    + "         and pa.PA_IN_AUTOEVALUAR = true                                            "
                    + "         and ce.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK                    "
                    + "         and ce.CU_ID_CUESTIONARIO_FK = :p_cuestionario                             "
                    + "         and temp.PA_ID_PARTICIPANTE_FK = pa.PA_ID_PARTICIPANTE_PK                  "
                    + " order by 1 , 8 , 9 , 16                                                            ");

            query.setInteger("p_proyecto", objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger("p_tipoc", Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA);
            query.setInteger("p_cuestionario", idCuestionario);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List listaReporteNivelParticipacion(Integer idCuestionario, ReporteGenerado objReporteGenerado) throws HibernateException {

        List listaResultado = null;

        try {

            iniciaOperacion();
            Query query = sesion.createSQLQuery(
                    " SELECT p.PA_ID_PARTICIPANTE_PK,                                           "
                    + " 	   p.PA_TX_DESCRIPCION,                                                 "
                    + "        sum(pers_total.pa_count),                                          "
                    + "        sum(pers_res.pa_count),                                             "
                    + " PA_TX_CORREO "
                    + "   FROM participante p,                                                    "
                    + "        cuestionario_evaluado ce,                                          "
                    + " 	   (select pa_id,                                                       "
                    + "                sum(pa_count) as pa_count                                  "
                    + " 		  from (select rp.PA_ID_PARTICIPANTE_FK as pa_id,                   "
                    + " 					   count(distinct r.RE_ID_PARTICIPANTE_PK) as pa_count  "
                    + " 				  from red_evaluacion r,                                    "
                    + " 					   relacion_participante rp                             "
                    + " 				 where r.RE_ID_PARTICIPANTE_PK = rp.RE_ID_PARTICIPANTE_FK   "
                    + " 				   and r.RE_ID_ESTADO in ( :est_red_eje , :est_red_ter )     "
                    + " 				   and r.PO_ID_PROYECTO_FK = :id_pro                         "
                    + " 				 group by rp.PA_ID_PARTICIPANTE_FK                          "
                    + " 				 union all                                                  "
                    + " 				select pa.PA_ID_PARTICIPANTE_PK as pa_id, 1 as pa_count     "
                    + " 				  from participante pa                                      "
                    + " 				 where pa.PA_ID_ESTADO in ( :est_par_eje ,:est_par_ter )       "
                    + " 				   and pa.PA_IN_AUTOEVALUAR = true                          "
                    + " 				   and pa.PO_ID_PROYECTO_FK = :id_pro ) temp1                    "
                    + " 		  group by pa_id) pers_total                                        "
                    + " 	 left join                                                              "
                    + " 	   (select pa_id,                                                       "
                    + "                sum(pa_count) as pa_count                                  "
                    + " 		  from (SELECT r.PA_ID_PARTICIPANTE_FK as pa_id,                    "
                    + " 		    		   count(distinct r.RE_ID_PARTICIPANTE_FK) as pa_count  "
                    + " 				  FROM resultado r, red_evaluacion re, componente c         "
                    + " 				 where r.PO_ID_PROYECTO_FK = :id_pro                            "
                    + " 				   and r.RE_ID_RELACION_FK is not null                      "
                    + "                    and re.RE_ID_PARTICIPANTE_PK = r.RE_ID_PARTICIPANTE_FK "
                    + "                    and re.RE_ID_ESTADO in ( :est_red_eje , :est_red_ter )                           "
                    + "                    and c.CO_ID_COMPONENTE_PK = r.CO_ID_COMPONENTE_FK      "
                    + "                    and c.CU_ID_CUESTIONARIO_FK = :id_cuest                        "
                    + " 				 group by r.PA_ID_PARTICIPANTE_FK                           "
                    + " 				 union all                                                  "
                    + " 				SELECT r.PA_ID_PARTICIPANTE_FK, 1                           "
                    + " 				  FROM resultado r, componente c                            "
                    + " 				 where r.PO_ID_PROYECTO_FK = :id_pro                            "
                    + " 				   and r.RE_ID_RELACION_FK is null                          "
                    + "                    and c.CO_ID_COMPONENTE_PK = r.CO_ID_COMPONENTE_FK      "
                    + "                    and c.CU_ID_CUESTIONARIO_FK = :id_cuest                        "
                    + " 				 group by r.PA_ID_PARTICIPANTE_FK) temp2                    "
                    + "          group by pa_id) pers_res on pers_res.pa_id = pers_total.pa_id    "
                    + "  where p.PA_ID_ESTADO in ( :est_par_eje, :est_par_ter )                                                 "
                    + "    and p.PA_IN_RED_CARGADA = true                                         "
                    + "    and p.PA_IN_RED_VERIFICADA = true                                      "
                    + "    and p.PO_ID_PROYECTO_FK = :id_pro                                          "
                    + "    and pers_total.pa_id = p.PA_ID_PARTICIPANTE_PK                         "
                    + "    and ce.PA_ID_PARTICIPANTE_FK = p.PA_ID_PARTICIPANTE_PK                 "
                    + "    and ce.CU_ID_CUESTIONARIO_FK = :id_cuest                                       "
                    + "  group by p.PA_TX_DESCRIPCION                    ");

            query.setInteger("id_pro", objReporteGenerado.getProyectoInfo().getIntIdProyecto());
            query.setInteger("est_par_eje", Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION);
            query.setInteger("est_par_ter", Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
            query.setInteger("est_red_eje", Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION);
            query.setInteger("est_red_ter", Constantes.INT_ET_ESTADO_EVALUADOR_TERMINADO);
            query.setInteger("id_cuest", idCuestionario);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

    public List getResultQuestionsByEvaluator(Integer idProyecto, Integer idParticipant) {

        List listaResultado = null;

        try {

            iniciaOperacion();

            Query query = sesion.createSQLQuery(
                    "select "
                    + "	tabla.CATEGORIA_ID, "
                    + "	tabla.CAT_DESCRIPCION, "
                    + "	tabla.PREC_ID, "
                    + "	tabla.PREC_DESCRIPCION, "
                    + "	tabla.RELACION, "
                    + "	tabla.PRE_PUN_REQ, "
                    + "	tabla.PRE_PUN_MIN, "
                    + "	AVG(tabla.MEDIDA) "
                    + "from "
                    + "	( "
                    + "	select "
                    + "		CAT.CO_ID_COMPONENTE_PK as CATEGORIA_ID, "
                    + "		CAT.CO_TX_DESCRIPCION as CAT_DESCRIPCION, "
                    + "		COP.CO_ID_COMPONENTE_PK as PREC_ID, "
                    + "		COP.CO_TX_DESCRIPCION as PREC_DESCRIPCION, "
                    + "		IFNULL(REL.RE_TX_ABREVIATURA, 'AUTO') as RELACION, "
                    + "		DM.DE_NU_ORDEN + 1 as MEDIDA, "
                    + "		COP.CO_NR_PUNTAJE_REQUERIDO as PRE_PUN_REQ, "
                    + "		COP.CO_NR_PUNTAJE_MINIMO_REQUERIDO as PRE_PUN_MIN "
                    + "	from "
                    + "		proyecto PO "
                    + "	inner join resultado RES on "
                    + "		RES.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK "
                    + "	inner join detalle_metrica DM on "
                    + "		DM.DE_ID_DETALLE_ESCALA_PK = RES.DE_ID_DETALLE_ESCALA_FK "
                    + "	inner join componente COP on "
                    + "		COP.CO_ID_COMPONENTE_PK = RES.CO_ID_COMPONENTE_FK "
                    + "	inner join componente CAT on "
                    + "		CAT.CO_ID_COMPONENTE_PK = COP.CO_ID_COMPONENTE_REF_FK "
                    + "	left join relacion REL on "
                    + "		REL.RE_ID_RELACION_PK = RES.RE_ID_RELACION_FK "
                    + "	where "
                    + "		PO.PO_ID_PROYECTO_PK = :idProyecto "
                    + "		and COP.CO_ID_TIPO_COMPONENTE = 46 "
                    + "		and RES.PA_ID_PARTICIPANTE_FK = :idParticipante "
                    + " ) tabla "
                    + "group by "
                    + "	tabla.CATEGORIA_ID, "
                    + "	tabla.CAT_DESCRIPCION, "
                    + "	tabla.PREC_ID, "
                    + "	tabla.PREC_DESCRIPCION, "
                    + "	tabla.RELACION, "
                    + "	tabla.PRE_PUN_REQ, "
                    + "	tabla.PRE_PUN_MIN "
                    + "order by "
                    + "	tabla.PREC_ID, "
                    + "	tabla.RELACION ");

            query.setInteger("idProyecto", idProyecto);
            query.setInteger("idParticipante", idParticipant);

            listaResultado = query.list();

        } finally {
            sesion.close();
        }

        return listaResultado;
    }

}
