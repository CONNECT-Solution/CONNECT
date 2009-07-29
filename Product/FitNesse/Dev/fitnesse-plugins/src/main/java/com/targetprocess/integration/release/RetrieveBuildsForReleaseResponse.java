
package com.targetprocess.integration.release;

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
 *         &lt;element name="RetrieveBuildsForReleaseResult" type="{http://targetprocess.com}ArrayOfBuildDTO" minOccurs="0"/>
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
    "retrieveBuildsForReleaseResult"
})
@XmlRootElement(name = "RetrieveBuildsForReleaseResponse")
public class RetrieveBuildsForReleaseResponse {

    @XmlElement(name = "RetrieveBuildsForReleaseResult")
    protected ArrayOfBuildDTO retrieveBuildsForReleaseResult;

    /**
     * Gets the value of the retrieveBuildsForReleaseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfBuildDTO }
     *     
     */
    public ArrayOfBuildDTO getRetrieveBuildsForReleaseResult() {
        return retrieveBuildsForReleaseResult;
    }

    /**
     * Sets the value of the retrieveBuildsForReleaseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfBuildDTO }
     *     
     */
    public void setRetrieveBuildsForReleaseResult(ArrayOfBuildDTO value) {
        this.retrieveBuildsForReleaseResult = value;
    }

}
