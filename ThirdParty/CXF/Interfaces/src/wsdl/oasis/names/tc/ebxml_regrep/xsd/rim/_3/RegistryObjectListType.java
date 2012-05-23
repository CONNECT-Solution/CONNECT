
package oasis.names.tc.ebxml_regrep.xsd.rim._3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegistryObjectListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistryObjectListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}Identifiable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistryObjectListType", propOrder = {
    "identifiable"
})
public class RegistryObjectListType {

    @XmlElementRef(name = "Identifiable", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends IdentifiableType>> identifiable;

    /**
     * Gets the value of the identifiable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifiable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifiable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AssociationType1 }{@code >}
     * {@link JAXBElement }{@code <}{@link PersonType }{@code >}
     * {@link JAXBElement }{@code <}{@link SpecificationLinkType }{@code >}
     * {@link JAXBElement }{@code <}{@link ClassificationType }{@code >}
     * {@link JAXBElement }{@code <}{@link AuditableEventType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdhocQueryType }{@code >}
     * {@link JAXBElement }{@code <}{@link ServiceBindingType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExtrinsicObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link FederationType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegistryPackageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ServiceType }{@code >}
     * {@link JAXBElement }{@code <}{@link ClassificationSchemeType }{@code >}
     * {@link JAXBElement }{@code <}{@link ClassificationNodeType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExternalIdentifierType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegistryObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubscriptionType }{@code >}
     * {@link JAXBElement }{@code <}{@link OrganizationType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExternalLinkType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegistryType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdentifiableType }{@code >}
     * {@link JAXBElement }{@code <}{@link ObjectRefType }{@code >}
     * {@link JAXBElement }{@code <}{@link UserType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends IdentifiableType>> getIdentifiable() {
        if (identifiable == null) {
            identifiable = new ArrayList<JAXBElement<? extends IdentifiableType>>();
        }
        return this.identifiable;
    }

}
