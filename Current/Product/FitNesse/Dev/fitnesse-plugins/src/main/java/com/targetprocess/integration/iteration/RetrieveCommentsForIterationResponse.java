
package com.targetprocess.integration.iteration;

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
 *         &lt;element name="RetrieveCommentsForIterationResult" type="{http://targetprocess.com}ArrayOfCommentDTO" minOccurs="0"/>
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
    "retrieveCommentsForIterationResult"
})
@XmlRootElement(name = "RetrieveCommentsForIterationResponse")
public class RetrieveCommentsForIterationResponse {

    @XmlElement(name = "RetrieveCommentsForIterationResult")
    protected ArrayOfCommentDTO retrieveCommentsForIterationResult;

    /**
     * Gets the value of the retrieveCommentsForIterationResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public ArrayOfCommentDTO getRetrieveCommentsForIterationResult() {
        return retrieveCommentsForIterationResult;
    }

    /**
     * Sets the value of the retrieveCommentsForIterationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public void setRetrieveCommentsForIterationResult(ArrayOfCommentDTO value) {
        this.retrieveCommentsForIterationResult = value;
    }

}
