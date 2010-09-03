/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

//import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.*;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import org.hl7.v3.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author rayj
 */
public class PixRetrieveBuilder {

    public static final String ControlActProcessCode = "PRPA_TE201309UV";
    private static final String AcceptAckCodeValue = "AL";
    private static final String InteractionIdExtension = "PRPA_IN201309";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "T";
    private static final String ITSVersion = "XML_1.0";
    private static final String MoodCodeValue = "EVN";


    public static PRPAIN201309UV02 createPixRetrieve(RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        List<String> targetAssigningAuthorities = extractTargetAssigningAuthorities(retrievePatientCorrelationsRequest);
        PRPAIN201309UV02 pixRetrieve = createTransmissionWrapper("1.1", null);

        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = createBaseControlActProcess();

        PRPAMT201307UV02QueryByParameter queryByParameter = createQueryByParameter(retrievePatientCorrelationsRequest.getQualifiedPatientIdentifier(), targetAssigningAuthorities);
        JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameterElement = createQueryByParameterElement(queryByParameter);
        controlActProcess.setQueryByParameter(createQueryByParameterElement);

        pixRetrieve.setControlActProcess(controlActProcess);

        return pixRetrieve;
    }

    private static List<String> extractTargetAssigningAuthorities(RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        //if assigning authorities are present, use those.  If not, convert home community to assigning authority
        List<String> targetAssigningAuthorities = retrievePatientCorrelationsRequest.getTargetAssigningAuthority();

        if (NullChecker.isNullish(targetAssigningAuthorities)) {
            List<String> targetHomeCommunities = retrievePatientCorrelationsRequest.getTargetHomeCommunity();
            if (NullChecker.isNotNullish(targetHomeCommunities)) {
                targetAssigningAuthorities = AssigningAuthorityHomeCommunityMappingHelper.lookupAssigningAuthorities(targetHomeCommunities);
            }
        }

        return targetAssigningAuthorities;
    }

    private static JAXBElement<COCTMT090100UV01AssignedPerson> createAssignedPersonElement() {

        COCTMT090100UV01AssignedPerson assignedPerson = new COCTMT090100UV01AssignedPerson();
        assignedPerson.getId().add(IIHelper.IIFactoryCreateNull());

        JAXBElement<COCTMT090100UV01AssignedPerson> assignedPersonElement;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedPerson");
        assignedPersonElement = new JAXBElement<COCTMT090100UV01AssignedPerson>(xmlqname, COCTMT090100UV01AssignedPerson.class, assignedPerson);

        return assignedPersonElement;
    }

    private static QUQIMT021001UV01AuthorOrPerformer createAuthor() {

        QUQIMT021001UV01AuthorOrPerformer author = new QUQIMT021001UV01AuthorOrPerformer();
        XParticipationAuthorPerformer xAuthor = XParticipationAuthorPerformer.AUT;
        author.setTypeCode(xAuthor);
        author.setAssignedPerson(createAssignedPersonElement());

        return author;
    }

    private static PRPAMT201307UV02ParameterList createParameterList(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        PRPAMT201307UV02PatientIdentifier patId = createPatientIdentifier(qualifiedSubjectIdentifier);
        parameterList.getPatientIdentifier().add(patId);

        if (targetAssigningAuthorities != null) {
            for (String targetAssigningAuthority : targetAssigningAuthorities) {
                PRPAMT201307UV02DataSource dataSourceItem = new PRPAMT201307UV02DataSource();
                II dataSourceValue = new II();
                dataSourceValue.setRoot(targetAssigningAuthority);
                dataSourceItem.getValue().add(dataSourceValue);
                parameterList.getDataSource().add(dataSourceItem);
            }
        }

        return parameterList;
    }

    private static PRPAIN201309UV02 createTransmissionWrapper(String senderId, String receiverId) {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();

        message.setITSVersion(ITSVersion);
        message.setId(UniqueIdHelper.createUniqueId());
        message.setCreationTime(CreationTimeHelper.getCreationTime());
        message.setInteractionId(InteractionIdHelper.createInteractionId(InteractionIdExtension));

        message.setProcessingCode(CSHelper.buildCS(ProcessingCodeValue));
        message.setProcessingModeCode(CSHelper.buildCS(ProcessingModeCode));
        message.setAcceptAckCode(CSHelper.buildCS(AcceptAckCodeValue));

        message.getReceiver().add(SenderReceiverHelper.CreateReceiver(receiverId));
        message.setSender(SenderReceiverHelper.CreateSender(senderId));

        return message;
    }

    private static PRPAIN201309UV02QUQIMT021001UV01ControlActProcess createBaseControlActProcess() {
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(CDHelper.CDFactory(ControlActProcessCode, Constants.HL7_OID));
        controlActProcess.getAuthorOrPerformer().add(createAuthor());

        return controlActProcess;
    }

    private static PRPAMT201307UV02QueryByParameter createQueryByParameter(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
        PRPAMT201307UV02QueryByParameter queryByParameter = new PRPAMT201307UV02QueryByParameter();
        queryByParameter.setQueryId(UniqueIdHelper.createUniqueId("1.1"));
        queryByParameter.setStatusCode(CSHelper.buildCS("new"));
        queryByParameter.setResponsePriorityCode(CSHelper.buildCS("I"));
        queryByParameter.setParameterList(createParameterList(qualifiedSubjectIdentifier, targetAssigningAuthorities));
        return queryByParameter;
    }

    private static JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameterElement(PRPAMT201307UV02QueryByParameter queryByParameter) {
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameterElement;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        queryByParameterElement = new JAXBElement<PRPAMT201307UV02QueryByParameter>(xmlqname, PRPAMT201307UV02QueryByParameter.class, new PRPAMT201307UV02QueryByParameter());
        queryByParameterElement.setValue(queryByParameter);
        return queryByParameterElement;
    }

    private static II getII(String assignAuth, String root, String ext) {
        II val = new II();

        val.setAssigningAuthorityName(assignAuth);
        val.setExtension(ext);
        val.setRoot(root);

        return val;
    }

    private static PRPAMT201307UV02PatientIdentifier createPatientIdentifier(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier) {
        PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.getValue().add(IIHelper.IIFactory(qualifiedSubjectIdentifier));
        patientIdentifier.setSemanticsText(SemanticsTextHelper.CreateSemanticsText("Patient.Id"));
        return patientIdentifier;
    }
}
