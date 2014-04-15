
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Certificate;

@XmlRootElement(name = "updateTrustBundleAttributes", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTrustBundleAttributes", namespace = "http://nhind.org/config", propOrder = {
    "trustBundleId",
    "trustBundleName",
    "trustBundleURL",
    "signingCert",
    "trustBundleRefreshInterval"
})
public class UpdateTrustBundleAttributes {

    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;
    @XmlElement(name = "trustBundleName", namespace = "")
    private String trustBundleName;
    @XmlElement(name = "trustBundleURL", namespace = "")
    private String trustBundleURL;
    @XmlElement(name = "signingCert", namespace = "")
    private Certificate signingCert;
    @XmlElement(name = "trustBundleRefreshInterval", namespace = "")
    private int trustBundleRefreshInterval;

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
     *     returns String
     */
    public String getTrustBundleName() {
        return this.trustBundleName;
    }

    /**
     * 
     * @param trustBundleName
     *     the value for the trustBundleName property
     */
    public void setTrustBundleName(String trustBundleName) {
        this.trustBundleName = trustBundleName;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getTrustBundleURL() {
        return this.trustBundleURL;
    }

    /**
     * 
     * @param trustBundleURL
     *     the value for the trustBundleURL property
     */
    public void setTrustBundleURL(String trustBundleURL) {
        this.trustBundleURL = trustBundleURL;
    }

    /**
     * 
     * @return
     *     returns Certificate
     */
    public Certificate getSigningCert() {
        return this.signingCert;
    }

    /**
     * 
     * @param signingCert
     *     the value for the signingCert property
     */
    public void setSigningCert(Certificate signingCert) {
        this.signingCert = signingCert;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getTrustBundleRefreshInterval() {
        return this.trustBundleRefreshInterval;
    }

    /**
     * 
     * @param trustBundleRefreshInterval
     *     the value for the trustBundleRefreshInterval property
     */
    public void setTrustBundleRefreshInterval(int trustBundleRefreshInterval) {
        this.trustBundleRefreshInterval = trustBundleRefreshInterval;
    }

}
