
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;

@XmlRootElement(name = "updateDNS", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateDNS", namespace = "http://nhind.org/config", propOrder = {
    "recordId",
    "record"
})
public class UpdateDNS {

    @XmlElement(name = "recordId", namespace = "")
    private long recordId;
    @XmlElement(name = "record", namespace = "")
    private DNSRecord record;

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

    /**
     * 
     * @return
     *     returns DNSRecord
     */
    public DNSRecord getRecord() {
        return this.record;
    }

    /**
     * 
     * @param record
     *     the value for the record property
     */
    public void setRecord(DNSRecord record) {
        this.record = record;
    }

}
