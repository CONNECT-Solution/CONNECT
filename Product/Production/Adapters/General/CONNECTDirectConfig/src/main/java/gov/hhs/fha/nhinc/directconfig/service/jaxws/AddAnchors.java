
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Anchor;

@XmlRootElement(name = "addAnchor", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAnchor", namespace = "http://nhind.org/config")
public class AddAnchors {

    @XmlElement(name = "anchor", namespace = "")
    private Collection<Anchor> anchor;

    /**
     * 
     * @return
     *     returns Collection<Anchor>
     */
    public Collection<Anchor> getAnchor() {
        return this.anchor;
    }

    /**
     * 
     * @param anchor
     *     the value for the anchor property
     */
    public void setAnchor(Collection<Anchor> anchor) {
        this.anchor = anchor;
    }

}
