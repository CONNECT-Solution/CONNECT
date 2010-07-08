/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

/**
 *
 * @author rayj
 */
public class AdapterDocumentRegistryProxyObjectFactory {
    public AdapterDocumentRegistryProxy getAdapterDocumentRegistryProxy() {
        //todo: hook into spring
        return new AdapterDocumentRegistryWebServiceProxy();
    }
}
