/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * AdtJdbcConnection.java
 *
 * Created on October 4, 2004, 12:53 PM
 */
package com.vangent.hieos.adt.db;

import com.vangent.hieos.xutil.db.support.SQLConnectionWrapper;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 * For use in communicating with the ADT database.  At the moment, this is
 * hardcoded to talk to a PostgreSQL database, but this will become more
 * flexible in future releases.
 * @author Andrew McCaffrey
 */
public class AdtJdbcConnection {
    private final static Logger logger = Logger.getLogger(AdtJdbcConnection.class);

    private Connection con = null;
    private Statement stmt = null;
    /**
     * Constant representing the name of the ADT database table.
     */
    public static String ADT_MAIN_TABLE = "patient";
    /**
     *
     */
    public static String ADT_PATIENT_NAME_TABLE = "patientname";
    /**
     *
     */
    public static String ADT_PATIENT_ADDRESS_TABLE = "patientaddress";
    /**
     *
     */
    public static String ADT_PATIENT_RACE_TABLE = "patientrace";
    /**
     *
     */
    public static String ADT_MAIN_UUID = "uuid";
    /**
     * Constant representing the patient ID column in the ADT database table.
     */
    public static String ADT_MAIN_PATIENTID = "id";
    /**
     * 
     */
    public static String ADT_MAIN_BIRTHDATETIME = "birthdatetime";
    /**
     *
     */
    public static String ADT_MAIN_ADMIN_SEX = "adminsex";
    /**
     *
     */
    public static String ADT_MAIN_ACCOUNT_NUMBER = "accountnumber";
    /**
     *
     */
    public static String ADT_MAIN_BED_ID = "bedid";
    /**
     *
     */
    public static String ADT_PATIENTNAME_PARENT = "parent";
    /**
     *
     */
    public static String ADT_PATIENTNAME_FAMILY_NAME = "familyname";
    /**
     *
     */
    public static String ADT_PATIENTNAME_GIVEN_NAME = "givenname";
    /**
     *
     */
    public static String ADT_PATIENTNAME_SECOND_AND_FURTHER_NAME = "secondandfurthername";
    /**
     *
     */
    public static String ADT_PATIENTNAME_SUFFIX = "suffix";
    /**
     *
     */
    public static String ADT_PATIENTNAME_PREFIX = "prefix";
    /**
     *
     */
    public static String ADT_PATIENTNAME_DEGREE = "degree";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_PARENT = "parent";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_STREET_ADDRESS = "streetaddress";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_OTHER_DESIGNATION = "otherdesignation";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_CITY = "city";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_STATE_OR_PROVINCE = "stateorprovince";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_ZIPCODE = "zipcode";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_COUNTRY = "country";
    /**
     *
     */
    public static String ADT_PATIENTADDRESS_COUNTY_OR_PARISH = "countyorparish";
    /**
     *
     */
    public static String ADT_PATIENTRACE_PARENT = "parent";
    /**
     *
     */
    public static String ADT_PATIENTRACE_RACE = "race";

    /**
     * Creates a new instance of JdbcConnection
     * @throws java.sql.SQLException Thrown if database access error.
     * @throws XdsInternalException
     */
    public AdtJdbcConnection() throws java.sql.SQLException, XdsInternalException {
        this.initialize();
    }

