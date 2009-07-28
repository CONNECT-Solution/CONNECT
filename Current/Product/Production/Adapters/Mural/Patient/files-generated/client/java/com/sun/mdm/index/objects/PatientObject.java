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
package com.sun.mdm.index.objects;

import java.util.*;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

public final class PatientObject extends ObjectNode
{
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    private final Logger mLogger = LogUtil.getLogger(this.getClass().getName());
    
    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add("PatientId");
        mFieldNames.add("PersonCatCode");
        mFieldNames.add("FirstName");
        mFieldNames.add("FirstName_Std");
        mFieldNames.add("FirstName_Phon");
        mFieldNames.add("MiddleName");
        mFieldNames.add("LastName");
        mFieldNames.add("LastName_Std");
        mFieldNames.add("LastName_Phon");
        mFieldNames.add("Suffix");
        mFieldNames.add("Title");
        mFieldNames.add("SSN");
        mFieldNames.add("DOB");
        mFieldNames.add("Death");
        mFieldNames.add("Gender");
        mFieldNames.add("MStatus");
        mFieldNames.add("Race");
        mFieldNames.add("Ethnic");
        mFieldNames.add("Religion");
        mFieldNames.add("Language");
        mFieldNames.add("SpouseName");
        mFieldNames.add("MotherName");
        mFieldNames.add("MotherMN");
        mFieldNames.add("FatherName");
        mFieldNames.add("Maiden");
        mFieldNames.add("PobCity");
        mFieldNames.add("PobState");
        mFieldNames.add("PobCountry");
        mFieldNames.add("VIPFlag");
        mFieldNames.add("VetStatus");
        mFieldNames.add("Status");
        mFieldNames.add("DriverLicense");
        mFieldNames.add("DriverLicenseSt");
        mFieldNames.add("Dod");
        mFieldNames.add("DeathCertificate");
        mFieldNames.add("Nationality");
        mFieldNames.add("Citizenship");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    public PatientObject() throws ObjectException
    {
        super("Patient", mFieldNames, mFieldTypes);
        mParentTag = "";
        mChildTags = new ArrayList();
        mChildTags.add("Alias");
        mChildTags.add("Address");
        mChildTags.add("Phone");
        mTag = "Patient";
        setKeyType("PatientId", false);
        setKeyType("PersonCatCode", false);
        setKeyType("FirstName", false);
        setKeyType("FirstName_Std", false);
        setKeyType("FirstName_Phon", false);
        setKeyType("MiddleName", false);
        setKeyType("LastName", false);
        setKeyType("LastName_Std", false);
        setKeyType("LastName_Phon", false);
        setKeyType("Suffix", false);
        setKeyType("Title", false);
        setKeyType("SSN", false);
        setKeyType("DOB", false);
        setKeyType("Death", false);
        setKeyType("Gender", false);
        setKeyType("MStatus", false);
        setKeyType("Race", false);
        setKeyType("Ethnic", false);
        setKeyType("Religion", false);
        setKeyType("Language", false);
        setKeyType("SpouseName", false);
        setKeyType("MotherName", false);
        setKeyType("MotherMN", false);
        setKeyType("FatherName", false);
        setKeyType("Maiden", false);
        setKeyType("PobCity", false);
        setKeyType("PobState", false);
        setKeyType("PobCountry", false);
        setKeyType("VIPFlag", false);
        setKeyType("VetStatus", false);
        setKeyType("Status", false);
        setKeyType("DriverLicense", false);
        setKeyType("DriverLicenseSt", false);
        setKeyType("Dod", false);
        setKeyType("DeathCertificate", false);
        setKeyType("Nationality", false);
        setKeyType("Citizenship", false);
        setNullable("PatientId", false);
        setNullable("PersonCatCode", true);
        setNullable("FirstName", false);
        setNullable("FirstName_Std", true);
        setNullable("FirstName_Phon", true);
        setNullable("MiddleName", true);
        setNullable("LastName", false);
        setNullable("LastName_Std", true);
        setNullable("LastName_Phon", true);
        setNullable("Suffix", true);
        setNullable("Title", true);
        setNullable("SSN", false);
        setNullable("DOB", false);
        setNullable("Death", true);
        setNullable("Gender", false);
        setNullable("MStatus", true);
        setNullable("Race", true);
        setNullable("Ethnic", true);
        setNullable("Religion", true);
        setNullable("Language", true);
        setNullable("SpouseName", true);
        setNullable("MotherName", true);
        setNullable("MotherMN", true);
        setNullable("FatherName", true);
        setNullable("Maiden", true);
        setNullable("PobCity", true);
        setNullable("PobState", true);
        setNullable("PobCountry", true);
        setNullable("VIPFlag", true);
        setNullable("VetStatus", true);
        setNullable("Status", true);
        setNullable("DriverLicense", true);
        setNullable("DriverLicenseSt", true);
        setNullable("Dod", true);
        setNullable("DeathCertificate", true);
        setNullable("Nationality", true);
        setNullable("Citizenship", true);
    }

