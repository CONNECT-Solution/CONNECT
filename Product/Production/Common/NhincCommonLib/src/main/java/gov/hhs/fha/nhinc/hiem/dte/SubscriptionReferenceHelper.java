/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

//import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author rayj
 */
public class SubscriptionReferenceHelper {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionReferenceHelper.class);

    /**
     * @deprecated
     * @param subscribeResponseXml
     * @return
     */

    public SubscribeResponse createSubscribeResponseFromXml(Element subscribeResponseXml) {
        SubscribeResponse subscribeResponse = new SubscribeResponse();

        Element endpointReferenceXml = null;
        endpointReferenceXml = XmlUtility.getSingleChildElement(subscribeResponseXml, Namespaces.WSNT, "SubscriptionReference");

        EndpointReferenceHelper endpointReferenceHelper = new EndpointReferenceHelper();
        EndpointReferenceType subscriptionReference = endpointReferenceHelper.createEndpointReference(endpointReferenceXml);
        subscribeResponse.setSubscriptionReference(subscriptionReference);

        //todo: handle "CurrentTime"
        //todo: handle "TerminationTime"

        return subscribeResponse;
    }
}
