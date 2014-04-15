
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "removeCertificates", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeCertificates", namespace = "http://nhind.org/config")
public class RemoveCertificates {

    @XmlElement(name = "certificateIds", namespace = "")
    private Collection<Long> certificateIds;

    /**
     * 
     * @return
     *     returns Collection<Long>
     */
    public Collection<Long> getCertificateIds() {
        return this.certificateIds;
    }

    /**
     * 
     * @param certificateIds
     *     the value for the certificateIds property
     */
    public void setCertificateIds(Collection<Long> certificateIds) {
        this.certificateIds = certificateIds;
    }

}
