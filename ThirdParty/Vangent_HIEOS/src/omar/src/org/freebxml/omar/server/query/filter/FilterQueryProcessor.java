/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/FilterQueryProcessor.java,v 1.19 2006/10/26 06:27:21 dougb62 Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.query.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.Utility;
import org.freebxml.omar.server.util.ServerResourceBundle;

import org.oasis.ebxml.registry.bindings.query.FilterQueryType;
import org.oasis.ebxml.registry.bindings.query.FilterType;

/**
 * Base type for all FilterQueryType.
 * Processes a FilterQueryType and converts it to an SQL Query string
 * using the convertToSQL() method.
 *
 * @author Nikola Stojanovic
 * @author Farrukh Najmi
 *
 */
public abstract class FilterQueryProcessor {
    protected FilterQueryProcessor parentQueryProcessor = null;
    protected String alias = null;
    protected String foreignKeyColumn = null;
    private ArrayList tableNameAliases = new ArrayList();

    // The FilterQUery for this class to process
    protected FilterQueryType filterQuery = null;

    // Keeps track for table aliases used by the SQL query as defined in the
    // "SELECT <alias>.<column> FROM <tablename> <alias> ..."
    // "SELECT ro.* FROM RegistryObjectTable ro ..."
    private HashSet aliases = new HashSet();

    private FilterQueryProcessor() {
    }

    public FilterQueryProcessor(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        this.parentQueryProcessor = parentQueryProcessor;
        this.filterQuery = filterQuery;
    }

    FilterQueryProcessor getParentQueryProcessor() {
        return parentQueryProcessor;
    }

    FilterQueryType getFilterQuery() {
        return filterQuery;
    }

    /**
     * Convert the specified FilterQueryType instance to an equivalent SQL query string.
     *
     */
    public String process() throws RegistryException {
        String sqlQuery = null;
        String tableName = Utility.getInstance().mapTableName(getRIMClassName());
        if (alias == null) {
            alias = getAliasForTable(tableName);
        }

        //Generate the "WHERE" clause for query which may have nested SELECTS etc.
        String whereClause = buildWhereClause();

        //Generate the initial part of query of form "SELECT tbl.* FROM <tablename> tbl"
        sqlQuery = buildSelectStatement();


        if (whereClause != null) {
            sqlQuery += whereClause;
        }

        return sqlQuery;
    }

    private String buildSelectStatement() throws RegistryException {
        String selectStatement = "";

        ////Only emit SELECT statement if top level FilterQueryProcessor (has no parent).
        //If this FilterQueryProcessor has a parentQueryProcessor
        //then use JOIN instead of NESTED SELECT as it makes the processing more efficient.
        //Use style (1) while avoiding style(2) below:
        //
        //1. SELECT ro.* FROM RegistryObject ro, Name nm WHERE (nm.parent = ro.id AND nm.value LIKE "%sun%");
        //1. SELECT ro.* FROM RegistryObject ro, Name nm1, ExternalIdentifier extId, Name nm2 WHERE ((nm1.parent = ro.id) AND (nm1.value LIKE "%sun%")) AND ((extId.registryObject = ro.id ) AND ((nm2.parent = extId.id) AND (nm2.value LIKE "%DUNS%")));
        //2. SELECT ro.* FROM RegistryObject ro WHERE (ro.id IN (SELECT nm.parent From Name_ nm WHERE nm.value LIKE "%sun%"));
        //2. SELECT ro.* FROM RegistryObject ro WHERE (ro.id IN (SELECT extId.registryObject From ExternalIdentifier extId WHERE extId.value = "DUNS"));

        if (parentQueryProcessor == null) {

            selectStatement = " SELECT DISTINCT ";
            String tableName = ((TableNameAlias)tableNameAliases.get(0)).tableName;
            String selectColumn = getSelectColumn();

            selectStatement += alias + "." + selectColumn + " FROM " + tableName + " " + alias + " ";

            for (int i=1; i<tableNameAliases.size(); i++) {
                tableName = ((TableNameAlias)tableNameAliases.get(i)).tableName;
                String otherAlias = ((TableNameAlias)tableNameAliases.get(i)).alias;
                selectStatement += ", " + tableName + " " + otherAlias;
            }
        }

        return selectStatement;
    }

