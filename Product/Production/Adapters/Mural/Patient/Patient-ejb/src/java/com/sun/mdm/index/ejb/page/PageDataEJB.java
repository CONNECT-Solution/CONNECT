/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.ejb.page;

import java.util.ArrayList;
import java.util.Comparator;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import com.sun.mdm.index.page.PageAdapter;
import com.sun.mdm.index.page.PageException;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Session bean to store a data result on the server and give client the data
 * one page at a time.
 */
@Stateful(mappedName="ejb/PatientPageData")
@Remote(PageDataRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/PatientDataSource" )
public class PageDataEJB implements PageDataRemote{
    /** Number of objects in the adapter.
     */    
    private int mObjectCount = -1;
    /** Current position of the adapter.
     */    
    private int mCurrentPosition = 0;
    /** The adapter for the data source
     */    
    private PageAdapter mPageAdapter;
    /** Forward only mode
     */      
    private boolean mForwardOnly = false;
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
  

    /**
     * Creates a new instance of PageDataEJB
     */
    public PageDataEJB() {
        /*
          To_Do: Spawn a thread to keep track of idle timeout.  Thread calls
          idleTimeOut() method.  Thread is destroyed during passivation
          and recreated during activation.
          */
    }


    /** Return the number of objects in the result set. 
     * @see PageData#count()
     * @exception PageException See PageData
     * @return See PageData
     */
    public int count()
        throws PageException {
        if (mObjectCount == -1) {
            mObjectCount = mPageAdapter.count();
        }
        return mObjectCount;
    }


    /** Standard EJB3 callback method.
     * @see javax.ejb.PostActivate
     */
    @PostActivate
    public void activate() {
        mPageAdapter.activate();
    }

    /** Standard EJB3 callback method.
     * @see javax.ejb.PrePassivate
     */
    @PrePassivate
    public void passivate() {
        mPageAdapter.passivate();
    }


    /** Standard EJB3 callback method.
     * @see javax.annotation.PreDestroy
     */
    @PreDestroy
    public void remove() {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Removing pageDateEJB.");
        }
        mPageAdapter.close();
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Finished removing pageDateEJB.");
        }
    }

    
    /** See PageData
     * @see PageData#setPageAdapter(PageAdapter pageAdapter)
     * @param pageAdapter See PageData
     */
    public void setPageAdapter(PageAdapter pageAdapter)
        throws PageException{    
        mPageAdapter = pageAdapter;
    }
    
    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * 
     * @param forwardOnly See PageData
     * @exception PageException See PageData
     */
    public void setReadForwardOnly(boolean forwardOnly) 
        throws PageException {
        mForwardOnly = forwardOnly;
    }    

    /**See PageData
     * @see PageData#next(int count)
     * @param count See PageData
     * @exception PageException See PageData
     * @return See PageData
     */
    public ArrayList next(int count)
        throws PageException {
        ArrayList list = new ArrayList();
        mPageAdapter.setReadForwardOnly(mForwardOnly);
        while (mPageAdapter.hasNext() && count > 0) {
            list.add(mPageAdapter.next());
            mCurrentPosition++;
            count--;
        }
        if (list.size() == 0) {
            return null;
        } else {
            return list;
        }
    }


    /** See PageData
     * @see PageData#next(int index, int count)
     * @param index See PageData
     * @param count See PageData
     * @exception PageException See PageData
     * @return See PageData
     */
    public ArrayList next(int index, int count)
        throws PageException {
        if (mCurrentPosition != index) {
            mPageAdapter.setCurrentPosition(index);
            mCurrentPosition = index;
        }
        return next(count);
    }


    /** See PageData
     * @see PageData#prev(int count)
     * @param count See PageData
     * @exception PageException See PageData
     * @return See PageData
     */
    public ArrayList prev(int count)
        throws PageException {
        ArrayList list = new ArrayList();
        while (mCurrentPosition > 0 && count > 0) {
            list.add(mPageAdapter.prev());
            mCurrentPosition--;
            count--;
        }
        if (list.size() == 0) {
            return null;
        } else {
            return list;
        }
    }


    /** See PageData
     * @see PageData#prev(int index, int count)
     * @param index See PageData
     * @param count See PageData
     * @exception PageException See PageData
     * @return See PageData
     */
    public ArrayList prev(int index, int count)
        throws PageException {
        if (mCurrentPosition != index) {
            mPageAdapter.setCurrentPosition(index);
            mCurrentPosition = index;
        }
        return prev(count);
    }


    /** See PageData
     * @see PageData#sort(Comparator c)
     * @param c See PageData
     * @exception PageException See PageData
     */
    public void sort(Comparator c)
        throws PageException {
        mPageAdapter.sort(c);
    }

    /** See PageData
     * @see PageData#sort(Comparator c)
     * @param c See PageData
     * @exception PageException See PageData
     */
    public void sortSummary(Comparator c)
        throws PageException {       
        mPageAdapter.sortSummary(c);       
    }

    /**
      * Allows adapter source to optionally release resources such as database
      * connection.
      */
    private void idleTimeOut() {
        mPageAdapter.idleTimeOut();
    }


}
