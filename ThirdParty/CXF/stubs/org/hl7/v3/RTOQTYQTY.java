
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RTO_QTY_QTY complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RTO_QTY_QTY">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}QTY">
 *       &lt;sequence>
 *         &lt;element name="numerator" type="{urn:hl7-org:v3}QTY"/>
 *         &lt;element name="denominator" type="{urn:hl7-org:v3}QTY"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RTO_QTY_QTY", propOrder = {
    "numerator",
    "denominator"
})
@XmlSeeAlso({
    RTO.class
})
public class RTOQTYQTY
    extends QTY
{

    @XmlElement(required = true)
    protected QTY numerator;
    @XmlElement(required = true)
    protected QTY denominator;

    /**
     * Gets the value of the numerator property.
     * 
     * @return
     *     possible object is
     *     {@link QTY }
     *     
     */
    public QTY getNumerator() {
        return numerator;
    }

    /**
     * Sets the value of the numerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link QTY }
     *     
     */
    public void setNumerator(QTY value) {
        this.numerator = value;
    }

    /**
     * Gets the value of the denominator property.
     * 
     * @return
     *     possible object is
     *     {@link QTY }
     *     
     */
    public QTY getDenominator() {
        return denominator;
    }

    /**
     * Sets the value of the denominator property.
     * 
     * @param value
     *     allowed object is
     *     {@link QTY }
     *     
     */
    public void setDenominator(QTY value) {
        this.denominator = value;
    }

}
