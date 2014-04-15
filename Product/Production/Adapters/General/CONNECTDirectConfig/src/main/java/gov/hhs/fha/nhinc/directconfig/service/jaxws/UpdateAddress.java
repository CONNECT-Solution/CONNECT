
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Address;

@XmlRootElement(name = "updateAddress", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateAddress", namespace = "http://nhind.org/config")
public class UpdateAddress {

    @XmlElement(name = "address", namespace = "")
    private Address address;

    /**
     * 
     * @return
     *     returns Address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * 
     * @param address
     *     the value for the address property
     */
    public void setAddress(Address address) {
        this.address = address;
    }

}
