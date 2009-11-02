/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/ExternalIdentifierQueryProcessor.java,v 1.3 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.ExternalIdentifierQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
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
class ExternalIdentifierQueryProcessor extends RegistryObjectQueryProcessor {
    
    public ExternalIdentifierQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
        
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //RegistryObjectQuery      
        RegistryObjectQueryType roQuery = ((ExternalIdentifierQueryType)filterQuery).getRegistryObjectQuery();
         
        if (roQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);
            String roAlias = getAliasForTable("RegistryObject"); 
            subQueryProcessor.setAlias(roAlias);

            String roQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".registryObject = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
        
        //IdentificationSchemeQuery        
        //SELECT ro.* FROM ExternalIdentifier e, ClassificationScheme s WHERE ((e.identificationScheme = s.id) AND (s ...))
        roQuery = ((ExternalIdentifierQueryType)filterQuery).getIdentificationSchemeQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String schemeAlias = getAliasForTable("ClassScheme"); 
            roQueryProcessor.setAlias(schemeAlias);
            
            String schemeQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".identificationScheme = " + schemeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, schemeQueryPred, relationShipPred);            
        }
                
        
        
        return subQueryPredicate;
    }
    
}
