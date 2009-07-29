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

public final class AddressBean implements ObjectBean
{
    public static final int version = 1;

    private AddressObject mAddressObject;
    private ClearFieldObject mClearFieldObj;
    private static java.text.SimpleDateFormat mDateFormat = new SimpleDateFormat(MetaDataService.getDateFormat());

    /**
     * Creates a new AddressBean instance.
     * @throws  ObjectException If creation fails. 
     */ 
    public AddressBean() throws ObjectException
    { 
       mAddressObject = new AddressObject();
    }
    
    /**
     * Creates a new AddressBean instance from a ClearFieldObject.
     */ 
    public AddressBean(ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mAddressObject = new AddressObject();
       mClearFieldObj = clearFieldObj;
    }

    /**
     * Creates a new AddressBean instance from a AddressObject.
     */
    protected AddressBean(AddressObject aAddressObject) throws ObjectException
    { 
       mAddressObject = aAddressObject;
    }
    
    /**
     * Creates a new AddressBean instance from 
     * a AddressObject and a ClearFieldObject.
     */
    protected AddressBean(AddressObject aAddressObject,
      ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mAddressObject = aAddressObject;
       mClearFieldObj = clearFieldObj;
    }
    
    /**
     * Getter for AddressId
     * @return a string value of AddressId
     */    
    public String getAddressId() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressId");
            Object value = mAddressObject.getValue("AddressId");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressType
     * @return a string value of AddressType
     */    
    public String getAddressType() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressType");
            Object value = mAddressObject.getValue("AddressType");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1
     * @return a string value of AddressLine1
     */    
    public String getAddressLine1() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1");
            Object value = mAddressObject.getValue("AddressLine1");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1_HouseNo
     * @return a string value of AddressLine1_HouseNo
     */    
    public String getAddressLine1_HouseNo() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1_HouseNo");
            Object value = mAddressObject.getValue("AddressLine1_HouseNo");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1_StDir
     * @return a string value of AddressLine1_StDir
     */    
    public String getAddressLine1_StDir() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1_StDir");
            Object value = mAddressObject.getValue("AddressLine1_StDir");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1_StName
     * @return a string value of AddressLine1_StName
     */    
    public String getAddressLine1_StName() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1_StName");
            Object value = mAddressObject.getValue("AddressLine1_StName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1_StPhon
     * @return a string value of AddressLine1_StPhon
     */    
    public String getAddressLine1_StPhon() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1_StPhon");
            Object value = mAddressObject.getValue("AddressLine1_StPhon");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine1_StType
     * @return a string value of AddressLine1_StType
     */    
    public String getAddressLine1_StType() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine1_StType");
            Object value = mAddressObject.getValue("AddressLine1_StType");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for AddressLine2
     * @return a string value of AddressLine2
     */    
    public String getAddressLine2() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("AddressLine2");
            Object value = mAddressObject.getValue("AddressLine2");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for City
     * @return a string value of City
     */    
    public String getCity() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("City");
            Object value = mAddressObject.getValue("City");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for StateCode
     * @return a string value of StateCode
     */    
    public String getStateCode() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("StateCode");
            Object value = mAddressObject.getValue("StateCode");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PostalCode
     * @return a string value of PostalCode
     */    
    public String getPostalCode() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("PostalCode");
            Object value = mAddressObject.getValue("PostalCode");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PostalCodeExt
     * @return a string value of PostalCodeExt
     */    
    public String getPostalCodeExt() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("PostalCodeExt");
            Object value = mAddressObject.getValue("PostalCodeExt");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for County
     * @return a string value of County
     */    
    public String getCounty() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("County");
            Object value = mAddressObject.getValue("County");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for CountryCode
     * @return a string value of CountryCode
     */    
    public String getCountryCode() throws ObjectException
    {
        try
        {
            int type = mAddressObject.pGetType("CountryCode");
            Object value = mAddressObject.getValue("CountryCode");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Setter for AddressId
     * @param string value of AddressId
     */ 
    public void setAddressId(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressId")) {
               mAddressObject.clearField("AddressId");
            } else {
               int type = mAddressObject.pGetType("AddressId");
               Object val = strToObj(value, type, "AddressId");
          
               mAddressObject.setValue("AddressId", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressType
     * @param string value of AddressType
     */ 
    public void setAddressType(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressType")) {
               mAddressObject.clearField("AddressType");
            } else {
               int type = mAddressObject.pGetType("AddressType");
               Object val = strToObj(value, type, "AddressType");
          
               mAddressObject.setValue("AddressType", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1
     * @param string value of AddressLine1
     */ 
    public void setAddressLine1(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1")) {
               mAddressObject.clearField("AddressLine1");
            } else {
               int type = mAddressObject.pGetType("AddressLine1");
               Object val = strToObj(value, type, "AddressLine1");
          
               mAddressObject.setValue("AddressLine1", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1_HouseNo
     * @param string value of AddressLine1_HouseNo
     */ 
    public void setAddressLine1_HouseNo(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1_HouseNo")) {
               mAddressObject.clearField("AddressLine1_HouseNo");
            } else {
               int type = mAddressObject.pGetType("AddressLine1_HouseNo");
               Object val = strToObj(value, type, "AddressLine1_HouseNo");
          
               mAddressObject.setValue("AddressLine1_HouseNo", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1_StDir
     * @param string value of AddressLine1_StDir
     */ 
    public void setAddressLine1_StDir(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1_StDir")) {
               mAddressObject.clearField("AddressLine1_StDir");
            } else {
               int type = mAddressObject.pGetType("AddressLine1_StDir");
               Object val = strToObj(value, type, "AddressLine1_StDir");
          
               mAddressObject.setValue("AddressLine1_StDir", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1_StName
     * @param string value of AddressLine1_StName
     */ 
    public void setAddressLine1_StName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1_StName")) {
               mAddressObject.clearField("AddressLine1_StName");
            } else {
               int type = mAddressObject.pGetType("AddressLine1_StName");
               Object val = strToObj(value, type, "AddressLine1_StName");
          
               mAddressObject.setValue("AddressLine1_StName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1_StPhon
     * @param string value of AddressLine1_StPhon
     */ 
    public void setAddressLine1_StPhon(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1_StPhon")) {
               mAddressObject.clearField("AddressLine1_StPhon");
            } else {
               int type = mAddressObject.pGetType("AddressLine1_StPhon");
               Object val = strToObj(value, type, "AddressLine1_StPhon");
          
               mAddressObject.setValue("AddressLine1_StPhon", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine1_StType
     * @param string value of AddressLine1_StType
     */ 
    public void setAddressLine1_StType(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine1_StType")) {
               mAddressObject.clearField("AddressLine1_StType");
            } else {
               int type = mAddressObject.pGetType("AddressLine1_StType");
               Object val = strToObj(value, type, "AddressLine1_StType");
          
               mAddressObject.setValue("AddressLine1_StType", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for AddressLine2
     * @param string value of AddressLine2
     */ 
    public void setAddressLine2(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("AddressLine2")) {
               mAddressObject.clearField("AddressLine2");
            } else {
               int type = mAddressObject.pGetType("AddressLine2");
               Object val = strToObj(value, type, "AddressLine2");
          
               mAddressObject.setValue("AddressLine2", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for City
     * @param string value of City
     */ 
    public void setCity(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("City")) {
               mAddressObject.clearField("City");
            } else {
               int type = mAddressObject.pGetType("City");
               Object val = strToObj(value, type, "City");
          
               mAddressObject.setValue("City", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for StateCode
     * @param string value of StateCode
     */ 
    public void setStateCode(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("StateCode")) {
               mAddressObject.clearField("StateCode");
            } else {
               int type = mAddressObject.pGetType("StateCode");
               Object val = strToObj(value, type, "StateCode");
          
               mAddressObject.setValue("StateCode", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PostalCode
     * @param string value of PostalCode
     */ 
    public void setPostalCode(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("PostalCode")) {
               mAddressObject.clearField("PostalCode");
            } else {
               int type = mAddressObject.pGetType("PostalCode");
               Object val = strToObj(value, type, "PostalCode");
          
               mAddressObject.setValue("PostalCode", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PostalCodeExt
     * @param string value of PostalCodeExt
     */ 
    public void setPostalCodeExt(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("PostalCodeExt")) {
               mAddressObject.clearField("PostalCodeExt");
            } else {
               int type = mAddressObject.pGetType("PostalCodeExt");
               Object val = strToObj(value, type, "PostalCodeExt");
          
               mAddressObject.setValue("PostalCodeExt", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for County
     * @param string value of County
     */ 
    public void setCounty(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("County")) {
               mAddressObject.clearField("County");
            } else {
               int type = mAddressObject.pGetType("County");
               Object val = strToObj(value, type, "County");
          
               mAddressObject.setValue("County", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for CountryCode
     * @param string value of CountryCode
     */ 
    public void setCountryCode(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAddressObject.isNullable("CountryCode")) {
               mAddressObject.clearField("CountryCode");
            } else {
               int type = mAddressObject.pGetType("CountryCode");
               Object val = strToObj(value, type, "CountryCode");
          
               mAddressObject.setValue("CountryCode", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    
    protected ObjectNode getObjectNode() {
        return mAddressObject;
    }

    /** 
     * Return AddressObject
     * @return AddressObject
     */ 
    public ObjectNode pGetObject() {
        return mAddressObject;
    }

    /** 
     * Getter for all children nodes
     * @return null because there is no child at the leaf
     */            
    public Collection pGetChildren() {            
         return null;
    }
    
    /** 
     * Getter for children of a specified type
     * @param type Type of children to retrieve
     * @return null because there is no child at the leaf
     */
    public Collection pGetChildren(String type) {
        return null;
    }

    /** 
     * Getter for child types
     * @return null because there is no child at the leaf
     */
    public ArrayList pGetChildTypes() {
        return null;
    }    

    /**
     * Count of all children
     * @return number of children
     */
    public int countChildren() {
        int count = 0;
        return count;
    }

    /**
     * Count of children of specified type
     * @param type of children to count
     * @return number of children of specified type
     */
    public int countChildren(String type) {
        int count = 0;
        return count;
    }
    
    /**
     * Delete itself from the parent and persist
     */
    public void delete() throws ObjectException {
        ObjectNode parent = mAddressObject.getParent();
        parent.deleteChild("Address", mAddressObject.pGetSuperKey()); 
    }
        
    // Find parent which is SystemObject    
    private SystemObject getParentSO() {
        ObjectNode obj = mAddressObject.getParent();
        
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
               return mDateFormat.format(value);              
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
             Object ret = mDateFormat.parse(str, pos);   
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
