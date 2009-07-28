
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
 *         &lt;element name="RetrieveUserStoriesForIterationResult" type="{http://targetprocess.com}ArrayOfUserStoryDTO" minOccurs="0"/>
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
    "retrieveUserStoriesForIterationResult"
})
@XmlRootElement(name = "RetrieveUserStoriesForIterationResponse")
public class RetrieveUserStoriesForIterationResponse {

    @XmlElement(name = "RetrieveUserStoriesForIterationResult")
    protected ArrayOfUserStoryDTO retrieveUserStoriesForIterationResult;

    /**
     * Gets the value of the retrieveUserStoriesForIterationResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public ArrayOfUserStoryDTO getRetrieveUserStoriesForIterationResult() {
        return retrieveUserStoriesForIterationResult;
    }

    /**
     * Sets the value of the retrieveUserStoriesForIterationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public void setRetrieveUserStoriesForIterationResult(ArrayOfUserStoryDTO value) {
        this.retrieveUserStoriesForIterationResult = value;
    }

}
