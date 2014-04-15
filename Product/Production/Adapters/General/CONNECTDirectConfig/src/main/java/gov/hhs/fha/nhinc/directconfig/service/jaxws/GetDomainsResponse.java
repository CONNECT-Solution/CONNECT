
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;

@XmlRootElement(name = "getDomainsResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDomainsResponse", namespace = "http://nhind.org/config")
public class GetDomainsResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<Domain> _return;

    /**
     * 
     * @return
     *     returns Collection<Domain>
     */
    public Collection<Domain> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<Domain> _return) {
        this._return = _return;
    }

}
