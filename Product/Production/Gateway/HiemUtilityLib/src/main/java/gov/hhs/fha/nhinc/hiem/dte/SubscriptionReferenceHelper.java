/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
//import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import java.io.StringWriter;
import javax.xml.bind.Marshaller;
import org.w3._2005._08.addressing.EndpointReferenceType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3c.dom.Node;

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
