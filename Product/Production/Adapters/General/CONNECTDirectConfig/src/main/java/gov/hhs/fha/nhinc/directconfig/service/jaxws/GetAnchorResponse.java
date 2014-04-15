
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Anchor;

@XmlRootElement(name = "getAnchorResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAnchorResponse", namespace = "http://nhind.org/config")
public class GetAnchorResponse {

    @XmlElement(name = "return", namespace = "")
    private Anchor _return;

    /**
     * 
     * @return
     *     returns Anchor
     */
    public Anchor getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Anchor _return) {
        this._return = _return;
    }

}
