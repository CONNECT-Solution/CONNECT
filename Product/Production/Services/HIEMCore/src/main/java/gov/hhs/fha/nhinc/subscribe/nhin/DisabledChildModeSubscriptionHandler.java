/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.subscribe.nhin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;

import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;

/**
 *
 *
 * @author Neil Webb
 */
class DisabledChildModeSubscriptionHandler extends BaseSubscriptionHandler {
    private static final Logger LOG = Logger.getLogger(DisabledChildModeSubscriptionHandler.class);
	
	@Override
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault {
        LOG.debug("In DisabledChildModeSubscriptionHandler.handleSubscribe");
        SubscribeResponse response = null;

        // Build subscription item
        LOG.debug("Calling createSubscriptionItem");
        HiemSubscriptionItem subscription = createSubscriptionItem(subscribe, "gateway", "nhin");

        // Store subscription
        LOG.debug("Calling storeSubscriptionItem");
        EndpointReferenceType subRef = storeSubscriptionItem(subscription);

        response = new SubscribeResponse();

        // response.setSubscriptionReference(subRef);
        // response.setSubscriptionReference((W3CEndpointReference)
        // convertEndpointReferenceToW3cEndpointReference(subRef));
        setSubscriptionReference(response, subRef);
        return response;
    }

    /**
     * Use reflection to set the subscription reference. The runtime parameter type of the setSubscriptionReference
     * method of SubscriptionResponse is checked and the correct parameter type is created and the method is invoked
     * with the correct type.
     *
     * This is necessary because the buildtime type and runtime type are different for the method called.
     *
     * @param response Subscription response method.
     * @param subRef Subscription reference
     */
    @SuppressWarnings("unchecked")
    private void setSubscriptionReference(SubscribeResponse response, EndpointReferenceType subRef) {
        LOG.debug("In setSubscriptionReference");
        if ((response != null) && (subRef != null)) {
            try {
                Method[] methods = response.getClass().getDeclaredMethods();
                if (methods != null) {
                    LOG.debug("Method count: " + methods.length);
                    for (Method m : methods) {
                        LOG.debug("Looking at method: " + m.getName());
                        if (m.getName().equals("setSubscriptionReference")) {
                            Class[] paramTypes = m.getParameterTypes();
                            if (paramTypes != null) {
                                LOG.debug("Parameter count: " + paramTypes.length);
                                for (Class paramType : paramTypes) {
                                    LOG.debug("Param type: " + paramType.getName());
                                    if (paramType.isAssignableFrom(EndpointReferenceType.class)) {
                                        LOG.debug("Param type is EndpointReferenceType");
                                        Object[] params = { subRef };
                                        LOG.debug("Invoking EndpointReferenceType param method");
                                        m.invoke(response, params);
                                        break;
                                    } else if (paramType.isAssignableFrom(W3CEndpointReference.class)) {
                                        LOG.debug("Param type is W3CEndpointReference");
                                        Object[] params = { convertEndpointReferenceToW3cEndpointReference(subRef) };
                                        LOG.debug("Invoking W3CEndpointReference param method");
                                        m.invoke(response, params);
                                        break;
                                    }

                                }
                            } else {
                                LOG.debug("Parameter types was null");
                            }
                            break;
                        }
                    }
                } else {
                    LOG.debug("Methods were null");
                }
            } catch (IllegalAccessException ex) {
                LOG.error("IllegalAccessException encountered: " + ex.getMessage(), ex);
            } catch (IllegalArgumentException ex) {
                LOG.error("IllegalArgumentException encountered: " + ex.getMessage(), ex);
            } catch (InvocationTargetException ex) {
                LOG.error("InvocationTargetException encountered: " + ex.getMessage(), ex);
            }
        }
    }

    private EndpointReference convertEndpointReferenceToW3cEndpointReference(EndpointReferenceType epr) {
        LOG.info("begin CreateSubscriptionReference");
        W3CEndpointReference subRef = null;

        if (epr != null) {
            W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();

            if (epr.getAddress() != null) {
                LOG.info("subscriptionManagerUrl=" + epr.getAddress().getValue());
                resultBuilder.address(epr.getAddress().getValue());
            }
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try {
                doc = docBuilderFactory.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
            doc.setXmlStandalone(true);

            if ((epr.getReferenceParameters() != null) && (epr.getReferenceParameters().getAny() != null)
                    && (!epr.getReferenceParameters().getAny().isEmpty())) {
                List<Object> refParams = epr.getReferenceParameters().getAny();
                for (Object o : refParams) {
                    LOG.debug("Processing a reference parameter");
                    if (o instanceof Element) {
                        Element refParam = (Element) o;
                        resultBuilder.referenceParameter(refParam);
                    } else {
                        LOG.warn("Reference parameter was not of type Element - was " + o.getClass());
                    }
                }
            } else {
                LOG.warn("Reference parameters or ref param list was null or empty");
            }

            LOG.info("building.. resultBuilder.build()");
            subRef = resultBuilder.build();
        } else {
            LOG.warn("The endpoint reference was null");
        }

        LOG.info("end CreateSubscriptionReference");
        return subRef;
    }

}
