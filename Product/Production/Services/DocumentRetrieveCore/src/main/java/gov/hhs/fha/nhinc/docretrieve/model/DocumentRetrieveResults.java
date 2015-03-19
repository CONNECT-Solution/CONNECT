/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.model;

/**
 *
 * @author achidamb
 */
public class DocumentRetrieveResults {

    private String repositoryId;
    private String documentId;
    private String HCID;
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
        return HCID;
    }

    /**
     *
     * @param HCID Set the HCID from DoucmentRetrieve Response.
     */
    public void setHCID(String HCID) {
        this.HCID = HCID;
    }

    /**
     *
     * @return Return the document byte[] to UI interface and this will be dispalyed in UI.
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
