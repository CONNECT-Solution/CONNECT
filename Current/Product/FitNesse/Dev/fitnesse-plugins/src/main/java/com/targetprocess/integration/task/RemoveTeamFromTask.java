
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
 *         &lt;element name="teamID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "teamID"
})
@XmlRootElement(name = "RemoveTeamFromTask")
public class RemoveTeamFromTask {

    protected int taskID;
    protected int teamID;

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
     * Gets the value of the teamID property.
     * 
     */
    public int getTeamID() {
        return teamID;
    }

    /**
     * Sets the value of the teamID property.
     * 
     */
    public void setTeamID(int value) {
        this.teamID = value;
    }

}
