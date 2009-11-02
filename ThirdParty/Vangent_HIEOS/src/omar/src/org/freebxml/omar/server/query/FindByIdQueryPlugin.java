/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/FindByIdQueryPlugin.java,v 1.2 2006/04/28 15:59:31 farrukh_najmi Exp $
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
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 * The QueryPlugin for FindObjectByIdAndType special parameterized query.
 * 
 * @author Farrukh Najmi
 *
 */
public class FindByIdQueryPlugin extends AbstractQueryPlugin {
    
    private Log log = LogFactory.getLog(FindByIdQueryPlugin.class);

    
    public void processRequest(RequestContext context) throws RegistryException {
        ServerRequestContext serverContext = ServerRequestContext.convert(context);
        
        Map queryParamsMap = serverContext.getQueryParamsMap();
        String objectId = (String)queryParamsMap.get("$id");
        String tableName = (String) queryParamsMap.get("$tableName");
        
        tableName = org.freebxml.omar.common.Utility.getInstance().mapTableName(tableName);
        RegistryObjectType ro = ServerCache.getInstance().getRegistryObject((ServerRequestContext)context, objectId, tableName);
        ArrayList results = new ArrayList();
        results.add(ro);
        serverContext.setSpecialQueryResults(results);        
    }

    public String getId() {
        return CanonicalConstants.CANONICAL_QUERY_FindObjectByIdAndType;
    }

    public InternationalStringType getName() {
        InternationalStringType is = null;
        try {
            //??I18N: Need a ResourceBundle class for minDB
            is = bu.createInternationalStringType("FindObjectByIdAndType");
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
            is = bu.createInternationalStringType("FindObjectByIdAndType");
        } catch (JAXRException e) {
            //can't happen
            log.error(e);
        }
        
        return is;
    }

}
