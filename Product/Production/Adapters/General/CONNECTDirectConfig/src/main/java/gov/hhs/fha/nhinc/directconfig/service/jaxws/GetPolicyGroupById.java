
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getPolicyGroupById", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyGroupById", namespace = "http://nhind.org/config")
public class GetPolicyGroupById {

    @XmlElement(name = "policyGroupId", namespace = "")
    private long policyGroupId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getPolicyGroupId() {
        return this.policyGroupId;
    }

    /**
     * 
     * @param policyGroupId
     *     the value for the policyGroupId property
     */
    public void setPolicyGroupId(long policyGroupId) {
        this.policyGroupId = policyGroupId;
    }

}
