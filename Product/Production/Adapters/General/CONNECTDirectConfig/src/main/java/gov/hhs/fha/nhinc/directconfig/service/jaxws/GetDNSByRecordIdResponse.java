
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.DNSRecord;

@XmlRootElement(name = "getDNSByRecordIdResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDNSByRecordIdResponse", namespace = "http://nhind.org/config")
public class GetDNSByRecordIdResponse {

    @XmlElement(name = "return", namespace = "")
    private DNSRecord _return;

    /**
     * 
     * @return
     *     returns DNSRecord
     */
    public DNSRecord getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(DNSRecord _return) {
        this._return = _return;
    }

}
