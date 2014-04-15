
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.EntityStatus;

@XmlRootElement(name = "setCertificateStatus", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setCertificateStatus", namespace = "http://nhind.org/config", propOrder = {
    "certificateIds",
    "status"
})
public class SetCertificateStatus {

    @XmlElement(name = "certificateIds", namespace = "")
    private Collection<Long> certificateIds;
    @XmlElement(name = "status", namespace = "")
    private EntityStatus status;

    /**
     * 
     * @return
     *     returns Collection<Long>
     */
    public Collection<Long> getCertificateIds() {
        return this.certificateIds;
    }

    /**
     * 
     * @param certificateIds
     *     the value for the certificateIds property
     */
    public void setCertificateIds(Collection<Long> certificateIds) {
        this.certificateIds = certificateIds;
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
