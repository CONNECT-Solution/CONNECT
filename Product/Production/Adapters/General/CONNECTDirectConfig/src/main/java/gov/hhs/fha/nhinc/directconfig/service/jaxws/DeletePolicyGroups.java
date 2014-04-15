
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deletePolicyGroups", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deletePolicyGroups", namespace = "http://nhind.org/config")
public class DeletePolicyGroups {

    @XmlElement(name = "policyGroupIds", namespace = "", nillable = true)
    private long[] policyGroupIds;

    /**
     * 
     * @return
     *     returns long[]
     */
    public long[] getPolicyGroupIds() {
        return this.policyGroupIds;
    }

    /**
     * 
     * @param policyGroupIds
     *     the value for the policyGroupIds property
     */
    public void setPolicyGroupIds(long[] policyGroupIds) {
        this.policyGroupIds = policyGroupIds;
    }

}
