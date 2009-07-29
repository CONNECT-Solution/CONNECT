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
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.objects.*;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

public final class PatientBean implements ObjectBean
{
    public static final int version = 1;
    
    private PatientObject mPatientObject; 
    private ClearFieldObject mClearFieldObj;
    private final Logger mLogger = LogUtil.getLogger(this.getClass().getName());


    private ArrayList mAlias = new ArrayList();
    private ArrayList mAddress = new ArrayList();
    private ArrayList mPhone = new ArrayList();

    /**
     * No argument constructor.
     */ 
    public PatientBean() throws ObjectException
    { 
       mPatientObject = new PatientObject();
    }
    
    /**
     * Creates a new PatientBean instance from a PatientObject.
     */ 
    protected PatientBean(PatientObject aPatientObject) throws ObjectException
    { 
       mPatientObject = aPatientObject;
       int size = 0;
       Iterator iterator = null;
       Collection children = null;
       iterator = null;
       children = mPatientObject.pGetChildren("Alias");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              AliasObject oNode = (AliasObject)iterator.next();
              AliasBean aAlias = new AliasBean(oNode, mClearFieldObj);            
              mAlias.add(aAlias);          
         }
   
       iterator = null;
       children = mPatientObject.pGetChildren("Address");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              AddressObject oNode = (AddressObject)iterator.next();
              AddressBean aAddress = new AddressBean(oNode, mClearFieldObj);            
              mAddress.add(aAddress);          
         }
   
       iterator = null;
       children = mPatientObject.pGetChildren("Phone");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              PhoneObject oNode = (PhoneObject)iterator.next();
              PhoneBean aPhone = new PhoneBean(oNode, mClearFieldObj);            
              mPhone.add(aPhone);          
         }
   
     }
    /**
     * Creates a new PatientBean instance from a PatientObject.
     * and a ClearFieldObject
     */
    protected PatientBean(PatientObject aPatientObject,
       ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mPatientObject = aPatientObject;
       mClearFieldObj = clearFieldObj;
       int size = 0;
       Iterator iterator = null;
       Collection children = null;
       iterator = null;
       children = mPatientObject.pGetChildren("Alias");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              AliasObject oNode = (AliasObject)iterator.next();
              AliasBean aAlias = new AliasBean(oNode, mClearFieldObj);            
              mAlias.add(aAlias);          
         }
   
       iterator = null;
       children = mPatientObject.pGetChildren("Address");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              AddressObject oNode = (AddressObject)iterator.next();
              AddressBean aAddress = new AddressBean(oNode, mClearFieldObj);            
              mAddress.add(aAddress);          
         }
   
       iterator = null;
       children = mPatientObject.pGetChildren("Phone");
       if (children != null) {
           size = children.size();
           iterator = children.iterator();
       }  
                               
       for (int i = 0; iterator != null && iterator.hasNext() ; i++) {
              PhoneObject oNode = (PhoneObject)iterator.next();
              PhoneBean aPhone = new PhoneBean(oNode, mClearFieldObj);            
              mPhone.add(aPhone);          
         }
   
     }



    /**
     * Getter for PatientId
     * @return a string value of PatientId
     */     
    public String getPatientId() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("PatientId");
            Object value = mPatientObject.getValue("PatientId");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PersonCatCode
     * @return a string value of PersonCatCode
     */     
    public String getPersonCatCode() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("PersonCatCode");
            Object value = mPatientObject.getValue("PersonCatCode");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for FirstName
     * @return a string value of FirstName
     */     
    public String getFirstName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("FirstName");
            Object value = mPatientObject.getValue("FirstName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for FirstName_Std
     * @return a string value of FirstName_Std
     */     
    public String getFirstName_Std() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("FirstName_Std");
            Object value = mPatientObject.getValue("FirstName_Std");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for FirstName_Phon
     * @return a string value of FirstName_Phon
     */     
    public String getFirstName_Phon() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("FirstName_Phon");
            Object value = mPatientObject.getValue("FirstName_Phon");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for MiddleName
     * @return a string value of MiddleName
     */     
    public String getMiddleName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("MiddleName");
            Object value = mPatientObject.getValue("MiddleName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for LastName
     * @return a string value of LastName
     */     
    public String getLastName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("LastName");
            Object value = mPatientObject.getValue("LastName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for LastName_Std
     * @return a string value of LastName_Std
     */     
    public String getLastName_Std() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("LastName_Std");
            Object value = mPatientObject.getValue("LastName_Std");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for LastName_Phon
     * @return a string value of LastName_Phon
     */     
    public String getLastName_Phon() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("LastName_Phon");
            Object value = mPatientObject.getValue("LastName_Phon");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Suffix
     * @return a string value of Suffix
     */     
    public String getSuffix() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Suffix");
            Object value = mPatientObject.getValue("Suffix");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Title
     * @return a string value of Title
     */     
    public String getTitle() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Title");
            Object value = mPatientObject.getValue("Title");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for SSN
     * @return a string value of SSN
     */     
    public String getSSN() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("SSN");
            Object value = mPatientObject.getValue("SSN");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for DOB
     * @return a string value of DOB
     */     
    public String getDOB() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("DOB");
            Object value = mPatientObject.getValue("DOB");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Death
     * @return a string value of Death
     */     
    public String getDeath() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Death");
            Object value = mPatientObject.getValue("Death");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Gender
     * @return a string value of Gender
     */     
    public String getGender() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Gender");
            Object value = mPatientObject.getValue("Gender");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for MStatus
     * @return a string value of MStatus
     */     
    public String getMStatus() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("MStatus");
            Object value = mPatientObject.getValue("MStatus");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Race
     * @return a string value of Race
     */     
    public String getRace() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Race");
            Object value = mPatientObject.getValue("Race");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Ethnic
     * @return a string value of Ethnic
     */     
    public String getEthnic() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Ethnic");
            Object value = mPatientObject.getValue("Ethnic");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Religion
     * @return a string value of Religion
     */     
    public String getReligion() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Religion");
            Object value = mPatientObject.getValue("Religion");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Language
     * @return a string value of Language
     */     
    public String getLanguage() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Language");
            Object value = mPatientObject.getValue("Language");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for SpouseName
     * @return a string value of SpouseName
     */     
    public String getSpouseName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("SpouseName");
            Object value = mPatientObject.getValue("SpouseName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for MotherName
     * @return a string value of MotherName
     */     
    public String getMotherName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("MotherName");
            Object value = mPatientObject.getValue("MotherName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for MotherMN
     * @return a string value of MotherMN
     */     
    public String getMotherMN() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("MotherMN");
            Object value = mPatientObject.getValue("MotherMN");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for FatherName
     * @return a string value of FatherName
     */     
    public String getFatherName() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("FatherName");
            Object value = mPatientObject.getValue("FatherName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Maiden
     * @return a string value of Maiden
     */     
    public String getMaiden() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Maiden");
            Object value = mPatientObject.getValue("Maiden");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PobCity
     * @return a string value of PobCity
     */     
    public String getPobCity() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("PobCity");
            Object value = mPatientObject.getValue("PobCity");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PobState
     * @return a string value of PobState
     */     
    public String getPobState() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("PobState");
            Object value = mPatientObject.getValue("PobState");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PobCountry
     * @return a string value of PobCountry
     */     
    public String getPobCountry() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("PobCountry");
            Object value = mPatientObject.getValue("PobCountry");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for VIPFlag
     * @return a string value of VIPFlag
     */     
    public String getVIPFlag() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("VIPFlag");
            Object value = mPatientObject.getValue("VIPFlag");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for VetStatus
     * @return a string value of VetStatus
     */     
    public String getVetStatus() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("VetStatus");
            Object value = mPatientObject.getValue("VetStatus");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Status
     * @return a string value of Status
     */     
    public String getStatus() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Status");
            Object value = mPatientObject.getValue("Status");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for DriverLicense
     * @return a string value of DriverLicense
     */     
    public String getDriverLicense() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("DriverLicense");
            Object value = mPatientObject.getValue("DriverLicense");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for DriverLicenseSt
     * @return a string value of DriverLicenseSt
     */     
    public String getDriverLicenseSt() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("DriverLicenseSt");
            Object value = mPatientObject.getValue("DriverLicenseSt");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Dod
     * @return a string value of Dod
     */     
    public String getDod() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Dod");
            Object value = mPatientObject.getValue("Dod");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for DeathCertificate
     * @return a string value of DeathCertificate
     */     
    public String getDeathCertificate() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("DeathCertificate");
            Object value = mPatientObject.getValue("DeathCertificate");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Nationality
     * @return a string value of Nationality
     */     
    public String getNationality() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Nationality");
            Object value = mPatientObject.getValue("Nationality");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Citizenship
     * @return a string value of Citizenship
     */     
    public String getCitizenship() throws ObjectException
    {
        try
        {
            int type = mPatientObject.pGetType("Citizenship");
            Object value = mPatientObject.getValue("Citizenship");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Setter for PatientId
     * @param a string value of PatientId
     */ 
    public void setPatientId(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("PatientId")) {
               mPatientObject.clearField("PatientId");
            } else {
               int type = mPatientObject.pGetType("PatientId");
               Object val = strToObj(value, type, "PatientId");
          
               mPatientObject.setValue("PatientId", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PersonCatCode
     * @param a string value of PersonCatCode
     */ 
    public void setPersonCatCode(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("PersonCatCode")) {
               mPatientObject.clearField("PersonCatCode");
            } else {
               int type = mPatientObject.pGetType("PersonCatCode");
               Object val = strToObj(value, type, "PersonCatCode");
          
               mPatientObject.setValue("PersonCatCode", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for FirstName
     * @param a string value of FirstName
     */ 
    public void setFirstName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("FirstName")) {
               mPatientObject.clearField("FirstName");
            } else {
               int type = mPatientObject.pGetType("FirstName");
               Object val = strToObj(value, type, "FirstName");
          
               mPatientObject.setValue("FirstName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for FirstName_Std
     * @param a string value of FirstName_Std
     */ 
    public void setFirstName_Std(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("FirstName_Std")) {
               mPatientObject.clearField("FirstName_Std");
            } else {
               int type = mPatientObject.pGetType("FirstName_Std");
               Object val = strToObj(value, type, "FirstName_Std");
          
               mPatientObject.setValue("FirstName_Std", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for FirstName_Phon
     * @param a string value of FirstName_Phon
     */ 
    public void setFirstName_Phon(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("FirstName_Phon")) {
               mPatientObject.clearField("FirstName_Phon");
            } else {
               int type = mPatientObject.pGetType("FirstName_Phon");
               Object val = strToObj(value, type, "FirstName_Phon");
          
               mPatientObject.setValue("FirstName_Phon", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for MiddleName
     * @param a string value of MiddleName
     */ 
    public void setMiddleName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("MiddleName")) {
               mPatientObject.clearField("MiddleName");
            } else {
               int type = mPatientObject.pGetType("MiddleName");
               Object val = strToObj(value, type, "MiddleName");
          
               mPatientObject.setValue("MiddleName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for LastName
     * @param a string value of LastName
     */ 
    public void setLastName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("LastName")) {
               mPatientObject.clearField("LastName");
            } else {
               int type = mPatientObject.pGetType("LastName");
               Object val = strToObj(value, type, "LastName");
          
               mPatientObject.setValue("LastName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for LastName_Std
     * @param a string value of LastName_Std
     */ 
    public void setLastName_Std(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("LastName_Std")) {
               mPatientObject.clearField("LastName_Std");
            } else {
               int type = mPatientObject.pGetType("LastName_Std");
               Object val = strToObj(value, type, "LastName_Std");
          
               mPatientObject.setValue("LastName_Std", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for LastName_Phon
     * @param a string value of LastName_Phon
     */ 
    public void setLastName_Phon(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("LastName_Phon")) {
               mPatientObject.clearField("LastName_Phon");
            } else {
               int type = mPatientObject.pGetType("LastName_Phon");
               Object val = strToObj(value, type, "LastName_Phon");
          
               mPatientObject.setValue("LastName_Phon", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Suffix
     * @param a string value of Suffix
     */ 
    public void setSuffix(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Suffix")) {
               mPatientObject.clearField("Suffix");
            } else {
               int type = mPatientObject.pGetType("Suffix");
               Object val = strToObj(value, type, "Suffix");
          
               mPatientObject.setValue("Suffix", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Title
     * @param a string value of Title
     */ 
    public void setTitle(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Title")) {
               mPatientObject.clearField("Title");
            } else {
               int type = mPatientObject.pGetType("Title");
               Object val = strToObj(value, type, "Title");
          
               mPatientObject.setValue("Title", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for SSN
     * @param a string value of SSN
     */ 
    public void setSSN(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("SSN")) {
               mPatientObject.clearField("SSN");
            } else {
               int type = mPatientObject.pGetType("SSN");
               Object val = strToObj(value, type, "SSN");
          
               mPatientObject.setValue("SSN", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for DOB
     * @param a string value of DOB
     */ 
    public void setDOB(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("DOB")) {
               mPatientObject.clearField("DOB");
            } else {
               int type = mPatientObject.pGetType("DOB");
               Object val = strToObj(value, type, "DOB");
          
               mPatientObject.setValue("DOB", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Death
     * @param a string value of Death
     */ 
    public void setDeath(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Death")) {
               mPatientObject.clearField("Death");
            } else {
               int type = mPatientObject.pGetType("Death");
               Object val = strToObj(value, type, "Death");
          
               mPatientObject.setValue("Death", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Gender
     * @param a string value of Gender
     */ 
    public void setGender(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Gender")) {
               mPatientObject.clearField("Gender");
            } else {
               int type = mPatientObject.pGetType("Gender");
               Object val = strToObj(value, type, "Gender");
          
               mPatientObject.setValue("Gender", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for MStatus
     * @param a string value of MStatus
     */ 
    public void setMStatus(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("MStatus")) {
               mPatientObject.clearField("MStatus");
            } else {
               int type = mPatientObject.pGetType("MStatus");
               Object val = strToObj(value, type, "MStatus");
          
               mPatientObject.setValue("MStatus", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Race
     * @param a string value of Race
     */ 
    public void setRace(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Race")) {
               mPatientObject.clearField("Race");
            } else {
               int type = mPatientObject.pGetType("Race");
               Object val = strToObj(value, type, "Race");
          
               mPatientObject.setValue("Race", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Ethnic
     * @param a string value of Ethnic
     */ 
    public void setEthnic(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Ethnic")) {
               mPatientObject.clearField("Ethnic");
            } else {
               int type = mPatientObject.pGetType("Ethnic");
               Object val = strToObj(value, type, "Ethnic");
          
               mPatientObject.setValue("Ethnic", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Religion
     * @param a string value of Religion
     */ 
    public void setReligion(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Religion")) {
               mPatientObject.clearField("Religion");
            } else {
               int type = mPatientObject.pGetType("Religion");
               Object val = strToObj(value, type, "Religion");
          
               mPatientObject.setValue("Religion", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Language
     * @param a string value of Language
     */ 
    public void setLanguage(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Language")) {
               mPatientObject.clearField("Language");
            } else {
               int type = mPatientObject.pGetType("Language");
               Object val = strToObj(value, type, "Language");
          
               mPatientObject.setValue("Language", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for SpouseName
     * @param a string value of SpouseName
     */ 
    public void setSpouseName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("SpouseName")) {
               mPatientObject.clearField("SpouseName");
            } else {
               int type = mPatientObject.pGetType("SpouseName");
               Object val = strToObj(value, type, "SpouseName");
          
               mPatientObject.setValue("SpouseName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for MotherName
     * @param a string value of MotherName
     */ 
    public void setMotherName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("MotherName")) {
               mPatientObject.clearField("MotherName");
            } else {
               int type = mPatientObject.pGetType("MotherName");
               Object val = strToObj(value, type, "MotherName");
          
               mPatientObject.setValue("MotherName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for MotherMN
     * @param a string value of MotherMN
     */ 
    public void setMotherMN(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("MotherMN")) {
               mPatientObject.clearField("MotherMN");
            } else {
               int type = mPatientObject.pGetType("MotherMN");
               Object val = strToObj(value, type, "MotherMN");
          
               mPatientObject.setValue("MotherMN", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for FatherName
     * @param a string value of FatherName
     */ 
    public void setFatherName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("FatherName")) {
               mPatientObject.clearField("FatherName");
            } else {
               int type = mPatientObject.pGetType("FatherName");
               Object val = strToObj(value, type, "FatherName");
          
               mPatientObject.setValue("FatherName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Maiden
     * @param a string value of Maiden
     */ 
    public void setMaiden(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Maiden")) {
               mPatientObject.clearField("Maiden");
            } else {
               int type = mPatientObject.pGetType("Maiden");
               Object val = strToObj(value, type, "Maiden");
          
               mPatientObject.setValue("Maiden", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PobCity
     * @param a string value of PobCity
     */ 
    public void setPobCity(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("PobCity")) {
               mPatientObject.clearField("PobCity");
            } else {
               int type = mPatientObject.pGetType("PobCity");
               Object val = strToObj(value, type, "PobCity");
          
               mPatientObject.setValue("PobCity", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PobState
     * @param a string value of PobState
     */ 
    public void setPobState(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("PobState")) {
               mPatientObject.clearField("PobState");
            } else {
               int type = mPatientObject.pGetType("PobState");
               Object val = strToObj(value, type, "PobState");
          
               mPatientObject.setValue("PobState", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PobCountry
     * @param a string value of PobCountry
     */ 
    public void setPobCountry(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("PobCountry")) {
               mPatientObject.clearField("PobCountry");
            } else {
               int type = mPatientObject.pGetType("PobCountry");
               Object val = strToObj(value, type, "PobCountry");
          
               mPatientObject.setValue("PobCountry", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for VIPFlag
     * @param a string value of VIPFlag
     */ 
    public void setVIPFlag(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("VIPFlag")) {
               mPatientObject.clearField("VIPFlag");
            } else {
               int type = mPatientObject.pGetType("VIPFlag");
               Object val = strToObj(value, type, "VIPFlag");
          
               mPatientObject.setValue("VIPFlag", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for VetStatus
     * @param a string value of VetStatus
     */ 
    public void setVetStatus(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("VetStatus")) {
               mPatientObject.clearField("VetStatus");
            } else {
               int type = mPatientObject.pGetType("VetStatus");
               Object val = strToObj(value, type, "VetStatus");
          
               mPatientObject.setValue("VetStatus", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Status
     * @param a string value of Status
     */ 
    public void setStatus(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Status")) {
               mPatientObject.clearField("Status");
            } else {
               int type = mPatientObject.pGetType("Status");
               Object val = strToObj(value, type, "Status");
          
               mPatientObject.setValue("Status", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for DriverLicense
     * @param a string value of DriverLicense
     */ 
    public void setDriverLicense(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("DriverLicense")) {
               mPatientObject.clearField("DriverLicense");
            } else {
               int type = mPatientObject.pGetType("DriverLicense");
               Object val = strToObj(value, type, "DriverLicense");
          
               mPatientObject.setValue("DriverLicense", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for DriverLicenseSt
     * @param a string value of DriverLicenseSt
     */ 
    public void setDriverLicenseSt(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("DriverLicenseSt")) {
               mPatientObject.clearField("DriverLicenseSt");
            } else {
               int type = mPatientObject.pGetType("DriverLicenseSt");
               Object val = strToObj(value, type, "DriverLicenseSt");
          
               mPatientObject.setValue("DriverLicenseSt", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Dod
     * @param a string value of Dod
     */ 
    public void setDod(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Dod")) {
               mPatientObject.clearField("Dod");
            } else {
               int type = mPatientObject.pGetType("Dod");
               Object val = strToObj(value, type, "Dod");
          
               mPatientObject.setValue("Dod", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for DeathCertificate
     * @param a string value of DeathCertificate
     */ 
    public void setDeathCertificate(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("DeathCertificate")) {
               mPatientObject.clearField("DeathCertificate");
            } else {
               int type = mPatientObject.pGetType("DeathCertificate");
               Object val = strToObj(value, type, "DeathCertificate");
          
               mPatientObject.setValue("DeathCertificate", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Nationality
     * @param a string value of Nationality
     */ 
    public void setNationality(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Nationality")) {
               mPatientObject.clearField("Nationality");
            } else {
               int type = mPatientObject.pGetType("Nationality");
               Object val = strToObj(value, type, "Nationality");
          
               mPatientObject.setValue("Nationality", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Citizenship
     * @param a string value of Citizenship
     */ 
    public void setCitizenship(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPatientObject.isNullable("Citizenship")) {
               mPatientObject.clearField("Citizenship");
            } else {
               int type = mPatientObject.pGetType("Citizenship");
               Object val = strToObj(value, type, "Citizenship");
          
               mPatientObject.setValue("Citizenship", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
   
            
    /**
     * Getter for Alias
     * @return a collection of AliasBean
     */ 
    public  AliasBean[] getAlias()
    {       
        int length = mAlias.size();
        AliasBean[] arrChildren = new AliasBean[length];
        return (AliasBean[] )mAlias.toArray(arrChildren);           
    }

    /**
     * Return the ith AliasBean
     * @param index of AliasBean
     * @return AliasBean
     */
    public  AliasBean getAlias(int i) throws ObjectException
    {    
      try {
         Collection children = mPatientObject.pGetChildren("Alias");
         int size = 0;
         if (children != null) {
           size = children.size();
         }  
     
         if (size <= i) {                     
            for (int j=size; j < i+1; j++) {
              AliasBean aAlias = new AliasBean(mClearFieldObj);
              ObjectNode oNode = aAlias.getObjectNode();
              mPatientObject.addChild(oNode);
              mAlias.add(aAlias);
                              
            }
         }
         return (AliasBean)mAlias.get(i); 
       } catch (ObjectException ex) {
            throw ex;
       }
    }

    /**
     * Return the size of Alias
     * @return size of Alias
     */
    public int countAlias() {
      return mAlias.size();
    }
    
    /**
     * Delete Alias
     */
    public void deleteAlias(String aliasId) 
       throws ObjectException{
       mPatientObject.deleteChild("Alias", aliasId);
    }

     
    /**
     * Getter for Address
     * @return a collection of AddressBean
     */ 
    public  AddressBean[] getAddress()
    {       
        int length = mAddress.size();
        AddressBean[] arrChildren = new AddressBean[length];
        return (AddressBean[] )mAddress.toArray(arrChildren);           
    }

    /**
     * Return the ith AddressBean
     * @param index of AddressBean
     * @return AddressBean
     */
    public  AddressBean getAddress(int i) throws ObjectException
    {    
      try {
         Collection children = mPatientObject.pGetChildren("Address");
         int size = 0;
         if (children != null) {
           size = children.size();
         }  
     
         if (size <= i) {                     
            for (int j=size; j < i+1; j++) {
              AddressBean aAddress = new AddressBean(mClearFieldObj);
              ObjectNode oNode = aAddress.getObjectNode();
              mPatientObject.addChild(oNode);
              mAddress.add(aAddress);
                              
            }
         }
         return (AddressBean)mAddress.get(i); 
       } catch (ObjectException ex) {
            throw ex;
       }
    }

    /**
     * Return the size of Address
     * @return size of Address
     */
    public int countAddress() {
      return mAddress.size();
    }
    
    /**
     * Delete Address
     */
    public void deleteAddress(String addressId) 
       throws ObjectException{
       mPatientObject.deleteChild("Address", addressId);
    }

     
    /**
     * Getter for Phone
     * @return a collection of PhoneBean
     */ 
    public  PhoneBean[] getPhone()
    {       
        int length = mPhone.size();
        PhoneBean[] arrChildren = new PhoneBean[length];
        return (PhoneBean[] )mPhone.toArray(arrChildren);           
    }

    /**
     * Return the ith PhoneBean
     * @param index of PhoneBean
     * @return PhoneBean
     */
    public  PhoneBean getPhone(int i) throws ObjectException
    {    
      try {
         Collection children = mPatientObject.pGetChildren("Phone");
         int size = 0;
         if (children != null) {
           size = children.size();
         }  
     
         if (size <= i) {                     
            for (int j=size; j < i+1; j++) {
              PhoneBean aPhone = new PhoneBean(mClearFieldObj);
              ObjectNode oNode = aPhone.getObjectNode();
              mPatientObject.addChild(oNode);
              mPhone.add(aPhone);
                              
            }
         }
         return (PhoneBean)mPhone.get(i); 
       } catch (ObjectException ex) {
            throw ex;
       }
    }

    /**
     * Return the size of Phone
     * @return size of Phone
     */
    public int countPhone() {
      return mPhone.size();
    }
    
    /**
     * Delete Phone
     */
    public void deletePhone(String phoneId) 
       throws ObjectException{
       mPatientObject.deleteChild("Phone", phoneId);
    }

     

    /**
     * Setter for Alias
     * @param index of Alias
     * @param AliasBean
     */ 
    public void setAlias(int index, AliasBean alias)
    {
        try
        {
            mPatientObject.addChild(alias.getObjectNode());
            mAlias.add(index, alias);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Setter for Alias
     * @param a collection of AliasBean
     */ 
    public void setAlias(AliasBean[] aliass)
    {
        try
        {
           for (int i = 0; i < aliass.length; i++) {
              mPatientObject.addChild(aliass[i].getObjectNode());
              mAlias.add(aliass);
           }
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    /**
     * Setter for Address
     * @param index of Address
     * @param AddressBean
     */ 
    public void setAddress(int index, AddressBean address)
    {
        try
        {
            mPatientObject.addChild(address.getObjectNode());
            mAddress.add(index, address);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Setter for Address
     * @param a collection of AddressBean
     */ 
    public void setAddress(AddressBean[] addresss)
    {
        try
        {
           for (int i = 0; i < addresss.length; i++) {
              mPatientObject.addChild(addresss[i].getObjectNode());
              mAddress.add(addresss);
           }
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    /**
     * Setter for Phone
     * @param index of Phone
     * @param PhoneBean
     */ 
    public void setPhone(int index, PhoneBean phone)
    {
        try
        {
            mPatientObject.addChild(phone.getObjectNode());
            mPhone.add(index, phone);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Setter for Phone
     * @param a collection of PhoneBean
     */ 
    public void setPhone(PhoneBean[] phones)
    {
        try
        {
           for (int i = 0; i < phones.length; i++) {
              mPatientObject.addChild(phones[i].getObjectNode());
              mPhone.add(phones);
           }
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }

    /** 
     * Return PatientObject
     * @return PatientObject ObjectNode
     */  
    public ObjectNode pGetObject() {
        return mPatientObject;
    }

    /** 
     * Return for all children nodes
     * @return collection of children nodes
     */            
    public Collection pGetChildren() {
         ArrayList allChildren = new ArrayList();

         if (mAlias.size() > 0) {
             allChildren.add(mAlias);
         }
         if (mAddress.size() > 0) {
             allChildren.add(mAddress);
         }
         if (mPhone.size() > 0) {
             allChildren.add(mPhone);
         }
         return allChildren;
    }
    
    /** 
     * Getter for children of a specified type
     * @param type Type of children to retrieve
     * @return Arraylist of children of specified type
     */
    public Collection pGetChildren(String type) {
        return mPatientObject.pGetChildren(type);
    }

    /** 
     * Getter for child types
     * @return Arraylist of child types
     */
    public ArrayList pGetChildTypes() {
        return mPatientObject.getChildTags();
    }    

    /**
     * Count of all children
     * @return number of children
     */
    public int countChildren() {
        return ((ArrayList) pGetChildren()).size();
    }

    /**
     * Count of children of specified type
     * @param type of children to count
     * @return number of children of specified type
     */
    public int countChildren(String type) {
        return ((ArrayList) pGetChildren(type)).size();
    }

    /**
     * Return parent which is SystemObject  
     */  
    public SystemObject getParentSO() {
        ObjectNode obj = mPatientObject.getParent();
        
        while (obj != null) {
           if (obj instanceof SystemObject) {
              return (SystemObject) obj;
           } else {
              obj = obj.getParent();
           }
        }
        return (SystemObject) obj;
    }   
                
    static String objToString(Object value, int type) throws ObjectException {
        if (value == null) {
            return null;
        } else {
            if ( type == ObjectField.OBJECTMETA_STRING_TYPE) {
                return (String) value;
            }
            else if (type == ObjectField.OBJECTMETA_DATE_TYPE) {
               SimpleDateFormat dateFormat = new SimpleDateFormat(MetaDataService.getDateFormat());
               return dateFormat.format(value);              
            } else {
                return value.toString();
            }
        }
    }
    
    static Object strToObj(String str, int type, String fieldName) throws ObjectException {
        if (str == null || str.trim().length() == 0) {
            return null;
        } else if ( type == ObjectField.OBJECTMETA_STRING_TYPE) {
            return  str;         
        } else if (type == ObjectField.OBJECTMETA_DATE_TYPE) {
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat(MetaDataService.getDateFormat());
            Object ret = dateFormat.parse(str, pos);   
            if ( ret == null) {
               throw new ObjectException("Invalid Date format of" + fieldName + ",value:" + str);
            }           
            return ret;
        } else if (type == ObjectField.OBJECTMETA_INT_TYPE) {                
            return Integer.valueOf(str);              
        } else if (type == ObjectField.OBJECTMETA_FLOAT_TYPE) {                
            return Float.valueOf(str);              
        } else if (type == ObjectField.OBJECTMETA_LONG_TYPE) {                
            return Long.valueOf(str);             
        } else if (type == ObjectField.OBJECTMETA_BOOL_TYPE) {                
            return Boolean.valueOf(str);              
        } else if (type == ObjectField.OBJECTMETA_CHAR_TYPE) {                
            return (new Character(str.charAt(0)));                 
        } else {
            throw new ObjectException("Invalid type of" + fieldName + ",value:" + str);
        }
    }    
}
