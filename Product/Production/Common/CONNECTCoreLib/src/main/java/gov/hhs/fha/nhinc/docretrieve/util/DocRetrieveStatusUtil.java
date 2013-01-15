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

    /**
     * Sets the response status (Success, Partial, Failure) of the to ResponseType based on the from ResponseType.
     * 
     * @param from the ResponseType to based the status from
     * @param to the ResponseType whose status is to be modified
     * @return a String containing the response set on the from ResponseType
     */
    public static String setResponseStatus(RetrieveDocumentSetResponseType from, RetrieveDocumentSetResponseType to) {

        String status = null;

        if (to.getRegistryResponse().getStatus() == null) {
            status = from.getRegistryResponse().getStatus();
        } else if (from.getRegistryResponse().getStatus().equalsIgnoreCase(to.getRegistryResponse().getStatus())) {
            status = from.getRegistryResponse().getStatus();
        } else if (!from.getRegistryResponse().getStatus().equals(to.getRegistryResponse().getStatus())) {
            status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS;
        }
        return status;

    }

    /**
     * Returns true if the passed in status string has a success value.
     * 
     * @param status String that contains the value to be checked
     * @return true if status string represents a success 
     */
    public static boolean isStatusSuccess(String status) {
        return DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS.equalsIgnoreCase(status);
    }

    /**
     * Returns true if the passed in status string has a failure value.
     * 
     * @param status String that contains the value to be checked
     * @return true if status string represents a failure or partial failure
     */
    public static boolean isStatusFailureOrPartialFailure(String status) {
        return !isStatusSuccess(status);
    }

}
