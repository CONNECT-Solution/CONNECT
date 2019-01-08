/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.patientdiscovery.utils;

import gov.hhs.fha.nhinc.common.nhinccommon.AddressType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.PhoneType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.XParticipationAuthorPerformer;

/**
 * @author zmelnick
 *
 */
public class PDTestUtils {

    private static final String localHCID = "1.1";
    private static final String localAA = "1.1";
    private static final String remoteHCID = "2.2";
    private static final String remoteAA = "2.2";
    private static ObjectFactory oObjectFactory = new ObjectFactory();

    public RespondingGatewayPRPAIN201305UV02RequestType createValidEntityRequest() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        request.setPRPAIN201305UV02(createValidPRPAIN201305UV02());
        request.setAssertion(createValidAssertion());
        request.setNhinTargetCommunities(createValidNhinTargetCommunitiesType());
        return request;
    }






    /**
     * @return
     */
    private NhinTargetCommunitiesType createValidNhinTargetCommunitiesType() {
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setDescription("Internal1");
        homeCommunity.setHomeCommunityId(localHCID);
        homeCommunity.setName("Internal1");
        targetCommunity.setHomeCommunity(homeCommunity );
        target.getNhinTargetCommunity().add(0, targetCommunity);
        return null;
    }

    /**
     * @return
     */
    private AssertionType createValidAssertion() {
        AssertionType assertion = new AssertionType();
        assertion.setAddress(createValidAddress());
        assertion.setDateOfBirth("19800516");
        assertion.setExplanationNonClaimantSignature("NEEDED");
        assertion.setHaveSecondWitnessSignature(true);
        assertion.setHaveSignature(true);
        assertion.setHaveWitnessSignature(true);
        assertion.setHomeCommunity(createValidHomeCommunityType());
        assertion.setPersonName(createValidPersonName("Yonger", "Gallow", "FJ", "Joe Smith", "Mr"));
        assertion.setPhoneNumber(createValidPhoneNumber("123", "1", "1234", "3456789"));
        assertion.setSecondWitnessAddress(createValidWitnessAddress("US", "FL", "Melbourne", "12345", "123 Johnson Road"));
        assertion.setSecondWitnessName(createValidPersonName("Hughes", "Howie", "HH", "Howie Hughes", "Dr"));
        assertion.setSecondWitnessPhone(createValidPhoneNumber("123","1", "1546", "4567892"));
        assertion.setSSN("123456789");
        assertion.getUniquePatientId().add(0, "1111^^^&amp;1.1&amp;ISO");
        assertion.setWitnessAddress(createValidWitnessAddress("US", "MN", "Frankfort", "65498", "432 Jackson St"));
        assertion.setWitnessName(createValidPersonName("Ugble", "Gary", "GU", "Gary Ugble", "Mr"));
        assertion.setWitnessPhone(createValidPhoneNumber("987", "1", "6549", "2222222"));
        assertion.setUserInfo(createValidUserInfo());
        assertion.setAuthorized(true);
        assertion.setPurposeOfDisclosureCoded(createValidPurposeOfDisclosure());
        assertion.setSamlAuthnStatement(createValidSamlAuthnStatement());
        assertion.setSamlAuthzDecisionStatement(createValidSamlAuthzDecisionStatement());
        return assertion;
    }

    /**
     * @return
     */
    private SamlAuthzDecisionStatementType createValidSamlAuthzDecisionStatement() {
        SamlAuthzDecisionStatementType samlAuthzDecision = new SamlAuthzDecisionStatementType();
        samlAuthzDecision.setDecision("Permit");
        samlAuthzDecision.setResource("https://1.1.1.1:8181/SamlReceiveService/SamlProcessWS");
        samlAuthzDecision.setAction("TestSaml");

        SamlAuthzDecisionStatementEvidenceType evidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType assertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        assertion.setId("40df7c0a-ff3e-4b26-baeb-f2910f6d05a9");
        assertion.setIssueInstant("009-04-16T13:10:39.093Z");
        assertion.setVersion("2.0");
        assertion.setIssuerFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        assertion.setIssuer("CN=SAML User,OU=connect,O=FHA,L=Melbourne,ST=FL,C=US");

        SamlAuthzDecisionStatementEvidenceConditionsType conditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        conditions.setNotBefore("2009-04-16T13:10:39.093Z");
        conditions.setNotOnOrAfter("2009-12-31T12:00:00.000Z");
        assertion.setConditions(conditions);

        assertion.getAccessConsentPolicy().add(0, "Claim-Ref-1234");
        assertion.getInstanceAccessConsentPolicy().add(0, "Claim-Instance-1");
        evidence.setAssertion(assertion);
        samlAuthzDecision.setEvidence(evidence);
        return samlAuthzDecision;
    }

    /**
     * @return
     */
    private SamlAuthnStatementType createValidSamlAuthnStatement() {
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        samlAuthnStatement.setAuthInstant("2009-04-16T13:15:39Z");
        samlAuthnStatement.setAuthInstant("987");
        samlAuthnStatement.setAuthContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
        samlAuthnStatement.setSubjectLocalityAddress("1.1.1.1");
        samlAuthnStatement.setSubjectLocalityDNSName("connectopensource.org");
        return samlAuthnStatement;
    }

    /**
     * @return
     */
    private CeType createValidPurposeOfDisclosure() {
        CeType purpose = new CeType();
        purpose.setCode("PUBLICHEALTH");
        purpose.setCodeSystem("2.16.840.1.113883.3.18.7.1");
        purpose.setCodeSystemName("nhin-purpose");
        purpose.setCodeSystemVersion("1.0");
        purpose.setDisplayName("Use or disclosure of Psychotherapy Notes");
        purpose.setOriginalText("Use or disclosure of Psychotherapy Notes");
        return purpose;
    }

    /**
     * @return
     */
    private UserType createValidUserInfo() {
        UserType userInfo = new UserType();
        userInfo.setPersonName(createValidPersonName("Anderson", "Wilma", "WA", "Wilma Anderson", "Mrs"));
        userInfo.setUserName("wanderson");
        userInfo.setOrg(createValidHomeCommunityType());

        CeType roleCoded = new CeType();
        roleCoded.setCode("307969004");
        roleCoded.setCodeSystem("2.16.840.1.113883.6.96");
        roleCoded.setCodeSystemName("SNOWMED_CT");
        roleCoded.setCodeSystemVersion("1.0");
        roleCoded.setDisplayName("Public Health");
        roleCoded.setOriginalText("Public Health");
        userInfo.setRoleCoded(roleCoded);
        return userInfo;
    }

    /**
     * @return
     */
    private PhoneType createValidPhoneNumber(String areaCode, String countryCode, String extension, String localNumber) {
        PhoneType phone = new PhoneType();
        phone.setAreaCode(areaCode);
        phone.setCountryCode(countryCode);
        phone.setExtension(extension);
        phone.setLocalNumber(localNumber);
        phone.setPhoneNumberType(createCeTypeWithCode("W"));
        return phone;
    }

    /**
     * @return
     */
    private AddressType createValidWitnessAddress(String country, String state, String city, String zip, String streetAddress) {
        AddressType address = new AddressType();
        address.setAddressType(createCeTypeWithCode("W"));
        address.setCity(city);
        address.setCountry(country);
        address.setState(state);
        address.setStreetAddress(streetAddress);
        address.setZipCode(zip);
        return address;
    }

    /**
     * @return
     */
    private CeType createCeTypeWithCode(String code) {
        CeType oCeType = new CeType();
        oCeType.setCode(code);
        return oCeType;
    }

    /**
     * @return
     */
    private PersonNameType createValidPersonName(String familyName, String givenName, String secondOrInitial, String fullName, String prefix) {
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName(familyName);
        personName.setGivenName(givenName);

        personName.setNameType(createCeTypeWithCode("G"));

        personName.setSecondNameOrInitials(secondOrInitial);
        personName.setFullName(fullName);
        personName.setPrefix(prefix);

        return personName;
    }

    /**
     * @return
     */
    private HomeCommunityType createValidHomeCommunityType() {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setDescription("InternalTest2");
        homeCommunity.setHomeCommunityId(remoteHCID);
        homeCommunity.setName("InternalTest2");
        return homeCommunity;
    }

    /**
     * @return
     */
    private AddressType createValidAddress() {
        AddressType address = new AddressType();
        address.setAddressType(createCeTypeWithCode("W"));
        address.setCity("Melbourne");
        address.setCountry("US");
        address.setState("FL");
        address.setStreetAddress("123 Johnson Rd");
        address.setZipCode("12345");
        return address;
    }

    /**
     * @return
     */
    private PRPAIN201305UV02 createValidPRPAIN201305UV02() {
        PRPAIN201305UV02 prpain = new PRPAIN201305UV02();
        prpain.setCreationTime(createValidCreationTime());
        prpain.setInteractionId(createValidInterationID());
        prpain.setProcessingCode(createValidProcessingCode());
        prpain.setProcessingModeCode(createValidProcessingModeCode());
        prpain.setAcceptAckCode(createValidAcceptAckCode());
        prpain.getReceiver().add(0, createValidReceiver());
        prpain.setSender(createValidSender());
        prpain.setControlActProcess(createValidControlActProcess());

        return prpain;
    }





    /**
     * @return
     */
    private MCCIMT000100UV01Receiver createValidReceiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("1.2.345.678.999");
        device.getId().add(0, id);

        MCCIMT000100UV01Agent asAgent = new MCCIMT000100UV01Agent();
        asAgent.getClassCode().add(0, "AGNT");

        MCCIMT000100UV01Organization representedOrganization = new MCCIMT000100UV01Organization();
        representedOrganization.setClassCode("ORG");
        representedOrganization.setDeterminerCode("INSTANCE");

        II id2 = new II();
        id2.setRoot(remoteHCID);
        representedOrganization.getId().add(0, id2);

        JAXBElement<MCCIMT000100UV01Organization> jaxbRepresentedOrganization = oObjectFactory.createMCCIMT000100UV01AgentRepresentedOrganization(representedOrganization );
        asAgent.setRepresentedOrganization(jaxbRepresentedOrganization);

        JAXBElement<MCCIMT000100UV01Agent> jaxbAsAgent = oObjectFactory.createMCCIMT000100UV01DeviceAsAgent(asAgent);
        device.setAsAgent(jaxbAsAgent);
        receiver.setDevice(device);
        return receiver;
    }

    /**
     * @return
     */
    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createValidControlActProcess() {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        createValidAuthorOrPerformer(controlActProcess);
        controlActProcess.setQueryByParameter(createValidQueryByParameter());

        return controlActProcess;
    }

    /**
     * @return
     */
    private JAXBElement<PRPAMT201306UV02QueryByParameter> createValidQueryByParameter() {
        PRPAMT201306UV02QueryByParameter queryByParameter = new PRPAMT201306UV02QueryByParameter();

        II queryId = new II();
        queryId.setRoot(remoteHCID);
        queryId.setExtension("-abd3453dcd24wkkks545");
        queryByParameter.setQueryId(queryId);

        queryByParameter.setStatusCode(createCSObjectAndSetCode("new"));
        queryByParameter.setResponseModalityCode(createCSObjectAndSetCode("R"));
        queryByParameter.setResponsePriorityCode(createCSObjectAndSetCode("I"));
        queryByParameter.setParameterList(createValidParameterList());

        JAXBElement<PRPAMT201306UV02QueryByParameter> queryByParameterJAXBElement = oObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParameter);

        return queryByParameterJAXBElement;
    }

    /**
     * @return
     */
    private PRPAMT201306UV02ParameterList createValidParameterList() {
        PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();

        parameterList.getLivingSubjectAdministrativeGender().add(0, createValidLivingSubjectAdministrativeGender());
        parameterList.getLivingSubjectBirthTime().add(0, createValidLivingSubjectBirthTime());
        parameterList.getLivingSubjectName().add(0, createValidLivingSubjectName());
        parameterList.getLivingSubjectId().add(0, createValidLivingSubjectId());

        return parameterList;
    }

    /**
     * @return
     */
    private PRPAMT201306UV02LivingSubjectId createValidLivingSubjectId() {
        PRPAMT201306UV02LivingSubjectId subjectId = new PRPAMT201306UV02LivingSubjectId();
        II value = new II();
        value.setRoot(remoteAA);
        value.setExtension("1111");
        subjectId.getValue().add(0, value);

        STExplicit oRepresentation = new STExplicit();
        oRepresentation.setRepresentation(BinaryDataEncoding.TXT);
        subjectId.setSemanticsText(oRepresentation);
        return subjectId;
    }

    /**
     * @return
     */
    private PRPAMT201306UV02LivingSubjectName createValidLivingSubjectName() {
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        ENExplicit nameValue = new ENExplicit();

        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setContent("Younger");
        familyName.setPartType("FAM");

        EnExplicitGiven givenName = new EnExplicitGiven();
        givenName.setContent("Gallow");
        givenName.setPartType("GIV");

        JAXBElement<EnExplicitFamily> jaxbFamilyName = oObjectFactory.createENExplicitFamily(familyName);
        JAXBElement<EnExplicitGiven> jaxbGivenName = oObjectFactory.createENExplicitGiven(givenName);

        nameValue.getContent().add(0, jaxbFamilyName);
        nameValue.getContent().add(1, jaxbGivenName);
        subjectName.getValue().add(0, nameValue);

        STExplicit oRepresentation = new STExplicit();
        oRepresentation.setRepresentation(BinaryDataEncoding.TXT);
        subjectName.setSemanticsText(oRepresentation);

        return subjectName;
    }

    /**
     * @param parameterList
     */
    private static PRPAMT201306UV02LivingSubjectBirthTime createValidLivingSubjectBirthTime() {
        PRPAMT201306UV02LivingSubjectBirthTime birthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit value = new IVLTSExplicit();
        value.setValue("19630804");
        birthTime.getValue().add(0, value);

        STExplicit oRepresentation = new STExplicit();
        oRepresentation.setRepresentation(BinaryDataEncoding.TXT);
        birthTime.setSemanticsText(oRepresentation);
        return birthTime;
    }

    /**
     * @param parameterList
     */
    private PRPAMT201306UV02LivingSubjectAdministrativeGender createValidLivingSubjectAdministrativeGender() {
        PRPAMT201306UV02LivingSubjectAdministrativeGender gender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();

        CE code = new CE();
        code.setCode("M");
        gender.getValue().add(0, code);

        STExplicit oRepresentation = new STExplicit();
        oRepresentation.setRepresentation(BinaryDataEncoding.TXT);
        gender.setSemanticsText(oRepresentation);
        return gender;
    }

    /**
     * @return
     */
    protected CS createCSObjectAndSetCode(String code) {
        CS statusCode = new CS();
        statusCode.setCode(code);
        return statusCode;
    }

    /**
     * @param controlActProcess
     */
    protected void createValidAuthorOrPerformer(PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess) {
        controlActProcess.getAuthorOrPerformer().add(0, new QUQIMT021001UV01AuthorOrPerformer());
        controlActProcess.getAuthorOrPerformer().get(0).setTypeCode(XParticipationAuthorPerformer.AUT);

        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        id.setRoot(remoteAA);

        assignedDevice.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        assignedDevice.getId().add(0, id);

        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = oObjectFactory
                .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice);

        controlActProcess.getAuthorOrPerformer().get(0).setAssignedDevice(assignedDeviceJAXBElement);
    }

    /**
     * @return
     */
    private MCCIMT000100UV01Sender createValidSender() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("1.2.345.678.999");
        device.getId().add(0, id);

        device.setAsAgent(createValidAsAgentForSender());
        sender.setDevice(device);
        return sender;
    }

    /**
     * @return
     */
    private JAXBElement<MCCIMT000100UV01Agent> createValidAsAgentForSender() {
        MCCIMT000100UV01Agent newAgent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization representedOrganization = new MCCIMT000100UV01Organization();

        representedOrganization.setClassCode(HL7Constants.ORG_CLASS_CODE);
        representedOrganization.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);

        II id = new II();
        id.setRoot(remoteHCID);
        representedOrganization.getId().add(0, id);

        JAXBElement<MCCIMT000100UV01Organization> orgElem = oObjectFactory
                .createMCCIMT000100UV01AgentRepresentedOrganization(representedOrganization);

        newAgent.setRepresentedOrganization(orgElem);
        newAgent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);
        JAXBElement<MCCIMT000100UV01Agent> asAgent = oObjectFactory.createMCCIMT000100UV01DeviceAsAgent(newAgent);
        return asAgent;
    }

    /**
     * @return
     */
    private CS createValidAcceptAckCode() {
        CS acceptAckCode = new CS();
        acceptAckCode.setCode("AL");
        return acceptAckCode;
    }

    /**
     * @return
     */
    private CS createValidProcessingModeCode() {
        CS processingModeCode = new CS();
        processingModeCode.setCode("T");
        return processingModeCode;
    }

    /**
     * @return
     */
    private CS createValidProcessingCode() {
        CS processingCode = new CS();
        processingCode.setCode("T");
        return processingCode;
    }

    /**
     * @return
     */
    private II createValidInterationID() {
        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201305UV02");
        return interactionId;
    }

    /**
     * @return
     */
    protected TSExplicit createValidCreationTime() {
        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue("20091116084800");
        return creationTime;
    }

}
