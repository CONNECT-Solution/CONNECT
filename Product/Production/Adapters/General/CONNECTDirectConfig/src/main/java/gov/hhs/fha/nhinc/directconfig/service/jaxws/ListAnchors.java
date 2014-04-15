
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "listAnchors", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listAnchors", namespace = "http://nhind.org/config", propOrder = {
    "lastAnchorId",
    "maxResults",
    "options"
})
public class ListAnchors {

    @XmlElement(name = "lastAnchorId", namespace = "")
    private Long lastAnchorId;
    @XmlElement(name = "maxResults", namespace = "")
    private int maxResults;
    @XmlElement(name = "options", namespace = "")
    private CertificateGetOptions options;

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getLastAnchorId() {
        return this.lastAnchorId;
    }

    /**
     * 
     * @param lastAnchorId
     *     the value for the lastAnchorId property
     */
    public void setLastAnchorId(Long lastAnchorId) {
        this.lastAnchorId = lastAnchorId;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getMaxResults() {
        return this.maxResults;
    }

    /**
     * 
     * @param maxResults
     *     the value for the maxResults property
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * 
     * @return
     *     returns CertificateGetOptions
     */
    public CertificateGetOptions getOptions() {
        return this.options;
    }

    /**
     * 
     * @param options
     *     the value for the options property
     */
    public void setOptions(CertificateGetOptions options) {
        this.options = options;
    }

}
