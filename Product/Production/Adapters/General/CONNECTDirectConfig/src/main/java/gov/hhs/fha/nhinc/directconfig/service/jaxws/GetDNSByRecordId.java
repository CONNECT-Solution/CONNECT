
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getDNSByRecordId", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDNSByRecordId", namespace = "http://nhind.org/config")
public class GetDNSByRecordId {

    @XmlElement(name = "recordId", namespace = "")
    private long recordId;

    /**
     * 
     * @return
     *     returns long
     */
    public long getRecordId() {
        return this.recordId;
    }

    /**
     * 
     * @param recordId
     *     the value for the recordId property
     */
    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

}
