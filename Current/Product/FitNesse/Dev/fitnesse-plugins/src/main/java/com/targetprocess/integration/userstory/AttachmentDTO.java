
package com.targetprocess.integration.userstory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AttachmentDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttachmentDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="AttachmentID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OriginalFileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UniqueFileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GeneralID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OwnerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AttachmentFileID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GeneralName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttachmentDTO", propOrder = {
    "attachmentID",
    "originalFileName",
    "uniqueFileName",
    "createDate",
    "description",
    "generalID",
    "messageID",
    "ownerID",
    "attachmentFileID",
    "generalName"
})
public class AttachmentDTO
    extends DataTransferObject
{

    @XmlElement(name = "AttachmentID", required = true, type = Integer.class, nillable = true)
    protected Integer attachmentID;
    @XmlElement(name = "OriginalFileName")
    protected String originalFileName;
    @XmlElement(name = "UniqueFileName")
    protected String uniqueFileName;
    @XmlElement(name = "CreateDate", required = true, nillable = true)
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "GeneralID", required = true, type = Integer.class, nillable = true)
    protected Integer generalID;
    @XmlElement(name = "MessageID", required = true, type = Integer.class, nillable = true)
    protected Integer messageID;
    @XmlElement(name = "OwnerID", required = true, type = Integer.class, nillable = true)
    protected Integer ownerID;
    @XmlElement(name = "AttachmentFileID", required = true, type = Integer.class, nillable = true)
    protected Integer attachmentFileID;
    @XmlElement(name = "GeneralName")
    protected String generalName;

    /**
     * Gets the value of the attachmentID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAttachmentID() {
        return attachmentID;
    }

    /**
     * Sets the value of the attachmentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAttachmentID(Integer value) {
        this.attachmentID = value;
    }

    /**
     * Gets the value of the originalFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * Sets the value of the originalFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalFileName(String value) {
        this.originalFileName = value;
    }

    /**
     * Gets the value of the uniqueFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueFileName() {
        return uniqueFileName;
    }

    /**
     * Sets the value of the uniqueFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueFileName(String value) {
        this.uniqueFileName = value;
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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMessageID(Integer value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the ownerID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the value of the ownerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwnerID(Integer value) {
        this.ownerID = value;
    }

    /**
     * Gets the value of the attachmentFileID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAttachmentFileID() {
        return attachmentFileID;
    }

    /**
     * Sets the value of the attachmentFileID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAttachmentFileID(Integer value) {
        this.attachmentFileID = value;
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

}
