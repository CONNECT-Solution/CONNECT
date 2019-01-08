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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.AssigningAuthorityHomeCommunityMappingHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.CDHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.CSHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.Constants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.IIHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.SemanticsTextHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.SenderReceiverHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.UniqueIdHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.COCTMT090100UV01AssignedPerson;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.XParticipationAuthorPerformer;

/**
 *
 * @author rayj
 */
public class PixRetrieveBuilder {

    private AssigningAuthorityHomeCommunityMappingHelper aaMappingHelper;

    public static final String ControlActProcessCode = "PRPA_TE201309UV";
    private static final String AcceptAckCodeValue = "AL";
    private static final String InteractionIdExtension = "PRPA_IN201309";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "T";
    private static final String ITSVersion = "XML_1.0";

    /**
     *
     */
    public PixRetrieveBuilder() {
        aaMappingHelper = new AssigningAuthorityHomeCommunityMappingHelper();
    }

    /**
     * @param aaMappingHelper
     */
    public PixRetrieveBuilder(AssigningAuthorityHomeCommunityMappingHelper aaMappingHelper) {
        this.aaMappingHelper = aaMappingHelper;
    }

    public PRPAIN201309UV02 createPixRetrieve(
        RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        List<String> targetAssigningAuthorities = extractTargetAssigningAuthorities(retrievePatientCorrelationsRequest);
        PRPAIN201309UV02 pixRetrieve = createTransmissionWrapper("1.1", null);

        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = createBaseControlActProcess();

        PRPAMT201307UV02QueryByParameter queryByParameter = createQueryByParameter(
            retrievePatientCorrelationsRequest.getQualifiedPatientIdentifier(), targetAssigningAuthorities);
        JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameterElement = createQueryByParameterElement(
            queryByParameter);
        controlActProcess.setQueryByParameter(createQueryByParameterElement);

        pixRetrieve.setControlActProcess(controlActProcess);

        return pixRetrieve;
    }

    protected List<String> extractTargetAssigningAuthorities(
        RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        // if assigning authorities are present, use those. If not, convert home community to assigning authority
        List<String> targetAssigningAuthorities = retrievePatientCorrelationsRequest.getTargetAssigningAuthority();

        if (NullChecker.isNullish(targetAssigningAuthorities)) {
            List<String> targetHomeCommunities = retrievePatientCorrelationsRequest.getTargetHomeCommunity();
            if (NullChecker.isNotNullish(targetHomeCommunities)) {
                targetAssigningAuthorities = aaMappingHelper
                    .lookupAssigningAuthorities(stripCommunityIdsPrefix(targetHomeCommunities));
            }
        }

        return targetAssigningAuthorities;
    }

    private static JAXBElement<COCTMT090100UV01AssignedPerson> createAssignedPersonElement() {

        COCTMT090100UV01AssignedPerson assignedPerson = new COCTMT090100UV01AssignedPerson();
        assignedPerson.getId().add(IIHelper.IIFactoryCreateNull());

        JAXBElement<COCTMT090100UV01AssignedPerson> assignedPersonElement;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedPerson");
        assignedPersonElement = new JAXBElement<>(xmlqname, COCTMT090100UV01AssignedPerson.class, assignedPerson);

        return assignedPersonElement;
    }

    private static QUQIMT021001UV01AuthorOrPerformer createAuthor() {

        QUQIMT021001UV01AuthorOrPerformer author = new QUQIMT021001UV01AuthorOrPerformer();
        XParticipationAuthorPerformer xAuthor = XParticipationAuthorPerformer.AUT;
        author.setTypeCode(xAuthor);
        author.setAssignedPerson(createAssignedPersonElement());

        return author;
    }

    private static PRPAMT201307UV02ParameterList createParameterList(
        QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
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

    private static PRPAMT201307UV02QueryByParameter createQueryByParameter(
        QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
        PRPAMT201307UV02QueryByParameter queryByParameter = new PRPAMT201307UV02QueryByParameter();
        queryByParameter.setQueryId(UniqueIdHelper.createUniqueId("1.1"));
        queryByParameter.setStatusCode(CSHelper.buildCS("new"));
        queryByParameter.setResponsePriorityCode(CSHelper.buildCS("I"));
        queryByParameter.setParameterList(createParameterList(qualifiedSubjectIdentifier, targetAssigningAuthorities));
        return queryByParameter;
    }

    private static JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameterElement(
        PRPAMT201307UV02QueryByParameter queryByParameter) {
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameterElement;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        queryByParameterElement = new JAXBElement<>(xmlqname, PRPAMT201307UV02QueryByParameter.class,
            new PRPAMT201307UV02QueryByParameter());
        queryByParameterElement.setValue(queryByParameter);
        return queryByParameterElement;
    }

    private static PRPAMT201307UV02PatientIdentifier createPatientIdentifier(
        QualifiedSubjectIdentifierType qualifiedSubjectIdentifier) {
        PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.getValue().add(IIHelper.IIFactory(qualifiedSubjectIdentifier));
        patientIdentifier.setSemanticsText(SemanticsTextHelper.createSemanticsText("Patient.Id"));
        return patientIdentifier;
    }

    protected List<String> stripCommunityIdsPrefix(List<String> targetCommunities) {
        if (NullChecker.isNotNullish(targetCommunities)) {
            List<String> targetCommunityIds = new ArrayList<>();
            for (String homeCommunityId : targetCommunities) {
                targetCommunityIds.add(HomeCommunityMap.formatHomeCommunityId(homeCommunityId));
            }
            return targetCommunityIds;
        }
        return null;
    }
}
