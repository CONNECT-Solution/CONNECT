/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.model;

/**
 * @author msw
 *
 */
public class Organization {
    
    private String HCID;
    private int PDCount;
    private int DQCount;
    private int DRCount;
    private int DSCount;
    private int ADCount;
    private int directCount;
    private int totalCount;
    
    /**
     * @return the hCID
     */
    public String getHCID() {
        return HCID;
    }
    /**
     * @param hCID the hCID to set
     */
    public void setHCID(String hCID) {
        HCID = hCID;
    }
    /**
     * @return the pDCount
     */
    public int getPDCount() {
        return PDCount;
    }
    /**
     * @param pDCount the pDCount to set
     */
    public void setPDCount(int pDCount) {
        PDCount = pDCount;
    }
    /**
     * @return the dQCount
     */
    public int getDQCount() {
        return DQCount;
    }
    /**
     * @param dQCount the dQCount to set
     */
    public void setDQCount(int dQCount) {
        DQCount = dQCount;
    }
    /**
     * @return the dRCount
     */
    public int getDRCount() {
        return DRCount;
    }
    /**
     * @param dRCount the dRCount to set
     */
    public void setDRCount(int dRCount) {
        DRCount = dRCount;
    }
    /**
     * @return the dSCount
     */
    public int getDSCount() {
        return DSCount;
    }
    /**
     * @param dSCount the dSCount to set
     */
    public void setDSCount(int dSCount) {
        DSCount = dSCount;
    }
    /**
     * @return the aDCount
     */
    public int getADCount() {
        return ADCount;
    }
    /**
     * @param aDCount the aDCount to set
     */
    public void setADCount(int aDCount) {
        ADCount = aDCount;
    }
    /**
     * @return the directCount
     */
    public int getDirectCount() {
        return directCount;
    }
    /**
     * @param directCount the directCount to set
     */
    public void setDirectCount(int directCount) {
        this.directCount = directCount;
    }
    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
