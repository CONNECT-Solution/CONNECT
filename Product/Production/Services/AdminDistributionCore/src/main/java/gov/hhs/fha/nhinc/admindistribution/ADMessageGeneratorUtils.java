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
package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;

/**
 * @author akong
 *
 */
public class ADMessageGeneratorUtils extends MessageGeneratorUtils {

    private static final ADMessageGeneratorUtils INSTANCE = new ADMessageGeneratorUtils();

    ADMessageGeneratorUtils() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static ADMessageGeneratorUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Converts the secured format message to the unsecured one.
     *
     * @param message
     * @param assertion
     * @param target
     * @return the unsecured format of the message
     */
    public RespondingGatewaySendAlertMessageType convertToUnsecured(
        RespondingGatewaySendAlertMessageSecuredType message, AssertionType assertion,
        NhinTargetCommunitiesType target) {
        RespondingGatewaySendAlertMessageType request = new RespondingGatewaySendAlertMessageType();

        request.setAssertion(assertion);
        request.setEDXLDistribution(message.getEDXLDistribution());
        request.setNhinTargetCommunities(message.getNhinTargetCommunities());

        return request;
    }

    public NhinTargetSystemType buildTargetSystem(UrlInfo urlInfo) {
        NhinTargetSystemType result = new NhinTargetSystemType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(urlInfo.getHcid());
        result.setHomeCommunity(hc);
        result.setUrl(urlInfo.getUrl());

        return result;
    }
}
