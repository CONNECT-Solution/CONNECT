
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
 *         &lt;element name="RetrieveAllForSeverityResult" type="{http://targetprocess.com}ArrayOfBugDTO" minOccurs="0"/>
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
    "retrieveAllForSeverityResult"
})
@XmlRootElement(name = "RetrieveAllForSeverityResponse")
public class RetrieveAllForSeverityResponse {

    @XmlElement(name = "RetrieveAllForSeverityResult")
    protected ArrayOfBugDTO retrieveAllForSeverityResult;

    /**
     * Gets the value of the retrieveAllForSeverityResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfBugDTO }
     *     
     */
    public ArrayOfBugDTO getRetrieveAllForSeverityResult() {
        return retrieveAllForSeverityResult;
    }

    /**
     * Sets the value of the retrieveAllForSeverityResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfBugDTO }
     *     
     */
    public void setRetrieveAllForSeverityResult(ArrayOfBugDTO value) {
        this.retrieveAllForSeverityResult = value;
    }

}
