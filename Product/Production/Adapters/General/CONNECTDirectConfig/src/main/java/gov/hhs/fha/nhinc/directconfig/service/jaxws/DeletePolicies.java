
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deletePolicies", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deletePolicies", namespace = "http://nhind.org/config")
public class DeletePolicies {

    @XmlElement(name = "policyIds", namespace = "", nillable = true)
    private long[] policyIds;

    /**
     * 
     * @return
     *     returns long[]
     */
    public long[] getPolicyIds() {
        return this.policyIds;
    }

    /**
     * 
     * @param policyIds
     *     the value for the policyIds property
     */
    public void setPolicyIds(long[] policyIds) {
        this.policyIds = policyIds;
    }

}
