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
package gov.hhs.fha.nhinc.docsubmission;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocSubmissionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DocSubmissionUtils.class);

    private static DocSubmissionUtils instance = new DocSubmissionUtils();

    private LargeFileUtils fileUtils = null;

    DocSubmissionUtils() {
        fileUtils = getLargeFileUtils();
    }

    /**
     * @return the singleton instance of DocSubmissionUtils
     */
    public static DocSubmissionUtils getInstance() {
        return instance;
    }

    /**
     * Checks to see if the query should be handled internally or passed through to an adapter.
     *
     * @param passThruProperty the passthough property
     * @return Returns true if the pass through property for a specified Patient Discovery Service in the
     * gateway.properties file is true.
     */
    public boolean isInPassThroughMode(String passThruProperty) {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getInstance().getPropertyBoolean(
                NhincConstants.GATEWAY_PROPERTY_FILE, passThruProperty);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file {}: {}", passThruProperty,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex.getLocalizedMessage(), ex);
        }
        return passThroughModeEnabled;
    }

    /**
     * set user spec version if target doesn't have it
     *
     * @param targets NhinTargetCommunitiesType
     * @param version service spec version
     */
    public void setTargetCommunitiesVersion(NhinTargetCommunitiesType targets, UDDI_SPEC_VERSION version) {
        if (targets == null) {
            targets = new ObjectFactory().createNhinTargetCommunitiesType();
        }

        if (StringUtils.isBlank(targets.getUseSpecVersion())) {
            targets.setUseSpecVersion(version.toString());
        }
    }

    public void convertFileLocationToDataIfEnabled(ProvideAndRegisterDocumentSetRequestType request)
        throws LargePayloadException {

        if (fileUtils.isParsePayloadAsFileLocationEnabled()) {
            try {
                for (Document doc : request.getDocument()) {
                    URI payloadUri = fileUtils.parseBase64DataAsUri(doc.getValue());
                    if (payloadUri != null) {
                        File payloadFile = new File(payloadUri);

                        DataHandler dh = fileUtils.convertToDataHandler(payloadFile);
                        doc.setValue(dh);
                    }
                }

            } catch (IOException | URISyntaxException e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    public void convertDataToFileLocationIfEnabled(ProvideAndRegisterDocumentSetRequestType request)
        throws LargePayloadException {

        if (fileUtils.isSavePayloadToFileEnabled()) {
            List<File> savedAttachmentList = new ArrayList<>();
            try {
                for (Document doc : request.getDocument()) {
                    File attachmentFile = fileUtils.saveDataToFile(doc.getValue());
                    setDocumentValueToFileURI(doc, attachmentFile);
                    savedAttachmentList.add(attachmentFile);
                }
            } catch (Exception e) {
                for (File file : savedAttachmentList) {
                    if (file.exists()) {
                        file.delete();
                    }
                }

                throw new LargePayloadException("Failed to save attachments to file system", e);
            }
        }
    }

    private void setDocumentValueToFileURI(Document doc, File file) {
        DataHandler dh = fileUtils.convertToDataHandler(file.toURI().toString());
        doc.setValue(dh);
    }

    protected LargeFileUtils getLargeFileUtils() {
        return LargeFileUtils.getInstance();
    }

    public void releaseFileLock(ProvideAndRegisterDocumentSetRequestType request) {
        if (fileUtils.isParsePayloadAsFileLocationEnabled()) {
            try {
                for (Document doc : request.getDocument()) {
                    fileUtils.closeStreamWithoutException(doc.getValue().getDataSource().getInputStream());
                }
            } catch (IOException e) {
                LOG.error("Failed to release file lock.", e);
            }
        }
    }
}
