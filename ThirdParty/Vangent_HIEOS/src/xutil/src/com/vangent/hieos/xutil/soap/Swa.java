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

import com.vangent.hieos.xutil.exception.HttpCodeException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xml.Util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class Swa {
	String endpoint;
	String protocol;
	String service;
	String port;
	String host;
	HttpURLConnection conn;
	FilterInputStream in;
	int response_code;
	String response_content_type;
	OMElement response;
	HashMap<String, ArrayList<Object>> attachments = null;
	String boundary = "MIMEBoundaryurn_uuid_30615C45AB8F5F79FF1200322399377";
	String start = "0.urn:uuid:30615C45AB8F5F79FF1200322399378@nist.gov";

	public Swa() {
	}

	void parse_endpoint() throws Exception {
		String[] parts;
		String rest;

		parts = this.endpoint.split(":");
		if (parts.length < 2) 
			throw new XdsInternalException("swa.parse_endpoint(): (" + endpoint + ") Cannot parse endpoint: cannot find http or https before :");
		protocol = parts[0];

		rest = endpoint.substring(protocol.length());

		parts = rest.split("/");
		if (parts.length < 4)  {
			StringBuffer buf = new StringBuffer();
			for (int i=0; i<parts.length; i++) 
				buf.append(i + ") " + parts[i] + "\n");
			throw new XdsInternalException("swa.parse_endpoint(): (" + endpoint + ") Cannot parse endpoint: cannot find machine and port (parts.length = " + parts.length +")\n" + buf.toString());
		}

		String host_port = parts[2];

		this.service = "";
		for (int i=3; i<parts.length; i++) 
			this.service += "/" + parts[i];

		parts = host_port.split(":");
		if (parts.length == 1) {
			this.host = parts[0];
			this.port = "80";
		} else if (parts.length == 2) {
			this.host = parts[0];
			this.port = parts[1];
		} else
			throw new XdsInternalException("swa.parse_endpoint(): (" + endpoint + ") Cannot parse endpoint: cannot parse machine and port");
	}

	public void connect(String endpoint) throws Exception {
		this.endpoint = endpoint;
		parse_endpoint();

		URL url = new URL(protocol, host, Integer.parseInt(port), service);
		conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
	}

	String getResponse()
	throws HttpCodeException, IOException, XdsInternalException {
		try {
			String encoding =conn.getContentEncoding();
			if (encoding == null) {
				in = (FilterInputStream) conn.getInputStream();
			} else {
				Object o = conn.getContent();
				in = (FilterInputStream) o;
			}
		} catch (java.io.IOException e) {
			response_code = conn.getResponseCode();
			//System.out.println("ERROR: code: " + String.valueOf(code) + " message: " + conn.getResponseMessage());
			InputStream is = conn.getErrorStream();
			if (is == null) {
				String msg = conn.getResponseMessage();
				URL url = conn.getURL();
				conn.disconnect();
				throw new XdsInternalException("Error retieving content of " + url.toString() + "; response was " + msg);
			} else {
				StringBuffer b = new StringBuffer();
				byte[] by = new byte[256];
				int cnt;
				while ( (cnt=is.read(by, 0, 256)) > 0 )
					b.append(new String(by, 0, cnt));  
				conn.disconnect();
				String err_response = new String(b);
				try {
					OMElement err_xml = Util.parse_xml(err_response);
					try {
						in.close();
						conn.disconnect();
					} catch (Exception e2) {}
					return err_response;
				} catch (Exception e1 ) {

				}
				throw new HttpCodeException(	"ERROR: HttpClient: code: " + String.valueOf(response_code) 
						+ 
						"\nmessage: " + conn.getResponseMessage() +
						"\n" + new String(b) + "\n"
				);
			}
		}
		response_content_type = conn.getHeaderField("Content-Type");
		String rsp = Io.getStringFromInputStream(in);
		in.close();
		conn.disconnect();
		return rsp;
	}


	public OMElement send(OMElement header_contents, OMElement body_contents) throws Exception {
		OMNamespace soap = MetadataSupport.om_factory.createOMNamespace("http://schemas.xmlsoap.org/soap/envelope/", "soapenv");
		OMElement envelope = MetadataSupport.om_factory.createOMElement("Envelope", soap);
		OMElement header = MetadataSupport.om_factory.createOMElement("Header", soap);
		OMElement body = MetadataSupport.om_factory.createOMElement("Body", soap);
		if (header_contents != null)
			header.addChild(header_contents);
		if (body_contents != null)
			body.addChild(body_contents);
		envelope.addChild(header);
		envelope.addChild(body);
		return send(envelope);
	}

	public OMElement send(OMElement envelope) throws Exception {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("SOAPAction", "\"SubmitObjectsRequest\"");
		if (attachments != null)
			headers.put("Content-Type", "multipart/related; boundary=" + boundary + "; type=\"text/xml\"; start=\"<" + start + ">\"");
		else
			headers.put("Content-Type", "\"text/xml\"");
		headers.put("User-Agent", "xdstest2");

		StringBuffer msg = new StringBuffer();

		if (attachments != null) {
			msg.append("--" + boundary + "\r\n");
			msg.append("Content-Type: text/xml; charset=UTF-8\r\n");
			msg.append("Content-Transfer-Encoding: 8bit\r\n");
			msg.append("Content-ID: <" + start + ">\r\n");
			msg.append("\r\n");
		}

		msg.append(envelope.toString() + " \r\n");

		if (attachments != null) {
			for (String id : attachments.keySet()) {
				ArrayList<Object> attachment = attachments.get(id);
				byte[] content = (byte[]) attachment.get(0);
				String mime_type = (String) attachment.get(1);

				msg.append("\n--" + boundary + "\r\n");
				msg.append("Content-Type: " + mime_type + "\r\n");
				msg.append("Content-Transfer-Encoding: binary\r\n");
				msg.append("Content-ID: <" + id + ">\r\n");
				msg.append("\r\n");
				msg.append(new String(content));
				msg.append("\r\n");
			}
			msg.append("--" + boundary + "--\r\n");
		}

		String reply_str = post(headers, msg.toString());
		OMElement reply_xml = Util.parse_xml(reply_str);

		if (reply_xml == null || !reply_xml.getLocalName().equals("Envelope"))
			throw new XdsInternalException("Reply is not SOAP Envelope: \n" + reply_str);
		
		if (hasFault(reply_xml))
			throw new XdsInternalException("Fault: " + getFaultMessage(reply_xml));

		this.response = reply_xml;

		return reply_xml;
	}
	
	boolean hasFault(OMElement reply) throws Exception {
		OMElement body = MetadataSupport.firstChildWithLocalName(reply, "Body");
		if (body == null)
			throw new XdsInternalException("Swa: hasFault(): SOAP message has no body");
		OMElement fault = MetadataSupport.firstChildWithLocalName(body, "Fault");
		return fault != null;
	}
	
	String getFaultMessage(OMElement reply) {
		OMElement body = MetadataSupport.firstChildWithLocalName(reply, "Body");
		if (body == null) return "";
		OMElement fault = MetadataSupport.firstChildWithLocalName(body, "Fault");
		if (fault == null) return "";
		OMElement faultstring = MetadataSupport.firstChildWithLocalName(fault, "faultstring");
		if (faultstring == null) return "";
		return faultstring.getText();
	}

	public String post(HashMap<String, String> headers, String body) throws Exception {
		for (String name : headers.keySet()) {
			String value = headers.get(name);
			conn.setRequestProperty(name, value);
		}
		conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
		conn.connect();

		OutputStream os = conn.getOutputStream();

		os.write(body.getBytes());

		os.close();


		return getResponse();
	}

	public void addAttachment(byte[] content, String mime_type, String id) {
		if (attachments == null)
			attachments = new HashMap<String, ArrayList<Object>>();
		ArrayList<Object> attachment = new ArrayList<Object>();
		attachment.add(content);
		attachment.add(mime_type);
		attachments.put(id, attachment);
	}

	public OMElement getHeader() {
		if (this.response == null)
			return null;
		return MetadataSupport.firstChildWithLocalName(this.response, "Header");
	}

	public OMElement getBody() {
		if (this.response == null)
			return null;
		return MetadataSupport.firstChildWithLocalName(this.response, "Body");
	}

}
