/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author svalluripalli
 */
public class DocRetrieveTransformHelper {
    private static final String ActionValue = "DocumentRetrieveIn";
        
    public static CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        //TODO: Need to handle DocumentSet
        //DocRetrieveMessageType docRetrieve = event.getMessage();
        RequestType request = new RequestType();
        request.setAction(ActionHelper.actionFactory(ActionValue));
        SubjectType subject = SubjectHelper.subjectFactory(event.getSendingHomeCommunity() ,  event.getMessage().getAssertion());
        request.getSubject().add(subject);
        DocRetrieveMessageType docMessage = event.getMessage();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = docMessage.getRetrieveDocumentSetRequest();
        List<DocumentRequest> docRequestList = retrieveDocumentSetRequest.getDocumentRequest();
        if(docRequestList != null && docRequestList.size() > 0)
        {
            for(DocumentRequest docReq : docRequestList)
            {
                request.getResource().add(getResource(docReq));
            }
        }
        CheckPolicyRequestType oPolicyRequest = new CheckPolicyRequestType();
        oPolicyRequest.setRequest(request);
        PurposeForUseHelper.appendPurposeForUse(oPolicyRequest, event.getMessage().getAssertion());
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }
    
    private static ResourceType getResource(DocumentRequest documentRequest)
    {
        String homeCommunityId = documentRequest.getHomeCommunityId();
        String repositoryUniqueId = documentRequest.getRepositoryUniqueId();
        String documentId = documentRequest.getDocumentUniqueId();
        ResourceType resource = new ResourceType();
        resource.getAttribute().add(AttributeHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, homeCommunityId));
        resource.getAttribute().add(AttributeHelper.attributeFactory(Constants.RespositoryAttributeId, Constants.DataTypeString, repositoryUniqueId));
        resource.getAttribute().add(AttributeHelper.attributeFactory(Constants.DocumentAttributeId, Constants.DataTypeString, documentId));
        return resource;
    }
}
