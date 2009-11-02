/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/SubscriptionQueryProcessor.java,v 1.3 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;


/**
 * Processor for ExternalIdentifierQuerys.
 * Processes a ExternalIdentifierQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class SubscriptionQueryProcessor extends RegistryObjectQueryProcessor {
    
    public SubscriptionQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        super(parentQueryProcessor, filterQuery);
    }
    
    
    /**
     * Builds the SQL predicates for the primary and additional filters for this FilterQuery
     *
     * SELECT ro.* FROM RegistryObject ro, VersionInfo v WHERE (ro.status == "<status>") AND (v.parent = ro.id AND v.versionName = "1.2");
     *
     */
    protected String processFilters() throws RegistryException {
        //Processing of primary filter if any are done by super class
        String filterPredicates = super.processFilters();        
        
        /*
        //Process additional non-primary filters if any
        FilterType versionInfoFilter = ((RegistryObjectQueryType)filterQuery).getVersionInfoFilter();
        if (versionInfoFilter != null) {
            FilterProcessor versionInfoFilterProcessor = FilterProcessor.newInstance(this, versionInfoFilter);
            
            if (versionInfoFilter instanceof SimpleFilterType) {
                //VersionInfoFilter is special as it does not require join because VersionInfo table
                //is flattened in RegistryObjectTable. (SimpleFilterProcessor)
                
                //String alias = getAliasForTable("VersionInfo");
                //versionInfoFilterProcessor.setJoinColumn("parent");
                ((SimpleFilterProcessor)versionInfoFilterProcessor).setAlias(getAlias());
            }
            String versionInfoFilterPredicate = versionInfoFilterProcessor.process();
            filterPredicates += " AND " + versionInfoFilterPredicate;
        }
        */
        
        return filterPredicates;
    }
    
    /**
     * Processes branches, if any, of this RegistryObjectQuery and
     * builds the SQL clauses for those branches.
     *
     * Avoid Nested SELECT due to performance issue and lack of support on some dbs.
     * Use style (1) while avoiding style(2) below:
     *
     * 1. SELECT ro.* FROM RegistryObject ro, Name nm WHERE (nm.parent = ro.id AND nm.value LIKE "%sun%");
     * 2. SELECT ro.* FROM RegistryObject ro WHERE (ro.id IN (SELECT nm.parent From Name_ nm WHERE nm.value LIKE "%sun%"));
     *
     */
    protected String processBranches() throws RegistryException {
        String branchPredicate = super.processBranches();
                
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
                
        return subQueryPredicate;
    }
    
}
