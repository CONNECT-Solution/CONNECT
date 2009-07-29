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

public final class PhoneObject extends ObjectNode
{
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add("PhoneId");
        mFieldNames.add("PhoneType");
        mFieldNames.add("Phone");
        mFieldNames.add("PhoneExt");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates new potentialDuplicate */
    public PhoneObject() throws ObjectException
    {
        super("Phone", mFieldNames, mFieldTypes);
        mParentTag = "Patient";
        mChildTags = null;
        mTag = "Phone";
        setKeyType("PhoneId", false);
        setKeyType("PhoneType", true);
        setKeyType("Phone", false);
        setKeyType("PhoneExt", false);
        setNullable("PhoneId", false);
        setNullable("PhoneType", false);
        setNullable("Phone", false);
        setNullable("PhoneExt", true);
    }

    public String getObjectId() throws ObjectException
    {
        return (getPhoneId());
    }

    public String getPhoneId() throws ObjectException
    {
        try
        {
            return ((String)getValue("PhoneId"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPhoneType() throws ObjectException
    {
        try
        {
            return ((String)getValue("PhoneType"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPhone() throws ObjectException
    {
        try
        {
            return ((String)getValue("Phone"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }

    public String getPhoneExt() throws ObjectException
    {
        try
        {
            return ((String)getValue("PhoneExt"));
        }
        catch (ObjectException e)
        {
            throw e;
        }
    }


    public void setObjectId(Object value) throws ObjectException
    {
        setPhoneId(value);
    }

    public void setPhoneId(Object value) throws ObjectException
    {
        try
        {
            setValue("PhoneId", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPhoneType(Object value) throws ObjectException
    {
        try
        {
            setValue("PhoneType", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPhone(Object value) throws ObjectException
    {
        try
        {
            setValue("Phone", value);
        }
        catch(ObjectException e)
        {
            throw e;
        }
    }
    
    public void setPhoneExt(Object value) throws ObjectException
    {
        try
        {
            setValue("PhoneExt", value);
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
        PhoneObject ret = null;
        try
        {
            ret = new PhoneObject();
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
        PhoneObject ret = null;
        try
        {
            ret = new PhoneObject();
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
