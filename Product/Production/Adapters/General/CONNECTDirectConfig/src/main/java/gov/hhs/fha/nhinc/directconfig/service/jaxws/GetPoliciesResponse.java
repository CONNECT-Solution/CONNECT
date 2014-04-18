
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicy;

@XmlRootElement(name = "getPoliciesResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPoliciesResponse", namespace = "http://nhind.org/config")
public class GetPoliciesResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<CertPolicy> _return;

    /**
     * 
     * @return
     *     returns Collection<CertPolicy>
     */
    public Collection<CertPolicy> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<CertPolicy> _return) {
        this._return = _return;
    }

}
