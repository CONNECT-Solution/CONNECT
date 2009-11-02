/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2007 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/GetClassificationNodeByPathQueryPlugin.java,v 1.2 2007/06/06 21:54:13 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.util.ArrayList;
import java.util.Map;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.CanonicalConstants;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;

/**
 * The QueryPlugin for GetClassificationNodeByPath special parameterized query.
 * 
 * @author Paul Sterk
 *
 */
public class GetClassificationNodeByPathQueryPlugin extends AbstractQueryPlugin {
    
    private Log log = LogFactory.getLog(GetClassificationNodeByPathQueryPlugin.class);

    
    public void processRequest(RequestContext context) throws RegistryException {
        ServerRequestContext serverContext = ServerRequestContext.convert(context);
        
        Map queryParamsMap = serverContext.getQueryParamsMap();
        String path = (String)queryParamsMap.get("$path");
        
        ClassificationNodeType ro = ServerCache.getInstance().getClassificationNodeByPath(path);
        ArrayList results = new ArrayList();
        if (ro != null) {
            results.add(ro);
        }
        // Need to set serverContext.specialQueryResults so that query is 
        // detected as special query, but don't add null as result or server will 
        // try to check access to null.
        serverContext.setSpecialQueryResults(results);        
    }

    public String getId() {
        return CanonicalConstants.CANONICAL_QUERY_GetClassificationNodeByPath;
    }

    public InternationalStringType getName() {
        InternationalStringType is = null;
        try {
            //??I18N: Need a ResourceBundle class for minDB
            is = bu.createInternationalStringType("GetClassificationNodeByPath");
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
            is = bu.createInternationalStringType("GetClassificationNodeByPath");
        } catch (JAXRException e) {
            //can't happen
            log.error(e);
        }
        
        return is;
    }

}
