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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.services.xds.registry.transactions;

import com.vangent.hieos.xutil.db.support.SQLConnectionWrapper;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.adt.db.AdtRecordBean;
import com.vangent.hieos.adt.db.Hl7Address;
import com.vangent.hieos.adt.db.Hl7Name;
import com.vangent.hieos.adt.db.AdtJdbcConnection;
import com.vangent.hieos.xutil.hl7.date.Hl7Date;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.uuid.UuidAllocator;

// XConfig.
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigRegistry;

// ATNA.
import com.vangent.hieos.xutil.atna.XATNALogger;

// Third-party.
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import java.sql.Connection;
import java.sql.PreparedStatement;

// Exceptions.
import com.vangent.hieos.xutil.exception.XdsInternalException;
import org.jaxen.JaxenException;
import java.sql.SQLException;

/**
 *
 * @author Bernie Thuman
 */
public class RegistryPatientIdentityFeed extends XBaseTransaction {

    private final static Logger logger = Logger.getLogger(RegistryPatientIdentityFeed.class);

    // Type type of message received.
    public enum MessageType {

        PatientRegistryRecordAdded,
        PatientRegistryRecordUpdated,
        PatientRegistryDuplicatesResolved
    };
    // XPath expressions:
    private final static String XPATH_PATIENT =
            "//*/ns:controlActProcess/ns:subject/ns:registrationEvent/ns:subject1/ns:patient[1]";
    private final static String XPATH_PRIOR_REGISTRATION_PATIENT_ID =
            "//*/ns:controlActProcess/ns:subject/ns:registrationEvent/ns:replacementOf/ns:priorRegistration/ns:subject1/ns:priorRegisteredRole/ns:id[1]";

    //private Message log_message = null;
    private String xconfRegistryName = "localregistry";
    private boolean errorDetected = false;
    private String patientId = "NOT ON REQUEST";

    /**
     * 
     * @param log_message
     */
    public RegistryPatientIdentityFeed(String xconfRegistryName, XLogMessage log_message) {
        this.log_message = log_message;
        this.xconfRegistryName = xconfRegistryName;
    }

    /**
     *
     * @param request
     * @param messageType
     * @return
     */
    public OMElement run(OMElement request, MessageType messageType) {
        OMElement result = null;
        Exception ex = null;
        boolean updateMode = true;
        try {
            switch (messageType) {
                case PatientRegistryRecordAdded:
                    updateMode = false;
                    this.processPatientRegistryRecordAdded(request);
                    break;
                case PatientRegistryRecordUpdated:
                    this.processPatientRegistryRecordUpdated(request);
                    break;
                case PatientRegistryDuplicatesResolved:
                    this.processPatientRegistyDuplicatesResolved(request);
                    break;
            }
            this.logResponse(result, !this.errorDetected /* success */);
        } catch (PatientIdentityFeedException feedException) {
            ex = feedException;
        } catch (XdsInternalException internalException) {
            ex = internalException;
        } catch (Exception e) {
            ex = e;
            this.logException(e.getMessage());  // Some lower level exception.
        }

        // Generate the response.
        result = this.generateACK(request, (ex != null) ? ex.getMessage() : null);
        this.logResponse(result, !this.errorDetected /* success */);

        // ATNA log (Start)
        OMElement idNode = this.getFirstChildWithName(request, "id");
        String messageId = (idNode != null) ? idNode.getAttributeValue(new QName("root")) : "UNKNOWN";
        this.logPatientIdentityFeedToATNA(
                this.patientId,
                (messageId != null) ? messageId : "UNKNOWN",
                updateMode /* updateMode */,
                this.errorDetected ? XATNALogger.OutcomeIndicator.MINOR_FAILURE : XATNALogger.OutcomeIndicator.SUCCESS);
        // ATNA log (Stop)
        return result;
    }

