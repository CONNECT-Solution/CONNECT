/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/RegistryQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;
import org.oasis.ebxml.registry.bindings.query.RegistryQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class RegistryQueryProcessor extends RegistryObjectQueryProcessor {
    
    public RegistryQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //OperatorQuery        
        //SELECT o.* FROM Registry r, Organization o WHERE ((r.operator = o.id) AND (o ...));
        RegistryObjectQueryType roQuery = ((RegistryQueryType)filterQuery).getOperatorQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String operatorAlias = getAliasForTable("Organization"); 
            roQueryProcessor.setAlias(operatorAlias);
            
            String operatorQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".operator = " + operatorAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, operatorQueryPred, relationShipPred);            
        }        
        
        return subQueryPredicate;
    }
    
}
