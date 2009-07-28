
package com.targetprocess.integration.project;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ReleaseDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReleaseDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="ReleaseID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TagsString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ModifyDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="LastCommentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="NumericPriority" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
 *         &lt;element name="LastCommentUserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OwnerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="LastEditorID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="EntityTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReleaseDTO", propOrder = {
    "releaseID",
    "name",
    "tagsString",
    "description",
    "startDate",
    "endDate",
    "createDate",
    "modifyDate",
    "lastCommentDate",
    "numericPriority",
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
    "lastCommentUserID",
    "ownerID",
    "lastEditorID",
    "projectID",
    "entityTypeName",
    "projectName"
})
public class ReleaseDTO
    extends DataTransferObject
{

    @XmlElement(name = "ReleaseID", required = true, type = Integer.class, nillable = true)
    protected Integer releaseID;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "TagsString")
    protected String tagsString;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "StartDate", required = true, nillable = true)
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate", required = true, nillable = true)
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "CreateDate", required = true, nillable = true)
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "ModifyDate", required = true, nillable = true)
    protected XMLGregorianCalendar modifyDate;
    @XmlElement(name = "LastCommentDate", required = true, nillable = true)
    protected XMLGregorianCalendar lastCommentDate;
    @XmlElement(name = "NumericPriority", required = true, type = Float.class, nillable = true)
    protected Float numericPriority;
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
    @XmlElement(name = "LastCommentUserID", required = true, type = Integer.class, nillable = true)
    protected Integer lastCommentUserID;
    @XmlElement(name = "OwnerID", required = true, type = Integer.class, nillable = true)
    protected Integer ownerID;
    @XmlElement(name = "LastEditorID", required = true, type = Integer.class, nillable = true)
    protected Integer lastEditorID;
    @XmlElement(name = "ProjectID", required = true, type = Integer.class, nillable = true)
    protected Integer projectID;
    @XmlElement(name = "EntityTypeName")
    protected String entityTypeName;
    @XmlElement(name = "ProjectName")
    protected String projectName;

    /**
     * Gets the value of the releaseID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReleaseID() {
        return releaseID;
    }

    /**
     * Sets the value of the releaseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReleaseID(Integer value) {
        this.releaseID = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the tagsString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTagsString() {
        return tagsString;
    }

    /**
     * Sets the value of the tagsString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTagsString(String value) {
        this.tagsString = value;
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
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the modifyDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModifyDate() {
        return modifyDate;
    }

    /**
     * Sets the value of the modifyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModifyDate(XMLGregorianCalendar value) {
        this.modifyDate = value;
    }

    /**
     * Gets the value of the lastCommentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastCommentDate() {
        return lastCommentDate;
    }

    /**
     * Sets the value of the lastCommentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastCommentDate(XMLGregorianCalendar value) {
        this.lastCommentDate = value;
    }

    /**
     * Gets the value of the numericPriority property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getNumericPriority() {
        return numericPriority;
    }

    /**
     * Sets the value of the numericPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setNumericPriority(Float value) {
        this.numericPriority = value;
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
     * Gets the value of the lastCommentUserID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLastCommentUserID() {
        return lastCommentUserID;
    }

    /**
     * Sets the value of the lastCommentUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLastCommentUserID(Integer value) {
        this.lastCommentUserID = value;
    }

    /**
     * Gets the value of the ownerID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the value of the ownerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwnerID(Integer value) {
        this.ownerID = value;
    }

    /**
     * Gets the value of the lastEditorID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLastEditorID() {
        return lastEditorID;
    }

    /**
     * Sets the value of the lastEditorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLastEditorID(Integer value) {
        this.lastEditorID = value;
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
     * Gets the value of the entityTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityTypeName() {
        return entityTypeName;
    }

    /**
     * Sets the value of the entityTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityTypeName(String value) {
        this.entityTypeName = value;
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

}
