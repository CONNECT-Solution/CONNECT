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

import java.util.*;
import com.sun.mdm.index.objects.*;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.update.UpdateHelper;

/**
 * EnterprisePatient value object
 */
public class EnterprisePatient implements EnterpriseObjectBean{
    public static final int version = 1; 
    private EnterpriseObject meo;
    private SBRPatient mSBRPatient;
    private SystemPatient[] mSystemPatients;
    private String mEUID;
    private String mStatus;

    /**
     * No argument constructor.
     */ 
    public EnterprisePatient() {
    }
    
    /**
     * Creates a new EnterprisePatient instance from a EnterpriseObject.
     */ 
    protected EnterprisePatient(EnterpriseObject eo) throws ObjectException {
        meo = eo;
        mSBRPatient = new SBRPatient(eo.getSBR());
        pSetSystemPatients();
        mEUID=meo.getEUID();
        mStatus = meo.getStatus();        
    }

    /**
     * Getter for EUID attribute of the EnterpriseObject object
     *
     * @exception ObjectException ObjectException
     * @return String
     */
    public String getEUID() throws ObjectException {
        return mEUID;
       
    }

    /**
     * Getter for SBRPatient attribute of the 
     * EnterprisePatient object
     *
     * @return SBRPatient
     */
    public SBRPatient getSBRPatient() {
        return mSBRPatient; 
    }
    
    /**
     * return SBRObjectBean of EnterprisePatient object
     */
    public SBRObjectBean pGetSBRObjectBean() {
        return getSBRPatient();
    }
    
    /**
     * Getter for Status attribute of the EnterpriseObject object
     *
     * @exception ObjectException ObjectException
     * @return String
     */
    public String getStatus() throws ObjectException {
        return mStatus;
    }

    /**
     * Return SystemObject of the EnterpriseObject object
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException ObjectException
     * @return SystemObject
     */
    public SystemPatient pGetSystemPatient(String system, String lid)
        throws ObjectException {
        SystemObject so = meo.getSystemObject(system, lid);
        return new SystemPatient(so);
    }
    
    
    /**
     * Return SystemObjectBean of the EnterpriseObject object 
     * from system code and local id
     *
     * @param systemCode System Code
     * @param lid Local ID
     * @exception ObjectException ObjectException
     * @return SystemObjectBean
     */
    public SystemObjectBean pGetSystemObjectBean(String systemCode, String lid)
        throws ObjectException {
        return pGetSystemPatient(systemCode, lid);
    }
            
    /**
     * Getter for SystemObjects attribute of the EnterpriseObject object
     *
     * @return Collection of SystemPatient
     */
    public SystemPatient[] getSystemPatient() {
        return mSystemPatients;
    }
    
    /**
     * Return the ith SystemPatient of the EnterpriseObject object
     * @param index index of SystemPatient
     * @return SystemPatient
     */
    public SystemPatient getSystemPatient(int index) {
        return mSystemPatients[index];
    }

    /**
     * Get SystemObject count, used for JCE for loop
     * @return number of SystemObject records
     */    
    public int countSystemPatient() {
        return mSystemPatients.length;
    }
    
    /**
     * Get SystemObject count
     * @return number of SystemObject records
     */
    public int getSystemPatientCount() {
        return countSystemPatient();
    }

    /**
     * return Collection of SystemObjectBean of the EnterpriseObject object
     *
     * @return Collection of SystemObjectBean
     */
    public SystemObjectBean[] pGetSystemObjectBean() {
        return getSystemPatient();
    }
    
    /**
     * Return the ith SystemObjectBean of the EnterpriseObject object
     *
     * @param index index of SystemObjectBean
     * @return SystemObjectBean
     */
    public SystemObjectBean pGetSystemObjectBean(int index) {
        return getSystemPatient(index);
    }
    
    /**
     * Setter for EUID attribute of the EnterpriseObject object
     *
     * @param euid EUID
     * @exception ObjectException ObjectException
     */
    public void setEUID(String euid) throws ObjectException {
        mEUID = euid;
    }

    /**
     * Setter for status attribute of the EnterpriseObject object
     *
     * @param status String
     * @exception ObjectException ObjectException
     */
    public void setStatus(String status) throws ObjectException {
        mStatus = status;
    }

    /**
     * Setter for SystemPatient of the EnterpriseObject object
     *
     * @param systemPatients SystemPatient[]
     * @exception ObjectException ObjectException
     */
    public void setSystemPatient(SystemPatient[] systemPatients) throws ObjectException {
        mSystemPatients = systemPatients;
    }

    /**
     * Adds collection of SystemPatient to the EnterpriseObject
     * object
     *
     * @param systemPatients The collection SystemPatient[] to be added
     * @exception ObjectException ObjectException
     */
    private void pSetSystemPatients(SystemPatient[] systemPatients) throws ObjectException {
        Collection systems = new ArrayList();
        for (int i = 0; i < systemPatients.length; i++) {
            systems.add(systemPatients[i].pGetSystemObject());
        }
        meo.addSystemObjects(systems);    
        pSetSystemPatients(); 
    }

    /**
     * Removes a SystemObject from EnterpriseObject
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException ObjectException
     */
    public void removeSystemObject(String system, String lid) throws ObjectException {
        meo.removeSystemObject(system, lid);       
    }

    /**
     * Mark a SystemObject from EnterpriseObject for deletion
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException ObjectException
     */
    public void deleteSystemObject(String system, String lid) throws ObjectException {
        meo.deleteSystemObject(system, lid);  
    }

    /**
     * Removes a SystemObject from EnterpriseObject
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException ObjectException
     */
    public void removeSystemObjectBean(String system, String lid) throws ObjectException {
        removeSystemObject(system, lid);
    }

    /**
     * Mark a SystemObject from EnterpriseObject for deletion
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException ObjectException
     */
    public void deleteSystemObjectBean(String system, String lid) throws ObjectException {
        deleteSystemObject(system, lid);
    }
    
    public EnterpriseObject pGetEnterpriseObject() {
        return meo;
    }
    
    /**
     * update SystemObject in an EnterpriseObject
     *
     * @param eo EnterpriseObject
     * @exception ObjectException ObjectException
     * @exception SystemObjectException SystemObjectException
     */
    public EnterpriseObject pUpdateEnterpriseObject(EnterpriseObject eo) throws ObjectException, SystemObjectException {
        UpdateHelper helper = new UpdateHelper();
        for (int i = 0; i < mSystemPatients.length; i++) {
            SystemObject so = mSystemPatients[i].pGetSystemObject();
            if(null!=eo.getSystemObject(so.getSystemCode(), so.getLID())){
                helper.updateSO(so, eo, true, true);
            }else{
                eo.addSystemObject(so);
            }
        }
        return eo;
    }

    private void pSetSystemPatients() throws ObjectException {
        Collection systems = meo.getSystemObjects();
        int size = systems.size();
        mSystemPatients = new SystemPatient[size];
        Iterator iterator = systems.iterator();
        for (int i = 0; iterator.hasNext(); i++  ) {
            SystemObject so = (SystemObject) iterator.next();
            mSystemPatients[i] = new SystemPatient(so);
        }
    }
     
}
