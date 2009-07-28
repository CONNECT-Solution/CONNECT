
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
 *         &lt;element name="RetrieveTeamsForBugResult" type="{http://targetprocess.com}ArrayOfTeamDTO" minOccurs="0"/>
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
    "retrieveTeamsForBugResult"
})
@XmlRootElement(name = "RetrieveTeamsForBugResponse")
public class RetrieveTeamsForBugResponse {

    @XmlElement(name = "RetrieveTeamsForBugResult")
    protected ArrayOfTeamDTO retrieveTeamsForBugResult;

    /**
     * Gets the value of the retrieveTeamsForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public ArrayOfTeamDTO getRetrieveTeamsForBugResult() {
        return retrieveTeamsForBugResult;
    }

    /**
     * Sets the value of the retrieveTeamsForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTeamDTO }
     *     
     */
    public void setRetrieveTeamsForBugResult(ArrayOfTeamDTO value) {
        this.retrieveTeamsForBugResult = value;
    }

}
