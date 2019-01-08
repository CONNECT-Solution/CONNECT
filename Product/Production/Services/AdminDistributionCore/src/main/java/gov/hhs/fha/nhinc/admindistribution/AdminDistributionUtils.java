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

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.NonXMLContentType;

/**
 * @author akong
 *
 */
public class AdminDistributionUtils {

    private static AdminDistributionUtils instance = new AdminDistributionUtils();

    private LargeFileUtils fileUtils = null;

    AdminDistributionUtils() {
        fileUtils = getLargeFileUtils();
    }

    /**
     * @return the singleton instance of http://www.google.com/
     */
    public static AdminDistributionUtils getInstance() {
        return instance;
    }

    /**
     * If enabled in the properties, this method will parse all ContentObject in the request from a base 64 encoded
     * string as a file URI and convert it as a Data Handler. This call will modify the pass in request! If an exception
     * is thrown, then the content object will not be converted back to its original value.
     *
     * @param request - the message to be converted
     * @throws LargePayloadException Throws LargePayloadException when retrieving contentObject or saving the attachment
     * to system.
     */
    public void convertFileLocationToDataIfEnabled(EDXLDistribution request) throws LargePayloadException {

        if (fileUtils.isParsePayloadAsFileLocationEnabled()) {
            try {
                if (request.getContentObject() != null) {
                    for (ContentObjectType co : request.getContentObject()) {
                        convertToDataHandler(co);
                    }
                }
            } catch (Exception e) {
                throw new LargePayloadException("Failed to attach payload to message.", e);
            }
        }
    }

    /**
     * If enabled in the properties, this method will save the ContentObject data from the request into a file in the
     * system. It will then replace the value of that element with the file URI. This call will modify the pass in
     * request! If an exception is thrown, then the content object will not be converted back to its original value.
     *
     * @param request - the message to be converted
     * @throws LargePayloadException Throws LargePayloadException when retrieving contentObject or saving the attachment
     * to system.
     *
     */
    public void convertDataToFileLocationIfEnabled(EDXLDistribution request) throws LargePayloadException {
        if (fileUtils.isSavePayloadToFileEnabled()) {
            List<File> savedAttachmentList = new ArrayList<>();
            try {
                if (request.getContentObject() != null) {
                    for (ContentObjectType co : request.getContentObject()) {
                        File attachmentFile = convertToFile(co);
                        if (attachmentFile != null) {
                            savedAttachmentList.add(attachmentFile);
                        }
                    }
                }

            } catch (Exception e) {
                deleteAllFiles(savedAttachmentList);

                throw new LargePayloadException("Failed to save attachments to file system", e);
            }
        }
    }

    private void deleteAllFiles(List<File> savedAttachmentList) {
        for (File file : savedAttachmentList) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void convertToDataHandler(ContentObjectType co) throws URISyntaxException, IOException {
        NonXMLContentType nonXmlContentType = co.getNonXMLContent();
        if (nonXmlContentType != null && nonXmlContentType.getContentData() != null) {
            URI payloadUri = fileUtils.parseBase64DataAsUri(nonXmlContentType.getContentData());
            if (payloadUri != null) {
                File payloadFile = new File(payloadUri);

                DataHandler dh = fileUtils.convertToDataHandler(payloadFile);
                nonXmlContentType.setContentData(dh);
            }
        }
    }

    private File convertToFile(ContentObjectType co) throws IOException {
        File attachmentFile = null;

        NonXMLContentType nonXmlContentType = co.getNonXMLContent();
        if (nonXmlContentType != null && nonXmlContentType.getContentData() != null) {
            attachmentFile = saveDataAttachmentToFile(nonXmlContentType);
            setDocumentValueToFileURI(nonXmlContentType, attachmentFile);
        }

        return attachmentFile;
    }

    private File saveDataAttachmentToFile(NonXMLContentType nonXmlContentType) throws IOException {
        File attachmentFile = fileUtils.generateAttachmentFile();
        fileUtils.saveDataToFile(nonXmlContentType.getContentData(), attachmentFile);

        return attachmentFile;
    }

    private void setDocumentValueToFileURI(NonXMLContentType nonXmlContentType, File file) {
        DataHandler dh = fileUtils.convertToDataHandler(file.toURI().toString());
        nonXmlContentType.setContentData(dh);
    }

    /**
     * @return an instance of LargeFileUtils.
     */
    protected LargeFileUtils getLargeFileUtils() {
        return LargeFileUtils.getInstance();
    }
}
