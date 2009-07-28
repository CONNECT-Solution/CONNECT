
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
 *         &lt;element name="RetrieveAllForEntityStateResult" type="{http://targetprocess.com}ArrayOfUserStoryDTO" minOccurs="0"/>
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
    "retrieveAllForEntityStateResult"
})
@XmlRootElement(name = "RetrieveAllForEntityStateResponse")
public class RetrieveAllForEntityStateResponse {

    @XmlElement(name = "RetrieveAllForEntityStateResult")
    protected ArrayOfUserStoryDTO retrieveAllForEntityStateResult;

    /**
     * Gets the value of the retrieveAllForEntityStateResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public ArrayOfUserStoryDTO getRetrieveAllForEntityStateResult() {
        return retrieveAllForEntityStateResult;
    }

    /**
     * Sets the value of the retrieveAllForEntityStateResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public void setRetrieveAllForEntityStateResult(ArrayOfUserStoryDTO value) {
        this.retrieveAllForEntityStateResult = value;
    }

}
