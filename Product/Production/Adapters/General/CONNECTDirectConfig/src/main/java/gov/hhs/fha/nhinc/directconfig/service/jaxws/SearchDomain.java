
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.EntityStatus;

@XmlRootElement(name = "searchDomain", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchDomain", namespace = "http://nhind.org/config", propOrder = {
    "name",
    "status"
})
public class SearchDomain {

    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "status", namespace = "")
    private EntityStatus status;

    /**
     * 
     * @return
     *     returns String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param name
     *     the value for the name property
     */
    public void setName(String name) {
        this.name = name;
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
