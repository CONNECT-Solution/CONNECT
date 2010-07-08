/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author rayj
 */
public interface AdapterDocumentRegistryProxy {
    public AdhocQueryResponse queryForDocument(AdhocQueryRequest adhocQuery, AssertionType assertion, NhinTargetSystemType target) throws Exception ;
}
