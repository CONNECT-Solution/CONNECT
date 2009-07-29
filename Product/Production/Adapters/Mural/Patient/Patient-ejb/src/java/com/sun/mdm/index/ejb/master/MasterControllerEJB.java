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
 
package com.sun.mdm.index.ejb.master;

import com.sun.mdm.index.ejb.page.PageDataRemote;

import com.sun.mdm.index.master.ConnectionInvalidException;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.master.search.enterprise.EOGetOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.master.UserException;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.security.SecurityManager;
import com.sun.mdm.index.update.UpdateResult;
import com.sun.mdm.index.util.Localizer;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.annotation.security.DeclareRoles;

import java.sql.Connection;
import java.util.Map;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/** The Master Controller EJB implements the main interface that is exposed to clients of
 * the indexing system. This is a stateless session bean, though some methods
 * will return objects that have handles to stateful beans.
 */
@Stateless(mappedName="ejb/PatientMasterController")
@Remote(MasterControllerRemote.class)
@Local(MasterControllerLocal.class)
@EJB(   name="ejb/PageDataRemote",
        beanInterface=PageDataRemote.class,
        mappedName="ejb/PatientPageData")
@Resources ({

    @Resource(  name="jdbc/BBEDataSource",
                type=javax.sql.DataSource.class,
                mappedName="jdbc/PatientDataSource" ),
    @Resource(  name="jms/outBoundSender",
                type=javax.jms.ConnectionFactory.class,
                mappedName="jms/PatientOutBoundSender" )
})
@TransactionManagement(TransactionManagementType.CONTAINER)
//if CONTAINER then _TransactionManagementType_TOKEN_ is TransactionManagementType.CONTAINER
//if BEAN and local then _TransactionManagementType_TOKEN_ is TransactionManagementType.CONTAINER
@DeclareRoles({"MasterIndex.Admin","MasterIndex.User"})
public class MasterControllerEJB implements MasterControllerRemote, MasterControllerLocal{
    

    private SessionContext mSessionContext;
    
    /**
     * Implementation of MasterControllerCore
     */
    private MasterControllerCore mControllerImpl;
    
    /**
     * 
     */
    
    private SecurityManager securityManager;
    
    /**
     * Controlling whether the business methods are in
     * transaction mode
     */
    private boolean mIsTransactional = false;
    
    private String transactionType = "CMT_XA";
    //if CONTAINER then CMT_XA;
    //if BEAN then BMT_XA;
    //if LOCAL them BMT_LOCAL
    
    
    
    private String objectName = "Patient";
    
    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * No argument constructor required by container.
     */
    public MasterControllerEJB() {
    }
    
    @Resource
    public void setSessionContext(SessionContext sessionContext){
    	  mSessionContext = sessionContext;
    }
    
    /** Create method specified in EJB 1.1 section 6.10.3
     *
     * 
     * @throws java.lang.Exception 
     */
    @PostConstruct
    public void initialize() throws Exception{
        mControllerImpl = new MasterControllerCoreImpl();
        mControllerImpl.setTransactionType(transactionType);
        mControllerImpl.setObjectName(objectName);
        
        mControllerImpl.init(mSessionContext);
        
        securityManager = new SecurityManager(mSessionContext);
        mControllerImpl.setSecurityManager(securityManager);
              
    }
    
    /**
     * cleanUp before the instance is destroyed
     */
    
    @PreDestroy
    public void cleanUp(){
    	mControllerImpl.cleanUp();
    }
    
