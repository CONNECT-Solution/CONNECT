/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/RegistryObjectQueryProcessor.java,v 1.9 2005/03/29 09:22:57 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.util.Iterator;
import java.util.List;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.InternationalStringBranchType;
import org.oasis.ebxml.registry.bindings.query.RegistryObjectQueryType;
import org.oasis.ebxml.registry.bindings.query.SlotBranchType;


/**
 * Base type for all RegistryObjectQueryProcessors.
 * Processes a RegistryObjectFilterQueryType and converts it to an SQL Query string
 * using the process() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
class RegistryObjectQueryProcessor extends FilterQueryProcessor {
    
    public RegistryObjectQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
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
        
        //Process additional non-primary filters if any
        FilterType versionInfoFilter = ((RegistryObjectQueryType)filterQuery).getVersionInfoFilter();
        if (versionInfoFilter != null) {
            FilterProcessor versionInfoFilterProcessor = FilterProcessor.newInstance(this, versionInfoFilter);
            
            //VersionInfoFilter is special as it does not require join because VersionInfo table
            //is flattened in RegistryObjectTable. Treated similar to primary filter.
            filterPredicates = appendPredicate(filterPredicates, versionInfoFilterProcessor.process() + " ");
        }
        
        return filterPredicates;
    }
    
    /**
     * Processes branches, if any, of this RegistryObjectQuery and
     * builds the SQL clauses for those branches.
     *
     * Use Nested SELECT as it makes the design simpler.
     * Use style (2) while avoiding style(1) below:
     *
     * 1. SELECT ro.* FROM RegistryObject ro, Name nm WHERE (nm.parent = ro.id AND nm.value LIKE "%sun%");
     * 2. SELECT ro.* FROM RegistryObject ro WHERE (ro.id IN (SELECT nm.parent From Name_ nm WHERE nm.value LIKE "%sun%"));
     *
     */
    protected String processBranches() throws RegistryException {
        String branchPredicate = super.processBranches();
        
        String alias = getAlias();
        
        //Name Table in SQL schema is handled special because InternationalString 
        //and LocalizedString tables have been flattened into a Name table
        //As such, they can be processed in a manner similar to secondary filters
        InternationalStringBranchType nameBranch = ((RegistryObjectQueryType)filterQuery).getNameBranch();
        if (nameBranch != null) {            
            FilterQueryProcessor nameBranchProcessor = FilterQueryProcessor.newInstance(this, nameBranch);
            nameBranchProcessor.setForeignKeyColumn("parent");
            nameBranchProcessor.setAlias(getAliasForTable("Name_"));
            
            branchPredicate = appendPredicate(branchPredicate, nameBranchProcessor.process() + " ");            
        }        
        
        //Name Table in SQL schema is handled special because InternationalString 
        //and LocalizedString tables have been flattened into a Name table
        //As such, they can be processed in a manner similar to secondary filters
        InternationalStringBranchType descBranch = ((RegistryObjectQueryType)filterQuery).getDescriptionBranch();
        if (descBranch != null) {            
            FilterQueryProcessor descBranchProcessor = FilterQueryProcessor.newInstance(this, descBranch);
            descBranchProcessor.setForeignKeyColumn("parent");
            descBranchProcessor.setAlias(getAliasForTable("Description"));
            branchPredicate = appendPredicate(branchPredicate, descBranchProcessor.process() + " ");           
        }        
        
        List slotBranches = ((RegistryObjectQueryType)filterQuery).getSlotBranch();
        Iterator iter = slotBranches.iterator();
        while (iter.hasNext()) {
            SlotBranchType slotBranch = (SlotBranchType)iter.next();
            if (slotBranch != null) {            
                FilterQueryProcessor slotBranchProcessor = FilterQueryProcessor.newInstance(this, slotBranch);
                slotBranchProcessor.setForeignKeyColumn("parent");
                //slotBranchProcessor.setAlias(getAliasForTable("Slot"));
                branchPredicate = appendPredicate(branchPredicate, slotBranchProcessor.process() + " ");            
            }     
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
        
        //ClassificationQuerys
        List classificationQueries = ((RegistryObjectQueryType)filterQuery).getClassificationQuery();
         
        if (classificationQueries != null) {
         
            Iterator iter = classificationQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                registryObjectQueryProcessor.setForeignKeyColumn("classifiedObject");
                
                //String alias = getAliasForTable("ExternalIdentifier"); 
                //registryObjectQueryProcessor.setAlias(alias);
                String classificationPred = registryObjectQueryProcessor.process() + " ";
                subQueryPredicate = appendPredicate(subQueryPredicate, classificationPred);
            }
        }        
        
        //ExternalIdentifierQuerys
        List extIdQueries = ((RegistryObjectQueryType)filterQuery).getExternalIdentifierQuery();
         
        if (extIdQueries != null) {
         
            Iterator iter = extIdQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                registryObjectQueryProcessor.setForeignKeyColumn("registryObject");
                
                //String alias = getAliasForTable("ExternalIdentifier"); 
                //registryObjectQueryProcessor.setAlias(alias);
                String extIdPred = registryObjectQueryProcessor.process() + " ";
                subQueryPredicate = appendPredicate(subQueryPredicate, extIdPred);
            }
        }
        
        //ObjectTypeQuery      
        RegistryObjectQueryType objTypeQuery = ((RegistryObjectQueryType)filterQuery).getObjectTypeQuery();
         
        if (objTypeQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, objTypeQuery);
            String objTypeAlias = getAliasForTable("ClassificationNode"); 
            subQueryProcessor.setAlias(objTypeAlias);

            String objTypeQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".objectType = " + objTypeAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, objTypeQueryPred, relationShipPred);            
        }
        
        //StatusQuery       
        RegistryObjectQueryType statusQuery = ((RegistryObjectQueryType)filterQuery).getStatusQuery();
         
        if (statusQuery != null) {
                  
            RegistryObjectQueryProcessor subQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, statusQuery);
            String statusAlias = getAliasForTable("ClassificationNode"); 
            subQueryProcessor.setAlias(statusAlias);

            String statusQueryPred = subQueryProcessor.process() + " ";

            String relationShipPred = "(" + alias + ".status = " + statusAlias + ".id) ";
            subQueryPredicate = appendPredicate(subQueryPredicate, statusQueryPred, relationShipPred);            
        }
        
        //SourceAssociationQuerys
        List srcAssQueries = ((RegistryObjectQueryType)filterQuery).getSourceAssociationQuery();
         
        if (srcAssQueries != null) {
         
            Iterator iter = srcAssQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                
                String srcAssAlias = getAliasForTable("Association"); 
                registryObjectQueryProcessor.setAlias(srcAssAlias);
                
                String srcAssQueryPred = registryObjectQueryProcessor.process() + " ";
                
                String relationShipPred = "(" + alias + ".id = " + srcAssAlias + ".sourceObject) ";
                subQueryPredicate = appendPredicate(subQueryPredicate, srcAssQueryPred, relationShipPred);            
            }
        }        
        
        //TargetAssociationQuerys
        List targetAssQueries = ((RegistryObjectQueryType)filterQuery).getTargetAssociationQuery();
         
        if (targetAssQueries != null) {
         
            Iterator iter = targetAssQueries.iterator();
            while (iter.hasNext()) {
                FilterQueryType subQuery = (FilterQueryType)iter.next();
         
                RegistryObjectQueryProcessor registryObjectQueryProcessor = (RegistryObjectQueryProcessor)FilterQueryProcessor.newInstance(this, subQuery);
                
                String targetAssAlias = getAliasForTable("Association"); 
                registryObjectQueryProcessor.setAlias(targetAssAlias);
                
                String targetAssQueryPred = registryObjectQueryProcessor.process() + " ";
                
                String relationShipPred = "(" + alias + ".id = " + targetAssAlias + ".targetObject) ";
                subQueryPredicate = appendPredicate(subQueryPredicate, targetAssQueryPred, relationShipPred);            
            }
        }        
        
        
        return subQueryPredicate;
    }
    
    /**
     * Gets the primary key column name for a RegistryObject
     */
    String getPrimaryKeyColumn() {
        return "id";        
    }
    
}
