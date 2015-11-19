/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.Marshaller;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.hl7.v3.PRPAIN201305UV02;
import org.w3c.dom.Element;

/**
 * @author akong
 *
 */
public class MessageGeneratorUtils {

    private static MessageGeneratorUtils INSTANCE = new MessageGeneratorUtils();

    private static final String NHINC_COMMON_CONTEXT = "gov.hhs.fha.nhinc.common.nhinccommon";
    private static final String NHINC_COMMON_URN = "urn:gov:hhs:fha:nhinc:common:nhinccommon";

    private static final String OASIS_QUERY_30_CONTEXT = "oasis.names.tc.ebxml_regrep.xsd.query._3";
    private static final String OASIS_QUERY_30_URN = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0";

    private static final String HL7_V3_CONTEXT = "org.hl7.v3";
    private static final String HL7_V3_URN = "urn:hl7-org:v3";

    protected MessageGeneratorUtils() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static MessageGeneratorUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Converts the first target into a NhinTargetSystemType format.
     *
     * @param targets
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType convertFirstToNhinTargetSystemType(NhinTargetCommunitiesType targets) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        if (targets != null && targets.getNhinTargetCommunity() != null && targets.getNhinTargetCommunity().size() > 0) {
            nhinTargetSystem.setHomeCommunity(targets.getNhinTargetCommunity().get(0).getHomeCommunity());
            nhinTargetSystem.setUseSpecVersion(targets.getUseSpecVersion());
        }

        return nhinTargetSystem;
    }

    /**
     * Converts the first target into a NhinTargetSystemType format.
     *
     * @param target
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType convertToNhinTargetSystemType(NhinTargetCommunityType target) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        if (target != null && target.getHomeCommunity() != null) {
            nhinTargetSystem.setHomeCommunity(target.getHomeCommunity());
        }

        return nhinTargetSystem;
    }

    /**
     * Clones the assertion object.
     *
     * @param assertion
     * @return a cloned assertion
     */
    public AssertionType clone(AssertionType assertion) {
        QName qName = new QName(NHINC_COMMON_URN, "Assertion");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(assertion, NHINC_COMMON_CONTEXT, qName);

        return (AssertionType) marshaller.unmarshallJaxbElement(jaxbElement, NHINC_COMMON_CONTEXT);
    }

    /**
     * Clones the assertion object but with a new message id
     *
     * @param assertion
     * @return a cloned assertion but with a new message id
     */
    public AssertionType cloneWithNewMsgId(AssertionType assertion) {
        AssertionType newAssertion = clone(assertion);
        newAssertion.setMessageId(new WSAHeaderHelper().generateMessageID());

        return newAssertion;
    }

    /**
     * Clones the Adhoc Query Request.
     *
     * @param adhocQueryRequest
     * @return a cloned adhocQueryRequest
     */
    public AdhocQueryRequest clone(AdhocQueryRequest adhocQueryRequest) {
        QName qName = new QName(OASIS_QUERY_30_URN, "AdhocQueryRequest");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(adhocQueryRequest, OASIS_QUERY_30_CONTEXT, qName);

        return (AdhocQueryRequest) marshaller.unmarshallJaxbElement(jaxbElement, OASIS_QUERY_30_CONTEXT);
    }

    /**
     * Clones the PRPAIN201305UV02.
     *
     * @param request
     * @return a cloned PRPAIN201305UV02
     */
    public PRPAIN201305UV02 clone(PRPAIN201305UV02 request) {
        QName qName = new QName(HL7_V3_URN, "PRPA_IN201305UV02");
        Marshaller marshaller = new Marshaller();

        Element jaxbElement = marshaller.marshal(request, HL7_V3_CONTEXT, qName);

        return (PRPAIN201305UV02) marshaller.unmarshallJaxbElement(jaxbElement, HL7_V3_CONTEXT);
    }

    /**
     * This message generates a messageId if it is not present and set it in the assertion object. If present it will
     * format the messsageId and return it back.
     *
     * Note: AssertionType is a required element. It can never be null.
     *
     * @param assertion
     * @return messageId
     */
    public String generateMessageId(AssertionType assertion) {
        WSAHeaderHelper wsaHelper = new WSAHeaderHelper();
        String assertionMsgId = assertion.getMessageId();
        if (NullChecker.isNotNullish(assertionMsgId)) {
            return wsaHelper.fixMessageIDPrefix(assertionMsgId);
        } else {
            assertionMsgId = wsaHelper.generateMessageID();
            assertion.setMessageId(assertionMsgId);
            return assertionMsgId;
        }
    }
}
