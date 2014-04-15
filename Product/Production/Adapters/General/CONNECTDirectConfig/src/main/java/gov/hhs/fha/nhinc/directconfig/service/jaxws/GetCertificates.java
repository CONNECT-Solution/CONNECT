
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

@XmlRootElement(name = "getCertificates", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCertificates", namespace = "http://nhind.org/config", propOrder = {
    "certificateIds",
    "options"
})
public class GetCertificates {

    @XmlElement(name = "certificateIds", namespace = "")
    private Collection<Long> certificateIds;
    @XmlElement(name = "options", namespace = "")
    private CertificateGetOptions options;

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

    /**
     * 
     * @return
     *     returns CertificateGetOptions
     */
    public CertificateGetOptions getOptions() {
        return this.options;
    }

    /**
     * 
     * @param options
     *     the value for the options property
     */
    public void setOptions(CertificateGetOptions options) {
        this.options = options;
    }

}
