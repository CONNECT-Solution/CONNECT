/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/RRFilterQueryProcessor.java,v 1.6 2007/09/28 15:49:20 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.freebxml.omar.common.IterativeQueryParams;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.query.sql.SQLQueryProcessor;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.BranchType;
import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.ResponseOptionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 * Class Declaration for FilterQueryProcessor
 * @see
 * @author Nikola Stojanovic
 */
public class RRFilterQueryProcessor {
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private FilterQueryProcessor _filterQueryProcessor; */
    private static RRFilterQueryProcessor instance = null;
    private static SQLQueryProcessor sqlQueryProcessor = SQLQueryProcessor.getInstance();

    protected RRFilterQueryProcessor() {
    }

    public RegistryObjectListType executeQuery(ServerRequestContext context, UserType user,
        FilterQueryType filterQuery, ResponseOptionType responseOption, IterativeQueryParams paramHolder)
        throws RegistryException {
        String sqlQuery = null;
        
        if (filterQuery instanceof BranchType) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.branchQuery"));
        } else {
            RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(null, filterQuery);
            sqlQuery = registryObjectQueryProcessor.process();
            
        }

        RegistryObjectListType roList = sqlQueryProcessor.executeQuery(context, user,
                sqlQuery, responseOption, paramHolder, false);

        return roList;
    }

    public synchronized static RRFilterQueryProcessor getInstance() {
        if (instance == null) {
            instance = new org.freebxml.omar.server.query.filter.RRFilterQueryProcessor();
        }

        return instance;
    }
}
