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
package gov.hhs.fha.nhinc.common.docmgr;

import ihe.iti.xds_b._2007.DocumentManagerPortType;
import ihe.iti.xds_b._2007.DocumentManagerService;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 *
 * @author kim
 */
public class DocumentManagerClient {

   private DocumentManagerService service = null;
   public static final String DOCUMENT_MANAGER_QNAME = "urn:ihe:iti:xds-b:2007";

   public DocumentManagerClient() {
   }

   public DocumentManagerClient(String endpoint) {
      initService(endpoint);
   }

   private void initService(String serviceEndpoint) {
      URL baseUrl;

      baseUrl = gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService.class.getResource(".");
      try {
         URL url = new URL(baseUrl, serviceEndpoint);
         service = new DocumentManagerService(url, new QName(DOCUMENT_MANAGER_QNAME, "DocumentManager_Service"));
      } catch (MalformedURLException e) {
         System.err.println("Failed to create URL for the wsdl Location: " + serviceEndpoint);
      }
   }

   public String generateUniqueId(String serviceEndpoint) {
      if (service == null) {
         initService(serviceEndpoint);
      }

      DocumentManagerPortType port = service.getDocumentManagerPortSoap();
      GenerateUniqueIdResponseType response = port.generateUniqueId(null);

      return response != null ? response.getUniqueId() : "";
   }
}
