
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
 *         &lt;element name="RetrieveCommentsForProjectResult" type="{http://targetprocess.com}ArrayOfCommentDTO" minOccurs="0"/>
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
    "retrieveCommentsForProjectResult"
})
@XmlRootElement(name = "RetrieveCommentsForProjectResponse")
public class RetrieveCommentsForProjectResponse {

    @XmlElement(name = "RetrieveCommentsForProjectResult")
    protected ArrayOfCommentDTO retrieveCommentsForProjectResult;

    /**
     * Gets the value of the retrieveCommentsForProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public ArrayOfCommentDTO getRetrieveCommentsForProjectResult() {
        return retrieveCommentsForProjectResult;
    }

    /**
     * Sets the value of the retrieveCommentsForProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCommentDTO }
     *     
     */
    public void setRetrieveCommentsForProjectResult(ArrayOfCommentDTO value) {
        this.retrieveCommentsForProjectResult = value;
    }

}
