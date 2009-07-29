
package com.targetprocess.integration.project;

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
 *         &lt;element name="RetrieveAllForProcessResult" type="{http://targetprocess.com}ArrayOfProjectDTO" minOccurs="0"/>
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
    "retrieveAllForProcessResult"
})
@XmlRootElement(name = "RetrieveAllForProcessResponse")
public class RetrieveAllForProcessResponse {

    @XmlElement(name = "RetrieveAllForProcessResult")
    protected ArrayOfProjectDTO retrieveAllForProcessResult;

    /**
     * Gets the value of the retrieveAllForProcessResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProjectDTO }
     *     
     */
    public ArrayOfProjectDTO getRetrieveAllForProcessResult() {
        return retrieveAllForProcessResult;
    }

    /**
     * Sets the value of the retrieveAllForProcessResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProjectDTO }
     *     
     */
    public void setRetrieveAllForProcessResult(ArrayOfProjectDTO value) {
        this.retrieveAllForProcessResult = value;
    }

}
