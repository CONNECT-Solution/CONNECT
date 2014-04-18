
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getPolicyById", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyById", namespace = "http://nhind.org/config")
public class GetPolicyById {

    @XmlElement(name = "policyId", namespace = "")
    private long policyId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getPolicyId() {
        return this.policyId;
    }

    /**
     * 
     * @param policyId
     *     the value for the policyId property
     */
    public void setPolicyId(long policyId) {
        this.policyId = policyId;
    }

}
