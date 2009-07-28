
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
 *         &lt;element name="actorID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="userID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "actorID",
    "userID"
})
@XmlRootElement(name = "AssignUserAsActor")
public class AssignUserAsActor {

    protected int userStoryID;
    protected int actorID;
    protected int userID;

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
     * Gets the value of the actorID property.
     * 
     */
    public int getActorID() {
        return actorID;
    }

    /**
     * Sets the value of the actorID property.
     * 
     */
    public void setActorID(int value) {
        this.actorID = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     */
    public void setUserID(int value) {
        this.userID = value;
    }

}
