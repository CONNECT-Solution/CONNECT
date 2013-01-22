/**
 * 
 */
package gov.hhs.fha.nhinc.transform.audit;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author msw
 *
 */
public class PatientDiscoveryTransformsBase {

    protected static String RECEIVER_VALUE = "receiver";
    protected static String SENDER_VALUE = "sender";
    protected static final String LOCAL_HCID = "1.1";
    protected static final String REMOTE_HCID = "2.2";
    protected static final String NON_TRUSTWORTHY_VALUE = "non-trustworthy-value";
    
    /**
     * 
     */
    public PatientDiscoveryTransformsBase() {
        super();
    }

    protected PatientDiscoveryTransforms getPatientDiscoveryTransforms() {
        return new PatientDiscoveryTransforms() {
            @Override
            protected String getHCIDFromReceiver(PRPAIN201305UV02 requestMessage) {
                return RECEIVER_VALUE;
            }
            
            @Override
            protected String getHCIDFromSender(PRPAIN201305UV02 requestMessage) {
                return SENDER_VALUE;
            }
            
            @Override
            protected String getHCIDFromReceiver(PRPAIN201306UV02 requestMessage) {
                return RECEIVER_VALUE;
            }
            
            @Override
            protected String getHCIDFromSender(PRPAIN201306UV02 requestMessage) {
                return SENDER_VALUE;
            }
            
            @Override
            protected String getHCIDFromReceiver(MCCIIN000002UV01 requestMessage) {
                return RECEIVER_VALUE;
            }
            
            @Override
            protected String getHCIDFromSender(MCCIIN000002UV01 requestMessage) {
                return SENDER_VALUE;
            }
            
            @Override
            protected String getLocalHCID() {
                return LOCAL_HCID;
            }
        };
    }
}