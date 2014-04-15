
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getTrustBundlesByDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTrustBundlesByDomain", namespace = "http://nhind.org/config", propOrder = {
    "domainId",
    "fetchAnchors"
})
public class GetTrustBundlesByDomain {

    @XmlElement(name = "domainId", namespace = "")
    private long domainId;
    @XmlElement(name = "fetchAnchors", namespace = "")
    private boolean fetchAnchors;

    /**
     * 
     * @return
     *     returns long
     */
    public long getDomainId() {
        return this.domainId;
    }

    /**
     * 
     * @param domainId
     *     the value for the domainId property
     */
    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isFetchAnchors() {
        return this.fetchAnchors;
    }

    /**
     * 
     * @param fetchAnchors
     *     the value for the fetchAnchors property
     */
    public void setFetchAnchors(boolean fetchAnchors) {
        this.fetchAnchors = fetchAnchors;
    }

}
