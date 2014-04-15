
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deleteSetting", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteSetting", namespace = "http://nhind.org/config")
public class DeleteSetting {

    @XmlElement(name = "names", namespace = "")
    private Collection<String> names;

    /**
     * 
     * @return
     *     returns Collection<String>
     */
    public Collection<String> getNames() {
        return this.names;
    }

    /**
     * 
     * @param names
     *     the value for the names property
     */
    public void setNames(Collection<String> names) {
        this.names = names;
    }

}
