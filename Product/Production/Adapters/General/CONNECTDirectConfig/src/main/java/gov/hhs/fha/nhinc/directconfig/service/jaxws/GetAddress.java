
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.EntityStatus;

@XmlRootElement(name = "getAddresss", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAddresss", namespace = "http://nhind.org/config", propOrder = {
    "emailAddress",
    "status"
})
public class GetAddress {

    @XmlElement(name = "emailAddress", namespace = "")
    private Collection<String> emailAddress;
    @XmlElement(name = "status", namespace = "")
    private EntityStatus status;

    /**
     * 
     * @return
     *     returns Collection<String>
     */
    public Collection<String> getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * 
     * @param emailAddress
     *     the value for the emailAddress property
     */
    public void setEmailAddress(Collection<String> emailAddress) {
        this.emailAddress = emailAddress;
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
