/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.util;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;

/**
 * @author achidamb
 * 
 */
public class DocRetrieveStatusUtil {

    public static String setResponseStatus(RetrieveDocumentSetResponseType from, RetrieveDocumentSetResponseType to) {

        String status = null;

        if (to.getRegistryResponse().getStatus() == null) {
            status = from.getRegistryResponse().getStatus();
        }

        else if (from.getRegistryResponse().getStatus().equalsIgnoreCase(to.getRegistryResponse().getStatus())) {
            status = from.getRegistryResponse().getStatus();
        }

        else if (!from.getRegistryResponse().getStatus().equals(to.getRegistryResponse().getStatus())) {
            status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS;
        }
        return status;

    }

}
