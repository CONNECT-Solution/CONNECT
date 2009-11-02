/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/GetSchemesByIdQueryPlugin.java,v 1.2 2006/04/28 15:59:31 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query;

import java.util.List;
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

/**
 * The QueryPlugin for GetClassificationSchemesById special parameterized query.
 * 
 * @author Farrukh Najmi
 *
 */
public class GetSchemesByIdQueryPlugin extends AbstractQueryPlugin {
    
    private Log log = LogFactory.getLog(GetSchemesByIdQueryPlugin.class);

    
    public void processRequest(RequestContext context) throws RegistryException {
        ServerRequestContext serverContext = ServerRequestContext.convert(context);
        
        Map queryParamsMap = serverContext.getQueryParamsMap();        
        
        //Use ClassificationSchemeCache to resolve this query
        String idPattern = (String)queryParamsMap.get("$idPattern");
        if (idPattern == null) {
            idPattern = "%";
        }
        List schemes = ServerCache.getInstance().getClassificationSchemesById(serverContext, idPattern);
        serverContext.setSpecialQueryResults(schemes);        
    }

    public String getId() {
        return CanonicalConstants.CANONICAL_QUERY_GetClassificationSchemesById;
    }

    public InternationalStringType getName() {
        InternationalStringType is = null;
        try {
            //??I18N: Need a ResourceBundle class for minDB
            is = bu.createInternationalStringType("GetClassificationSchemesById");
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
            is = bu.createInternationalStringType("GetClassificationSchemesById");
        } catch (JAXRException e) {
            //can't happen
            log.error(e);
        }
        
        return is;
    }

}
