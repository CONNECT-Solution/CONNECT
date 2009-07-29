
package com.targetprocess.integration.project;

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
 *         &lt;element name="projectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="projectMember" type="{http://targetprocess.com}ProjectMemberDTO" minOccurs="0"/>
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
    "projectID",
    "projectMember"
})
@XmlRootElement(name = "AddProjectMemberToProject")
public class AddProjectMemberToProject {

    protected int projectID;
    protected ProjectMemberDTO projectMember;

    /**
     * Gets the value of the projectID property.
     * 
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the projectID property.
     * 
     */
    public void setProjectID(int value) {
        this.projectID = value;
    }

    /**
     * Gets the value of the projectMember property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectMemberDTO }
     *     
     */
    public ProjectMemberDTO getProjectMember() {
        return projectMember;
    }

    /**
     * Sets the value of the projectMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectMemberDTO }
     *     
     */
    public void setProjectMember(ProjectMemberDTO value) {
        this.projectMember = value;
    }

}
