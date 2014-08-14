/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * @author cmay
 *
 */
public class AdapterCORE_X12DSRealTimeOrchImpl {

    Log log = LogFactory.getLog(AdapterCORE_X12DSRealTimeOrchImpl.class);

    public COREEnvelopeRealTimeResponse realTimeRequest(COREEnvelopeRealTimeRequest msg, AssertionType assertion) {
        log.info("Begin AdapterCORE_X12DSRealTimeOrchImpl.realTimeRequest()");
        log.info("CORE Paylod Type = " + msg.getPayloadType());
        log.info("CORE Processing Mode = " + msg.getProcessingMode());
        log.info("CORE Rule version = " + msg.getCORERuleVersion());
        log.info("CORE Payload Id = " + msg.getPayloadID());
        log.info("CORE Receiver Id = " + msg.getReceiverID());
        log.info("CORE Sender Id = " + msg.getSenderID());
        log.info("CORE TimeStamp = " + msg.getTimeStamp());
        log.info("CORE Payload = " + msg.getPayload());
        log.info("End AdapterCORE_X12DSRealTimeOrchImpl.realTimeRequest()");
        return new COREEnvelopeRealTimeResponse();
    }
}
