
package com.targetprocess.integration.iteration;

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
 *         &lt;element name="RetrieveAttachedRequestsForIterationResult" type="{http://targetprocess.com}ArrayOfRequestGeneralDTO" minOccurs="0"/>
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
    "retrieveAttachedRequestsForIterationResult"
})
@XmlRootElement(name = "RetrieveAttachedRequestsForIterationResponse")
public class RetrieveAttachedRequestsForIterationResponse {

    @XmlElement(name = "RetrieveAttachedRequestsForIterationResult")
    protected ArrayOfRequestGeneralDTO retrieveAttachedRequestsForIterationResult;

    /**
     * Gets the value of the retrieveAttachedRequestsForIterationResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public ArrayOfRequestGeneralDTO getRetrieveAttachedRequestsForIterationResult() {
        return retrieveAttachedRequestsForIterationResult;
    }

    /**
     * Sets the value of the retrieveAttachedRequestsForIterationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public void setRetrieveAttachedRequestsForIterationResult(ArrayOfRequestGeneralDTO value) {
        this.retrieveAttachedRequestsForIterationResult = value;
    }

}
