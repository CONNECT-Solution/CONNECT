
package com.targetprocess.integration.release;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for RequestGeneralDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestGeneralDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="RequestGeneralID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="IsAttached" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="GeneralID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GeneralName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestGeneralDTO", propOrder = {
    "requestGeneralID",
    "createDate",
    "isAttached",
    "generalID",
    "requestID",
    "generalName",
    "requestName"
})
public class RequestGeneralDTO
    extends DataTransferObject
{

    @XmlElement(name = "RequestGeneralID", required = true, type = Integer.class, nillable = true)
    protected Integer requestGeneralID;
    @XmlElement(name = "CreateDate", required = true, nillable = true)
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "IsAttached", required = true, type = Boolean.class, nillable = true)
    protected Boolean isAttached;
    @XmlElement(name = "GeneralID", required = true, type = Integer.class, nillable = true)
    protected Integer generalID;
    @XmlElement(name = "RequestID", required = true, type = Integer.class, nillable = true)
    protected Integer requestID;
    @XmlElement(name = "GeneralName")
    protected String generalName;
    @XmlElement(name = "RequestName")
    protected String requestName;

    /**
     * Gets the value of the requestGeneralID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRequestGeneralID() {
        return requestGeneralID;
    }

    /**
     * Sets the value of the requestGeneralID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRequestGeneralID(Integer value) {
        this.requestGeneralID = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the isAttached property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAttached() {
        return isAttached;
    }

    /**
     * Sets the value of the isAttached property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAttached(Boolean value) {
        this.isAttached = value;
    }

    /**
     * Gets the value of the generalID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGeneralID() {
        return generalID;
    }

    /**
     * Sets the value of the generalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGeneralID(Integer value) {
        this.generalID = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRequestID(Integer value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the generalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneralName() {
        return generalName;
    }

    /**
     * Sets the value of the generalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneralName(String value) {
        this.generalName = value;
    }

    /**
     * Gets the value of the requestName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestName() {
        return requestName;
    }

    /**
     * Sets the value of the requestName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestName(String value) {
        this.requestName = value;
    }

}
