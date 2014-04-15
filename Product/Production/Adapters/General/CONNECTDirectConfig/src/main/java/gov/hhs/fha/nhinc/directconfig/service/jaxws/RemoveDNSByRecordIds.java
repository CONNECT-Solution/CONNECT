
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "removeDNSByRecordIds", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeDNSByRecordIds", namespace = "http://nhind.org/config")
public class RemoveDNSByRecordIds {

    @XmlElement(name = "recordIds", namespace = "", nillable = true)
    private long[] recordIds;

    /**
     * 
     * @return
     *     returns long[]
     */
    public long[] getRecordIds() {
        return this.recordIds;
    }

    /**
     * 
     * @param recordIds
     *     the value for the recordIds property
     */
    public void setRecordIds(long[] recordIds) {
        this.recordIds = recordIds;
    }

}
