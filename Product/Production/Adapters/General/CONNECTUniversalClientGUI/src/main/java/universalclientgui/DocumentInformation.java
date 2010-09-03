/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

/**
 *
 * @author patlollav
 */
public class DocumentInformation {
    private String uniquePatientID;
    private String documentID;
    private String creationDate;
    private String title;
    private String documentType;
    private String institution;
    private String homeCommunityID;
    private String repositoryUniqueID;

    public String getHomeCommunityID() {
        return homeCommunityID;
    }

    public void setHomeCommunityID(String homeCommunityID) {
        this.homeCommunityID = homeCommunityID;
    }

    public String getRepositoryUniqueID() {
        return repositoryUniqueID;
    }

    public void setRepositoryUniqueID(String repositoryUniqueID) {
        this.repositoryUniqueID = repositoryUniqueID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getUniquePatientID() {
        return uniquePatientID;
    }

    public void setUniquePatientID(String uniquePatientID) {
        this.uniquePatientID = uniquePatientID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
