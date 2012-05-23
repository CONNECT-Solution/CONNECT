
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CareRecord_QUPC_IN043200UV01ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CareRecord_QUPC_IN043200UV01ResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="careRecord" type="{urn:hl7-org:v3}QUPC_IN043200UV01.MFMI_MT700712UV01.Subject1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CareRecord_QUPC_IN043200UV01ResponseType", propOrder = {
    "careRecord"
})
public class CareRecordQUPCIN043200UV01ResponseType {

    @XmlElement(required = true)
    protected QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord;

    /**
     * Gets the value of the careRecord property.
     * 
     * @return
     *     possible object is
     *     {@link QUPCIN043200UV01MFMIMT700712UV01Subject1 }
     *     
     */
    public QUPCIN043200UV01MFMIMT700712UV01Subject1 getCareRecord() {
        return careRecord;
    }

    /**
     * Sets the value of the careRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link QUPCIN043200UV01MFMIMT700712UV01Subject1 }
     *     
     */
    public void setCareRecord(QUPCIN043200UV01MFMIMT700712UV01Subject1 value) {
        this.careRecord = value;
    }

}
