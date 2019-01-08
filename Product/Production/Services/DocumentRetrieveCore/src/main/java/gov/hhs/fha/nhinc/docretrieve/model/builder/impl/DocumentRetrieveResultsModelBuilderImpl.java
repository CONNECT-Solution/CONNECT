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
package gov.hhs.fha.nhinc.docretrieve.model.builder.impl;

import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieveResults;
import gov.hhs.fha.nhinc.docretrieve.model.builder.DocumentRetrieveResultsModelBuilder;
import gov.hhs.fha.nhinc.util.StreamUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class DocumentRetrieveResultsModelBuilderImpl implements DocumentRetrieveResultsModelBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRetrieveResultsModelBuilderImpl.class);

    private DocumentRetrieveResults results = null;

    private RetrieveDocumentSetResponseType response = null;

    @Override
    public DocumentRetrieveResults getDocumentRetrieveResultsModel() {
        return results;
    }

    @Override
    public void setRetrieveDocumentSetResponseType(RetrieveDocumentSetResponseType response) {
        this.response = response;
    }

    @Override
    public void build() {
        results = new DocumentRetrieveResults();
        DocumentResponse docResponse = getRetrieveDocumentResponse(response);
        if (docResponse != null) {
            results.setDocumentId(docResponse.getDocumentUniqueId());
            results.setHCID(docResponse.getHomeCommunityId());
            results.setRepositoryId(docResponse.getRepositoryUniqueId());
            results.setDocument(getDocument(docResponse.getDocument()));
            results.setContentType(docResponse.getMimeType());
        }
    }

    private static DocumentResponse getRetrieveDocumentResponse(RetrieveDocumentSetResponseType response) {
        DocumentResponse documentResponse = null;
        if (response != null && CollectionUtils.isNotEmpty(response.getDocumentResponse())) {
            documentResponse = response.getDocumentResponse().get(0);
        }
        return documentResponse;
    }

    private static byte[] getDocument(DataHandler document) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = null;
        try {
            in = document.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) >= 0) {
                bos.write(buffer, 0, bytesRead);
            }

        } catch (IOException ex) {
            LOG.error("Error while reading the document: {}", ex.getLocalizedMessage(), ex);
        } finally {
            StreamUtils.closeStreamSilently(in);
        }
        return bos.toByteArray();
    }
}
