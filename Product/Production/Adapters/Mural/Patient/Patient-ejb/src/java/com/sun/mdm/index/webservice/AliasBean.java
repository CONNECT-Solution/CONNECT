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

public final class AliasBean implements ObjectBean
{
    public static final int version = 1;

    private AliasObject mAliasObject;
    private ClearFieldObject mClearFieldObj;
    private static java.text.SimpleDateFormat mDateFormat = new SimpleDateFormat(MetaDataService.getDateFormat());

    /**
     * Creates a new AliasBean instance.
     * @throws  ObjectException If creation fails. 
     */ 
    public AliasBean() throws ObjectException
    { 
       mAliasObject = new AliasObject();
    }
    
    /**
     * Creates a new AliasBean instance from a ClearFieldObject.
     */ 
    public AliasBean(ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mAliasObject = new AliasObject();
       mClearFieldObj = clearFieldObj;
    }

    /**
     * Creates a new AliasBean instance from a AliasObject.
     */
    protected AliasBean(AliasObject aAliasObject) throws ObjectException
    { 
       mAliasObject = aAliasObject;
    }
    
    /**
     * Creates a new AliasBean instance from 
     * a AliasObject and a ClearFieldObject.
     */
    protected AliasBean(AliasObject aAliasObject,
      ClearFieldObject clearFieldObj) throws ObjectException
    { 
       mAliasObject = aAliasObject;
       mClearFieldObj = clearFieldObj;
    }
    
    /**
     * Getter for AliasId
     * @return a string value of AliasId
     */    
    public String getAliasId() throws ObjectException
    {
        try
        {
            int type = mAliasObject.pGetType("AliasId");
            Object value = mAliasObject.getValue("AliasId");
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
            int type = mAliasObject.pGetType("FirstName");
            Object value = mAliasObject.getValue("FirstName");
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
            int type = mAliasObject.pGetType("MiddleName");
            Object value = mAliasObject.getValue("MiddleName");
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
            int type = mAliasObject.pGetType("LastName");
            Object value = mAliasObject.getValue("LastName");
            return objToString(value, type);        
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    /**
     * Setter for AliasId
     * @param string value of AliasId
     */ 
    public void setAliasId(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAliasObject.isNullable("AliasId")) {
               mAliasObject.clearField("AliasId");
            } else {
               int type = mAliasObject.pGetType("AliasId");
               Object val = strToObj(value, type, "AliasId");
          
               mAliasObject.setValue("AliasId", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for FirstName
     * @param string value of FirstName
     */ 
    public void setFirstName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAliasObject.isNullable("FirstName")) {
               mAliasObject.clearField("FirstName");
            } else {
               int type = mAliasObject.pGetType("FirstName");
               Object val = strToObj(value, type, "FirstName");
          
               mAliasObject.setValue("FirstName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for MiddleName
     * @param string value of MiddleName
     */ 
    public void setMiddleName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAliasObject.isNullable("MiddleName")) {
               mAliasObject.clearField("MiddleName");
            } else {
               int type = mAliasObject.pGetType("MiddleName");
               Object val = strToObj(value, type, "MiddleName");
          
               mAliasObject.setValue("MiddleName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    /**
     * Setter for LastName
     * @param string value of LastName
     */ 
    public void setLastName(String value) throws ObjectException
    {
        try
        {
            Object clearFieldIndicator = (mClearFieldObj != null) ? mClearFieldObj.getClearFieldIndicator() : null;
            if (value != null && clearFieldIndicator != null &&
                value.equals(clearFieldIndicator) &&
                mAliasObject.isNullable("LastName")) {
               mAliasObject.clearField("LastName");
            } else {
               int type = mAliasObject.pGetType("LastName");
               Object val = strToObj(value, type, "LastName");
          
               mAliasObject.setValue("LastName", val);
            }
        
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    
    protected ObjectNode getObjectNode() {
        return mAliasObject;
    }

    /** 
     * Return AliasObject
     * @return AliasObject
     */ 
    public ObjectNode pGetObject() {
        return mAliasObject;
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
        ObjectNode parent = mAliasObject.getParent();
        parent.deleteChild("Alias", mAliasObject.pGetSuperKey()); 
    }
        
    // Find parent which is SystemObject    
    private SystemObject getParentSO() {
        ObjectNode obj = mAliasObject.getParent();
        
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
