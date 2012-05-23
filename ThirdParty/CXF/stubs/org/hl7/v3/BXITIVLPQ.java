
package org.hl7.v3;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BXIT_IVL_PQ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BXIT_IVL_PQ">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}IVL_PQ">
 *       &lt;attribute name="qty" type="{urn:hl7-org:v3}int" default="1" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BXIT_IVL_PQ")
public class BXITIVLPQ
    extends IVLPQ
{

    @XmlAttribute(name = "qty")
    protected BigInteger qty;

    /**
     * Gets the value of the qty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQty() {
        if (qty == null) {
            return new BigInteger("1");
        } else {
            return qty;
        }
    }

    /**
     * Sets the value of the qty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQty(BigInteger value) {
        this.qty = value;
    }

}
