
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.policy.PolicyLexicon;

@XmlRootElement(name = "updatePolicyAttributes", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updatePolicyAttributes", namespace = "http://nhind.org/config", propOrder = {
    "policyId",
    "policyName",
    "policyLexicon",
    "policyData"
})
public class UpdatePolicyAttributes {

    @XmlElement(name = "policyId", namespace = "")
    private long policyId;
    @XmlElement(name = "policyName", namespace = "")
    private String policyName;
    @XmlElement(name = "policyLexicon", namespace = "")
    private PolicyLexicon policyLexicon;
    @XmlElement(name = "policyData", namespace = "", nillable = true)
    private byte[] policyData;

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

    /**
     * 
     * @return
     *     returns PolicyLexicon
     */
    public PolicyLexicon getPolicyLexicon() {
        return this.policyLexicon;
    }

    /**
     * 
     * @param policyLexicon
     *     the value for the policyLexicon property
     */
    public void setPolicyLexicon(PolicyLexicon policyLexicon) {
        this.policyLexicon = policyLexicon;
    }

    /**
     * 
     * @return
     *     returns byte[]
     */
    public byte[] getPolicyData() {
        return this.policyData;
    }

    /**
     * 
     * @param policyData
     *     the value for the policyData property
     */
    public void setPolicyData(byte[] policyData) {
        this.policyData = policyData;
    }

}
