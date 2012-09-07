/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.ONExplicit;

/**
 *
 * @author kim
 */
public abstract class DocumentBuilder {

   protected ObjectFactory objectFactory = null;

   protected String orgOID = "";
   protected String orgName = "";

   protected String patientId = null;
   protected List<CdaTemplate> templates = null;   // templates for section
   protected DocumentType documentType = null;
   
   public static final String INFO_SOURCE_FILE = "infoSource";
   public static final String INFO_SOURCE_CITY = "City";
   public static final String INFO_SOURCE_STREET = "Street";
   public static final String INFO_SOURCE_STATE = "State";
   public static final String INFO_SOURCE_ZIP = "Zip";
   public static final String INFO_SOURCE_TELECOM = "Telecom";
   private Log log = null;
   
   public DocumentBuilder() {
      initialize();
   }

   public DocumentBuilder(List<CdaTemplate> templates) {
      initialize();
      this.templates = templates;
   }

   public DocumentBuilder(String id) {
      initialize();
      this.patientId = id;
   }

   private void initialize() {
      if (objectFactory == null) {
         objectFactory = new ObjectFactory();
      }

	  log = createLogger();
          orgOID = AssemblyConstants.ORGANIZATION_OID;
          orgName = AssemblyConstants.ORGANIZATION_NAME;

   }

	protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
	
   public String getPatientId() {
      return patientId;
   }

   public void setPatientId(String id) {
      this.patientId = id;
   }

   protected Date getCreatedDTM() {
      return Calendar.getInstance().getTime();
   }

   protected String createDocumentId() {
      return UUIDGenerator.generateRandomUUID();
   }

   protected II getOrganization() {
      II id = new II();
      id.setRoot(orgOID);
      return id;
   }

   public void setDocumentType(String docType) {
         documentType = DocumentType.getDocument(docType);
   }

   public DocumentType getDocumentType() {
      return documentType;
   }

   /*
	protected String getInfoSourceProperty(String sKey)
            throws PropertyAccessException {
        return PropertyAccessor.getProperty(INFO_SOURCE_FILE, sKey);
    }
*/	
	/*
	Using slightly different set up for getting the property from the property file.  Sending the property choice 
	from the calling object instead of 5 separate methods for each piece of information
	*/
/*	protected String getInfoSourceText(String contentChoice){
		String property = "";
		try {
			property = getInfoSourceProperty(contentChoice);
			log.debug("Retrieved from property file (" + INFO_SOURCE_FILE + ".properties) " + contentChoice + "='" + property + "')");
		} catch (PropertyAccessException ex) {
			log.warn("Error occurred reading retry attempts value from property file (" + INFO_SOURCE_FILE + ".properties).  Exception = " + ex.toString());
		}
		return property;
	}
*/	
   protected POCDMT000040Organization getRepresentedOrganization() {
      POCDMT000040Organization repOrg = new POCDMT000040Organization();
	  String infoSourceText = "";

      repOrg.getId().add(getOrganization());

      ONExplicit onName = objectFactory.createONExplicit();
      onName.getContent().add(orgName);
      repOrg.getName().add(onName);

      org.hl7.v3.ADExplicit addr = new org.hl7.v3.ADExplicit();

	  infoSourceText = "Replace this CITY value";
      org.hl7.v3.AdxpExplicitCity city = new org.hl7.v3.AdxpExplicitCity();
      city.setContent(infoSourceText);

	  infoSourceText = "Replace this STREET Value";
      org.hl7.v3.AdxpExplicitStreetAddressLine street = new org.hl7.v3.AdxpExplicitStreetAddressLine();
      street.setContent(infoSourceText);

	  infoSourceText = "Replace this STATE value";
      org.hl7.v3.AdxpExplicitState state = new org.hl7.v3.AdxpExplicitState();
      state.setContent(infoSourceText);

	  infoSourceText = "replace this ZIP value";
      org.hl7.v3.AdxpExplicitPostalCode zip = new org.hl7.v3.AdxpExplicitPostalCode();
      zip.setContent(infoSourceText);

      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

      addr.getContent().add(factory.createADExplicitStreetAddressLine(street));
      addr.getContent().add(factory.createADExplicitCity(city));
      addr.getContent().add(factory.createADExplicitState(state));
      addr.getContent().add(factory.createADExplicitPostalCode(zip));

      repOrg.getAddr().add(addr);

	  infoSourceText = "tel:+1-555-555-5555";
      org.hl7.v3.TELExplicit telecom = new org.hl7.v3.TELExplicit();
      telecom.setValue(infoSourceText);

      repOrg.getTelecom().add(telecom);

      return repOrg;
   }
}
