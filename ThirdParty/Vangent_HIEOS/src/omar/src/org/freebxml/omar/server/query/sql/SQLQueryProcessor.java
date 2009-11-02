/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/sql/SQLQueryProcessor.java,v 1.31 2007/09/28 15:49:20 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.sql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.common.IterativeQueryParams;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectList;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 * Processor for SQL queries. Used by the QueryManagerImpl.
 *
 * @see
 * @author Farrukh S. Najmi
 */
public class SQLQueryProcessor {
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private SQLQueryProcessor _sqlQueryProcessor; */
    private static SQLQueryProcessor instance = null;
    private Log log = LogFactory.getLog(this.getClass());
    boolean bypassSQLParser = false;

    protected SQLQueryProcessor() {
        bypassSQLParser = Boolean.valueOf(RegistryProperties.getInstance()
            .getProperty("org.freebxml.omar.server.query.sql.SQLQueryProcessor.bypassSQLParser", "false")).booleanValue();
    }

    public RegistryObjectListType executeQuery(ServerRequestContext context, UserType user, String sqlQuery,
        ResponseOptionType responseOption, IterativeQueryParams paramHolder, boolean bypassSQLParserForQuery) throws RegistryException {
        RegistryObjectList sqlResult = null;
        log.debug("unparsed query: " + sqlQuery + ";");
        // System.out.println("unparsed query: " + sqlQuery + ";"); // HIEOS/BHT (DEBUG).
        try {
            sqlResult = BindingUtility.getInstance().rimFac.createRegistryObjectList();

            //Fix the query according to the responseOption to return the right type of objects
            String fixedQuery = sqlQuery;
            String tableName=null;


            if (!(bypassSQLParser||bypassSQLParserForQuery)) {
                //parse the queryString to sget at certain info like the select column and table name etc.
                InputStream stream = new ByteArrayInputStream(sqlQuery.getBytes("utf-8"));
                SQLParser parser = new SQLParser(new InputStreamReader(stream, "utf-8"));
                fixedQuery = parser.processQuery(user, responseOption);
                log.debug("Fixed query: " + fixedQuery + ";");
                tableName = parser.firstTableName;
            } else {
                String[] strs = sqlQuery.toUpperCase().split(" FROM ");
                if (strs.length > 1) {
                    tableName = (strs[1].split(" "))[0];
                }
                //tableName = sqlQuery.substring(sqlQuery.indexOf("FROM"));
            }
	    if (log.isTraceEnabled()) {
                log.trace(ServerResourceBundle.getInstance().getString("message.executingQuery",
				                                                        new Object[]{fixedQuery}));
            }
            //Get the List of objects (ObjectRef, RegistryObject, leaf class) as
            //specified by the responeOption
            List objectRefs = new ArrayList();
            List queryParams = context.getStoredQueryParams();
            if (queryParams.size() == 0) {
                queryParams = null;
            }

            log.debug("queryParams = " + queryParams);
            List objs = PersistenceManagerFactory.getInstance()
                                                 .getPersistenceManager()
                                                 .executeSQLQuery(context, fixedQuery, queryParams,
                    responseOption, tableName, objectRefs, paramHolder);

            if (queryParams != null) {
                queryParams.clear();
            }

            List list = sqlResult.getIdentifiable();
            if ((list != null) && (objs != null))  {
                list.addAll(objs);
            }

            //BindingUtility.getInstance().getJAXBContext().createMarshaller().marshal(objs.get(0), System.err);
            //BindingUtility.getInstance().getJAXBContext().createMarshaller().marshal(sqlResult, System.err);
            //TODO: Not sure what this code was about but leaving it commented for now.

            /*
                // Attaching the ObjectRef to the response. objectsRefs contains duplicates!
                Iterator objectRefsIter = objectRefs.iterator();
                // It is to store the ObjectRef 's id after removing duplicated ObjectRef. It is a dirty fix, change it later!!!!
                List finalObjectRefsIds = new java.util.ArrayList();
                List finalObjectRefs = new java.util.ArrayList();
                while(objectRefsIter.hasNext()) {
                    Object obj = objectRefsIter.next();
                    if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ObjectRef) {
                        ObjectRef objectRef = (ObjectRef) obj;
                        String id = objectRef.getId();
                        if (!finalObjectRefsIds.contains(id)) {
                            finalObjectRefsIds.add(id);
                            ObjectRef or = new ObjectRef();
                            or.setId(id);
                            finalObjectRefs.add(or);
                        }
                    }
                    else {
                        throw new RegistryException("Unexpected object" + obj);
                    }
                }

                Iterator finalObjectRefsIter = finalObjectRefs.iterator();
                while (finalObjectRefsIter.hasNext()) {
                    Object obj = finalObjectRefsIter.next();
                    if (obj instanceof org.oasis.ebxml.registry.bindings.rim.ObjectRef) {
                        RegistryObjectListTypeTypeItem li = new RegistryObjectListTypeTypeItem();
                        li.setObjectRef((ObjectRef)obj);
                        sqlResult.addRegistryObjectListTypeTypeItem(li);
                    }
                    else {
                        throw new RegistryException("Unexpected object" + obj);
                    }
                }
            */
        } catch (UnsupportedEncodingException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (ParseException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (javax.xml.bind.JAXBException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        }

        return sqlResult;
    }

    public synchronized static SQLQueryProcessor getInstance() {
        if (instance == null) {
            instance = new org.freebxml.omar.server.query.sql.SQLQueryProcessor();
        }

        return instance;
    }
}
