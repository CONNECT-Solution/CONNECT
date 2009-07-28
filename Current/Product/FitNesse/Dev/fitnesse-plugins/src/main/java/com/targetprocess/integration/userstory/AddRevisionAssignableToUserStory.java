
package com.targetprocess.integration.userstory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="userStoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="revisionAssignable" type="{http://targetprocess.com}RevisionAssignableDTO" minOccurs="0"/>
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
    "userStoryID",
    "revisionAssignable"
})
@XmlRootElement(name = "AddRevisionAssignableToUserStory")
public class AddRevisionAssignableToUserStory {

    protected int userStoryID;
    protected RevisionAssignableDTO revisionAssignable;

    /**
     * Gets the value of the userStoryID property.
     * 
     */
    public int getUserStoryID() {
        return userStoryID;
    }

    /**
     * Sets the value of the userStoryID property.
     * 
     */
    public void setUserStoryID(int value) {
        this.userStoryID = value;
    }

    /**
     * Gets the value of the revisionAssignable property.
     * 
     * @return
     *     possible object is
     *     {@link RevisionAssignableDTO }
     *     
     */
    public RevisionAssignableDTO getRevisionAssignable() {
        return revisionAssignable;
    }

    /**
     * Sets the value of the revisionAssignable property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevisionAssignableDTO }
     *     
     */
    public void setRevisionAssignable(RevisionAssignableDTO value) {
        this.revisionAssignable = value;
    }

}
