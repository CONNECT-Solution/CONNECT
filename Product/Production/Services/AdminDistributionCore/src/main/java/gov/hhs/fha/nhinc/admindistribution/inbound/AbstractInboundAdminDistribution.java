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

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInboundAdminDistribution implements InboundAdminDistribution {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractInboundAdminDistribution.class);
    protected AdminDistributionAuditLogger auditLogger = new AdminDistributionAuditLogger();
    protected AdminDistributionUtils adminUtils = AdminDistributionUtils.getInstance();
    protected AdapterAdminDistributionProxyObjectFactory adapterFactory = new AdapterAdminDistributionProxyObjectFactory();

    public abstract void processAdminDistribution(EDXLDistribution body, AssertionType assertion);

    /**
     * This method sends sendAlertMessage to agency/agencies.
     *
     * @param body - Emergency Message Distribution Element transaction message body.
     * @param assertion - Assertion received.
     */
    @Override
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        auditRequestFromNhin(body, assertion);

        processAdminDistribution(body, assertion);
    }

    protected void auditRequestFromNhin(EDXLDistribution body, AssertionType assertion) {
        auditLogger.auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, null,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }

    protected void sendToAdapter(EDXLDistribution body, AssertionType assertion, AdminDistributionUtils adminUtils,
            AdapterAdminDistributionProxyObjectFactory adapterFactory) {
        try {
            adminUtils.convertDataToFileLocationIfEnabled(body);
            AdapterAdminDistributionProxy adapterProxy = adapterFactory.getAdapterAdminDistProxy();
            adapterProxy.sendAlertMessage(body, assertion);
        } catch (LargePayloadException lpe) {
            LOG.error("Failed to retrieve payload document.", lpe);
        }
    }
}
