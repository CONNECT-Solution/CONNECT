
package com.targetprocess.integration.time;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TimeDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="TimeID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Spent" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Remain" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SpentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Estimation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CustomField1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomField15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CustomActivityID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ActorID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AssignableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomActivityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "TimeDTO", propOrder = {
    "timeID",
    "description",
    "spent",
    "remain",
    "spentDate",
    "estimation",
    "customField1",
    "customField2",
    "customField3",
    "customField4",
    "customField5",
    "customField6",
    "customField7",
    "customField8",
    "customField9",
    "customField10",
    "customField11",
    "customField12",
    "customField13",
    "customField14",
    "customField15",
    "projectID",
    "userID",
    "assignableID",
    "customActivityID",
    "actorID",
    "projectName",
    "assignableName",
    "customActivityName",
    "actorName"
})
public class TimeDTO
    extends DataTransferObject
{

    @XmlElement(name = "TimeID", required = true, type = Integer.class, nillable = true)
    protected Integer timeID;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Spent", required = true, nillable = true)
    protected BigDecimal spent;
    @XmlElement(name = "Remain", required = true, nillable = true)
    protected BigDecimal remain;
    @XmlElement(name = "SpentDate", required = true, nillable = true)
    protected XMLGregorianCalendar spentDate;
    @XmlElement(name = "Estimation", required = true, type = Boolean.class, nillable = true)
    protected Boolean estimation;
    @XmlElement(name = "CustomField1")
    protected String customField1;
    @XmlElement(name = "CustomField2")
    protected String customField2;
    @XmlElement(name = "CustomField3")
    protected String customField3;
    @XmlElement(name = "CustomField4")
    protected String customField4;
    @XmlElement(name = "CustomField5")
    protected String customField5;
    @XmlElement(name = "CustomField6")
    protected String customField6;
    @XmlElement(name = "CustomField7")
    protected String customField7;
    @XmlElement(name = "CustomField8")
    protected String customField8;
    @XmlElement(name = "CustomField9")
    protected String customField9;
    @XmlElement(name = "CustomField10")
    protected String customField10;
    @XmlElement(name = "CustomField11")
    protected String customField11;
    @XmlElement(name = "CustomField12")
    protected String customField12;
    @XmlElement(name = "CustomField13")
    protected String customField13;
    @XmlElement(name = "CustomField14")
    protected String customField14;
    @XmlElement(name = "CustomField15")
    protected String customField15;
    @XmlElement(name = "ProjectID", required = true, type = Integer.class, nillable = true)
    protected Integer projectID;
    @XmlElement(name = "UserID", required = true, type = Integer.class, nillable = true)
    protected Integer userID;
    @XmlElement(name = "AssignableID", required = true, type = Integer.class, nillable = true)
    protected Integer assignableID;
    @XmlElement(name = "CustomActivityID", required = true, type = Integer.class, nillable = true)
    protected Integer customActivityID;
    @XmlElement(name = "ActorID", required = true, type = Integer.class, nillable = true)
    protected Integer actorID;
    @XmlElement(name = "ProjectName")
    protected String projectName;
    @XmlElement(name = "AssignableName")
    protected String assignableName;
    @XmlElement(name = "CustomActivityName")
    protected String customActivityName;
    @XmlElement(name = "ActorName")
    protected String actorName;

    /**
     * Gets the value of the timeID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTimeID() {
        return timeID;
    }

    /**
     * Sets the value of the timeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTimeID(Integer value) {
        this.timeID = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the spent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSpent() {
        return spent;
    }

    /**
     * Sets the value of the spent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSpent(BigDecimal value) {
        this.spent = value;
    }

    /**
     * Gets the value of the remain property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * Sets the value of the remain property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemain(BigDecimal value) {
        this.remain = value;
    }

    /**
     * Gets the value of the spentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSpentDate() {
        return spentDate;
    }

    /**
     * Sets the value of the spentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSpentDate(XMLGregorianCalendar value) {
        this.spentDate = value;
    }

    /**
     * Gets the value of the estimation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstimation() {
        return estimation;
    }

    /**
     * Sets the value of the estimation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstimation(Boolean value) {
        this.estimation = value;
    }

    /**
     * Gets the value of the customField1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField1() {
        return customField1;
    }

    /**
     * Sets the value of the customField1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField1(String value) {
        this.customField1 = value;
    }

    /**
     * Gets the value of the customField2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField2() {
        return customField2;
    }

    /**
     * Sets the value of the customField2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField2(String value) {
        this.customField2 = value;
    }

    /**
     * Gets the value of the customField3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField3() {
        return customField3;
    }

    /**
     * Sets the value of the customField3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField3(String value) {
        this.customField3 = value;
    }

    /**
     * Gets the value of the customField4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField4() {
        return customField4;
    }

    /**
     * Sets the value of the customField4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField4(String value) {
        this.customField4 = value;
    }

    /**
     * Gets the value of the customField5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField5() {
        return customField5;
    }

    /**
     * Sets the value of the customField5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField5(String value) {
        this.customField5 = value;
    }

    /**
     * Gets the value of the customField6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField6() {
        return customField6;
    }

    /**
     * Sets the value of the customField6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField6(String value) {
        this.customField6 = value;
    }

    /**
     * Gets the value of the customField7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField7() {
        return customField7;
    }

    /**
     * Sets the value of the customField7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField7(String value) {
        this.customField7 = value;
    }

    /**
     * Gets the value of the customField8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField8() {
        return customField8;
    }

    /**
     * Sets the value of the customField8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField8(String value) {
        this.customField8 = value;
    }

    /**
     * Gets the value of the customField9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField9() {
        return customField9;
    }

    /**
     * Sets the value of the customField9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField9(String value) {
        this.customField9 = value;
    }

    /**
     * Gets the value of the customField10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField10() {
        return customField10;
    }

    /**
     * Sets the value of the customField10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField10(String value) {
        this.customField10 = value;
    }

    /**
     * Gets the value of the customField11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField11() {
        return customField11;
    }

    /**
     * Sets the value of the customField11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField11(String value) {
        this.customField11 = value;
    }

    /**
     * Gets the value of the customField12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField12() {
        return customField12;
    }

    /**
     * Sets the value of the customField12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField12(String value) {
        this.customField12 = value;
    }

    /**
     * Gets the value of the customField13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField13() {
        return customField13;
    }

    /**
     * Sets the value of the customField13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField13(String value) {
        this.customField13 = value;
    }

    /**
     * Gets the value of the customField14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField14() {
        return customField14;
    }

    /**
     * Sets the value of the customField14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField14(String value) {
        this.customField14 = value;
    }

    /**
     * Gets the value of the customField15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomField15() {
        return customField15;
    }

    /**
     * Sets the value of the customField15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomField15(String value) {
        this.customField15 = value;
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
     * Gets the value of the customActivityID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCustomActivityID() {
        return customActivityID;
    }

    /**
     * Sets the value of the customActivityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCustomActivityID(Integer value) {
        this.customActivityID = value;
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
     * Gets the value of the customActivityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomActivityName() {
        return customActivityName;
    }

    /**
     * Sets the value of the customActivityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomActivityName(String value) {
        this.customActivityName = value;
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
