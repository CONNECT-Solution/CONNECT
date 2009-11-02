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

import com.vangent.hieos.xutil.exception.XdsIOException;
import com.vangent.hieos.xutil.iosupport.Io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;

//import sun.misc.BASE64Decoder;
import org.apache.commons.codec.binary.Base64;

public class Mtom {
	private OMElement document;
	private String content_type;
	private byte[] contents;
	private boolean xop;
	
	public boolean isOptimized() { return xop; }

	public void decode(OMElement document) throws XdsIOException, IOException {
		this.document = document;
		OMText binaryNode = (OMText) document.getFirstOMChild();
		//System.out.println("isOptimized: " + binaryNode.isOptimized());

		xop = binaryNode.isOptimized(); 
		
		if (xop) {
			javax.activation.DataHandler datahandler = (javax.activation.DataHandler) binaryNode.getDataHandler();
			InputStream is = null;
			try {
				is = datahandler.getInputStream();
				contents = Io.getBytesFromInputStream(is);
			}
			catch (IOException e) {
				throw new XdsIOException("Error accessing XOP encoded document content from message");
			}
			this.content_type = datahandler.getContentType();
		} else {
			String base64 = binaryNode.getText();
            contents = Base64.decodeBase64(base64.getBytes());
            /* BHT: REMOVED (and replaced with above line).
			BASE64Decoder d  = new BASE6decoded.toString();4Decoder();
			contents = d.decodeBuffer(base64);
             */
			this.content_type = null;
		}
	}

	public String getContent_type() {
		return content_type;
	}

	public byte[] getContents() {
		return contents;
	}
	
}
