
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "disassociateTrustBundleFromDomains", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "disassociateTrustBundleFromDomains", namespace = "http://nhind.org/config")
public class DisassociateTrustBundleFromDomains {

    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getTrustBundleId() {
        return this.trustBundleId;
    }

    /**
     * 
     * @param trustBundleId
     *     the value for the trustBundleId property
     */
    public void setTrustBundleId(long trustBundleId) {
        this.trustBundleId = trustBundleId;
    }

}
