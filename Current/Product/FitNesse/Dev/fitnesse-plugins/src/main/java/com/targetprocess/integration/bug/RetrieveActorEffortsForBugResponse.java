
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
 *         &lt;element name="RetrieveActorEffortsForBugResult" type="{http://targetprocess.com}ArrayOfActorEffortDTO" minOccurs="0"/>
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
    "retrieveActorEffortsForBugResult"
})
@XmlRootElement(name = "RetrieveActorEffortsForBugResponse")
public class RetrieveActorEffortsForBugResponse {

    @XmlElement(name = "RetrieveActorEffortsForBugResult")
    protected ArrayOfActorEffortDTO retrieveActorEffortsForBugResult;

    /**
     * Gets the value of the retrieveActorEffortsForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfActorEffortDTO }
     *     
     */
    public ArrayOfActorEffortDTO getRetrieveActorEffortsForBugResult() {
        return retrieveActorEffortsForBugResult;
    }

    /**
     * Sets the value of the retrieveActorEffortsForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfActorEffortDTO }
     *     
     */
    public void setRetrieveActorEffortsForBugResult(ArrayOfActorEffortDTO value) {
        this.retrieveActorEffortsForBugResult = value;
    }

}
