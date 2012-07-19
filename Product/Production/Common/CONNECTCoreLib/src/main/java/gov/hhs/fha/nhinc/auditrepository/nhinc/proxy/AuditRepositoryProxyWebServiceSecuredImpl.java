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
package gov.hhs.fha.nhinc.auditrepository.nhinc.proxy;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.service.AuditRepositorySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author akong
 * 
 */
public class AuditRepositoryProxyWebServiceSecuredImpl implements AuditRepositoryProxy {

    private Log log = null;

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    public AuditRepositoryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        log.debug("Entering AuditRepositoryProxyWebServiceSecured.auditLog(...)");
        AcknowledgementType result = new AcknowledgementType();

        LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
        if (request.getAuditMessage() == null) {
            log.error("Audit Request is null");
        }
        secureRequest.setAuditMessage(request.getAuditMessage());
        secureRequest.setDirection(request.getDirection());
        secureRequest.setInterface(request.getInterface());

        try {
            if (request != null) {

                String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME);

                if (NullChecker.isNotNullish(url)) {

                    ServicePortDescriptor<AuditRepositoryManagerSecuredPortType> portDescriptor = new AuditRepositorySecuredServicePortDescriptor();

                    CONNECTClient<AuditRepositoryManagerSecuredPortType> client = CONNECTCXFClientFactory.getInstance()
                            .getCONNECTClientSecured(portDescriptor, url, assertion);

                    result = (AcknowledgementType) client.invokePort(AuditRepositoryManagerSecuredPortType.class,
                            "logEvent", secureRequest);

                } else {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                            + ").  The URL is null.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SECURE_SERVICE_NAME
                    + ").  An unexpected exception occurred.  " + "Exception: " + e.getMessage(), e);
        }

        log.debug("In AuditRepositoryProxyWebServiceSecured.auditLog(...) - completed called to ConnectionManager to retrieve endpoint.");

        return result;
    }

}
