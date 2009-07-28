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

public final class PhoneBean implements ObjectBean
{
    public static final int version = 1;

    private PhoneObject mPhoneObject;
    private ClearFieldObject mClearFieldObj;
    private static java.text.SimpleDateFormat mDateFormat = new SimpleDateFormat(MetaDataService.getDateFormat());

    /**
     * Creates a new PhoneBean instance.
     * @throws  ObjectException If creation fails. 
     */ 
    public PhoneBean() throws ObjectException
    { 
       mPhoneObject = new PhoneObject();
    }
    
    /**
     * Creates a new PhoneBean instance from a ClearFieldObject.
     */ 
    public PhoneBean(ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mPhoneObject = new PhoneObject();
       mClearFieldObj = clearFieldObj;
    }

    /**
     * Creates a new PhoneBean instance from a PhoneObject.
     */
    protected PhoneBean(PhoneObject aPhoneObject) throws ObjectException
    { 
       mPhoneObject = aPhoneObject;
    }
    
    /**
     * Creates a new PhoneBean instance from 
     * a PhoneObject and a ClearFieldObject.
     */
    protected PhoneBean(PhoneObject aPhoneObject,
      ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mPhoneObject = aPhoneObject;
       mClearFieldObj = clearFieldObj;
    }
    
    /**
     * Getter for PhoneId
     * @return a string value of PhoneId
     */    
    public String getPhoneId() throws ObjectException
    {
        try
        {
            int type = mPhoneObject.pGetType("PhoneId");
            Object value = mPhoneObject.getValue("PhoneId");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PhoneType
     * @return a string value of PhoneType
     */    
    public String getPhoneType() throws ObjectException
    {
        try
        {
            int type = mPhoneObject.pGetType("PhoneType");
            Object value = mPhoneObject.getValue("PhoneType");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for Phone
     * @return a string value of Phone
     */    
    public String getPhone() throws ObjectException
    {
        try
        {
            int type = mPhoneObject.pGetType("Phone");
            Object value = mPhoneObject.getValue("Phone");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Getter for PhoneExt
     * @return a string value of PhoneExt
     */    
    public String getPhoneExt() throws ObjectException
    {
        try
        {
            int type = mPhoneObject.pGetType("PhoneExt");
            Object value = mPhoneObject.getValue("PhoneExt");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Setter for PhoneId
     * @param string value of PhoneId
     */ 
    public void setPhoneId(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPhoneObject.isNullable("PhoneId")) {
               mPhoneObject.clearField("PhoneId");
            } else {
               int type = mPhoneObject.pGetType("PhoneId");
               Object val = strToObj(value, type, "PhoneId");
          
               mPhoneObject.setValue("PhoneId", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PhoneType
     * @param string value of PhoneType
     */ 
    public void setPhoneType(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPhoneObject.isNullable("PhoneType")) {
               mPhoneObject.clearField("PhoneType");
            } else {
               int type = mPhoneObject.pGetType("PhoneType");
               Object val = strToObj(value, type, "PhoneType");
          
               mPhoneObject.setValue("PhoneType", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for Phone
     * @param string value of Phone
     */ 
    public void setPhone(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPhoneObject.isNullable("Phone")) {
               mPhoneObject.clearField("Phone");
            } else {
               int type = mPhoneObject.pGetType("Phone");
               Object val = strToObj(value, type, "Phone");
          
               mPhoneObject.setValue("Phone", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for PhoneExt
     * @param string value of PhoneExt
     */ 
    public void setPhoneExt(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mPhoneObject.isNullable("PhoneExt")) {
               mPhoneObject.clearField("PhoneExt");
            } else {
               int type = mPhoneObject.pGetType("PhoneExt");
               Object val = strToObj(value, type, "PhoneExt");
          
               mPhoneObject.setValue("PhoneExt", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    
    protected ObjectNode getObjectNode() {
        return mPhoneObject;
    }

    /** 
     * Return PhoneObject
     * @return PhoneObject
     */ 
    public ObjectNode pGetObject() {
        return mPhoneObject;
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
        ObjectNode parent = mPhoneObject.getParent();
        parent.deleteChild("Phone", mPhoneObject.pGetSuperKey()); 
    }
        
    // Find parent which is SystemObject    
    private SystemObject getParentSO() {
        ObjectNode obj = mPhoneObject.getParent();
        
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
