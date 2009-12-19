/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclientgui.objects;

/**
 *
 * @author Duane DeCouteau
 */
public class TabViewerDisplayObject {
    private String orgName = "";
    private String docType = "";
    private String tabTitle = "";
    private String rawXML = "";
    private String docId = "";
    private String convertedXMLDoc = "";

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the tabTitle
     */
    public String getTabTitle() {
        return tabTitle;
    }

    /**
     * @param tabTitle the tabTitle to set
     */
    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    /**
     * @return the docId
     */
    public String getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * @return the convertedXMLDoc
     */
    public String getConvertedXMLDoc() {
        return convertedXMLDoc;
    }

    /**
     * @param convertedXMLDoc the convertedXMLDoc to set
     */
    public void setConvertedXMLDoc(String convertedXMLDoc) {
        this.convertedXMLDoc = convertedXMLDoc;
    }

    

}