    /**
     * 
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void initialize() throws XdsInternalException, SQLException {
        con = this.getConnection();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            logger.error("ADT: Could not create statement", ex);
            try {
                con.close();
                con = null;
            } catch (SQLException ex1) {
                logger.error("ADT: Could not close connection", ex1);
            }
            throw ex;
        }
    }

    /**
     *  Open ADT database connection.
     *
     * @return Database connection instance on success.  Null on failure.
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private Connection getConnection() throws XdsInternalException {
        return new SQLConnectionWrapper().getConnection(SQLConnectionWrapper.adtJNDIResourceName);
    }

    /**
     * Close the connection.
     */
    public void closeConnection() {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException e) {
            logger.error("ADT: Could not close statement", e);
        }
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            logger.error("ADT: Could not close connection", e);
        }
    }

    /**
     *
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    private ResultSet executeQuery(String sql) throws SQLException {
        ResultSet result = null;
        logger.trace("SQL(adt) = " + sql);
        result = stmt.executeQuery(sql);
        return result;
    }

    /**
     * Executes the SQL update to the database.
     * @param sql The SQL of the update.
     * @throws java.sql.SQLException Thrown if database access error.
     * @return An int representing the number of rows affected by update.  (If zero,
     * then no update occured.)
     */
    private int executeUpdate(String sql) throws SQLException {
        logger.trace("SQL(adt) = " + sql);
        return stmt.executeUpdate(sql);
    }

    /**
     *
     * @return
     */
    private String getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return df.format(new Date());
    }

    /**
     *
     * @param record
     * @return
     * @throws java.sql.SQLException
     */
    public boolean addAdtRecord(AdtRecord record) throws SQLException {
        // FIXME (BHT): This should really be fixed to enforce referential integrity.
        // Right now, the subordinate tables are updated before the parent (Main).

        // Store race data.
        Collection races = record.getPatientRace();
        Iterator itRace = races.iterator();
        while (itRace.hasNext()) {
            Hl7Race race = (Hl7Race) itRace.next();
            this.addHl7Race(race);
        }

        // Store patient name data.
        Collection names = record.getPatientNames();
        Iterator itName = names.iterator();
        while (itName.hasNext()) {
            Hl7Name name = (Hl7Name) itName.next();
            this.addHl7Name(name);
        }

        // Store address data.
        Collection addresses = record.getPatientAddresses();
        Iterator itAddress = addresses.iterator();
        while (itAddress.hasNext()) {
            Hl7Address address = (Hl7Address) itAddress.next();
            this.addHl7Address(address);
        }

        // First see if we are in INSERT or UPDATE mode.
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT uuid FROM ");
        sb.append(this.ADT_MAIN_TABLE);
        sb.append(" WHERE " + this.ADT_MAIN_UUID + " = '" + record.getUuid() + "'");
        sb.append(";");
        ResultSet resultSet = this.executeQuery(sb.toString());
        boolean updateMode = resultSet.first();
        sb = new StringBuffer();
        if (updateMode == false) {
            // Insert:
            sb.append("INSERT INTO ");
            sb.append(this.ADT_MAIN_TABLE);
            sb.append(" (" + this.ADT_MAIN_ACCOUNT_NUMBER + "," + this.ADT_MAIN_ADMIN_SEX + ",");
            sb.append(this.ADT_MAIN_BED_ID + "," + this.ADT_MAIN_BIRTHDATETIME + ",");
            sb.append(this.ADT_MAIN_PATIENTID + "," + this.ADT_MAIN_UUID + "," + "timestamp" + ")");
            sb.append(" VALUES ");
            sb.append("('" + record.getPatientAccountNumber() + "','" + record.getPatientAdminSex() + "','");
            sb.append(record.getPatientBedId() + "','" + record.getPatientBirthDateTime() + "','");
            sb.append(record.getPatientId() + "','" + record.getUuid() + "','" + getDate() + "');");
        } else {
            // Update:
            sb.append("UPDATE ");
            sb.append(this.ADT_MAIN_TABLE);
            sb.append(" SET ");
            sb.append(this.ADT_MAIN_ACCOUNT_NUMBER + " = " + "'" + record.getPatientAccountNumber() + "'").append(",");
            sb.append(this.ADT_MAIN_ADMIN_SEX + " = " + "'" + record.getPatientAdminSex() + "'").append(",");
            sb.append(this.ADT_MAIN_BED_ID + " = " + "'" + record.getPatientBedId() + "'").append(",");
            sb.append(this.ADT_MAIN_BIRTHDATETIME + " = " + "'" + record.getPatientBirthDateTime() + "'").append(",");
            sb.append("timestamp" + " = " + "'" + getDate() + "'");
            sb.append(" WHERE " + this.ADT_MAIN_UUID + " = '" + record.getUuid() + "'").append(";");
        }
        int rowsAffected = this.executeUpdate(sb.toString());
        return rowsAffected > 0;
    }

    /**
     *
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    private boolean addHl7Name(Hl7Name name) throws java.sql.SQLException {
        StringBuffer sb = new StringBuffer();

        // First see if we are in INSERT or UPDATE mode.
        sb.append("SELECT parent FROM ");
        sb.append(this.ADT_PATIENT_NAME_TABLE);
        sb.append(" WHERE " + this.ADT_PATIENTNAME_PARENT + " = '" + name.getParent() + "'");
        sb.append(";");
        ResultSet resultSet = this.executeQuery(sb.toString());
        boolean updateMode = resultSet.first();
        sb = new StringBuffer();
        if (updateMode == false) {
            // Insert:
            sb.append("INSERT INTO ");
            sb.append(this.ADT_PATIENT_NAME_TABLE);
            sb.append(" (" + this.ADT_PATIENTNAME_DEGREE + "," + this.ADT_PATIENTNAME_FAMILY_NAME + ",");
            sb.append(this.ADT_PATIENTNAME_GIVEN_NAME + "," + this.ADT_PATIENTNAME_PARENT + ",");
            sb.append(this.ADT_PATIENTNAME_PREFIX + "," + this.ADT_PATIENTNAME_SECOND_AND_FURTHER_NAME + ",");
            sb.append(this.ADT_PATIENTNAME_SUFFIX + ")");
            sb.append(" VALUES ");
            sb.append("('" + name.getDegree() + "','" + name.getFamilyName() + "','");
            sb.append(name.getGivenName() + "','" + name.getParent() + "','");
            sb.append(name.getPrefix() + "','" + name.getSecondAndFurtherName() + "','");
            sb.append(name.getSuffix() + "');");
        } else {
            // Update:
            sb.append("UPDATE ");
            sb.append(this.ADT_PATIENT_NAME_TABLE);
            sb.append(" SET ");
            sb.append(this.ADT_PATIENTNAME_DEGREE + " = " + "'" + name.getDegree() + "'").append(",");
            sb.append(this.ADT_PATIENTNAME_FAMILY_NAME + " = " + "'" + name.getFamilyName() + "'").append(",");
            sb.append(this.ADT_PATIENTNAME_GIVEN_NAME + " = " + "'" + name.getGivenName() + "'").append(",");
            sb.append(this.ADT_PATIENTNAME_PREFIX + " = " + "'" + name.getPrefix() + "'").append(",");
            sb.append(this.ADT_PATIENTNAME_SECOND_AND_FURTHER_NAME + " = " + "'" + name.getSecondAndFurtherName() + "'").append(",");
            sb.append(this.ADT_PATIENTNAME_SUFFIX + " = " + "'" + name.getSuffix() + "'");
            sb.append(" WHERE " + this.ADT_PATIENTNAME_PARENT + " = '" + name.getParent() + "'").append(";");
        }
        int rowsAffected = this.executeUpdate(sb.toString());
        return rowsAffected > 0;
    }

    /**
     *
     * @param address
     * @return
     * @throws java.sql.SQLException
     */
    private boolean addHl7Address(Hl7Address address) throws java.sql.SQLException {
        StringBuffer sb = new StringBuffer();

        // First see if we are in INSERT or UPDATE mode.
        sb.append("SELECT parent FROM ");
        sb.append(this.ADT_PATIENT_ADDRESS_TABLE);
        sb.append(" WHERE " + this.ADT_PATIENTADDRESS_PARENT + " = '" + address.getParent() + "'");
        sb.append(";");
        ResultSet resultSet = this.executeQuery(sb.toString());
        boolean updateMode = resultSet.first();
        sb = new StringBuffer();
        if (updateMode == false) {
            // Insert:
            sb.append("INSERT INTO ");
            sb.append(this.ADT_PATIENT_ADDRESS_TABLE);
            sb.append(" (" + this.ADT_PATIENTADDRESS_CITY + "," + this.ADT_PATIENTADDRESS_COUNTRY + ",");
            sb.append(this.ADT_PATIENTADDRESS_COUNTY_OR_PARISH + "," + this.ADT_PATIENTADDRESS_OTHER_DESIGNATION + ",");
            sb.append(this.ADT_PATIENTADDRESS_PARENT + "," + this.ADT_PATIENTADDRESS_STATE_OR_PROVINCE + ",");
            sb.append(this.ADT_PATIENTADDRESS_STREET_ADDRESS + "," + this.ADT_PATIENTADDRESS_ZIPCODE + ")");
            sb.append(" VALUES ");
            sb.append("('" + address.getCity() + "','" + address.getCountry() + "','");
            sb.append(address.getCountyOrParish() + "','" + address.getOtherDesignation() + "','");
            sb.append(address.getParent() + "','" + address.getStateOrProvince() + "','");
            sb.append(address.getStreetAddress() + "','" + address.getZipCode() + "');");
        } else {
            // Update:
            sb.append("UPDATE ");
            sb.append(this.ADT_PATIENT_ADDRESS_TABLE);
            sb.append(" SET ");
            sb.append(this.ADT_PATIENTADDRESS_CITY + " = " + "'" + address.getCity() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_COUNTRY + " = " + "'" + address.getCountry() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_COUNTY_OR_PARISH + " = " + "'" + address.getCountyOrParish() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_OTHER_DESIGNATION + " = " + "'" + address.getOtherDesignation() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_STATE_OR_PROVINCE + " = " + "'" + address.getStateOrProvince() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_STREET_ADDRESS + " = " + "'" + address.getStreetAddress() + "'").append(",");
            sb.append(this.ADT_PATIENTADDRESS_ZIPCODE + " = " + "'" + address.getZipCode() + "'");
            sb.append(" WHERE " + this.ADT_PATIENTADDRESS_PARENT + " = '" + address.getParent() + "'").append(";");
        }
        int rowsAffected = this.executeUpdate(sb.toString());
        return rowsAffected > 0;
    }

    /**
     *
     * @param race
     * @return
     * @throws java.sql.SQLException
     */
    private boolean addHl7Race(Hl7Race race) throws java.sql.SQLException {
        StringBuffer sb = new StringBuffer();
        // First see if we are in INSERT or UPDATE mode.
        sb.append("SELECT parent FROM ");
        sb.append(this.ADT_PATIENT_RACE_TABLE);
        sb.append(" WHERE " + this.ADT_PATIENTRACE_PARENT + " = '" + race.getParent() + "'");
        sb.append(";");
        ResultSet resultSet = this.executeQuery(sb.toString());
        boolean updateMode = resultSet.first();
        if (updateMode == false) {
            // Insert:
            sb.append("INSERT INTO ");
            sb.append(this.ADT_PATIENT_RACE_TABLE);
            sb.append(" (" + this.ADT_PATIENTRACE_PARENT + "," + this.ADT_PATIENTRACE_RACE + ")");
            sb.append(" VALUES ");
            sb.append("('" + race.getParent() + "','" + race.getRace() + "');");
        } else {
            // Update:
            // FIXME (BHT) -- do at some point.
        }
        int rowsAffected = this.executeUpdate(sb.toString());
        return rowsAffected > 0;
    }

    /**
     * Queries the database to see if the given ID already exists in the database.
     * This method does an exist, case-sensitive search only.  Use
     * getInexactMatch(String) for wild-cards.
     * @param id The ID to query on.
     * @throws java.sql.SQLException Thrown if database access error.
     * @return Boolean.  True if ID does exist.  False if ID does not exist.
     */
    public boolean doesIdExist(String id) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + this.ADT_MAIN_TABLE + " ");
        sb.append("WHERE " + this.ADT_MAIN_PATIENTID + " = '" + id + "';");
        ResultSet result = this.executeQuery(sb.toString());
        return result.next();
    }

    /**
     *
     * @param patientId
     * @return
     * @throws java.sql.SQLException
     */
    public String getPatientUUID(String patientId) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT UUID ");
        sb.append("FROM " + this.ADT_MAIN_TABLE + " ");
        sb.append("WHERE " + this.ADT_MAIN_PATIENTID + " = '" + patientId + "';");
        ResultSet result = this.executeQuery(sb.toString());
        if (result.first() == false) {
            // not found.
            return null;  // Early exit.
        }
        return result.getString(this.ADT_MAIN_UUID);
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.sql.SQLException
     */
    public Collection getPatientNames(String uuid) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + this.ADT_PATIENT_NAME_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTNAME_PARENT + " = '" + uuid + "';");
        ResultSet result = this.executeQuery(sb.toString());
        Collection names = new ArrayList();
        while (result.next()) {
            Hl7Name name = new Hl7Name();
            name.setParent(result.getString(this.ADT_PATIENTNAME_PARENT));
            name.setDegree(result.getString(this.ADT_PATIENTNAME_DEGREE));
            name.setFamilyName(result.getString(this.ADT_PATIENTNAME_FAMILY_NAME));
            name.setGivenName(result.getString(this.ADT_PATIENTNAME_GIVEN_NAME));
            name.setPrefix(result.getString(this.ADT_PATIENTNAME_PREFIX));
            name.setSecondAndFurtherName(result.getString(this.ADT_PATIENTNAME_SECOND_AND_FURTHER_NAME));
            name.setSuffix(result.getString(this.ADT_PATIENTNAME_SUFFIX));
            names.add(name);
        }
        return names;
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.sql.SQLException
     */
    public Collection getPatientRaces(String uuid) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + this.ADT_PATIENT_RACE_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTRACE_PARENT + " = '" + uuid + "';");
        ResultSet result = this.executeQuery(sb.toString());
        Collection races = new ArrayList();
        while (result.next()) {
            Hl7Race race = new Hl7Race();
            race.setParent(result.getString(this.ADT_PATIENTRACE_PARENT));
            race.setRace(result.getString(this.ADT_PATIENTRACE_RACE));
            races.add(race);
        }
        return races;
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.sql.SQLException
     */
    public Collection getPatientAddresses(String uuid) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + this.ADT_PATIENT_ADDRESS_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTADDRESS_PARENT + " = '" + uuid + "';");
        ResultSet result = this.executeQuery(sb.toString());
        Collection addresses = new ArrayList();
        while (result.next()) {
            Hl7Address address = new Hl7Address();
            address.setParent(result.getString(this.ADT_PATIENTADDRESS_PARENT));
            address.setCity(result.getString(this.ADT_PATIENTADDRESS_CITY));
            address.setCountry(result.getString(this.ADT_PATIENTADDRESS_COUNTRY));
            address.setCountyOrParish(result.getString(this.ADT_PATIENTADDRESS_COUNTY_OR_PARISH));
            address.setOtherDesignation(result.getString(this.ADT_PATIENTADDRESS_OTHER_DESIGNATION));
            address.setStateOrProvince(result.getString(this.ADT_PATIENTADDRESS_STATE_OR_PROVINCE));
            address.setStreetAddress(result.getString(this.ADT_PATIENTADDRESS_STREET_ADDRESS));
            address.setZipCode(result.getString(this.ADT_PATIENTADDRESS_ZIPCODE));
            addresses.add(address);
        }
        return addresses;
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public AdtRecord getAdtRecord(String uuid) throws SQLException, XdsInternalException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM " + this.ADT_MAIN_TABLE + " ");
        sb.append("WHERE " + this.ADT_MAIN_UUID + " = '" + uuid + "';");
        ResultSet result = this.executeQuery(sb.toString());
        try {
            result.next();
        } catch (SQLException e) {
            // not found.
            return null;
        }
        AdtRecord record = new AdtRecord();
        record.setUuid(uuid);
        record.setPatientAccountNumber(result.getString(this.ADT_MAIN_ACCOUNT_NUMBER));
        record.setPatientAdminSex(result.getString(this.ADT_MAIN_ADMIN_SEX));
        record.setPatientBedId(result.getString(this.ADT_MAIN_BED_ID));
        record.setPatientBirthDateTime(result.getString(this.ADT_MAIN_BIRTHDATETIME));
        record.setPatientId(result.getString(this.ADT_MAIN_PATIENTID));
        record.setPatientAddresses(this.getPatientAddresses(uuid));
        record.setPatientNames(this.getPatientNames(uuid));
        record.setPatientRace(this.getPatientRaces(uuid));
        return record;
    }

    /**
     * 
     * @param uuid
     * @throws SQLException
     */
    public void deleteAdtRecord(String uuid) throws SQLException {
        StringBuffer sb;
        int rowsAffected;

        // Delete the main table entries.
        sb = new StringBuffer();
        sb.append("DELETE ");
        sb.append("FROM " + this.ADT_MAIN_TABLE + " ");
        sb.append("WHERE " + this.ADT_MAIN_UUID + " = '" + uuid + "';");
        rowsAffected = this.executeUpdate(sb.toString());

        // Delete the address table entries.
        sb = new StringBuffer();
        sb.append("DELETE ");
        sb.append("FROM " + this.ADT_PATIENT_ADDRESS_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTADDRESS_PARENT + " = '" + uuid + "';");
        rowsAffected = this.executeUpdate(sb.toString());

        // Delete the name table entries.
        sb = new StringBuffer();
        sb.append("DELETE ");
        sb.append("FROM " + this.ADT_PATIENT_NAME_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTNAME_PARENT + " = '" + uuid + "';");
        rowsAffected = this.executeUpdate(sb.toString());

        // Delete the race table entries.
        sb = new StringBuffer();
        sb.append("DELETE ");
        sb.append("FROM " + this.ADT_PATIENT_RACE_TABLE + " ");
        sb.append("WHERE " + this.ADT_PATIENTRACE_PARENT + " = '" + uuid + "';");
        rowsAffected = this.executeUpdate(sb.toString());
    }

    /**
     *
     * @return
     */
    public static String getCurrentTimestamp() {
        return new Date().toString();
    }
}
