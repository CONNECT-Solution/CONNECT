
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.EntityStatus;

@XmlRootElement(name = "setAnchorStatusForOwner", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setAnchorStatusForOwner", namespace = "http://nhind.org/config", propOrder = {
    "owner",
    "status"
})
public class SetAnchorStatusForOwner {

    @XmlElement(name = "owner", namespace = "")
    private String owner;
    @XmlElement(name = "status", namespace = "")
    private EntityStatus status;

    /**
     * 
     * @return
     *     returns String
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * 
     * @param owner
     *     the value for the owner property
     */
    public void setOwner(String owner) {
        this.owner = owner;
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
