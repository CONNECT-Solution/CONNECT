/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclientgui.objects;

/**
 *
 * @author Duane DeCouteau
 */
public class DocumentManagerObjectManagement {
    private String patientId;
    private String requestingUserId;
    private String patientFirstName;
    private String patientLastName;
    private String patientMiddleInitial;
    private String dateOfBirth;
    private String sourceOfDocument;
    private String sourceDocumentId;
    private String sourcePatientId;
    private String sourceHomeCommunityId;
    private String sourceHomeCommunityName;
    private String sourceDocumentTitle;
    private String sourceDocumentType;
    private String sourceRepositoryId;
    private String localDocumentId;
    private String localRepositoryId;
    private boolean processingComplete;
    private boolean hasbeenViewed;
    private boolean selectedForViewing;
    private String localHash;
    private String nhinHash;


    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the requestingUserId
     */
    public String getRequestingUserId() {
        return requestingUserId;
    }

    /**
     * @param requestingUserId the requestingUserId to set
     */
    public void setRequestingUserId(String requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    /**
     * @return the patientFirstName
     */
    public String getPatientFirstName() {
        return patientFirstName;
    }

    /**
     * @param patientFirstName the patientFirstName to set
     */
    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    /**
     * @return the patientLastName
     */
    public String getPatientLastName() {
        return patientLastName;
    }

    /**
     * @param patientLastName the patientLastName to set
     */
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    /**
     * @return the patientMiddleInitial
     */
    public String getPatientMiddleInitial() {
        return patientMiddleInitial;
    }

    /**
     * @param patientMiddleInitial the patientMiddleInitial to set
     */
    public void setPatientMiddleInitial(String patientMiddleInitial) {
        this.patientMiddleInitial = patientMiddleInitial;
    }

    /**
     * @return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the sourceOfDocument
     */
    public String getSourceOfDocument() {
        return sourceOfDocument;
    }

    /**
     * @param sourceOfDocument the sourceOfDocument to set
     */
    public void setSourceOfDocument(String sourceOfDocument) {
        this.sourceOfDocument = sourceOfDocument;
    }

    /**
     * @return the sourceDocumentId
     */
    public String getSourceDocumentId() {
        return sourceDocumentId;
    }

    /**
     * @param sourceDocumentId the sourceDocumentId to set
     */
    public void setSourceDocumentId(String sourceDocumentId) {
        this.sourceDocumentId = sourceDocumentId;
    }

    /**
     * @return the sourceHomeCommunityId
     */
    public String getSourceHomeCommunityId() {
        return sourceHomeCommunityId;
    }

    /**
     * @param sourceHomeCommunityId the sourceHomeCommunityId to set
     */
    public void setSourceHomeCommunityId(String sourceHomeCommunityId) {
        this.sourceHomeCommunityId = sourceHomeCommunityId;
    }

    /**
     * @return the sourceHomeCommunityName
     */
    public String getSourceHomeCommunityName() {
        return sourceHomeCommunityName;
    }

    /**
     * @param sourceHomeCommunityName the sourceHomeCommunityName to set
     */
    public void setSourceHomeCommunityName(String sourceHomeCommunityName) {
        this.sourceHomeCommunityName = sourceHomeCommunityName;
    }

    /**
     * @return the sourceDocumentTitle
     */
    public String getSourceDocumentTitle() {
        return sourceDocumentTitle;
    }

    /**
     * @param sourceDocumentTitle the sourceDocumentTitle to set
     */
    public void setSourceDocumentTitle(String sourceDocumentTitle) {
        this.sourceDocumentTitle = sourceDocumentTitle;
    }

    /**
     * @return the sourceDocumentType
     */
    public String getSourceDocumentType() {
        return sourceDocumentType;
    }

    /**
     * @param sourceDocumentType the sourceDocumentType to set
     */
    public void setSourceDocumentType(String sourceDocumentType) {
        this.sourceDocumentType = sourceDocumentType;
    }

    /**
     * @return the localDocumentId
     */
    public String getLocalDocumentId() {
        return localDocumentId;
    }

    /**
     * @param localDocumentId the localDocumentId to set
     */
    public void setLocalDocumentId(String localDocumentId) {
        this.localDocumentId = localDocumentId;
    }

    /**
     * @return the processingComplete
     */
    public boolean isProcessingComplete() {
        return processingComplete;
    }

    /**
     * @param processingComplete the processingComplete to set
     */
    public void setProcessingComplete(boolean processingComplete) {
        this.processingComplete = processingComplete;
    }

    /**
     * @return the hasbeenViewed
     */
    public boolean isHasbeenViewed() {
        return hasbeenViewed;
    }

    /**
     * @param hasbeenViewed the hasbeenViewed to set
     */
    public void setHasbeenViewed(boolean hasbeenViewed) {
        this.hasbeenViewed = hasbeenViewed;
    }

    /**
     * @return the selectedForViewing
     */
    public boolean isSelectedForViewing() {
        return selectedForViewing;
    }

    /**
     * @param selectedForViewing the selectedForViewing to set
     */
    public void setSelectedForViewing(boolean selectedForViewing) {
        this.selectedForViewing = selectedForViewing;
    }

    /**
     * @return the localHash
     */
    public String getLocalHash() {
        return localHash;
    }

    /**
     * @param localHash the localHash to set
     */
    public void setLocalHash(String localHash) {
        this.localHash = localHash;
    }

    /**
     * @return the nhinHash
     */
    public String getNhinHash() {
        return nhinHash;
    }

    /**
     * @param nhinHash the nhinHash to set
     */
    public void setNhinHash(String nhinHash) {
        this.nhinHash = nhinHash;
    }

    /**
     * @return the sourcePatientId
     */
    public String getSourcePatientId() {
        return sourcePatientId;
    }

    /**
     * @param sourcePatientId the sourcePatientId to set
     */
    public void setSourcePatientId(String sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    /**
     * @return the sourceRepositoryId
     */
    public String getSourceRepositoryId() {
        return sourceRepositoryId;
    }

    /**
     * @param sourceRepositoryId the sourceRepositoryId to set
     */
    public void setSourceRepositoryId(String sourceRepositoryId) {
        this.sourceRepositoryId = sourceRepositoryId;
    }

    /**
     * @return the localRepositoryId
     */
    public String getLocalRepositoryId() {
        return localRepositoryId;
    }

    /**
     * @param localRepositoryId the localRepositoryId to set
     */
    public void setLocalRepositoryId(String localRepositoryId) {
        this.localRepositoryId = localRepositoryId;
    }




}
