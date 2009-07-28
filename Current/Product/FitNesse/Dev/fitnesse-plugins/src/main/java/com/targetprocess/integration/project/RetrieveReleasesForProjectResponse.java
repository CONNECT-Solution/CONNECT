
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
 *         &lt;element name="RetrieveReleasesForProjectResult" type="{http://targetprocess.com}ArrayOfReleaseDTO" minOccurs="0"/>
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
    "retrieveReleasesForProjectResult"
})
@XmlRootElement(name = "RetrieveReleasesForProjectResponse")
public class RetrieveReleasesForProjectResponse {

    @XmlElement(name = "RetrieveReleasesForProjectResult")
    protected ArrayOfReleaseDTO retrieveReleasesForProjectResult;

    /**
     * Gets the value of the retrieveReleasesForProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfReleaseDTO }
     *     
     */
    public ArrayOfReleaseDTO getRetrieveReleasesForProjectResult() {
        return retrieveReleasesForProjectResult;
    }

    /**
     * Sets the value of the retrieveReleasesForProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfReleaseDTO }
     *     
     */
    public void setRetrieveReleasesForProjectResult(ArrayOfReleaseDTO value) {
        this.retrieveReleasesForProjectResult = value;
    }

}
