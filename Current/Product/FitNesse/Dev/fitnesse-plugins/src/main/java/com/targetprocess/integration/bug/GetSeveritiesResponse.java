
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
 *         &lt;element name="GetSeveritiesResult" type="{http://targetprocess.com}ArrayOfSeverityDTO" minOccurs="0"/>
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
    "getSeveritiesResult"
})
@XmlRootElement(name = "GetSeveritiesResponse")
public class GetSeveritiesResponse {

    @XmlElement(name = "GetSeveritiesResult")
    protected ArrayOfSeverityDTO getSeveritiesResult;

    /**
     * Gets the value of the getSeveritiesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSeverityDTO }
     *     
     */
    public ArrayOfSeverityDTO getGetSeveritiesResult() {
        return getSeveritiesResult;
    }

    /**
     * Sets the value of the getSeveritiesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSeverityDTO }
     *     
     */
    public void setGetSeveritiesResult(ArrayOfSeverityDTO value) {
        this.getSeveritiesResult = value;
    }

}
