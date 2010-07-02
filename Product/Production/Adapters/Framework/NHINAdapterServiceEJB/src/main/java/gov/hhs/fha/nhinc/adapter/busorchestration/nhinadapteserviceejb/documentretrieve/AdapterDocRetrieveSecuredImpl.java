/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentretrieve;

import gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.AdapterConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.ServiceHelper;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implement document retrieve - All documents should actually be in the dynamic document archive
 *
 * @author Jerry Goodnough
 */

/**
 *
 * @author mflynn02
 */
public class AdapterDocRetrieveSecuredImpl {
    private static AdapterDocRetrieveSecuredImpl singlton = null;
    private static Log log = LogFactory.getLog(
            AdapterDocRetrieveSecuredImpl.class);


    static
    {
        singlton = new AdapterDocRetrieveSecuredImpl();
    }

    /**
     * Provides public access to the singletion.
     *
     * @return the Singlton
     */
    public static AdapterDocRetrieveSecuredImpl getInstance()
    {
        return singlton;
    }

    private AdapterDocRetrieveSecuredImpl()
    {
    }
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, WebServiceContext context) {
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response;
        request.setRetrieveDocumentSetRequest(body);
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        request.setAssertion(assertion);
        response = respondingGatewayCrossGatewayRetrieve(request);
        return response;
    }
    /**
     * The core implementation of the DocumentRetrieve request.
     * The request is vectored to the Document Manager / Dynamic Document Archive
     * to retrieve the document.
     *
     * In additionall documents retrieved are marked to retention, since usage
     * of this interface is considered public publication.
     *
     * @param respondingGatewayCrossGatewayRetrieveRequest Request for the docuents to retrieve
     * @return requested documents if available.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        log.debug("Entering Document Retrieve");

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType result=null;
        String docMgrEndpoint=getDocMgrEndpoint();
        try
            {
            try
            { // Fetch the requested documents
                ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
                ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
                if (docMgrEndpoint != null && !docMgrEndpoint.isEmpty())
                {
                    //Use the BOS Endpoint
					gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, docMgrEndpoint);
                }
                log.debug("Calling Document Manager to retrieve from the Archive");
                result = port.documentManagerRetrieveDynamicDocument(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
                log.debug("Call to Document Manager to retrieve from the Archive complete");
            }
            catch (Exception ex)
            {
                log.error("Exception During DocumentManager Retrieve from Archivce",ex);
                throw ex;

            }

            if (result.getDocumentResponse().size()>0)
            {
                //In an ideal world the next call would happen in another thread or as
                //as result in JMS queue or the like. Another option would be be to
                //Have the document manager hanlde it as a side effect of the prior
                //call.

                try
                { // Ask all dynamic documents to be retained in the archive
                    ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
                    ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
                    if (docMgrEndpoint != null && !docMgrEndpoint.isEmpty())
                    {
                        //Use the BOS Endpoint
						gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, docMgrEndpoint);
                    }
                    log.debug("Call to Document Manager to Mark the Documents to be saved");

                    oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType results = port.documentManagerArchiveDynamicDocument(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
                    log.debug("Called to Document Manager to Mark the Documents to be saved");
                    //Ignore the result.
                }
                catch (Exception ex)
                {
                    log.error("Exception during Dynamic Document Retrieve - Mark to retention",ex);
                    //Question - If an exception here fatal? In the best possible world the request would be
                    //handled in a independant mamnner and not be considered blocking.
                    //For now if this fails, we should note it, and let the user get the requested
                    //document, since we assume it is clinically important.
                    //In full production it may be better to use the JMS method or at
                    //the very least insure that the document request is dumped to a monitored log
                }
            }
        }
        catch(Exception ex)
        {
            log.error("General Exception during Retrieve Documents call, details above",ex);
            throw new WebServiceException("Exception during DocumentRetrieve",ex);
        }
       log.debug("Document Retieve is Complete");

        return result;
    }

    /**
     * Get the DoumentManagerEndpoint
     * @return The Endpoint or null if not mapped
     */
    private String getDocMgrEndpoint()
    {
        return ServiceHelper.getEndpointFromBOS(AdapterConstants.DOCUMENT_MANAGER);
    }


}
