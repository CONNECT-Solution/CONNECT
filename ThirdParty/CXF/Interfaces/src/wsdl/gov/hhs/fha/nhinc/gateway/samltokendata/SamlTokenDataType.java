
package gov.hhs.fha.nhinc.gateway.samltokendata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SamlTokenDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SamlTokenDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resource" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="purposeOfUseRoleCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="purposeOfUseCodeSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="purposeOfUseCodeSystemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="purposeOfUseDisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userMiddleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userOrganization" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userRoleCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userRoleCodeSystem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userRoleCodeSystemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userRoleCodeDisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contentReference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SamlTokenDataType", propOrder = {
    "action",
    "resource",
    "purposeOfUseRoleCode",
    "purposeOfUseCodeSystem",
    "purposeOfUseCodeSystemName",
    "purposeOfUseDisplayName",
    "userFirstName",
    "userMiddleName",
    "userLastName",
    "userName",
    "userOrganization",
    "userRoleCode",
    "userRoleCodeSystem",
    "userRoleCodeSystemName",
    "userRoleCodeDisplayName",
    "expirationDate",
    "signDate",
    "contentReference",
    "content"
})
public class SamlTokenDataType {

    @XmlElement(required = true)
    protected String action;
    @XmlElement(required = true)
    protected String resource;
    @XmlElement(required = true)
    protected String purposeOfUseRoleCode;
    @XmlElement(required = true)
    protected String purposeOfUseCodeSystem;
    @XmlElement(required = true)
    protected String purposeOfUseCodeSystemName;
    @XmlElement(required = true)
    protected String purposeOfUseDisplayName;
    @XmlElement(required = true)
    protected String userFirstName;
    @XmlElement(required = true)
    protected String userMiddleName;
    @XmlElement(required = true)
    protected String userLastName;
    @XmlElement(required = true)
    protected String userName;
    @XmlElement(required = true)
    protected String userOrganization;
    @XmlElement(required = true)
    protected String userRoleCode;
    @XmlElement(required = true)
    protected String userRoleCodeSystem;
    @XmlElement(required = true)
    protected String userRoleCodeSystemName;
    @XmlElement(required = true)
    protected String userRoleCodeDisplayName;
    @XmlElement(required = true)
    protected String expirationDate;
    @XmlElement(required = true)
    protected String signDate;
    @XmlElement(required = true)
    protected String contentReference;
    @XmlElement(required = true)
    protected byte[] content;

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the resource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the value of the resource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResource(String value) {
        this.resource = value;
    }

    /**
     * Gets the value of the purposeOfUseRoleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeOfUseRoleCode() {
        return purposeOfUseRoleCode;
    }

    /**
     * Sets the value of the purposeOfUseRoleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeOfUseRoleCode(String value) {
        this.purposeOfUseRoleCode = value;
    }

    /**
     * Gets the value of the purposeOfUseCodeSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeOfUseCodeSystem() {
        return purposeOfUseCodeSystem;
    }

    /**
     * Sets the value of the purposeOfUseCodeSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeOfUseCodeSystem(String value) {
        this.purposeOfUseCodeSystem = value;
    }

    /**
     * Gets the value of the purposeOfUseCodeSystemName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeOfUseCodeSystemName() {
        return purposeOfUseCodeSystemName;
    }

    /**
     * Sets the value of the purposeOfUseCodeSystemName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeOfUseCodeSystemName(String value) {
        this.purposeOfUseCodeSystemName = value;
    }

    /**
     * Gets the value of the purposeOfUseDisplayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeOfUseDisplayName() {
        return purposeOfUseDisplayName;
    }

    /**
     * Sets the value of the purposeOfUseDisplayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeOfUseDisplayName(String value) {
        this.purposeOfUseDisplayName = value;
    }

    /**
     * Gets the value of the userFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * Sets the value of the userFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserFirstName(String value) {
        this.userFirstName = value;
    }

    /**
     * Gets the value of the userMiddleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserMiddleName() {
        return userMiddleName;
    }

    /**
     * Sets the value of the userMiddleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserMiddleName(String value) {
        this.userMiddleName = value;
    }

    /**
     * Gets the value of the userLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * Sets the value of the userLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserLastName(String value) {
        this.userLastName = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserOrganization() {
        return userOrganization;
    }

    /**
     * Sets the value of the userOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserOrganization(String value) {
        this.userOrganization = value;
    }

    /**
     * Gets the value of the userRoleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRoleCode() {
        return userRoleCode;
    }

    /**
     * Sets the value of the userRoleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRoleCode(String value) {
        this.userRoleCode = value;
    }

    /**
     * Gets the value of the userRoleCodeSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRoleCodeSystem() {
        return userRoleCodeSystem;
    }

    /**
     * Sets the value of the userRoleCodeSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRoleCodeSystem(String value) {
        this.userRoleCodeSystem = value;
    }

    /**
     * Gets the value of the userRoleCodeSystemName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRoleCodeSystemName() {
        return userRoleCodeSystemName;
    }

    /**
     * Sets the value of the userRoleCodeSystemName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRoleCodeSystemName(String value) {
        this.userRoleCodeSystemName = value;
    }

    /**
     * Gets the value of the userRoleCodeDisplayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRoleCodeDisplayName() {
        return userRoleCodeDisplayName;
    }

    /**
     * Sets the value of the userRoleCodeDisplayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRoleCodeDisplayName(String value) {
        this.userRoleCodeDisplayName = value;
    }

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the signDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignDate() {
        return signDate;
    }

    /**
     * Sets the value of the signDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignDate(String value) {
        this.signDate = value;
    }

    /**
     * Gets the value of the contentReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentReference() {
        return contentReference;
    }

    /**
     * Sets the value of the contentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentReference(String value) {
        this.contentReference = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContent(byte[] value) {
        this.content = value;
    }

}
