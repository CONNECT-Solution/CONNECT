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
package gov.hhs.fha.nhinc.assemblymanager;

import gov.hhs.fha.nhinc.assemblymanager.builder.C62DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.C32DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.utils.JAXBUtil;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;

/**
 *
 * @author kim
 */
public class AssemblyManager {


   private static Log log = LogFactory.getLog(AssemblyManager.class);

   private String documentType = "";
   
   public List<POCDMT000040ClinicalDocument> getDocuments(String docType, String patientId) throws InvalidTypeException, InvalidIdentifierException, DocumentBuilderException {

      List<POCDMT000040ClinicalDocument> clinDocsList = null;
      String displayName = null;

      // check for valid document type
      if (docType == null) {
         throw new InvalidTypeException("Must specify a document type.");
      }
      if (!DocumentType.isValidDocumentType(docType)) {
         log.debug("Requested document type \"" + docType + "\" is not supported!");
         throw new InvalidTypeException("Requested document type \"" + docType + "\" is not supported.");
      }

      documentType = docType;

      // check for valid patient identifier parameter
      if (patientId == null || patientId.length() < 1) {
         log.debug("Patient identifier \"" + patientId + "\" is invalid!");
         throw new InvalidIdentifierException("Identifier is invalid.");
      }

      log.debug("Requesting document of type=" + docType + " for patient=" + patientId);

      // get a document builder
      if (docType.equalsIgnoreCase(AssemblyConstants.C32_CLASS_CODE))
      {
          C32DocumentBuilder c32Builder = AssemblerFactory.C32Builder(docType);
          c32Builder.setPatientId(patientId);
          clinDocsList = c32Builder.build();
          if(clinDocsList.size() == 0)
          {
              clinDocsList = null;
          }
          displayName = c32Builder.getDocumentType().getDisplayName();
      }
      else if (docType.equalsIgnoreCase(AssemblyConstants.C62_CLASS_CODE))
      {
          C62DocumentBuilder c62Builder = AssemblerFactory.c62Builder(docType);
          c62Builder.setPatientId(patientId);
          clinDocsList = c62Builder.build();
          if(clinDocsList.size() == 0)
          {
              clinDocsList = null;
          }
      }
      else
      {
          log.debug("Document of type: " + docType + " not supported...");
      }
      return clinDocsList;
   }

   public DocumentType getDocumentType(String docType) {
      DocumentType d = DocumentType.getDocument(docType);
      return d;
   }

   /**
    * Returns a serialized copy of this POCDMT000040ClinicalDocument object.
    * @param pObject
    * @return
    * @throws javax.xml.bind.JAXBException
    */
   public String serializeCDAContentToXMLString(POCDMT000040ClinicalDocument pObject) throws JAXBException {

      if (pObject == null) {
         throw new JAXBException("POCDMT000040ClinicalDocument object cannot be NULL!");
      }

      //JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      JAXBContext jc = JAXBUtil.getInstance().getJAXBContextOrg();
      java.io.StringWriter sw = new StringWriter();
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
      marshaller.marshal(factory.createClinicalDocument(pObject), sw);

      String clinDocString = sw.toString();

      // add stylesheet reference:
      //   <?xml-stylesheet type="text/xsl" href="CCD.xsl"?>
      if(!documentType.equalsIgnoreCase(AssemblyConstants.C62_CLASS_CODE))
      {
        String replacementString =
                  "<?xml-stylesheet type=\"text/xsl\" href=\"" + getStyleSheet() + "\"?>" +
                  System.getProperty("line.separator") + "<ClinicalDocument";

        clinDocString = clinDocString.replaceFirst("<ClinicalDocument", replacementString);
      }

      // remove empty xmlns="" which is an issue for stylesheet
      clinDocString = clinDocString.replaceAll("xmlns=\"\"", "");

      // quick fix for getting the <content> tag correctly shown in XML
      clinDocString = clinDocString.replaceAll("&lt;content", "<content");
      clinDocString = clinDocString.replaceAll("&lt;/content&gt;", "</content>");
      clinDocString = clinDocString.replaceAll("\"&gt;", "\">");

      // quick fix for getting the <paragraph> tag correctly shown in XML
      clinDocString = clinDocString.replaceAll("&lt;paragraph", "<paragraph");
      clinDocString = clinDocString.replaceAll("&lt;/paragraph&gt;", "</paragraph>");
      clinDocString = clinDocString.replaceAll("\"&gt;", "\">");

      return clinDocString;
   }

   public byte[] serializeCDAContentToXMLBytes(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      String xmlString = serializeCDAContentToXMLString(pObject);
      return xmlString.getBytes();
   }

   private String getStyleSheet() {

      if (documentType.equalsIgnoreCase(AssemblyConstants.C32_CLASS_CODE)) {
         return AssemblyConstants.C32_STYLESHEET;
      } else {
         log.error("No stylesheet defined for document type \"" + documentType + "\"");
      }

      return "CCD.xsl";
   }
}
