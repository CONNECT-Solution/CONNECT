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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public class AdapterDocRetrieveDelegate implements InboundDelegate {

    private static Log log = LogFactory.getLog(AdapterDocRetrieveDelegate.class);

    public AdapterDocRetrieveDelegate() {
    }

    @Override
	public Orchestratable process(Orchestratable message) {
    	if (message instanceof NhinDocRetrieveOrchestratable) {
    		process((InboundOrchestratable) message);
    	  } else {
              getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
          }
		return null;
	}
    
    
    public InboundOrchestratable process(InboundOrchestratable message) {
   
            // TODO: check connection manager for which endpoint to use

            // if we are using _a0
            if (true) {
                AdapterDocRetrieveStrategyContext context = new AdapterDocRetrieveStrategyContext(new AdapterDocRetrieveStrategyImpl_a0());
                context.executeStrategy((NhinDocRetrieveOrchestratable) message);
            } else { // if we are using _a1
                // TODO: implement _a1 strategy
            }
            
            return null;
    }

    public void createErrorResponse(InboundOrchestratable message, String error) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof NhinDocRetrieveOrchestratableImpl_g0) {
            RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext(error);
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
            ((NhinDocRetrieveOrchestratableImpl_g0) message).setResponse(response);
        } else /* if(message instanceof NhinDocRetrieveOrchestratableImpl_g1) */ {

        }
    }

    private Log getLogger() {
        return log;
    }

    private class AdapterDocRetrieveStrategyContext {

        private AdapterDocRetrieveStrategy strategy;

        // Constructor
        public AdapterDocRetrieveStrategyContext(AdapterDocRetrieveStrategy strategy) {
            this.strategy = strategy;
        }

        public void executeStrategy(NhinDocRetrieveOrchestratable message) {
            strategy.execute(message);
        }
    }

	
}
