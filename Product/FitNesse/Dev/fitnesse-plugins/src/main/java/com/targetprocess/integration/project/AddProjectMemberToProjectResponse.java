
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
 *         &lt;element name="AddProjectMemberToProjectResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addProjectMemberToProjectResult"
})
@XmlRootElement(name = "AddProjectMemberToProjectResponse")
public class AddProjectMemberToProjectResponse {

    @XmlElement(name = "AddProjectMemberToProjectResult")
    protected int addProjectMemberToProjectResult;

    /**
     * Gets the value of the addProjectMemberToProjectResult property.
     * 
     */
    public int getAddProjectMemberToProjectResult() {
        return addProjectMemberToProjectResult;
    }

    /**
     * Sets the value of the addProjectMemberToProjectResult property.
     * 
     */
    public void setAddProjectMemberToProjectResult(int value) {
        this.addProjectMemberToProjectResult = value;
    }

}
