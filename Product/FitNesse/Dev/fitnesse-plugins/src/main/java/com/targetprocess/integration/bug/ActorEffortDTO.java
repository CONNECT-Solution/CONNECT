
package com.targetprocess.integration.bug;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActorEffortDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActorEffortDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="ActorEffortID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Effort" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="EffortCompleted" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="EffortToDo" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TimeSpent" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="TimeRemain" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SubstractionFromParentEffort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="AssignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "ActorEffortDTO", propOrder = {
    "actorEffortID",
    "effort",
    "effortCompleted",
    "effortToDo",
    "timeSpent",
    "timeRemain",
    "substractionFromParentEffort",
    "assignableID",
    "actorID",
    "assignableName",
    "actorName"
})
public class ActorEffortDTO
    extends DataTransferObject
{

    @XmlElement(name = "ActorEffortID", required = true, type = Integer.class, nillable = true)
    protected Integer actorEffortID;
    @XmlElement(name = "Effort", required = true, nillable = true)
    protected BigDecimal effort;
    @XmlElement(name = "EffortCompleted", required = true, nillable = true)
    protected BigDecimal effortCompleted;
    @XmlElement(name = "EffortToDo", required = true, nillable = true)
    protected BigDecimal effortToDo;
    @XmlElement(name = "TimeSpent", required = true, nillable = true)
    protected BigDecimal timeSpent;
    @XmlElement(name = "TimeRemain", required = true, nillable = true)
    protected BigDecimal timeRemain;
    @XmlElement(name = "SubstractionFromParentEffort", required = true, type = Boolean.class, nillable = true)
    protected Boolean substractionFromParentEffort;
    @XmlElement(name = "AssignableID", required = true, type = Integer.class, nillable = true)
    protected Integer assignableID;
    @XmlElement(name = "ActorID", required = true, type = Integer.class, nillable = true)
    protected Integer actorID;
    @XmlElement(name = "AssignableName")
    protected String assignableName;
    @XmlElement(name = "ActorName")
    protected String actorName;

    /**
     * Gets the value of the actorEffortID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getActorEffortID() {
        return actorEffortID;
    }

    /**
     * Sets the value of the actorEffortID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setActorEffortID(Integer value) {
        this.actorEffortID = value;
    }

    /**
     * Gets the value of the effort property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEffort() {
        return effort;
    }

    /**
     * Sets the value of the effort property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEffort(BigDecimal value) {
        this.effort = value;
    }

    /**
     * Gets the value of the effortCompleted property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEffortCompleted() {
        return effortCompleted;
    }

    /**
     * Sets the value of the effortCompleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEffortCompleted(BigDecimal value) {
        this.effortCompleted = value;
    }

    /**
     * Gets the value of the effortToDo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEffortToDo() {
        return effortToDo;
    }

    /**
     * Sets the value of the effortToDo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEffortToDo(BigDecimal value) {
        this.effortToDo = value;
    }

    /**
     * Gets the value of the timeSpent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTimeSpent() {
        return timeSpent;
    }

    /**
     * Sets the value of the timeSpent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTimeSpent(BigDecimal value) {
        this.timeSpent = value;
    }

    /**
     * Gets the value of the timeRemain property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTimeRemain() {
        return timeRemain;
    }

    /**
     * Sets the value of the timeRemain property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTimeRemain(BigDecimal value) {
        this.timeRemain = value;
    }

    /**
     * Gets the value of the substractionFromParentEffort property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubstractionFromParentEffort() {
        return substractionFromParentEffort;
    }

    /**
     * Sets the value of the substractionFromParentEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubstractionFromParentEffort(Boolean value) {
        this.substractionFromParentEffort = value;
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
