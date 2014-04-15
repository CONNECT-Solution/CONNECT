
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Domain;

@XmlRootElement(name = "getDomainResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDomainResponse", namespace = "http://nhind.org/config")
public class GetDomainResponse {

    @XmlElement(name = "return", namespace = "")
    private Domain _return;

    /**
     * 
     * @return
     *     returns Domain
     */
    public Domain getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Domain _return) {
        this._return = _return;
    }

}
