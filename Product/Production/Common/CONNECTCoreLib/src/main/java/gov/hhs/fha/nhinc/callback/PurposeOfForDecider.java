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
package gov.hhs.fha.nhinc.callback;

import gov.hhs.fha.nhinc.callback.opensaml.CallbackProperties;
import gov.hhs.fha.nhinc.callback.purposeuse.PurposeUseProxy;
import gov.hhs.fha.nhinc.callback.purposeuse.PurposeUseProxyObjectFactory;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mweaver
 *
 */
public class PurposeOfForDecider {

    private static final Logger LOG = LoggerFactory.getLogger(PurposeOfForDecider.class);

    protected NhinEndpointManager getNhinEndpointManager() {
        return new NhinEndpointManager();
    }

    protected PurposeUseProxy getPurposeUseProxyObjectFactory() {
        PurposeUseProxyObjectFactory purposeFactory = new PurposeUseProxyObjectFactory();
        return purposeFactory.getPurposeUseProxy();
    }

    public boolean isPurposeFor(CallbackProperties properties) {
        // if this isn't an Nhin Spec, just return PurposeOf
        NHIN_SERVICE_NAMES serviceName;
        boolean purposeFor = false;

        String action = properties.getAction();

        serviceName = NHIN_SERVICE_NAMES.fromValueString(action);
        if (null == serviceName) {
            LOG.warn("Could not read purpose of / for action: service name is null for action {}", action);
            return purposeFor;
        }

        // determine 2010 vs 2011 spec version
        GATEWAY_API_LEVEL apiLevel = properties.getTargetApiLevel();
        String hcid = properties.getTargetHomeCommunityId();

        if (hcid.startsWith(NhincConstants.HCID_PREFIX)) {
            hcid = hcid.replace(NhincConstants.HCID_PREFIX, "");
        }

        if (apiLevel == null && hcid != null) {
            NhinEndpointManager nem = getNhinEndpointManager();
            apiLevel = nem.getApiVersion(hcid, serviceName);
        }

        if (GATEWAY_API_LEVEL.LEVEL_g0 == apiLevel) {
            PurposeUseProxy purposeUse = getPurposeUseProxyObjectFactory();
            purposeFor = purposeUse.isPurposeForUseEnabled(properties);
        }

        if (LOG.isDebugEnabled()) {
            logPurposeDecision(purposeFor, hcid, serviceName.getUDDIServiceName());
        }

        return purposeFor;
    }

    private void logPurposeDecision(Boolean purposeFor, String hcid, String serviceName) {
        String purposeName = purposeFor ? "PURPOSE FOR" : "PURPOSE OF";
        LOG.debug("PurposeOfForDecider decision for HCID: {} and Service Name: {} is = {}.", hcid, serviceName,
            purposeName);
    }
}
