/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxy;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
class ForwardChildModeSubscriptionHandler extends BaseSubscriptionHandler {

    @Override
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault {
        log.debug("In ForwardChildModeSubscriptionHandler.handleSubscribe");
        AssertionType assertion = null;
        NhinTargetSystemType target = null;

        log.info("initialize HiemSubscribeAdapterProxyObjectFactory");
        HiemSubscribeAdapterProxyObjectFactory adapterFactory = new HiemSubscribeAdapterProxyObjectFactory();
        log.info("initialize HIEM subscribe adapter proxy");
        HiemSubscribeAdapterProxy adapterProxy = adapterFactory.getHiemSubscribeAdapterProxy();

        try {
            log.info("begin invoke HIEM subscribe adapter proxy");
            Element subscribeResponseFromAdapter = adapterProxy.subscribe(subscribe, assertion, target);
            log.info("end invoke HIEM subscribe adapter proxy");
        } catch (Exception ex) {
            log.error("failed to forward subscribe to adapter", ex);
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        // Build subscription item
        log.debug("Calling createSubscriptionItem");
        HiemSubscriptionItem subscription = createSubscriptionItem(subscribe, "gateway", "nhin");

        // Store subscription
        log.debug("Calling storeSubscriptionItem");
        EndpointReferenceType subRef = storeSubscriptionItem(subscription);

        SubscribeResponse response = null;
        response = new SubscribeResponse();
        response.setSubscriptionReference(subRef);

        return response;
    }

//    /**
//     * Use reflection to set the subscription reference. The runtime parameter
//     * type of the setSubscriptionReference method of SubscriptionResponse is
//     * checked and the correct parameter type is created and the method is
//     * invoked with the correct type.
//     *
//     * This is necessary because the buildtime type and runtime type are
//     * different for the method called.
//     *
//     * @param response Subscription response method.
//     * @param subRef Subscription reference
//     */
//    @SuppressWarnings("unchecked")
//    private void setSubscriptionReference(SubscribeResponse response, EndpointReferenceType subRef) {
//        log.debug("In setSubscriptionReference");
//        if ((response != null) && (subRef != null)) {
//            try {
//                Method[] methods = response.getClass().getDeclaredMethods();
//                if (methods != null) {
//                    log.debug("Method count: " + methods.length);
//                    for (Method m : methods) {
//                        log.debug("Looking at method: " + m.getName());
//                        if (m.getName().equals("setSubscriptionReference")) {
//                            Class[] paramTypes = m.getParameterTypes();
//                            if (paramTypes != null) {
//                                log.debug("Parameter count: " + paramTypes.length);
//                                for (Class paramType : paramTypes) {
//                                    log.debug("Param type: " + paramType.getName());
//                                    if (paramType.isAssignableFrom(EndpointReferenceType.class)) {
//                                        log.debug("Param type is EndpointReferenceType");
//                                        Object[] params = {subRef};
//                                        log.debug("Invoking EndpointReferenceType param method");
//                                        m.invoke(response, params);
//                                        break;
//                                    } else if (paramType.isAssignableFrom(W3CEndpointReference.class)) {
//                                        log.debug("Param type is W3CEndpointReference");
//                                        Object[] params = {convertEndpointReferenceToW3cEndpointReference(subRef)};
//                                        log.debug("Invoking W3CEndpointReference param method");
//                                        m.invoke(response, params);
//                                        break;
//                                    }
//
//                                }
//                            } else {
//                                log.debug("Parameter types was null");
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    log.debug("Methods were null");
//                }
//            } catch (IllegalAccessException ex) {
//                log.error("IllegalAccessException encountered: " + ex.getMessage(), ex);
//            } catch (IllegalArgumentException ex) {
//                log.error("IllegalArgumentException encountered: " + ex.getMessage(), ex);
//            } catch (InvocationTargetException ex) {
//                log.error("InvocationTargetException encountered: " + ex.getMessage(), ex);
//            }
//        }
//    }
//
//    private EndpointReference convertEndpointReferenceToW3cEndpointReference(EndpointReferenceType epr) {
//        log.info("begin CreateSubscriptionReference");
//        W3CEndpointReference subRef = null;
//
//        if (epr != null) {
//            W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();
//
//            if (epr.getAddress() != null) {
//                log.info("subscriptionManagerUrl=" + epr.getAddress().getValue());
//                resultBuilder.address(epr.getAddress().getValue());
//            }
//            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//            Document doc = null;
//            try {
//                doc = docBuilderFactory.newDocumentBuilder().newDocument();
//            } catch (ParserConfigurationException ex) {
//                throw new RuntimeException(ex);
//            }
//            doc.setXmlStandalone(true);
//
//            if ((epr.getReferenceParameters() != null) &&
//                    (epr.getReferenceParameters().getAny() != null) &&
//                    (!epr.getReferenceParameters().getAny().isEmpty())) {
//                List<Object> refParams = epr.getReferenceParameters().getAny();
//                for (Object o : refParams) {
//                    log.debug("Processing a reference parameter");
//                    if (o instanceof Element) {
//                        Element refParam = (Element) o;
//                        resultBuilder.referenceParameter(refParam);
//                    } else {
//                        log.warn("Reference parameter was not of type Element - was " + o.getClass());
//                    }
//                }
//            } else {
//                log.warn("Reference parameters or ref param list was null or empty");
//            }
//
//            log.info("building.. resultBuilder.build()");
//            subRef = resultBuilder.build();
//        } else {
//            log.warn("The endpoint reference was null");
//        }
//
//        log.info("end CreateSubscriptionReference");
//        return subRef;
//    }
}
