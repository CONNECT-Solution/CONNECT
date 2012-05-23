
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Note: because this type is defined as an extension of SXCM_T,
 *             all of the attributes and elements accepted for T are also
 *             accepted by this definition.  However, they are NOT allowed
 *             by the normative description of this type.  Unfortunately,
 *             we cannot write a general purpose schematron contraints to
 *             provide that extra validation, thus applications must be
 *             aware that instance (fragments) that pass validation with
 *             this might might still not be legal.
 *          
 * 
 * <p>Java class for EIVL_TS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EIVL_TS">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_TS">
 *       &lt;sequence>
 *         &lt;element name="event" type="{urn:hl7-org:v3}EIVL.event" minOccurs="0"/>
 *         &lt;element name="offset" type="{urn:hl7-org:v3}IVL_PQ" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EIVL_TS", propOrder = {
    "event",
    "offset"
})
public class EIVLTS
    extends SXCMTS
{

    protected EIVLEvent event;
    protected IVLPQ offset;

    /**
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link EIVLEvent }
     *     
     */
    public EIVLEvent getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link EIVLEvent }
     *     
     */
    public void setEvent(EIVLEvent value) {
        this.event = value;
    }

    /**
     * Gets the value of the offset property.
     * 
     * @return
     *     possible object is
     *     {@link IVLPQ }
     *     
     */
    public IVLPQ getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLPQ }
     *     
     */
    public void setOffset(IVLPQ value) {
        this.offset = value;
    }

}
