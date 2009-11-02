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
package com.vangent.hieos.xutil.registry;

//java Imports
import java.io.StringWriter;

//freebxml imports (from omar)
import org.freebxml.omar.common.spi.LifeCycleManager;
import org.freebxml.omar.common.spi.LifeCycleManagerFactory;
import org.freebxml.omar.common.spi.QueryManager;
import org.freebxml.omar.common.spi.QueryManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;

//ebxml bindings imports
import org.oasis.ebxml.registry.bindings.lcm.ApproveObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.DeprecateObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.RelocateObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.RemoveObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.SetStatusOnObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.UndeprecateObjectsRequestType;
import org.oasis.ebxml.registry.bindings.lcm.UpdateObjectsRequestType;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequestType;
import org.oasis.ebxml.registry.bindings.rim.UserType;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponseType;
//axis 2 imports
import org.apache.axiom.om.OMElement;

import com.vangent.hieos.xutil.exception.XdsInternalException;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author nistra
 */
public class OmarRegistry {

    private final static Logger log = Logger.getLogger(OmarRegistry.class);

    //Instantiate required objects
    private org.freebxml.omar.common.BindingUtility bu = org.freebxml.omar.common.BindingUtility.getInstance();
    private AuthenticationServiceImpl authc = AuthenticationServiceImpl.getInstance();
    private ServerRequestContext context = null;
    //This object is for adhocquery
    private QueryManager qm = QueryManagerFactory.getInstance().getQueryManager();
    //This object is for submitobjectrequest
    private LifeCycleManager lcm = LifeCycleManagerFactory.getInstance().
            getLifeCycleManager();
    public static final String ALIAS_REGISTRY_OPERATOR = "urn:freebxml:registry:predefinedusers:registryoperator";

    public OmarRegistry(OMElement request) {
        try {
            //Create Variables which are required to call to the Request class.
            //creating the context object
            Object requestObject = bu.getRequestObject(request.getLocalName(),
                    request.toString());
            // FIXME (BHT): Should use UUID as context ID.
            String contextId = "Request." + requestObject.getClass().getName();
            context = new ServerRequestContext(contextId,
                    (RegistryRequestType) requestObject);

            //instantiate User object with registryObject
            //registryObject has the authority to submit object requests as well as query the registry objects
            //In future this have to be changed to handle real user configured and created in in the omar DB
            UserType user = authc.registryOperator;
            context.setUser(user);
        } catch (Exception e) {
            // FIXME (BHT): Can not just eat exceptions.
            log.error("**OMAR EXCEPTION**", e);
        }
    }

    /**
     * Processes the Request by dispatching it to a service in the registry.
     */
    public OMElement process()
            throws XdsInternalException {
        RegistryResponseType rr = null;
        try {
            RegistryRequestType message = context.getCurrentRegistryRequest();
            long startTime = System.currentTimeMillis();
            if (message instanceof AdhocQueryRequestType) {
                log.trace("OMAR: submitAdhocQuery ...");
                rr = qm.submitAdhocQuery(context);
                log.trace("OMAR: submitAdhocQuery ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof SubmitObjectsRequestType) {
                log.trace("OMAR: submitObjects ...");
                rr = lcm.submitObjects(context);
                log.trace("OMAR: submitObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof ApproveObjectsRequestType) {
                log.trace("OMAR: approveObjects ...");
                rr = lcm.approveObjects(context);
                log.trace("OMAR: approveObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof DeprecateObjectsRequestType) {
                log.trace("OMAR: deprecateObjects ...");
                rr = lcm.deprecateObjects(context);
                log.trace("OMAR: deprecateObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof SetStatusOnObjectsRequestType) {
                log.trace("OMAR: setStatusOnObjects ...");
                rr = lcm.setStatusOnObjects(context);
                log.trace("OMAR: setStatusOnObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof UndeprecateObjectsRequestType) {
                log.trace("OMAR: unDeprecateObjects ...");
                rr = lcm.unDeprecateObjects(context);
                log.trace("OMAR: unDeprecateObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof RemoveObjectsRequestType) {
                log.trace("OMAR: removeObjects ...");
                rr = lcm.removeObjects(context);
                log.trace("OMAR: removeObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof UpdateObjectsRequestType) {
                log.trace("OMAR: updateObjects ...");
                rr = lcm.updateObjects(context);
                log.trace("OMAR: updateObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else if (message instanceof RelocateObjectsRequestType) {
                log.trace("OMAR: relocateObjects ...");
                rr = lcm.relocateObjects(context);
                log.trace("OMAR: relocateObjects ELAPSED TIME: " + new Long(System.currentTimeMillis() - startTime).toString());
            } else {
                throw new XdsInternalException(ServerResourceBundle.getInstance().
                        getString("message.unknownRequest") +
                        message.getClass().getName());
            }
        } catch (Exception e) {
            // FIXME (BHT): Can not just eat exceptions.
            log.error("**OMAR EXCEPTION**", e);
        }
        OMElement response = null;
        try {
            StringWriter sw = new StringWriter();
            javax.xml.bind.Marshaller marshaller = bu.rsFac.createMarshaller();
            marshaller.setProperty(
                    javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            marshaller.marshal(rr, sw);

            //Now get the RegistryResponse as a String
            String respStr = sw.toString();
            //convert the response to OMElement to send the response
            response = AXIOMUtil.stringToOM(respStr);
        } catch (Exception e) {
            // FIXME (BHT): Do not eat exceptions.
            log.error("**OMAR EXCEPTION**", e);
        }
        return response;
    }
}
