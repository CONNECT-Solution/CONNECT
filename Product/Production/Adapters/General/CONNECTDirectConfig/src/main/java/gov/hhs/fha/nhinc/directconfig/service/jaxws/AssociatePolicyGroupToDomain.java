
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "associatePolicyGroupToDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "associatePolicyGroupToDomain", namespace = "http://nhind.org/config", propOrder = {
    "domainId",
    "policyGroupId"
})
public class AssociatePolicyGroupToDomain {

    @XmlElement(name = "domainId", namespace = "")
    private long domainId;
    @XmlElement(name = "policyGroupId", namespace = "")
    private long policyGroupId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getDomainId() {
        return this.domainId;
    }

    /**
     * 
     * @param domainId
     *     the value for the domainId property
     */
    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

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
