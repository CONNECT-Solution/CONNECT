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

package gov.hhs.fha.nhinc.auditrepository.nhinc;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.auditrepository.nhinc.AuditRepositoryOrchImpl;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
/**
 *
 * @author mflynn02
 */
public class AuditRepositoryUnsecuredImpl {
    private Log log = null;

    public AuditRepositoryUnsecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected AuditRepositoryOrchImpl getAuditRepositoryOrchImpl() {
        return new AuditRepositoryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }
    public AcknowledgementType logEvent(LogEventRequestType logEventRequest, WebServiceContext context) {
        log.info("Entering AuditRepositoryUnsecuredImpl.logAudit");
        AcknowledgementType response = null;

        if (logEventRequest == null) {
            log.warn("request was null.");
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
                    String message = "Error occurred calling AuditRepositoryImpl.logAudit. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryUnsecuredImpl.logAudit");
        return response;
    }
    public FindCommunitiesAndAuditEventsResponseType queryAuditEvents(FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest, WebServiceContext context) {
        log.info("Entering AuditRepositoryUnsecuredImpl.queryAuditEvents");
        FindCommunitiesAndAuditEventsResponseType response = null;

        if (queryAuditEventsRequest == null) {
            log.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = queryAuditEventsRequest.getAssertion();
                    loadAssertion(assertion, context);

                    response = processor.findAudit(queryAuditEventsRequest.getFindAuditEvents(), assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryUnsecuredImpl.queryAuditEvents. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryUnsecuredImpl.queryAuditEvents");
        return response;

    }

}
