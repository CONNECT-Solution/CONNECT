/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2005 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/IterativeQueryParams.java,v 1.1 2005/03/02 18:29:16 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

/**
 * This class is used to hold parameters used in execute iterative queries
 * 
 * @author Paul Sterk
 */
public class IterativeQueryParams {
    
    // The starting index in a single query iteration
    public int startIndex = 0;
    // The maximum number of results that are returned in a single query interation
    public int maxResults = -1;
    // The total number of results contained in the result set
    public int totalResultCount = -1;
    
    /** 
     * Default Constructor
     *
     * Parameters are set to default values
     */
    public IterativeQueryParams() {
    }
    
    /** 
     * Constructor
     * 
     * @param startIndex
     *  The starting index to use
     * @param maxResults
     *  The maximum results to return
     */
    public IterativeQueryParams(int startIndex, int maxResults) {
        this.startIndex = startIndex;
        this.maxResults = maxResults;
    }
}
