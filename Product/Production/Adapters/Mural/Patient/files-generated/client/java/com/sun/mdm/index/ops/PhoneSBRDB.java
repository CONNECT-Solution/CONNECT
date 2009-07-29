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

package com.sun.mdm.index.ops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.ops.exception.*;
import com.sun.mdm.index.idgen.CUIDManager;

public final class PhoneSBRDB 
    extends ObjectPersistenceService 
    implements EntityOPS {
    
    static private String mDeleteString;
    static private String mInsertString;
    static private String mSelectStringKeyed;
    static private String mSelectStringNonKeyed;
    static private String mUpdateString;

    static {
		mSelectStringKeyed =
		"       select \n"+
		"               patientid, \n"+
		"               phoneid, \n"+
		"               phonetype, \n"+
		"               phone, \n"+
		"               phoneext \n"+
		"       from \n"+
		"               sbyn_phonesbr \n"+
		"       where \n"+
		"               patientid = ? \n";
		
		mSelectStringNonKeyed =
		"       select \n" +
		"               patientid, \n" +
		"               phoneid, \n" +
		"               phonetype, \n" +
		"               phone, \n" +
		"               phoneext \n" +
		"       from \n" +
		"               sbyn_phonesbr \n"+
		"       where \n" +
		"               patientid = ? \n" +
                "       order by \n" +
		"               phoneid asc";
		
		mInsertString =
		"       insert into sbyn_phonesbr \n"+
		"       ( \n"+
		"               patientid, \n"+
		"               phoneid, \n"+
		"               phonetype, \n"+
		"               phone, \n"+
		"               phoneext \n"+
		"       ) \n"+
		"       values \n"+
		"       ( \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ? \n"+
		"       ) \n";
		
		mUpdateString =
		"       update sbyn_phonesbr \n"+
		"       set \n"+
		"               patientid = ?, \n"+
		"               phoneid = ?, \n"+
		"               phonetype = ?, \n"+
		"               phone = ?, \n"+
		"               phoneext = ?  \n"+
		"       where \n"+
		"               phoneid = ? \n";
		
		mDeleteString =
		"       delete from sbyn_phonesbr \n"+
		"       where \n"+
		"               phoneid = ? \n";
    }
    
        
    public PhoneSBRDB() 
        throws OPSException {
		super();
    }
    
    public void create(Connection conn, HashMap opsmap, String[] keys, ObjectNode node) 
        throws OPSException {
        if (keys == null) {
            throw new OPSException ("PhoneSBRDB: invalid parent key(s) in create()");
        }
        
        PhoneObject phone_obj = (PhoneObject) node;                    
		PreparedStatement stmt = null;
		try {
            log("creating PhoneSBR");
        
		    stmt = getStatement(mInsertString, conn);
	    	int count = 1;
                if (phone_obj.getPhoneId() == null) {
                    phone_obj.setPhoneId(CUIDManager.getNextUID(conn, "PHONESBR"));
                }
	    	for(int i=0; i<keys.length; i++) {
	    	    stmt.setString(count++, keys[i]);
	    	    log("    keys[" + i + "]: " + keys[i]);
	    	}
			setParam(stmt, count++, "String", phone_obj.getPhoneId());
			setParam(stmt, count++, "String", phone_obj.getPhoneType());
			setParam(stmt, count++, "String", phone_obj.getPhone());
			setParam(stmt, count++, "String", phone_obj.getPhoneExt());
	    
	    	stmt.executeUpdate();
		}
		catch (SQLException e)
		{
            String sql_err = e.getMessage();
            ArrayList params = new ArrayList();
            try
            {
                for(int i=0; i<keys.length; i++)
                {
                    params = addobject(params, keys[i]);
                }
			    params = addobject(params, phone_obj.getPhoneId());
			    params = addobject(params, phone_obj.getPhoneType());
			    params = addobject(params, phone_obj.getPhone());
			    params = addobject(params, phone_obj.getPhoneExt());
                String sql = sql2str(mInsertString, params);
                throw new OPSException(sql + e.getMessage());
            }
            catch (ObjectException oe)
            {
                throw new OPSException (oe.getMessage() + sql_err);
            }
		}
		catch (ObjectException e)
		{
		    throw new OPSException(e.getMessage());
		}
		catch (OPSException e)
		{
		    throw e;
		}
		catch (Exception e)
		{
		    throw new OPSException(e.getMessage());
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }
    
    public ArrayList get(Connection conn, HashMap opsmap, String[] keys) throws OPSException {
        boolean keyedObject = true;
        if (keys == null) {
            throw new OPSException ("PhoneSBRDB: invalid parent key(s) in get()");
        }
                    
        ArrayList a_ret = null;
        PreparedStatement stmt = null;
        ResultSet r_set = null;
        try {
            log("retrieving PhoneSBR");
            PhoneObject phone_obj_temp = new PhoneObject();
            if (phone_obj_temp.pGetKey() != null) {
                stmt = getStatement(mSelectStringKeyed, conn);
            } else {
                stmt = getStatement(mSelectStringNonKeyed, conn);
                keyedObject = false;
            }
            int count = 1;
            for(int i=0; i<keys.length; i++) {
                stmt.setString(count++, keys[i]);
                log("    key[" + i + "]: " + keys[i]);
            }
            r_set = stmt.executeQuery();
            while (r_set.next()) {
                if (null == a_ret) {
                    a_ret = new ArrayList();
                }
			
                PhoneObject phone_obj = new PhoneObject();
                phone_obj.setPhoneId(getValue(r_set, "PhoneId", "String"));
                phone_obj.setPhoneType(getValue(r_set, "PhoneType", "String"));
                phone_obj.setPhone(getValue(r_set, "Phone", "String"));
                phone_obj.setPhoneExt(getValue(r_set, "PhoneExt", "String"));
				
                String[] pid = null;
                phone_obj.resetAll();
                a_ret.add(phone_obj);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            for(int i=0; i<keys.length; i++) {
                params = addobject(params, keys[i]);
            }

            String sql = null;
            if (keyedObject) {
                sql = sql2str(mSelectStringKeyed, params);
            } else {
                sql = sql2str(mSelectStringNonKeyed, params);
            }

            throw new OPSException(sql+e.getMessage());
        } catch (ObjectException e) {
            throw new OPSException(e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
            try {
                if (r_set != null) {
                    r_set.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException("failed to close statement");
            }
        }
        return a_ret;
    }
    
    public void update(Connection conn, HashMap opsmap, String[] keys, ObjectNode node) throws OPSException
    {
        if (keys == null) {
            throw new OPSException ("PhoneSBRDB: invalid parent key(s) in update()");
        }
                    
        PhoneObject phone_obj = (PhoneObject)node;
    	PreparedStatement stmt = null;
    	try
    	{
            log("updating PhoneSBR");
    
    		if (!phone_obj.isAdded())
    		{
				if (phone_obj.isUpdated())
				{
				    stmt = getStatement(mUpdateString, conn);
					int count = 1;
					for(int i=0; i<keys.length; i++)
					{
					    stmt.setString(count++, keys[i]);
					    log("   key[" + i + "]: " + keys[i]);
					}
					setParam(stmt, count++, "String", phone_obj.getPhoneId());
					setParam(stmt, count++, "String", phone_obj.getPhoneType());
					setParam(stmt, count++, "String", phone_obj.getPhone());
					setParam(stmt, count++, "String", phone_obj.getPhoneExt());
					stmt.setString(count++, phone_obj.getPhoneId());
					log("   PhoneId: " + phone_obj.getPhoneId());
					
					stmt.executeUpdate();
					phone_obj.setUpdateFlag(false);
	    		}
	    	    else if (phone_obj.isRemoved())
	    	    {
			    	this.remove(conn, opsmap, phone_obj);
	    	    }

                if (!phone_obj.isRemoved()) {
                }
	    	}
	    	else
	    	{
				this.create(conn, opsmap, keys, phone_obj);
	    	}

	    }	
	    catch (SQLException e)
	    {
            String sql_err = e.getMessage();
            ArrayList params = new ArrayList();
            try
            {
                for(int i=0; i<keys.length; i++)
                {
                    params = addobject(params, keys[i]);
                }
                params = addobject(params, phone_obj.getPhoneId());
                params = addobject(params, phone_obj.getPhoneType());
                params = addobject(params, phone_obj.getPhone());
                params = addobject(params, phone_obj.getPhoneExt());
                params = addobject(params, phone_obj.getPhoneId());
                String sql = sql2str(mUpdateString, params);	
                throw new OPSException(sql + e.getMessage());
            }
            catch (ObjectException oe)
            {
                throw new OPSException (oe.getMessage() + sql_err);
            }
	    }
	    catch (ObjectException e)
	    {
			throw new OPSException(e.getMessage());
	    }
		catch (OPSException e)
		{
		    throw e;
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }
    
    public void remove(Connection conn, HashMap opsmap, ObjectNode node) throws OPSException
    {
        PhoneObject phone_obj = (PhoneObject) node;
        PreparedStatement stmt = null;
		try
		{
            log("To remove PhoneSBR");

            stmt = getStatement(mDeleteString, conn);
		    stmt.setString(1, phone_obj.getPhoneId());
		    log("   PhoneId: " + phone_obj.getPhoneId());
		    
		    stmt.executeUpdate();
		}
		catch (SQLException e)
		{
            String sql_err = e.getMessage();
            try
            {
                ArrayList params = new ArrayList();
                params = addobject(params, phone_obj.getPhoneId());
                String sql = sql2str(mDeleteString, params);
                throw new OPSException(sql + e.getMessage());
            }
            catch (ObjectException oe)
            {
                throw new OPSException(oe.getMessage() + sql_err);
            }
		}
		catch (ObjectException e)
		{
		    throw new OPSException(e.getMessage());
		}
		catch (OPSException e)
		{
		    throw e;
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }
}