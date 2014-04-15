
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Anchor;

@XmlRootElement(name = "getIncomingAnchorsResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getIncomingAnchorsResponse", namespace = "http://nhind.org/config")
public class GetIncomingAnchorsResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<Anchor> _return;

    /**
     * 
     * @return
     *     returns Collection<Anchor>
     */
    public Collection<Anchor> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<Anchor> _return) {
        this._return = _return;
    }

}
