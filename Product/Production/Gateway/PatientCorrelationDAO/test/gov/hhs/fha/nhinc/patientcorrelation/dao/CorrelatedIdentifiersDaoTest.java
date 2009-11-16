/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.dao;

import gov.hhs.fha.nhinc.patientcorrelation.model.CorrelatedIdentifiers;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author svalluripalli
 */
public class CorrelatedIdentifiersDaoTest {

    public CorrelatedIdentifiersDaoTest() {
    }

     @Test
     public void testAddPatientCorrelation() {
        CorrelatedIdentifiersDao dao = new CorrelatedIdentifiersDao(){
            public void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
                
            }
        };

        CorrelatedIdentifiers ci = new CorrelatedIdentifiers();
        ci.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.198");
        ci.setCorrelatedPatientId("D123401");
        ci.setPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci.setPatientId("123456789");
        ci.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci);
        CorrelatedIdentifiers ci1 = new CorrelatedIdentifiers();
        ci1.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.192");
        ci1.setCorrelatedPatientId("1018");
        ci1.setPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci1.setPatientId("500000000");
        ci1.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci1);
        CorrelatedIdentifiers ci2 = new CorrelatedIdentifiers();
        ci2.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci2.setCorrelatedPatientId("500000000");
        ci2.setPatientAssigningAuthorityId("2.16.840.1.113883.3.192");
        ci2.setPatientId("1018");
        ci2.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci2);        CorrelatedIdentifiers ci3 = new CorrelatedIdentifiers();
        ci3.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci3.setCorrelatedPatientId("123456789");
        ci3.setPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci3.setPatientId("500000000");
        ci3.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci3);
        CorrelatedIdentifiers ci4 = new CorrelatedIdentifiers();
        ci4.setCorrelatedPatientAssigningAuthorityId("2.16.840.1.113883.3.200");
        ci4.setCorrelatedPatientId("500000000");
        ci4.setPatientAssigningAuthorityId("2.16.840.1.113883.4.1");
        ci4.setPatientId("123456789");
        ci4.setCorrelationExpirationDate(new Date());
        dao.addPatientCorrelation(ci4 );
    }
     
     
}
