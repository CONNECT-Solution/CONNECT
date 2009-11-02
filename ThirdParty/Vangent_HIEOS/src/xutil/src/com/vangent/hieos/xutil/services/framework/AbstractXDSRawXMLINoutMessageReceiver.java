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
package com.vangent.hieos.xutil.services.framework;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.i18n.Messages;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.log4j.Logger;

abstract public class AbstractXDSRawXMLINoutMessageReceiver extends AbstractInOutMessageReceiver {
    private final static Logger logger = Logger.getLogger(AbstractXDSRawXMLINoutMessageReceiver.class);

    abstract public void validate_action(MessageContext msgContext, MessageContext newmsgContext) throws Exception;

    private Method findOperation(AxisOperation op, Class implClass) {

        Method method = (Method) (op.getParameterValue("myMethod"));

        if (method != null) {
            return method;
        }

        String methodName = op.getName().getLocalPart();

        try {

            // Looking for a method of the form "OMElement method(OMElement)"

            method = implClass.getMethod(methodName,
                    new Class[]{OMElement.class});

            if (method.getReturnType().equals(OMElement.class)) {
                try {
                    op.addParameter("myMethod", method);
                } catch (AxisFault axisFault) {
                    // Do nothing here
                }
                return method;
            }

        } catch (NoSuchMethodException e) {
            // Fault through
        }
        return null;

    }

    /**
     * Invokes the bussiness logic invocation on the service implementation class
     *
     * @param msgContext    the incoming message context
     * @param newmsgContext the response message context
     * @throws AxisFault on invalid method (wrong signature) or behaviour (return null)
     */
    public void invokeBusinessLogic(MessageContext msgContext, MessageContext newmsgContext)
            throws AxisFault {
        try {
            String in_action = msgContext.getWSAAction();
            newmsgContext.setRelationships(new RelatesTo[]{new RelatesTo(msgContext.getMessageID())});
            validate_action(msgContext, newmsgContext);

            // get the implementation class for the Web Service
            Object obj = getTheImplementationObject(msgContext);

            // make return message context available to service
            if (obj instanceof XAbstractService) {
                XAbstractService xo = (XAbstractService) obj;
                xo.setReturnMessageContext(newmsgContext);
            }

            // find the WebService method
            Class implClass = obj.getClass();
            AxisOperation opDesc = msgContext.getAxisOperation();
            Method method = findOperation(opDesc, implClass);
            Method methodDisplay = implClass.getMethod("setMessageContextIn",
                    new Class[]{MessageContext.class});
            methodDisplay.invoke(obj, new Object[]{msgContext});
            if (method == null) {
                throw new AxisFault(Messages.getMessage(
                        "methodDoesNotExistInOut",
                        opDesc.getName().toString()));
            }

            OMElement result = (OMElement) method.invoke(
                    obj, new Object[]{msgContext.getEnvelope().getBody().getFirstElement()});

            SOAPFactory fac = getSOAPFactory(msgContext);
            SOAPEnvelope envelope = fac.getDefaultEnvelope();
            if (result != null) {
                envelope.getBody().addChild(result);
            }
            newmsgContext.setEnvelope(envelope);
        } catch (Exception e) {
            logger.error("Error in XDSRawXMLInOut:\n" + e.getMessage() + "\ngetSoapAction = " + msgContext.getSoapAction());
            throw AxisFault.makeFault(e);

        }

    }
}
