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
package gov.hhs.fha.nhinc.hiem._20.subscribe.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import gov.hhs.fha.nhinc.subscribe.entity.EntitySubscribeOrchImpl;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 *
 * @author Neil Webb
 */
public class EntitySubscribeServiceImpl {

    private static Log log = LogFactory.getLog(EntitySubscribeServiceImpl.class);

    private EntitySubscribeOrchImpl orchImpl;
    
    public EntitySubscribeServiceImpl(EntitySubscribeOrchImpl orchImpl){
    	this.orchImpl = orchImpl;
    }
    
    public SubscribeDocumentResponseType subscribeDocument(SubscribeDocumentRequestType arg0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public SubscribeDocumentResponseType subscribeDocument(SubscribeDocumentRequestSecuredType arg0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType subscribeRequest,
            WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault,
            InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault,
            SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault,
            TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault,
            UnsupportedPolicyRequestFault {
        log.debug("In subscribe");

        AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);

        Subscribe subscribe = subscribeRequest.getSubscribe();
        NhinTargetCommunitiesType targetCommunitites = subscribeRequest.getNhinTargetCommunities();

        SubscribeResponse response = null;
        try {
            response = subscribe(subscribe, assertion, targetCommunitites);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } 

        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest, WebServiceContext context)
                    throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault,
                    gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault,
                    gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault {
        log.debug("In subscribe");
        
        AssertionType assertion = subscribeRequest.getAssertion();

        Subscribe subscribe = subscribeRequest.getSubscribe();
        NhinTargetCommunitiesType targetCommunitites = subscribeRequest.getNhinTargetCommunities();

        SubscribeResponse response = null;
        try {
            response = subscribe(subscribe, assertion, targetCommunitites);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault(ex.getMessage(),
                    ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault(ex.getMessage(),
                    ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault(ex.getMessage(),
                    ex.getFaultInfo(), ex.getCause());
        }
        return response;
    }

    private org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(Subscribe subscribe,
            AssertionType assertion, NhinTargetCommunitiesType targetCommunitites)
                    throws org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault,
                    org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault,
                    org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault {
        SubscribeResponse response = null;
        response = getOrchImpl().processSubscribe(subscribe, assertion, targetCommunitites);
        return response;
    }
    
    /**
    * return the orchImpl object.
    * @return
    */
    protected EntitySubscribeOrchImpl getOrchImpl(){
    	return this.orchImpl;
    }
}
