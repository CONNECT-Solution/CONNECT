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
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecured;
import gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author jhoppesc
 */
public class ProxyAuditQueryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ProxyAuditQueryImpl.class);
    private static NhincProxyAuditLogQuerySecured service = new NhincProxyAuditLogQuerySecured();

    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_AUDIT_QUERY_SECURED_SERVICE_NAME);

            NhincProxyAuditLogQuerySecuredPortType port = getPort(url);

            AssertionType assertIn = findAuditEventsRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
  
            gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType();
            body.setFindAuditEvents(findAuditEventsRequest.getFindAuditEvents());
            body.setNhinTargetSystem(findAuditEventsRequest.getNhinTargetSystem());

            response = port.findAuditEvents(body);
        }
        catch (Exception ex)
        {
            log.error("Failed to send entity audit query from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private NhincProxyAuditLogQuerySecuredPortType getPort(String url) {
        NhincProxyAuditLogQuerySecuredPortType port = service.getNhincProxyAuditLogQuerySecuredPortSoap();

        log.info("Setting endpoint address to NHIN Proxy Audit Query Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

}
