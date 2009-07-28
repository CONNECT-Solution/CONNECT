/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.dao;

import gov.hhs.fha.nhinc.patientcorrelation.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.model.QualifiedPatientIdentifier;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class CorrelatedIdentifiersDao {

    Log log = LogFactory.getLog(CorrelatedIdentifiersDao.class);

    public void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        Storer.addPatientCorrelation(correlatedIdentifers);
    }

    public List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier, List<String> includeOnlyAssigningAuthorities) {
        return Retriever.retrievePatientCorrelation(qualifiedPatientIdentifier, includeOnlyAssigningAuthorities);
    }

    public List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier) {
        return Retriever.retrievePatientCorrelation(qualifiedPatientIdentifier);
    }

    public void removePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        Storer.removePatientCorrelation(correlatedIdentifers);
    }
}
