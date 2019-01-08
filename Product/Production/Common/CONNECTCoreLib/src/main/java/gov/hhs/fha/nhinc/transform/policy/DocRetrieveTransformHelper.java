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
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public class DocRetrieveTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DocRetrieveTransformHelper.class);
    private static final String ActionInValue = "DocumentRetrieveIn";
    private static final String ActionOutValue = "DocumentRetrieveOut";

    public static CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        // TODO: Need to handle DocumentSet
        RequestType request = new RequestType();

        if (InboundOutboundChecker.isInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.isOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(),
            event.getMessage().getAssertion());
        request.getSubject().add(subject);
        DocRetrieveMessageType docMessage = event.getMessage();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = docMessage.getRetrieveDocumentSetRequest();
        List<DocumentRequest> docRequestList = retrieveDocumentSetRequest.getDocumentRequest();
        if (CollectionUtils.isNotEmpty(docRequestList)) {
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
        LOG.debug("getResource - homeCommunityId: " + homeCommunityId);
        LOG.debug("getResource - repositoryUniqueId: " + repositoryUniqueId);
        LOG.debug("getResource - documentId: " + documentId);
        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId,
            Constants.DataTypeString, homeCommunityId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.RespositoryAttributeId,
            Constants.DataTypeString, repositoryUniqueId));
        resource.getAttribute()
        .add(attrHelper.attributeFactory(Constants.DocumentAttributeId, Constants.DataTypeString, documentId));
        return resource;
    }
}
