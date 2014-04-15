
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "listCertificates", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listCertificates", namespace = "http://nhind.org/config", propOrder = {
    "lastCertificateId",
    "maxResutls",
    "options"
})
public class ListCertificates {

    @XmlElement(name = "lastCertificateId", namespace = "")
    private long lastCertificateId;
    @XmlElement(name = "maxResutls", namespace = "")
    private int maxResutls;
    @XmlElement(name = "options", namespace = "")
    private CertificateGetOptions options;

    /**
     * 
     * @return
     *     returns long
     */
    public long getLastCertificateId() {
        return this.lastCertificateId;
    }

    /**
     * 
     * @param lastCertificateId
     *     the value for the lastCertificateId property
     */
    public void setLastCertificateId(long lastCertificateId) {
        this.lastCertificateId = lastCertificateId;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getMaxResutls() {
        return this.maxResutls;
    }

    /**
     * 
     * @param maxResutls
     *     the value for the maxResutls property
     */
    public void setMaxResutls(int maxResutls) {
        this.maxResutls = maxResutls;
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
