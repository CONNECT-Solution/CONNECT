
package com.targetprocess.integration.bug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TeamDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TeamDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="TeamID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ActorID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssignableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ActorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TeamDTO", propOrder = {
    "teamID",
    "assignableID",
    "userID",
    "actorID",
    "assignableName",
    "actorName"
})
public class TeamDTO
    extends DataTransferObject
{

    @XmlElement(name = "TeamID", required = true, type = Integer.class, nillable = true)
    protected Integer teamID;
    @XmlElement(name = "AssignableID", required = true, type = Integer.class, nillable = true)
    protected Integer assignableID;
    @XmlElement(name = "UserID", required = true, type = Integer.class, nillable = true)
    protected Integer userID;
    @XmlElement(name = "ActorID", required = true, type = Integer.class, nillable = true)
    protected Integer actorID;
    @XmlElement(name = "AssignableName")
    protected String assignableName;
    @XmlElement(name = "ActorName")
    protected String actorName;

    /**
     * Gets the value of the teamID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTeamID() {
        return teamID;
    }

    /**
     * Sets the value of the teamID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTeamID(Integer value) {
        this.teamID = value;
    }

    /**
     * Gets the value of the assignableID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssignableID() {
        return assignableID;
    }

    /**
     * Sets the value of the assignableID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssignableID(Integer value) {
        this.assignableID = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUserID(Integer value) {
        this.userID = value;
    }

    /**
     * Gets the value of the actorID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getActorID() {
        return actorID;
    }

    /**
     * Sets the value of the actorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setActorID(Integer value) {
        this.actorID = value;
    }

    /**
     * Gets the value of the assignableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignableName() {
        return assignableName;
    }

    /**
     * Sets the value of the assignableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignableName(String value) {
        this.assignableName = value;
    }

    /**
     * Gets the value of the actorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActorName() {
        return actorName;
    }

    /**
     * Sets the value of the actorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActorName(String value) {
        this.actorName = value;
    }

}
