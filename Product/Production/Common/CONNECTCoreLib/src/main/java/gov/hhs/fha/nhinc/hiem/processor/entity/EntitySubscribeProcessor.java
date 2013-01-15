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
package gov.hhs.fha.nhinc.hiem.processor.entity;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import gov.hhs.fha.nhinc.hiem.processor.entity.handler.EntitySubscribeHandler;
import gov.hhs.fha.nhinc.hiem.processor.entity.handler.EntitySubscribeHandlerFactory;
import gov.hhs.fha.nhinc.hiem.processor.common.PatientIdExtractor;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;

/**
 * 
 * 
 * @author Neil Webb
 */
public class EntitySubscribeProcessor {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(EntitySubscribeProcessor.class);

    public SubscribeResponse processSubscribe(Subscribe subscribe, Element subscribeElement, AssertionType assertion,
            NhinTargetCommunitiesType targetCommunitites) throws TopicNotSupportedFault, InvalidTopicExpressionFault,
            SubscribeCreationFailedFault, ResourceUnknownFault {
        SubscribeResponse response = null;
        
        auditInputMessage(subscribe, assertion,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        TopicConfigurationEntry topicConfig;
        try {
            log.debug("determine topic configuration");
            log.debug(XmlUtility.serializeElementIgnoreFaults(subscribeElement));
            topicConfig = getTopicConfiguration(subscribeElement);
            log.debug("getTopicConfiguration complete.  isnull=" + (topicConfig == null));

            if (topicConfig == null) {
                throw new SoapFaultFactory().getUnknownTopic(null);
            }
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getTopicConfigurationException(ex);
        }

        QualifiedSubjectIdentifierType patientIdentifier = new PatientIdExtractor().extractPatientIdentifier(
                subscribeElement, topicConfig);

        EntitySubscribeHandler subscribeHandler = new EntitySubscribeHandlerFactory().getEntitySubscribeHandler(
                topicConfig, patientIdentifier);
        response = subscribeHandler.handleSubscribe(topicConfig, subscribe, subscribeElement, assertion,
                targetCommunitites);

        auditResponseMessage(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        
        return response;
    }

    private TopicConfigurationEntry getTopicConfiguration(Element subscribeElement) throws TopicNotSupportedFault,
            InvalidTopicExpressionFault, ConfigurationException {
        TopicConfigurationEntry topicConfig = null;

        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();

        Element topicElement;
        try {
            log.debug("finding topic from message");
            topicElement = rootTopicExtractor.extractTopicExpressionElementFromSubscribeElement(subscribeElement);
            log.debug("complete with finding topic.  found=" + (topicElement != null));
        } catch (XPathExpressionException ex) {
            throw new SoapFaultFactory().getUnableToParseTopicExpressionFromSubscribeFault(ex);
        }

        if (topicElement != null) {
            topicConfig = TopicConfigurationManager.getInstance().getTopicConfiguration(topicElement);
            if ((topicConfig != null) && !topicConfig.isSupported()) {
                throw new SoapFaultFactory().getKnownTopicNotSupported(topicElement);
            }
        }

        return topicConfig;
    }

    /**
     * Create generic audit log for Input requests.
     * @param subscribe The subscribe message to be audited
     * @param assertion The assertion to be audited
     * @param logDirection The direction of the log being audited (Inbound or Outbound)
     * @param logInterface The interface of the log being audited (NHIN or Adapter)
     */
    private void auditInputMessage(Subscribe subscribe, AssertionType assertion,
            String logDirection, String logInterface ) {
        log.debug("In EntitySubscribeProcessor.auditInputMessage");
        AcknowledgementType ack = null;
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
                    logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    /**
     * Create generic audit log for response messages.
     * @param response the response to be audited
     * @param assertion the assertion to be audited
     * @param logDirection the direction of the log to be audited (Inbound or Outbound)
     * @param logInterface the interface of the log being audited (NHIN or Adapter)
     */
    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion, String logDirection,
            String logInterface ) {
        log.debug("In EntitySubscribeProcessor.auditResponseMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message,
                    logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe response message: " + t.getMessage(), t);
        }
    }
}
