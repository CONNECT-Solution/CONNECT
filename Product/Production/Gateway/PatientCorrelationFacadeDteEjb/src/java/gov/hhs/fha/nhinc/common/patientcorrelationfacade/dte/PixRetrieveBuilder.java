/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

//import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.AssigningAuthorityHomeCommunityMappingHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.CDHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.CSHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.Configuration;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.Constants;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.IIHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.SemanticsTextHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.UniqueIdHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.SenderReceiverHelper;
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

    public static CreatePixRetrieveResponseType createPixRetrieve(CreatePixRetrieveRequestType createPixRetrieveRequest) {
        PRPAIN201309UV pixRetrieve = createPixRetrieve(createPixRetrieveRequest.getRetrievePatientCorrelationsRequest());
        CreatePixRetrieveResponseType response = new CreatePixRetrieveResponseType();
        response.setPRPAIN201309UV(pixRetrieve);
        return response;
    }

    public static PRPAIN201309UV createPixRetrieve(RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        List<String> targetAssigningAuthorities = extractTargetAssigningAuthorities(retrievePatientCorrelationsRequest);
        PRPAIN201309UV pixRetrieve = createTransmissionWrapper(Configuration.getMyCommunityId(), null);

        PRPAIN201309UVQUQIMT021001UV01ControlActProcess controlActProcess = createBaseControlActProcess();

        PRPAMT201307UVQueryByParameter queryByParameter = createQueryByParameter(retrievePatientCorrelationsRequest.getQualifiedPatientIdentifier(), targetAssigningAuthorities);
        JAXBElement<PRPAMT201307UVQueryByParameter> createQueryByParameterElement = createQueryByParameterElement(queryByParameter);
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

    private static PRPAMT201307UVParameterList createParameterList(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
        PRPAMT201307UVParameterList parameterList = new PRPAMT201307UVParameterList();
        PRPAMT201307UVPatientIdentifier patId = createPatientIdentifier(qualifiedSubjectIdentifier);
        parameterList.getPatientIdentifier().add(patId);

        if (targetAssigningAuthorities != null) {
            for (String targetAssigningAuthority : targetAssigningAuthorities) {
                PRPAMT201307UVDataSource dataSourceItem = new PRPAMT201307UVDataSource();
                II dataSourceValue = new II();
                dataSourceValue.setRoot(targetAssigningAuthority);
                dataSourceItem.getValue().add(dataSourceValue);
                parameterList.getDataSource().add(dataSourceItem);
            }
        }

        return parameterList;
    }

    private static PRPAIN201309UV createTransmissionWrapper(String senderId, String receiverId) {
        PRPAIN201309UV message = new PRPAIN201309UV();

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

    private static PRPAIN201309UVQUQIMT021001UV01ControlActProcess createBaseControlActProcess() {
        PRPAIN201309UVQUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UVQUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(MoodCodeValue);
        controlActProcess.setCode(CDHelper.CDFactory(ControlActProcessCode, Constants.HL7_OID));
        controlActProcess.getAuthorOrPerformer().add(createAuthor());

        return controlActProcess;
    }

    private static PRPAMT201307UVQueryByParameter createQueryByParameter(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier, List<String> targetAssigningAuthorities) {
        PRPAMT201307UVQueryByParameter queryByParameter = new PRPAMT201307UVQueryByParameter();
        queryByParameter.setQueryId(UniqueIdHelper.createUniqueId(Configuration.getMyCommunityId()));
        queryByParameter.setStatusCode(CSHelper.buildCS("new"));
        queryByParameter.setResponsePriorityCode(CSHelper.buildCS("I"));
        queryByParameter.setParameterList(createParameterList(qualifiedSubjectIdentifier, targetAssigningAuthorities));
        return queryByParameter;
    }

    private static JAXBElement<PRPAMT201307UVQueryByParameter> createQueryByParameterElement(PRPAMT201307UVQueryByParameter queryByParameter) {
        JAXBElement<PRPAMT201307UVQueryByParameter> queryByParameterElement;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        queryByParameterElement = new JAXBElement<PRPAMT201307UVQueryByParameter>(xmlqname, PRPAMT201307UVQueryByParameter.class, new PRPAMT201307UVQueryByParameter());
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

    private static PRPAMT201307UVPatientIdentifier createPatientIdentifier(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier) {
        PRPAMT201307UVPatientIdentifier patientIdentifier = new PRPAMT201307UVPatientIdentifier();
        patientIdentifier.getValue().add(IIHelper.IIFactory(qualifiedSubjectIdentifier));
        patientIdentifier.setSemanticsText(SemanticsTextHelper.CreateSemanticsText("Patient.Id"));
        return patientIdentifier;
    }
}
