/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentrepository.proxy;

/**
 *
 * @author rayj
 */
public class AdapterDocumentRepositoryProxyObjectFactory {
    public AdapterDocumentRepositoryProxy getAdapterDocumentRepositoryProxy() {
        //todo: hook into spring
        return new AdapterDocumentRepositoryWebServiceProxy();
    }
}
