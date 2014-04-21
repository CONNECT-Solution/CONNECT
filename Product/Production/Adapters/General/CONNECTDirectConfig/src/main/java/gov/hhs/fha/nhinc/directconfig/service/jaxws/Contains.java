
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;

@XmlRootElement(name = "contains", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contains", namespace = "http://nhind.org/config")
public class Contains {

    @XmlElement(name = "cert", namespace = "")
    private Certificate cert;

    /**
     * 
     * @return
     *     returns Certificate
     */
    public Certificate getCert() {
        return this.cert;
    }

    /**
     * 
     * @param cert
     *     the value for the cert property
     */
    public void setCert(Certificate cert) {
        this.cert = cert;
    }

}
