
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.EntityStatus;

@XmlRootElement(name = "getDomains", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDomains", namespace = "http://nhind.org/config", propOrder = {
    "names",
    "status"
})
public class GetDomains {

    @XmlElement(name = "names", namespace = "")
    private Collection<String> names;
    @XmlElement(name = "status", namespace = "")
    private EntityStatus status;

    /**
     * 
     * @return
     *     returns Collection<String>
     */
    public Collection<String> getNames() {
        return this.names;
    }

    /**
     * 
     * @param names
     *     the value for the names property
     */
    public void setNames(Collection<String> names) {
        this.names = names;
    }

    /**
     * 
     * @return
     *     returns EntityStatus
     */
    public EntityStatus getStatus() {
        return this.status;
    }

    /**
     * 
     * @param status
     *     the value for the status property
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

}
