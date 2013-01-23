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
package gov.hhs.fha.nhinc.admindistribution.inbound;

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 * @author akong
 * 
 */
public class PassthroughInboundAdminDistribution extends AbstractInboundAdminDistribution {

    private static final Logger LOG = Logger.getLogger(PassthroughInboundAdminDistribution.class);
    private AdminDistributionUtils adminUtils = AdminDistributionUtils.getInstance();
    private AdapterAdminDistributionProxyObjectFactory adapterFactory = new AdapterAdminDistributionProxyObjectFactory();

    /**
     * Constructor.
     */
    public PassthroughInboundAdminDistribution() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param auditLogger
     * @param adminUtils
     * @param adapterFactory
     */
    public PassthroughInboundAdminDistribution(AdminDistributionAuditLogger auditLogger,
            AdminDistributionUtils adminUtils, AdapterAdminDistributionProxyObjectFactory adapterFactory) {
        this.auditLogger = auditLogger;
        this.adminUtils = adminUtils;
        this.adapterFactory = adapterFactory;
    }

    @Override
    void processAdminDistribution(EDXLDistribution body, AssertionType assertion) {
        try {
            adminUtils.convertDataToFileLocationIfEnabled(body);
            sendToAdapter(body, assertion);
        } catch (LargePayloadException lpe) {
            LOG.error("Failed to retrieve payload document.", lpe);
        }
    }

    private void sendToAdapter(EDXLDistribution body, AssertionType assertion) {
        auditRequestToAdapter(body, assertion);

        AdapterAdminDistributionProxy adapterProxy = adapterFactory.getAdapterAdminDistProxy();
        adapterProxy.sendAlertMessage(body, assertion);
    }

    private void auditRequestToAdapter(EDXLDistribution body, AssertionType assertion) {
        auditLogger.auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                null, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
    }

}
