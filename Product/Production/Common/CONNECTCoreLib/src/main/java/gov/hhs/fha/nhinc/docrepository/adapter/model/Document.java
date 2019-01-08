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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentType;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;

/**
 * Hibernate/JPA Entity for holding submitted document binary data.
 *
 * @author Patrick Lobre
 *
 */
public class Document {

    private Long repoId;
    private DocumentMetadata metadata;
    private String documentUniqueId;
    private String repositoryUniqueId;
    private byte[] rawData;


    /**
     * Copies the Document into a new detached entity.
     *
     * @param document
     */
    public Document(Document document) {
        documentUniqueId = document.getDocumentUniqueId();
        repositoryUniqueId = document.getRepositoryUniqueId();
        rawData = document.getRawData().clone();
    }

    /**
     * Creates a new one-to-one relationship with the given Document Metadata
     *
     * @param document
     */
    public Document(DocumentMetadata document) {
        documentUniqueId = document.getDocumentUniqueId();
        repositoryUniqueId = document.getNewRepositoryUniqueId();
        rawData = new byte[0];
        metadata = document;
        document.setDocument(this);
    }


    public Document() {
        rawData = new byte[0];
    }

    public Document(DocumentType document, DocumentMetadata docMetadata) {
        repoId = CoreHelpUtils.isId(document.getRepoId()) ? document.getRepoId() : null;
        metadata = docMetadata;
        documentUniqueId = null == document.getDocumentUniqueId() ? docMetadata.getDocumentUniqueId()
            : document.getDocumentUniqueId();
        repositoryUniqueId = null == document.getRepositoryUniqueId() ? docMetadata.getNewRepositoryUniqueId()
            : document.getRepositoryUniqueId();
        rawData = document.getRawData();
    }

    public DocumentType getDocumentType() {
        DocumentType build = new DocumentType();
        build.setRepoId(repoId);
        build.setDocumentid(metadata.getDocumentid());
        build.setDocumentUniqueId(documentUniqueId);
        build.setRepositoryUniqueId(repositoryUniqueId);
        build.setRawData(rawData);
        return build;
    }

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public DocumentMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DocumentMetadata metadata) {
        this.metadata = metadata;
    }

    public String getDocumentUniqueId() {
        return documentUniqueId;
    }

    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }

    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

}
