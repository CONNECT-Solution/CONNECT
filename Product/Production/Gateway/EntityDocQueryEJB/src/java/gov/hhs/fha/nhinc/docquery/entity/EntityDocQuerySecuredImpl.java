package gov.hhs.fha.nhinc.docquery.entity;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.proxy.NhincProxyDocQueryImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityDocQuerySecuredImpl
{
    private static Log log = LogFactory.getLog(EntityDocQuerySecuredImpl.class);
    private String localHomeCommunity = null;
    
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context)
    {
        log.debug("Entering EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = new AdhocQueryResponse();
        boolean isTargeted = false;
        List<NhinTargetCommunityType> targets = null;
        RegistryObjectListType regObjList = null;
        RegistryErrorList regErrList = null;

        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Audit the Document Query Request Message received on the Entity Interface
        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        AcknowledgementType ack = auditLog.audit(request, assertion);

        // Get the local home community id
        try {
            localHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
            return null;
        }

        // Determine if this is a targeted Document Query request or not.
        if (request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())) {
            targets = request.getNhinTargetCommunities().getNhinTargetCommunity();
            isTargeted = true;
        }

        // Validate that the message is not null
        if (request.getAdhocQueryRequest() != null &&
                request.getAdhocQueryRequest().getAdhocQuery() != null &&
                NullChecker.isNotNullish(request.getAdhocQueryRequest().getAdhocQuery().getSlot())) {
            List<SlotType1> slotList = request.getAdhocQueryRequest().getAdhocQuery().getSlot();

            RetrievePatientCorrelationsResponseType correlationsResult = retreiveCorrelations(slotList, targets);

            // Make sure the valid results back
            if (correlationsResult != null &&
                    NullChecker.isNotNullish(correlationsResult.getQualifiedPatientIdentifier())) {

                NhincProxyDocQueryImpl nhincDocQueryProxy = new NhincProxyDocQueryImpl();

                // For each correlation send out a document query request
                for (QualifiedSubjectIdentifierType subId : correlationsResult.getQualifiedPatientIdentifier()) {
                    gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType docQuery = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType();
                    docQuery.setAdhocQueryRequest(request.getAdhocQueryRequest());
                    docQuery.setAssertion(assertion);

                    NhinTargetSystemType targetSystem = new NhinTargetSystemType();
                    HomeCommunityType targetCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(subId.getAssigningAuthorityIdentifier());
                    targetSystem.setHomeCommunity(targetCommunity);
                    docQuery.setNhinTargetSystem(targetSystem);

                    // Replace the patient id in the document query message
                    DocumentQueryTransform transform = new DocumentQueryTransform();
                    AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(request.getAdhocQueryRequest(), localHomeCommunity, subId.getAssigningAuthorityIdentifier(), subId.getSubjectIdentifier());

                    AdhocQueryResponse queryResults = nhincDocQueryProxy.respondingGatewayCrossGatewayQuery(docQuery);
                    //response = nhincDocQueryProxy.respondingGatewayCrossGatewayQuery(docQuery);

                    // Aggregate document query results.
                    if (queryResults.getRegistryObjectList() != null &&
                            NullChecker.isNotNullish(queryResults.getRegistryObjectList().getIdentifiable())) {
                        if (regObjList == null) {
                            regObjList = new RegistryObjectListType();
                        }
                        List<JAXBElement<? extends IdentifiableType>> regObjs = queryResults.getRegistryObjectList().getIdentifiable();

                        for (JAXBElement<? extends IdentifiableType> oJAXBElement : regObjs) {
                            regObjList.getIdentifiable().add(oJAXBElement);
                        }
                    }

                    // Aggregate document query error cases.
                    if (queryResults.getRegistryErrorList() != null &&
                            NullChecker.isNotNullish(queryResults.getRegistryErrorList().getRegistryError())) {
                        if (regErrList == null) {
                            regErrList = new RegistryErrorList();
                        }

                        List<RegistryError> regErrors = queryResults.getRegistryErrorList().getRegistryError();

                        for (RegistryError regError : regErrors) {
                            regErrList.getRegistryError().add(regError);
                        }
                    }
                }
            }
        }

        // Set the Registry Object List
        if (regObjList != null) {
            response.setRegistryObjectList(regObjList);
        }

        // Set the Registry Error List
        if (regErrList != null) {
            response.setRegistryErrorList(regErrList);
        }

        // Audit the Document Query Response Message received on the Nhin Interface
        AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
        auditMsg.setAdhocQueryResponse(response);
        auditMsg.setAssertion(assertion);
        ack = auditLog.auditResponse(auditMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        log.debug("Exiting EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    private RetrievePatientCorrelationsResponseType retreiveCorrelations(List<SlotType1> slotList, List<NhinTargetCommunityType> targets) {
        RetrievePatientCorrelationsResponseType results = null;
        RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        boolean querySelf = false;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {

            // Find the Patient Id slot
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    qualSubId.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(slot.getValueList().getValue().get(0)));
                    qualSubId.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0)));

                    log.info("Extracting subject id: " + qualSubId.getSubjectIdentifier());
                    log.info("Extracting assigning authority id: " + qualSubId.getAssigningAuthorityIdentifier());
                    patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);
                }

                // Save off the target home community ids to use in the patient correlation query
                if (NullChecker.isNotNullish(targets)) {
                    for (NhinTargetCommunityType target : targets) {
                        if (target.getHomeCommunity() != null &&
                                NullChecker.isNotNullish(target.getHomeCommunity().getHomeCommunityId())) {
                            patientCorrelationReq.getTargetHomeCommunity().add(target.getHomeCommunity().getHomeCommunityId());

                            if (target.getHomeCommunity().getHomeCommunityId().equals(localHomeCommunity)) {
                                querySelf = true;
                            }
                        }
                    }
                }

                break;
            }
        }

        // Get the property to determine whether we should query our local home community
        try {
            querySelf = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DOC_QUERY_SELF_PROPERTY_NAME);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }

        // Retreive Patient Correlations this patient
        PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
        PatientCorrelationFacadeProxy proxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();

        results = proxy.retrievePatientCorrelations(patientCorrelationReq);

        // Make sure the response is valid
        if (results != null &&
                results.getQualifiedPatientIdentifier() != null) {
            // If we are querying ourselves as well then add this community to the list of correlations
            if (querySelf == true) {
                results.getQualifiedPatientIdentifier().add(patientCorrelationReq.getQualifiedPatientIdentifier());
            }
        }

        return results;
    }
    
}
