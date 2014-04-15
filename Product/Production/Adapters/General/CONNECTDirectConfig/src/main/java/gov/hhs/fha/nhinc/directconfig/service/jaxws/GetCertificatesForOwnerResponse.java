
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;

@XmlRootElement(name = "getCertificatesForOwnerResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCertificatesForOwnerResponse", namespace = "http://nhind.org/config")
public class GetCertificatesForOwnerResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<Certificate> _return;

    /**
     * 
     * @return
     *     returns Collection<Certificate>
     */
    public Collection<Certificate> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<Certificate> _return) {
        this._return = _return;
    }

}
