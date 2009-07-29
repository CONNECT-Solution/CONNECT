
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
 *         &lt;element name="RetrieveCommentsForUserStoryResult" type="{http://targetprocess.com}ArrayOfCommentDTO" minOccurs="0"/>
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
    "retrieveCommentsForUserStoryResult"
})
@XmlRootElement(name = "RetrieveCommentsForUserStoryResponse")
public class RetrieveCommentsForUserStoryResponse {

    @XmlElement(name = "RetrieveCommentsForUserStoryResult")
    protected ArrayOfCommentDTO retrieveCommentsForUserStoryResult;

    /**
     * Gets the value of the retrieveCommentsForUserStoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public ArrayOfCommentDTO getRetrieveCommentsForUserStoryResult() {
        return retrieveCommentsForUserStoryResult;
    }

    /**
     * Sets the value of the retrieveCommentsForUserStoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public void setRetrieveCommentsForUserStoryResult(ArrayOfCommentDTO value) {
        this.retrieveCommentsForUserStoryResult = value;
    }

}
