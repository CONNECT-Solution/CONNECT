
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Certificate;

@XmlRootElement(name = "updateTrustBundleSigningCertificate", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTrustBundleSigningCertificate", namespace = "http://nhind.org/config", propOrder = {
    "trustBundleIds",
    "signingCert"
})
public class UpdateTrustBundleSigningCertificate {

    @XmlElement(name = "trustBundleIds", namespace = "")
    private long trustBundleIds;
    @XmlElement(name = "signingCert", namespace = "")
    private Certificate signingCert;

    /**
     * 
     * @return
     *     returns long
     */
    public long getTrustBundleIds() {
        return this.trustBundleIds;
    }

    /**
     * 
     * @param trustBundleIds
     *     the value for the trustBundleIds property
     */
    public void setTrustBundleIds(long trustBundleIds) {
        this.trustBundleIds = trustBundleIds;
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

}
