/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/FilterProcessor.java,v 1.10 2005/03/29 09:22:56 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.registry.RegistryException;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;


/**
 * Based class for types of FilterProcessor clases.
 * 
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
abstract class FilterProcessor {
    protected FilterType filter = null;
    protected FilterQueryProcessor parentQueryProcessor = null;
    protected String alias = null;

    private FilterProcessor() {        
    }
    
    public FilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        this.parentQueryProcessor = parentQueryProcessor;
        this.filter = filter;
    }
    
    FilterQueryType getParentFilterQuery() {
        return parentQueryProcessor.getFilterQuery();
    }

    void setAlias(String alias) {
        this.alias = alias;
    }
    
    String getAlias() {
        if (alias == null) {
            //Handles case where filter is a secondary filter
            alias = parentQueryProcessor.getAlias();
        }
        
        return alias;
    }
    
    protected boolean isPrimaryFilter() {
        return ((getParentFilterQuery().getPrimaryFilter()) == filter);
    }
    
    /*
     * Returns whether the predicate for this Filter requires a join between the parent table and the 
     * filter domain class table.
     *
     * Note that primayFilters never require joins while secondary filter typically do.
     * The exception is when a RIM class has been flattened into parent class in SQL schema
     * (e.g. VersionInfo in RegistryObject). The way to distinguish teh special case is that
     * the
     *
     */
    protected boolean requiresJoin() {
        return !isPrimaryFilter();
    }
    
    static FilterProcessor newInstance(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        FilterProcessor filterProcessor = null;
        
        String className = org.freebxml.omar.common.Utility.getInstance().getClassNameNoPackage(filter);
        if (className.endsWith("Impl")) {
            className = className.substring(0, className.length()-4);   //Remove "Impl" suffix.
        }
        if (className.endsWith("Type")) {
            className = className.substring(0, className.length()-4);   //Remove "Type" suffix.
        }
        
        className = "org.freebxml.omar.server.query.filter." + className + "Processor";
        
        try {            
            Class filterProcessorClass = Class.forName(className);
            
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = FilterQueryProcessor.class;
            parameterTypes[1] = FilterType.class;
            Constructor constructor = filterProcessorClass.getConstructor(parameterTypes);
            
            Object[] parameterValues = new Object[2];
            parameterValues[0] = parentQueryProcessor;                
            parameterValues[1] = filter;                
            filterProcessor = (FilterProcessor) constructor.newInstance(parameterValues);    
            
            //filterProcessor = new CompoundFilterProcessor(parentQueryProcessor, filter);
        } catch (ClassNotFoundException e) {
            throw new RegistryException(e);
        } catch (NoSuchMethodException e) {
            throw new RegistryException(e);
        } catch (IllegalArgumentException e) {
            throw new RegistryException(e);
        } catch (IllegalAccessException e) {
            throw new RegistryException(e);
        } catch (InvocationTargetException e) {
            throw new RegistryException(e);
        } catch (ExceptionInInitializerError e) {
            throw new RegistryException(e);
        } catch (InstantiationException e) {
            throw new RegistryException(e);
        }            
        
        return filterProcessor;
    }
    
    /*
     * Converts filter to an SQL predicate
     */
    public String process() throws RegistryException {
        String filterPredicate = processInternal();
        
        if (filter.isNegate()) {
            filterPredicate = " ( NOT (" + filterPredicate + ")) ";
        }
        
        return filterPredicate;
    }
    
    protected abstract String processInternal() throws RegistryException;
    
}
