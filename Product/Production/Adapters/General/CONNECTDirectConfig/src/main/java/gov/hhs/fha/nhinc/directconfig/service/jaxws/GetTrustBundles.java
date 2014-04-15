
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getTrustBundles", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTrustBundles", namespace = "http://nhind.org/config")
public class GetTrustBundles {

    @XmlElement(name = "fetchAnchors", namespace = "")
    private boolean fetchAnchors;

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
