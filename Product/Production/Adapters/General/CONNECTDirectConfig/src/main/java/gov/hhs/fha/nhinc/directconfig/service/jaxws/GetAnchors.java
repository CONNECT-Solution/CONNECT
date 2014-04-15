
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions;

@XmlRootElement(name = "getAnchors", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAnchors", namespace = "http://nhind.org/config", propOrder = {
    "anchorId",
    "options"
})
public class GetAnchors {

    @XmlElement(name = "anchorId", namespace = "")
    private Collection<Long> anchorId;
    @XmlElement(name = "options", namespace = "")
    private CertificateGetOptions options;

    /**
     * 
     * @return
     *     returns Collection<Long>
     */
    public Collection<Long> getAnchorId() {
        return this.anchorId;
    }

    /**
     * 
     * @param anchorId
     *     the value for the anchorId property
     */
    public void setAnchorId(Collection<Long> anchorId) {
        this.anchorId = anchorId;
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
