
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "updateSetting", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateSetting", namespace = "http://nhind.org/config", propOrder = {
    "name",
    "value"
})
public class UpdateSetting {

    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "value", namespace = "")
    private String value;

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
     *     returns String
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 
     * @param value
     *     the value for the value property
     */
    public void setValue(String value) {
        this.value = value;
    }

}
