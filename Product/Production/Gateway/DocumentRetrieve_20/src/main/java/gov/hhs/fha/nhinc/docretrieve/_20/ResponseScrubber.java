/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve._20;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * 
 * @author mweaver
 */
public class ResponseScrubber {

    private static ResponseScrubber INSTANCE = new ResponseScrubber();

    private ResponseScrubber() {
    }

    /**
     * Returns the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static ResponseScrubber getInstance() {
        return INSTANCE;
    }

    /**
     * Scrubs the RetrieveDocumentSetResponseType message to conform to DR 2.0 specs by removing unsupported dynamic
     * document ids.  This call has a side effect of actually modifying the passed in message.
     * 
     * @param resp The message to be scrubbed.
     */
    public void scrub(RetrieveDocumentSetResponseType resp) {
        for (RetrieveDocumentSetResponseType.DocumentResponse dr : resp.getDocumentResponse()) {
            dr.setNewDocumentUniqueId(null);
            dr.setNewRepositoryUniqueId(null);
        }
    }
}
