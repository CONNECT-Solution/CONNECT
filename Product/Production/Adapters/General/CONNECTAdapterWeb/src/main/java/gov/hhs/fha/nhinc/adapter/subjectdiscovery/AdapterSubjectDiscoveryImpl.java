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
package gov.hhs.fha.nhinc.adapter.subjectdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterSubjectDiscoveryImpl {
    public static final String AGENCY_ACK_MSG = "Successful Ack From Agency";

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null &&
                request.getPRPAIN201301UV02() != null) {
            // Extract the message id
            if (request.getPRPAIN201301UV02().getId() != null) {
                msgId = request.getPRPAIN201301UV02().getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                senderOID = request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getPRPAIN201301UV02().getSender() != null &&
                    request.getPRPAIN201301UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            ack = HL7AckTransforms.createAckMessage(null, msgId, HL7AckTransforms.ACK_TYPE_CODE_ACCEPT, AGENCY_ACK_MSG, senderOID, receiverOID);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null &&
                request.getPRPAIN201302UV02() != null) {
            // Extract the message id
            if (request.getPRPAIN201302UV02().getId() != null) {
                msgId = request.getPRPAIN201302UV02().getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                senderOID = request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getPRPAIN201302UV02().getSender() != null &&
                    request.getPRPAIN201302UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            ack = HL7AckTransforms.createAckMessage(null, msgId, HL7AckTransforms.ACK_TYPE_CODE_ACCEPT, AGENCY_ACK_MSG, senderOID, receiverOID);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest, WebServiceContext context) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest, WebServiceContext context) {
        return new PIXConsumerPRPAIN201309UVResponseType();
    }
}