    /**
     *
     * @param PRPA_IN201301UV02_Message
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void processPatientRegistryRecordAdded(OMElement PRPA_IN201301UV02_Message)
            throws PatientIdentityFeedException, XdsInternalException {
        // Pull out the patient from the request.
        OMElement patientNode = this.selectSingleNode(PRPA_IN201301UV02_Message, this.XPATH_PATIENT);
        OMElement idNode = this.getFirstChildWithName(patientNode, "id");
        if (idNode != null) {
            this.patientId = this.getPatientIdFromIIType(idNode);
            this.logInfo("Patient ID", this.patientId);

            // First see if the patient id already exists.
            if (!this.adtPatientExists(this.patientId)) {
                this.adtUpdate(patientNode, this.patientId, false /* updateMode */);
            } else {
                // Patient Id already exists (ignore request).
                throw this.logException("Patient ID " + this.patientId + " already exists - skipping ADD!");
            }
        } else {
            throw this.logException("No patient id found on request");
        }
    }

    /**
     *
     * @param PRPA_IN201302UV02_Message
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void processPatientRegistryRecordUpdated(OMElement PRPA_IN201302UV02_Message)
            throws PatientIdentityFeedException, XdsInternalException {
        // Pull out the patient from the request.
        OMElement patientNode = this.selectSingleNode(PRPA_IN201302UV02_Message, this.XPATH_PATIENT);
        OMElement idNode = this.getFirstChildWithName(patientNode, "id");
        if (idNode != null) {
            this.patientId = this.getPatientIdFromIIType(idNode);
            this.logInfo("Patient ID", this.patientId);

            // First see if the patient id already exists.
            if (this.adtPatientExists(this.patientId)) {
                this.adtUpdate(patientNode, this.patientId, true /* updateMode */);
            } else {
                // Patient Id already exists (ignore request).
                throw this.logException("Patient ID " + this.patientId + " does not exist - skipping UPDATE!");
            }
        } else {
            throw this.logException("No patient id found on request");
        }
    }

    /**
     *
     * @param PRPA_IN201304UV02_Message
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void processPatientRegistyDuplicatesResolved(OMElement PRPA_IN201304UV02_Message)
            throws PatientIdentityFeedException, XdsInternalException {
        OMElement patientNode = this.selectSingleNode(PRPA_IN201304UV02_Message, this.XPATH_PATIENT);
        OMElement idNode = this.getFirstChildWithName(patientNode, "id");
        String survivingPatientId = this.getPatientIdFromIIType(idNode);
        // Get the patient Id that will be subsumed (this is the duplicate).
        String priorRegistrationPatientId = this.getPriorRegistrationPatientId(PRPA_IN201304UV02_Message);

        // Check existance of surviving patient id on request.
        if (survivingPatientId == null) {
            throw this.logException("Surviving Patient ID not present on request- skipping MERGE!");
        }
        this.patientId = survivingPatientId;
        this.logInfo("Patient ID (Surviving)", survivingPatientId);

        // Check existance of priorRegistration on request.
        if (priorRegistrationPatientId == null) {
            throw this.logException("Prior Registration (to be subsumed) Patient ID not present on request - skipping MERGE!");
        }
        this.logInfo("Prior Registration Patient ID (Subsumed)", priorRegistrationPatientId);

        // See if they are the same patient (if so, skip merge).
        if (survivingPatientId.equals(priorRegistrationPatientId)) {
            throw this.logException("Surviving Patient ID and Prior Registration (to be subsumed) ID are the same - skipping MERGE!");
        }

        // We better have knowledge of both patient identifiers:
        if (!this.adtPatientExists(survivingPatientId)) {
            throw this.logException("Surviving Patient ID " + survivingPatientId + " not known to registry - skipping MERGE!");
        }

        if (!this.adtPatientExists(priorRegistrationPatientId)) {
            throw this.logException("Prior Registration (to be subsumed) Patient ID " + priorRegistrationPatientId + " not known to registry - skipping MERGE!");
        }

        // Now, we can do some updates.

        // First, take care of the registry by updating external identifiers.
        this.regUpdateExternalIdentifiers(survivingPatientId, priorRegistrationPatientId);

        // FIXME (Should just MARK): Delete the ADT record for the priorRegistrationPatientId.
        this.adtDeletePatientId(priorRegistrationPatientId);
    }

// Helper methods:
    /**
     *
     * @param idNode
     * @return
     */
    String getPatientIdFromIIType(OMElement idNode) {
        if (idNode == null) {
            return null;  // GUARD: Early exit.
        }
        String assigningAuthorityName = idNode.getAttributeValue(new QName("assigningAuthorityName"));
        String extension = idNode.getAttributeValue(new QName("extension"));
        String root = idNode.getAttributeValue(new QName("root"));
        String patientId = this.formattedPatientId(root, extension);
        String assigningAuthority = this.formattedAssigningAuthority(root);
        // DEBUG (Start)
        logger.info("  assigningAuthorityName = " + assigningAuthorityName);
        logger.info("  extension = " + extension);
        logger.info("  root = " + root);
        logger.info("*** AA = " + assigningAuthority);
        logger.info("*** Patient ID = " + patientId);
        // DEBUG (Stop)
        return patientId;
    }

    /**
     *
     * @param rootNode
     * @return
     */
    String getPriorRegistrationPatientId(OMElement rootNode) {
        OMElement idNode = this.selectSingleNode(rootNode, this.XPATH_PRIOR_REGISTRATION_PATIENT_ID);
        String patientId = null;
        if (idNode != null) {
            patientId = this.getPatientIdFromIIType(idNode);
        }
        return patientId;
    }

    /**
     * NOTE (BHT): Obviously, this routine is long and leborious.  Should change over to a light-weight
     * HL7 v3 Java Library (JAXB generated code may be an option, but on first attempt, code generated
     * was massive.
     *
     * @param request
     * @return
     */
    private OMElement generateACK(OMElement request, String errorString) {
        OMFactory omfactory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = omfactory.createOMNamespace("urn:hl7-org:v3", "ns");

        // MCCI_IN000002UV01:
        OMElement ackResponseNode = omfactory.createOMElement("MCCI_IN000002UV01", ns);
        ackResponseNode.addAttribute("ITSVersion", "XML_1.0", null);

        // /MCCI_IN000002UV01/id:
        OMElement idNode = omfactory.createOMElement("id", ns);
        ackResponseNode.addChild(idNode);
        idNode.addAttribute("root", this.getUUID(), null);

        // /MCCI_IN000002UV01/creationTime:
        OMElement creationTimeNode = omfactory.createOMElement("creationTime", ns);
        ackResponseNode.addChild(creationTimeNode);
        creationTimeNode.addAttribute("value", this.getHL7Date(), null);

        /* Transmission Wrapper */

        // /MCCI_IN000002UV01/versionCode (OK):
        OMElement versionCodeNode = omfactory.createOMElement("versionCode", ns);
        ackResponseNode.addChild(versionCodeNode);
        versionCodeNode.addAttribute("code", "V3PR1", null);  // Denotes HL7v3.

        // /MCCI_IN000002UV01/interactionId (?):
        OMElement interactionIdNode = omfactory.createOMElement("interactionId", ns);
        ackResponseNode.addChild(interactionIdNode);
        interactionIdNode.addAttribute("displayable", "true", null);
        interactionIdNode.addAttribute("extension", "MCCI_IN000002UV01", null);
        interactionIdNode.addAttribute("root", "2.16.840.1.113883", null);  // Denotes an HL7v3 interaction.

        // /MCCI_IN000002UV01/processingCode (?):
        OMElement processingCodeNode = omfactory.createOMElement("processingCode", ns);
        ackResponseNode.addChild(processingCodeNode);
        processingCodeNode.addAttribute("code", "P", null);  // ????? Should value be "T"?

        // /MCCI_IN000002UV01/processingModeCode (OK):
        OMElement processingModeCodeNode = omfactory.createOMElement("processingModeCode", ns);
        ackResponseNode.addChild(processingModeCodeNode);
        processingModeCodeNode.addAttribute("code", "T", null);

        // //MCCI_IN000002UV01/acceptAckCode (OK):
        OMElement acceptAckCodeNode = omfactory.createOMElement("acceptAckCode", ns);
        ackResponseNode.addChild(acceptAckCodeNode);
        acceptAckCodeNode.addAttribute("code", "NE", null);

        // Build "Receiver" (Patient Identity Source):
        OMElement senderOnRequestNode = MetadataSupport.firstChildWithLocalName(request, "sender");
        OMElement senderDeviceOnRequestNode =
                MetadataSupport.firstChildWithLocalName(senderOnRequestNode, "device");

        // /MCCI_IN000002UV01/receiver
        OMElement receiverNode = omfactory.createOMElement("receiver", ns);
        ackResponseNode.addChild(receiverNode);
        receiverNode.addAttribute("typeCode", "RCV", null);

        // /MCCI_IN000002UV01/receiver/device
        receiverNode.addChild(senderDeviceOnRequestNode);  // Sender is now receiver in ACK.

        // Build "Sender" (the Registry):

        // /MCCI_IN000002UV01/sender
        OMElement senderNode = omfactory.createOMElement("sender", ns);
        ackResponseNode.addChild(senderNode);
        senderNode.addAttribute("typeCode", "SND", null);

        // Add "device" for Sender:
        // /MCCI_IN000002UV01/sender/device
        OMElement deviceNode = omfactory.createOMElement("device", ns);
        senderNode.addChild(deviceNode);
        deviceNode.addAttribute("classCode", "DEV", null);
        deviceNode.addAttribute("determinerCode", "INSTANCE", null);

        // /MCCI_IN000002UV01/sender/device/id
        idNode = omfactory.createOMElement("id", ns);
        deviceNode.addChild(idNode);
        idNode.addAttribute("root", this.getRegistryReceiverDeviceId(), null);

        // /MCCI_IN000002UV01/sender/device/name
        OMElement nameNode = omfactory.createOMElement("name", ns);
        deviceNode.addChild(nameNode);
        nameNode.setText(this.getRegistryReceiverDeviceName());

        // /MCCI_IN000002UV01/acknowledgement:
        OMElement ackNode = omfactory.createOMElement("acknowledgement", ns);
        ackResponseNode.addChild(ackNode);

        // /MCCI_IN000002UV01/acknowledgement/typeCode
        OMElement typeCodeNode = omfactory.createOMElement("typeCode", ns);
        ackNode.addChild(typeCodeNode);
        if (errorString == null) {
            // Accept Acknoweledgement Commit Accept
            typeCodeNode.addAttribute("code", "CA", null);
        } else {
            // Accept Acknoweledgement Commit Error
            typeCodeNode.addAttribute("code", "CE", null);
        }

        // /MCCI_IN000002UV01/acknowledgement/targetMessage
        OMElement targetMessageNode = omfactory.createOMElement("targetMessage", ns);
        ackNode.addChild(targetMessageNode);

        // /MCCI_IN000002UV01/acknowledgement/targetMessage/id
        // Need to put in the "id" from the request in the ACK.
        OMElement idNodeOnRequest = MetadataSupport.firstChildWithLocalName(request, "id");
        targetMessageNode.addChild(idNodeOnRequest);

        // FOR ERROR REPORTING:
        if (errorString != null) {

            // /MCCI_IN000002UV01/acknowledgement/acknowledgementDetail
            OMElement acknowledgementDetail = omfactory.createOMElement("acknowledgementDetail", ns);
            ackNode.addChild(acknowledgementDetail);

            // /MCCI_IN000002UV01/acknowledgement/acknowledgementDetail/text
            OMElement textNode = omfactory.createOMElement("text", ns);
            acknowledgementDetail.addChild(textNode);
            textNode.setText(errorString);
        }
        return ackResponseNode;
    }

    /**
     *
     * @return  Value of the property, null if not found.
     */
    private String getRegistryReceiverDeviceId() {
        return this.getXConfigRegistryProperty("ReceiverDeviceId");
    }

    /**
     *
     * @return  Value of the property, null if not found.
     */
    private String getRegistryReceiverDeviceName() {
        return this.getXConfigRegistryProperty("ReceiverDeviceName");
    }

    /**
     *
     * @param rootNode
     * @param xpathExpression
     * @return
     */
    private OMElement selectSingleNode(OMElement rootNode, String xpathExpression) {
        OMElement resultNode = null;
        try {
            AXIOMXPath xpath = new AXIOMXPath(xpathExpression);
            xpath.addNamespace("ns", "urn:hl7-org:v3");
            resultNode = (OMElement) xpath.selectSingleNode(rootNode);
            if (resultNode != null) {
                logger.info("*** Found node for XPATH: " + xpathExpression);
            } else {
                logger.error("*** Could not find node for XPATH: " + xpathExpression);
            }
            return resultNode;
        } catch (JaxenException e) {
            this.logInternalException(e, "Problem with xpathExpression: " + xpathExpression);
        }
        return resultNode;
    }

    /**
     *
     * @param rootNode
     * @param localName
     * @return
     */
    private OMElement getFirstChildWithName(OMElement rootNode, String localName) {
        OMElement resultNode = null;
        if (rootNode != null) {
            resultNode = MetadataSupport.firstChildWithLocalName(rootNode, localName);
        }
        return resultNode;
    }

    /**
     *
     * @param domain
     * @param pid
     * @return
     */
    private String formattedPatientId(String domain, String pid) {
        // fd8c812cae1645e^^^&1.3.6.1.4.1.21367.2009.1.2.315&ISO
        return (pid + "^^^" + this.formattedAssigningAuthority(domain));
    }

    /**
     *
     * @param domain
     * @return
     */
    private String formattedAssigningAuthority(String domain) {
        //fd8c812cae1645e^^^&1.3.6.1.4.1.21367.2009.1.2.315&ISO
        return "&" + domain + "&ISO";
    }

    /**
     * Return a UUID.
     *
     * @return The UUID as a String.
     */
    private String getUUID() {
        return UuidAllocator.allocate();
    /*
    UUIDFactory factory = UUIDFactory.getInstance();
    UUID uuid = factory.newUUID();
    return uuid.toString(); */
    }

