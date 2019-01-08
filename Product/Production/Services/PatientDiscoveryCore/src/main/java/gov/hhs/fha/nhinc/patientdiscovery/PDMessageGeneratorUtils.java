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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class PDMessageGeneratorUtils extends MessageGeneratorUtils {

    private static final PDMessageGeneratorUtils INSTANCE = new PDMessageGeneratorUtils();

    PDMessageGeneratorUtils() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static PDMessageGeneratorUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Extracts the patient id from response message subject element
     *
     * @param subject
     * @return <code>II</code> with extracted patient id
     */
    public II extractPatientIdFromSubject(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        II id = null;
        boolean hasPatient = subject != null && subject.getRegistrationEvent() != null
            && subject.getRegistrationEvent().getSubject1() != null && subject.getRegistrationEvent().getSubject1().
            getPatient() != null;
        boolean hasId = hasPatient && CollectionUtils.isNotEmpty(subject.getRegistrationEvent().getSubject1().
            getPatient().getId()) && subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null;
        if (hasId && NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0)
            .getExtension())
            && NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0)
                .getRoot())) {
            id = new II();
            id.setExtension(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            id.setRoot(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        }
        return id;
    }

    /**
     * Creates a CommunityPRPAIN201306UV02ResponseType message with the given hcid as the target. No PRPAIN201306UV02 is
     * set in the community response.
     *
     * @param hcid the value to use for the generated nhin target
     * @return the generated CommunityPRPAIN201306UV02ResponseType
     */
    public CommunityPRPAIN201306UV02ResponseType createCommunityPRPAIN201306UV02ResponseType(String hcid) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);

        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
        communityResponse.setNhinTargetCommunity(target);

        return communityResponse;
    }

    public RespondingGatewayPRPAIN201305UV02RequestType createRespondingGatewayRequest(PRPAIN201305UV02 message,
        AssertionType assertion, NhinTargetCommunitiesType targets) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(message);
        newRequest.setNhinTargetCommunities(targets);

        return newRequest;
    }

    public RespondingGatewayPRPAIN201306UV02RequestType createRespondingGatewayRequest(PRPAIN201306UV02 message,
        AssertionType assertion, NhinTargetCommunitiesType target) {
        RespondingGatewayPRPAIN201306UV02RequestType newRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201306UV02(message);
        newRequest.setNhinTargetCommunities(target);

        return newRequest;
    }

    public NhinTargetSystemType createNhinTargetSystemType(String exchangeName, String url, String hcid) {
        NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
        targetSystemType.setUrl(url);
        targetSystemType.setExchangeName(exchangeName);
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        targetSystemType.setHomeCommunity(homeCommunity);

        return targetSystemType;
    }
}
