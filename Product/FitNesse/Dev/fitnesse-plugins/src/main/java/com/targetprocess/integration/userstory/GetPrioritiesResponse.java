
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
 *         &lt;element name="GetPrioritiesResult" type="{http://targetprocess.com}ArrayOfPriorityDTO" minOccurs="0"/>
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
    "getPrioritiesResult"
})
@XmlRootElement(name = "GetPrioritiesResponse")
public class GetPrioritiesResponse {

    @XmlElement(name = "GetPrioritiesResult")
    protected ArrayOfPriorityDTO getPrioritiesResult;

    /**
     * Gets the value of the getPrioritiesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPriorityDTO }
     *     
     */
    public ArrayOfPriorityDTO getGetPrioritiesResult() {
        return getPrioritiesResult;
    }

    /**
     * Sets the value of the getPrioritiesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPriorityDTO }
     *     
     */
    public void setGetPrioritiesResult(ArrayOfPriorityDTO value) {
        this.getPrioritiesResult = value;
    }

}
