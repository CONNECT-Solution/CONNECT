
package com.targetprocess.integration.task;

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
 *         &lt;element name="RetrieveTeamsForTaskResult" type="{http://targetprocess.com}ArrayOfTeamDTO" minOccurs="0"/>
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
    "retrieveTeamsForTaskResult"
})
@XmlRootElement(name = "RetrieveTeamsForTaskResponse")
public class RetrieveTeamsForTaskResponse {

    @XmlElement(name = "RetrieveTeamsForTaskResult")
    protected ArrayOfTeamDTO retrieveTeamsForTaskResult;

    /**
     * Gets the value of the retrieveTeamsForTaskResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public ArrayOfTeamDTO getRetrieveTeamsForTaskResult() {
        return retrieveTeamsForTaskResult;
    }

    /**
     * Sets the value of the retrieveTeamsForTaskResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public void setRetrieveTeamsForTaskResult(ArrayOfTeamDTO value) {
        this.retrieveTeamsForTaskResult = value;
    }

}
