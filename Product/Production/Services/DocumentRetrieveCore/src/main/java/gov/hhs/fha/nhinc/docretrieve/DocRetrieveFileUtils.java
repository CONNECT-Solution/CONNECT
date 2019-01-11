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
package gov.hhs.fha.nhinc.docretrieve;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.activation.DataHandler;

/**
 * @author akong
 *
 */
public class DocRetrieveFileUtils {
    private LargeFileUtils fileUtils = null;

    private static DocRetrieveFileUtils instance = new DocRetrieveFileUtils();

    DocRetrieveFileUtils() {
        fileUtils = getLargeFileUtils();
    }

    /**
     * @return the singleton instance of DocRetrieveFileUtils
     */
    public static DocRetrieveFileUtils getInstance() {
        return instance;
    }

    /**
     * Parses the payload as a file URI and converts it into data handlers pointing to the actual documents.
     *
     * @param msg
     * @throws IOException
     * @throws URISyntaxException
     */
    public void convertFileLocationToDataIfEnabled(RetrieveDocumentSetResponseType msg) throws IOException,
            URISyntaxException {

        if (fileUtils.isParsePayloadAsFileLocationEnabled()) {
            List<DocumentResponse> docResponseList = msg.getDocumentResponse();
            for (DocumentResponse docResponse : docResponseList) {
                DataHandler dh = docResponse.getDocument();

                URI fileURI = fileUtils.parseBase64DataAsUri(dh);
                File file = new File(fileURI);

                DataHandler data = fileUtils.convertToDataHandler(file);
                docResponse.setDocument(data);
            }
        }
    }

    /**
     * Saves the actual documents in the response into the file system if enabled.
     *
     * @param response
     * @throws IOException
     */
    public void streamDocumentsToFileSystemIfEnabled(RetrieveDocumentSetResponseType response) throws IOException {
        if (fileUtils.isSavePayloadToFileEnabled()) {
            List<DocumentResponse> docResponseList = response.getDocumentResponse();
            if (docResponseList == null) {
                return;
            }

            for (DocumentResponse docResponse : docResponseList) {
                if (docResponse != null && docResponse.getDocument() != null) {
                    saveDocumentAndSetPayloadToFileURI(docResponse);
                }
            }
        }
    }

    private void saveDocumentAndSetPayloadToFileURI(DocumentResponse docResponse) throws IOException {
        File file = fileUtils.saveDataToFile(docResponse.getDocument());
        DataHandler dh = fileUtils.convertToDataHandler(file.toURI().toString());
        docResponse.setDocument(dh);
    }

    protected LargeFileUtils getLargeFileUtils() {
        return LargeFileUtils.getInstance();
    }

}