    public String getObjectId() throws ObjectException
    {
        return (getPatientId());
    }

    public String getPatientId() throws ObjectException
    {
        try
        {
            return ((String)getValue("PatientId"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPersonCatCode() throws ObjectException
    {
        try
        {
            return ((String)getValue("PersonCatCode"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getFirstName() throws ObjectException
    {
        try
        {
            return ((String)getValue("FirstName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getFirstName_Std() throws ObjectException
    {
        try
        {
            return ((String)getValue("FirstName_Std"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getFirstName_Phon() throws ObjectException
    {
        try
        {
            return ((String)getValue("FirstName_Phon"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getMiddleName() throws ObjectException
    {
        try
        {
            return ((String)getValue("MiddleName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getLastName() throws ObjectException
    {
        try
        {
            return ((String)getValue("LastName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getLastName_Std() throws ObjectException
    {
        try
        {
            return ((String)getValue("LastName_Std"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getLastName_Phon() throws ObjectException
    {
        try
        {
            return ((String)getValue("LastName_Phon"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getSuffix() throws ObjectException
    {
        try
        {
            return ((String)getValue("Suffix"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getTitle() throws ObjectException
    {
        try
        {
            return ((String)getValue("Title"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getSSN() throws ObjectException
    {
        try
        {
            return ((String)getValue("SSN"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public Date getDOB() throws ObjectException
    {
        try
        {
            return ((Date)getValue("DOB"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getDeath() throws ObjectException
    {
        try
        {
            return ((String)getValue("Death"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getGender() throws ObjectException
    {
        try
        {
            return ((String)getValue("Gender"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getMStatus() throws ObjectException
    {
        try
        {
            return ((String)getValue("MStatus"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getRace() throws ObjectException
    {
        try
        {
            return ((String)getValue("Race"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getEthnic() throws ObjectException
    {
        try
        {
            return ((String)getValue("Ethnic"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getReligion() throws ObjectException
    {
        try
        {
            return ((String)getValue("Religion"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getLanguage() throws ObjectException
    {
        try
        {
            return ((String)getValue("Language"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getSpouseName() throws ObjectException
    {
        try
        {
            return ((String)getValue("SpouseName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getMotherName() throws ObjectException
    {
        try
        {
            return ((String)getValue("MotherName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getMotherMN() throws ObjectException
    {
        try
        {
            return ((String)getValue("MotherMN"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getFatherName() throws ObjectException
    {
        try
        {
            return ((String)getValue("FatherName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getMaiden() throws ObjectException
    {
        try
        {
            return ((String)getValue("Maiden"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPobCity() throws ObjectException
    {
        try
        {
            return ((String)getValue("PobCity"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPobState() throws ObjectException
    {
        try
        {
            return ((String)getValue("PobState"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPobCountry() throws ObjectException
    {
        try
        {
            return ((String)getValue("PobCountry"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getVIPFlag() throws ObjectException
    {
        try
        {
            return ((String)getValue("VIPFlag"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getVetStatus() throws ObjectException
    {
        try
        {
            return ((String)getValue("VetStatus"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getStatus() throws ObjectException
    {
        try
        {
            return ((String)getValue("Status"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getDriverLicense() throws ObjectException
    {
        try
        {
            return ((String)getValue("DriverLicense"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getDriverLicenseSt() throws ObjectException
    {
        try
        {
            return ((String)getValue("DriverLicenseSt"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public Date getDod() throws ObjectException
    {
        try
        {
            return ((Date)getValue("Dod"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getDeathCertificate() throws ObjectException
    {
        try
        {
            return ((String)getValue("DeathCertificate"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getNationality() throws ObjectException
    {
        try
        {
            return ((String)getValue("Nationality"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getCitizenship() throws ObjectException
    {
        try
        {
            return ((String)getValue("Citizenship"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }


    public void setObjectId(Object value) throws ObjectException
    {
        setPatientId(value);
    }

    public void setPatientId(Object value) throws ObjectException
    {
        try
        {
            setValue("PatientId", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPersonCatCode(Object value) throws ObjectException
    {
        try
        {
            setValue("PersonCatCode", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setFirstName(Object value) throws ObjectException
    {
        try
        {
            setValue("FirstName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setFirstName_Std(Object value) throws ObjectException
    {
        try
        {
            setValue("FirstName_Std", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setFirstName_Phon(Object value) throws ObjectException
    {
        try
        {
            setValue("FirstName_Phon", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setMiddleName(Object value) throws ObjectException
    {
        try
        {
            setValue("MiddleName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setLastName(Object value) throws ObjectException
    {
        try
        {
            setValue("LastName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setLastName_Std(Object value) throws ObjectException
    {
        try
        {
            setValue("LastName_Std", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setLastName_Phon(Object value) throws ObjectException
    {
        try
        {
            setValue("LastName_Phon", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setSuffix(Object value) throws ObjectException
    {
        try
        {
            setValue("Suffix", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setTitle(Object value) throws ObjectException
    {
        try
        {
            setValue("Title", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setSSN(Object value) throws ObjectException
    {
        try
        {
            setValue("SSN", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDOB(Object value) throws ObjectException
    {
        try
        {
            setValue("DOB", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDeath(Object value) throws ObjectException
    {
        try
        {
            setValue("Death", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setGender(Object value) throws ObjectException
    {
        try
        {
            setValue("Gender", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setMStatus(Object value) throws ObjectException
    {
        try
        {
            setValue("MStatus", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setRace(Object value) throws ObjectException
    {
        try
        {
            setValue("Race", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setEthnic(Object value) throws ObjectException
    {
        try
        {
            setValue("Ethnic", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setReligion(Object value) throws ObjectException
    {
        try
        {
            setValue("Religion", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setLanguage(Object value) throws ObjectException
    {
        try
        {
            setValue("Language", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setSpouseName(Object value) throws ObjectException
    {
        try
        {
            setValue("SpouseName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setMotherName(Object value) throws ObjectException
    {
        try
        {
            setValue("MotherName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setMotherMN(Object value) throws ObjectException
    {
        try
        {
            setValue("MotherMN", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setFatherName(Object value) throws ObjectException
    {
        try
        {
            setValue("FatherName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setMaiden(Object value) throws ObjectException
    {
        try
        {
            setValue("Maiden", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPobCity(Object value) throws ObjectException
    {
        try
        {
            setValue("PobCity", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPobState(Object value) throws ObjectException
    {
        try
        {
            setValue("PobState", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPobCountry(Object value) throws ObjectException
    {
        try
        {
            setValue("PobCountry", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setVIPFlag(Object value) throws ObjectException
    {
        try
        {
            setValue("VIPFlag", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setVetStatus(Object value) throws ObjectException
    {
        try
        {
            setValue("VetStatus", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setStatus(Object value) throws ObjectException
    {
        try
        {
            setValue("Status", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDriverLicense(Object value) throws ObjectException
    {
        try
        {
            setValue("DriverLicense", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDriverLicenseSt(Object value) throws ObjectException
    {
        try
        {
            setValue("DriverLicenseSt", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDod(Object value) throws ObjectException
    {
        try
        {
            setValue("Dod", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setDeathCertificate(Object value) throws ObjectException
    {
        try
        {
            setValue("DeathCertificate", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setNationality(Object value) throws ObjectException
    {
        try
        {
            setValue("Nationality", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setCitizenship(Object value) throws ObjectException
    {
        try
        {
            setValue("Citizenship", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public AttributeMetaData getMetaData()
    {
        return null;
    }
            
    public Collection getAlias()
    {
        return pGetChildren("Alias");
    }
    
    public Collection getAddress()
    {
        return pGetChildren("Address");
    }
    
    public Collection getPhone()
    {
        return pGetChildren("Phone");
    }
    
    public void addAlias(AliasObject alias)
    {
        try
        {
            addChild(alias);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    
    public void addAddress(AddressObject address)
    {
        try
        {
            addChild(address);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    
    public void addPhone(PhoneObject phone)
    {
        try
        {
            addChild(phone);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    
    public void addSecondaryObject(ObjectNode obj) throws SystemObjectException
    {
        try
        {
            addChild(obj);
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    
    public void dropSecondaryObject(ObjectNode obj) throws SystemObjectException
    {
        try
        {
            removeChild(obj.pGetTag(), obj.pGetKey());
        }
        catch (ObjectException ex)
        {
            mLogger.error(ex.getMessage(), ex);
        }
    }
    
    public Collection getSecondaryObject(String type) throws SystemObjectException
    {
        return pGetChildren(type);
    }    

    public ArrayList getChildTags()
    {
        if (null == mChildTags)
        {
            mChildTags = new ArrayList();
            mChildTags.add("Alias");
            mChildTags.add("Address");
            mChildTags.add("Phone");
        }        
        
        return mChildTags;
    }
    
    public ObjectNode copy () throws ObjectException
    {
        PatientObject ret = null;
        try
        {
            ret = new PatientObject();
            String tag = pGetTag ();
            ArrayList names = pGetFieldNames ();
            ArrayList values = pGetFieldValues ();

            for(int i=0; i<names.size (); i++)
            {
                String name = (String)names.get (i);
                ret.setValue (name, getValue(name));
                ret.setVisible (name, isVisible (name));
                ret.setSearched (name, isSearched (name));
                ret.setChanged (name, isChanged (name));
                ret.setKeyType (name, isKeyType (name));
            }
            ret.setUpdateFlag ( isUpdated());
            ret.setRemoveFlag ( isRemoved());
            ret.setAddFlag ( isAdded());
            ret.setKeyChangeFlag ( isKeyChanged() );

            ArrayList fieldUpdateLogs = null;
            if ( pGetFieldUpdateLogs()!= null ){
                fieldUpdateLogs = (ArrayList)pGetFieldUpdateLogs().clone();    
            }
            ret.setFieldUpdateLogs( fieldUpdateLogs );
            
            if (null != mParent)
            {
                ret.setParent(mParent);
            }
           
            ArrayList aKeyedChildren = getAllChildrenFromHashMap();
            if (aKeyedChildren != null) {
                for (int i = 0; i < aKeyedChildren.size(); i++) {
                    ObjectNode child = (ObjectNode) aKeyedChildren.get(i);
                    ret.addChildNoFlagSet(child.copy());
                }
            }
        }
        catch (ObjectException e)
        {
            throw e;
        }
                
        return (ObjectNode)ret;
    }
    
    public ObjectNode structCopy () throws ObjectException
    {
        PatientObject ret = null;
        try
        {
            ret = new PatientObject();
            String tag = pGetTag ();
            ArrayList names = pGetFieldNames ();

            for(int i=0; i<names.size (); i++)
            {
                String name = (String)names.get (i);
                ret.setVisible (name, isVisible (name));
                ret.setSearched (name, isSearched (name));
                ret.setKeyType (name, isKeyType (name));
            }

            ObjectKey key = pGetKey();
            if (key != null) {
                ret.setKey(key);
            }                

            if (null != mParent)
            {
                ret.setParent(mParent);
            }

            ArrayList aKeyedChildren = getAllChildrenFromHashMap();
            if (aKeyedChildren != null) {
                for (int i = 0; i < aKeyedChildren.size(); i++) {
                    ObjectNode child = (ObjectNode) aKeyedChildren.get(i);
                    ret.addChildNoFlagSet(child.structCopy());
                }
            }
        }
        catch (ObjectException e)
        {
            throw e;
        }
                
        return (ObjectNode)ret;
    }
}
