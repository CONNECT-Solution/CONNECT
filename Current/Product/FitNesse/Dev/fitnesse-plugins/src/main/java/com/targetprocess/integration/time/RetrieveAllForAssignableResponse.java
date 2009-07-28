
package com.targetprocess.integration.time;

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
 *         &lt;element name="RetrieveAllForAssignableResult" type="{http://targetprocess.com}ArrayOfTimeDTO" minOccurs="0"/>
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
    "retrieveAllForAssignableResult"
})
@XmlRootElement(name = "RetrieveAllForAssignableResponse")
public class RetrieveAllForAssignableResponse {

    @XmlElement(name = "RetrieveAllForAssignableResult")
    protected ArrayOfTimeDTO retrieveAllForAssignableResult;

    /**
     * Gets the value of the retrieveAllForAssignableResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTimeDTO }
     *     
     */
    public ArrayOfTimeDTO getRetrieveAllForAssignableResult() {
        return retrieveAllForAssignableResult;
    }

    /**
     * Sets the value of the retrieveAllForAssignableResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTimeDTO }
     *     
     */
    public void setRetrieveAllForAssignableResult(ArrayOfTimeDTO value) {
        this.retrieveAllForAssignableResult = value;
    }

}
