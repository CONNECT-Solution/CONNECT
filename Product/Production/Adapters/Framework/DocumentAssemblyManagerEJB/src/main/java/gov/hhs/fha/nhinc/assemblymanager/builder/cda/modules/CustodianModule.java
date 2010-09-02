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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.POCDMT000040AssignedCustodian;
import org.hl7.v3.POCDMT000040Custodian;
import org.hl7.v3.POCDMT000040CustodianOrganization;

/**
 *
 * @author kim
 */
public class CustodianModule extends DocumentBuilder {

   public CustodianModule() {
   }

   public POCDMT000040Custodian build() throws DocumentBuilderException {
      POCDMT000040Custodian custodian = new POCDMT000040Custodian();

      POCDMT000040AssignedCustodian assignedCustodian = new POCDMT000040AssignedCustodian();
      assignedCustodian.setRepresentedCustodianOrganization(createCustodianOrganization());
      custodian.setAssignedCustodian(assignedCustodian);

      return custodian;
   }

   private POCDMT000040CustodianOrganization createCustodianOrganization() {
      POCDMT000040CustodianOrganization custodianOrg = new POCDMT000040CustodianOrganization();

      custodianOrg.getId().add(getOrganization());
      ONExplicit oOrgName = objectFactory.createONExplicit();
      oOrgName.getContent().add(orgName);
      custodianOrg.setName(oOrgName);

      return custodianOrg;
   }
}
