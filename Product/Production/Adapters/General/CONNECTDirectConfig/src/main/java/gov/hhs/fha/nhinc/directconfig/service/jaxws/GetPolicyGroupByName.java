
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getPolicyGroupByName", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyGroupByName", namespace = "http://nhind.org/config")
public class GetPolicyGroupByName {

    @XmlElement(name = "policyGroupName", namespace = "")
    private String policyGroupName;

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
