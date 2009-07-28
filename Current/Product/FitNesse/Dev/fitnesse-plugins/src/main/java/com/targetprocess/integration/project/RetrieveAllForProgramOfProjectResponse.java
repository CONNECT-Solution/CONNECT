
package com.targetprocess.integration.project;

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
 *         &lt;element name="RetrieveAllForProgramOfProjectResult" type="{http://targetprocess.com}ArrayOfProjectDTO" minOccurs="0"/>
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
    "retrieveAllForProgramOfProjectResult"
})
@XmlRootElement(name = "RetrieveAllForProgramOfProjectResponse")
public class RetrieveAllForProgramOfProjectResponse {

    @XmlElement(name = "RetrieveAllForProgramOfProjectResult")
    protected ArrayOfProjectDTO retrieveAllForProgramOfProjectResult;

    /**
     * Gets the value of the retrieveAllForProgramOfProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProjectDTO }
     *     
     */
    public ArrayOfProjectDTO getRetrieveAllForProgramOfProjectResult() {
        return retrieveAllForProgramOfProjectResult;
    }

    /**
     * Sets the value of the retrieveAllForProgramOfProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProjectDTO }
     *     
     */
    public void setRetrieveAllForProgramOfProjectResult(ArrayOfProjectDTO value) {
        this.retrieveAllForProgramOfProjectResult = value;
    }

}
