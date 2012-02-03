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
package gov.hhs.fha.nhinc.nhinauditquery.proxy;

import com.nhin.services.AuditLogQuery;
import com.nhin.services.FindAuditEvents;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinAuditQueryWebServiceProxy implements NhinAuditQueryProxy {

    private static Log log = LogFactory.getLog(NhinAuditQueryWebServiceProxy.class);
    static FindAuditEvents nhinService = new FindAuditEvents();

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        String url = null;
        FindAuditEventsResponseType results = new FindAuditEventsResponseType();

        if (request.getNhinTargetSystem() != null) {
            try {
                url = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTarget(request.getNhinTargetSystem(), NhincConstants.AUDIT_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_QUERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        if (NullChecker.isNotNullish(url)) {
            AuditLogQuery port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            // Set up the parameters to the Nhin Audit Query call
            String patientId = null;
            if (NullChecker.isNotNullish(request.getFindAuditEvents().getPatientId())) {
                patientId = request.getFindAuditEvents().getPatientId();
            }

            String userId = null;
            if (NullChecker.isNotNullish(request.getFindAuditEvents().getUserId())) {
                userId = request.getFindAuditEvents().getUserId();
            }

            XMLGregorianCalendar beginDateTime = null;
            if (request.getFindAuditEvents().getBeginDateTime() != null) {
                beginDateTime = request.getFindAuditEvents().getBeginDateTime();
            }

            XMLGregorianCalendar endDateTime = null;
            if (request.getFindAuditEvents().getEndDateTime() != null) {
                endDateTime = request.getFindAuditEvents().getEndDateTime();
            }

            List<AuditMessageType> auditMsgList = port.findAuditEvents(patientId, userId, beginDateTime, endDateTime);

            // Set up the return value
            results.getFindAuditEventsReturn().addAll(auditMsgList);

        } else {
            log.error("The URL for service: " + NhincConstants.AUDIT_QUERY_SERVICE_NAME + " is null");
        }

        return results;
    }

    private AuditLogQuery getPort(String url) {
        AuditLogQuery port = nhinService.getAuditLogQuery();

        log.info("Setting endpoint address to Nhin Audit Query Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
