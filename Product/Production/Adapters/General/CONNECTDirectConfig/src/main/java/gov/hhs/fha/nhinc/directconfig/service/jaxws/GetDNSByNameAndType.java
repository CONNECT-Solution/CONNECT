
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getDNSByNameAndType", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDNSByNameAndType", namespace = "http://nhind.org/config", propOrder = {
    "name",
    "type"
})
public class GetDNSByNameAndType {

    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "type", namespace = "")
    private int type;

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
     *     returns int
     */
    public int getType() {
        return this.type;
    }

    /**
     * 
     * @param type
     *     the value for the type property
     */
    public void setType(int type) {
        this.type = type;
    }

}
