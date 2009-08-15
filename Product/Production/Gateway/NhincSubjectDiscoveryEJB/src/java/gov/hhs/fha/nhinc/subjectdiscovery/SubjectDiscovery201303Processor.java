/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscovery201303Processor {

    public MCCIIN000002UV01 process201303(PIXConsumerPRPAIN201303UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();

        // Get all of the Assigning Authority Mappings for the Sending Home Community Id
        List<String> assignAuthIds = getAssigningAuthorities(request);

        // Extract the Patient Id
        II patId = new II();
        if (request != null &&
                request.getPRPAIN201303UV() != null &&
                request.getPRPAIN201303UV().getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getControlActProcess().getSubject()) &&
                request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0) != null &&
                request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            patId.setExtension(request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            patId.setRoot(request.getPRPAIN201303UV().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        }

        // For each of the assigning authority ids attempt to get patient correlations for that assigning authority
        for (String assignAuthId : assignAuthIds) {
            removeCorrelations(patId, assignAuthId);
        }

        ack = ackCreater.createAck(request, "Success");
        return ack;
    }

    private List<String> getAssigningAuthorities(PIXConsumerPRPAIN201303UVRequestType request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        List<String> aaList = null;

        if (request != null &&
                request.getPRPAIN201303UV() != null &&
                request.getPRPAIN201303UV().getSender() != null &&
                request.getPRPAIN201303UV().getSender().getDevice() != null &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getSender().getDevice().getId()) &&
                request.getPRPAIN201303UV().getSender().getDevice().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getPRPAIN201303UV().getSender().getDevice().getId().get(0).getRoot())) {
            aaList = mappingDao.getAssigningAuthoritiesByHomeCommunity(request.getPRPAIN201303UV().getSender().getDevice().getId().get(0).getRoot());
        }

        return aaList;
    }

    private void removeCorrelations(II patientId, String assignAuthId) {
        // Set up the request message to find out if there are any patient correlations for this patient id and assigning authority
        RetrievePatientCorrelationsRequestType patCorrelationQueryReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
        subId.setAssigningAuthorityIdentifier(patientId.getRoot());
        subId.setSubjectIdentifier(patientId.getExtension());
        patCorrelationQueryReq.setQualifiedPatientIdentifier(subId);
        patCorrelationQueryReq.getTargetAssigningAuthority().add(assignAuthId);

        // Retrieve any correlations
        PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
        PatientCorrelationFacadeProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();
        RetrievePatientCorrelationsResponseType patientCorrelations = patCorrelationProxy.retrievePatientCorrelations(patCorrelationQueryReq);

        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        qualSubId.setAssigningAuthorityIdentifier(patientId.getRoot());
        qualSubId.setSubjectIdentifier(patientId.getExtension());

        // Remove each correlation
        if (patientCorrelations != null &&
                NullChecker.isNotNullish(patientCorrelations.getQualifiedPatientIdentifier())) {
            for (QualifiedSubjectIdentifierType patId : patientCorrelations.getQualifiedPatientIdentifier()) {
                RemovePatientCorrelationRequestType removeCorrelationReq = new RemovePatientCorrelationRequestType();
                removeCorrelationReq.getQualifiedPatientIdentifier().add(qualSubId);
                removeCorrelationReq.getQualifiedPatientIdentifier().add(patId);
                RemovePatientCorrelationResponseType resp = patCorrelationProxy.removePatientCorrelation(removeCorrelationReq);
            }
        }
    }
}
