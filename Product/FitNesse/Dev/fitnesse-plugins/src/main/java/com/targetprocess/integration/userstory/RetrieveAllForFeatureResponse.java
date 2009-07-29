
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
 *         &lt;element name="RetrieveAllForFeatureResult" type="{http://targetprocess.com}ArrayOfUserStoryDTO" minOccurs="0"/>
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
    "retrieveAllForFeatureResult"
})
@XmlRootElement(name = "RetrieveAllForFeatureResponse")
public class RetrieveAllForFeatureResponse {

    @XmlElement(name = "RetrieveAllForFeatureResult")
    protected ArrayOfUserStoryDTO retrieveAllForFeatureResult;

    /**
     * Gets the value of the retrieveAllForFeatureResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public ArrayOfUserStoryDTO getRetrieveAllForFeatureResult() {
        return retrieveAllForFeatureResult;
    }

    /**
     * Sets the value of the retrieveAllForFeatureResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public void setRetrieveAllForFeatureResult(ArrayOfUserStoryDTO value) {
        this.retrieveAllForFeatureResult = value;
    }

}
