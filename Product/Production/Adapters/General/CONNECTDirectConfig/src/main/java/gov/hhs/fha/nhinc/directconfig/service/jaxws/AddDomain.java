
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Domain;

@XmlRootElement(name = "addDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addDomain", namespace = "http://nhind.org/config")
public class AddDomain {

    @XmlElement(name = "domain", namespace = "")
    private Domain domain;

    /**
     * 
     * @return
     *     returns Domain
     */
    public Domain getDomain() {
        return this.domain;
    }

    /**
     * 
     * @param domain
     *     the value for the domain property
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

}
