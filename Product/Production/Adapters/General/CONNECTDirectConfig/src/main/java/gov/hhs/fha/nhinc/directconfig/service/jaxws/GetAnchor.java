
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getAnchor", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAnchor", namespace = "http://nhind.org/config", propOrder = {
    "owner",
    "thumbprint",
    "options"
})
public class GetAnchor {

    @XmlElement(name = "owner", namespace = "")
    private String owner;
    @XmlElement(name = "thumbprint", namespace = "")
    private String thumbprint;
    @XmlElement(name = "options", namespace = "")
    private CertificateGetOptions options;

    /**
     * 
     * @return
     *     returns String
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * 
     * @param owner
     *     the value for the owner property
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getThumbprint() {
        return this.thumbprint;
    }

    /**
     * 
     * @param thumbprint
     *     the value for the thumbprint property
     */
    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
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
