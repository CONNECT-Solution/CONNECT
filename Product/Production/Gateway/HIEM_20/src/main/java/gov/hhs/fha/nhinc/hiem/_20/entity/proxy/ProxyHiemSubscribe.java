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
package gov.hhs.fha.nhinc.hiem._20.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationProducer", portName = "NhincProxyNotificationProducerPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribe/NhincProxySubscriptionManagement.wsdl")
@HandlerChain(file = "ProxyHiemSubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyHiemSubscribe {

    @Resource
    private WebServiceContext context;
    
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType subscribeRequest)
            throws UnsupportedPolicyRequestFault, UnrecognizedPolicyRequestFault, TopicExpressionDialectUnknownFault,
            ResourceUnknownFault, SubscribeCreationFailedFault, TopicNotSupportedFault,
            InvalidProducerPropertiesExpressionFault, UnacceptableInitialTerminationTimeFault, InvalidFilterFault,
            InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, InvalidMessageContentExpressionFault {
    	ProxyHiemSubscribeImpl hiemSubscribeImpl = new ProxyHiemSubscribeImpl();
        try {
            return hiemSubscribeImpl.subscribe(subscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex) {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex) {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex) {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex) {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex) {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex) {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex) {
            throw new InvalidFilterFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex) {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }
    }

}
