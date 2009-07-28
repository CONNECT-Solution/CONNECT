
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
 *         &lt;element name="RetrieveAllForReleaseResult" type="{http://targetprocess.com}ArrayOfUserStoryDTO" minOccurs="0"/>
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
    "retrieveAllForReleaseResult"
})
@XmlRootElement(name = "RetrieveAllForReleaseResponse")
public class RetrieveAllForReleaseResponse {

    @XmlElement(name = "RetrieveAllForReleaseResult")
    protected ArrayOfUserStoryDTO retrieveAllForReleaseResult;

    /**
     * Gets the value of the retrieveAllForReleaseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public ArrayOfUserStoryDTO getRetrieveAllForReleaseResult() {
        return retrieveAllForReleaseResult;
    }

    /**
     * Sets the value of the retrieveAllForReleaseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public void setRetrieveAllForReleaseResult(ArrayOfUserStoryDTO value) {
        this.retrieveAllForReleaseResult = value;
    }

}
