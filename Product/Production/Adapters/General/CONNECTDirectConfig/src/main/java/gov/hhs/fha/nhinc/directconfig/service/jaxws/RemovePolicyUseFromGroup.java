
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "removePolicyUseFromGroup", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removePolicyUseFromGroup", namespace = "http://nhind.org/config")
public class RemovePolicyUseFromGroup {

    @XmlElement(name = "policyGroupReltnId", namespace = "")
    private long policyGroupReltnId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getPolicyGroupReltnId() {
        return this.policyGroupReltnId;
    }

    /**
     * 
     * @param policyGroupReltnId
     *     the value for the policyGroupReltnId property
     */
    public void setPolicyGroupReltnId(long policyGroupReltnId) {
        this.policyGroupReltnId = policyGroupReltnId;
    }

}
