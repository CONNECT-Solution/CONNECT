/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/query/filter/SQLClause.java,v 1.2 2003/10/26 13:19:31 farrukh_najmi Exp $
 * ====================================================================
 */
/*
 * $Header:
 */
package org.freebxml.omar.server.query.filter;


/**
 * Class Declaration for ClauseType
 * @see
 * @author Nikola Stojanovic
 */
public class SQLClause {
    boolean isReverseSelectNeeded = false;
    boolean isSubSelectNeeded = false;
    String parentJoinColumn = "id";
    String clause = null;

    // insert accessors
}
