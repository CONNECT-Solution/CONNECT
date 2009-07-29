
package com.targetprocess.integration.release;

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
 *         &lt;element name="RetrieveFeaturesForReleaseResult" type="{http://targetprocess.com}ArrayOfFeatureDTO" minOccurs="0"/>
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
    "retrieveFeaturesForReleaseResult"
})
@XmlRootElement(name = "RetrieveFeaturesForReleaseResponse")
public class RetrieveFeaturesForReleaseResponse {

    @XmlElement(name = "RetrieveFeaturesForReleaseResult")
    protected ArrayOfFeatureDTO retrieveFeaturesForReleaseResult;

    /**
     * Gets the value of the retrieveFeaturesForReleaseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFeatureDTO }
     *     
     */
    public ArrayOfFeatureDTO getRetrieveFeaturesForReleaseResult() {
        return retrieveFeaturesForReleaseResult;
    }

    /**
     * Sets the value of the retrieveFeaturesForReleaseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFeatureDTO }
     *     
     */
    public void setRetrieveFeaturesForReleaseResult(ArrayOfFeatureDTO value) {
        this.retrieveFeaturesForReleaseResult = value;
    }

}
