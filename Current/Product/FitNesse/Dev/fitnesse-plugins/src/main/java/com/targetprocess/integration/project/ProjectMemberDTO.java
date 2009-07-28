
package com.targetprocess.integration.project;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ProjectMemberDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProjectMemberDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="ProjectMemberID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WeeklyAvailableHours" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="MembershipEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Allocation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RoleID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RoleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectMemberDTO", propOrder = {
    "projectMemberID",
    "weeklyAvailableHours",
    "membershipEndDate",
    "allocation",
    "projectID",
    "userID",
    "roleID",
    "projectName",
    "roleName"
})
public class ProjectMemberDTO
    extends DataTransferObject
{

    @XmlElement(name = "ProjectMemberID", required = true, type = Integer.class, nillable = true)
    protected Integer projectMemberID;
    @XmlElement(name = "WeeklyAvailableHours", required = true, nillable = true)
    protected BigDecimal weeklyAvailableHours;
    @XmlElement(name = "MembershipEndDate", required = true, nillable = true)
    protected XMLGregorianCalendar membershipEndDate;
    @XmlElement(name = "Allocation", required = true, type = Integer.class, nillable = true)
    protected Integer allocation;
    @XmlElement(name = "ProjectID", required = true, type = Integer.class, nillable = true)
    protected Integer projectID;
    @XmlElement(name = "UserID", required = true, type = Integer.class, nillable = true)
    protected Integer userID;
    @XmlElement(name = "RoleID", required = true, type = Integer.class, nillable = true)
    protected Integer roleID;
    @XmlElement(name = "ProjectName")
    protected String projectName;
    @XmlElement(name = "RoleName")
    protected String roleName;

    /**
     * Gets the value of the projectMemberID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProjectMemberID() {
        return projectMemberID;
    }

    /**
     * Sets the value of the projectMemberID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProjectMemberID(Integer value) {
        this.projectMemberID = value;
    }

    /**
     * Gets the value of the weeklyAvailableHours property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeeklyAvailableHours() {
        return weeklyAvailableHours;
    }

    /**
     * Sets the value of the weeklyAvailableHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeeklyAvailableHours(BigDecimal value) {
        this.weeklyAvailableHours = value;
    }

    /**
     * Gets the value of the membershipEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMembershipEndDate() {
        return membershipEndDate;
    }

    /**
     * Sets the value of the membershipEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMembershipEndDate(XMLGregorianCalendar value) {
        this.membershipEndDate = value;
    }

    /**
     * Gets the value of the allocation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAllocation() {
        return allocation;
    }

    /**
     * Sets the value of the allocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAllocation(Integer value) {
        this.allocation = value;
    }

    /**
     * Gets the value of the projectID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the projectID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProjectID(Integer value) {
        this.projectID = value;
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
     * Gets the value of the roleID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRoleID() {
        return roleID;
    }

    /**
     * Sets the value of the roleID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRoleID(Integer value) {
        this.roleID = value;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectName(String value) {
        this.projectName = value;
    }

    /**
     * Gets the value of the roleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

}
