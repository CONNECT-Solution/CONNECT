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
import com.sun.mdm.index.objects.*;
import com.sun.mdm.index.objects.exception.*;
import java.util.Date;
import com.sun.mdm.index.webservice.SystemObjectBean;
import com.sun.mdm.index.webservice.ObjectBean;
import com.sun.mdm.index.webservice.ClearFieldObject;
import javax.xml.bind.annotation.XmlRootElement;

public class SystemPatient implements SystemObjectBean{
    private static final String DEFAULT_CLEAR_FIELD_INDICATOR = "\"\"";
    public static final int version = 1;
    /**
     * active status
     */
    public static final String STATUS_ACTIVE = "active";
    /**
     * inactive status
     */
    public static final String STATUS_INACTIVE = "inactive";
    /**
     * merged status
     */
    public static final String STATUS_MERGED = "merged";

    private SystemObject mSystemObject;
    private PatientBean mPatient;
    private ClearFieldObject mClearFieldObj;

    /**
     * No argument constructor.
     */ 
    public SystemPatient() throws ObjectException {
       PatientObject aPatient = new PatientObject();
       mClearFieldObj = new ClearFieldObject(DEFAULT_CLEAR_FIELD_INDICATOR);
       mSystemObject = new SystemObject();       
       mSystemObject.setStatus("active");
       mSystemObject.setChildType("Patient"); 
       mSystemObject.setObject(aPatient);
       mPatient = new PatientBean(aPatient, mClearFieldObj);
    }
    
    /**
     * Creates a new SystemPatient instance from a SystemObject.
     */
    protected SystemPatient(SystemObject so) throws ObjectException {
      mSystemObject = so; 
      mClearFieldObj = new ClearFieldObject(DEFAULT_CLEAR_FIELD_INDICATOR);
      mPatient = new PatientBean((PatientObject)so.getObject(), mClearFieldObj);
    }

    /**
     * Return clear field indicator
     * @return Object clear field indicator
     */    
    public Object getClearFieldIndicator() {
       return mClearFieldObj.getClearFieldIndicator();
    }
 
    /**
     * Getter for local ID
     * @exception ObjectException object exception
     * @return String lid
     */
    public String getLocalId() throws ObjectException {
       return mSystemObject.getLID(); 
    }

    /**
     * Setter for SystemCode
     * @exception ObjectException object exception
     * @return String system code
     */
    public String getSystemCode() throws ObjectException {
       return mSystemObject.getSystemCode();
    }

    /**
     * Getter for Patient
     * @return PatientBean 
     */
    public PatientBean getPatient() {
       return mPatient;
    }

    /**
     * Getter for ObjectBean
     * @return ObjectBean 
     */
    public ObjectBean pGetObjectBean() {
       return getPatient();
    } 
    
    /**
     * Getter for status
     * @return status 
     */
    public String getStatus() throws ObjectException {
       return mSystemObject.getStatus(); 
    }

    /**
     * Return CreateDateTime
     * @exception ObjectException object exception
     * @return DAte create date time
     */
    public Date getCreateDateTime() throws ObjectException {
        return mSystemObject.getCreateDateTime();
    }

    /**
     * Return Create Function
     * @exception ObjectException object exception
     * @return String create function
     */
    public String getCreateFunction() throws ObjectException {
     return mSystemObject.getCreateFunction();
    }

    /**
     * Return Create User
     * @exception ObjectException object exception
     * @return String create user
     */
    public String getCreateUser() throws ObjectException {
        return mSystemObject.getCreateUser();
    }

    /**
     * Return Update User
     * @exception ObjectException object exception
     * @return String update user
     */
    public String getUpdateUser() throws ObjectException {
        return mSystemObject.getUpdateUser();
    }

    /**
     * Setter for clearFieldIndicator
     * @param value clear field indicator
     */    
    public void setClearFieldIndicator(Object value) {
       mClearFieldObj.setClearFieldIndicator(value);
    }
     
    /**
     * Setter for LID attribute of the SystemObject object
     * @param lid LID
     * @exception ObjectException object exception
     */
    public void setLocalId(String lid) throws ObjectException {
       mSystemObject.setLID(lid);
    }

    /**
     * Setter for SystemCode attribute of the SystemObject object    
     * @param system system code
     * @exception ObjectException object exception
     * @todo Document: 
     */
    public void setSystemCode(String system) throws ObjectException {
       mSystemObject.setSystemCode(system);
    }

    /**
     * Setter for Status attribute of the SystemObject object
     * @param status status
     * @exception ObjectException object exception
     */
    public void setStatus(String status) throws ObjectException {
       mSystemObject.setStatus(status);
    }

    /**
     * Setter for CreateDateTime attribute of the SystemObject object
     * @param createdatetime create date time
     * @exception ObjectException object exception
     */
    public void setCreateDateTime(Date createdatetime)
        throws ObjectException {
        mSystemObject.setCreateDateTime(createdatetime);
        mSystemObject.setUpdateDateTime(createdatetime);
    }

    /**
     * Setter for CreateFunction attribute of the SystemObject object
     * @param createfunction create function
     * @exception ObjectException object exception
     */
    public void setCreateFunction(String createfunction)
        throws ObjectException {
       mSystemObject.setCreateFunction(createfunction);
    }

    /**
     * Setter for CreateUser attribute of the SystemObject object
     * @param createuser create user
     * @exception ObjectException object exception
     */
    public void setCreateUser(String createuser) throws ObjectException {
        mSystemObject.setCreateUser(createuser);
    }

    /**
     * Setter for Patient of the SystemObject object
     * @param aPatient PatientBean
     * @exception ObjectException object exception
     */
    public void setPatient(PatientBean aPatient) throws ObjectException {
      mPatient = aPatient;
      mSystemObject.setObject(aPatient.pGetObject());
    }

    /**
     * Setter for UpdateUser attribute of the SystemObject object    
     * @param updateuser update user
     * @exception ObjectException object exception
     */
    public void setUpdateUser(String updateuser) throws ObjectException {
      mSystemObject.setUpdateUser(updateuser);
    }
    
    /**
     * Return SystemObject     
     * @return SystemObject
     */
    public SystemObject pGetSystemObject() {
      return mSystemObject;
    }
    
}
