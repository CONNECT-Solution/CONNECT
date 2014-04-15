
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "removeAddress", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeAddress", namespace = "http://nhind.org/config")
public class RemoveAddress {

    @XmlElement(name = "emailAddress", namespace = "")
    private String emailAddress;

    /**
     * 
     * @return
     *     returns String
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * 
     * @param emailAddress
     *     the value for the emailAddress property
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
