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
package gov.hhs.fha.nhinc.adapterauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQuery;
import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryWebServiceProxy implements AdapterAuditQueryProxy {

    private static Log log = LogFactory.getLog(AdapterAuditQueryWebServiceProxy.class);
    static AdapterAuditLogQuery adapterService = new AdapterAuditLogQuery();

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        String url = null;

        try {
            log.debug("NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME);
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        AdapterAuditLogQueryPortType port = getAdapterPort(url);
        FindAuditEventsResponseType resp = port.findAuditEvents(request);

        return resp;
    }

    private AdapterAuditLogQueryPortType getAdapterPort(String url) {
        AdapterAuditLogQueryPortType port = adapterService.getAdapterAuditLogQueryPortSoap();

        log.info("Setting endpoint address to Audit Repository Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
