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

import javax.xml.bind.annotation.XmlRegistry;

/**
 *
 * @author Mastan.Ketha
 */
/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the generated package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubjectType }
     *
     */
    public SubjectType createSubjectType() {
        return new SubjectType();
    }

    /**
     * Create an instance of {@link RuleType }
     *
     */
    public RuleType createRuleType() {
        return new RuleType();
    }

    /**
     * Create an instance of {@link AttributeDesignatorType }
     *
     */
    public AttributeDesignatorType createAttributeDesignatorType() {
        return new AttributeDesignatorType();
    }

    /**
     * Create an instance of {@link AttributeValueType }
     *
     */
    public AttributeValueType createAttributeValueType() {
        return new AttributeValueType();
    }

    /**
     * Create an instance of {@link SubjectsType }
     *
     */
    public SubjectsType createSubjectsType() {
        return new SubjectsType();
    }

    /**
     * Create an instance of {@link TargetType }
     *
     */
    public TargetType createTargetType() {
        return new TargetType();
    }

    /**
     * Create an instance of {@link SubjectMatchType }
     *
     */
    public SubjectMatchType createSubjectMatchType() {
        return new SubjectMatchType();
    }

    /**
     * Create an instance of {@link Policy }
     *
     */
    public Policy createPolicy() {
        return new Policy();
    }
}
