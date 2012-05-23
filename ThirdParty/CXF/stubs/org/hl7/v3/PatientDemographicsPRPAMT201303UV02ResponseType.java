
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PatientDemographics_PRPA_MT201303UV02ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PatientDemographics_PRPA_MT201303UV02ResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}PRPA_MT201303UV02.Patient"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientDemographics_PRPA_MT201303UV02ResponseType", propOrder = {
    "subject"
})
public class PatientDemographicsPRPAMT201303UV02ResponseType {

    @XmlElement(required = true)
    protected PRPAMT201303UV02Patient subject;

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT201303UV02Patient }
     *     
     */
    public PRPAMT201303UV02Patient getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT201303UV02Patient }
     *     
     */
    public void setSubject(PRPAMT201303UV02Patient value) {
        this.subject = value;
    }

}
