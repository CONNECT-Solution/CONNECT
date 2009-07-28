
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
 *         &lt;element name="RetrieveOpenForUserResult" type="{http://targetprocess.com}ArrayOfUserStoryDTO" minOccurs="0"/>
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
    "retrieveOpenForUserResult"
})
@XmlRootElement(name = "RetrieveOpenForUserResponse")
public class RetrieveOpenForUserResponse {

    @XmlElement(name = "RetrieveOpenForUserResult")
    protected ArrayOfUserStoryDTO retrieveOpenForUserResult;

    /**
     * Gets the value of the retrieveOpenForUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public ArrayOfUserStoryDTO getRetrieveOpenForUserResult() {
        return retrieveOpenForUserResult;
    }

    /**
     * Sets the value of the retrieveOpenForUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserStoryDTO }
     *     
     */
    public void setRetrieveOpenForUserResult(ArrayOfUserStoryDTO value) {
        this.retrieveOpenForUserResult = value;
    }

}
