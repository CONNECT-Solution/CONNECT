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
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.ops.exception.*;
import com.sun.mdm.index.idgen.CUIDManager;

public final class AddressSBRDB 
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
		"               addressid, \n"+
		"               addresstype, \n"+
		"               addressline1, \n"+
		"               addressline1_houseno, \n"+
		"               addressline1_stdir, \n"+
		"               addressline1_stname, \n"+
		"               addressline1_stphon, \n"+
		"               addressline1_sttype, \n"+
		"               addressline2, \n"+
		"               city, \n"+
		"               statecode, \n"+
		"               postalcode, \n"+
		"               postalcodeext, \n"+
		"               county, \n"+
		"               countrycode \n"+
		"       from \n"+
		"               sbyn_addresssbr \n"+
		"       where \n"+
		"               patientid = ? \n";
		
		mSelectStringNonKeyed =
		"       select \n" +
		"               patientid, \n" +
		"               addressid, \n" +
		"               addresstype, \n" +
		"               addressline1, \n" +
		"               addressline1_houseno, \n" +
		"               addressline1_stdir, \n" +
		"               addressline1_stname, \n" +
		"               addressline1_stphon, \n" +
		"               addressline1_sttype, \n" +
		"               addressline2, \n" +
		"               city, \n" +
		"               statecode, \n" +
		"               postalcode, \n" +
		"               postalcodeext, \n" +
		"               county, \n" +
		"               countrycode \n" +
		"       from \n" +
		"               sbyn_addresssbr \n"+
		"       where \n" +
		"               patientid = ? \n" +
                "       order by \n" +
		"               addressid asc";
		
		mInsertString =
		"       insert into sbyn_addresssbr \n"+
		"       ( \n"+
		"               patientid, \n"+
		"               addressid, \n"+
		"               addresstype, \n"+
		"               addressline1, \n"+
		"               addressline1_houseno, \n"+
		"               addressline1_stdir, \n"+
		"               addressline1_stname, \n"+
		"               addressline1_stphon, \n"+
		"               addressline1_sttype, \n"+
		"               addressline2, \n"+
		"               city, \n"+
		"               statecode, \n"+
		"               postalcode, \n"+
		"               postalcodeext, \n"+
		"               county, \n"+
		"               countrycode \n"+
		"       ) \n"+
		"       values \n"+
		"       ( \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ?, \n"+
		"               ? \n"+
		"       ) \n";
		
		mUpdateString =
		"       update sbyn_addresssbr \n"+
		"       set \n"+
		"               patientid = ?, \n"+
		"               addressid = ?, \n"+
		"               addresstype = ?, \n"+
		"               addressline1 = ?, \n"+
		"               addressline1_houseno = ?, \n"+
		"               addressline1_stdir = ?, \n"+
		"               addressline1_stname = ?, \n"+
		"               addressline1_stphon = ?, \n"+
		"               addressline1_sttype = ?, \n"+
		"               addressline2 = ?, \n"+
		"               city = ?, \n"+
		"               statecode = ?, \n"+
		"               postalcode = ?, \n"+
		"               postalcodeext = ?, \n"+
		"               county = ?, \n"+
		"               countrycode = ?  \n"+
		"       where \n"+
		"               addressid = ? \n";
		
		mDeleteString =
		"       delete from sbyn_addresssbr \n"+
		"       where \n"+
		"               addressid = ? \n";
    }
    
        
    public AddressSBRDB() 
        throws OPSException {
		super();
    }
    
    public void create(Connection conn, HashMap opsmap, String[] keys, ObjectNode node) 
        throws OPSException {
        if (keys == null) {
            throw new OPSException ("AddressSBRDB: invalid parent key(s) in create()");
        }
        
        AddressObject address_obj = (AddressObject) node;                    
		PreparedStatement stmt = null;
		try {
            log("creating AddressSBR");
        
		    stmt = getStatement(mInsertString, conn);
	    	int count = 1;
                if (address_obj.getAddressId() == null) {
                    address_obj.setAddressId(CUIDManager.getNextUID(conn, "ADDRESSSBR"));
                }
	    	for(int i=0; i<keys.length; i++) {
	    	    stmt.setString(count++, keys[i]);
	    	    log("    keys[" + i + "]: " + keys[i]);
	    	}
			setParam(stmt, count++, "String", address_obj.getAddressId());
			setParam(stmt, count++, "String", address_obj.getAddressType());
			setParam(stmt, count++, "String", address_obj.getAddressLine1());
			setParam(stmt, count++, "String", address_obj.getAddressLine1_HouseNo());
			setParam(stmt, count++, "String", address_obj.getAddressLine1_StDir());
			setParam(stmt, count++, "String", address_obj.getAddressLine1_StName());
			setParam(stmt, count++, "String", address_obj.getAddressLine1_StPhon());
			setParam(stmt, count++, "String", address_obj.getAddressLine1_StType());
			setParam(stmt, count++, "String", address_obj.getAddressLine2());
			setParam(stmt, count++, "String", address_obj.getCity());
			setParam(stmt, count++, "String", address_obj.getStateCode());
			setParam(stmt, count++, "String", address_obj.getPostalCode());
			setParam(stmt, count++, "String", address_obj.getPostalCodeExt());
			setParam(stmt, count++, "String", address_obj.getCounty());
			setParam(stmt, count++, "String", address_obj.getCountryCode());
	    
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
			    params = addobject(params, address_obj.getAddressId());
			    params = addobject(params, address_obj.getAddressType());
			    params = addobject(params, address_obj.getAddressLine1());
			    params = addobject(params, address_obj.getAddressLine1_HouseNo());
			    params = addobject(params, address_obj.getAddressLine1_StDir());
			    params = addobject(params, address_obj.getAddressLine1_StName());
			    params = addobject(params, address_obj.getAddressLine1_StPhon());
			    params = addobject(params, address_obj.getAddressLine1_StType());
			    params = addobject(params, address_obj.getAddressLine2());
			    params = addobject(params, address_obj.getCity());
			    params = addobject(params, address_obj.getStateCode());
			    params = addobject(params, address_obj.getPostalCode());
			    params = addobject(params, address_obj.getPostalCodeExt());
			    params = addobject(params, address_obj.getCounty());
			    params = addobject(params, address_obj.getCountryCode());
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
            throw new OPSException ("AddressSBRDB: invalid parent key(s) in get()");
        }
                    
        ArrayList a_ret = null;
        PreparedStatement stmt = null;
        ResultSet r_set = null;
        try {
            log("retrieving AddressSBR");
            AddressObject address_obj_temp = new AddressObject();
            if (address_obj_temp.pGetKey() != null) {
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
			
                AddressObject address_obj = new AddressObject();
                address_obj.setAddressId(getValue(r_set, "AddressId", "String"));
                address_obj.setAddressType(getValue(r_set, "AddressType", "String"));
                address_obj.setAddressLine1(getValue(r_set, "AddressLine1", "String"));
                address_obj.setAddressLine1_HouseNo(getValue(r_set, "AddressLine1_HouseNo", "String"));
                address_obj.setAddressLine1_StDir(getValue(r_set, "AddressLine1_StDir", "String"));
                address_obj.setAddressLine1_StName(getValue(r_set, "AddressLine1_StName", "String"));
                address_obj.setAddressLine1_StPhon(getValue(r_set, "AddressLine1_StPhon", "String"));
                address_obj.setAddressLine1_StType(getValue(r_set, "AddressLine1_StType", "String"));
                address_obj.setAddressLine2(getValue(r_set, "AddressLine2", "String"));
                address_obj.setCity(getValue(r_set, "City", "String"));
                address_obj.setStateCode(getValue(r_set, "StateCode", "String"));
                address_obj.setPostalCode(getValue(r_set, "PostalCode", "String"));
                address_obj.setPostalCodeExt(getValue(r_set, "PostalCodeExt", "String"));
                address_obj.setCounty(getValue(r_set, "County", "String"));
                address_obj.setCountryCode(getValue(r_set, "CountryCode", "String"));
				
                String[] pid = null;
                address_obj.resetAll();
                a_ret.add(address_obj);
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
            throw new OPSException ("AddressSBRDB: invalid parent key(s) in update()");
        }
                    
        AddressObject address_obj = (AddressObject)node;
    	PreparedStatement stmt = null;
    	try
    	{
            log("updating AddressSBR");
    
    		if (!address_obj.isAdded())
    		{
				if (address_obj.isUpdated())
				{
				    stmt = getStatement(mUpdateString, conn);
					int count = 1;
					for(int i=0; i<keys.length; i++)
					{
					    stmt.setString(count++, keys[i]);
					    log("   key[" + i + "]: " + keys[i]);
					}
					setParam(stmt, count++, "String", address_obj.getAddressId());
					setParam(stmt, count++, "String", address_obj.getAddressType());
					setParam(stmt, count++, "String", address_obj.getAddressLine1());
					setParam(stmt, count++, "String", address_obj.getAddressLine1_HouseNo());
					setParam(stmt, count++, "String", address_obj.getAddressLine1_StDir());
					setParam(stmt, count++, "String", address_obj.getAddressLine1_StName());
					setParam(stmt, count++, "String", address_obj.getAddressLine1_StPhon());
					setParam(stmt, count++, "String", address_obj.getAddressLine1_StType());
					setParam(stmt, count++, "String", address_obj.getAddressLine2());
					setParam(stmt, count++, "String", address_obj.getCity());
					setParam(stmt, count++, "String", address_obj.getStateCode());
					setParam(stmt, count++, "String", address_obj.getPostalCode());
					setParam(stmt, count++, "String", address_obj.getPostalCodeExt());
					setParam(stmt, count++, "String", address_obj.getCounty());
					setParam(stmt, count++, "String", address_obj.getCountryCode());
					stmt.setString(count++, address_obj.getAddressId());
					log("   AddressId: " + address_obj.getAddressId());
					
					stmt.executeUpdate();
					address_obj.setUpdateFlag(false);
	    		}
	    	    else if (address_obj.isRemoved())
	    	    {
			    	this.remove(conn, opsmap, address_obj);
	    	    }

                if (!address_obj.isRemoved()) {
                }
	    	}
	    	else
	    	{
				this.create(conn, opsmap, keys, address_obj);
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
                params = addobject(params, address_obj.getAddressId());
                params = addobject(params, address_obj.getAddressType());
                params = addobject(params, address_obj.getAddressLine1());
                params = addobject(params, address_obj.getAddressLine1_HouseNo());
                params = addobject(params, address_obj.getAddressLine1_StDir());
                params = addobject(params, address_obj.getAddressLine1_StName());
                params = addobject(params, address_obj.getAddressLine1_StPhon());
                params = addobject(params, address_obj.getAddressLine1_StType());
                params = addobject(params, address_obj.getAddressLine2());
                params = addobject(params, address_obj.getCity());
                params = addobject(params, address_obj.getStateCode());
                params = addobject(params, address_obj.getPostalCode());
                params = addobject(params, address_obj.getPostalCodeExt());
                params = addobject(params, address_obj.getCounty());
                params = addobject(params, address_obj.getCountryCode());
                params = addobject(params, address_obj.getAddressId());
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
        AddressObject address_obj = (AddressObject) node;
        PreparedStatement stmt = null;
		try
		{
            log("To remove AddressSBR");

            stmt = getStatement(mDeleteString, conn);
		    stmt.setString(1, address_obj.getAddressId());
		    log("   AddressId: " + address_obj.getAddressId());
		    
		    stmt.executeUpdate();
		}
		catch (SQLException e)
		{
            String sql_err = e.getMessage();
            try
            {
                ArrayList params = new ArrayList();
                params = addobject(params, address_obj.getAddressId());
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