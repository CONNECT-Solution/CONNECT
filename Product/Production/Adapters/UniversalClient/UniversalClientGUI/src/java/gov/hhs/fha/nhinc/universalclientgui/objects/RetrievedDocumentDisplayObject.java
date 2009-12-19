/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclientgui.objects;

/**
 *
 * @author Duane DeCouteau
 */
public class RetrievedDocumentDisplayObject {
    private boolean selected = false;
    private String organizationName;
    private String orgId;
    private String documentTitle;
    private String documentType;
    private String uniqueDocumentId;
    private boolean availableInLocalStore;
    private boolean hasBeenAccessed = false;
    private String documentStatus;
    private String requestingUser;
    private String patientId;
    private String creationDate;
    private String repositoryId;

    public RetrievedDocumentDisplayObject() {

    }

    public RetrievedDocumentDisplayObject(boolean selected, String organizationName, String orgId, String documentTitle,
                                          String documentType, String uniqueDocumentId, boolean availableInLocalStore,
                                          boolean hasbeenaccessed, String status, String requestinguser, String patientId,
                                          String creationdate, String respositoryid) {
        this.selected = selected;
        this.organizationName = organizationName;
        this.orgId = orgId;
        this.documentTitle = documentTitle;
        this.documentType = documentType;
        this.uniqueDocumentId = uniqueDocumentId;
        this.availableInLocalStore = availableInLocalStore;
        this.hasBeenAccessed = hasbeenaccessed;
        this.documentStatus = status;
        this.requestingUser = requestinguser;
        this.patientId = patientId;
        this.creationDate = creationdate;
        this.repositoryId = respositoryid;
    }




    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the documentTitle
     */
    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * @param documentTitle the documentTitle to set
     */
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return the uniqueDocumentId
     */
    public String getUniqueDocumentId() {
        return uniqueDocumentId;
    }

    /**
     * @param uniqueDocumentId the uniqueDocumentId to set
     */
    public void setUniqueDocumentId(String uniqueDocumentId) {
        this.uniqueDocumentId = uniqueDocumentId;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the availableInLocalStore
     */
    public boolean isAvailableInLocalStore() {
        return availableInLocalStore;
    }

    /**
     * @param availableInLocalStore the availableInLocalStore to set
     */
    public void setAvailableInLocalStore(boolean availableInLocalStore) {
        this.availableInLocalStore = availableInLocalStore;
    }

    /**
     * @return the hasBeenAccessed
     */
    public boolean isHasBeenAccessed() {
        return hasBeenAccessed;
    }

    /**
     * @param hasBeenAccessed the hasBeenAccessed to set
     */
    public void setHasBeenAccessed(boolean hasBeenAccessed) {
        this.hasBeenAccessed = hasBeenAccessed;
    }

    /**
     * @return the documentStatus
     */
    public String getDocumentStatus() {
        return documentStatus;
    }

    /**
     * @param documentStatus the documentStatus to set
     */
    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    /**
     * @return the requestingUser
     */
    public String getRequestingUser() {
        return requestingUser;
    }

    /**
     * @param requestingUser the requestingUser to set
     */
    public void setRequestingUser(String requestingUser) {
        this.requestingUser = requestingUser;
    }

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
     * @return the creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the respositoryId
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * @param respositoryId the respositoryId to set
     */
    public void setRepositoryId(String respositoryId) {
        this.repositoryId = respositoryId;
    }



}
