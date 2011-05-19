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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class DocRetrieveTransformHelper {

    private static Log log = LogFactory.getLog(DocRetrieveTransformHelper.class);
    private static final String ActionInValue = "DocumentRetrieveIn";
    private static final String ActionOutValue = "DocumentRetrieveOut";

    public static CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        //TODO: Need to handle DocumentSet
        //DocRetrieveMessageType docRetrieve = event.getMessage();
        RequestType request = new RequestType();

        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }


        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);
        DocRetrieveMessageType docMessage = event.getMessage();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = docMessage.getRetrieveDocumentSetRequest();
        List<DocumentRequest> docRequestList = retrieveDocumentSetRequest.getDocumentRequest();
        if (docRequestList != null && docRequestList.size() > 0) {
            for (DocumentRequest docReq : docRequestList) {
                request.getResource().add(getResource(docReq));
            }
        }
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        CheckPolicyRequestType policyRequest = new CheckPolicyRequestType();
        policyRequest.setRequest(request);
        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    private static ResourceType getResource(DocumentRequest documentRequest) {
        String homeCommunityId = documentRequest.getHomeCommunityId();
        String repositoryUniqueId = documentRequest.getRepositoryUniqueId();
        String documentId = documentRequest.getDocumentUniqueId();
        log.debug("getResource - homeCommunityId: " + homeCommunityId);
        log.debug("getResource - repositoryUniqueId: " + repositoryUniqueId);
        log.debug("getResource - documentId: " + documentId);
        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, homeCommunityId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.RespositoryAttributeId, Constants.DataTypeString, repositoryUniqueId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.DocumentAttributeId, Constants.DataTypeString, documentId));
        return resource;
    }
}
