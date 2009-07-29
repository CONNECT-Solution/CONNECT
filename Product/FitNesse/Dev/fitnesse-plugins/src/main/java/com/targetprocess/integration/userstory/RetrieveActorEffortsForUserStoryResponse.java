
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
 *         &lt;element name="RetrieveActorEffortsForUserStoryResult" type="{http://targetprocess.com}ArrayOfActorEffortDTO" minOccurs="0"/>
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
    "retrieveActorEffortsForUserStoryResult"
})
@XmlRootElement(name = "RetrieveActorEffortsForUserStoryResponse")
public class RetrieveActorEffortsForUserStoryResponse {

    @XmlElement(name = "RetrieveActorEffortsForUserStoryResult")
    protected ArrayOfActorEffortDTO retrieveActorEffortsForUserStoryResult;

    /**
     * Gets the value of the retrieveActorEffortsForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfActorEffortDTO }
     *     
     */
    public ArrayOfActorEffortDTO getRetrieveActorEffortsForUserStoryResult() {
        return retrieveActorEffortsForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveActorEffortsForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfActorEffortDTO }
     *     
     */
    public void setRetrieveActorEffortsForUserStoryResult(ArrayOfActorEffortDTO value) {
        this.retrieveActorEffortsForUserStoryResult = value;
    }

}
