
package com.targetprocess.integration.project;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfProjectMemberDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfProjectMemberDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProjectMemberDTO" type="{http://targetprocess.com}ProjectMemberDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfProjectMemberDTO", propOrder = {
    "projectMemberDTO"
})
public class ArrayOfProjectMemberDTO {

    @XmlElement(name = "ProjectMemberDTO", required = true, nillable = true)
    protected List<ProjectMemberDTO> projectMemberDTO;

    /**
     * Gets the value of the projectMemberDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projectMemberDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjectMemberDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectMemberDTO }
     * 
     * 
     */
    public List<ProjectMemberDTO> getProjectMemberDTO() {
        if (projectMemberDTO == null) {
            projectMemberDTO = new ArrayList<ProjectMemberDTO>();
        }
        return this.projectMemberDTO;
    }

}
