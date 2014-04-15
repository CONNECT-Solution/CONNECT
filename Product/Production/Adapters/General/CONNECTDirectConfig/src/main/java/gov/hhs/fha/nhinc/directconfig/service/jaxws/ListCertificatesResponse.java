
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Certificate;

@XmlRootElement(name = "listCertificatesResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listCertificatesResponse", namespace = "http://nhind.org/config")
public class ListCertificatesResponse {

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
