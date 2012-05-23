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
package gov.hhs.fha.nhinc.hiem.processor.faults;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.b_2.InvalidTopicExpressionFaultType;
import org.oasis_open.docs.wsn.b_2.NotifyMessageNotSupportedFaultType;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;
import org.oasis_open.docs.wsn.b_2.TopicNotSupportedFaultType;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.oasis_open.docs.wsrf.bf_2.BaseFaultType.Description;
import org.oasis_open.docs.wsrf.bf_2.BaseFaultType.FaultCause;
import org.w3c.dom.Node;

/**
 * 
 * @author rayj
 */
public class SoapFaultFactory {
    // todo: after i can see what the faults look like, we can choose to populate the inner fault info"

    // public SubscribeCreationFailedFault getMiscError() {
    // SubscribeCreationFailedFaultType faultInfo = null;
    // //Throwable cause = null;
    // return new SubscribeCreationFailedFault("Unknown error occurred", faultInfo);
    // }
    public InvalidTopicExpressionFault getUnableToParseTopicExpressionFromSubscribeFault(Throwable t) {
        InvalidTopicExpressionFaultType faultInfo = null;
        return new InvalidTopicExpressionFault("Unable to parse topic expression from subscribe", faultInfo, t);
    }

    public SubscribeCreationFailedFault getTopicConfigurationException(Throwable t) {
        SubscribeCreationFailedFaultType faultInfo = null;
        // Throwable cause = null;
        return new SubscribeCreationFailedFault("Topic configuration exception occurred", faultInfo, t);
    }

    public SubscribeCreationFailedFault getConfigurationException(Throwable t) {
        SubscribeCreationFailedFaultType faultInfo = null;
        return new SubscribeCreationFailedFault("Configuration exception occurred", faultInfo, t);
    }

    public ConfigurationException getConfigurationException(String message, Throwable t) {
        return new ConfigurationException("Configuration exception occurred.  " + message, t);
    }

    public SubscribeCreationFailedFault getUnableToExtractPatientIdentifierFromSubscribe(Throwable t) {
        SubscribeCreationFailedFaultType faultInfo = null;
        // Throwable cause = null;
        return new SubscribeCreationFailedFault(
                "An error occurred extracting the patient identifier from the subscribe message", faultInfo, t);
    }

    public SubscribeCreationFailedFault getMalformedSubscribe(String message, Throwable t) {
        SubscribeCreationFailedFaultType faultInfo = null;
        // Throwable cause = null;
        return new SubscribeCreationFailedFault("Malformed Subscribe. " + message, faultInfo, t);
    }

    public SubscribeCreationFailedFault getMalformedSubscribeResponse(String message, Throwable t) {
        SubscribeCreationFailedFaultType faultInfo = null;
        // Throwable cause = null;
        return new SubscribeCreationFailedFault("Malformed Subscribe Response. " + message, faultInfo, t);
    }

    public NotifyMessageNotSupportedFault getSubscriptionsNotSupported() {
        NotifyMessageNotSupportedFaultType faultInfo = null;
        // Throwable cause = null;
        return new NotifyMessageNotSupportedFault("HIEM is not supported by this gateway configuration.", faultInfo);
    }

    public SubscribeCreationFailedFault getUnknownSubscriptionServiceMode(String serviceMode) {
        SubscribeCreationFailedFaultType faultInfo = null;
        // Throwable cause = null;
        return new SubscribeCreationFailedFault("Unknown subscription service mode '" + serviceMode + "'", faultInfo);
    }

    public SubscribeCreationFailedFault getUnknownSubscriptionServiceAdapterModeFault(
            String childAdapterSubscriptionMode) {
        SubscribeCreationFailedFaultType faultInfo = null;
        String message = "Unknown child adapter subscription mode '" + childAdapterSubscriptionMode + "'";
        return new SubscribeCreationFailedFault(message, faultInfo);

    }

    public SubscribeCreationFailedFault getPatientNotInSubscribeMessage() {                
        return new SubscribeCreationFailedFault("Patient required for this type of topic, but not supplied.");
    }

    public TopicNotSupportedFault getUnknownTopic(Node topic) {
        String serializedTopic = XmlUtility.getNodeValue(topic);

        TopicNotSupportedFaultType faultInfo = new TopicNotSupportedFaultType();

        // Throwable cause = null;
        TopicNotSupportedFault fault = null;
        if (NullChecker.isNullish(serializedTopic)) {
            fault = new TopicNotSupportedFault("Unknown topic '" + serializedTopic + "'", faultInfo);
        } else {
            fault = new TopicNotSupportedFault("Unknown topic", faultInfo);
        }
        return fault;
    }

    public TopicNotSupportedFault getKnownTopicNotSupported(Node topic) {
        String serializedTopic = XmlUtility.getNodeValue(topic);

        TopicNotSupportedFaultType faultInfo = new TopicNotSupportedFaultType();
        // Throwable cause = null;
        TopicNotSupportedFault fault = new TopicNotSupportedFault("Topic '" + serializedTopic
                + "' is not supported by this gateway configuration", faultInfo);
        return fault;
    }

    public SubscribeCreationFailedFault getFailedToForwardSubscribeToAgencyFault(Exception ex) {
        SubscribeCreationFailedFault fault;
        String message = null;
        // if (ex instanceof ClientTransportException) {
        // message = "Failed to forward subscribe to agency due to ClientTransportException.";
        // } else {
        message = "Failed to forward subscribe to agency. [" + ex.getMessage() + "]";
        // }

        // FaultCause faultCause = new FaultCause();
        // faultCause.setAny(ex);

        SubscribeCreationFailedFaultType faultInfo = null;
        faultInfo = new SubscribeCreationFailedFaultType();
        // faultInfo.setFaultCause(faultCause);
        // Description description = new Description();
        // description.setValue("Something bad happened");
        // faultInfo.getDescription().add(description);

        fault = new SubscribeCreationFailedFault(message, faultInfo);
        return fault;

    }

}
