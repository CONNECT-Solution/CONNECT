/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/NotificationQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.NotificationQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class NotificationQueryProcessor extends RegistryObjectQueryProcessor {
    
    public NotificationQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     * SELECT ro.* FROM RegistryObject ro WHERE ro.id IN (SELECT c.id FROM Classification c WHERE c.registryObject = ro.id AND c.value = "somevalue");
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //RegistryObjectQuery //TODO      
        RegistryObjectQueryType roQuery = ((NotificationQueryType)filterQuery).getRegistryObjectQuery();
         
        if (roQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);
            String roAlias = getAliasForTable("NotificationObject"); 
            subQueryProcessor.setAlias(roAlias);

            String roQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".sourceObject = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
        return subQueryPredicate;
    }
    
}
