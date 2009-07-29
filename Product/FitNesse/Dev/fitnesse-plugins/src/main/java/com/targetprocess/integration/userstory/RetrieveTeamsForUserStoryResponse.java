
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
 *         &lt;element name="RetrieveTeamsForUserStoryResult" type="{http://targetprocess.com}ArrayOfTeamDTO" minOccurs="0"/>
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
    "retrieveTeamsForUserStoryResult"
})
@XmlRootElement(name = "RetrieveTeamsForUserStoryResponse")
public class RetrieveTeamsForUserStoryResponse {

    @XmlElement(name = "RetrieveTeamsForUserStoryResult")
    protected ArrayOfTeamDTO retrieveTeamsForUserStoryResult;

    /**
     * Gets the value of the retrieveTeamsForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public ArrayOfTeamDTO getRetrieveTeamsForUserStoryResult() {
        return retrieveTeamsForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveTeamsForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public void setRetrieveTeamsForUserStoryResult(ArrayOfTeamDTO value) {
        this.retrieveTeamsForUserStoryResult = value;
    }

}
