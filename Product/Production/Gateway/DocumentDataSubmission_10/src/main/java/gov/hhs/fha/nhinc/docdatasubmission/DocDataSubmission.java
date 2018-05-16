/**
 *
 */
package gov.hhs.fha.nhinc.docdatasubmission;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayRegisterDocumentSetSecuredRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public interface DocDataSubmission {

    public RegistryResponseType registerDocumentSetB(RespondingGatewayRegisterDocumentSetSecuredRequestType body);
}
