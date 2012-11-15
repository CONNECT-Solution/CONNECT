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
package gov.hhs.fha.nhinc.admindistribution.nhin;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.aspect.InboundProcessingEventDescriptionBuilder;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 */

public class NhinAdminDistributionOrchImpl {
    private Log log = null;

    /**
     * Constructor.
     */
    public NhinAdminDistributionOrchImpl() {
        log = createLogger();
    }

    /**
     * @return log.
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * @return AdminDistributionUtils instance.
     */
    protected AdminDistributionUtils getAdminDistributionUtils() {
        return AdminDistributionUtils.getInstance();
    }

    /**
     * This method sends sendAlertMessage to agency/agencies.
     * 
     * @param body
     *            Emergency Message Distribution Element transaction message body.
     * @param assertion
     *            Assertion received.
     */
    @InboundProcessingEvent(serviceType = "Admin Distribution", version = "2.0",
            afterReturningBuilder = InboundProcessingEventDescriptionBuilder.class,
            beforeBuilder = InboundProcessingEventDescriptionBuilder.class)
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        log.info("begin sendAlert");
        // With the one-way service in a one-machine setup,
        // we were hanging on the next webservice call.
        // sleep allows Glassfish to catch up. Only applies to one box (dev)
        // setups. Please refer to the CONNECT 3.1 Release Notes for more information.

        auditMessage(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (this.isInPassThroughMode() || checkPolicy(body, assertion)) {
            try {
                getAdminDistributionUtils().convertDataToFileLocationIfEnabled(body);
                sendToAgency(body, assertion);
            } catch (LargePayloadException lpe) {
                log.error("Failed to retrieve payload document.", lpe);
            }
        }
        log.info("End sendAlert");
    }

    /**
     * This method returns boolean true if in passthru mode.
     * 
     * @return true if in AdminDist is in passthrumode.
     */
    protected boolean isInPassThroughMode() {
        return new AdminDistributionHelper().isInPassThroughMode();
    }

    /**
     * This method forwards AdminDist message to Agency/Agencies.
     * 
     * @param body
     *            Emergency Message Distribution Element transaction message body.
     * @param assertion
     *            Assertion received.
     */
    protected void sendToAgency(EDXLDistribution body, AssertionType assertion) {
        auditMessage(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        log.debug("begin send to agency");
        this.getAdapterAdminDistProxy().sendAlertMessage(body, assertion);

    }

    /**
     * @return AdapterAdminDist Bean from Spring Proxy config file and instantiate the classes based on defined beans.
     */
    protected AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
        return this.getAdminFactory().getAdapterAdminDistProxy();
    }

    /**
     * @return AdapterAdminDistributionProxyObjectFactory instance.
     */
    protected AdapterAdminDistributionProxyObjectFactory getAdminFactory() {
        return new AdapterAdminDistributionProxyObjectFactory();
    }

    /**
     * @return AdminDist Auditlogger.
     */
    protected AdminDistributionAuditLogger getLogger() {
        return new AdminDistributionAuditLogger();
    }

    /**
     * This method checks the policy for AdminDist Service and returns boolean.
     * 
     * @param body
     *            Emergency Message Distribution Element transaction message body.
     * @param assertion
     *            Assertion received.
     * @return true if Permit; else denied.
     */
    protected boolean checkPolicy(EDXLDistribution body, AssertionType assertion) {
        boolean result = false;

        log.debug("begin checkPolicy");
        if (body != null) {
            result = this.getPolicyChecker().checkIncomingPolicy(body, assertion);
        } else {
            log.warn("EDXLDistribution was null");
        }

        log.debug("End Check Policy");
        return result;
    }

    /**
     * @return AdminDistributionPolicyChecker instance.
     */
    protected AdminDistributionPolicyChecker getPolicyChecker() {
        return new AdminDistributionPolicyChecker();
    }

    /**
     * @param body
     *            Emergency Message Distribution Element transaction message body.
     * @param assertion
     *            Assertion received.
     * @param direction
     *            The direction can be eigther outbound or inbound.
     * @param logInterface
     *            The interface can be Adapter/Entity/Nhin.
     */
    protected void auditMessage(EDXLDistribution body, AssertionType assertion, String direction, String logInterface) {
        AcknowledgementType ack = getLogger().auditNhinAdminDist(body, assertion, direction, logInterface);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }
    }
}
