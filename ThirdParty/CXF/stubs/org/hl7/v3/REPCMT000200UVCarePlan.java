
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for REPC_MT000200UV.CarePlan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000200UV.CarePlan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *         &lt;element name="title" type="{urn:hl7-org:v3}ST" minOccurs="0"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT000200UV.Performer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT000200UV.Author" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT000200UV.DataEnterer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT000200UV.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="participant" type="{urn:hl7-org:v3}REPC_MT000200UV.Participant" minOccurs="0"/>
 *         &lt;element name="finalGoal" type="{urn:hl7-org:v3}REPC_MT000200UV.FinalGoal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="definition" type="{urn:hl7-org:v3}REPC_MT000200UV.Definition1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reason" type="{urn:hl7-org:v3}REPC_MT000200UV.Reason2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component1" type="{urn:hl7-org:v3}REPC_MT000200UV.Component" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component2" type="{urn:hl7-org:v3}REPC_MT000200UV.Component10" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component3" type="{urn:hl7-org:v3}REPC_MT000200UV.Component7" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}REPC_MT000200UV.Subject2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}REPC_MT000200UV.Subject" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassCareProvision" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}ActMoodIntent" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000200UV.CarePlan", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "title",
    "text",
    "statusCode",
    "effectiveTime",
    "performer",
    "author",
    "dataEnterer",
    "verifier",
    "participant",
    "finalGoal",
    "definition",
    "reason",
    "component1",
    "component2",
    "component3",
    "subjectOf1",
    "subjectOf2"
})
public class REPCMT000200UVCarePlan {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CD code;
    protected ST title;
    protected EDExplicit text;
    protected CS statusCode;
    protected IVLTSExplicit effectiveTime;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVPerformer> performer;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVAuthor> author;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVDataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVVerifier> verifier;
    @XmlElementRef(name = "participant", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVParticipant> participant;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVFinalGoal> finalGoal;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVDefinition1> definition;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVReason2> reason;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVComponent> component1;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVComponent10> component2;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVComponent7> component3;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVSubject2> subjectOf1;
    @XmlElement(nillable = true)
    protected List<REPCMT000200UVSubject> subjectOf2;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected ActClassCareProvision classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected String moodCode;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getId() {
        if (id == null) {
            id = new ArrayList<II>();
        }
        return this.id;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CD }
     *     
     */
    public CD getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CD }
     *     
     */
    public void setCode(CD value) {
        this.code = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link ST }
     *     
     */
    public ST getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link ST }
     *     
     */
    public void setTitle(ST value) {
        this.title = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setText(EDExplicit value) {
        this.text = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setStatusCode(CS value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the effectiveTime property.
     * 
     * @return
     *     possible object is
     *     {@link IVLTSExplicit }
     *     
     */
    public IVLTSExplicit getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * Sets the value of the effectiveTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLTSExplicit }
     *     
     */
    public void setEffectiveTime(IVLTSExplicit value) {
        this.effectiveTime = value;
    }

    /**
     * Gets the value of the performer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVPerformer }
     * 
     * 
     */
    public List<REPCMT000200UVPerformer> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT000200UVPerformer>();
        }
        return this.performer;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVAuthor }
     * 
     * 
     */
    public List<REPCMT000200UVAuthor> getAuthor() {
        if (author == null) {
            author = new ArrayList<REPCMT000200UVAuthor>();
        }
        return this.author;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataEnterer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataEnterer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVDataEnterer }
     * 
     * 
     */
    public List<REPCMT000200UVDataEnterer> getDataEnterer() {
        if (dataEnterer == null) {
            dataEnterer = new ArrayList<REPCMT000200UVDataEnterer>();
        }
        return this.dataEnterer;
    }

    /**
     * Gets the value of the verifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVerifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVVerifier }
     * 
     * 
     */
    public List<REPCMT000200UVVerifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT000200UVVerifier>();
        }
        return this.verifier;
    }

    /**
     * Gets the value of the participant property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVParticipant }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVParticipant> getParticipant() {
        return participant;
    }

    /**
     * Sets the value of the participant property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVParticipant }{@code >}
     *     
     */
    public void setParticipant(JAXBElement<REPCMT000200UVParticipant> value) {
        this.participant = value;
    }

    /**
     * Gets the value of the finalGoal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the finalGoal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinalGoal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVFinalGoal }
     * 
     * 
     */
    public List<REPCMT000200UVFinalGoal> getFinalGoal() {
        if (finalGoal == null) {
            finalGoal = new ArrayList<REPCMT000200UVFinalGoal>();
        }
        return this.finalGoal;
    }

    /**
     * Gets the value of the definition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the definition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVDefinition1 }
     * 
     * 
     */
    public List<REPCMT000200UVDefinition1> getDefinition() {
        if (definition == null) {
            definition = new ArrayList<REPCMT000200UVDefinition1>();
        }
        return this.definition;
    }

    /**
     * Gets the value of the reason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVReason2 }
     * 
     * 
     */
    public List<REPCMT000200UVReason2> getReason() {
        if (reason == null) {
            reason = new ArrayList<REPCMT000200UVReason2>();
        }
        return this.reason;
    }

    /**
     * Gets the value of the component1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the component1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponent1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVComponent }
     * 
     * 
     */
    public List<REPCMT000200UVComponent> getComponent1() {
        if (component1 == null) {
            component1 = new ArrayList<REPCMT000200UVComponent>();
        }
        return this.component1;
    }

    /**
     * Gets the value of the component2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the component2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponent2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVComponent10 }
     * 
     * 
     */
    public List<REPCMT000200UVComponent10> getComponent2() {
        if (component2 == null) {
            component2 = new ArrayList<REPCMT000200UVComponent10>();
        }
        return this.component2;
    }

    /**
     * Gets the value of the component3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the component3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponent3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVComponent7 }
     * 
     * 
     */
    public List<REPCMT000200UVComponent7> getComponent3() {
        if (component3 == null) {
            component3 = new ArrayList<REPCMT000200UVComponent7>();
        }
        return this.component3;
    }

    /**
     * Gets the value of the subjectOf1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVSubject2 }
     * 
     * 
     */
    public List<REPCMT000200UVSubject2> getSubjectOf1() {
        if (subjectOf1 == null) {
            subjectOf1 = new ArrayList<REPCMT000200UVSubject2>();
        }
        return this.subjectOf1;
    }

    /**
     * Gets the value of the subjectOf2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000200UVSubject }
     * 
     * 
     */
    public List<REPCMT000200UVSubject> getSubjectOf2() {
        if (subjectOf2 == null) {
            subjectOf2 = new ArrayList<REPCMT000200UVSubject>();
        }
        return this.subjectOf2;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link ActClassCareProvision }
     *     
     */
    public ActClassCareProvision getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActClassCareProvision }
     *     
     */
    public void setClassCode(ActClassCareProvision value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoodCode() {
        return moodCode;
    }

    /**
     * Sets the value of the moodCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoodCode(String value) {
        this.moodCode = value;
    }

}
