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
package gov.hhs.fha.nhinc.docretrieve.model;

/**
 *
 * @author achidamb
 */
public class DocumentRetrieveResults {

    private String repositoryId;
    private String documentId;
    private String hcid;
    private byte[] document;
    private String contentType;

    /**
     *
     * @return Return the Document ContentType to UI interface.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @param contentType Set the Document ContentType from DocumentRetrieve Response.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     *
     * @return Return the Document RepositoryId to UI interface
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     *
     * @param repositoryId Set the Document Repository Id from DocumentRetrieve Response.
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     *
     * @return Return the DocumentUniqueId to UI interface
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     *
     * @param documentId Set the DocumentUniqueId from DocumentRetrieve Response.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     *
     * @return Return the HCID to UI interface
     */
    public String getHCID() {
        return hcid;
    }

    /**
     *
     * @param HCID Set the HCID from DoucmentRetrieve Response.
     */
    public void setHCID(String HCID) {
        this.hcid = HCID;
    }

    /**
     *
     * @return Return the document byte[] to UI interface and this will be displayed in UI.
     */
    public byte[] getDocument() {
        return document;
    }

    /**
     *
     * @param document Set the document from DocumentRetrieve Response. The response will be returning the document as a
     * DataHandler and further manipulation is done to return it as a byte[] array.
     */
    public void setDocument(byte[] document) {
        this.document = document;
    }

}