    private String buildWhereClause() throws RegistryException {
        String whereClause = "";
        String filterPredicate = processFilters();
        String branchPredicate = processBranches();
        String subQueryPredicate = processSubQueries();

        if (!(Utility.containsWhiteSpacesOnly(filterPredicate))) {
            whereClause += filterPredicate;
        }

        whereClause = appendPredicate(whereClause, branchPredicate);
        whereClause = appendPredicate(whereClause, subQueryPredicate);

        //Only add "WHERE" if if top level FilterQueryProcessor (has no parent) AND
        //there are non-white-space characters in whereClause
        if (parentQueryProcessor == null) {
            if (!(Utility.containsWhiteSpacesOnly(whereClause))) {
                whereClause = " WHERE " + whereClause;
            }
        }
        return whereClause;
    }

    /**
     * Process filters for this filter querey and builds the SQL predicates
     * for the primary and secondary filters.
     *
     */
    protected String processFilters() throws RegistryException {
        String filterPredicates = " ";

        //First process the primaryFilter if any
        FilterType primaryFilter = filterQuery.getPrimaryFilter();

        if (primaryFilter != null) {
            FilterProcessor primaryFilterProcessor = FilterProcessor.newInstance(this, primaryFilter);
            String primaryFilterPredicate = primaryFilterProcessor.process();
            filterPredicates = appendPredicate(filterPredicates, primaryFilterPredicate);

        }

        //Processing of additional non-primary filters if any are done by sub-classes of this class

        return filterPredicates;
    }

    /**
     * Processes branches, if any, of this FilterQuery and
     * builds the SQL clauses for those branches.
     *
     */
    protected String processBranches() throws RegistryException {
        //Processing of branches handled by derived classes

        return " ";
    }

    /**
     * Processes sub-queries, if any, of this FilterQuery and
     * builds the SQL clauses for those sub-queries.
     *
     */
    protected String processSubQueries() throws RegistryException {
        //Processing of sub-queries handled by derived classes

        return "";
    }

    /**
     * Gets the RIM class name that matches this QueryProcessor.
     */
    private String getRIMClassName() {
        String name = Utility.getInstance().getClassNameNoPackage(this);
        //Remove "QueryProcessor" suffix
        if (name.endsWith("QueryProcessor")) {
            name = name.substring(0, name.length()-14);
        }
        //Remove "BranchProcessor" suffix
        if (name.endsWith("BranchProcessor")) {
            name = name.substring(0, name.length()-15);
        }

        return name;
    }

    /**
     * Gets the join predicate for a sub-query, or branch.
     *
     * Sample query (1) below exemplifies query where join is not required.
     * Sample query (2) below exemplifies query where join is required.
     *
     * 1. SELECT ro.* FROM RegistryObject ro WHERE
     *     (ro.status == "<status>")
     *
     * 2. SELECT ro.* FROM RegistryObject ro, VersionInfo v WHERE
     *     ((v.parent = ro.id) AND v.versionName = "1.2");
     *
     * ro = parentAlias
     * v = alias
     * status = filterColumn
     * versionName = filterColumn
     * parent = foreignKeyColumn
     * id = parent.primaryKeyColumn
     *
     * This method provides the (v.parent = ro.id) portion.  Filters are
     * added elsewhere
     */
    protected String getJoinPredicate() {
        String joinPredicate = "";

        if ((parentQueryProcessor != null) && (foreignKeyColumn != null)) {
            String parentAlias = parentQueryProcessor.getAlias();
            String parentPrimaryKeyColumn =
		parentQueryProcessor.getPrimaryKeyColumn();
	    String parentJoinPredicate = parentQueryProcessor.getJoinPredicate();

	    // Use full version of appendPredicate() to avoid recursion
	    joinPredicate =
		appendPredicate(joinPredicate,
				parentJoinPredicate,
				alias + "." + foreignKeyColumn + " = " +
				parentAlias + "." + parentPrimaryKeyColumn);
	}

        return joinPredicate;
    }

    /*
     * Gets the alias name for the table for this FilterQuery within the SELECT stmt.
     *
     */
    String getAlias() {
        return alias;
    }

