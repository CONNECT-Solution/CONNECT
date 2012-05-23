
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryResponseType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObjectList"/>
 *       &lt;/sequence>
 *       &lt;attribute name="startIndex" type="{http://www.w3.org/2001/XMLSchema}integer" default="0" />
 *       &lt;attribute name="totalResultCount" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "registryObjectList"
})
@XmlRootElement(name = "AdhocQueryResponse")
public class AdhocQueryResponse
    extends RegistryResponseType
{

    @XmlElement(name = "RegistryObjectList", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", required = true)
    protected RegistryObjectListType registryObjectList;
    @XmlAttribute(name = "startIndex")
    protected BigInteger startIndex;
    @XmlAttribute(name = "totalResultCount")
    protected BigInteger totalResultCount;

    /**
     * Gets the value of the registryObjectList property.
     * 
     * @return
     *     possible object is
     *     {@link RegistryObjectListType }
     *     
     */
    public RegistryObjectListType getRegistryObjectList() {
        return registryObjectList;
    }

    /**
     * Sets the value of the registryObjectList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistryObjectListType }
     *     
     */
    public void setRegistryObjectList(RegistryObjectListType value) {
        this.registryObjectList = value;
    }

    /**
     * Gets the value of the startIndex property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStartIndex() {
        if (startIndex == null) {
            return new BigInteger("0");
        } else {
            return startIndex;
        }
    }

    /**
     * Sets the value of the startIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStartIndex(BigInteger value) {
        this.startIndex = value;
    }

    /**
     * Gets the value of the totalResultCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalResultCount() {
        return totalResultCount;
    }

    /**
     * Sets the value of the totalResultCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalResultCount(BigInteger value) {
        this.totalResultCount = value;
    }

}
