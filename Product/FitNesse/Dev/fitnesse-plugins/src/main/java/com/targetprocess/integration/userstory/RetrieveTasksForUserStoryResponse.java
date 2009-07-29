
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
 *         &lt;element name="RetrieveTasksForUserStoryResult" type="{http://targetprocess.com}ArrayOfTaskDTO" minOccurs="0"/>
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
    "retrieveTasksForUserStoryResult"
})
@XmlRootElement(name = "RetrieveTasksForUserStoryResponse")
public class RetrieveTasksForUserStoryResponse {

    @XmlElement(name = "RetrieveTasksForUserStoryResult")
    protected ArrayOfTaskDTO retrieveTasksForUserStoryResult;

    /**
     * Gets the value of the retrieveTasksForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTaskDTO }
     *     
     */
    public ArrayOfTaskDTO getRetrieveTasksForUserStoryResult() {
        return retrieveTasksForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveTasksForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTaskDTO }
     *     
     */
    public void setRetrieveTasksForUserStoryResult(ArrayOfTaskDTO value) {
        this.retrieveTasksForUserStoryResult = value;
    }

}
