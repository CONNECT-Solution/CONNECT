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
package gov.hhs.fha.nhinc.corex12.ds.audit.transform;

import gov.hhs.fha.nhinc.corex12.ds.audit.X12AuditDataTransformConstants;

import java.io.ByteArrayOutputStream;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class X12BatchAuditTransforms extends
    X12AuditTransforms<COREEnvelopeBatchSubmission, COREEnvelopeBatchSubmissionResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(X12BatchAuditTransforms.class);

    @Override
    protected byte[] marshallToByteArrayFromRequest(COREEnvelopeBatchSubmission request) {
        byte[] bObject = null;
        if (request != null) {
            DataHandler payloadHandler = request.getPayload();
            try {
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                //since we don't audit payload element, set it to null.
                request.setPayload(null);
                Object element = new JAXBElement<>(getQname(X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI,
                    X12AuditDataTransformConstants.CORE_X12_BATCH_REQUEST_LOCALPART),
                    COREEnvelopeBatchSubmission.class, request);
                getMarshaller().marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            } catch (JAXBException ex) {
                LOG.error("Error while marshalling COREEnvelopeBatchSubmission Request: {}",
                    ex.getLocalizedMessage(), ex);
            }
            request.setPayload(payloadHandler);
        }
        return bObject;
    }

    @Override
    protected byte[] marshallToByteArrayFromResponse(COREEnvelopeBatchSubmissionResponse response) {
        byte[] bObject = null;
        if (response != null) {
            DataHandler payloadHandler = response.getPayload();
            try {
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                //since we don't audit payload element, set it to null.
                response.setPayload(null);
                Object element = new JAXBElement<>(getQname(X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI,
                    X12AuditDataTransformConstants.CORE_X12_BATCH_RESPONSE_LOCALPART),
                    COREEnvelopeBatchSubmissionResponse.class, response);
                getMarshaller().marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            } catch (JAXBException ex) {
                LOG.error("Error while marshalling COREEnvelopeBatchSubmission Response: {}",
                    ex.getLocalizedMessage(), ex);
            }
            response.setPayload(payloadHandler);
        }
        return bObject;
    }

    @Override
    protected String getPayloadFromRequest(COREEnvelopeBatchSubmission request) {
        if (request != null && request.getPayloadID() != null) {
            return request.getPayloadID();
        }
        return null;
    }

    @Override
    protected String getPayloadFromResponse(COREEnvelopeBatchSubmissionResponse response) {
        if (response != null && response.getPayloadID() != null) {
            return response.getPayloadID();
        }
        return null;
    }
}
