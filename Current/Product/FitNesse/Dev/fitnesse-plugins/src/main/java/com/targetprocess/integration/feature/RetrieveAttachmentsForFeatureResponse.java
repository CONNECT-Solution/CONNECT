
package com.targetprocess.integration.feature;

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
 *         &lt;element name="RetrieveAttachmentsForFeatureResult" type="{http://targetprocess.com}ArrayOfAttachmentDTO" minOccurs="0"/>
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
    "retrieveAttachmentsForFeatureResult"
})
@XmlRootElement(name = "RetrieveAttachmentsForFeatureResponse")
public class RetrieveAttachmentsForFeatureResponse {

    @XmlElement(name = "RetrieveAttachmentsForFeatureResult")
    protected ArrayOfAttachmentDTO retrieveAttachmentsForFeatureResult;

    /**
     * Gets the value of the retrieveAttachmentsForFeatureResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public ArrayOfAttachmentDTO getRetrieveAttachmentsForFeatureResult() {
        return retrieveAttachmentsForFeatureResult;
    }

    /**
     * Sets the value of the retrieveAttachmentsForFeatureResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttachmentDTO }
     *     
     */
    public void setRetrieveAttachmentsForFeatureResult(ArrayOfAttachmentDTO value) {
        this.retrieveAttachmentsForFeatureResult = value;
    }

}
