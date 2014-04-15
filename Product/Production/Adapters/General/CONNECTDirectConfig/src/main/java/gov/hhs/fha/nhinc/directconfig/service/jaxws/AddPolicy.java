
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.CertPolicy;

@XmlRootElement(name = "addPolicy", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addPolicy", namespace = "http://nhind.org/config")
public class AddPolicy {

    @XmlElement(name = "policy", namespace = "")
    private CertPolicy policy;

    /**
     * 
     * @return
     *     returns CertPolicy
     */
    public CertPolicy getPolicy() {
        return this.policy;
    }

    /**
     * 
     * @param policy
     *     the value for the policy property
     */
    public void setPolicy(CertPolicy policy) {
        this.policy = policy;
    }

}
