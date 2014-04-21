
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;

@XmlRootElement(name = "getCertificateResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCertificateResponse", namespace = "http://nhind.org/config")
public class GetCertificateResponse {

    @XmlElement(name = "return", namespace = "")
    private Certificate _return;

    /**
     * 
     * @return
     *     returns Certificate
     */
    public Certificate getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Certificate _return) {
        this._return = _return;
    }

}
