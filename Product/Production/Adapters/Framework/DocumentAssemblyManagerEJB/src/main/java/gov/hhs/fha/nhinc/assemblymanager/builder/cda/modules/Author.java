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

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import org.hl7.v3.POCDMT000040AssignedAuthor;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040AuthoringDevice;
import org.hl7.v3.SCExplicit;
import org.hl7.v3.TSExplicit;

/**
 *  This module includes information about the author or creator of the information
 *  contained within the exchange.
 *
 *  <author>
 *    <time value="20081216170000+0500"/>
 *		<assignedAuthor>
 *			<id root="[organization OID]"/>
 *       <assignedAuthoringDevice>
 *         <softwareName>[name of software system]</softwareName>
 *       </assignedAuthoringDevice>*
 *			<representedOrganization>
 *				<id root="<organization OID>"/>
 *				<name>[organization name]</name>
 *			</representedOrganization>
 *		</assignedEntity>
 *	</informant>
 * 
 * @author kim
 */
public class Author extends DocumentBuilder {

   public Author() {
   }

   public POCDMT000040Author build() throws DocumentBuilderException {
      POCDMT000040Author author = new POCDMT000040Author();

      try {
         // Author.time: Signifies the time at which this information was created.
         // Will be the same as ClinicalDocument.effectiveTime and will be set when
         // response is formulated..
         TSExplicit authorTime = new TSExplicit();
         authorTime.setValue("000000000000000-0000");
         //authorTime.setValue(DateUtil.convertToCDATime(getCreatedDTM()));
         author.setTime(authorTime);

         // Author.assignedAuthor: The name of the system that created the information content
         author.setAssignedAuthor(createAssignedAuthor());
         return author;
      } catch (Exception e) {
         throw new DocumentBuilderException(e.getMessage());
      }
   }

   private POCDMT000040AssignedAuthor createAssignedAuthor() {
      POCDMT000040AssignedAuthor assignedAuthor = new POCDMT000040AssignedAuthor();

      assignedAuthor.getId().add(this.getOrganization());
      assignedAuthor.setAssignedAuthoringDevice(createAuthoringDevice());
      assignedAuthor.setRepresentedOrganization(getRepresentedOrganization());

      return assignedAuthor;
   }

   private POCDMT000040AuthoringDevice createAuthoringDevice() {
      POCDMT000040AuthoringDevice device = new POCDMT000040AuthoringDevice();

      SCExplicit softwareName = new SCExplicit();
      softwareName.getContent().add(PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_SYSTEM, true));
      device.setSoftwareName(softwareName);

      return device;
   }
}
