
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ServiceBindingQuery" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceQueryType", propOrder = {
    "serviceBindingQuery"
})
public class ServiceQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "ServiceBindingQuery")
    protected List<ServiceBindingQueryType> serviceBindingQuery;

    /**
     * Gets the value of the serviceBindingQuery property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceBindingQuery property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceBindingQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceBindingQueryType }
     * 
     * 
     */
    public List<ServiceBindingQueryType> getServiceBindingQuery() {
        if (serviceBindingQuery == null) {
            serviceBindingQuery = new ArrayList<ServiceBindingQueryType>();
        }
        return this.serviceBindingQuery;
    }

}
