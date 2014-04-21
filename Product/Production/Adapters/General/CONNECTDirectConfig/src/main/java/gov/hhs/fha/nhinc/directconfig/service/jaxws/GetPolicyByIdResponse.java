
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;

@XmlRootElement(name = "getPolicyByIdResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyByIdResponse", namespace = "http://nhind.org/config")
public class GetPolicyByIdResponse {

    @XmlElement(name = "return", namespace = "")
    private CertPolicy _return;

    /**
     * 
     * @return
     *     returns CertPolicy
     */
    public CertPolicy getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(CertPolicy _return) {
        this._return = _return;
    }

}
