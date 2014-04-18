
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.entity.CertPolicyGroup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "addPolicyGroup", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addPolicyGroup", namespace = "http://nhind.org/config")
public class AddPolicyGroup {

    @XmlElement(name = "policyGroup", namespace = "")
    private CertPolicyGroup policyGroup;

    /**
     * 
     * @return
     *     returns CertPolicyGroup
     */
    public CertPolicyGroup getPolicyGroup() {
        return this.policyGroup;
    }

    /**
     * 
     * @param policyGroup
     *     the value for the policyGroup property
     */
    public void setPolicyGroup(CertPolicyGroup policyGroup) {
        this.policyGroup = policyGroup;
    }

}
