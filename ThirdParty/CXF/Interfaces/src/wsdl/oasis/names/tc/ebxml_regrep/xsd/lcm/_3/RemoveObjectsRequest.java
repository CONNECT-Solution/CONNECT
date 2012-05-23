
package oasis.names.tc.ebxml_regrep.xsd.lcm._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryRequestType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}AdhocQuery" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}ObjectRefList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="deletionScope" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" default="urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteAll" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "adhocQuery",
    "objectRefList"
})
@XmlRootElement(name = "RemoveObjectsRequest")
public class RemoveObjectsRequest
    extends RegistryRequestType
{

    @XmlElement(name = "AdhocQuery", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0")
    protected AdhocQueryType adhocQuery;
    @XmlElement(name = "ObjectRefList", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0")
    protected ObjectRefListType objectRefList;
    @XmlAttribute(name = "deletionScope")
    protected String deletionScope;

    /**
     * Gets the value of the adhocQuery property.
     * 
     * @return
     *     possible object is
     *     {@link AdhocQueryType }
     *     
     */
    public AdhocQueryType getAdhocQuery() {
        return adhocQuery;
    }

    /**
     * Sets the value of the adhocQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdhocQueryType }
     *     
     */
    public void setAdhocQuery(AdhocQueryType value) {
        this.adhocQuery = value;
    }

    /**
     * Gets the value of the objectRefList property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectRefListType }
     *     
     */
    public ObjectRefListType getObjectRefList() {
        return objectRefList;
    }

    /**
     * Sets the value of the objectRefList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectRefListType }
     *     
     */
    public void setObjectRefList(ObjectRefListType value) {
        this.objectRefList = value;
    }

    /**
     * Gets the value of the deletionScope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeletionScope() {
        if (deletionScope == null) {
            return "urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteAll";
        } else {
            return deletionScope;
        }
    }

    /**
     * Sets the value of the deletionScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeletionScope(String value) {
        this.deletionScope = value;
    }

}
