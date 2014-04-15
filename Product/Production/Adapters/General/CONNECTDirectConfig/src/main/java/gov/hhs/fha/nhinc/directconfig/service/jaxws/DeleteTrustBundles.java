
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deleteTrustBundles", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteTrustBundles", namespace = "http://nhind.org/config")
public class DeleteTrustBundles {

    @XmlElement(name = "trustBundleIds", namespace = "", nillable = true)
    private long[] trustBundleIds;

    /**
     * 
     * @return
     *     returns long[]
     */
    public long[] getTrustBundleIds() {
        return this.trustBundleIds;
    }

    /**
     * 
     * @param trustBundleIds
     *     the value for the trustBundleIds property
     */
    public void setTrustBundleIds(long[] trustBundleIds) {
        this.trustBundleIds = trustBundleIds;
    }

}
