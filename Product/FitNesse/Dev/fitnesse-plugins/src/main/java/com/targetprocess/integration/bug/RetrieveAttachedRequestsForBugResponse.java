
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
 *         &lt;element name="RetrieveAttachedRequestsForBugResult" type="{http://targetprocess.com}ArrayOfRequestGeneralDTO" minOccurs="0"/>
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
    "retrieveAttachedRequestsForBugResult"
})
@XmlRootElement(name = "RetrieveAttachedRequestsForBugResponse")
public class RetrieveAttachedRequestsForBugResponse {

    @XmlElement(name = "RetrieveAttachedRequestsForBugResult")
    protected ArrayOfRequestGeneralDTO retrieveAttachedRequestsForBugResult;

    /**
     * Gets the value of the retrieveAttachedRequestsForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public ArrayOfRequestGeneralDTO getRetrieveAttachedRequestsForBugResult() {
        return retrieveAttachedRequestsForBugResult;
    }

    /**
     * Sets the value of the retrieveAttachedRequestsForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public void setRetrieveAttachedRequestsForBugResult(ArrayOfRequestGeneralDTO value) {
        this.retrieveAttachedRequestsForBugResult = value;
    }

}
