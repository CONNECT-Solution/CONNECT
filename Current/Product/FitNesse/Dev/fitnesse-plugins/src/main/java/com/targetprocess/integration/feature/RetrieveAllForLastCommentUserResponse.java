
package com.targetprocess.integration.feature;

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
 *         &lt;element name="RetrieveAllForLastCommentUserResult" type="{http://targetprocess.com}ArrayOfFeatureDTO" minOccurs="0"/>
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
    "retrieveAllForLastCommentUserResult"
})
@XmlRootElement(name = "RetrieveAllForLastCommentUserResponse")
public class RetrieveAllForLastCommentUserResponse {

    @XmlElement(name = "RetrieveAllForLastCommentUserResult")
    protected ArrayOfFeatureDTO retrieveAllForLastCommentUserResult;

    /**
     * Gets the value of the retrieveAllForLastCommentUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFeatureDTO }
     *     
     */
    public ArrayOfFeatureDTO getRetrieveAllForLastCommentUserResult() {
        return retrieveAllForLastCommentUserResult;
    }

    /**
     * Sets the value of the retrieveAllForLastCommentUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFeatureDTO }
     *     
     */
    public void setRetrieveAllForLastCommentUserResult(ArrayOfFeatureDTO value) {
        this.retrieveAllForLastCommentUserResult = value;
    }

}
