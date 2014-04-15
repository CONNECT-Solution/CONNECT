
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "removeAnchors", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeAnchors", namespace = "http://nhind.org/config")
public class RemoveAnchors {

    @XmlElement(name = "anchorId", namespace = "")
    private Collection<Long> anchorId;

    /**
     * 
     * @return
     *     returns Collection<Long>
     */
    public Collection<Long> getAnchorId() {
        return this.anchorId;
    }

    /**
     * 
     * @param anchorId
     *     the value for the anchorId property
     */
    public void setAnchorId(Collection<Long> anchorId) {
        this.anchorId = anchorId;
    }

}
