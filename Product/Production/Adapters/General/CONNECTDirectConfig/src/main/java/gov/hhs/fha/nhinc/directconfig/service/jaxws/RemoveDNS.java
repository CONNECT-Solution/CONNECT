
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;

@XmlRootElement(name = "removeDNS", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeDNS", namespace = "http://nhind.org/config")
public class RemoveDNS {

    @XmlElement(name = "records", namespace = "")
    private Collection<DNSRecord> records;

    /**
     * 
     * @return
     *     returns Collection<DNSRecord>
     */
    public Collection<DNSRecord> getRecords() {
        return this.records;
    }

    /**
     * 
     * @param records
     *     the value for the records property
     */
    public void setRecords(Collection<DNSRecord> records) {
        this.records = records;
    }

}
