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
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.ops.exception.*;
import com.sun.mdm.index.idgen.CUIDManager;

public final class AliasSBRDB 
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
		"               aliasid, \n"+
		"               firstname, \n"+
		"               middlename, \n"+
		"               lastname \n"+
		"       from \n"+
		"               sbyn_aliassbr \n"+
		"       where \n"+
		"               patientid = ? \n";
		
		mSelectStringNonKeyed =
		"       select \n" +
		"               patientid, \n" +
		"               aliasid, \n" +
		"               firstname, \n" +
		"               middlename, \n" +
		"               lastname \n" +
		"       from \n" +
		"               sbyn_aliassbr \n"+
		"       where \n" +
		"               patientid = ? \n" +
                "       order by \n" +
		"               aliasid asc";
		
		mInsertString =
		"       insert into sbyn_aliassbr \n"+
		"       ( \n"+
		"               patientid, \n"+
		"               aliasid, \n"+
		"               firstname, \n"+
		"               middlename, \n"+
		"               lastname \n"+
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
		"       update sbyn_aliassbr \n"+
		"       set \n"+
		"               patientid = ?, \n"+
		"               aliasid = ?, \n"+
		"               firstname = ?, \n"+
		"               middlename = ?, \n"+
		"               lastname = ?  \n"+
		"       where \n"+
		"               aliasid = ? \n";
		
		mDeleteString =
		"       delete from sbyn_aliassbr \n"+
		"       where \n"+
		"               aliasid = ? \n";
    }
    
        
    public AliasSBRDB() 
        throws OPSException {
		super();
    }
    
    public void create(Connection conn, HashMap opsmap, String[] keys, ObjectNode node) 
        throws OPSException {
        if (keys == null) {
            throw new OPSException ("AliasSBRDB: invalid parent key(s) in create()");
        }
        
        AliasObject alias_obj = (AliasObject) node;                    
		PreparedStatement stmt = null;
		try {
            log("creating AliasSBR");
        
		    stmt = getStatement(mInsertString, conn);
	    	int count = 1;
                if (alias_obj.getAliasId() == null) {
                    alias_obj.setAliasId(CUIDManager.getNextUID(conn, "ALIASSBR"));
                }
	    	for(int i=0; i<keys.length; i++) {
	    	    stmt.setString(count++, keys[i]);
	    	    log("    keys[" + i + "]: " + keys[i]);
	    	}
			setParam(stmt, count++, "String", alias_obj.getAliasId());
			setParam(stmt, count++, "String", alias_obj.getFirstName());
			setParam(stmt, count++, "String", alias_obj.getMiddleName());
			setParam(stmt, count++, "String", alias_obj.getLastName());
	    
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
			    params = addobject(params, alias_obj.getAliasId());
			    params = addobject(params, alias_obj.getFirstName());
			    params = addobject(params, alias_obj.getMiddleName());
			    params = addobject(params, alias_obj.getLastName());
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
            throw new OPSException ("AliasSBRDB: invalid parent key(s) in get()");
        }
                    
        ArrayList a_ret = null;
        PreparedStatement stmt = null;
        ResultSet r_set = null;
        try {
            log("retrieving AliasSBR");
            AliasObject alias_obj_temp = new AliasObject();
            if (alias_obj_temp.pGetKey() != null) {
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
			
                AliasObject alias_obj = new AliasObject();
                alias_obj.setAliasId(getValue(r_set, "AliasId", "String"));
                alias_obj.setFirstName(getValue(r_set, "FirstName", "String"));
                alias_obj.setMiddleName(getValue(r_set, "MiddleName", "String"));
                alias_obj.setLastName(getValue(r_set, "LastName", "String"));
				
                String[] pid = null;
                alias_obj.resetAll();
                a_ret.add(alias_obj);
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
            throw new OPSException ("AliasSBRDB: invalid parent key(s) in update()");
        }
                    
        AliasObject alias_obj = (AliasObject)node;
    	PreparedStatement stmt = null;
    	try
    	{
            log("updating AliasSBR");
    
    		if (!alias_obj.isAdded())
    		{
				if (alias_obj.isUpdated())
				{
				    stmt = getStatement(mUpdateString, conn);
					int count = 1;
					for(int i=0; i<keys.length; i++)
					{
					    stmt.setString(count++, keys[i]);
					    log("   key[" + i + "]: " + keys[i]);
					}
					setParam(stmt, count++, "String", alias_obj.getAliasId());
					setParam(stmt, count++, "String", alias_obj.getFirstName());
					setParam(stmt, count++, "String", alias_obj.getMiddleName());
					setParam(stmt, count++, "String", alias_obj.getLastName());
					stmt.setString(count++, alias_obj.getAliasId());
					log("   AliasId: " + alias_obj.getAliasId());
					
					stmt.executeUpdate();
					alias_obj.setUpdateFlag(false);
	    		}
	    	    else if (alias_obj.isRemoved())
	    	    {
			    	this.remove(conn, opsmap, alias_obj);
	    	    }

                if (!alias_obj.isRemoved()) {
                }
	    	}
	    	else
	    	{
				this.create(conn, opsmap, keys, alias_obj);
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
                params = addobject(params, alias_obj.getAliasId());
                params = addobject(params, alias_obj.getFirstName());
                params = addobject(params, alias_obj.getMiddleName());
                params = addobject(params, alias_obj.getLastName());
                params = addobject(params, alias_obj.getAliasId());
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
        AliasObject alias_obj = (AliasObject) node;
        PreparedStatement stmt = null;
		try
		{
            log("To remove AliasSBR");

            stmt = getStatement(mDeleteString, conn);
		    stmt.setString(1, alias_obj.getAliasId());
		    log("   AliasId: " + alias_obj.getAliasId());
		    
		    stmt.executeUpdate();
		}
		catch (SQLException e)
		{
            String sql_err = e.getMessage();
            try
            {
                ArrayList params = new ArrayList();
                params = addobject(params, alias_obj.getAliasId());
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