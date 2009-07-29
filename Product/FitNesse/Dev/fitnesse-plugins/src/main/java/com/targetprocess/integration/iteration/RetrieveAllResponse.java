
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
 *         &lt;element name="RetrieveAllResult" type="{http://targetprocess.com}ArrayOfIterationDTO" minOccurs="0"/>
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
    "retrieveAllResult"
})
@XmlRootElement(name = "RetrieveAllResponse")
public class RetrieveAllResponse {

    @XmlElement(name = "RetrieveAllResult")
    protected ArrayOfIterationDTO retrieveAllResult;

    /**
     * Gets the value of the retrieveAllResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfIterationDTO }
     *     
     */
    public ArrayOfIterationDTO getRetrieveAllResult() {
        return retrieveAllResult;
    }

    /**
     * Sets the value of the retrieveAllResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfIterationDTO }
     *     
     */
    public void setRetrieveAllResult(ArrayOfIterationDTO value) {
        this.retrieveAllResult = value;
    }

}
