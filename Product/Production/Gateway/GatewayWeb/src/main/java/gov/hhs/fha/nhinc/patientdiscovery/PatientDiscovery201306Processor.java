/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7ReceiverTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class PatientDiscovery201306Processor {
    private static Log log = LogFactory.getLog(PatientDiscovery201306Processor.class);


    public PRPAIN201306UV02 createNewRequest(PRPAIN201306UV02 request, String targetCommunityId) {
        PRPAIN201306UV02 newRequest = new PRPAIN201306UV02();
        newRequest = request;

        if (request != null &&
                NullChecker.isNotNullish(targetCommunityId)) {
            //the new request will have the target community as the only receiver
            newRequest.getReceiver().clear();
            MCCIMT000300UV01Receiver oNewReceiver = HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(targetCommunityId);
            newRequest.getReceiver().add(oNewReceiver);
            log.debug("Created a new request for target communityId: " + targetCommunityId);
        }
        else {
            log.error("A null input paramter was passed to the method: createNewRequest in class: PatientDiscovery201305Processor");
            return null;
        }

        return newRequest;
    }

}
