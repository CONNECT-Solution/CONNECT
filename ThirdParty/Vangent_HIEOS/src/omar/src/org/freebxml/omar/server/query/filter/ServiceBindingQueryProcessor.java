/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ServiceBindingQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;
import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;
import org.oasis.ebxml.registry.bindings.query.ServiceBindingQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class ServiceBindingQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ServiceBindingQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ServiceQuery
        RegistryObjectQueryType roQuery = ((ServiceBindingQueryType)filterQuery).getServiceQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String roAlias = getAliasForTable("Service"); 
            roQueryProcessor.setAlias(roAlias);
            
            String roQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".service = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
        
        //SpecificationLinkQuerys
        List specLinkQueries = ((ServiceBindingQueryType)filterQuery).getSpecificationLinkQuery();
         
        if (specLinkQueries != null) {
         
            Iterator iter = specLinkQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                registryObjectQueryProcessor.setForeignKeyColumn("serviceBinding");
                
                String specLinkPred = registryObjectQueryProcessor.process() + " ";
                subQueryPredicate = appendPredicate(subQueryPredicate, specLinkPred);
            }
        }
        
        //TargetBindingQuery
        roQuery = ((ServiceBindingQueryType)filterQuery).getTargetBindingQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String roAlias = getAliasForTable("ServiceBinding"); 
            roQueryProcessor.setAlias(roAlias);
            
            String roQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".targetBinding = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
        
        return subQueryPredicate;
    }
    
}
