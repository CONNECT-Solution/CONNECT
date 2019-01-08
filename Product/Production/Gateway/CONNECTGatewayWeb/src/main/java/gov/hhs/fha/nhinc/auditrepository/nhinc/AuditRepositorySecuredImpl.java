/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditRepositorySecuredImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRepositorySecuredImpl.class);

    public AuditRepositorySecuredImpl() {
    }


    protected AuditRepositoryOrchImpl getAuditRepositoryOrchImpl() {
        return new AuditRepositoryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }

    public AcknowledgementType logAudit(LogEventSecureRequestType mess, WebServiceContext context) {
        LOG.info("Entering AuditRepositoryImpl.logAudit");
        AcknowledgementType response = null;

        if (mess == null) {
            LOG.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
                    loadAssertion(assertion, context);

                    response = processor.logAudit(mess, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryImpl.logAudit. Error: " + ex.getMessage();
                    LOG.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                LOG.warn("AuditRepositoryOrchImpl was null.");
            }
        }

        LOG.info("Exiting AuditRepositoryImpl.logAudit");
        return response;
    }

    public FindCommunitiesAndAuditEventsResponseType findAudit(FindAuditEventsType query, WebServiceContext context) {
        LOG.info("Entering AuditRepositoryImpl.findAudit");
        FindCommunitiesAndAuditEventsResponseType response = null;

        if (query == null) {
            LOG.warn("request was null.");
        } else {
            try {
                AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
                loadAssertion(assertion, context);

                response = new AuditRepositoryOrchImpl().findAudit(query);
            } catch (Exception ex) {
                String message = "Error occurred calling AuditRepositoryImpl.findAudit. Error: " + ex.getMessage();
                LOG.error(message, ex);
                throw new RuntimeException(message, ex);
            }
        }

        LOG.info("Exiting AuditRepositoryImpl.findAudit");
        return response;

    }
}
