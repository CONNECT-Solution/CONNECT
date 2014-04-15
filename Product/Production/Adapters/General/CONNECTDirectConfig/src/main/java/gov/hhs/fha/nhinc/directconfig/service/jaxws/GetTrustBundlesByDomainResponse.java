
package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.nhindirect.config.store.TrustBundleDomainReltn;

@XmlRootElement(name = "getTrustBundlesByDomainResponse", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTrustBundlesByDomainResponse", namespace = "http://nhind.org/config")
public class GetTrustBundlesByDomainResponse {

    @XmlElement(name = "return", namespace = "")
    private Collection<TrustBundleDomainReltn> _return;

    /**
     * 
     * @return
     *     returns Collection<TrustBundleDomainReltn>
     */
    public Collection<TrustBundleDomainReltn> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(Collection<TrustBundleDomainReltn> _return) {
        this._return = _return;
    }

}