    /*
     * Called by parent for branches and sub-queries.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /*
     * Gets the SQL table name for the table for this FilterQuery within the SELECT stmt.
     *
     */
    String getTableName() {
        String tableName = ((TableNameAlias)tableNameAliases.get(0)).tableName;

        return tableName;
    }

    private void addTableNameAndAlias(String tableName, String alias) {
        TableNameAlias tableNameAlias = new TableNameAlias(tableName, alias);
        tableNameAliases.add(tableNameAlias);
        aliases.add(alias);
    }

    /*
     * Gets the alias name for the table for this FilterQuery within the SELECT stmt.
     * Handles case where same table may be used more than once in a SELECT statment.
     * Forward to parentProcessor if any since only top level processor is emitting
     * SELECT statement that contains all aliases (no nested SELECTs).
     *
     */
    protected String getAliasForTable(String tableName) {
        String alias = null;

        if (parentQueryProcessor != null) {
            alias = parentQueryProcessor.getAliasForTable(tableName);
        } else {
            alias = tableName.toLowerCase();
            String originalAlias = alias;

            int i=0;
            while (aliases.contains(alias)) {
                alias = originalAlias + i;
                i++;
            }

            addTableNameAndAlias(tableName, alias);
        }

        return alias;
    }

    /*
     * Sets the foreign key column for the table matching this processor.
     * Must be called by a parentQueryProcessor on a child queryProcessor.
     *
     */
    void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    /*
     * Appends a new predicate to an old predicate using an AND logical operator between them
     * and used the default joinPred to constrain the newPred.
     * Handles case where old or new predicate is non-existent (white spaces only).
     *
     */
    protected String appendPredicate(String oldPred, String newPred) {
        if (!(Utility.containsWhiteSpacesOnly(newPred))) {
            String joinPred = getJoinPredicate();

	    oldPred = appendPredicate(oldPred, newPred, joinPred);
        }

        return oldPred;
    }

    /*
     * Appends a new predicate to an old predicate using an AND logical operator between them
     * and uses the specified relationshipPred to constrain the newPred.
     *
     * Handles case where old or new predicate is non-existent (white spaces only).
     *
     */
    protected String appendPredicate(String oldPred, String newPred, String relationshipPred) {
        if (!(Utility.containsWhiteSpacesOnly(newPred))) {
            if (!(Utility.containsWhiteSpacesOnly(relationshipPred))) {
                newPred = "(" + relationshipPred + "AND " + newPred + ") ";
            }

            if (!(Utility.containsWhiteSpacesOnly(oldPred))) {
                oldPred += "AND ";
            }
            oldPred += newPred;
        }

        return oldPred;
    }


    /*
     * Gets the primary key column for the table matching this processor.
     *
     */
    abstract String getPrimaryKeyColumn();

    private String getSelectColumn() throws RegistryException {
        if (parentQueryProcessor == null) {
            return "*";
        } else {
            if (foreignKeyColumn == null) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.foreignKeyColumnNotSet"));
            }
            return foreignKeyColumn;
        }
    }


    static FilterQueryProcessor newInstance(FilterQueryProcessor parentQueryProcessor, FilterQueryType filterQuery) throws RegistryException {
        FilterQueryProcessor filterQueryProcessor = null;
        String className = Utility.getInstance().getClassNameNoPackage(filterQuery);
        if (className.endsWith("Impl")) {
            className = className.substring(0, className.length()-4);   //Remove Impl suffix.
        }
        if (className.endsWith("Type")) {
            className = className.substring(0, className.length()-4);   //Remove Type suffix.
        }
        className = "org.freebxml.omar.server.query.filter." + className + "Processor";

        try {
            Class filterQueryProcessorClass = Class.forName(className);

            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = FilterQueryProcessor.class;
            parameterTypes[1] = FilterQueryType.class;
            Constructor constructor = filterQueryProcessorClass.getConstructor(parameterTypes);

            Object[] parameterValues = new Object[2];
            parameterValues[0] = parentQueryProcessor;
            parameterValues[1] = filterQuery;
            filterQueryProcessor = (FilterQueryProcessor) constructor.newInstance(parameterValues);
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

        return filterQueryProcessor;
    }

    private class TableNameAlias {
        String tableName = null;
        String alias = null;

        TableNameAlias(String tableName, String alias) {
            this.tableName = tableName;
            this.alias = alias;
        }
    }
}
