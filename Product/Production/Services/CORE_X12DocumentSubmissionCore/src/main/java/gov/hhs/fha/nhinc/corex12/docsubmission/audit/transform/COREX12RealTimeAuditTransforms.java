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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform;

import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditDataTransformConstants;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class COREX12RealTimeAuditTransforms extends
    COREX12AuditTransforms<COREEnvelopeRealTimeRequest, COREEnvelopeRealTimeResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(COREX12RealTimeAuditTransforms.class);

    @Override
    protected byte[] marshallToByteArrayFromRequest(COREEnvelopeRealTimeRequest msg) {
        byte[] bObject = null;
        if (msg != null) {
            try {
                Object element = null;
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                msg.setPayload("");
                element = new JAXBElement<>(getQname(
                    CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI,
                    CORE_X12AuditDataTransformConstants.CORE_X12_REQUEST_LOCALPART),
                    COREEnvelopeRealTimeRequest.class, msg);
                getMarshaller().marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            } catch (JAXBException ex) {
                LOG.error("Error while marshalling COREEnvelopeRealTimeRequest Request: "
                    + ex.getLocalizedMessage(), ex);
            }
        }
        return bObject;
    }

    @Override
    protected byte[] marshallToByteArrayFromResponse(COREEnvelopeRealTimeResponse msg) {
        byte[] bObject = null;
        if (msg != null) {
            try {
                Object element = null;
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                msg.setPayload("");
                element = new JAXBElement<>(getQname(
                    CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI,
                    CORE_X12AuditDataTransformConstants.CORE_X12_RESPONSE_LOCALPART),
                    COREEnvelopeRealTimeResponse.class, msg);
                getMarshaller().marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            } catch (JAXBException ex) {
                LOG.error("Error while marshalling COREEnvelopeRealTimeResponse Response: "
                    + ex.getLocalizedMessage(), ex);
            }
        }
        return bObject;
    }

    @Override
    protected String getPayloadFromRequest(COREEnvelopeRealTimeRequest request) {
        if (request != null && request.getPayloadID() != null) {
            return request.getPayloadID();
        }
        return null;
    }

    @Override
    protected String getPayloadFromResponse(COREEnvelopeRealTimeResponse response) {
        if (response != null && response.getPayloadID() != null) {
            return response.getPayloadID();
        }
        return null;
    }

}
