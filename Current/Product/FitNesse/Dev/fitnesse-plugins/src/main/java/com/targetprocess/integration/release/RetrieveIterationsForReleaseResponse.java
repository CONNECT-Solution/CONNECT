
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
 *         &lt;element name="RetrieveIterationsForReleaseResult" type="{http://targetprocess.com}ArrayOfIterationDTO" minOccurs="0"/>
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
    "retrieveIterationsForReleaseResult"
})
@XmlRootElement(name = "RetrieveIterationsForReleaseResponse")
public class RetrieveIterationsForReleaseResponse {

    @XmlElement(name = "RetrieveIterationsForReleaseResult")
    protected ArrayOfIterationDTO retrieveIterationsForReleaseResult;

    /**
     * Gets the value of the retrieveIterationsForReleaseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfIterationDTO }
     *     
     */
    public ArrayOfIterationDTO getRetrieveIterationsForReleaseResult() {
        return retrieveIterationsForReleaseResult;
    }

    /**
     * Sets the value of the retrieveIterationsForReleaseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfIterationDTO }
     *     
     */
    public void setRetrieveIterationsForReleaseResult(ArrayOfIterationDTO value) {
        this.retrieveIterationsForReleaseResult = value;
    }

}
