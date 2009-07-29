
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
 *         &lt;element name="RetrieveAllForPriorityResult" type="{http://targetprocess.com}ArrayOfBugDTO" minOccurs="0"/>
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
    "retrieveAllForPriorityResult"
})
@XmlRootElement(name = "RetrieveAllForPriorityResponse")
public class RetrieveAllForPriorityResponse {

    @XmlElement(name = "RetrieveAllForPriorityResult")
    protected ArrayOfBugDTO retrieveAllForPriorityResult;

    /**
     * Gets the value of the retrieveAllForPriorityResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfBugDTO }
     *     
     */
    public ArrayOfBugDTO getRetrieveAllForPriorityResult() {
        return retrieveAllForPriorityResult;
    }

    /**
     * Sets the value of the retrieveAllForPriorityResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfBugDTO }
     *     
     */
    public void setRetrieveAllForPriorityResult(ArrayOfBugDTO value) {
        this.retrieveAllForPriorityResult = value;
    }

}