// ADT methods (keep here for now, but should move ultimately into a well factored structure.
    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private AdtJdbcConnection adtGetDatabaseConnection() throws XdsInternalException {
        // Open the connection to the ADT database.
        AdtJdbcConnection conn = null;
        try {
            conn = new AdtJdbcConnection();
        } catch (Exception e) {
            throw this.logInternalException(e, "ADT EXCEPTION: Problem getting ADT database connection");
        }
        return conn;
    }

    /**
     *
     * @param patientId
     */
    private void adtDeletePatientId(String patientId) throws XdsInternalException {
        String uuid = this.adtGetPatientUUID(patientId);
        AdtJdbcConnection conn = this.adtGetDatabaseConnection();
        try {
            conn.deleteAdtRecord(uuid);
        } catch (SQLException e) {
            throw this.logInternalException(e, "ADT EXCEPTION: Problem with deleting patient ID = " + patientId);
        } finally {
            conn.closeConnection();
        }
    }

    /**
     *
     * @param patientNode
     * @param patientId
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void adtUpdate(OMElement patientNode, String patientId, boolean updateMode) throws XdsInternalException {
        // Get the ADT record:
        AdtRecordBean arb;
        String uuid;
        // Are we in "update" mode (i.e. patient id exists already)
        if (updateMode == true) {
            uuid = this.adtGetPatientUUID(patientId);  // Get existing UUID.
            arb = new AdtRecordBean();
            arb.setPatientUUID(uuid);  // Force UUID from the previous db query.
        } else {
            // We are in "insert mode" (i.e. patient id does not exist)
            arb = new AdtRecordBean();
            uuid = arb.getPatientUUID();  // This is a new UUID.
        }
        arb.setPatientId(patientId);  // Set the patient id.

        // Get the demographic data.
        OMElement patientPersonNode = this.getFirstChildWithName(patientNode, "patientPerson");
        if (patientPersonNode == null) {
            this.logInfo("Note", "Request does not contain <patientPerson>");
        // Just keep going since we do have a patient id.
        } else {
            // Update patient demographic data.
            this.adtSetPatientBirthTime(patientPersonNode, arb);
            this.adtSetPatientName(patientPersonNode, uuid, arb);
            this.adtSetPatientAddress(patientPersonNode, uuid, arb);
        }
        // Store (which should have at least the patient id) to the database.
        this.adtSavePatientRecord(arb);
    }

    /**
     *
     * @param patientPersonNode
     * @param arb
     */
    private void adtSetPatientName(OMElement patientPersonNode, String parentUUID, AdtRecordBean arb) {
        OMElement nameNode = this.getFirstChildWithName(patientPersonNode, "name");
        if (nameNode == null) {
            this.logInfo("Note", "Request does not contain patient name");
            return; // Early exit.
        }
        OMElement givenNameNode = this.getFirstChildWithName(nameNode, "given");
        OMElement familyNameNode = this.getFirstChildWithName(nameNode, "family");
        String familyName = (familyNameNode != null) ? familyNameNode.getText() : null;
        String givenName = (givenNameNode != null) ? givenNameNode.getText() : null;
        if ((familyName != null) || (givenName != null)) {
            Hl7Name hl7Name = new Hl7Name(parentUUID);
            hl7Name.setFamilyName(familyName);
            hl7Name.setGivenName(givenName);
            arb.setAddName(hl7Name);
        } else {
            this.logInfo("Note", "Request does not contain patient name");
        }
    }

    /**
     * 
     * @param patientPersonNode
     * @param arb
     */
    private void adtSetPatientBirthTime(OMElement patientPersonNode, AdtRecordBean arb) {
        OMElement birthDateNode = this.getFirstChildWithName(patientPersonNode, "birthTime");
        if (birthDateNode == null) {
            this.logInfo("Note", "Request does not contain birthTime");
            return; // Early exit.
        }
        String birthTime = birthDateNode.getAttributeValue(new QName("value"));
        if (birthTime != null) {
            arb.setPatientBirthDateTime(birthTime);
        } else {
            this.logInfo("Note", "Request does not contain birthTime");
        }
    }

    /**
     *
     * @param patientPersonNode
     * @param arb
     */
    private void adtSetPatientAddress(OMElement patientPersonNode, String parentUUID, AdtRecordBean arb) {
        OMElement addrNode = this.getFirstChildWithName(patientPersonNode, "addr");
        if (addrNode == null) {
            this.logInfo("Note", "Request does not contain patient address");
            return;  // Early exit.
        }
        // Pull out address component parts:

        OMElement streetAddressLineNode = this.getFirstChildWithName(addrNode, "streetAddressLine");
        OMElement cityNode = this.getFirstChildWithName(addrNode, "city");
        OMElement stateNode = this.getFirstChildWithName(addrNode, "state");
        OMElement postalCodeNode = this.getFirstChildWithName(addrNode, "postalCode");

        // Get address string values (guard against nodes not present):
        String streetAddressLine = (streetAddressLineNode != null) ? streetAddressLineNode.getText() : null;
        String city = (cityNode != null) ? cityNode.getText() : null;
        String state = (stateNode != null) ? stateNode.getText() : null;
        String postalCode = (postalCodeNode != null) ? postalCodeNode.getText() : null;

        // Place into the ADT record:
        if ((streetAddressLine != null) || (city != null) || (state != null) || (postalCode != null)) {
            Hl7Address hl7Address = new Hl7Address(parentUUID);
            hl7Address.setStreetAddress(streetAddressLine);
            hl7Address.setCity(city);
            hl7Address.setStateOrProvince(state);
            hl7Address.setZipCode(postalCode);
            arb.setAddAddress(hl7Address);
        } else {
            this.logInfo("Note", "Request does not contain patient address");
        }
    }

    /**
     * 
     * @param arb
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void adtSavePatientRecord(AdtRecordBean arb) throws XdsInternalException {
        try {
            arb.saveToDatabase();
        } catch (Exception e) {
            throw this.logInternalException(e, "ADT EXCEPTION: Problem saving patient record");
        }
    }

    /**
     *
     * @param patientId
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private boolean adtPatientExists(String patientId) throws XdsInternalException {
        AdtJdbcConnection conn = this.adtGetDatabaseConnection();
        boolean patientExists = false;
        try {
            patientExists = conn.doesIdExist(patientId);
        } catch (SQLException e) {
            throw this.logInternalException(e, "ADT EXCEPTION: Problem checking for patient ID existence = " + patientId);
        } finally {
            conn.closeConnection();
        }
        return patientExists;
    }

    /**
     *
     * @param patientId
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private String adtGetPatientUUID(String patientId) throws XdsInternalException {
        AdtJdbcConnection conn = this.adtGetDatabaseConnection();
        String uuid = null;
        try {
            uuid = conn.getPatientUUID(patientId);
        } catch (SQLException e) {
            throw this.logInternalException(e, "ADT EXCEPTION: Problem getting patient UUID for patient ID existence = " + patientId);
        } finally {
            conn.closeConnection();
        }
        return uuid;
    }

    // Registry helper methods.
    /**
     * 
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private Connection regGetDatabaseConnection() throws XdsInternalException {
        return new SQLConnectionWrapper().getConnection(SQLConnectionWrapper.registryJNDIResourceName);
    }

    /**
     *
     * @param survivingPatientId
     * @param priorRegistrationPatientId
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void regUpdateExternalIdentifiers(String survivingPatientId, String priorRegistrationPatientId)
            throws XdsInternalException {
        Connection conn = this.regGetDatabaseConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE EXTERNALIDENTIFIER SET VALUE = ? WHERE VALUE = ?");
            preparedStatement.setString(1, survivingPatientId);
            preparedStatement.setString(2, priorRegistrationPatientId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw this.logInternalException(e, "REGISTRY EXCEPTION: Problem with updating Registry external identifiers");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Could not close connection", e);
            }
        }
    }

    /**
     * 
     * @return
     */
    private String getXConfigHomeCommunityProperty(String propertyName) {
        String propertyValue = null;
        try {
            XConfig xconf = XConfig.getInstance();
            propertyValue = xconf.getHomeCommunityProperty(propertyName);
        } catch (XdsInternalException e) {
            // FIXME? - not forwarding along exception.
            this.logInternalException(e, "Unable to get XConfig property");
        }
        return propertyValue;
    }

    /**
     *
     * @param propertyName
     * @return
     */
    private String getXConfigRegistryProperty(String propertyName) {
        String propertyValue = null;
        try {
            // Get the registry's configuration (registry name came from axis2 "services.xml").
            XConfig xconf = XConfig.getInstance();
            XConfigRegistry registry = xconf.getRegistryByName(this.xconfRegistryName);
            if (registry != null) {
                propertyValue = registry.getProperty(propertyName);
            }
        } catch (XdsInternalException e) {
            // FIXME? - not forwarding along exception.
            this.logInternalException(e, "Unable to get XConfig property");
        }
        return propertyValue;
    }

    /**
     *
     * @return
     */
    private String getHL7Date() {
        Hl7Date date = new Hl7Date();
        return date.now();
    }

