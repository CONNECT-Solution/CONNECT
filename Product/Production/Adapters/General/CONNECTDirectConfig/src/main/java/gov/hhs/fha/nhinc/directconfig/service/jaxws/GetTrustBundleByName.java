
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getTrustBundleByName", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTrustBundleByName", namespace = "http://nhind.org/config")
public class GetTrustBundleByName {

    @XmlElement(name = "bundleName", namespace = "")
    private String bundleName;

    /**
     * 
     * @return
     *     returns String
     */
    public String getBundleName() {
        return this.bundleName;
    }

    /**
     * 
     * @param bundleName
     *     the value for the bundleName property
     */
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

}
