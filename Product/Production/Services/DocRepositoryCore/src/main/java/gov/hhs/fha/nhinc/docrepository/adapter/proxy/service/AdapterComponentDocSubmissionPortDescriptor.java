/**
 * 
 */
package gov.hhs.fha.nhinc.docrepository.adapter.proxy.service;

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;

/**
 * @author achidambaram
 *
 */
public class AdapterComponentDocSubmissionPortDescriptor extends
 SOAP12ServicePortDescriptor<DocumentRepositoryPortType> {
    
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<DocumentRepositoryPortType> getPortClass() {
        return DocumentRepositoryPortType.class;
    }

}
