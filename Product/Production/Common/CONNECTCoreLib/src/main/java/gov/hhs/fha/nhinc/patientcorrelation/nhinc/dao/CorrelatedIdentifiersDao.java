/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public interface CorrelatedIdentifiersDao {


    public void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers);

    public List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier, List<String> includeOnlyAssigningAuthorities);

    public List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier);

    public void removePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers);
}
