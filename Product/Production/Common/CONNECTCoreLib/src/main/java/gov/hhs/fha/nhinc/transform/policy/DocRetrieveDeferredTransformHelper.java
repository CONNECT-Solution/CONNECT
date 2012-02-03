/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredTransformHelper {
    private static final String ActionInValue = "DocRetrieveDeferredResponseIn";
    private static final String ActionOutValue = "DocRetrieveDeferredResponseOut";
    private static Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public DocRetrieveDeferredTransformHelper() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param message
     * @param assertion
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformDocRetrieveDeferredRespToCheckPolicy(DocRetrieveResultEventType event) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredTransformHelper.transformDocRetrieveDeferredRespToCheckPolicy --");
        }
        CheckPolicyRequestType checkPolicyRequest = null;
        if (null == event) {
            if (debugEnabled) {
                log.debug("Request is null.");
            }
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
            RequestType request = new RequestType();
            SubjectHelper subjHelp = new SubjectHelper();
            SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
            request.getSubject().add(subject);
            if (debugEnabled) {
                log.debug("transformDocRetrieveDeferredRespToCheckPolicy - adding assertion data");
            }
            AssertionHelper assertHelp = new AssertionHelper();
            assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());
            if (NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION.equals(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionInValue));
            } else if (NhincConstants.POLICYENGINE_INBOUND_DIRECTION.equals(event.getDirection())) {
                request.setAction(ActionHelper.actionFactory(ActionOutValue));
            }
            if (event.getMessage() != null && event.getMessage().getRetrieveDocumentSetResponse() != null && event.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse() != null && event.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().size() > 0) {
                DocumentResponse documentResponse = event.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().get(0);
                request.getResource().add(getResource(documentResponse));
            }
            checkPolicyRequest.setRequest(request);
            checkPolicyRequest.setAssertion(event.getMessage().getAssertion());
        }
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredTransformHelper.transformDocRetrieveDeferredToCheckPolicy --");
        }
        return checkPolicyRequest;
    }

    /**
     * 
     * @param hc
     * @param assertion
     * @return SubjectType
     */
    protected SubjectType createSubject(HomeCommunityType hc, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredTransformHelper.createSubject --");
        }
        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(hc, assertion);
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredTransformHelper.createSubject --");
        }
        return subject;
    }

    /**
     * 
     * @param documentRequest
     * @return ResourceType
     */
    private static ResourceType getResource(DocumentResponse documentResponse) {
        String homeCommunityId = documentResponse.getHomeCommunityId();
        String repositoryUniqueId = documentResponse.getRepositoryUniqueId();
        String documentId = documentResponse.getDocumentUniqueId();
        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString, homeCommunityId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.RespositoryAttributeId, Constants.DataTypeString, repositoryUniqueId));
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.DocumentAttributeId, Constants.DataTypeString, documentId));
        return resource;
    }
    
}
