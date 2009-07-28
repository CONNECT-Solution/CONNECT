
package com.targetprocess.integration.task;

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
 *         &lt;element name="AddTeamToTaskResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addTeamToTaskResult"
})
@XmlRootElement(name = "AddTeamToTaskResponse")
public class AddTeamToTaskResponse {

    @XmlElement(name = "AddTeamToTaskResult")
    protected int addTeamToTaskResult;

    /**
     * Gets the value of the addTeamToTaskResult property.
     * 
     */
    public int getAddTeamToTaskResult() {
        return addTeamToTaskResult;
    }

    /**
     * Sets the value of the addTeamToTaskResult property.
     * 
     */
    public void setAddTeamToTaskResult(int value) {
        this.addTeamToTaskResult = value;
    }

}
