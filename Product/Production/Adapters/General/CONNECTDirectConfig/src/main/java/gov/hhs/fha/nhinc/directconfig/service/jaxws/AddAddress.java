
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.hhs.fha.nhinc.directconfig.entity.Address;

@XmlRootElement(name = "addAddress", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAddress", namespace = "http://nhind.org/config")
public class AddAddress {

    @XmlElement(name = "address", namespace = "")
    private Collection<Address> address;

    /**
     * 
     * @return
     *     returns Collection<Address>
     */
    public Collection<Address> getAddress() {
        return this.address;
    }

    /**
     * 
     * @param address
     *     the value for the address property
     */
    public void setAddress(Collection<Address> address) {
        this.address = address;
    }

}
