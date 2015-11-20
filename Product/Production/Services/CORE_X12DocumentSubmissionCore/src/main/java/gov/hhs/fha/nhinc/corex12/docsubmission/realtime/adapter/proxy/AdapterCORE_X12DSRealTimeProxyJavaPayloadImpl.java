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

package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.Base64Coder;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;


/**
 *
 * @author jassmit
 */
public class AdapterCORE_X12DSRealTimeProxyJavaPayloadImpl implements AdapterCORE_X12DSRealTimeProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterCORE_X12DSRealTimeProxyJavaPayloadImpl.class);

    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest coreEnvelopeRealTimeRequest, AssertionType assertion) {
        LOG.trace("Begin AdapterCORE_X12DSRealTimeProxyJavaPayloadImpl.realTimeTransaction()");

        COREEnvelopeRealTimeResponse oResponse;
        if (coreEnvelopeRealTimeRequest != null) {
            oResponse = buildAdapterCORE_X12DSRealTimeResponseMetadata();
            logAdapterCORE_X12DSRealTimeRequest(coreEnvelopeRealTimeRequest);
        } else {
            oResponse = new COREEnvelopeRealTimeResponse();
            //TODO: Need to add error handling
        }

        LOG.trace("End AdapterCORE_X12DSRealTimeProxyJavaPayloadImpl.realTimeTransaction()");
        return oResponse;
    }

    private COREEnvelopeRealTimeResponse buildAdapterCORE_X12DSRealTimeResponseMetadata() {
        COREEnvelopeRealTimeResponse oResponse = new COREEnvelopeRealTimeResponse();
        oResponse.setPayloadType("X12_278_Response_005010X279A1");
        oResponse.setProcessingMode("RealTime");
        oResponse.setPayloadID("a81d44ae-7dec-11d0-a765-00a0c91e6ba0");
        oResponse.setTimeStamp("2007-08-30T10:20:34Z");
        oResponse.setSenderID("PayerB");
        oResponse.setReceiverID("HospitalA");
        oResponse.setCORERuleVersion("2.2.0");
        oResponse.setPayload(buildPayload());

        oResponse.setErrorCode("Success");
        oResponse.setErrorMessage("None");
        return oResponse;
    }

    private String buildPayload() {
        String payload = null;
        FileInputStream fStream = null;
        try {
            String fileName = getPropertyAccessor().getProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME, NhincConstants.CORE_X12DS_RT_DYNAMIC_DOC_FILE);
            if (NullChecker.isNotNullish(fileName)) {
                fStream = new FileInputStream(new File(fileName));
                byte[] testBytes = IOUtils.toByteArray(fStream);
                payload = new String(Base64Coder.encode(testBytes));
            }

        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to get adapter real time payload: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            LOG.warn("Unable to get adapter real time payload: " + ex.getMessage(), ex);
        } finally {
            StreamUtils.closeStreamSilently(fStream);
        }

        return payload;
    }

    private void logAdapterCORE_X12DSRealTimeRequest(COREEnvelopeRealTimeRequest coreEnvelopeRealTimeRequest) {
        LOG.info("CORE Paylod Type = " + coreEnvelopeRealTimeRequest.getPayloadType());
        LOG.info("CORE Processing Mode = " + coreEnvelopeRealTimeRequest.getProcessingMode());
        LOG.info("CORE Payload Id = " + coreEnvelopeRealTimeRequest.getPayloadID());
        LOG.info("CORE TimeStamp = " + coreEnvelopeRealTimeRequest.getTimeStamp());
        LOG.info("CORE Sender Id = " + coreEnvelopeRealTimeRequest.getSenderID());
        LOG.info("CORE Receiver Id = " + coreEnvelopeRealTimeRequest.getReceiverID());
        LOG.info("CORE Rule version = " + coreEnvelopeRealTimeRequest.getCORERuleVersion());
        LOG.debug("CORE Payload = " + Base64Coder.decodeString(coreEnvelopeRealTimeRequest.getPayload()));
    }

    protected PropertyAccessor getPropertyAccessor(){
        return PropertyAccessor.getInstance();
    }

}
