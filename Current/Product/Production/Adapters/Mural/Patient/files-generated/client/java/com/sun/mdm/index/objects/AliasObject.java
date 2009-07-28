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

public final class AliasObject extends ObjectNode
{
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    static
    {
        mFieldNames = new ArrayList();
        mFieldNames.add("AliasId");
        mFieldNames.add("FirstName");
        mFieldNames.add("MiddleName");
        mFieldNames.add("LastName");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    
    /** Creates new potentialDuplicate */
    public AliasObject() throws ObjectException
    {
        super("Alias", mFieldNames, mFieldTypes);
        mParentTag = "Patient";
        mChildTags = null;
        mTag = "Alias";
        setKeyType("AliasId", false);
        setKeyType("FirstName", true);
        setKeyType("MiddleName", true);
        setKeyType("LastName", true);
        setNullable("AliasId", false);
        setNullable("FirstName", false);
        setNullable("MiddleName", true);
        setNullable("LastName", false);
    }

    public String getObjectId() throws ObjectException
    {
        return (getAliasId());
    }

    public String getAliasId() throws ObjectException
    {
        try
        {
            return ((String)getValue("AliasId"));
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


    public void setObjectId(Object value) throws ObjectException
    {
        setAliasId(value);
    }

    public void setAliasId(Object value) throws ObjectException
    {
        try
        {
            setValue("AliasId", value);
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
        AliasObject ret = null;
        try
        {
            ret = new AliasObject();
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
        AliasObject ret = null;
        try
        {
            ret = new AliasObject();
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
