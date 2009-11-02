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

package com.vangent.hieos.services.xds.repository.serviceimpl;

import com.vangent.hieos.xutil.services.framework.*;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.XdsValidationException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.services.framework.ContentValidationService;
import com.vangent.hieos.services.xds.repository.transactions.ProvideAndRegisterDocumentSet;
import com.vangent.hieos.services.xds.repository.transactions.RetrieveDocumentSet;

import org.apache.axis2.AxisFault;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import org.apache.log4j.Logger;

// Axis2 LifeCycle support:
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;

import com.vangent.hieos.xutil.atna.XATNALogger;

public class XDSbRepository extends XAbstractService implements ContentValidationService {
    private final static Logger logger = Logger.getLogger(XDSbRepository.class);

    boolean optimize_retrieve = true;
    String alternateRegistryEndpoint = null;

    /**
     *
     */
    public XDSbRepository() {
        super();
    }


    /**
     *
     * @param sor
     * @return
     * @throws org.apache.axis2.AxisFault
     */
    public OMElement SubmitObjectsRequest(OMElement sor) throws AxisFault {
        return ProvideAndRegisterDocumentSetRequest(sor);
    }

    /**
     *
     * @param sor
     * @return
     * @throws org.apache.axis2.AxisFault
     */
    public OMElement ProvideAndRegisterDocumentSetRequest(OMElement sor) throws AxisFault {
        try {
            OMElement startup_error = beginTransaction(getPnRTransactionName(), sor, XAbstractService.repository_actor);
            if (startup_error != null) {
                return startup_error;
            }
            log_message.setTestMessage(getPnRTransactionName());
            validateWS();
            validatePnRTransaction(sor);
            ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message, getXdsVersion(), getMessageContext());
            if (alternateRegistryEndpoint != null) {
                s.setRegistryEndPoint(alternateRegistryEndpoint);
            }
            OMElement result = s.provideAndRegisterDocumentSet(sor, this);
            endTransaction(s.getStatus());
            return result;
        } catch (Exception e) {
            return endTransaction(sor, e, XAbstractService.repository_actor, "");
        }
    }

    /**
     * 
     * @param rdsr
     * @return
     * @throws org.apache.axis2.AxisFault
     */
    public OMElement RetrieveDocumentSetRequest(OMElement rdsr) throws AxisFault {
        try {
            OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, XAbstractService.repository_actor);
            if (startup_error != null) {
                return startup_error;
            }
            log_message.setTestMessage(getRetTransactionName());
            validateWS();
            validateRetTransaction(rdsr);
            OMNamespace ns = rdsr.getNamespace();
            String ns_uri = ns.getNamespaceURI();
            if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
                OMElement res = this.start_up_error(rdsr, "AbstractRepository.java", XAbstractService.repository_actor, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
                endTransaction(false);
                return res;
            }
            RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XBaseTransaction.xds_b, getMessageContext());
            OMElement result = s.retrieveDocumentSet(rdsr, this, true /* optimize */, this);
            endTransaction(s.getStatus());
            return result;
        } catch (Exception e) {
            return endTransaction(rdsr, e, XAbstractService.repository_actor, "");
        }
    }

    /**
     *
     * @param endpoint
     */
    private void setAlternateRegistryEndpoint(String endpoint) {
        alternateRegistryEndpoint = endpoint;
    }

    /**
     *
     * @param opt
     */
    public void optimize_retrieve(boolean opt) {
        optimize_retrieve = opt;
    }

    public boolean runContentValidationService(Metadata m, Response response) throws MetadataException {
        return true;
    }

    private String getPnRTransactionName() {
        return "PnR.b";
    }

    private String getRetTransactionName() {
        return "RET.b";
    }

    private void validateWS() throws XdsWSException {
        checkSOAP12();
        if (isAsync()) {
            throw new XdsWSException("Asynchronous web service request not acceptable on this endpoint" +
                    " - replyTo is " + getMessageContext().getReplyTo().getAddress());
        }
    }

    private short getXdsVersion() {
        return XBaseTransaction.xds_b;
    }

    private void validatePnRTransaction(OMElement sor) throws XdsValidationException {
        OMNamespace ns = sor.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
            throw new XdsValidationException("Invalid namespace on " + sor.getLocalName() + " (" + ns_uri + ")");
        }
    }

    private void validateRetTransaction(OMElement rds) throws XdsValidationException {
        OMNamespace ns = rds.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
            throw new XdsValidationException("Invalid namespace on " + rds.getLocalName() + " (" + ns_uri + ")");
        }
    }

    // BHT (ADDED Axis2 LifeCycle methods):
    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     */
    @Override
    public void startUp(ConfigurationContext configctx, AxisService service) {
        logger.info("Repository::startUp()");
        this.ATNAlogStart(XATNALogger.ActorType.REPOSITORY);
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     */
    @Override
    public void shutDown(ConfigurationContext configctx, AxisService service) {
        logger.info("RepositoryB::shutDown()");
        this.ATNAlogStop(XATNALogger.ActorType.REPOSITORY);
    }
}
