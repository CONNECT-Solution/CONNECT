
package com.targetprocess.integration.userstory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RetrieveAttachmentsForUserStoryResult" type="{http://targetprocess.com}ArrayOfAttachmentDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "retrieveAttachmentsForUserStoryResult"
})
@XmlRootElement(name = "RetrieveAttachmentsForUserStoryResponse")
public class RetrieveAttachmentsForUserStoryResponse {

    @XmlElement(name = "RetrieveAttachmentsForUserStoryResult")
    protected ArrayOfAttachmentDTO retrieveAttachmentsForUserStoryResult;

    /**
     * Gets the value of the retrieveAttachmentsForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public ArrayOfAttachmentDTO getRetrieveAttachmentsForUserStoryResult() {
        return retrieveAttachmentsForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveAttachmentsForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public void setRetrieveAttachmentsForUserStoryResult(ArrayOfAttachmentDTO value) {
        this.retrieveAttachmentsForUserStoryResult = value;
    }

}
