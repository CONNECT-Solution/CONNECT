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

public final class AddressObject extends ObjectNode
{
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add("AddressId");
        mFieldNames.add("AddressType");
        mFieldNames.add("AddressLine1");
        mFieldNames.add("AddressLine1_HouseNo");
        mFieldNames.add("AddressLine1_StDir");
        mFieldNames.add("AddressLine1_StName");
        mFieldNames.add("AddressLine1_StPhon");
        mFieldNames.add("AddressLine1_StType");
        mFieldNames.add("AddressLine2");
        mFieldNames.add("City");
        mFieldNames.add("StateCode");
        mFieldNames.add("PostalCode");
        mFieldNames.add("PostalCodeExt");
        mFieldNames.add("County");
        mFieldNames.add("CountryCode");
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
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates new potentialDuplicate */
    public AddressObject() throws ObjectException
    {
        super("Address", mFieldNames, mFieldTypes);
        mParentTag = "Patient";
        mChildTags = null;
        mTag = "Address";
        setKeyType("AddressId", false);
        setKeyType("AddressType", true);
        setKeyType("AddressLine1", false);
        setKeyType("AddressLine1_HouseNo", false);
        setKeyType("AddressLine1_StDir", false);
        setKeyType("AddressLine1_StName", false);
        setKeyType("AddressLine1_StPhon", false);
        setKeyType("AddressLine1_StType", false);
        setKeyType("AddressLine2", false);
        setKeyType("City", false);
        setKeyType("StateCode", false);
        setKeyType("PostalCode", false);
        setKeyType("PostalCodeExt", false);
        setKeyType("County", false);
        setKeyType("CountryCode", false);
        setNullable("AddressId", false);
        setNullable("AddressType", false);
        setNullable("AddressLine1", false);
        setNullable("AddressLine1_HouseNo", true);
        setNullable("AddressLine1_StDir", true);
        setNullable("AddressLine1_StName", true);
        setNullable("AddressLine1_StPhon", true);
        setNullable("AddressLine1_StType", true);
        setNullable("AddressLine2", true);
        setNullable("City", true);
        setNullable("StateCode", true);
        setNullable("PostalCode", true);
        setNullable("PostalCodeExt", true);
        setNullable("County", true);
        setNullable("CountryCode", true);
    }

    public String getObjectId() throws ObjectException
    {
        return (getAddressId());
    }

    public String getAddressId() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressId"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressType() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressType"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1_HouseNo() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1_HouseNo"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1_StDir() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1_StDir"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1_StName() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1_StName"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1_StPhon() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1_StPhon"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine1_StType() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine1_StType"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getAddressLine2() throws ObjectException
    {
        try
        {
            return ((String)getValue("AddressLine2"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getCity() throws ObjectException
    {
        try
        {
            return ((String)getValue("City"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getStateCode() throws ObjectException
    {
        try
        {
            return ((String)getValue("StateCode"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPostalCode() throws ObjectException
    {
        try
        {
            return ((String)getValue("PostalCode"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPostalCodeExt() throws ObjectException
    {
        try
        {
            return ((String)getValue("PostalCodeExt"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getCounty() throws ObjectException
    {
        try
        {
            return ((String)getValue("County"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getCountryCode() throws ObjectException
    {
        try
        {
            return ((String)getValue("CountryCode"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }


    public void setObjectId(Object value) throws ObjectException
    {
        setAddressId(value);
    }

    public void setAddressId(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressId", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressType(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressType", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1_HouseNo(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1_HouseNo", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1_StDir(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1_StDir", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1_StName(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1_StName", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1_StPhon(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1_StPhon", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine1_StType(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine1_StType", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setAddressLine2(Object value) throws ObjectException
    {
        try
        {
            setValue("AddressLine2", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setCity(Object value) throws ObjectException
    {
        try
        {
            setValue("City", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setStateCode(Object value) throws ObjectException
    {
        try
        {
            setValue("StateCode", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPostalCode(Object value) throws ObjectException
    {
        try
        {
            setValue("PostalCode", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPostalCodeExt(Object value) throws ObjectException
    {
        try
        {
            setValue("PostalCodeExt", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setCounty(Object value) throws ObjectException
    {
        try
        {
            setValue("County", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setCountryCode(Object value) throws ObjectException
    {
        try
        {
            setValue("CountryCode", value);
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

    public String getParentTag()
    {
        return "Patient";
    }

    
    public ObjectNode copy() throws ObjectException
    {
        AddressObject ret = null;
        try
        {
            ret = new AddressObject();
            String tag = pGetTag ();
            ArrayList names = pGetFieldNames ();
            ArrayList types = getFieldTypes ();
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

            ArrayList fieldUpdateLogs = null;
            if ( pGetFieldUpdateLogs()!= null ){
                fieldUpdateLogs = (ArrayList)pGetFieldUpdateLogs().clone();    
            }
            ret.setFieldUpdateLogs( fieldUpdateLogs );
            ret.setKeyChangeFlag ( isKeyChanged() );

            if (null != mParent)
            {
                ret.setParent (mParent);
            }
        }
        catch (ObjectException e)
        {
            throw e;
        }
                
        return (ObjectNode)ret;
    } 
    
    public ObjectNode structCopy() throws ObjectException
    {
        AddressObject ret = null;
        try
        {
            ret = new AddressObject();
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
                ret.setParent (mParent);
            }
        }
        catch (ObjectException e)
        {
            throw e;
        }
                
        return (ObjectNode)ret;
    } 
}
