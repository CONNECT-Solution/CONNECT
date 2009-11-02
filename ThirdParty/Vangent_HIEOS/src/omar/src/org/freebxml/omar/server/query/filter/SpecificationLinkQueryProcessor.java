/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/SpecificationLinkQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.InternationalStringBranchType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;
import org.oasis.ebxml.registry.bindings.query.SpecificationLinkQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class SpecificationLinkQueryProcessor extends RegistryObjectQueryProcessor {
    
    public SpecificationLinkQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
            
    /**
     * Processes branches, if any, of this RegistryObjectQuery and
     * builds the SQL clauses for those branches.
     *
     *
     */
    protected String processBranches() throws RegistryException {
        String branchPredicate = super.processBranches();
                
        //UsageDescription Table in SQL schema is handled special because InternationalString 
        //and LocalizedString tables have been flattened into a Name table
        //As such, they can be processed in a manner similar to secondary filters
        InternationalStringBranchType usageDescBranch = ((SpecificationLinkQueryType)filterQuery).getUsageDescriptionBranch();
        if (usageDescBranch != null) {            
            FilterQueryProcessor usageDescBranchProcessor = FilterQueryProcessor.newInstance(this, usageDescBranch);
            usageDescBranchProcessor.setForeignKeyColumn("parent");
            usageDescBranchProcessor.setAlias(getAliasForTable("UsageDescription"));
            
            branchPredicate = appendPredicate(branchPredicate, usageDescBranchProcessor.process() + " ");            
        }        
        
        return branchPredicate;
    }
    
    /**
     * Builds the SQL clauses for the sub-queries of this FilterQuery.
     *
     * SELECT ro.* FROM RegistryObject ro WHERE ro.id IN (SELECT c.id FROM Classification c WHERE c.registryObject = ro.id AND c.value = "somevalue");
     *
     */
    protected String processSubQueries() throws RegistryException {
        String subQueryPredicate = super.processSubQueries();
                
        //ServiceBindingQuery
        RegistryObjectQueryType roQuery = ((SpecificationLinkQueryType)filterQuery).getServiceBindingQuery();
         
        if (roQuery != null) {         
            RegistryObjectQueryProcessor roQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, roQuery);

            String roAlias = getAliasForTable("ServiceBinding"); 
            roQueryProcessor.setAlias(roAlias);
            
            String roQueryPred = roQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".serviceBinding = " + roAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, roQueryPred, relationShipPred);            
        }
        
        //SpecificationObjectQuery     
        RegistryObjectQueryType specQuery = ((SpecificationLinkQueryType)filterQuery).getSpecificationObjectQuery();
         
        if (specQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, specQuery);
            String specAlias = getAliasForTable("RegistryObject"); 
            subQueryProcessor.setAlias(specAlias);

            String specQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".specificationObject = " + specAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, specQueryPred, relationShipPred);            
        }
        return subQueryPredicate;
    }
    
}
