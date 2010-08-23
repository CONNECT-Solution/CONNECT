/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adapterentitydocretrieveapp;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author JHOPPESC
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AssertionType assertion = new AssertionType();
        UserType userInfo = new UserType();
        userInfo.setUserName("username");
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Johnson");
        person.setGivenName("Huey");
        userInfo.setPersonName(person);
        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId("1.1");
        hc.setName("Community1");
        userInfo.setOrg(hc);
        CeType ce = new CeType();
        ce.setCode("307969004");
        ce.setCodeSystem("2.16.840.1.113883.6.96");
        ce.setCodeSystemName("SNOMED_CT");
        userInfo.setRoleCoded(ce);
        assertion.setUserInfo(userInfo);

        assertion.setHomeCommunity(hc);

        assertion.setPurposeOfDisclosureCoded(ce);

        assertion.setSamlAuthnStatement(new SamlAuthnStatementType());

        SamlAuthzDecisionStatementType authDecStatement = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceType evidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType samlAssert = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceConditionsType conditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        samlAssert.setConditions(conditions);
        evidence.setAssertion(samlAssert);

        authDecStatement.setEvidence(evidence);
        assertion.setSamlAuthzDecisionStatement(authDecStatement);

        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType org = new HomeCommunityType();
        org.setHomeCommunityId("2.2");
        target.setHomeCommunity(org);
        targets.getNhinTargetCommunity().add(target);

        RetrieveDocumentSetRequestType docSetRequest = new RetrieveDocumentSetRequestType();
        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setHomeCommunityId("2.2");

        String docId = null;
        if (args.length == 1 &&
                args[0] != null &&
                args[0].length() > 0) {
           docId = args[0];
        }
        else {
           docId = "1.123456.33333";
        }

        docRequest.setDocumentUniqueId(docId);
        docRequest.setRepositoryUniqueId("1");
        docSetRequest.getDocumentRequest().add(docRequest);


        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setAssertion(assertion);
        request.setNhinTargetCommunities(targets);
        request.setRetrieveDocumentSetRequest(docSetRequest);

        EntityDocRetrieve service = new EntityDocRetrieve();
        EntityDocRetrievePortType port = service.getEntityDocRetrievePortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/CONNECTGateway/EntityService/EntityDocRetrieve");
        SOAPBinding binding = (SOAPBinding)((javax.xml.ws.BindingProvider) port).getBinding();
        binding.setMTOMEnabled(true);

        if (binding.isMTOMEnabled()) {
            System.out.println("MTOM is enabled");
        }
        else {
            System.out.println("MTOM is disabled");
        }

        RetrieveDocumentSetResponseType response = port.respondingGatewayCrossGatewayRetrieve(request);

        if (response != null &&
                response.getDocumentResponse() != null &&
                response.getDocumentResponse().size() > 0) {
            for (DocumentResponse doc : response.getDocumentResponse()) {
                System.out.println("Received file of size: " + doc.getDocument().length + " bytes");
            }
        }
    }

}
