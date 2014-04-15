
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;

@XmlRootElement(name = "addTrustBundle", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addTrustBundle", namespace = "http://nhind.org/config")
public class AddTrustBundle {

    @XmlElement(name = "bundle", namespace = "")
    private TrustBundle bundle;

    /**
     * 
     * @return
     *     returns TrustBundle
     */
    public TrustBundle getBundle() {
        return this.bundle;
    }

    /**
     * 
     * @param bundle
     *     the value for the bundle property
     */
    public void setBundle(TrustBundle bundle) {
        this.bundle = bundle;
    }

}
