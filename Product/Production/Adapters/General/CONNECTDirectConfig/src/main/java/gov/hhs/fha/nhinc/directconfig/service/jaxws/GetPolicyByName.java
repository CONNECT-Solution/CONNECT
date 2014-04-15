
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getPolicyByName", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPolicyByName", namespace = "http://nhind.org/config")
public class GetPolicyByName {

    @XmlElement(name = "policyName", namespace = "")
    private String policyName;

    /**
     * 
     * @return
     *     returns String
     */
    public String getPolicyName() {
        return this.policyName;
    }

    /**
     * 
     * @param policyName
     *     the value for the policyName property
     */
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

}
