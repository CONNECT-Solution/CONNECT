
package com.targetprocess.integration.task;

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
 *         &lt;element name="RetrieveAttachedRequestsForTaskResult" type="{http://targetprocess.com}ArrayOfRequestGeneralDTO" minOccurs="0"/>
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
    "retrieveAttachedRequestsForTaskResult"
})
@XmlRootElement(name = "RetrieveAttachedRequestsForTaskResponse")
public class RetrieveAttachedRequestsForTaskResponse {

    @XmlElement(name = "RetrieveAttachedRequestsForTaskResult")
    protected ArrayOfRequestGeneralDTO retrieveAttachedRequestsForTaskResult;

    /**
     * Gets the value of the retrieveAttachedRequestsForTaskResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public ArrayOfRequestGeneralDTO getRetrieveAttachedRequestsForTaskResult() {
        return retrieveAttachedRequestsForTaskResult;
    }

    /**
     * Sets the value of the retrieveAttachedRequestsForTaskResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public void setRetrieveAttachedRequestsForTaskResult(ArrayOfRequestGeneralDTO value) {
        this.retrieveAttachedRequestsForTaskResult = value;
    }

}
