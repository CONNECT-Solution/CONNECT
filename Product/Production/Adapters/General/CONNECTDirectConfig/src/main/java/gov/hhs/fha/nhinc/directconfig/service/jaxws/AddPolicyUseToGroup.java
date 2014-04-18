
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "addPolicyUseToGroup", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addPolicyUseToGroup", namespace = "http://nhind.org/config", propOrder = {
    "policyGroupId",
    "policyId",
    "policyUse",
    "incoming",
    "outgoing"
})
public class AddPolicyUseToGroup {

    @XmlElement(name = "policyGroupId", namespace = "")
    private long policyGroupId;
    @XmlElement(name = "policyId", namespace = "")
    private long policyId;
    @XmlElement(name = "policyUse", namespace = "")
    private CertPolicyUse policyUse;
    @XmlElement(name = "incoming", namespace = "")
    private boolean incoming;
    @XmlElement(name = "outgoing", namespace = "")
    private boolean outgoing;

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

    /**
     * 
     * @return
     *     returns CertPolicyUse
     */
    public CertPolicyUse getPolicyUse() {
        return this.policyUse;
    }

    /**
     * 
     * @param policyUse
     *     the value for the policyUse property
     */
    public void setPolicyUse(CertPolicyUse policyUse) {
        this.policyUse = policyUse;
    }

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isIncoming() {
        return this.incoming;
    }

    /**
     * 
     * @param incoming
     *     the value for the incoming property
     */
    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isOutgoing() {
        return this.outgoing;
    }

    /**
     * 
     * @param outgoing
     *     the value for the outgoing property
     */
    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

}
