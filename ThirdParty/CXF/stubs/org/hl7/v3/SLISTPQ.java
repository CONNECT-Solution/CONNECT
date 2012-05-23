
package org.hl7.v3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SLIST_PQ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLIST_PQ">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ANY">
 *       &lt;sequence>
 *         &lt;element name="origin" type="{urn:hl7-org:v3}PQ"/>
 *         &lt;element name="scale" type="{urn:hl7-org:v3}PQ"/>
 *         &lt;element name="digits" type="{urn:hl7-org:v3}list_int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLIST_PQ", propOrder = {
    "origin",
    "scale",
    "digits"
})
public class SLISTPQ
    extends ANY
{

    @XmlElement(required = true)
    protected PQ origin;
    @XmlElement(required = true)
    protected PQ scale;
    @XmlList
    @XmlElement(required = true)
    protected List<BigInteger> digits;

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link PQ }
     *     
     */
    public PQ getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQ }
     *     
     */
    public void setOrigin(PQ value) {
        this.origin = value;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return
     *     possible object is
     *     {@link PQ }
     *     
     */
    public PQ getScale() {
        return scale;
    }

    /**
     * Sets the value of the scale property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQ }
     *     
     */
    public void setScale(PQ value) {
        this.scale = value;
    }

    /**
     * Gets the value of the digits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the digits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDigits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getDigits() {
        if (digits == null) {
            digits = new ArrayList<BigInteger>();
        }
        return this.digits;
    }

}
