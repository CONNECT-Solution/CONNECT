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
package gov.hhs.fha.nhinc.auditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.auditquery.EntityAuditLog;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxy;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class ProxyAuditLogQueryImpl {

    private static Log log = LogFactory.getLog(ProxyAuditLogQueryImpl.class);

    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsSecuredRequestType findAuditEventsRequest, WebServiceContext context) {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        FindAuditEventsRequestType request = new FindAuditEventsRequestType();
        request.setAssertion(assertion);
        request.setFindAuditEvents(findAuditEventsRequest.getFindAuditEvents());
        request.setNhinTargetSystem(findAuditEventsRequest.getNhinTargetSystem());
        return findAuditEvents(request);
    }
    /**
     * This method will perform an audit log query to a specified community on the Nhin Interface
     * and return a list of audit log records will be returned to the user.
     *
     * @param findAuditEventsRequest The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        log.debug("Entering ProxyAuditLogQueryImpl.findAuditEvents...");      

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        EntityAuditLog auditLog = new EntityAuditLog();
        AcknowledgementType ack = auditLog.audit(findAuditEventsRequest);

        NhinAuditQueryProxyObjectFactory auditFactory = new NhinAuditQueryProxyObjectFactory();
        NhinAuditQueryProxy proxy = auditFactory.getNhinAuditQueryProxy();

        log.debug("Exiting ProxyAuditLogQueryImpl.findAuditEvents...");
        return proxy.auditQuery(findAuditEventsRequest);
    }

}