    /**
     * Retrieves a tree structure representing all of the merge
     * transactions associated with the specified EUID.
     * <p>
     * @param euid The EUID associated with the merge tree to
     * retrieve.
     * @return <CODE>MergeHistoryNode</CODE> - The merge history
     * tree for the specified EUID.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid EUID is passed
     * as a parameter.
     * @include
     */
    public MergeHistoryNode getMergeHistory(
            String euid)
    throws ProcessingException, UserException {
        MergeHistoryNode mhn = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mhn = mControllerImpl.getMergeHistory(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;            
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mhn;
    }
    
    
    /**
     * <b>executeMatch</b> is one of four methods that process a
     * system object based on the configuration defined
     * for the Master Index Manager Service and associated runtime components.
     * This method searches for possible matches in the database, and if it finds
     * a match, it replaces the system object (instead of updating the
     * system object, which is what <b>executeMatchUpdate</b> does).
     * The following runtime components configure <b>executeMatch</b>.
     * <UL>
     * <LI>The Query Builder defines the blocking queries used for
     * matching.
     * <LI>Threshold configuration specifies which blocking query to use
     * and specifies matching parameters, including duplicate and match
     * thresholds and whether potential duplicates are automatically
     * recalculated for updated records.
     * <LI>The pass controller and block picker classes specify how the
     * blocking query is executed.
     * </UL>
     * <p>
     * @param sysObj The system object to process into the database.
     * @return <CODE>MatchResult</CODE> - A match result object containing
     * the results of the matching process.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid parameter is passed
     * to the method.
     * @include
     */
    public MatchResult executeMatch(
            SystemObject sysObj)
    throws ProcessingException, UserException {
        MatchResult mr = null;
        Connection con = null;        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mr = mControllerImpl.executeMatch(con, sysObj);
            switch (mr.getResultCode()) {
                case MatchResult.SYS_ID_MATCH:
                case MatchResult.ASSUMED_MATCH:
                case MatchResult.NEW_EO:
                    mControllerImpl.commitTransaction(con);
                    break;
                    
                default: break;
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mr;
    }
    
    /**
     * <b>executeMatchDupRecalc</b> is one of four methods that process a
     * system object based on the configuration defined for the Master Index Manager
     * Service and associated runtime components. It is configured by the
     * same components as <b>executeMatch</b>, and is similar to
     * <b>executeMatch</B> but allows you to control whether potential duplicates
     * are recalculated when an object is updated regardless of whether the update
     * mode is set to optimistic or pessimistic. <b>executeMatchDupRecalc</b>
     * differs from <b>executeMatch</b> in two ways:
     * <UL>
     * <LI>It provides an override flag for the update mode specified in the
     * Threshold configuration file.
     * <LI>The match result object returned by this method includes an
     * indicator of whether match fields were modified. This helps you determine
     * whether you need to process potential duplicates for this object at a
     * later time.
     * </UL>
     * <p>
     * NOTE: To process potential duplicates at a later time, call
     * <b>calculatePotentialDuplicates</b>.
     * <p>
     * @param sysObj The system object to process into the database.
     * @param performPessimistic A Boolean indicator of whether to recalculate
     * potential duplicates on update or to defer it until later. Specify <b>true</b>
     * to recalculate on update; specify <b>false</b> to defer the recalculation.
     * @return <CODE>MatchResult</CODE> - A match result object containing
     * the results of the matching process.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid parameter is passed
     * to the method.
     * @include
     */
    public MatchResult executeMatchDupRecalc(
            SystemObject sysObj, 
            Boolean performPessimistic)
    throws ProcessingException, UserException {
        MatchResult mr = null;
        Connection con = null;        
        try {
            mControllerImpl.beginTransaction();            
            con = mControllerImpl.getConnection();
            mr = mControllerImpl.executeMatchDupRecalc(con, sysObj, performPessimistic);
            switch (mr.getResultCode()) {
                case MatchResult.SYS_ID_MATCH:
                case MatchResult.ASSUMED_MATCH:
                case MatchResult.NEW_EO:
                    mControllerImpl.commitTransaction(con);
                    break;
                    
                default: break;
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mr;
    }
    
    /**
     * <b>executeMatchGui</b> is identical to <b>executeMatch</b>, but it
     * is only called by the Enterprise Data Manager. It processes a
     * system object based on the configuration defined for the Master Index
     * Manager Service and associated runtime components. This method
     * searches for possible matches in the database, and if it finds
     * a match, it replaces the system object (instead of updating the
     * system object). The following runtime components configure
     * <b>executeMatchGui</b>.
     * <UL>
     * <LI>The Query Builder defines the blocking queries used for
     * matching.
     * <LI>Threshold configuration specifies which blocking query to use
     * and specifies matching parameters, including duplicate and match
     * thresholds.
     * <LI>The pass controller and block picker classes specify how the
     * blocking query is executed.
     * </UL>
     * <p>
     * @param sysObj The system object to process into the database.
     * @return <CODE>MatchResult</CODE> - A match result object containing
     * the results of the matching process.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid parameter is passed
     * to the method.
     * @include
     */
    public MatchResult executeMatchGui(
            SystemObject sysObj)
    throws ProcessingException, UserException {
        MatchResult mr = null;
        Connection con = null;

        try {

            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mr = mControllerImpl.executeMatchGui(con, sysObj);
            switch (mr.getResultCode()) {
                case MatchResult.SYS_ID_MATCH:
                case MatchResult.ASSUMED_MATCH:
                case MatchResult.NEW_EO:                  
                    mControllerImpl.commitTransaction(con);
                    break;
                    
                default: break;
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);            
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mr;
    }
    
    /**
     * <b>executeMatchUpdate</b> is one of four methods that process a
     * system object based on the configuration defined for the Master Index
     * Manager Service and associated runtime components. It is configured
     * by the same components as <b>executeMatch</b>. This method
     * searches for possible matches in the database, and if it finds
     * a match, it updates the system object (instead of replacing the
     * system object, which is what <b>executeMatch</b> does).
     * The following runtime components configure <b>executeMatchUpdate</b>.
     * <UL>
     * <LI>The Query Builder defines the blocking queries used for
     * matching.
     * <LI>Threshold configuration specifies which blocking query to use
     * and specifies matching parameters, including duplicate and match
     * thresholds.
     * <LI>The pass controller and block picker classes specify how the
     * blocking query is executed.
     * </UL>
     * <p>
     * @param sysObj The system object to process into the database.
     * @return <CODE>MatchResult</CODE> - A match result object containing
     * the results of the matching process.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid parameter is passed
     * to the method.
     * @include
     */
    public MatchResult executeMatchUpdate(
            SystemObject sysObj)
    throws ProcessingException, UserException {
        MatchResult mr = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mr = mControllerImpl.executeMatchUpdate(con, sysObj);
            switch (mr.getResultCode()) {
                case MatchResult.SYS_ID_MATCH:
                case MatchResult.ASSUMED_MATCH:
                case MatchResult.NEW_EO:
                    mControllerImpl.commitTransaction(con);
                    break;
                    
                default: break;
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mr;
    }
    
    /**
     * <b>executeMatchUpdateDupRecalc</b> is one of four methods that process a
     * system object based on the configuration defined for the Master Index Manager
     * Service and associated runtime components. It is configured by the same
     * components as <b>executeMatch</b>, and is similar to
     * <b>executeMatchUpdate</B> but allows you to control whether potential duplicates
     * are recalculated when an object is updated regardless of whether the update
     * mode is set to optimistic or pessimistic. <b>executeMatchUpdateDupRecalc</b>
     * differs from <b>executeMatchUpdate</b> in two ways:
     * <UL>
     * <LI>It provides an override flag for the update mode specified in the
     * Threshold configuration file.
     * <LI>The match result object returned by this method includes an
     * indicator of whether match fields were modified. This helps you determine
     * whether you need to process potential duplicates for this object at a
     * later time.
     * </UL>
     * <p>
     * NOTE: To process potential duplicates at a later time, call
     * <b>calculatePotentialDuplicates</b>.
     * <p>
     * @param sysObj The system object to process into the database.
     * @param performPessimistic A Boolean indicator of whether to recalculate
     * potential duplicates on update or to defer it until later. Specify <b>true</b>
     * to recalculate on update; specify <b>false</b> to defer the recalculation.
     * @return <CODE>MatchResult</CODE> - A match result object containing
     * the results of the matching process.
     * @exception ProcessingException Thrown if an error occurs
     * during processing.
     * @exception UserException Thrown if an invalid parameter is passed
     * to the method.
     * @include
     */
    public MatchResult executeMatchUpdateDupRecalc(
            SystemObject sysObj, 
            Boolean performPessimistic)
    throws ProcessingException, UserException {
        MatchResult mr = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mr = mControllerImpl.executeMatchUpdateDupRecalc(con, sysObj, performPessimistic);
            switch (mr.getResultCode()) {
                case MatchResult.SYS_ID_MATCH:
                case MatchResult.ASSUMED_MATCH:
                case MatchResult.NEW_EO:
                    mControllerImpl.commitTransaction(con);
                    break;
                    
                default: break;
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mr;
    }
    
    /**
     * Returns an iterator of enterprise objects that match the specified
     * search criteria and options. The criteria are contained in a system
     * object. The options consist of a query builder ID and certain
     * attributes of the query.
     * <p>
     * @param criteria An EOSearchCriteria object containing the search
     * criteria.
     * @param searchOptions An EOSearchOptions object defining attributes
     * of the search.
     * @return <CODE>EOSearchResultIterator</CODE> - An iterator containing
     * the results of the query.
     * @exception ProcessingException Thrown if an error occurs while processing
     * the query.
     * @exception UserException Thrown if the search object is invalid.
     * @include
     */
    public EOSearchResultIterator searchEnterpriseObject(
            EOSearchCriteria criteria,
            EOSearchOptions searchOptions)
    throws ProcessingException, UserException {
        return mControllerImpl.searchEnterpriseObject(criteria, searchOptions);
    }
    
    /**
     * Returns an iterator of enterprise objects that match the specified
     * EUIDs and options. The options consist of a query builder ID and
     * certain attributes of the query.
     * <p>
     * @param Euids  An array of EUID's for which to search
     * @param searchOptions An EOSearchOptions object defining attributes of the search.
     * @return EOSearchResultIterator - An iterator containing
     * the results of the query.
     * @exception ProcessingException Thrown if an error occurs while processing the query.
     * @exception UserException Thrown if the search object is invalid.
     * @include
     */
    public EOSearchResultIterator searchEnterpriseObject(
            String Euids[],
            EOSearchOptions searchOptions)
    throws ProcessingException, UserException {
        return mControllerImpl.searchEnterpriseObject(Euids, searchOptions);
    }
    
    /**
     * Returns the enterprise object associated with the specified EUID.
     * If no matching enterprise object is found, this method returns
     * null.
     * <p>
     * @param euid The EUID of the enterprise object you want to retrieve.
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object
     * associated with the specified EUID. Returns null if no enterprise
     * object with the specified EUID is found.
     * @exception ProcessingException Thrown if an error occurs while
     * processing the search.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public EnterpriseObject getEnterpriseObject(
            String euid)
    throws ProcessingException, UserException {
        EnterpriseObject eo = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            eo = mControllerImpl.getEnterpriseObject(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return eo;
    }
    
    /**
     * Returns the enterprise object associated with the specified EUID.
     * If no matching enterprise object is found, this method returns
     * null. This method allows you to specify which objects to return
     * in the enterprise record associated with the given EUID instead of
     * returning the entire enterprise record. The objects to include in
     * the resulting enterprise object are specified by their ePaths. Below
     * is a sample implementation.
     * <p>
     * <p><CODE>   String ePaths[] =
     * <p>   {
     * <p>   "Enterprise.SystemObject.Person.Phone[*].*",
     * <p>   "Enterprise.SystemObject.Person.Alias[*].*"
     * <p>   };
     * <p>   EOGetOptions options = new EOGetOptions();
     * <p>   options.setFieldsToRetrieve(ePaths);
     * <p>   EnterpriseObject eo = mc.getEnterpriseObject (euid, options); </CODE>
     * <p>
     * @param euid The EUID of the enterprise object you want to retrieve.
     * @param options A list of ePaths that define which types of objects
     * to retrieve to create the resulting EnterpriseObject.
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object
     * associated with the given EUID. If no matching EUID is found, the
     * return value is null.
     * @exception ProcessingException Thrown if an error occurs while
     * processing the search.
     * @exception UserException Thrown if an invalid EUID is entered (for
     * example, a null or empty string).
     * @include
     */
    public EnterpriseObject getEnterpriseObject(
            String euid, 
            EOGetOptions options)
    throws ProcessingException, UserException {
        EnterpriseObject eo = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            eo = mControllerImpl.getEnterpriseObject(con, euid, options);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return eo;
    }
    
    
    /**
     * Returns the enterprise object associated with a system code and
     * local ID pair (SystemObjectPK). If no matching enterprise object
     * is found, this method returns null. Only SystemObjectPK objects
     * representing active system records can be used.
     * <p>
     * @param key The system object primary key.
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object
     * associated with the specified primary key. Returns null if no
     * enterprise object with the specified system object primary key
     * is found.
     * @exception ProcessingException Thrown if an error occurs while
     * processing the search.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public EnterpriseObject getEnterpriseObject(
            SystemObjectPK key)
    throws ProcessingException, UserException {
        EnterpriseObject eo = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            eo = mControllerImpl.getEnterpriseObject(con, key);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return eo;
    }
    
    
    /**
     * Returns the SBR object associated with the specified EUID.
     * If no matching SBR object is found, this method returns
     * null.
     * <p>
     * @param euid The EUID associated with the SBR you want to retrieve.
     * @return <CODE>SBR</CODE> - The SBR object associated with the
     * specified EUID. Returns null if no SBR associated with
     * the specified EUID is found.
     * @exception ProcessingException Thrown if an error occurs while
     * processing the search.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public SBR getSBR(
            String euid)
    throws ProcessingException, UserException{
        SBR sbr = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            sbr = mControllerImpl.getSBR(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return sbr;
    }
    
    
    /**
     * Updates the database with the new values of the modified system
     * object specified as a parameter.
     * <p>
     * @param sysobj The updated system object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * updating the database.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public void updateSystemObject(
            SystemObject sysobj)
    throws ProcessingException, UserException{
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.updateSystemObject(con, sysobj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    /**
     * Updates the database with the new values of the modified system
     * object specified as a parameter. This method compares the revision number of the
     * SBR associated with the updated system object with the revision number in the
     * database. If the numbers are different, the update is not performed (a difference
     * indicates that the record was updated by another process).
     * <p>
     * @param sysobj The updated system object.
     * @param revisionNumber The revision number of the SBR of the associated SO.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * updating the database.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public void updateSystemObject(
            SystemObject sysobj, 
            String revisionNumber)
    throws ProcessingException, UserException{
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.updateSystemObject(con, sysobj, revisionNumber);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    /**
     * Retrieves the attributes of an external systemCode from the master index
     * database, such as the system code, masking flags, local ID format and
     * so on.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>SystemDefinition[]</CODE> - An array of system attributes.
     * @exception ProcessingException Thrown if an error occurs while
     * looking up the system.
     * @include
     */
    public SystemDefinition[] lookupSystemDefinitions()
    throws ProcessingException{
        SystemDefinition[] sd = null;
        Connection con = null;
 
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            sd = mControllerImpl.lookupSystemDefinitions(con);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return sd;
    }
    
    /**
     * Retrieves the attributes of an external system from the master index
     * database based on the system code. Attributes include the system code,
     * masking flags, local ID format and so on.
     * <p>
     * @param systemCode  The system code of an external system.
     * @return <CODE>SystemDefinition</CODE> - A set of system attributes for
     * the system identified the given system code.
     * @exception ProcessingException Thrown if an error occurs while
     * looking up the system.
     * @include
     */
    public SystemDefinition lookupSystemDefinition(
            String systemCode)
    throws ProcessingException{
        SystemDefinition sd = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            sd = mControllerImpl.lookupSystemDefinition(con, systemCode);
             mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return sd;
    }
    
    /**
     * Looks up the active system objects associated with the specified
     * EUID.
     * <p>
     * @param euid The EUID whose associated system objects will be
     * retrieved.
     * @return <CODE>SystemObject[]</CODE> - An array of system objects
     * that are associated with the specified EUID.
     * @exception ProcessingException Thrown if an error occurs while
     * performing the lookup.
     * @exception UserException Thrown if the EUID is invalid.
     * @include
     */
    public SystemObject[] lookupSystemObjects(
            String euid)
    throws ProcessingException, UserException {
        SystemObject[] so = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            so = mControllerImpl.lookupSystemObjects(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return so;
    }
    
    
    /**
     * Looks up the system objects of a certain status that are
     * associated with the specified EUID.
     * <p>
     * @param euid The EUID whose associated system objects will be
     * retrieved.
     * @param status The status code of the system objects to
     * retrieve.
     * @return <CODE>SystemObject[]</CODE> - A list of system objects
     * of the specified status that are associated with the given EUID.
     * @exception ProcessingException Thrown if an error occurs while
     * performing the lookup.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public SystemObject[] lookupSystemObjects(
            String euid, 
            String status)
    throws ProcessingException, UserException {
        SystemObject[] so = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();         
            con = mControllerImpl.getConnection();
            so = mControllerImpl.lookupSystemObjects(con, euid, status);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return so;
    }
    
    
    /**
     * Adds the system object to the enterprise object associated with the EUID.
     * If the update mode in Threshold configuration is pessimistic, the application
     * checks whether any key fields (that is, fields used for blocking or matching)
     * were updated in the SBR. If key fields were updated, potential duplicates
     * are recalculated for the enterprise object.
     * <p>
     * @param euid The EUID of the enterprise object to which the system
     * object will be added.
     * @param sysobj The system object to add to the enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * adding the system object.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void addSystemObject(
            String euid, 
            SystemObject sysobj)
    throws ProcessingException, UserException{
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.addSystemObject(con, euid, sysobj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Creates a new enterprise object to add to the master index
     * database using the information in the specified system object. This
     * method bypasses potential duplicate processing.
     * <p>
     * @param sysobj The system object to use as a basis for the
     * enterprise object.
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object
     * created from the specified system object.
     * @exception ProcessingException Thrown if an error occurs while
     * creating the enterprise object.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public EnterpriseObject createEnterpriseObject(
            SystemObject sysobj)
    throws ProcessingException, UserException{
        EnterpriseObject eo = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            eo = mControllerImpl.createEnterpriseObject(con, sysobj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return eo;
    }
    
    
    /**
     * Creates a new enterprise object to add to the master index
     * database using the information in the specified array of
     * system objects. This method bypasses potential duplicate processing.
     * <p>
     * @param sysobj The array of system objects to use as a basis
     * for the enterprise object.
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object
     * created from the specified system objects.
     * @exception ProcessingException Thrown if an error occurs while
     * creating the enterprise object.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public EnterpriseObject createEnterpriseObject(
            SystemObject[] sysobj)
    throws ProcessingException, UserException{
        EnterpriseObject eo = null;
        Connection con = null;       
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            eo = mControllerImpl.createEnterpriseObject(con, sysobj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return eo;
    }
    
    
    /** Updates the database to reflect the new values of the specified
     * enterprise object. If the enterprise object is deactivated during
     * the update, potential duplicates are deleted for that object. If
     * the enterprise object is still active, was changed during the
     * transaction, and the update mode is set to pessimistic, the application
     * checks whether any key fields were updated in the SBR of the enterprise
     * object. If key fields were updated, potential duplicates are recalculated.
     * <p>
     * @param eo The enterprise object (EnterpriseObject class) to be updated.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs during the update.
     * @exception UserException Thrown if the enterprise object containing
     * the updated data is invalid.
     * @include
     */
    public void updateEnterpriseObject(
            EnterpriseObject eo)
    throws ProcessingException, UserException{
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.updateEnterpriseObject(con, eo);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    /**
     * Updates the database to reflect the new values of the specified
     * enterprise object. This method is similar to <b>updateEnterpriseObject</b>
     * but it allows you to control whether potential duplicate recalculation is
     * performed when an enterprise object is updated, regardless of whether
     * the update mode is set to optimistic or pessimistic. This method
     * differs from <b>updateEnterpriseObject</b> in two ways:
     * <UL>
     * <LI>It provides an override flag for the update mode specified in the
     * Threshold configuration file.
     * <LI>The update result object returned by this method includes an
     * indicator of whether match fields were modified. This helps you determine
     * whether you need to process potential duplicates for this object at a
     * later time.
     * </UL>
     * <p>
     * NOTE: To process potential duplicates at a later time, call
     * <b>calculatePotentialDuplicates</b>.
     * <p>
     * @param eo The enterprise object (EnterpriseObject class) to be updated.
     * @param performPessimistic A Boolean indicator of whether to defer potential
     * duplicate processing. Specify <b>true</b> to recalculate potential duplicates
     * on update; specify <b>false</b> to defer recalculation to a later time.
     * @return <CODE>UpdateResult</CODE> - The UpdateResult object
     * created from the update.
     * @exception ProcessingException Thrown if an error occurs during the update.
     * @exception UserException Thrown if the enterprise object containing
     * the updated data is invalid.
     * @include
     */
    public UpdateResult updateEnterpriseDupRecalc(
            EnterpriseObject eo, 
            Boolean performPessimistic)
    throws ProcessingException, UserException{
        UpdateResult rs = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            rs = mControllerImpl.updateEnterpriseDupRecalc(con, eo, performPessimistic);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return rs;
    }
    
    /**
     * Calculates potential duplicates for the specified EUID and transaction ID.
     * Call this method if you chose not to check for potential duplicates during the
     * match or update processes.
     * <p>
     * @param euid The EUID for which potential duplicates should be calculated.
     * @param transID The transaction number for which potential duplicates should be
     * calculated.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs during the potential
     * duplicate calculation.
     * @include
     */
    public void calculatePotentialDuplicates(
            String euid, 
            String transID)
    throws ProcessingException{
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.calculatePotentialDuplicates(con, euid, transID);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    /**
     * Returns a transaction summary for the transaction associated with
     * the specified transaction number.
     * <p>
     * @param transId The transaction number for the transaction.
     * @return <CODE>TransactionSummary</CODE> - The transaction summary
     * for the specified transaction ID.
     * @exception ProcessingException Thrown if an error occurs while retrieving
     * the summary.
     * @exception UserException Thrown if the transaction number is invalid.
     * @include
     */
    public TransactionSummary lookupTransaction(
            String transId)
    throws ProcessingException, UserException{
        TransactionSummary ts = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            ts = mControllerImpl.lookupTransaction(con, transId);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return ts;
    }
    
    
    /**
     * Returns an array of transaction summaries based on the search criteria
     * contained in the specified transaction search object.
     * <p>
     * @param obj The transaction search object containing the search criteria.
     * @return <CODE>TransactionSummary</CODE> - An array of transaction summaries
     * matching the given search criteria.
     * @exception ProcessingException Thrown if an error occurs during the search.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public TransactionIterator lookupTransactions(
            TransactionSearchObject obj)
    throws ProcessingException, UserException{
        TransactionIterator ti = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            ti = mControllerImpl.lookupTransactions(con, obj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return ti;
    }
    
    
    /**
     * Returns an iterator of PotentialDuplicateSummary objects based on the
     * criteria contained in the potential duplicate search object
     * (PotentialDuplicateSearchObject class).
     * <p>
     * @param obj An instance of PotentialDuplicateSearchObject containing the
     * potential duplicate search criteria.
     * @return <CODE>PotentialDuplicateIterator</CODE> - An iterator of
     * search results (PotentialDuplicateSummary objects).
     * @exception ProcessingException Thrown if an error occurs during the lookup.
     * @exception UserException Thrown if the search object is invalid.
     * @include
     */
    public PotentialDuplicateIterator lookupPotentialDuplicates(
            PotentialDuplicateSearchObject obj)
    throws ProcessingException, UserException{
        PotentialDuplicateIterator pdi = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            pdi = mControllerImpl.lookupPotentialDuplicates(con, obj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return pdi;
    }
    
    
    /** Counts the number of potential duplicate records matching the 
     * criteria specified in search object.  This does not
     * handle searches based on EUID nor SystemCode/LID.
     *
     * @param obj Search criteria.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid search object
     * @return count of the potential duplicate records matching the search criteria.
     */
    public int countPotentialDuplicates(PotentialDuplicateSearchObject pdso) 
            throws ProcessingException, UserException {
        Connection con = null;
        int count = 0;
        try {
            mControllerImpl.beginTransaction();  
            con = mControllerImpl.getConnection();
            count = mControllerImpl.countPotentialDuplicates(con,pdso);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources(con);
        }
        return count;
    }
    
    /**
     * Returns an iterator of AssumedMatchSummary objects based on the
     * criteria contained in the assumed match search object
     * (AssumedMatchSearchObject class).
     * <p>
     * @param obj An instance of AssumedMatchSearchObject containing the
     * potential duplicate search criteria.
     * @return <CODE>AssumedMatchIterator</CODE> - An iterator of
     * search results (AssumedMatchSummary objects).
     * @exception ProcessingException Thrown if an error occurs during the lookup.
     * @exception UserException Thrown if the search object is invalid.
     * @include
     */
    public AssumedMatchIterator lookupAssumedMatches(
            AssumedMatchSearchObject obj)
    throws ProcessingException, UserException{
        AssumedMatchIterator ami = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            ami = mControllerImpl.lookupAssumedMatches(con, obj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return ami;
    }
    
    /** Counts the number of assumed match records matching the 
     * date criteria specified in search object.  This does not
     * handle searches based on EUID nor SystemCode/LID.
     *
     * @param obj Search criteria.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid search object
     * @return count of the assumed match records matching the search criteria.
     */
    public int countAssumedMatches(AssumedMatchSearchObject amso)
        throws ProcessingException, UserException {
        Connection con = null;
        int count = 0;
        try {
            mControllerImpl.beginTransaction();  
            con = mControllerImpl.getConnection();
            count = mControllerImpl.countAssumedMatches(con,amso);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources(con);
        }
        return count;
    }
    
    /**
     * Reverses an assumed match transaction, unmerging the two objects that were
     * matched and creating a new enterprise object for the record that caused
     * the assumed match. Potential duplicates are calculated for the new enterprise
     * object regardless of the update mode. If the update mode is set to pessimistic,
     * the application checks whether any key fields (that is, fields used for matching
     * or blocking) were updated in the SBR of the original enterprise object. If key
     * fields were updated, potential duplicates are recalculated for that object.
     * <p>
     * @param assumedMatchId The assumed match ID of assumed match transaction
     * to reverse.
     * @return <CODE>String</CODE> - The EUID of the newly created enterprise
     * object.
     * @exception ProcessingException Thrown if an error occurs during the undo
     * process.
     * @exception UserException Thrown if the assumed match ID is invalid.
     * @include
     */
    public String undoAssumedMatch(
            String assumedMatchId)
    throws ProcessingException, UserException{
        String euid = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            euid = mControllerImpl.undoAssumedMatch(con, assumedMatchId);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return euid;
    }

    /**
     * Provides capability to have preview of 
     * undo assumed Match before its is persisted in the Database.
     * <p>
     * @param assumedMatchId The assumed match ID of assumed match transaction
     * to reverse.
     * @return <CODE>String</CODE> - The EUID of the newly created enterprise
     * object.
     * @exception ProcessingException Thrown if an error occurs during the undo
     * process.
     * @exception UserException Thrown if the assumed match ID is invalid.
     * @include
     */
public EnterpriseObject previewUndoAssumedMatch(
            String assumedMatchId)
    throws ProcessingException, UserException{
        EnterpriseObject newEO = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            newEO = mControllerImpl.previewUndoAssumedMatch(con, assumedMatchId);
            
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return newEO;
    }
            
    /**
     * Merges two enterprise objects based on the specified EUIDs. When this
     * method is called with calculateOnly set to false, the application changes
     * the status of the merged enterprise object to "merged" and deletes all
     * potential duplicate listings for that object. If the update mode is set to
     * pessimistic, the application checks whether any key fields (that is, fields
     * used for matching or blocking) were updated in the SBR of the surviving
     * enterprise object. If key fields were updated, potential duplicates are
     * recalculated for the surviving object.
     * <p>
     * @param fromEUID The EUID of the non-surviving enterprise
     * object.
     * @param toEUID The EUID of the surviving enterprise
     * object.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeEnterpriseObject(
            String fromEUID, 
            String toEUID,
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeEnterpriseObject(con, fromEUID, toEUID, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges two enterprise objects based on the specified EUIDs. This merge
     * method takes the revision numbers of the SBRs so you can check for updates
     * before finalizing the transaction. When this method is called with
     * calculateOnly set to false, the application changes the status of the merged
     * enterprise object to "merged" and deletes all potential duplicate listings
     * for that object. If the update mode is set to pessimistic, the application
     * checks whether any key fields (that is, fields used for matching or blocking)
     * were updated in the SBR of the surviving enterprise object. If key fields
     * were updated, potential duplicates are recalculated for the surviving object.
     * <p>
     * The SBR revision numbers of both the surviving and non-surviving enterprise
     * objects are passed as arguments. These are compared to the SBRs stored in the
     * database. If they differ, it means that either the source or destination record
     * was modified by another user.  In this case, the merge should not be allowed.
     * <p>
     * @param fromEUID The EUID of the non-surviving enterprise
     * object.
     * @param toEUID The EUID of the surviving enterprise
     * object.
     * @param srcRevisionNumber The SBR revision number of the non-surviving
     * enterprise object.
     * @param destRevisionNumber The SBR revision number of the surviving
     * enterprise object.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeEnterpriseObject(
            String fromEUID,
            String toEUID,
            String srcRevisionNumber,
            String destRevisionNumber,
            boolean calculateOnly)
    throws ProcessingException, UserException {
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeEnterpriseObject(con, fromEUID, toEUID, srcRevisionNumber, destRevisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges two enterprise objects based on the specified EUID and enterprise
     * object. This method allows you to perform
     * additional actions against the surviving enterprise object after the merge
     * is performed. When this method is called with calculateOnly set to false,
     * the application changes the status of the non-surviving enterprise object
     * to "merged" and deletes all potential duplicate listings for that object.
     * If the update mode is set to pessimistic, the application checks whether
     * any key fields (that is, fields used for matching or blocking) were updated
     * in the SBR of the surviving enterprise object. If key fields were updated,
     * potential duplicates are recalculated for the surviving object.
     * <p>
     * @param sourceEUID The EUID of the non-surviving enterprise
     * object.
     * @param destinationEO The enterprise object that will survive
     * after the merge process.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeEnterpriseObject(
            String sourceEUID,
            EnterpriseObject destinationEO,
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeEnterpriseObject(con, sourceEUID, destinationEO, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges two enterprise objects based on the specified EUID and enterprise
     * object. This merge method takes the revision numbers of the SBRs so you
     * can check for updates before finalizing the transaction, and also allows
     * you to perform additional actions against the
     * surviving enterprise object after the merge is performed. When this
     * method is called with calculateOnly set to false, the application changes
     * the status of the merged enterprise object to "merged" and deletes all
     * potential duplicate listings for that object. If the update mode is set to
     * pessimistic, the application checks whether any key fields (that is, fields
     * used for matching or blocking) were updated in the SBR of the surviving
     * enterprise object. If key fields were updated, potential duplicates are
     * recalculated for the surviving object.
     * <p>
     * The SBR revision numbers of both the surviving and non-surviving enterprise
     * objects are passed as arguments. These are compared to the SBRs stored in the
     * database. If they differ, it means that either the source or destination record
     * was modified by another user.  In this case, the merge should not be allowed.
     * <p>
     * @param sourceEUID The EUID of the non-surviving enterprise object.
     * @param destinationEO The enterprise object that will survive
     * after the merge process.
     * @param srcRevisionNumber The SBR revision number of the non-surviving
     * enterprise object.
     * @param destRevisionNumber The SBR revision number of the surviving
     * enterprise object.
     * @param calculateOnly A Boolean indicator of whether to commit changes
     * to the database or to simply compute the merge results. Specify
     * <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeEnterpriseObject(
            String sourceEUID,
            EnterpriseObject destinationEO,
            String srcRevisionNumber,
            String destRevisionNumber,
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeEnterpriseObject(con, sourceEUID, destinationEO, srcRevisionNumber, destRevisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
	/**
	 * Merges multiple enterprise objects based on the specified EUIDs and enterprise
	 * object. The source EUIDs will each be successively merged
         * into the destination EO.  For example, sourceEUIDs[0] will be merged into
         * the destination EO.  Then sourceEUIDs[1] will be merged into the destination
         * EO.  Next, sourceEUIDS[2] will be merged into the destination EO.  
         * If there are n merges, there will be n merge transaction log entries.  All of
         * these transactions must be unmerged in order to restore the state prior to the
         * multiple merge. This merge method takes the revision numbers of the SBRs so you
	 * can check for updates before finalizing the transaction, and also allows
	 * you to perform additional actions against the surviving enterprise object
	 * after the merge is performed. When this method is called with
	 * calculateOnly set to false, the application changes the status of the
	 * merged enterprise object to "merged" and deletes all potential duplicate
	 * listings for that object. If the update mode is set to pessimistic, the
	 * application checks whether any key fields (that is, fields used for
	 * matching or blocking) were updated in the SBR of the surviving enterprise
	 * object. If key fields were updated, potential duplicates are recalculated
	 * for the surviving object.
	 * <p>
	 * The SBR revision numbers of both the surviving and non-surviving
	 * enterprise objects are passed as arguments. These are compared to the
	 * SBRs stored in the database. If they differ, it means that either the
	 * source or destination record was modified by another user. In this case,
	 * the merge should not be allowed.
	 * <p>
	 * 
	 * @param sourceEUIDs
	 *            The EUIDs of the non-surviving enterprise objects.
	 * @param destinationEO
	 *            The enterprise object that will survive after the merge
	 *            process.
	 * @param srcRevisionNumbers
	 *            The SBR revision numbers of the non-surviving enterprise
	 *            object.
	 * @param destRevisionNumber
	 *            The SBR revision number of the surviving enterprise object.
	 * @param calculateOnly
	 *            A Boolean indicator of whether to commit changes to the
	 *            database or to simply compute the merge results. Specify
	 *            <b>false</b> to commit the changes.
	 * @return <CODE>MergeResult</CODE>[] - The results of the merge operations.
	 * @exception ProcessingException
	 *                Thrown if an error occurs during the merge process.
	 * @exception UserException
	 *                Thrown if a parameter is invalid.
	 * @include
	 */
    public MergeResult[] mergeMultipleEnterpriseObjects(
            String[] sourceEUIDs,
            EnterpriseObject destinationEO,
            String[] srcRevisionNumbers,
            String destRevisionNumber,
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult[] mergeResults = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResults = mControllerImpl.mergeMultipleEnterpriseObjects(con, 
                                                                sourceEUIDs, 
                                                                destinationEO, 
                                                                srcRevisionNumbers, 
                                                                destRevisionNumber, 
                                                                calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResults;
    }
    
    
    /**
     * Unmerges the two enterprise objects that were involved in the most
     * recent merge transaction for the specified EUID. The enterprise
     * object that had a status of "merged" after the merge transaction
     * is changed to an active object. You can retrieve the active
     * enterprise object (represented by activeEUID) by calling
     * MergeResult.getDestinationEO. You can retrieve the reactivated
     * enterprise object by calling MergeResult.getSourceEO.
     * <p>
     * When this method is called with calculateOnly set to false, the
     * application changes the status of the merged enterprise object back
     * to "active" and recalculates potential duplicate listings for object.
     * If the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields used for matching or blocking)
     * were updated in the SBR of the enterprise object that was still active
     * after the merge. If key fields were updated, potential duplicates are
     * recalculated for that enterprise object.
     * <p>
     * @param euid The EUID of the enterprise object to be unmerged.
     * @param calculateOnly An indicator of whether to commit the unmerge
     * to the database or to calculate the changes for viewing. Specify
     * <b>true</b> to calculate for viewing; specify <b>false</b> to commit
     * the changes to the database.
     * @return <CODE>MergeResult</CODE> - The result of the unmerge
     * transaction.
     * @exception ProcessingException Thrown if an error occurs during
     * the unmerge process.
     * @exception UserException Thrown if the EUID is invalid.
     * @include
     */
    public MergeResult unmergeEnterpriseObject(
            String euid, 
            boolean calculateOnly)
    throws ProcessingException, UserException {
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.unmergeEnterpriseObject(con, euid, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Unmerges the two enterprise objects that were involved in the most
     * recent merge transaction for the specified EUID. The enterprise
     * object that had a status of "merged" after the merge transaction
     * is changed back to an active object after the unmerge. You can
     * retrieve the active enterprise object (represented by activeEUID)
     * by calling MergeResult.getDestinationEO. You can retrieve the reactivated
     * enterprise object by calling MergeResult.getSourceEO.
     * <p>
     * This method allows you to specify a revision number for the SBR of the
     * enterprise object to unmerge so you can check for updates before finalizing
     * the transaction. When this method is called with calculateOnly set to false,
     * the application changes the status of the merged enterprise object back
     * to "active" and recalculates potential duplicate listings for object.
     * If the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields used for matching or blocking)
     * were updated in the SBR of the enterprise object that was still active
     * after the merge. If key fields were updated, potential duplicates are
     * recalculated for that enterprise object.
     * <p>
     * The SBR revision number of the enterprise object to unmerge is passed as an
     * argument. This is compared to the SBR stored in the database. If the two
     * differ, it means that the record was modified by another user. In this case,
     * the unmerge should not be allowed.
     * <p>
     * @param euid The EUID of the enterprise object to unmerge.
     * @param revisionNumber  The SBR revision number of the enterprise object
     * to unmerge.
     * @param calculateOnly An indicator of whether to commit the unmerge
     * to the database or to calculate the changes for viewing. Specify
     * <b>true</b> to calculate for viewing; specify <b>false</b> to commit
     * the changes to the database.
     * @return <CODE>MergeResult</CODE> - The result of the unmerge transaction.
     * @exception ProcessingException Thrown if an error occurs during
     * the unmerge process.
     * @exception UserException Thrown if the EUID is invalid.
     * @include
     */
    
    public MergeResult unmergeEnterpriseObject(
            String euid, 
            String revisionNumber, 
            boolean calculateOnly)
    throws ProcessingException, UserException {
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.unmergeEnterpriseObject(con, euid, revisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    
    /**
     * Flags a potential duplicate pair with "Resolved" or "Auto Resolved"
     * status. If resolved, the pair can be marked as potential
     * duplicates again during a future transaction. If auto-resolved, the
     * pair is permanently flagged as resolved.
     * <p>
     * @param id The potential duplicate ID of the pair to be resolved.
     * @param autoResolve A Boolean value indicating whether to
     * resolve or auto-resolve the pair. Specify <b>true</b> to auto-resolve;
     * specify <b>false</b> to simply resolve the pair.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if the specified potential duplicate
     * ID is invalid.
     * @include
     */
    public void resolvePotentialDuplicate(
            String id, 
            boolean autoResolve)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.resolvePotentialDuplicate(con, id, autoResolve);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Changes the status of a resolved or auto-resolved potential duplicate
     * record pair back to unresolved and places the records back in the
     * potential duplicate listing.
     * <p>
     * @param id The potential duplicate ID of the records to unresolve.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if the specified potential duplicate
     * ID is invalid.
     * @include
     */
    public void unresolvePotentialDuplicate(
            String id)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.unresolvePotentialDuplicate(con, id);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Returns the system object associated with the system code and local
     * ID contained in the specified SystemObjectPK object.
     * <p>
     * @param key The system object key containing the local ID and system
     * code to lookup.
     * @return <CODE>SystemObject</CODE> - The system object associated with
     * the specified local ID and system code.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if the specified system object key is
     * invalid.
     * @include
     */
    public SystemObject getSystemObject(SystemObjectPK key)
    throws ProcessingException, UserException{
        SystemObject so = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            so = mControllerImpl.getSystemObject(con, key);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return so;
    }
    
    
    /**
     * Returns an array of all system objects associated with the
     * specified EUID.
     * <p>
     * @param euid The EUID of the enterprise object containing the system
     * objects to retrieve.
     * @return <CODE>SystemObjectPK[]</CODE> - An array of system objects
     * associated with the specified EUID.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if the specified EUID is invalid.
     * @include
     */
    public SystemObjectPK[] lookupSystemObjectPKs(String euid)
    throws ProcessingException, UserException{
        SystemObjectPK[] retVal = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            retVal = mControllerImpl.lookupSystemObjectPKs(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return retVal;
    }
    
    
    /**
     * Returns an array of all system objects of a certain status that are
     * associated with the specified EUID.
     * <p>
     * @param euid The EUID associated with the system objects to retrieve.
     * @param status The status of the system objects to retrieve.
     * @return <CODE>SystemObjectPK[]</CODE> - An array of system objects of the
     * specified status that are associated with the specified EUID. Returns null
     * if no results are found.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public SystemObjectPK[] lookupSystemObjectPKs(
            String euid, 
            String status)
    throws ProcessingException, UserException{
        SystemObjectPK[] retVal = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            retVal = mControllerImpl.lookupSystemObjectPKs(con, euid, status);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return retVal;
    }
    
    /**
     * Returns an array of system object keys (local ID and system code pairs)
     * of the specified status that belong to the specified destination system.
     * The returned keys are associated with the EUID that is associated
     * with the source system code and local ID pair.
     * <p>
     * @param sourceSystem The source system code of the known local ID.
     * @param sourceLID A local ID associated with the source system.
     * @param destSystem The system code of the system object keys to retrieve.
     * @param status The status of the records to lookup.
     * @return <CODE>SystemObjectPK[]</CODE> - An array of system objects of the
     * specified status. Returns null if no results are found.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if  a parameter is invalid.
     * @include
     */
    public SystemObjectPK[] lookupSystemObjectPKs(
            String sourceSystem,
            String sourceLID, 
            String destSystem, 
            String status)
    throws ProcessingException, UserException{
        SystemObjectPK[] retVal = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            retVal = mControllerImpl.lookupSystemObjectPKs(con, sourceSystem, sourceLID, destSystem, status);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return retVal;
    }
    
    
    /**
     * Returns the EUID associated with the system code and local ID specified in
     * the SystemObjectPK object.
     * <p>
     * @param key The system object key containing the system code and local ID
     * to use as search criteria.
     * @return <CODE>String</CODE> - The EUID associated with the given system
     * object key. Returns null if no results are found.
     * @exception ProcessingException Thrown if there is an error during
     * processing.
     * @exception UserException Thrown if the parameter is invalid.
     * @include
     */
    public String getEUID(SystemObjectPK key)
    throws ProcessingException, UserException{
        String euid = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            euid = mControllerImpl.getEUID(con, key);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return euid;
    }
    
    /**
     * Merges two system objects from the specified system. The system objects
     * may belong to a single enterprise object or to two different enterprise
     * objects. They must originate from the same system. When this method
     * is called with calculateOnly set to false, the
     * application changes the status of the merged system object to "merged".
     * If the system objects were merged within the same enterprise object and
     * the update mode is set to pessimistic, the application checks whether any
     * key fields (that is, fields used for matching or blocking) were updated in
     * the SBR. If key fields were updated, potential duplicates are recalculated
     * for the enterprise object.
     * <p>
     * If the two system objects originated from two different enterprise objects
     * and the enterprise object that contained the unkept the system object no longer
     * has any active system objects, that enterprise object is deactivated and all
     * associated potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields that are used for matching or blocking)
     * were updated in the SBR for each enterprise object. If key fields were updated,
     * potential duplicates are recalculated for each enterprise object.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be merged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeSystemObject(
            String systemCode, 
            String sourceLID,
            String destLID, 
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeSystemObject(con, systemCode, sourceLID, destLID, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges two system objects from the specified system. The system objects
     * may belong to a single enterprise object or to two different enterprise
     * objects. They must originate from the same system. This method includes
     * the revision numbers so you can check for updates before finalizing the
     * transaction. When this method is called with calculateOnly set to false,
     * the application changes the status of the merged system object to "merged".
     * If the system objects were merged within the same enterprise object and
     * the update mode is set to pessimistic, the application checks whether any
     * key fields (that is, fields used for matching or blocking) were updated in
     * the SBR. If key fields were updated, potential duplicates are recalculated
     * for the enterprise object.
     * <p>
     * If the two system objects originated from two different enterprise objects
     * and the enterprise object that contained the unkept the system object no longer
     * has any active system objects, that enterprise object is deactivated and all
     * associated potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields that are used for matching or blocking)
     * were updated in the SBR for each enterprise object. If key fields were updated,
     * potential duplicates are recalculated for each enterprise object.
     * <p>
     * The revision numbers of both the source and destination SBRs are passed as
     * arguments. These are compared to the SBRs stored in the database.  If they
     * differ, it means that either the source or destination records were modified
     * by another user.  In this case, the merge should not be allowed.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be merged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param srcRevisionNumber  The SBR revision number of the source enterprise object.
     * @param destRevisionNumber  The SBR revision number of the destination enterprise
     * object.
     * @param calculateOnly A Boolean indicator of whether to commit changes to the
     * database or to simply compute the merge results. Specify <b>false</b> to commit
     * the changes.
     * @return <CODE>MergeResult</CODE> - The result of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeSystemObject(
            String systemCode, 
            String sourceLID,
            String destLID, 
            String srcRevisionNumber, 
            String destRevisionNumber,
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeSystemObject(con, systemCode, sourceLID, destLID, srcRevisionNumber, destRevisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges the source system object into the destination system object. This
     * merge method allows the new image of the merged system object to be defined
     * if the sending system includes this information. The system objects
     * may belong to a single enterprise object or to two different enterprise
     * objects. They must originate from the same system. When this method
     * is called with calculateOnly set to false, the
     * application changes the status of the merged system object to "merged".
     * If the system objects were merged within the same enterprise object and
     * the update mode is set to pessimistic, the application checks whether any
     * key fields (that is, fields used for matching or blocking) were updated in
     * the SBR. If key fields were updated, potential duplicates are recalculated
     * for the enterprise object.
     * <p>
     * If the two system objects originated from two different enterprise objects
     * and the enterprise object that contained the unkept the system object no longer
     * has any active system objects, that enterprise object is deactivated and all
     * associated potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields that are used for matching or blocking)
     * were updated in the SBR for each enterprise object. If key fields were updated,
     * potential duplicates are recalculated for each enterprise object.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be merged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param destImage The new image for the destination system object.
     * For example, if the system object contains a person object, then a
     * person object must be used.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The results of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeSystemObject(
            String systemCode, 
            String sourceLID,
            String destLID, 
            ObjectNode destImage, 
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeSystemObject(con, systemCode, sourceLID, destLID, destImage, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges the source system object into the destination system object. This
     * merge method allows the new image of the merged system object to be defined
     * if the sending system includes this information. The system objects
     * may belong to a single enterprise object or to two different enterprise
     * objects. They must originate from the same system. When this method
     * is called with calculateOnly set to false, the
     * application changes the status of the merged system object to "merged".
     * If the system objects were merged within the same enterprise object and
     * the update mode is set to pessimistic, the application checks whether any
     * key fields (that is, fields used for matching or blocking) were updated in
     * the SBR. If key fields were updated, potential duplicates are recalculated
     * for the enterprise object.
     * <p>
     * If the two system objects originated from two different enterprise objects
     * and the enterprise object that contained the unkept the system object no longer
     * has any active system objects, that enterprise object is deactivated and all
     * associated potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields that are used for matching or blocking)
     * were updated in the SBR for each enterprise object. If key fields were updated,
     * potential duplicates are recalculated for each enterprise object.
     * <p>
     * The SBR revision numbers of both the source and destination enterprise objects
     * are passed as arguments. These are compared to the SBRs stored in the database.
     * If they differ, it means that either the source or destination record was modified
     * by another user.  In this case, the merge should not be allowed.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be merged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param destImage The new image for the destination system object.
     * For example, if the system object contains a person object, then a
     * person object must be used.
     * @param srcRevisionNumber  The SBR revision number of the source enterprise
     * object.
     * @param destRevisionNumber  The SBR revision number of the destination enterprise
     * object.
     * @param calculateOnly A Boolean indicator of whether to commit changes to the
     * database or to simply compute the merge results. Specify <b>false</b> to commit
     * the changes.
     * @return <CODE>MergeResult</CODE> - The result of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeSystemObject(
            String systemCode, 
            String sourceLID,
            String destLID, 
            ObjectNode destImage, 
            String srcRevisionNumber,
            String destRevisionNumber, 
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeSystemObject(con, systemCode, sourceLID, destLID, destImage,
                    srcRevisionNumber, destRevisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Merges the source system object into the destination system object. This
     * merge method allows the new image of the merged system object to be defined
     * if the sending system includes this information. The system objects
     * may belong to a single enterprise object or to two different enterprise
     * objects. They must originate from the same system. When this method
     * is called with calculateOnly set to false, the
     * application changes the status of the merged system object to "merged".
     * <p>
     * If the system objects were merged within the same enterprise object and
     * the update mode is set to pessimistic, the application checks whether any
     * key fields (that is, fields used for matching or blocking) were updated in
     * the SBR. If key fields were updated, potential duplicates are recalculated
     * for the enterprise object. Regardless of the value of the update mode, if
     * the performPessimistic flag is set to <b>true</b> the application checks for
     * key field updates and then checks for potential duplicates. If the
     * performPessimistic flag is set to <b>false</b>, neither of these steps is
     * performed.
     * <p>
     * If the system objects originated from two different enterprise objects
     * and the enterprise object that contained the unkept the system object no longer
     * has any active system objects, that enterprise object is deactivated and all
     * associated potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the application checks
     * whether any key fields (that is, fields that are used for matching or blocking)
     * were updated in the SBR for each enterprise object. If key fields were updated,
     * potential duplicates are recalculated for each enterprise object. Again, the value
     * of the performPessimistic flag overrides the value of the update mode.
     * <p>
     * The SBR revision numbers of both the source and destination enterprise objects
     * are passed as arguments. These are compared to the SBRs stored in the database.
     * If they differ, it means that either the source or destination record was modified
     * by another user.  In this case, the merge should not be allowed.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be merged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param destImage The new image for the destination system object.
     * For example, if the system object contains a person object, then a
     * person object must be used.
     * @param srcRevisionNumber  The SBR revision number of the source enterprise
     * object.
     * @param destRevisionNumber  The SBR revision number of the destination enterprise
     * object.
     * @param calculateOnly A Boolean indicator of whether to commit changes to the
     * database or to simply compute the merge results. Specify <b>false</b> to commit
     * the changes.
     * @param performPessimistic A Boolean indicator of whether to recalculate
     * potential duplicates on update or to defer it until later. Specify <b>true</b>
     * to recalculate on update; specify <b>false</b> to defer the recalculation.
     * @return <CODE>MergeResult</CODE> - The result of the merge operation.
     * @exception ProcessingException Thrown if an error occurs during
     * the merge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult mergeSystemObject(
            String systemCode, 
            String sourceLID,
            String destLID, 
            ObjectNode destImage, 
            String srcRevisionNumber,
            String destRevisionNumber, 
            boolean calculateOnly, 
            Boolean performPessimistic)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.mergeSystemObject(con, systemCode, sourceLID, destLID, destImage,
                    srcRevisionNumber, destRevisionNumber, calculateOnly, performPessimistic);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Unmerges the two system objects that were involved in the most
     * recent merge transaction for the specified local ID. The system
     * object that had a status of "merged" after the merge transaction
     * is changed to an active object. When this method is called with
     * calculateOnly set to false, the application changes the status of
     * the merged system object back to "active". If the source enterprise
     * object (the object that contained the merge result system object
     * after the merge) has more than one active system object after the
     * unmerge and the update mode is set to pessimistic, the application
     * checks whether any key fields were updated in that object. If key
     * fields were updated, potential duplicates are recalculated for the
     * source enterprise object.
     * <p>
     * If the source enterprise object has only one active system, potential
     * duplicate processing is performed regardless of the update mode and
     * regardless of whether there were any changes to key fields. If the update
     * mode is set to pessimistic, the application checks whether any key fields
     * were updated in the SBR for the destination enterprise object. If key
     * fields were updated, potential duplicates are recalculated for the
     * enterprise object.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be unmerged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param calculateOnly A Boolean indicator of whether
     * to commit changes to the database or to simply compute the
     * merge results. Specify <b>false</b> to commit the changes.
     * @return <CODE>MergeResult</CODE> - The result of the unmerge
     * transaction.
     * @exception ProcessingException Thrown if an error occurs during
     * the unmerge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult unmergeSystemObject(
            String systemCode,
            String sourceLID, 
            String destLID, 
            boolean calculateOnly)
    throws ProcessingException, UserException{
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.unmergeSystemObject(con, systemCode, sourceLID, destLID, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    /**
     * Unmerges the two system objects that were involved in the most
     * recent merge transaction for the specified local ID. The system
     * object that had a status of "merged" after the merge transaction
     * is changed to an active object. When this method is called with
     * calculateOnly set to false, the application changes the status of
     * the merged system object back to "active". If the source enterprise
     * object (the object that contained the merge result system object
     * after the merge) has more than one active system object after the
     * unmerge and the update mode is set to pessimistic, the application
     * checks whether any key fields were updated in that object. If key
     * fields were updated, potential duplicates are recalculated for the
     * source enterprise object.
     * <p>
     * If the source enterprise object has only one active system, potential
     * duplicate processing is performed regardless of the update mode and
     * regardless of whether there were any changes to key fields. If the update
     * mode is set to pessimistic, the application checks whether any key fields
     * were updated in the SBR for the destination enterprise object. If key
     * fields were updated, potential duplicates are recalculated for the
     * enterprise object.
     * <p>
     * The SBR revision number of the enterprise object containing the system
     * objects to unmerge is passed as an argument. Before finalizing the
     * unmerge, the revision number is compared to the SBR stored in the database.
     * If they differ, it means that the record was modified by another user.  In
     * this case, the unmerge should not be allowed.
     * <p>
     * @param systemCode The system to which the local IDs of the objects to
     * be unmerged belong.
     * @param sourceLID The local ID of the non-surviving system object.
     * @param destLID The local ID of the surviving system object.
     * @param srcRevisionNumber  The SBR revision number of the enterprise
     * object containing the system objects to unmerge.
     * @param calculateOnly A Boolean indicator of whether to commit changes
     * to the database or to simply compute the merge results. Specify <b>false</b>
     * to commit the changes.
     * @return <CODE>MergeResult</CODE> - The result of the unmerge transaction.
     * @exception ProcessingException Thrown if an error occurs during
     * the unmerge process.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public MergeResult unmergeSystemObject(
            String systemCode,
            String sourceLID,
            String destLID,
            String srcRevisionNumber,
            boolean calculateOnly)
    throws ProcessingException, UserException {
        MergeResult mergeResult = null;
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mergeResult = mControllerImpl.unmergeSystemObject(con, systemCode, sourceLID, destLID, srcRevisionNumber, calculateOnly);
            if (!calculateOnly) {
                mControllerImpl.commitTransaction(con);
            } else {
                mControllerImpl.rollbackTransaction(con);
            }
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return mergeResult;
    }
    
    
    /**
     * Changes the status of a deactivated system object back to active. If
     * the update mode in Threshold configuration is pessimistic, the application
     * checks whether any key fields (that is, fields used for
     * matching or blocking) were updated in the SBR. If key fields were
     * updated, potential duplicates are recalculated for the enterprise object.
     * <p>
     * @param systemKey The system code and local ID of the system object
     * to activate.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * changing the status.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void activateSystemObject(
            SystemObjectPK systemKey)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.activateSystemObject(con, systemKey);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Changes the status of a deactivated enterprise object back
     * to active. Since all potential duplicates were deleted when the EUID
     * was originally deactivated, potential duplicates are always recalculated,
     * regardless of the update mode.
     * <p>
     * @param euid The EUID associated with the enterprise object
     * to activate.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * changing the status.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void activateEnterpriseObject(
            String euid)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.activateEnterpriseObject(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Changes the status of a system object from active to inactive. If
     * the enterprise object containing this system object has no active system
     * objects remaining, the enterprise object is deactivated and all potential
     * duplicate listings are deleted. If the enterprise object has active system
     * objects after the transaction and the update mode is set to pessimistic,
     * the application checks whether any key fields (that is, fields used for
     * matching or blocking) were updated in the SBR. If fields were updated,
     * potential duplicates are recalculated for the enterprise object.
     * <p>
     * @param systemKey The system code and local ID of the system object
     * to deactivate.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * changing the status.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void deactivateSystemObject(
            SystemObjectPK systemKey)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.deactivateSystemObject(con, systemKey);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Changes the status of an enterprise object from active to
     * inactive and deletes all potential duplicate listings for that
     * object.
     * <p>
     * @param euid The EUID associated with the enterprise object
     * to deactivate.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * changing the status.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void deactivateEnterpriseObject(
            String euid)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.deactivateEnterpriseObject(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Permanently deletes a system object from its associated enterprise
     * object. If the enterprise object containing this system object has no
     * active system objects remaining, the enterprise object is also deleted,
     * along with any potential duplicate listings. If the enterprise object
     * has active system records after the transaction and the update mode is
     * set to pessimistic, the application checks whether any key fields (that
     * is, fields used for matching or blocking) were
     * updated in the SBR. If key fields were updated, potential duplicates are
     * recalculated for the enterprise record.
     * <p>
     * @param systemKey The system code and local ID of the system object
     * to delete.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * deleting the object.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void deleteSystemObject(
            SystemObjectPK systemKey)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.deleteSystemObject(con, systemKey);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Transfers the specified system object from its current enterprise
     * object to a different enterprise object. If the enterprise object
     * from which the system object was transferred no longer has any active
     * system objects, that enterprise object is deactivated and all associated
     * potential duplicate listings are deleted. If both enterprise objects
     * are still active and the update mode is set to pessimistic, the
     * application checks whether any key fields (that is, fields used for matching
     * or blocking) were updated in the SBR for each enterprise object. If key
     * fields were updated, potential duplicates are recalculated for each
     * enterprise object.
     * <p>
     * @param toEUID The EUID of the enterprise object to which the system
     * object will be transferred.
     * @param systemKey The system code and local ID of the system object
     * to transfer.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * transferring the object.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void transferSystemObject(
            String toEUID, 
            SystemObjectPK systemKey)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.transferSystemObject(con, toEUID, systemKey);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    /**
     * Calculates a new single best record (SBR) for an enterprise object
     * that has been updated.
     * <p>
     * @param eo The enterprise object whose SBR will be recalculated.
     * @return <CODE>SBR</CODE> - The recalculated SBR of the enterprise
     * object.
     * @exception ProcessingException Thrown if an error occurs while
     * transferring the object.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public SBR calculateSBR(
            EnterpriseObject eo)
    throws ProcessingException, UserException {
        return mControllerImpl.calculateSBR(eo);
    }
    
    /**
     * Inserts an audit log record of a transaction into the database.
     * <p>
     * @param auditObject The audit log record to insert.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException Thrown if an error occurs while
     * transferring the object.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public void insertAuditLog(
            AuditDataObject auditObject)
    throws ProcessingException, UserException {
        Connection con = null;

        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            mControllerImpl.insertAuditLog(con, auditObject);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
    }
    
    
    
    /**
     * Looks up an audit log record based on the criteria contained in
     * an audit search object.
     * <p>
     * @param obj An instance of AuditSearchObject containing the audit
     * log search criteria.
     * @return <CODE>AuditIterator</CODE> - An iterator of audit log matches
     * to the given search criteria.
     * @exception ProcessingException Thrown if an error occurs during
     * the lookup.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public AuditIterator lookupAuditLog(
            AuditSearchObject obj)
    throws ProcessingException, UserException {
        AuditIterator iterator = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            iterator = mControllerImpl.lookupAuditLog(con, obj);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } catch (UserException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return iterator;
    }
    
    /**
     * Retrieves the configuration for a master controller parameter, such as
     * the EUID length, duplicate threshold, or match threshold.
     * <p>
     * @param param The parameter name. These parameters are defined in the
     * Threshold configuration file of the Master Index Project.
     * @return <CODE>Object</CODE> - An object containing the value of the
     * specified parameter.
     * @exception ProcessingException Thrown if an error occurs during
     * the lookup.
     * @exception UserException Thrown if a parameter is invalid.
     * @include
     */
    public Object getConfigurationValue(
            String param)
    throws ProcessingException, UserException {
        return mControllerImpl.getConfigurationValue(param);
    }
    
    /**
     * Retrieves the status of the master index database.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The status of the master index database.
     * @exception ProcessingException Thrown if an error occurs while checking
     * the status.
     * @exception UserException Thrown if the call is invalid.
     * @include
     */
    public String getDatabaseStatus()
    throws ProcessingException, UserException {
        return mControllerImpl.getDatabaseStatus();
    }
    
    /**
     * Retrieves the SBR revision number for the specified EUID.
     * <p>
     * @param euid  The EUID containing the SBR revision number to retrieve.
     * @return <CODE>Integer</CODE> - The revision number for the SBR.
     * @exception ProcessingException Thrown if an error occurs during
     * the lookup.
     * @include
     */
    public Integer getRevisionNumber(
            String euid)
    throws ProcessingException {
        Integer rn = null;
        Connection con = null;
        
        try {
            mControllerImpl.beginTransaction();
            con = mControllerImpl.getConnection();
            rn = mControllerImpl.getRevisionNumber(con, euid);
            mControllerImpl.commitTransaction(con);
        } catch (ProcessingException e) {
            mControllerImpl.rollbackTransaction(con);
            throw e;
        } finally {
            mControllerImpl.releaseResources( con );
        }
        return rn;
    }
    
    /**
     *  Retrieve the potential duplicate threshold.
     *
     * @returns the value of the potential duplicate threshold.
     */
    public float getDuplicateThreshold()  {
        return mControllerImpl.getDuplicateThreshold();
    }

    /**
     *  Retrieve the Assumed Match threshold.
     *
     * @returns the value of the Assumed Match threshold.
     */
    public float getAssumedMatchThreshold() {
        return mControllerImpl.getAssumedMatchThreshold();
    }
    
    // for SBROverriding
    /** Updates SBR by collecting the values from MAP to the SBR that specified by EUID.
    *
    * @param mapSystems The Map consists of epath as key and System as value from which the filed should take for updating SBR
    * @param euid The EUID of SBR on which the updation of SBR to perform.
    *
    */
    public EnterpriseObject updateSBR(Map mapSystems, EnterpriseObject eo, boolean removalFlag)
            throws ProcessingException, UserException {
        return mControllerImpl.updateSBR(mapSystems, eo, removalFlag);
		// updateEnterpriseObject(eo);
    }
    
    /** Returns a map with (fieldName, actual value for link) for the given EO.
    *
    * @param eo The EnterpriseObject that has LINKs
    * @return resultMap map with (fieldName, actual value for link) for the given EO.
    * @throws ObjectException An error occured.
    * @throws ConnectionInvalidException An error occured.
    * @throws OPSException An error occured.
    *
    */
    public Map getLinkValues(EnterpriseObject eo)
            throws ObjectException, ConnectionInvalidException, OPSException, ProcessingException {
        Connection con = null;
        Map result;
        try {            
            con = mControllerImpl.getConnection();
            result = mControllerImpl.getLinkValues(eo, con);            
        } catch (ProcessingException e) {
            throw e;
        } finally {
            mControllerImpl.releaseResources(con);
        }
        return result;
    }
    @AroundInvoke
    public Object intercept(InvocationContext invocation) throws Exception {
    	securityManager.setCurrentMethod(invocation.getMethod().getName());
    	return invocation.proceed();
 
    }
    
}
