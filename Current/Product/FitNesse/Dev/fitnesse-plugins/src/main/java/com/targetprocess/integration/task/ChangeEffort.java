
package com.targetprocess.integration.task;

import java.math.BigDecimal;
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
 *         &lt;element name="taskID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="actorID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="effort" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
    "taskID",
    "actorID",
    "effort"
})
@XmlRootElement(name = "ChangeEffort")
public class ChangeEffort {

    protected int taskID;
    protected int actorID;
    @XmlElement(required = true)
    protected BigDecimal effort;

    /**
     * Gets the value of the taskID property.
     * 
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Sets the value of the taskID property.
     * 
     */
    public void setTaskID(int value) {
        this.taskID = value;
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

}
