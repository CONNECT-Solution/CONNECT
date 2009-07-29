
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
 *         &lt;element name="RetrieveRevisionAssignablesForTaskResult" type="{http://targetprocess.com}ArrayOfRevisionAssignableDTO" minOccurs="0"/>
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
    "retrieveRevisionAssignablesForTaskResult"
})
@XmlRootElement(name = "RetrieveRevisionAssignablesForTaskResponse")
public class RetrieveRevisionAssignablesForTaskResponse {

    @XmlElement(name = "RetrieveRevisionAssignablesForTaskResult")
    protected ArrayOfRevisionAssignableDTO retrieveRevisionAssignablesForTaskResult;

    /**
     * Gets the value of the retrieveRevisionAssignablesForTaskResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public ArrayOfRevisionAssignableDTO getRetrieveRevisionAssignablesForTaskResult() {
        return retrieveRevisionAssignablesForTaskResult;
    }

    /**
     * Sets the value of the retrieveRevisionAssignablesForTaskResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRevisionAssignableDTO }
     *     
     */
    public void setRetrieveRevisionAssignablesForTaskResult(ArrayOfRevisionAssignableDTO value) {
        this.retrieveRevisionAssignablesForTaskResult = value;
    }

}
