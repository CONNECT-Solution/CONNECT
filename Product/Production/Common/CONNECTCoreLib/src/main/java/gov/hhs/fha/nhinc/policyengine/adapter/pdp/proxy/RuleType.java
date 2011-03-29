/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Mastan.Ketha
 */
/**
 * <p>Java class for RuleType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Target" type="{}TargetType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RuleId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Effect" use="required" type="{}EffectType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleType", propOrder = {
    "description",
    "target"
})
public class RuleType {

    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Target")
    protected TargetType target;
    @XmlAttribute(name = "RuleId", required = true)
    protected String ruleId;
    @XmlAttribute(name = "Effect", required = true)
    protected EffectType effect;

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
     * Gets the value of the target property.
     *
     * @return
     *     possible object is
     *     {@link TargetType }
     *
     */
    public TargetType getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     *
     * @param value
     *     allowed object is
     *     {@link TargetType }
     *
     */
    public void setTarget(TargetType value) {
        this.target = value;
    }

    /**
     * Gets the value of the ruleId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * Sets the value of the ruleId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRuleId(String value) {
        this.ruleId = value;
    }

    /**
     * Gets the value of the effect property.
     *
     * @return
     *     possible object is
     *     {@link EffectType }
     *
     */
    public EffectType getEffect() {
        return effect;
    }

    /**
     * Sets the value of the effect property.
     *
     * @param value
     *     allowed object is
     *     {@link EffectType }
     *
     */
    public void setEffect(EffectType value) {
        this.effect = value;
    }
}
