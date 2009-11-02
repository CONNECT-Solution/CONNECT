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
 * AdtRecord.java
 *
 * Created on January 6, 2005, 4:20 PM
 */
package com.vangent.hieos.adt.db;

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.uuid.UuidAllocator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Currently for the purposes of XDS, an ADT patient record consists of only a
 * patient ID and the patient's name.  This class gives access to both pieces of
 * information.
 * July 2005 -- we're adding to it now. Now it is more than just id/name.
 * @author Andrew McCaffrey
 */
public class AdtRecord {
    private final static Logger logger = Logger.getLogger(AdtRecord.class);

    private String uuid = null;
    private String patientId = null;
    private String timestamp = null;
    private Collection patientNames = null;             // Collection of Hl7Name
    private String patientBirthDateTime = null;
    private String patientAdminSex = null;
    private String patientAccountNumber = null;
    private String patientBedId = null;
    private Collection patientRace = null;              // Collection of Hl7Race
    private Collection patientAddresses = null;         // Collection of Hl7Address

    /**
     * 
     */
    public AdtRecord() {
        this.generateAndSetNewUuid();
    }

    /**
     * Creates a new instance of AdtRecord, using parameters for patient ID
     * and patient name.
     * @param patientId The patient ID to set.
     * @param patientNames
     */
    public AdtRecord(String patientId, Collection patientNames) {
        this.setPatientId(patientId);
        this.setPatientNames(patientNames);
        this.generateAndSetNewUuid();
    }

    /**
     *
     * @param patientId
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public AdtRecord(String patientId) throws SQLException, XdsInternalException {
        this.setPatientId(patientId);
        this.generateAndSetNewUuid();
    }

    /**
     *
     * @param patientId
     * @param patientNames
     * @param patientBirthDateTime
     * @param patientAdminSex
     * @param patientAddresses
     * @param patientAccountNumber
     * @param patientRace
     * @param patientBedId
     */
    public AdtRecord(String patientId, Collection patientNames, String patientBirthDateTime, String patientAdminSex,
            Collection patientAddresses, String patientAccountNumber, Collection patientRace, String patientBedId) {

        this.setPatientId(patientId);
        this.setPatientNames(patientNames);
        this.setPatientBirthDateTime(patientBirthDateTime);
        this.setPatientAdminSex(patientAdminSex);
        this.setPatientAddresses(patientAddresses);
        this.setPatientRace(patientRace);
        this.setPatientAccountNumber(patientAccountNumber);
        this.setPatientBedId(patientBedId);
        this.generateAndSetNewUuid();
    }

    /**
     * Getter for property patientId.
     * @return Value of property patientId.
     */
    public java.lang.String getPatientId() {
        return patientId;
    }

    /**
     * Setter for property patientId.
     * @param patientId New value of property patientId.
     */
    public void setPatientId(java.lang.String patientId) {
        this.patientId = patientId;
    }

    /**
     * Getter for property patientName.
     * @return Value of property patientName.
     */
    public Collection getPatientNames() {
        if (patientNames == null) {
            patientNames = new ArrayList();
        }

        return patientNames;
    }

    /**
     * Setter for property patientName.
     * @param patientNames
     */
    public void setPatientNames(Collection patientNames) {
        this.patientNames = patientNames;
    }

    /**
     *
     * @return
     */
    public String getPatientBirthDateTime() {
        return patientBirthDateTime;
    }

    /**
     *
     * @param patientBirthDateTime
     */
    public void setPatientBirthDateTime(String patientBirthDateTime) {
        this.patientBirthDateTime = patientBirthDateTime;
    }

    /**
     *
     * @return
     */
    public String getPatientAdminSex() {
        return patientAdminSex;
    }

    /**
     *
     * @param patientAdminSex
     */
    public void setPatientAdminSex(String patientAdminSex) {
        this.patientAdminSex = patientAdminSex;
    }

    /**
     *
     * @return
     */
    public Collection getPatientAddresses() {
        if (patientAddresses == null) {
            patientAddresses = new ArrayList();
        }
        return patientAddresses;
    }

    /**
     *
     * @param patientAddresses
     */
    public void setPatientAddresses(Collection patientAddresses) {
        this.patientAddresses = patientAddresses;
    }

    /**
     *
     * @return
     */
    public String getPatientAccountNumber() {
        return patientAccountNumber;
    }

    /**
     *
     * @param patientAccountNumber
     */
    public void setPatientAccountNumber(String patientAccountNumber) {
        this.patientAccountNumber = patientAccountNumber;
    }

    /**
     *
     * @return
     */
    public String getPatientBedId() {
        return patientBedId;
    }

    /**
     *
     * @param patientBedId
     */
    public void setPatientBedId(String patientBedId) {
        this.patientBedId = patientBedId;
    }

    /**
     * 
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Patient ID = " + this.getPatientId() + '\n');
        //       sb.append("Patient Name = " + this.getFullName().getPrefix() + " " + this.getFullName().getGivenName() + " ");
        //       sb.append(this.getFullName().getSecondAndFurtherName() + " " + this.getFullName().getFamilyName() + " ");
        //       sb.append(this.getFullName().getSuffix() + " " + this.getFullName().getDegree() + '\n');
        sb.append("Patient Birth Date/Time = " + this.getPatientBirthDateTime() + '\n');
        sb.append("Patient Admin Sex = " + this.getPatientAdminSex() + '\n');
        //       sb.append("Patient Address = " + this.getPatientAddress() + '\n');
        sb.append("Patient Account Number = " + this.getPatientAccountNumber() + '\n');
        sb.append("Patient Bed ID = " + this.getPatientBedId() + '\n');
        for (int i = 0; i < this.getPatientRace().size(); i++) {
            sb.append("Patient Race #" + (i + 1) + " = " + this.getPatientRace().toArray()[i] + '\n');
        }
        for (int i = 0; i < this.getPatientAddresses().size(); i++) {
            Hl7Address address = (Hl7Address) this.getPatientAddresses().toArray()[i];
            sb.append("Patient Address #" + (i + 1) + " = " + address.toString());
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public Collection getPatientRace() {
        if (patientRace == null) {
            patientRace = new ArrayList();
        }
        return patientRace;
    }

    /**
     *
     * @param patientRace
     */
    public void setPatientRace(Collection patientRace) {
        this.patientRace = patientRace;
    }

    /**
     *
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    /**
     *
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     *
     */
    public void generateAndSetNewUuid() {
        this.setUuid(AdtRecord.getFreshUuid());
    }

    /**
     *
     * @return
     */
    static public String getFreshUuid() {
        /*
        UUID newUuid = AdtRecord.getUuidFactory().newUUID();
        return "urn:uuid:" + newUuid.toString();*/
        return UuidAllocator.allocate();
    }

    /**
     *
     * @return
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public void saveToDatabase() throws XdsInternalException, SQLException {
        if (timestamp == null) {
            timestamp = new Date().toString();
        }
        AdtJdbcConnection con = new AdtJdbcConnection();
        try {
            con.addAdtRecord(this);
        } catch (SQLException e) {
            logger.error("Problem adding ADT record", e);
            throw e;
        } finally {
            con.closeConnection();
        }
    }
}
