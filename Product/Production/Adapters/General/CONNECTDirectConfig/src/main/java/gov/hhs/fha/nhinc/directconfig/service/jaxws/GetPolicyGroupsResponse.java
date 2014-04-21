
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;

@XmlRootElement(name = "getPolicyGroupsResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyGroupsResponse", namespace = "http://nhind.org/config")
public class GetPolicyGroupsResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<CertPolicyGroup> _return;

    /**
     * 
     * @return
     *     returns Collection<CertPolicyGroup>
     */
    public Collection<CertPolicyGroup> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<CertPolicyGroup> _return) {
        this._return = _return;
    }

}
