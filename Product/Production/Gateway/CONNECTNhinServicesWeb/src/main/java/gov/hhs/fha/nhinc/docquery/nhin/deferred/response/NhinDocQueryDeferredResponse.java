/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhin.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "RespondingGateway_QueryDeferredResponse_Service", portName = "RespondingGateway_QueryDeferredResponse_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.RespondingGatewayQueryDeferredResponsePortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/NhinDocQueryDeferredResponse/NhinDocQueryDeferredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhinDocQueryDeferredResponse {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse body) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        return new NhinDocQueryDeferredResponseImpl().respondingGatewayCrossGatewayQuery(body, assertion);
    }

}
