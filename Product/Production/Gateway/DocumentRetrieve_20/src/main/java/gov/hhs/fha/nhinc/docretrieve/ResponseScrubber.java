/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * 
 * @author mweaver
 */
public class ResponseScrubber {

    public ResponseScrubber() {
    }

    public RetrieveDocumentSetResponseType Scrub(RetrieveDocumentSetResponseType resp) {
        for (RetrieveDocumentSetResponseType.DocumentResponse dr : resp.getDocumentResponse()) {
            dr.setNewDocumentUniqueId(null);
            dr.setNewRepositoryUniqueId(null);
        }
        return resp;
    }
}
