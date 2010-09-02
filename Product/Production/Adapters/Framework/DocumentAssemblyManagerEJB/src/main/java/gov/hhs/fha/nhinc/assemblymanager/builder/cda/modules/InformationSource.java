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
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040Informant12;

/**
 *  This module This includes information about the author or creator of the information
 *  contained within the exchange.
 *
 *  <informant>
 *		<assignedEntity>
 *			<id root="[organization OID]"/>
 *			<representedOrganization>
 *				<id root="<organization OID>"/>
 *				<name>[organization name]</name>
 *			</representedOrganization>
 *		</assignedEntity>
 *	</informant>
 * 
 * @author kim
 */
public class InformationSource extends DocumentBuilder {

   public InformationSource() {
      super();
   }

   public POCDMT000040Informant12 build() throws DocumentBuilderException {
      POCDMT000040Informant12 informant = new POCDMT000040Informant12();
      POCDMT000040AssignedEntity assignedEntity = new POCDMT000040AssignedEntity();

      try {
         assignedEntity.getId().add(getOrganization());
         assignedEntity.setRepresentedOrganization(getRepresentedOrganization());
         informant.setAssignedEntity(assignedEntity);
         return informant;
      } catch (Exception e) {
         throw new DocumentBuilderException(e.getMessage());
      }
   }
}
