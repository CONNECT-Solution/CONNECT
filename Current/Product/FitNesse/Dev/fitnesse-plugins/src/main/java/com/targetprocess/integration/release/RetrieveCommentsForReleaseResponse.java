
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
 *         &lt;element name="RetrieveCommentsForReleaseResult" type="{http://targetprocess.com}ArrayOfCommentDTO" minOccurs="0"/>
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
    "retrieveCommentsForReleaseResult"
})
@XmlRootElement(name = "RetrieveCommentsForReleaseResponse")
public class RetrieveCommentsForReleaseResponse {

    @XmlElement(name = "RetrieveCommentsForReleaseResult")
    protected ArrayOfCommentDTO retrieveCommentsForReleaseResult;

    /**
     * Gets the value of the retrieveCommentsForReleaseResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public ArrayOfCommentDTO getRetrieveCommentsForReleaseResult() {
        return retrieveCommentsForReleaseResult;
    }

    /**
     * Sets the value of the retrieveCommentsForReleaseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public void setRetrieveCommentsForReleaseResult(ArrayOfCommentDTO value) {
        this.retrieveCommentsForReleaseResult = value;
    }

}
