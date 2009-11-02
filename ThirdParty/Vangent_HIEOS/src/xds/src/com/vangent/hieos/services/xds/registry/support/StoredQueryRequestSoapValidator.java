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

package com.vangent.hieos.services.xds.registry.support;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.metadata.validation.ValidationEngine;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;

public class StoredQueryRequestSoapValidator extends ValidationEngine {
	short xds_version;
	MessageContext messageContext;
	
	protected String getTFReferences() {
		return "3.18.3 Referenced Standards";
	}


	
	public StoredQueryRequestSoapValidator(short xds_version, MessageContext messageContext) {
		this.xds_version = xds_version;
		this.messageContext = messageContext;
	}
	
	public void run() {
		SOAPEnvelope env = messageContext.getEnvelope();
		OMNamespace ns = env.getNamespace();
		boolean isSOAP12 = ns.getNamespaceURI().contains("http://www.w3.org/2003/05/soap-envelope");
		boolean isSOAP11 = ns.getNamespaceURI().contains("http://schemas.xmlsoap.org/soap/envelope/");

		if (xds_version == XBaseTransaction.xds_a) {
			if ( isSOAP12) 
				newError("A SOAP 1.1 endpoint (XDS.a SQ) was used but message was in SOAP 1.2 format. SOAP Namespace is ")
				.appendError(ns.getNamespaceURI());
			else if ( isSOAP11 )
				;
			else 
				newError("A SOAP 1.1 endpoint (XDS.a SQ) was used but message was in unknown SOAP format. SOAP Namespace is ")
				.appendError(ns.getNamespaceURI());
				
		} else if (xds_version == XBaseTransaction.xds_b) {
			if ( isSOAP11 ) 
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but message was in SOAP 1.1 format. SOAP Namespace is ")
				.appendError(ns.getNamespaceURI());
			else if ( isSOAP12 )
				;
			else
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but message was in unknown SOAP format. SOAP Namespace is ")
				.appendError(ns.getNamespaceURI());
			
			OMElement hdr = MetadataSupport.firstChildWithLocalName(env, "Header");
			if (hdr == null) {
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but no SOAP Header was found.");
			}
			OMElement to = MetadataSupport.firstChildWithLocalName(hdr, "To");
			if (to == null) {
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <To> is missing.");
			} else {
				try {
					new URL(to.getText());
				} catch (MalformedURLException e) {
					newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <To> does not parse as a URL.");
					appendError(" Value is <");
					appendError(to.getText());
					appendError(">");
				}
			}
			
			OMElement action = MetadataSupport.firstChildWithLocalName(hdr, "Action");
			if (action == null) 
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <Action> is missing.");

				
		}
		else
			newError("StoredQueryRequestSoapValidator: internal error - request is labeled as neither XDS.a nor XDS.b");
	}


}