// All of the log methods below should not generate exceptions if problems occur.
    /**
     *
     * @param patientId
     * @param messageId
     * @param updateMode
     */
    private void logPatientIdentityFeedToATNA(String patientId, String messageId, boolean updateMode, XATNALogger.OutcomeIndicator outcome) {
        try {
            //this.selectSingleNode(request, this.XPATH_MESSAGE_ID);
            XATNALogger xATNALogger = new XATNALogger(XATNALogger.TXN_ITI44, XATNALogger.ActorType.REGISTRY);
            xATNALogger.auditPatientIdentityFeedToRegistry(
                    patientId, messageId, updateMode, outcome);
        } catch (Exception e) {
            this.logInternalException(e, "Error trying to perform ATNA logging for Patient Identity Feed");
        }
    }

    /**
     *
     * @param response
     * @param status
     */
    private void logResponse(OMElement response, boolean status) {
        if (response != null) {
            log_message.addOtherParam("Response", response.toString());
        }
        log_message.setPass(status);
    }

    /**
     *
     * @param errorString
     */
    private void logError(String errorString) {
        this.errorDetected = true;  // Make note of a problem.
        log_message.addErrorParam("Errors", errorString);
        logger.error(errorString);
    }

    /**
     *
     * @param errorString
     * @return
     */
    private PatientIdentityFeedException logException(String errorString) {
        this.logError(errorString);
        return new PatientIdentityFeedException(errorString);
    }

    /**
     *
     * @param e
     * @param errorString
     * @return
     */
    private XdsInternalException logInternalException(Exception e, String errorString) {
        String exceptionString = ExceptionUtil.exception_details(e, errorString);
        this.logError(exceptionString);
        return new XdsInternalException(errorString);
    }

    /**
     *
     * @param logLabel
     * @param infoString
     */
    private void logInfo(String logLabel, String infoString) {
        log_message.addOtherParam(logLabel, infoString);
        logger.info(logLabel + " : " + infoString);
    }

    // Inner class
    public class PatientIdentityFeedException extends Exception {

        public PatientIdentityFeedException(String msg) {
            super(msg);
        }
    }
}
