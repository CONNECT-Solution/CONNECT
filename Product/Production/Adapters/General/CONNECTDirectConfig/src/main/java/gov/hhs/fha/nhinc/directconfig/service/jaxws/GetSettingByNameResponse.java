
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.Setting;

@XmlRootElement(name = "getSettingByNameResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSettingByNameResponse", namespace = "http://nhind.org/config")
public class GetSettingByNameResponse {

    @XmlElement(name = "return", namespace = "")
    private Setting _return;

    /**
     * 
     * @return
     *     returns Setting
     */
    public Setting getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Setting _return) {
        this._return = _return;
    }

}
