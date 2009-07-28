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
import com.sun.mdm.index.objects.PatientObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.exception.*;
import com.sun.mdm.index.ops.exception.*;
import com.sun.mdm.index.idgen.CUIDManager;

public final class PatientSBRDB 
    extends ObjectPersistenceService 
    implements EntityOPS {
    
    static private String mDeleteString;
    static private String mInsertString;
    static private String mSelectString;
    static private String mUpdateString;

    static {
		mSelectString =
		"       select \n"+
		"               euid, \n"+
		"               patientid, \n"+
		"               personcatcode, \n"+
		"               firstname, \n"+
		"               firstname_std, \n"+
		"               firstname_phon, \n"+
		"               middlename, \n"+
		"               lastname, \n"+
		"               lastname_std, \n"+
		"               lastname_phon, \n"+
		"               suffix, \n"+
		"               title, \n"+
		"               ssn, \n"+
		"               dob, \n"+
		"               death, \n"+
		"               gender, \n"+
		"               mstatus, \n"+
		"               race, \n"+
		"               ethnic, \n"+
		"               religion, \n"+
		"               language, \n"+
		"               spousename, \n"+
		"               mothername, \n"+
		"               mothermn, \n"+
		"               fathername, \n"+
		"               maiden, \n"+
		"               pobcity, \n"+
		"               pobstate, \n"+
		"               pobcountry, \n"+
		"               vipflag, \n"+
		"               vetstatus, \n"+
		"               status, \n"+
		"               driverlicense, \n"+
		"               driverlicensest, \n"+
		"               dod, \n"+
		"               deathcertificate, \n"+
		"               nationality, \n"+
		"               citizenship \n"+
		"       from \n"+
		"               sbyn_patientsbr \n"+
		"       where \n"+
		"               euid = ? \n";
		
		mInsertString =
		"       insert into sbyn_patientsbr \n"+
		"       ( \n"+
		"               euid, \n"+
		"               patientid, \n"+
		"               personcatcode, \n"+
		"               firstname, \n"+
		"               firstname_std, \n"+
		"               firstname_phon, \n"+
		"               middlename, \n"+
		"               lastname, \n"+
		"               lastname_std, \n"+
		"               lastname_phon, \n"+
		"               suffix, \n"+
		"               title, \n"+
		"               ssn, \n"+
		"               dob, \n"+
		"               death, \n"+
		"               gender, \n"+
		"               mstatus, \n"+
		"               race, \n"+
		"               ethnic, \n"+
		"               religion, \n"+
		"               language, \n"+
		"               spousename, \n"+
		"               mothername, \n"+
		"               mothermn, \n"+
		"               fathername, \n"+
		"               maiden, \n"+
		"               pobcity, \n"+
		"               pobstate, \n"+
		"               pobcountry, \n"+
		"               vipflag, \n"+
		"               vetstatus, \n"+
		"               status, \n"+
		"               driverlicense, \n"+
		"               driverlicensest, \n"+
		"               dod, \n"+
		"               deathcertificate, \n"+
		"               nationality, \n"+
		"               citizenship \n"+
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
		"       update sbyn_patientsbr \n"+
		"       set \n"+
		"               euid = ?, \n"+
		"               patientid = ?, \n"+
		"               personcatcode = ?, \n"+
		"               firstname = ?, \n"+
		"               firstname_std = ?, \n"+
		"               firstname_phon = ?, \n"+
		"               middlename = ?, \n"+
		"               lastname = ?, \n"+
		"               lastname_std = ?, \n"+
		"               lastname_phon = ?, \n"+
		"               suffix = ?, \n"+
		"               title = ?, \n"+
		"               ssn = ?, \n"+
		"               dob = ?, \n"+
		"               death = ?, \n"+
		"               gender = ?, \n"+
		"               mstatus = ?, \n"+
		"               race = ?, \n"+
		"               ethnic = ?, \n"+
		"               religion = ?, \n"+
		"               language = ?, \n"+
		"               spousename = ?, \n"+
		"               mothername = ?, \n"+
		"               mothermn = ?, \n"+
		"               fathername = ?, \n"+
		"               maiden = ?, \n"+
		"               pobcity = ?, \n"+
		"               pobstate = ?, \n"+
		"               pobcountry = ?, \n"+
		"               vipflag = ?, \n"+
		"               vetstatus = ?, \n"+
		"               status = ?, \n"+
		"               driverlicense = ?, \n"+
		"               driverlicensest = ?, \n"+
		"               dod = ?, \n"+
		"               deathcertificate = ?, \n"+
		"               nationality = ?, \n"+
		"               citizenship = ?  \n"+
		"       where \n"+
		"               patientid = ? \n";
		
		mDeleteString =
		"       delete from sbyn_patientsbr \n"+
		"       where \n"+
		"               patientid = ? \n";
    }
    
        
    public PatientSBRDB() 
        throws OPSException {
		super();
    }
    
    public void create(Connection conn, HashMap opsmap, String[] keys, ObjectNode node) 
        throws OPSException {
        if (keys == null) {
            throw new OPSException ("PatientSBRDB: invalid parent key(s) in create()");
        }
        
        PatientObject patient_obj = (PatientObject) node;                    
	    PreparedStatement stmt = null;
		try {
            log("creating PatientSBR");
        
		    stmt = getStatement(mInsertString, conn);
	    	int count = 1;
                if (patient_obj.getPatientId() == null) {
                    patient_obj.setPatientId(CUIDManager.getNextUID(conn, "PATIENTSBR"));
                }
	    	for(int i=0; i<keys.length; i++) {
	    	    stmt.setString(count++, keys[i]);
	    	    log("    keys[" + i + "]: " + keys[i]);
	    	}
			setParam(stmt, count++, "String", patient_obj.getPatientId());
			setParam(stmt, count++, "String", patient_obj.getPersonCatCode());
			setParam(stmt, count++, "String", patient_obj.getFirstName());
			setParam(stmt, count++, "String", patient_obj.getFirstName_Std());
			setParam(stmt, count++, "String", patient_obj.getFirstName_Phon());
			setParam(stmt, count++, "String", patient_obj.getMiddleName());
			setParam(stmt, count++, "String", patient_obj.getLastName());
			setParam(stmt, count++, "String", patient_obj.getLastName_Std());
			setParam(stmt, count++, "String", patient_obj.getLastName_Phon());
			setParam(stmt, count++, "String", patient_obj.getSuffix());
			setParam(stmt, count++, "String", patient_obj.getTitle());
			setParam(stmt, count++, "String", patient_obj.getSSN());
			setParam(stmt, count++, "Date", patient_obj.getDOB());
			setParam(stmt, count++, "String", patient_obj.getDeath());
			setParam(stmt, count++, "String", patient_obj.getGender());
			setParam(stmt, count++, "String", patient_obj.getMStatus());
			setParam(stmt, count++, "String", patient_obj.getRace());
			setParam(stmt, count++, "String", patient_obj.getEthnic());
			setParam(stmt, count++, "String", patient_obj.getReligion());
			setParam(stmt, count++, "String", patient_obj.getLanguage());
			setParam(stmt, count++, "String", patient_obj.getSpouseName());
			setParam(stmt, count++, "String", patient_obj.getMotherName());
			setParam(stmt, count++, "String", patient_obj.getMotherMN());
			setParam(stmt, count++, "String", patient_obj.getFatherName());
			setParam(stmt, count++, "String", patient_obj.getMaiden());
			setParam(stmt, count++, "String", patient_obj.getPobCity());
			setParam(stmt, count++, "String", patient_obj.getPobState());
			setParam(stmt, count++, "String", patient_obj.getPobCountry());
			setParam(stmt, count++, "String", patient_obj.getVIPFlag());
			setParam(stmt, count++, "String", patient_obj.getVetStatus());
			setParam(stmt, count++, "String", patient_obj.getStatus());
			setParam(stmt, count++, "String", patient_obj.getDriverLicense());
			setParam(stmt, count++, "String", patient_obj.getDriverLicenseSt());
			setParam(stmt, count++, "Date", patient_obj.getDod());
			setParam(stmt, count++, "String", patient_obj.getDeathCertificate());
			setParam(stmt, count++, "String", patient_obj.getNationality());
			setParam(stmt, count++, "String", patient_obj.getCitizenship());
	    
	    	stmt.executeUpdate();
			ArrayList alias_list = (ArrayList)patient_obj.getAlias();
			if (null != alias_list)
			{
				AliasSBRDB alias = (AliasSBRDB)opsmap.get("AliasSBRDB");
				for(int i=0; i<alias_list.size(); i++)
				{
				   	AliasObject alias_obj = (AliasObject)alias_list.get(i);
                                        if (patient_obj.getPatientId() == null) {
                                            alias_obj.setAliasId(com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "ALIASSBR"));
                                        }
				   	String[] pid = new String[1];
				   	pid[0] = patient_obj.getPatientId();
				   	alias.create(conn, opsmap, pid, alias_obj);
				}
			}
					
			ArrayList address_list = (ArrayList)patient_obj.getAddress();
			if (null != address_list)
			{
				AddressSBRDB address = (AddressSBRDB)opsmap.get("AddressSBRDB");
				for(int i=0; i<address_list.size(); i++)
				{
				   	AddressObject address_obj = (AddressObject)address_list.get(i);
                                        if (patient_obj.getPatientId() == null) {
                                            address_obj.setAddressId(com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "ADDRESSSBR"));
                                        }
				   	String[] pid = new String[1];
				   	pid[0] = patient_obj.getPatientId();
				   	address.create(conn, opsmap, pid, address_obj);
				}
			}
					
			ArrayList phone_list = (ArrayList)patient_obj.getPhone();
			if (null != phone_list)
			{
				PhoneSBRDB phone = (PhoneSBRDB)opsmap.get("PhoneSBRDB");
				for(int i=0; i<phone_list.size(); i++)
				{
				   	PhoneObject phone_obj = (PhoneObject)phone_list.get(i);
                                        if (patient_obj.getPatientId() == null) {
                                            phone_obj.setPhoneId(com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "PHONESBR"));
                                        }
				   	String[] pid = new String[1];
				   	pid[0] = patient_obj.getPatientId();
				   	phone.create(conn, opsmap, pid, phone_obj);
				}
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
			    params = addobject(params, patient_obj.getPatientId());
			    params = addobject(params, patient_obj.getPersonCatCode());
			    params = addobject(params, patient_obj.getFirstName());
			    params = addobject(params, patient_obj.getFirstName_Std());
			    params = addobject(params, patient_obj.getFirstName_Phon());
			    params = addobject(params, patient_obj.getMiddleName());
			    params = addobject(params, patient_obj.getLastName());
			    params = addobject(params, patient_obj.getLastName_Std());
			    params = addobject(params, patient_obj.getLastName_Phon());
			    params = addobject(params, patient_obj.getSuffix());
			    params = addobject(params, patient_obj.getTitle());
			    params = addobject(params, patient_obj.getSSN());
			    params = addobject(params, patient_obj.getDOB());
			    params = addobject(params, patient_obj.getDeath());
			    params = addobject(params, patient_obj.getGender());
			    params = addobject(params, patient_obj.getMStatus());
			    params = addobject(params, patient_obj.getRace());
			    params = addobject(params, patient_obj.getEthnic());
			    params = addobject(params, patient_obj.getReligion());
			    params = addobject(params, patient_obj.getLanguage());
			    params = addobject(params, patient_obj.getSpouseName());
			    params = addobject(params, patient_obj.getMotherName());
			    params = addobject(params, patient_obj.getMotherMN());
			    params = addobject(params, patient_obj.getFatherName());
			    params = addobject(params, patient_obj.getMaiden());
			    params = addobject(params, patient_obj.getPobCity());
			    params = addobject(params, patient_obj.getPobState());
			    params = addobject(params, patient_obj.getPobCountry());
			    params = addobject(params, patient_obj.getVIPFlag());
			    params = addobject(params, patient_obj.getVetStatus());
			    params = addobject(params, patient_obj.getStatus());
			    params = addobject(params, patient_obj.getDriverLicense());
			    params = addobject(params, patient_obj.getDriverLicenseSt());
			    params = addobject(params, patient_obj.getDod());
			    params = addobject(params, patient_obj.getDeathCertificate());
			    params = addobject(params, patient_obj.getNationality());
			    params = addobject(params, patient_obj.getCitizenship());
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
        if (keys == null) {
            throw new OPSException ("PatientSBRDB: invalid parent key(s) in get()");
        }
                    
		ArrayList a_ret = null;
	    PreparedStatement stmt = null;
	    ResultSet r_set = null;
		try
		{
            log("retrieving PatientSBR");

		    stmt = getStatement(mSelectString, conn);
		    int count = 1;
		    for(int i=0; i<keys.length; i++)
		    {
	    	    stmt.setString(count++, keys[i]);
	    	    log("    keys[" + i + "]: " + keys[i]);
	    	}
	    	r_set = stmt.executeQuery();
	    	while (r_set.next())
	    	{
				if (null == a_ret)
			    	a_ret = new ArrayList();
			
				PatientObject patient_obj = new PatientObject();
				patient_obj.setPatientId(getValue(r_set, "PatientId", "String"));
				patient_obj.setPersonCatCode(getValue(r_set, "PersonCatCode", "String"));
				patient_obj.setFirstName(getValue(r_set, "FirstName", "String"));
				patient_obj.setFirstName_Std(getValue(r_set, "FirstName_Std", "String"));
				patient_obj.setFirstName_Phon(getValue(r_set, "FirstName_Phon", "String"));
				patient_obj.setMiddleName(getValue(r_set, "MiddleName", "String"));
				patient_obj.setLastName(getValue(r_set, "LastName", "String"));
				patient_obj.setLastName_Std(getValue(r_set, "LastName_Std", "String"));
				patient_obj.setLastName_Phon(getValue(r_set, "LastName_Phon", "String"));
				patient_obj.setSuffix(getValue(r_set, "Suffix", "String"));
				patient_obj.setTitle(getValue(r_set, "Title", "String"));
				patient_obj.setSSN(getValue(r_set, "SSN", "String"));
				patient_obj.setDOB(getValue(r_set, "DOB", "Date"));
				patient_obj.setDeath(getValue(r_set, "Death", "String"));
				patient_obj.setGender(getValue(r_set, "Gender", "String"));
				patient_obj.setMStatus(getValue(r_set, "MStatus", "String"));
				patient_obj.setRace(getValue(r_set, "Race", "String"));
				patient_obj.setEthnic(getValue(r_set, "Ethnic", "String"));
				patient_obj.setReligion(getValue(r_set, "Religion", "String"));
				patient_obj.setLanguage(getValue(r_set, "Language", "String"));
				patient_obj.setSpouseName(getValue(r_set, "SpouseName", "String"));
				patient_obj.setMotherName(getValue(r_set, "MotherName", "String"));
				patient_obj.setMotherMN(getValue(r_set, "MotherMN", "String"));
				patient_obj.setFatherName(getValue(r_set, "FatherName", "String"));
				patient_obj.setMaiden(getValue(r_set, "Maiden", "String"));
				patient_obj.setPobCity(getValue(r_set, "PobCity", "String"));
				patient_obj.setPobState(getValue(r_set, "PobState", "String"));
				patient_obj.setPobCountry(getValue(r_set, "PobCountry", "String"));
				patient_obj.setVIPFlag(getValue(r_set, "VIPFlag", "String"));
				patient_obj.setVetStatus(getValue(r_set, "VetStatus", "String"));
				patient_obj.setStatus(getValue(r_set, "Status", "String"));
				patient_obj.setDriverLicense(getValue(r_set, "DriverLicense", "String"));
				patient_obj.setDriverLicenseSt(getValue(r_set, "DriverLicenseSt", "String"));
				patient_obj.setDod(getValue(r_set, "Dod", "Date"));
				patient_obj.setDeathCertificate(getValue(r_set, "DeathCertificate", "String"));
				patient_obj.setNationality(getValue(r_set, "Nationality", "String"));
				patient_obj.setCitizenship(getValue(r_set, "Citizenship", "String"));
				
				String[] pid = null;
				AliasSBRDB alias = (AliasSBRDB)opsmap.get("AliasSBRDB");
				pid = new String[1];
				pid[0] = patient_obj.getPatientId();
				ArrayList alias_list = alias.get(conn, opsmap, pid);
				if (null != alias_list)
				{
			    	for(int i=0; i<alias_list.size(); i++)
			    	{
						AliasObject alias_obj = (AliasObject)alias_list.get(i);
						patient_obj.addAlias(alias_obj);
			    	}
				}
			
				AddressSBRDB address = (AddressSBRDB)opsmap.get("AddressSBRDB");
				pid = new String[1];
				pid[0] = patient_obj.getPatientId();
				ArrayList address_list = address.get(conn, opsmap, pid);
				if (null != address_list)
				{
			    	for(int i=0; i<address_list.size(); i++)
			    	{
						AddressObject address_obj = (AddressObject)address_list.get(i);
						patient_obj.addAddress(address_obj);
			    	}
				}
			
				PhoneSBRDB phone = (PhoneSBRDB)opsmap.get("PhoneSBRDB");
				pid = new String[1];
				pid[0] = patient_obj.getPatientId();
				ArrayList phone_list = phone.get(conn, opsmap, pid);
				if (null != phone_list)
				{
			    	for(int i=0; i<phone_list.size(); i++)
			    	{
						PhoneObject phone_obj = (PhoneObject)phone_list.get(i);
						patient_obj.addPhone(phone_obj);
			    	}
				}
			
				patient_obj.resetAll();
				a_ret.add(patient_obj);
	    	}
		}
		catch (SQLException e)
		{
            ArrayList params = new ArrayList();
            for(int i=0; i<keys.length; i++)
            {
                params = addobject(params, keys[i]);
            }
            String sql = sql2str(mSelectString, params);
		    throw new OPSException(sql+e.getMessage());
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
            throw new OPSException ("PatientSBRDB: invalid parent key(s) in update()");
        }
                    
        PatientObject patient_obj = (PatientObject)node;
	    PreparedStatement stmt = null;
    	try
    	{
            log("updating PatientSBR");
        
    		if (!patient_obj.isAdded())
    		{
				if (patient_obj.isUpdated())
				{
				    stmt = getStatement(mUpdateString, conn);
					int count = 1;
					for(int i=0; i<keys.length; i++)
					{
					    stmt.setString(count++, keys[i]);
        	    	    log("    keys[" + i + "]: " + keys[i]);
					}
					setParam(stmt, count++, "String", patient_obj.getPatientId());
					setParam(stmt, count++, "String", patient_obj.getPersonCatCode());
					setParam(stmt, count++, "String", patient_obj.getFirstName());
					setParam(stmt, count++, "String", patient_obj.getFirstName_Std());
					setParam(stmt, count++, "String", patient_obj.getFirstName_Phon());
					setParam(stmt, count++, "String", patient_obj.getMiddleName());
					setParam(stmt, count++, "String", patient_obj.getLastName());
					setParam(stmt, count++, "String", patient_obj.getLastName_Std());
					setParam(stmt, count++, "String", patient_obj.getLastName_Phon());
					setParam(stmt, count++, "String", patient_obj.getSuffix());
					setParam(stmt, count++, "String", patient_obj.getTitle());
					setParam(stmt, count++, "String", patient_obj.getSSN());
					setParam(stmt, count++, "Date", patient_obj.getDOB());
					setParam(stmt, count++, "String", patient_obj.getDeath());
					setParam(stmt, count++, "String", patient_obj.getGender());
					setParam(stmt, count++, "String", patient_obj.getMStatus());
					setParam(stmt, count++, "String", patient_obj.getRace());
					setParam(stmt, count++, "String", patient_obj.getEthnic());
					setParam(stmt, count++, "String", patient_obj.getReligion());
					setParam(stmt, count++, "String", patient_obj.getLanguage());
					setParam(stmt, count++, "String", patient_obj.getSpouseName());
					setParam(stmt, count++, "String", patient_obj.getMotherName());
					setParam(stmt, count++, "String", patient_obj.getMotherMN());
					setParam(stmt, count++, "String", patient_obj.getFatherName());
					setParam(stmt, count++, "String", patient_obj.getMaiden());
					setParam(stmt, count++, "String", patient_obj.getPobCity());
					setParam(stmt, count++, "String", patient_obj.getPobState());
					setParam(stmt, count++, "String", patient_obj.getPobCountry());
					setParam(stmt, count++, "String", patient_obj.getVIPFlag());
					setParam(stmt, count++, "String", patient_obj.getVetStatus());
					setParam(stmt, count++, "String", patient_obj.getStatus());
					setParam(stmt, count++, "String", patient_obj.getDriverLicense());
					setParam(stmt, count++, "String", patient_obj.getDriverLicenseSt());
					setParam(stmt, count++, "Date", patient_obj.getDod());
					setParam(stmt, count++, "String", patient_obj.getDeathCertificate());
					setParam(stmt, count++, "String", patient_obj.getNationality());
					setParam(stmt, count++, "String", patient_obj.getCitizenship());
					stmt.setString(count++, patient_obj.getPatientId());
					
					stmt.executeUpdate();
					patient_obj.setUpdateFlag(false);
	    		}
	    	    else if (patient_obj.isRemoved())
	    	    {
			    	this.remove(conn, opsmap, patient_obj);
	    	    }

                if (!patient_obj.isRemoved()) {
    				ArrayList alias_list = (ArrayList)patient_obj.getAlias();
    				if (null != alias_list)
    				{
    	    			AliasSBRDB alias = (AliasSBRDB)opsmap.get("AliasSBRDB");
    	    			for(int i=0; i<alias_list.size(); i++)
    	    			{
    						AliasObject alias_obj = (AliasObject)alias_list.get(i);
                            if (!alias_obj.isAdded() && alias_obj.isKeyChanged() && alias_obj.isUpdated()) {
                                ObjectNode n = alias_obj.copy();
                                n.resetAll();
                                alias_obj.unChange();
                                node.deleteChild(alias_obj);
                                node.addChild(n);
    						    alias.remove(conn, opsmap, alias_obj);
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    alias.create(conn, opsmap, pid, n);
                            } else {
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    alias.update(conn, opsmap, pid, alias_obj);
    	    			    }
    	    			}
    				}

    				ArrayList address_list = (ArrayList)patient_obj.getAddress();
    				if (null != address_list)
    				{
    	    			AddressSBRDB address = (AddressSBRDB)opsmap.get("AddressSBRDB");
    	    			for(int i=0; i<address_list.size(); i++)
    	    			{
    						AddressObject address_obj = (AddressObject)address_list.get(i);
                            if (!address_obj.isAdded() && address_obj.isKeyChanged() && address_obj.isUpdated()) {
                                ObjectNode n = address_obj.copy();
                                n.resetAll();
                                address_obj.unChange();
                                node.deleteChild(address_obj);
                                node.addChild(n);
    						    address.remove(conn, opsmap, address_obj);
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    address.create(conn, opsmap, pid, n);
                            } else {
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    address.update(conn, opsmap, pid, address_obj);
    	    			    }
    	    			}
    				}

    				ArrayList phone_list = (ArrayList)patient_obj.getPhone();
    				if (null != phone_list)
    				{
    	    			PhoneSBRDB phone = (PhoneSBRDB)opsmap.get("PhoneSBRDB");
    	    			for(int i=0; i<phone_list.size(); i++)
    	    			{
    						PhoneObject phone_obj = (PhoneObject)phone_list.get(i);
                            if (!phone_obj.isAdded() && phone_obj.isKeyChanged() && phone_obj.isUpdated()) {
                                ObjectNode n = phone_obj.copy();
                                n.resetAll();
                                phone_obj.unChange();
                                node.deleteChild(phone_obj);
                                node.addChild(n);
    						    phone.remove(conn, opsmap, phone_obj);
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    phone.create(conn, opsmap, pid, n);
                            } else {
    						    String[] pid = new String[1];
    						    pid[0] = patient_obj.getPatientId();
    						    phone.update(conn, opsmap, pid, phone_obj);
    	    			    }
    	    			}
    				}

                }
	    	}
	    	else
	    	{
				this.create(conn, opsmap, keys, patient_obj);
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
                params = addobject(params, patient_obj.getPatientId());
                params = addobject(params, patient_obj.getPersonCatCode());
                params = addobject(params, patient_obj.getFirstName());
                params = addobject(params, patient_obj.getFirstName_Std());
                params = addobject(params, patient_obj.getFirstName_Phon());
                params = addobject(params, patient_obj.getMiddleName());
                params = addobject(params, patient_obj.getLastName());
                params = addobject(params, patient_obj.getLastName_Std());
                params = addobject(params, patient_obj.getLastName_Phon());
                params = addobject(params, patient_obj.getSuffix());
                params = addobject(params, patient_obj.getTitle());
                params = addobject(params, patient_obj.getSSN());
                params = addobject(params, patient_obj.getDOB());
                params = addobject(params, patient_obj.getDeath());
                params = addobject(params, patient_obj.getGender());
                params = addobject(params, patient_obj.getMStatus());
                params = addobject(params, patient_obj.getRace());
                params = addobject(params, patient_obj.getEthnic());
                params = addobject(params, patient_obj.getReligion());
                params = addobject(params, patient_obj.getLanguage());
                params = addobject(params, patient_obj.getSpouseName());
                params = addobject(params, patient_obj.getMotherName());
                params = addobject(params, patient_obj.getMotherMN());
                params = addobject(params, patient_obj.getFatherName());
                params = addobject(params, patient_obj.getMaiden());
                params = addobject(params, patient_obj.getPobCity());
                params = addobject(params, patient_obj.getPobState());
                params = addobject(params, patient_obj.getPobCountry());
                params = addobject(params, patient_obj.getVIPFlag());
                params = addobject(params, patient_obj.getVetStatus());
                params = addobject(params, patient_obj.getStatus());
                params = addobject(params, patient_obj.getDriverLicense());
                params = addobject(params, patient_obj.getDriverLicenseSt());
                params = addobject(params, patient_obj.getDod());
                params = addobject(params, patient_obj.getDeathCertificate());
                params = addobject(params, patient_obj.getNationality());
                params = addobject(params, patient_obj.getCitizenship());
                params = addobject(params, patient_obj.getPatientId());
    	        log("    PatientId: " + patient_obj.getPatientId());
    
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
        PatientObject patient_obj = (PatientObject) node;
	    PreparedStatement stmt = null;
		try
		{
            log("to remove PatientSBR");

		    ArrayList alias_list = (ArrayList)patient_obj.getAlias();
		    if (null != alias_list)
		    {
				AliasSBRDB alias = (AliasSBRDB)opsmap.get("AliasSBRDB");
				for(int i=0; i<alias_list.size(); i++)
				{
				    AliasObject alias_obj = (AliasObject)alias_list.get(i);
				    alias.remove(conn, opsmap, alias_obj);
				}
		    }
		    
		    ArrayList address_list = (ArrayList)patient_obj.getAddress();
		    if (null != address_list)
		    {
				AddressSBRDB address = (AddressSBRDB)opsmap.get("AddressSBRDB");
				for(int i=0; i<address_list.size(); i++)
				{
				    AddressObject address_obj = (AddressObject)address_list.get(i);
				    address.remove(conn, opsmap, address_obj);
				}
		    }
		    
		    ArrayList phone_list = (ArrayList)patient_obj.getPhone();
		    if (null != phone_list)
		    {
				PhoneSBRDB phone = (PhoneSBRDB)opsmap.get("PhoneSBRDB");
				for(int i=0; i<phone_list.size(); i++)
				{
				    PhoneObject phone_obj = (PhoneObject)phone_list.get(i);
				    phone.remove(conn, opsmap, phone_obj);
				}
		    }
		    
            stmt = getStatement(mDeleteString, conn);
		    stmt.setString(1, patient_obj.getPatientId());
	        log("    PatientId: " + patient_obj.getPatientId());
	        
		    stmt.executeUpdate();
		}
		catch (SQLException e)
		{
            String sql_err = e.getMessage();
            try
            {
                ArrayList params = new ArrayList();
                params = addobject(params, patient_obj.getPatientId());
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