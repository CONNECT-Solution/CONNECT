
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "associateTrustBundleToDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "associateTrustBundleToDomain", namespace = "http://nhind.org/config", propOrder = {
    "domainId",
    "trustBundleId",
    "incoming",
    "outgoing"
})
public class AssociateTrustBundleToDomain {

    @XmlElement(name = "domainId", namespace = "")
    private long domainId;
    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;
    @XmlElement(name = "incoming", namespace = "")
    private boolean incoming;
    @XmlElement(name = "outgoing", namespace = "")
    private boolean outgoing;

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

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isIncoming() {
        return this.incoming;
    }

    /**
     * 
     * @param incoming
     *     the value for the incoming property
     */
    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isOutgoing() {
        return this.outgoing;
    }

    /**
     * 
     * @param outgoing
     *     the value for the outgoing property
     */
    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

}
