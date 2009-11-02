/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xutil.soap;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xml.Util;

import java.util.HashMap;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;

import com.vangent.hieos.xutil.xconfig.XConfig;

public class Soap {

    // BHT (FIXED): Can not have this as static; encountered multi-threading problem / and nested
    // call problem.
    //static ServiceClient serviceClient = null
    ServiceClient serviceClient = null;
    OMElement result = null;
    boolean async = false;

    public void setAsync(boolean async) {
        this.async = async;
    }

    public OMElement soapCall(OMElement body, String endpoint, boolean mtom,
            boolean addressing, boolean soap12, String action, String expected_return_action)
            throws XdsException {

        try {
            if (serviceClient == null) {
                serviceClient = new ServiceClient();
            }

            Options options = serviceClient.getOptions();
            options.setTo(new EndpointReference(endpoint));

            if (System.getenv("XDSHTTP10") != null) {
                System.out.println("Generating HTTP 1.0");

                options.setProperty(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
                        org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_10);

                options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,
                        Boolean.FALSE);
            }
            // Get the configured timeout value.
            XConfig xconfig = XConfig.getInstance();
            long timeOut = xconfig.getHomeCommunityPropertyAsLong("SOAPtimeOutInMilliseconds");
            // Set the timeout value.
            options.setTimeOutInMilliSeconds(timeOut);
            options.setProperty(Constants.Configuration.ENABLE_MTOM,
                    ((mtom) ? Constants.VALUE_TRUE : Constants.VALUE_FALSE));
            options.setAction(action);
            if (addressing) {
                serviceClient.engageModule("addressing");
            } else {
                serviceClient.disengageModule("addressing");    // this does not work in Axis2 yet
            }
            options.setSoapVersionURI(
                    ((soap12) ? SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI : SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI));
            if (async && !options.isUseSeparateListener()) {
                options.setUseSeparateListener(async);
            }

            OMElement result = serviceClient.sendReceive(body);

            if (async) {
                serviceClient.cleanupTransport();
            }

            Object in = serviceClient.getServiceContext().getLastOperationContext().getMessageContexts().get("In");
            if (!(in instanceof MessageContext)) {
                throw new XdsInternalException("Soap: In MessageContext of type " + in.getClass().getName() + " instead of MessageContext");
            }
            MessageContext inMsgCxt = (MessageContext) in;
            boolean responseMtom = inMsgCxt.isDoingMTOM();

            if (mtom != responseMtom) {
                if (mtom) {
                    throw new XdsFormatException("Request was MTOM format but response was SIMPLE SOAP");
                } else {
                    throw new XdsFormatException("Request was SIMPLE SOAP but response was MTOM");
                }
            }

            this.result = result;

            if (async) {
                verify_returned_action(expected_return_action, "urn:mediateResponse");
            } else {
                verify_returned_action(expected_return_action, null);
            }

            return result;

        } catch (AxisFault e) {
            throw new XdsException(e.getMessage());
        }
    }

    public OMElement getResult() {
        return result;
    }

    void verify_returned_action(String expected_return_action, String alternate_return_action) throws XdsException {
        if (expected_return_action == null) {
            return;
        }

        OMElement hdr = getInHeader();
        if (hdr == null && expected_return_action != null) {
            throw new XdsInternalException("No SOAPHeader returned: expected header with action = " + expected_return_action);
        }
        OMElement action = MetadataSupport.firstChildWithLocalName(hdr, "Action");
        if (action == null && expected_return_action != null) {
            throw new XdsInternalException("No action returned in SOAPHeader: expected action = " + expected_return_action);
        }
        String action_value = action.getText();
        if (alternate_return_action == null) {
            if (action_value == null || !action_value.equals(expected_return_action)) {
                throw new XdsInternalException("Wrong action returned in SOAPHeader: expected action = " + expected_return_action +
                        " returned action = " + action_value);
            }
        } else {
            if (action_value == null ||
                    ((!action_value.equals(expected_return_action)) && (!action_value.equals(alternate_return_action)))) {
                throw new XdsInternalException("Wrong action returned in SOAPHeader: expected action = " + expected_return_action +
                        " returned action = " + action_value);
            }
        }
    }

    public OMElement getInHeader() throws XdsInternalException {
        OperationContext oc = serviceClient.getLastOperationContext();
        HashMap<String, MessageContext> ocs = oc.getMessageContexts();
        MessageContext in = ocs.get("In");

        if (in == null) {
            return null;
        }

        if (in.getEnvelope() == null) {
            return null;
        }

        if (in.getEnvelope().getHeader() == null) {
            return null;
        }

        return Util.deep_copy(in.getEnvelope().getHeader());
    }

    public OMElement getOutHeader() throws XdsInternalException {
        OperationContext oc = serviceClient.getLastOperationContext();
        HashMap<String, MessageContext> ocs = oc.getMessageContexts();
        MessageContext out = ocs.get("Out");

        if (out == null) {
            return null;
        }

        return Util.deep_copy(out.getEnvelope().getHeader());
    }
}
