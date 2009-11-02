/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/ArbitraryQueryQueryPlugin.java,v 1.1 2006/03/24 00:03:30 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.util.Map;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.CanonicalConstants;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequestType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQueryType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.QueryExpressionType;

/**
 * The QueryPlugin for ArbitraryQuery special parameterized query.
 * 
 * @author Farrukh Najmi
 *
 */
public class ArbitraryQueryQueryPlugin extends AbstractQueryPlugin {
    
    private Log log = LogFactory.getLog(ArbitraryQueryQueryPlugin.class);

    
    public void processRequest(RequestContext context) throws RegistryException {
        ServerRequestContext serverContext = ServerRequestContext.convert(context);        
        Map queryParamsMap = serverContext.getQueryParamsMap();        
        AdhocQueryRequestType newReq = (AdhocQueryRequestType)((ServerRequestContext)context).getCurrentRegistryRequest();

        String queryStr = (String)queryParamsMap.get("$query");
                
        //Replace query with $query param value
        AdhocQueryType query = newReq.getAdhocQuery();
        QueryExpressionType queryExp = query.getQueryExpression();
        String queryLang = queryExp.getQueryLanguage();
        queryExp.getContent().clear();
        queryExp.getContent().add(queryStr);        
    }

    public String getId() {
        return CanonicalConstants.CANONICAL_QUERY_ArbitraryQuery;
    }

    public InternationalStringType getName() {
        InternationalStringType is = null;
        try {
            //??I18N: Need a ResourceBundle class for minDB
            is = bu.createInternationalStringType("ArbitraryQuery");
        } catch (JAXRException e) {
            //can't happen
            log.error(e);
        }
        
        return is;
    }

    public InternationalStringType getDescription() {
        InternationalStringType is = null;
        try {
            //??I18N: Need a ResourceBundle class for minDB
            is = bu.createInternationalStringType("ArbitraryQuery");
        } catch (JAXRException e) {
            //can't happen
            log.error(e);
        }
        
        return is;
    }

}
