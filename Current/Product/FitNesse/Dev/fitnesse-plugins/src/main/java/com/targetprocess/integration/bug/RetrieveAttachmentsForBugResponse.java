
package com.targetprocess.integration.bug;

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
 *         &lt;element name="RetrieveAttachmentsForBugResult" type="{http://targetprocess.com}ArrayOfAttachmentDTO" minOccurs="0"/>
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
    "retrieveAttachmentsForBugResult"
})
@XmlRootElement(name = "RetrieveAttachmentsForBugResponse")
public class RetrieveAttachmentsForBugResponse {

    @XmlElement(name = "RetrieveAttachmentsForBugResult")
    protected ArrayOfAttachmentDTO retrieveAttachmentsForBugResult;

    /**
     * Gets the value of the retrieveAttachmentsForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public ArrayOfAttachmentDTO getRetrieveAttachmentsForBugResult() {
        return retrieveAttachmentsForBugResult;
    }

    /**
     * Sets the value of the retrieveAttachmentsForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public void setRetrieveAttachmentsForBugResult(ArrayOfAttachmentDTO value) {
        this.retrieveAttachmentsForBugResult = value;
    }

}
