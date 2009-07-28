
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
 *         &lt;element name="RetrieveCommentsForBugResult" type="{http://targetprocess.com}ArrayOfCommentDTO" minOccurs="0"/>
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
    "retrieveCommentsForBugResult"
})
@XmlRootElement(name = "RetrieveCommentsForBugResponse")
public class RetrieveCommentsForBugResponse {

    @XmlElement(name = "RetrieveCommentsForBugResult")
    protected ArrayOfCommentDTO retrieveCommentsForBugResult;

    /**
     * Gets the value of the retrieveCommentsForBugResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public ArrayOfCommentDTO getRetrieveCommentsForBugResult() {
        return retrieveCommentsForBugResult;
    }

    /**
     * Sets the value of the retrieveCommentsForBugResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public void setRetrieveCommentsForBugResult(ArrayOfCommentDTO value) {
        this.retrieveCommentsForBugResult = value;
    }

}
