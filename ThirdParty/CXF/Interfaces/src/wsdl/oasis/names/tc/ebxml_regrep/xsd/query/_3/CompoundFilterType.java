
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CompoundFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompoundFilterType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}FilterType">
 *       &lt;sequence>
 *         &lt;element name="LeftFilter" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}FilterType"/>
 *         &lt;element name="RightFilter" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}FilterType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="logicalOperator" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *             &lt;enumeration value="AND"/>
 *             &lt;enumeration value="OR"/>
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
@XmlType(name = "CompoundFilterType", propOrder = {
    "leftFilter",
    "rightFilter"
})
public class CompoundFilterType
    extends FilterType
{

    @XmlElement(name = "LeftFilter", required = true)
    protected FilterType leftFilter;
    @XmlElement(name = "RightFilter", required = true)
    protected FilterType rightFilter;
    @XmlAttribute(name = "logicalOperator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String logicalOperator;

    /**
     * Gets the value of the leftFilter property.
     * 
     * @return
     *     possible object is
     *     {@link FilterType }
     *     
     */
    public FilterType getLeftFilter() {
        return leftFilter;
    }

    /**
     * Sets the value of the leftFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterType }
     *     
     */
    public void setLeftFilter(FilterType value) {
        this.leftFilter = value;
    }

    /**
     * Gets the value of the rightFilter property.
     * 
     * @return
     *     possible object is
     *     {@link FilterType }
     *     
     */
    public FilterType getRightFilter() {
        return rightFilter;
    }

    /**
     * Sets the value of the rightFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterType }
     *     
     */
    public void setRightFilter(FilterType value) {
        this.rightFilter = value;
    }

    /**
     * Gets the value of the logicalOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalOperator() {
        return logicalOperator;
    }

    /**
     * Sets the value of the logicalOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalOperator(String value) {
        this.logicalOperator = value;
    }

}
