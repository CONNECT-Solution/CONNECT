
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "refreshTrustBundle", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refreshTrustBundle", namespace = "http://nhind.org/config")
public class RefreshTrustBundle {

    @XmlElement(name = "id", namespace = "")
    private long id;

    /**
     * 
     * @return
     *     returns long
     */
    public long getId() {
        return this.id;
    }

    /**
     * 
     * @param id
     *     the value for the id property
     */
    public void setId(long id) {
        this.id = id;
    }

}
