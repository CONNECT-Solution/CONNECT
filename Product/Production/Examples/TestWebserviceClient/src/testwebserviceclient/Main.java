/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testwebserviceclient;

/**
 *
 * @author svalluripalli
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        try { // Call Web Service Operation
            gov.hhs.fha.nhinc.adaptermpi.AdapterMpiService service = new gov.hhs.fha.nhinc.adaptermpi.AdapterMpiService();
            gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType port = service.getAdapterMpiPort();
            // TODO initialize WS operation arguments here
            org.hl7.v3.PIXConsumerPRPAIN201305UVRequestType findCandidatesRequest = new org.hl7.v3.PIXConsumerPRPAIN201305UVRequestType();
            // TODO process result here
            org.hl7.v3.PRPAIN201306UV result = port.findCandidates(findCandidatesRequest);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }


    }

}
