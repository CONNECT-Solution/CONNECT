
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FindPatients_PRPA_IN201305UV02RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindPatients_PRPA_IN201305UV02RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="localDeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receiverOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="query" type="{urn:hl7-org:v3}PRPA_IN201305UV02.MCCI_MT000100UV01.Message"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindPatients_PRPA_IN201305UV02RequestType", propOrder = {
    "localDeviceId",
    "senderOID",
    "receiverOID",
    "query"
})
public class FindPatientsPRPAIN201305UV02RequestType {

    @XmlElement(required = true)
    protected String localDeviceId;
    @XmlElement(required = true)
    protected String senderOID;
    @XmlElement(required = true)
    protected String receiverOID;
    @XmlElement(required = true)
    protected PRPAIN201305UV02MCCIMT000100UV01Message query;

    /**
     * Gets the value of the localDeviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalDeviceId() {
        return localDeviceId;
    }

    /**
     * Sets the value of the localDeviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalDeviceId(String value) {
        this.localDeviceId = value;
    }

    /**
     * Gets the value of the senderOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderOID() {
        return senderOID;
    }

    /**
     * Sets the value of the senderOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderOID(String value) {
        this.senderOID = value;
    }

    /**
     * Gets the value of the receiverOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverOID() {
        return receiverOID;
    }

    /**
     * Sets the value of the receiverOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverOID(String value) {
        this.receiverOID = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAIN201305UV02MCCIMT000100UV01Message }
     *     
     */
    public PRPAIN201305UV02MCCIMT000100UV01Message getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAIN201305UV02MCCIMT000100UV01Message }
     *     
     */
    public void setQuery(PRPAIN201305UV02MCCIMT000100UV01Message value) {
        this.query = value;
    }

}
