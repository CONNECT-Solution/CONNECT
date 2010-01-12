/*
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
