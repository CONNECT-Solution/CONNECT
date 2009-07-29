
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
 *         &lt;element name="RetrieveRevisionAssignablesForUserStoryResult" type="{http://targetprocess.com}ArrayOfRevisionAssignableDTO" minOccurs="0"/>
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
    "retrieveRevisionAssignablesForUserStoryResult"
})
@XmlRootElement(name = "RetrieveRevisionAssignablesForUserStoryResponse")
public class RetrieveRevisionAssignablesForUserStoryResponse {

    @XmlElement(name = "RetrieveRevisionAssignablesForUserStoryResult")
    protected ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForUserStoryResult;

    /**
     * Gets the value of the retrieveRevisionAssignablesForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public ArrayOfRevisionAssignableDTO getRetrieveRevisionAssignablesForUserStoryResult() {
        return retrieveRevisionAssignablesForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveRevisionAssignablesForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public void setRetrieveRevisionAssignablesForUserStoryResult(ArrayOfRevisionAssignableDTO value) {
        this.retrieveRevisionAssignablesForUserStoryResult = value;
    }

}
