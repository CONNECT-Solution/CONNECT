/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ServiceQueryProcessor.java,v 1.9 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;
import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.ServiceQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class ServiceQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ServiceQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
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
                
        //ServiceBindingQuerys
        List bindingQueries = ((ServiceQueryType)filterQuery).getServiceBindingQuery();
         
        if (bindingQueries != null) {
         
            Iterator iter = bindingQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                registryObjectQueryProcessor.setForeignKeyColumn("service");
                
                String bindingPred = registryObjectQueryProcessor.process() + " ";
                subQueryPredicate = appendPredicate(subQueryPredicate, bindingPred);
            }
        }
        
        return subQueryPredicate;
    }
    
}
