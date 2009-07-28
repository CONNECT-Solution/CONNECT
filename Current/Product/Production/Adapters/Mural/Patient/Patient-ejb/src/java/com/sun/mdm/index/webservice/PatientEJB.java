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

package com.sun.mdm.index.webservice;

import com.sun.mdm.index.ejb.master.MasterControllerLocal;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.*;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.annotation.security.RunAs;

/** 
    Wrapper class to interface MasterController. It invokes methods of MasterControllerEJB
    which is a Stateless session bean. The objects exposed in this class are Java beans that can
    be exposed in BPEL editor or any other editor. The methods are type specific
    to Patient where methods in MasterController are not any object type specific.
 */
@Stateless
@WebService
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PatientEJB {
    private final static String ALPHA_SEARCH = "ALPHA-SEARCH";
    private final static String BLOCKER_SEARCH = "BLOCKER-SEARCH";
    private final static String PHONETIC_SEARCH = "PHONETIC-SEARCH";
    private final static String EJBLOCAL_MASTER = "ejb/PatientMasterController";

    @Resource
    private SessionContext mSessionContext;
    
    @EJB(beanInterface=MasterControllerLocal.class)
    private MasterControllerLocal mMC;

    private final Logger mLogger = LogUtil.getLogger(this);
    
    /**
     * No argument constructor required by container.
     */
    public PatientEJB() {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("Initializing:" + "PatientEJB");
        }
    }

    /** 
     * Processes the system object based on configuration options defined for
     * the master controller and associated components in the runtime xml file.
     * The options affect executeMatch in the following ways: a) Query Builder
     * class and options sets for blocking b) Matching rules, Pass Controller
     * and Block Picker classes c) Decision Maker class and options
     *
     * @param sysObjBean System object to process.
     * @exception ProcessingException An error has occured.
     * @exception UserException A user error occured
     * @return MatchColResult.
     */
    @WebMethod 
    public MatchColResult executeMatch(
        @WebParam(name = "sysObjBean") SystemPatient sysObjBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("PatientEJB" + "executeMatch" + sysObjBean);
        }

        SystemObject sourceSystem = sysObjBean.pGetSystemObject();
        MatchResult matchResult = mMC.executeMatch(sourceSystem);

        if (mLogger.isDebugEnabled()) {
            mLogger.info("matchResult EUID:" + matchResult.getEUID());
        }

        return new MatchColResult(matchResult);
    }
    
    /** 
     * Processes the system object in update mode.  Null fields are left unchanged.
     *
     * @param sysObjBean System object to process.
     * @exception ProcessingException An error has occured.
     * @exception UserException A user error occured
     * @return MatchColResult.
     */
    @WebMethod 
    public MatchColResult executeMatchUpdate(
        @WebParam(name = "sysObjBean") SystemPatient sysObjBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("PatientEJB" + "executeMatchUpdate" + sysObjBean);
        }

        SystemObject sourceSystem = sysObjBean.pGetSystemObject();
        MatchResult matchResult = mMC.executeMatchUpdate(sourceSystem);

        if (mLogger.isDebugEnabled()) {
            mLogger.info("matchResult EUID:" + matchResult.getEUID());
        }

        return new MatchColResult(matchResult);
    }    
   

    /** 
     * Does the exact search on passed ObjectBean attribute values
     *
     * @param objBean The object bean contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchObjectResult[]
     */
    @WebMethod(exclude=true)
    public SearchObjectResult[] pSearchExact(
        ObjectBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("psearchExact:" + objBean);
        }

        return search(objBean, ALPHA_SEARCH, false);                        
    }
    
    /** 
     * Does the exact search on passed ObjectBean attribute values
     *
     * @param objBean The object bean contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchPatientResult[]
     */ 
    @WebMethod
    public SearchPatientResult[] searchExact(
        @WebParam(name = "objBean") PatientBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("searchExact:" + objBean);        
        }  

        return (SearchPatientResult[]) pSearchExact(objBean);             
    }

    /** 
     * Does the standardized search on passed PatientBean attribute values
     *
     * @param objBean The Patient contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchObjectResult[]
     */
    @WebMethod(exclude=true)
    public SearchObjectResult[] pSearchPhonetic(
        ObjectBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pSearchPhonetic:" + objBean);
        }

        return search(objBean, PHONETIC_SEARCH, true);            
    }

    /** 
     * Does the standardized search on passed PatientBean attribute values
     *
     * @param objBean The Patient contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchPatientResult[]
     */ 
    @WebMethod
    public SearchPatientResult[] searchPhonetic(
        @WebParam(name = "objBean") PatientBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("SearchPhonetic:" + objBean); 
        }

        return (SearchPatientResult[]) pSearchPhonetic(objBean);             
    }
    
    /** 
     * Does the block search on passed PatientBean attribute values
     *
     * @param objBean The Patient contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchObjectResult[]
     */
    @WebMethod(exclude=true)
    public SearchObjectResult[] pSearchBlock(
        ObjectBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pSearchBlock:" + objBean); 
        }

        return search(objBean, BLOCKER_SEARCH, true);
    }

    /** 
     * Does the block search on passed PatientBean attribute values
     *
     * @param objBean The Patient contains the search criteria.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return SearchPatientResult[]
     */
    @WebMethod
    public SearchPatientResult[] searchBlock(
        @WebParam(name = "objBean") PatientBean objBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("SearchBlock:" + objBean);   
        }

        return (SearchPatientResult[]) pSearchBlock(objBean);             
    }
    
    /** 
     * Return EnterpiseObject associated with EUID or null if not found.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return EnterpriseObject for given EUID or null if not found.
     */
    @WebMethod(exclude=true)
    public EnterpriseObjectBean pGetEnterpriseRecordByEUID(
        String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetEnterpriseRecordByEUID:" + euid); 
        }

        EnterpriseObject eo = mMC.getEnterpriseObject(euid);

        if (null == eo) {
           return null;
        }

        return new EnterprisePatient(eo);
    }

    /** 
     * Return EnterpiseObject associated with EUID or null if not found.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return EnterprisePatient for given EUID or null if not found.
     */
    @WebMethod
    public EnterprisePatient getEnterpriseRecordByEUID(
        @WebParam(name = "euid") String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getEnterpriseRecordByEUID:" + euid);
        }

        return (EnterprisePatient) pGetEnterpriseRecordByEUID(euid);
    }

   /** 
     * Return EnterpriseObject associated with a system object key or null if
     * not found.  Only active system objects can be used for the lookup.
     *
     * @param key The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid key (null or empty string)
     * @return EnterpriseObject for given key.
     */
    @WebMethod(exclude=true)
    public EnterpriseObjectBean pGetEnterpriseRecordByLID(
        String systemCode, 
        String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetEnterpriseRecordByLID:" + localid + ",systemCode:" + systemCode);
        }

        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);            
        EnterpriseObject eo = mMC.getEnterpriseObject(skey);

        if (null == eo) {
            return null;
        }

        return new EnterprisePatient(eo);
    }

   /** 
     * Return EnterpriseObject associated with a system object key or null if
     * not found.  Only active system objects can be used for the lookup.
     *
     * @param key The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid key (null or empty string)
     * @return EnterprisePatient for given key.
     */
    @WebMethod
    public EnterprisePatient getEnterpriseRecordByLID(    	
        @WebParam(name = "systemCode") String systemCode, 
        @WebParam(name = "localid") String localid)
        throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getEnterpriseRecordByLID:" + localid + ",systemCode:" + systemCode);
        }

        return (EnterprisePatient) pGetEnterpriseRecordByLID(systemCode, localid);
    }

    /** 
     * Return SBR associated with an EUID or null if not found.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid (null or empty string)
     * @return SBR for given EUID.
     */
    @WebMethod(exclude=true)
    public SBRObjectBean pGetSBR(
        String euid)
    throws ProcessingException, UserException  {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetSBR EUID:" + euid);
        }
 
        SBR sbr = mMC.getSBR(euid);

        if (null == sbr) {
           return null;
        }

        return new SBRPatient(sbr);

    }

    /** 
     * Return SBR associated with an EUID or null if not found.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid (null or empty string)
     * @return SBRPatient for given EUID.
     */    
    @WebMethod
    public SBRPatient getSBR(
        @WebParam(name = "euid")  String euid) 
    throws ProcessingException, UserException  {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getSBR EUID:" + euid);
        }

        return (SBRPatient) pGetSBR(euid);

    }
    
    /** 
     * Return EUID associated with a system object key or null if not found.
     *
     * @param key The system object key on which to perform the action
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid key (nulls or empty strings)
     * @return EUID for given key.
     */
    @WebMethod 
    public String getEUID(
         @WebParam(name = "systemCode") String systemCode, 
         @WebParam(name = "localid") String localid)    
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getEUID systemCode:" + systemCode + ", localid:" + localid);
        }

        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);

        return mMC.getEUID(skey);
    }




    /** 
     * Update the database to reflect the new values of the given modified
     * system object.
     *
     * @param sysobjBean The SO to be updated.
     * @exception ProcessingException An error has occured.
     * @exception UserException A user error occured
     */
    @WebMethod     
    public void updateSystemRecord(
        @WebParam(name="sysObjBean") SystemPatient sysObjBean)
    throws ProcessingException, UserException  {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("updateSystemRecord:" + sysObjBean);
        }

        SystemObject so = sysObjBean.pGetSystemObject();                      
        mMC.updateSystemObject(so);
    }


    
   /** 
     * Update the database to reflect the new values of the given modified
     * enterprise object.
     *
     * @param eoBean The EO to be updated.
     * @exception ProcessingException An error has occured.
     * @exception UserException A user error occured
     */
    @WebMethod 
    public void updateEnterpriseRecord(
        @WebParam(name = "eoBean") EnterprisePatient eoBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("updateEnterpriseRecord:" + eoBean);
        }
 
        EnterpriseObject originalEO = mMC.getEnterpriseObject(eoBean.getEUID());
        EnterpriseObject eo = eoBean.pUpdateEnterpriseObject(originalEO);
        mMC.updateEnterpriseObject(eo);
    }
    
    


    /** 
     * Adds the SystemObject to the EnterpriseObject specified by EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @param sysObj The system object to be added.
     * @exception ProcessingException An error has occured.
     * @exception UserException A user error occured
     */
    @WebMethod 
    public void addSystemRecord(
        @WebParam(name = "euid") String euid, 
        @WebParam(name = "sysObjBean") SystemPatient sysObjBean)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("addSystemRecord:" + sysObjBean);
        }

        SystemObject so = sysObjBean.pGetSystemObject();    
        mMC.addSystemObject(euid, so);
    }

    /** 
     * Merge the enterprise records based on the given EUID's.
     *
     * @param sourceEUID The EUID to be merged.
     * @param destinationEUID The EUID to be kept.
     * @param calculateOnly Indicate whether to commit changes to DB or just
     * compute the MergeResult.  See Constants.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euids
     * @return Result of merge operation.
     */
    @WebMethod(exclude=true)
    public MergePatientResult pMergeEnterpriseRecord(
        String fromEUID, 
        String toEUID,
        boolean calculateOnly)
    throws ProcessingException, UserException  {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pMergeEnterpriseRecord fromEUID:" + fromEUID + ", toEUID" + toEUID );
        }
            
        MergeResult mresult = mMC.mergeEnterpriseObject(fromEUID, toEUID, calculateOnly);

        return new MergePatientResult(mresult);
        
    }

    /** 
     * Merge the enterprise records based on the given EUID's.
     *
     * @param sourceEUID The EUID to be merged.
     * @param destinationEUID The EUID to be kept.
     * @param calculateOnly Indicate whether to commit changes to DB or just
     * compute the MergeResult.  See Constants.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euids
     * @return Result of merge operation.
     */
    @WebMethod
    public MergePatientResult mergeEnterpriseRecord(
        @WebParam(name = "fromEUID") String fromEUID, 
        @WebParam(name = "toEUID") String toEUID,
        @WebParam(name = "calculateOnly") boolean calculateOnly)
    throws ProcessingException, UserException  {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("mergeEnterpriseRecord fromEUID:" + fromEUID + ", toEUID" + toEUID );
        }

        return (MergePatientResult) pMergeEnterpriseRecord(fromEUID, toEUID, calculateOnly);        
    }
 
   /** 
     * Return SystemObject associated with a key or null if not found.
     *
     * @param key The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid key (null or empty string)
     * @return SystemObject for given key or null if not found.
     */
    @WebMethod(exclude=true)
    public SystemObjectBean pGetSystemRecord(
        String systemCode, 
        String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetSystemRecord systemCode:" + systemCode + ", localid" + localid );
        }

        SystemPatient s = null;
        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);
        SystemObject so = mMC.getSystemObject(skey);

        if (null != so) {
            s = new SystemPatient(so);
        }
            
        return s;
    }

   /** 
     * Return SystemObject associated with a key or null if not found.
     *
     * @param key The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid key (null or empty string)
     * @return SystemPatient for given key or null if not found.
     */    
    @WebMethod
    public SystemPatient getSystemRecord(
        @WebParam(name = "systemCode") String systemCode, 
        @WebParam(name = "localid") String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getSystemRecord systemCode:" + systemCode + ", localid" + localid );
        }

        return (SystemPatient) pGetSystemRecord(systemCode, localid);
    }
    
    /** Lookup active system objects only for the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid 
     * @return Array of system objects.
     */
    @WebMethod(exclude=true)
    public SystemObjectBean[] pGetSystemRecords(
        String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetSystemRecord EUID:" + euid );
        }

        SystemPatient[] systemPatients = null;
        SystemObject[] systemObjects = mMC.lookupSystemObjects(euid);

        if (null != systemObjects) {
            int l = systemObjects.length;
            systemPatients = new SystemPatient[l];
            for (int i = 0; i < l; i++) {
                systemPatients[i] = new SystemPatient(systemObjects[i]); 
            }
        }
        
        return systemPatients;
    }

    /** Lookup active system objects only for the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid 
     * @return Array of SystemPatient.
     */
    @WebMethod
    public SystemPatient[] getSystemRecordsByEUID(
        @WebParam(name = "euid") String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getSystemRecordsByEUID EUID:" + euid );
        }

        return (SystemPatient[]) pGetSystemRecords(euid);
    }

    /** 
     * Lookup system objects with the given EUID and status
     *
     * @param euid The EUID on which to perform the action.
     * @param status Status filter.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid parameters
     * @return Array of system objects.
     */
    @WebMethod(exclude=true)
    public SystemObjectBean[] pGetSystemRecords(
        String euid, 
        String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetSystemRecords EUID:" + euid +", status:" + status );
        }

        SystemPatient[] systemPatients = null;
        SystemObject[] systemObjects = mMC.lookupSystemObjects(euid, status);

        if (null != systemObjects) {
            int l = systemObjects.length;
            systemPatients = new SystemPatient[l];
            for (int i = 0; i < l; i++) {
                systemPatients[i] = new SystemPatient(systemObjects[i]); 
            }
        }
        
        return systemPatients;
    }

    /** 
     * Lookup system objects with the given EUID and status
     *
     * @param euid The EUID on which to perform the action.
     * @param status Status filter.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid parameters
     * @return Array of SystemPatient.
     */
    @WebMethod
    public SystemPatient[] getSystemRecordsByEUIDStatus(
        @WebParam(name = "euid") String euid, 
        @WebParam(name = "status") String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getSystemRecordsByEUIDStatus EUID:" + euid +", status:" + status );
        }
        
        return (SystemPatient[]) pGetSystemRecords(euid, status);
    }


   /** 
     * Returns an array of all system object keys belonging to the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid 
     * @return Array of system object keys.
     */
    @WebMethod(exclude=true)
    public SystemObjectPKBean[] pGetLIDs(String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetLIDs EUID:" + euid);
        }

        SystemPatientPK[] objNamePKArr = null;
        SystemObjectPK[] sysObjPKArr = mMC.lookupSystemObjectPKs(euid);
        if (null != sysObjPKArr) {
            int count = sysObjPKArr.length;
            objNamePKArr = new SystemPatientPK[count];
            for (int i = 0; i < count; i++) {
                objNamePKArr[i] = new SystemPatientPK(sysObjPKArr[i]);
            }
        }
        
        return objNamePKArr;
    }

   /** 
     * Returns an array of all system object keys belonging to the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid 
     * @return Array of SystemPatientPK.
     */
    @WebMethod
    public SystemPatientPK[] getLIDs(
        @WebParam(name = "euid") String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("getLIDs EUID:" + euid);
        }

        return (SystemPatientPK[]) pGetLIDs(euid);
    }

    /** 
     * Returns an array of system object keys with the given status belonging 
     * to the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @param status Status filter.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid or status
     * @return Array of system object keys or null.
     */
    @WebMethod(exclude=true)
    public SystemObjectPKBean[] pGetLIDsByStatus(
        String euid, 
        String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetLIDs EUID:" + euid +", status:" + status);
        }

        SystemPatientPK[] objNamePKArr = null;            
        SystemObjectPK[] sysObjPKArr = mMC.lookupSystemObjectPKs(euid, status);
        if (null != sysObjPKArr) {
            int count = sysObjPKArr.length;
            objNamePKArr = new SystemPatientPK[count];
            for (int i = 0; i < count; i++) {
                objNamePKArr[i] = new SystemPatientPK(sysObjPKArr[i]);
            }
        }
       
        return objNamePKArr;
   }

    /** 
     * Returns an array of system object keys with the given status belonging 
     * to the given EUID.
     *
     * @param euid The EUID on which to perform the action.
     * @param status Status filter.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid or status
     * @return Array ofSystemPatientPK or null.
     */
    @WebMethod
    public SystemPatientPK[] getLIDsByStatus(
        @WebParam(name = "euid") String euid, 
        @WebParam(name = "status") String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pGetLIDs EUID:" + euid +", status:" + status);
        }

        return (SystemPatientPK[]) pGetLIDsByStatus(euid, status);
    }
    
    
    /** 
     * Returns an array of system object keys with the given status belonging 
     * to the given destination system whose EUID matches the source system 
     * code / lid.
     *
     * @param sourceSystemCode the source system code
     * @param sourceLID the source local id
     * @param destSystemCode the destination system code
     * @param status status of records in destination system to search for
     * @exception ProcessingException See MasterControllerEJB
     * @exception UserException See MasterControllerEJB
     * @return Array of system object keys or null if not found
     */
    @WebMethod(exclude=true)
    public SystemObjectPKBean[] pLookupLIDs(
        String sourceSystemCode, 
        String sourceLID, 
        String destSystemCode, 
        String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pLookupLIDs sourceSystemCode:" + sourceSystemCode + 
                ", sourceLID:" + sourceLID + ", destSystemCode:" + destSystemCode + ", status:" +
                status );
        }

        SystemPatientPK[] objNamePKArr = null;
        SystemObjectPK[] sysObjPKArr = mMC.lookupSystemObjectPKs(sourceSystemCode, sourceLID,  destSystemCode,  status);
        if (null != sysObjPKArr) {
            int count = sysObjPKArr.length;
            objNamePKArr = new SystemPatientPK[count];
            for (int i = 0; i < count; i++) {
                objNamePKArr[i] = new SystemPatientPK(sysObjPKArr[i]);
            }
        }
        
        return objNamePKArr;    
   }

    /** 
     * Returns an array of system object keys with the given status belonging 
     * to the given destination system whose EUID matches the source system 
     * code / lid.
     *
     * @param sourceSystemCode the source system code
     * @param sourceLID the source local id
     * @param destSystemCode the destination system code
     * @param status status of records in destination system to search for
     * @exception ProcessingException See MasterControllerEJB
     * @exception UserException See MasterControllerEJB
     * @return Array of SystemPatientPK or null if not found
     */
    @WebMethod
    public SystemPatientPK[] lookupLIDs(
        @WebParam(name = "sourceSystemCode") String sourceSystemCode, 
        @WebParam(name = "sourceLID") String sourceLID, 
        @WebParam(name = "destSystemCode") String destSystemCode, 
        @WebParam(name = "status") String status)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("lookupLIDs sourceSystemCode:" + sourceSystemCode + 
                ", sourceLID:" + sourceLID + ", destSystemCode:" + destSystemCode + ", status:" +
                status );
        }

        return (SystemPatientPK[]) pLookupLIDs(sourceSystemCode, sourceLID, destSystemCode, status);
    }
            
    
  
    /** 
     * Merge the two lids for the given system.  Note that the keys may both 
     * belong to a single EO, or may belong to two different EO's.
     *
     * @param systemCode The system to which these local id's belong.
     * @param sourceLID The lid to be merged.
     * @param destLID The lid to be kept.
     * @param calculateOnly Indicate whether to commit changes to DB or just
     * compute the MergeResult.  See Constants.
     * @exception ProcessingException See MasterControllerEJB
     * @exception UserException See MasterControllerEJB
     * @return See MasterControllerEJB
     */
    @WebMethod(exclude=true)
    public MergePatientResult pMergeSystemRecord(
        String systemCode, 
        String sourceLID,
        String destLID, 
        boolean calculateOnly)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("pMergeSystemRecord systemCode:" + systemCode + 
                ", sourceLID:" + sourceLID + ", destLID:" + destLID + ", calculateOnly:" +
                calculateOnly );
        }

        MergePatientResult mr = null;
        MergeResult mresult = mMC.mergeSystemObject(systemCode, sourceLID,
                destLID, calculateOnly);

        return new MergePatientResult(mresult);
   }
  
    /** 
     * Merge the two lids for the given system.  Note that the keys may both 
     * belong to a single EO, or may belong to two different EO's.
     *
     * @param systemCode The system to which these local id's belong.
     * @param sourceLID The lid to be merged.
     * @param destLID The lid to be kept.
     * @param calculateOnly Indicate whether to commit changes to DB or just
     * compute the MergeResult.  See Constants.
     * @exception ProcessingException See MasterControllerEJB
     * @exception UserException See MasterControllerEJB
     * @return MergePatientResult
     */   
   @WebMethod
   public MergePatientResult mergeSystemRecord(
       @WebParam(name = "systemCode") String systemCode, 
       @WebParam(name = "sourceLID") String sourceLID,
       @WebParam(name = "destLID") String destLID, 
       @WebParam(name = "calculateOnly") boolean calculateOnly)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("mergeSystemRecord systemCode:" + systemCode + 
                ", sourceLID:" + sourceLID + ", destLID:" + destLID + ", calculateOnly:" +
                calculateOnly );
        }

        return (MergePatientResult) pMergeSystemRecord(systemCode, sourceLID, 
                  destLID, calculateOnly);
   }


 

    /** Return a deactivated system object back to active status.
     *
     * @param systemKey The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid key (null or empty string)
     */
    @WebMethod 
    public void activateSystemRecord(
       @WebParam(name = "systemCode") String systemCode, 
       @WebParam(name = "localid") String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("activateSystemRecord systemCode:" + systemCode + 
                ", localid:" + localid);
        }

        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);
        mMC.activateSystemObject(skey);
   }

    
    /** 
     * Return a deactivated enterprise object back to active status.
     *
     * @param euid The euid on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid (null or empty string)
     */
    @WebMethod
    public void activateEnterpriseRecord(
        @WebParam(name = "euid") String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("activateSystemRecord euid:" + euid);
        }
            
        mMC.activateEnterpriseObject(euid);            
   }
    
    
    /** 
     * Deactivate a system object based on the given key. Note that this 
     * is different than deleteSystemObject in that the record is not removed
     * from the database, only its status is changed.
     *
     * @param systemKey The system object key on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid key (null or empty string)
     */
    @WebMethod 
    public void deactivateSystemRecord(
        @WebParam(name = "systemCode") String systemCode, 
        @WebParam(name = "localid") String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("deactivateSystemRecord systemCode:" + systemCode + 
                ", localid:" + localid);
        }

        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);
        mMC.deactivateSystemObject(skey);
    }

    
   /** 
     * Deactivate enterprise object based on key.
     *
     * @param euid The euid on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid euid (null or empty string)
     */
    @WebMethod 
    public void deactivateEnterpriseRecord(
        @WebParam(name = "euid") String euid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("deactivateSystemRecord euid:" + euid);
        }

        mMC.deactivateEnterpriseObject(euid);
   }
    

    /** 
     * Transfer system object from one enterprise object to another
     *
     * @param destinationEUID The EUID to transfer the SO to.
     * @param systemKey The key of the SO to transfer.
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid parameters
     */
    @WebMethod 
    public void transferSystemRecord(
        @WebParam(name = "toEUID") String toEUID, 
        @WebParam(name = "systemCode") String systemCode, 
        @WebParam(name = "localid") String localid)
    throws ProcessingException, UserException {

        if (mLogger.isDebugEnabled()) {
            mLogger.info("transferSystemRecord toEUID:" + toEUID + ", systemCode:" + systemCode +
                ", localid:" + localid);
        }
 
        SystemObjectPK skey = new SystemObjectPK(systemCode, localid);
        mMC.transferSystemObject(toEUID, skey);
    }
    
    /**
     * Returns an array of potential duplicate record based on search
     * criteria.
     * @param pdsoBean PotentialDuplicateSearchObjectBean
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid search object.
     * @exception PageException An error has occured.
     * @exception RemoteException An error has occured.
     * @return an array of PotentialDuplicateResult.
     */
    @WebMethod
    public PotentialDuplicateResult[] lookupPotentialDuplicates(
            @WebParam(name = "PotentialDuplicateSearchObjectBean")PotentialDuplicateSearchObjectBean pdsoBean) 
            throws ProcessingException, UserException, PageException, RemoteException{
        //if no value of pageSize or pageNumber are set(they default to 0), or 
        //they are set to 0 or less, then retrieve maximum records.
        int startIndex=(pdsoBean.getPageNumber()-1) * pdsoBean.getPageSize()+1;
        int endIndex=pdsoBean.getPageNumber() * pdsoBean.getPageSize();
        if (pdsoBean.getPageNumber()<1 || pdsoBean.getPageSize()<1){
            startIndex = 1;            
            if (pdsoBean.getMaxElements()>0){
                endIndex = pdsoBean.getMaxElements();
            }else{
                endIndex = Constants.DEFAULT_MAX_ELEMENTS;
            }                   
        }

        PotentialDuplicateIterator iterator = 
                mMC.lookupPotentialDuplicates(pdsoBean.getPotentialDuplicateSearchObjec());
        
        
        int count =0;
        ArrayList<PotentialDuplicateResult> results = new ArrayList<PotentialDuplicateResult>();
        while (iterator.hasNext()) {
            count++;
            if (count>endIndex){
                break;
            }
            PotentialDuplicateSummary pds = iterator.next();
            if (count>=startIndex){
                PotentialDuplicateResult pdbresult = new PotentialDuplicateResult(pds);
                results.add(pdbresult);
            }
        }
        int size = results.size();
        PotentialDuplicateResult[] pdbarray= new PotentialDuplicateResult[size];
        return results.toArray(pdbarray);        
    }
 
    /** 
     * Return EnterpiseObject associated with EUID or null if not found.
     *
     * @param euid The EUID on which to perform the action.
     * @exception ProcessingException An error has occured.
     * @throws UserException Invalid euid (null or empty string)
     * @return EnterpriseObject for given EUID or null if not found.
     */
    private SearchObjectResult[] search(
        ObjectBean objBean,
        String searchId, 
        boolean weightOption)
    throws ProcessingException, UserException {
        try {

            PatientBean aPatientBean = (PatientBean) objBean;
            EPathArrayList fields = new EPathArrayList();
            fields.add("Enterprise.SystemSBR." + "Patient" + ".EUID");
            SystemPatient sysPatient = new SystemPatient();
            sysPatient.setPatient(aPatientBean);
            SystemObject so = sysPatient.pGetSystemObject();

            String path = "Enterprise.SystemSBR." + "Patient";        
            populateEPathList(fields, path); 
        

            EOSearchOptions searchOptions = new EOSearchOptions(searchId, fields);
            searchOptions.setWeighted(weightOption);
            EOSearchCriteria criteria = new EOSearchCriteria(so);

            EOSearchResultIterator iterator = mMC.searchEnterpriseObject(criteria, searchOptions);
            ArrayList results = new ArrayList();
            while (iterator.hasNext()) {
                EOSearchResultRecord resultRecord = iterator.next();
                ObjectNode objectNode = resultRecord.getObject();
                String euid = resultRecord.getEUID();
                float weighted = resultRecord.getComparisonScore();
                PatientBean bean = new PatientBean ((PatientObject)objectNode);
                SearchPatientResult searchResult =  
                    new SearchPatientResult(bean, euid, weighted);
                results.add(searchResult);
            }

            int size = results.size();
            SearchPatientResult[] searchBeanResults = new SearchPatientResult[size];
            return (SearchPatientResult[])results.toArray(searchBeanResults);
        } catch (UserException ux) {
            throw ux;
        } catch (ProcessingException px) {
            throw px;
        } catch (Exception ex) {
            throw new ProcessingException(ex);
        }        
    }

    /** 
     * Populate EPath list.
     *
     * @param fields EPath array list.
     * @param path EPath.
     * @exception ProcessingException An error has occured.
     */
    private void populateEPathList(
        EPathArrayList fields, 
        String path)
    throws ProcessingException {
        try {
            
            String[] keyPaths = MetaDataService.getObjectFK(path);
            
            String[] fieldPaths = MetaDataService.getFieldPaths(path);
            for (int i = 0; i < fieldPaths.length; i++) {
                String fd = fieldPaths[i];
                boolean found = false;
                for (int j = 0; keyPaths != null && j < keyPaths.length; j++) {
                  String keyField = keyPaths[j];
                  int beginIndex = keyField.indexOf('.');
                  String keyStr = keyField.substring(beginIndex);
                  if (fd.endsWith(keyStr)) {
                      found = true;
                      break;
                  }
                }
                if ( found == false) {             
                   fields.add(fd);
                }
            }
            
            String childPaths[] = MetaDataService.getChildTypePaths(path);
            for (int i = 0; childPaths != null && i < childPaths.length; i++) {
                String childPath = childPaths[i];
                populateEPathList(fields, childPath);
             }
               
        } catch (Exception ex) {
            throw new ProcessingException(ex);
        }
    }
}
