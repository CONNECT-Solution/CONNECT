
package oasis.names.tc.ebxml_regrep.xsd.rim._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;


/**
 * Mapping of the same named interface in ebRIM.
 * 
 * <p>Java class for RegistryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObjectType">
 *       &lt;attribute name="operator" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" />
 *       &lt;attribute name="specificationVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="replicationSyncLatency" type="{http://www.w3.org/2001/XMLSchema}duration" default="P1D" />
 *       &lt;attribute name="catalogingLatency" type="{http://www.w3.org/2001/XMLSchema}duration" default="P1D" />
 *       &lt;attribute name="conformanceProfile" default="registryLite">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *             &lt;enumeration value="registryFull"/>
 *             &lt;enumeration value="registryLite"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistryType")
public class RegistryType
    extends RegistryObjectType
{

    @XmlAttribute(name = "operator", required = true)
    protected String operator;
    @XmlAttribute(name = "specificationVersion", required = true)
    protected String specificationVersion;
    @XmlAttribute(name = "replicationSyncLatency")
    protected Duration replicationSyncLatency;
    @XmlAttribute(name = "catalogingLatency")
    protected Duration catalogingLatency;
    @XmlAttribute(name = "conformanceProfile")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String conformanceProfile;

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the specificationVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificationVersion() {
        return specificationVersion;
    }

    /**
     * Sets the value of the specificationVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificationVersion(String value) {
        this.specificationVersion = value;
    }

    /**
     * Gets the value of the replicationSyncLatency property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getReplicationSyncLatency() {
        return replicationSyncLatency;
    }

    /**
     * Sets the value of the replicationSyncLatency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setReplicationSyncLatency(Duration value) {
        this.replicationSyncLatency = value;
    }

    /**
     * Gets the value of the catalogingLatency property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getCatalogingLatency() {
        return catalogingLatency;
    }

    /**
     * Sets the value of the catalogingLatency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setCatalogingLatency(Duration value) {
        this.catalogingLatency = value;
    }

    /**
     * Gets the value of the conformanceProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConformanceProfile() {
        if (conformanceProfile == null) {
            return "registryLite";
        } else {
            return conformanceProfile;
        }
    }

    /**
     * Sets the value of the conformanceProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConformanceProfile(String value) {
        this.conformanceProfile = value;
    }

}
