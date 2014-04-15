
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "updateGroupAttributes", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateGroupAttributes", namespace = "http://nhind.org/config", propOrder = {
    "policyGroupId",
    "policyGroupName"
})
public class UpdateGroupAttributes {

    @XmlElement(name = "policyGroupId", namespace = "")
    private long policyGroupId;
    @XmlElement(name = "policyGroupName", namespace = "")
    private String policyGroupName;

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

    /**
     * 
     * @return
     *     returns String
     */
    public String getPolicyGroupName() {
        return this.policyGroupName;
    }

    /**
     * 
     * @param policyGroupName
     *     the value for the policyGroupName property
     */
    public void setPolicyGroupName(String policyGroupName) {
        this.policyGroupName = policyGroupName;
    }

}
