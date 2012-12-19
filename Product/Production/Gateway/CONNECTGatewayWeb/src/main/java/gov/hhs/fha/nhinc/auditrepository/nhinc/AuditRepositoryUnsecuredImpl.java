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
package gov.hhs.fha.nhinc.auditrepository.nhinc;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;

import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author mflynn02
 */
public class AuditRepositoryUnsecuredImpl {
    private static final Logger LOG = Logger.getLogger(AuditRepositoryUnsecuredImpl.class);

    protected AuditRepositoryOrchImpl getAuditRepositoryOrchImpl() {
        return new AuditRepositoryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }

    public AcknowledgementType logEvent(LogEventRequestType logEventRequest, WebServiceContext context) {
        LOG.info("Entering AuditRepositoryUnsecuredImpl.logAudit");
        AcknowledgementType response = null;

        if (logEventRequest == null) {
            LOG.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = logEventRequest.getAssertion();
                    loadAssertion(assertion, context);

                    LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
                    secureRequest.setAuditMessage(logEventRequest.getAuditMessage());
                    secureRequest.setDirection(logEventRequest.getDirection());
                    secureRequest.setInterface(logEventRequest.getInterface());

                    response = processor.logAudit(secureRequest, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryImpl.logAudit. Error: " + ex.getMessage();
                    LOG.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                LOG.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        LOG.info("Exiting AuditRepositoryUnsecuredImpl.logAudit");
        return response;
    }

    public FindCommunitiesAndAuditEventsResponseType queryAuditEvents(
            FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest, WebServiceContext context) {
        LOG.info("Entering AuditRepositoryUnsecuredImpl.queryAuditEvents");
        FindCommunitiesAndAuditEventsResponseType response = null;

        if (queryAuditEventsRequest == null) {
            LOG.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = queryAuditEventsRequest.getAssertion();
                    loadAssertion(assertion, context);

                    response = processor.findAudit(queryAuditEventsRequest.getFindAuditEvents(), assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryUnsecuredImpl.queryAuditEvents. Error: "
                            + ex.getMessage();
                    LOG.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                LOG.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        LOG.info("Exiting AuditRepositoryUnsecuredImpl.queryAuditEvents");
        return response;

    }

}
