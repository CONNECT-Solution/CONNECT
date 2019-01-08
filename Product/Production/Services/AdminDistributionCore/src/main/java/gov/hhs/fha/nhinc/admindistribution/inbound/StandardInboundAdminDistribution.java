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
package gov.hhs.fha.nhinc.admindistribution.inbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.aspect.EDXLDistributionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class StandardInboundAdminDistribution extends AbstractInboundAdminDistribution {

    private static final Logger LOG = LoggerFactory.getLogger(StandardInboundAdminDistribution.class);
    private AdminDistributionPolicyChecker policyChecker = new AdminDistributionPolicyChecker();

    public StandardInboundAdminDistribution() {
        super();
    }

    public StandardInboundAdminDistribution(AdminDistributionPolicyChecker policyChecker,
        AdminDistributionAuditLogger auditLogger, AdapterAdminDistributionProxyObjectFactory adapterFactory,
        AdminDistributionUtils adminUtils) {
        this.policyChecker = policyChecker;
        this.auditLogger = auditLogger;
        this.adapterFactory = adapterFactory;
        this.adminUtils = adminUtils;

    }

    @Override
    public void processAdminDistribution(EDXLDistribution body, AssertionType assertion) {
        logInfoServiceProcess(this.getClass());
        if (isPolicyValid(body, assertion)) {
            auditRequestToAdapter(body, assertion);
            sendToAdapter(body, assertion, adminUtils, adapterFactory);
        } else {
            LOG.warn("Invalid policy.  Will not send message to adapter.");
        }
    }

    private boolean isPolicyValid(EDXLDistribution body, AssertionType assertion) {
        boolean result = false;

        if (body != null) {
            result = policyChecker.checkIncomingPolicy(body, assertion);
        } else {
            LOG.warn("Admin Dist request body was null");
        }

        return result;
    }

    private void auditRequestToAdapter(EDXLDistribution body, AssertionType assertion) {
        auditLogger.auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, null,
            NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
    }

    @Override
    @InboundProcessingEvent(serviceType = "Admin Distribution", version = "", afterReturningBuilder = EDXLDistributionEventDescriptionBuilder.class, beforeBuilder = EDXLDistributionEventDescriptionBuilder.class)
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        auditRequestFromNhin(body, assertion);

        processAdminDistribution(body, assertion);
    }

}
