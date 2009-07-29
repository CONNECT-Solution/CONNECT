
package com.targetprocess.integration.task;

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
 *         &lt;element name="taskID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "taskID",
    "revisionAssignable"
})
@XmlRootElement(name = "AddRevisionAssignableToTask")
public class AddRevisionAssignableToTask {

    protected int taskID;
    protected RevisionAssignableDTO revisionAssignable;

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
