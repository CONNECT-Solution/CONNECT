
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;

@XmlRootElement(name = "getPolicyGroupByIdResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyGroupByIdResponse", namespace = "http://nhind.org/config")
public class GetPolicyGroupByIdResponse {

    @XmlElement(name = "return", namespace = "")
    private CertPolicyGroup _return;

    /**
     * 
     * @return
     *     returns CertPolicyGroup
     */
    public CertPolicyGroup getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(CertPolicyGroup _return) {
        this._return = _return;
    }

}
