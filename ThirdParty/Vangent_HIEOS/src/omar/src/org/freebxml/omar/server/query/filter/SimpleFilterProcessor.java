/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/SimpleFilterProcessor.java,v 1.4 2005/04/21 19:24:22 joehw Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.util.ServerResourceBundle;

import org.oasis.ebxml.registry.bindings.query.Comparator;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.SimpleFilterType;

/**
 * FilterProcessor subtype for processing SimpleFilters
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
abstract class SimpleFilterProcessor extends FilterProcessor {
    private String filterColumn =  null;
    //private String tableName = null;
    
    SimpleFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
        filterColumn = org.freebxml.omar.common.Utility.getInstance().mapColumnName(((SimpleFilterType)filter).getDomainAttribute());
        
        //tableName = org.freebxml.omar.common.Utility.getInstance().getClassNameNoPackage(getParentFilterQuery());
    }
    
    /*
     * Must be called by the parentQueryProcessor after creating this object.
     *
    public void setFilterDomainClass(String filterDomainClass) {
        tableName = org.freebxml.omar.common.Utility.getInstance().mapTableName(filterDomainClass);
    }*/
            
    /**
     * Builds the SQL predicates for the SimpleFilter.
     * Join predicate is added by the parentQueryProcessor
     * and not this Filter.
     *
     * Sample query (1) below exemplifies filterPredicate where join is not required.
     * Sample query (2) below exemplifies filterPredicate where join is required.
     *
     * 1. SELECT ro.* FROM RegistryObject ro WHERE
     *     (ro.status == "<status>")
     *
     * 2. SELECT ro.* FROM RegistryObject ro, VersionInfo v WHERE
     *     (v.parent = ro.id AND v.versionName = "1.2");
     *
     * ro = parentAlias
     * v = alias
     * status = filterColumn
     * versionName = filterColumn
     * parent = foreignKeyColumn
     * id = parent.primaryKeyColumn
     *
     */
    protected String processInternal() throws RegistryException {
        String filterPredicate = null;
        String alias = getAlias();
        
        filterPredicate = "(" + alias + "." + filterColumn +
                mapComparatorToSQL(((SimpleFilterType)filter).getComparator()) +
                getValue() + ") ";        
        
        return filterPredicate;
    }
    
    abstract protected String getValue() throws RegistryException;
    
    private String mapComparatorToSQL(Comparator comparator) throws RegistryException {
        String sqlComparator = null;
        
        if (comparator == Comparator.EQ) {
            sqlComparator = " = ";
        } else if (comparator == Comparator.GE) {
            sqlComparator = " >= ";
        } else if (comparator == Comparator.GT) {
            sqlComparator = " > ";
        } else if (comparator == Comparator.LE) {
            sqlComparator = " <= ";
        } else if (comparator == Comparator.LIKE) {
            sqlComparator = " LIKE ";
        } else if (comparator == Comparator.LT) {
            sqlComparator = " < ";
        } else if (comparator == Comparator.NE) {
            sqlComparator = " != ";
        } else if (comparator == Comparator.NOT_LIKE) {
            sqlComparator = " NOT LIKE ";
        } else {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.unsuportedComparatorAttribute"));
        }
        
        return sqlComparator;
    }
    
}
