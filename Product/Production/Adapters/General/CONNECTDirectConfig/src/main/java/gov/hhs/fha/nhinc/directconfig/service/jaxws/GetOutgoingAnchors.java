
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getOutgoingAnchors", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getOutgoingAnchors", namespace = "http://nhind.org/config", propOrder = {
    "owner",
    "options"
})
public class GetOutgoingAnchors {

    @XmlElement(name = "owner", namespace = "")
    private String owner;
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
