
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "disassociatePolicyGroupsFromDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "disassociatePolicyGroupsFromDomain", namespace = "http://nhind.org/config")
public class DisassociatePolicyGroupsFromDomain {

    @XmlElement(name = "domainId", namespace = "")
    private long domainId;

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

}
