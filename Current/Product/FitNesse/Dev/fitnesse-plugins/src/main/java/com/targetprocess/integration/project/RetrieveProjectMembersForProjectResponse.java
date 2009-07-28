
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
 *         &lt;element name="RetrieveProjectMembersForProjectResult" type="{http://targetprocess.com}ArrayOfProjectMemberDTO" minOccurs="0"/>
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
    "retrieveProjectMembersForProjectResult"
})
@XmlRootElement(name = "RetrieveProjectMembersForProjectResponse")
public class RetrieveProjectMembersForProjectResponse {

    @XmlElement(name = "RetrieveProjectMembersForProjectResult")
    protected ArrayOfProjectMemberDTO retrieveProjectMembersForProjectResult;

    /**
     * Gets the value of the retrieveProjectMembersForProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProjectMemberDTO }
     *     
     */
    public ArrayOfProjectMemberDTO getRetrieveProjectMembersForProjectResult() {
        return retrieveProjectMembersForProjectResult;
    }

    /**
     * Sets the value of the retrieveProjectMembersForProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProjectMemberDTO }
     *     
     */
    public void setRetrieveProjectMembersForProjectResult(ArrayOfProjectMemberDTO value) {
        this.retrieveProjectMembersForProjectResult = value;
    }

}
