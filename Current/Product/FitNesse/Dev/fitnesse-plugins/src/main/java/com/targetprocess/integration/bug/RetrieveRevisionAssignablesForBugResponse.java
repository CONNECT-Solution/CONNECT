
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
 *         &lt;element name="RetrieveRevisionAssignablesForBugResult" type="{http://targetprocess.com}ArrayOfRevisionAssignableDTO" minOccurs="0"/>
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
    "retrieveRevisionAssignablesForBugResult"
})
@XmlRootElement(name = "RetrieveRevisionAssignablesForBugResponse")
public class RetrieveRevisionAssignablesForBugResponse {

    @XmlElement(name = "RetrieveRevisionAssignablesForBugResult")
    protected ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForBugResult;

    /**
     * Gets the value of the retrieveRevisionAssignablesForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public ArrayOfRevisionAssignableDTO getRetrieveRevisionAssignablesForBugResult() {
        return retrieveRevisionAssignablesForBugResult;
    }

    /**
     * Sets the value of the retrieveRevisionAssignablesForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public void setRetrieveRevisionAssignablesForBugResult(ArrayOfRevisionAssignableDTO value) {
        this.retrieveRevisionAssignablesForBugResult = value;
    }

}
