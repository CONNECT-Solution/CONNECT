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
package gov.hhs.fha.nhinc.corex12.ds.utils;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.activation.DataHandler;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public class X12LargePayloadUtils {

    private static final Logger LOG = LoggerFactory.getLogger(X12LargePayloadUtils.class);
    private static final LargeFileUtils FILE_UTILS = LargeFileUtils.getInstance();

    /**
     *
     * @param request
     * @throws LargePayloadException
     */
    public static void convertFileLocationToDataIfEnabled(COREEnvelopeBatchSubmission request)
        throws LargePayloadException {

        if (FILE_UTILS.isParsePayloadAsFileLocationEnabled()) {
            try {
                if (request.getPayload() != null) {
                    URI payloadUri = LargeFileUtils.getInstance().parseBase64DataAsUri(request.getPayload());
                    if (payloadUri != null) {
                        File payloadFile = new File(payloadUri);
                        DataHandler dh = FILE_UTILS.convertToDataHandler(payloadFile);
                        request.setPayload(dh);
                    }
                }
            } catch (URISyntaxException e) {
                throw new LargePayloadException("Unable to locate the file path to create URI.", e);
            } catch (IOException e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    /**
     *
     * @param request
     * @throws LargePayloadException
     */
    public static void convertFileLocationToDataIfEnabled(COREEnvelopeBatchSubmissionResponse request)
        throws LargePayloadException {

        if (FILE_UTILS.isParsePayloadAsFileLocationEnabled()) {
            try {
                if (request.getPayload() != null) {
                    URI payloadUri = LargeFileUtils.getInstance().parseBase64DataAsUri(request.getPayload());
                    if (payloadUri != null) {
                        File payloadFile = new File(payloadUri);
                        javax.activation.DataHandler dh = FILE_UTILS.convertToDataHandler(payloadFile);
                        request.setPayload(dh);
                    }
                }
            } catch (URISyntaxException e) {
                throw new LargePayloadException("Unable to locate the file path to create URI.", e);
            } catch (IOException e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    /**
     *
     * @param response
     * @throws LargePayloadException
     */
    public static void convertDataToFileLocationIfEnabled(COREEnvelopeBatchSubmissionResponse response)
        throws LargePayloadException {

        if (FILE_UTILS.isSavePayloadToFileEnabled()) {
            try {
                File attachmentFile = FILE_UTILS.saveDataToFile(response.getPayload());
                javax.activation.DataHandler dh = FILE_UTILS.convertToDataHandler(attachmentFile.toURI().toString());
                response.setPayload(dh);
            } catch (IOException e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    /**
     *
     * @param response
     * @throws LargePayloadException
     */
    public static void convertDataToFileLocationIfEnabled(COREEnvelopeBatchSubmission response)
        throws LargePayloadException {

        if (FILE_UTILS.isSavePayloadToFileEnabled()) {
            try {
                File attachmentFile = FILE_UTILS.saveDataToFile(response.getPayload());
                DataHandler dh = FILE_UTILS.convertToDataHandler(attachmentFile.toURI().toString());
                response.setPayload(dh);
            } catch (IOException e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    public static void releaseFileLock(COREEnvelopeBatchSubmission request) {
        if (FILE_UTILS.isParsePayloadAsFileLocationEnabled()) {
            try {
                if (request.getPayload() != null) {
                    FILE_UTILS.closeStreamWithoutException(request.getPayload().getDataSource().getInputStream());
                }
            } catch (IOException e) {
                LOG.info("Unable to release File lock", e);
            }
        }
    }
}
