
package com.targetprocess.integration.project;

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
 *         &lt;element name="RetrieveAttachedRequestsForProjectResult" type="{http://targetprocess.com}ArrayOfRequestGeneralDTO" minOccurs="0"/>
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
    "retrieveAttachedRequestsForProjectResult"
})
@XmlRootElement(name = "RetrieveAttachedRequestsForProjectResponse")
public class RetrieveAttachedRequestsForProjectResponse {

    @XmlElement(name = "RetrieveAttachedRequestsForProjectResult")
    protected ArrayOfRequestGeneralDTO retrieveAttachedRequestsForProjectResult;

    /**
     * Gets the value of the retrieveAttachedRequestsForProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public ArrayOfRequestGeneralDTO getRetrieveAttachedRequestsForProjectResult() {
        return retrieveAttachedRequestsForProjectResult;
    }

    /**
     * Sets the value of the retrieveAttachedRequestsForProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRequestGeneralDTO }
     *     
     */
    public void setRetrieveAttachedRequestsForProjectResult(ArrayOfRequestGeneralDTO value) {
        this.retrieveAttachedRequestsForProjectResult = value;
    }

}
