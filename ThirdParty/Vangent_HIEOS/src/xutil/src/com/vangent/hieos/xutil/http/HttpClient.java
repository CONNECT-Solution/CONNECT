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

/*
 * HttpClient.java
 *
 * Created on September 29, 2003, 8:06 AM
 */

package com.vangent.hieos.xutil.http;

import com.vangent.hieos.xutil.exception.HttpCodeException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * HttpClient handles all non-SOAP, non-JAXR communication between the NIST tools
 * and the ebXMLrr Registry.
 * @author bill
 * @author Bernie Thuman (removed a ton of code now that we are using OMAR).
 */
public class HttpClient implements HostnameVerifier {
	HttpURLConnection conn;
	FilterInputStream in;

	public HttpClient() {
	}

    /**
     *
     * @param uri
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
	public static String httpGet(String uri) throws java.net.MalformedURLException, java.io.IOException, java.lang.Exception {
		URI u_uri = new URI(uri);
        HttpClient hc = new HttpClient();
        hc.basic_raw_get(u_uri);
        return hc.getReply();
	}

    // tells any https connections we make not to verify hostnames  (all are ok)
	public boolean verify(String hostname,
			SSLSession session) {
		return true;
	}

    /**
     */
	private String basic_raw_get(URI uri)
	throws java.net.MalformedURLException, java.io.IOException, XdsInternalException, HttpCodeException {
		URL url;
		try {
			url = uri.toURL();
		} catch (Exception e) {
			throw new XdsInternalException("Error trying to retrieve " + uri.toString() + " : " + e.getMessage());
		}

		HttpsURLConnection.setDefaultHostnameVerifier(this);  // call verify() above to validate hostnames
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "text/html, text/xml, text/plain, */*");
		conn.connect();

		return getResponse();
	}

    /**
     *
     * @return
     * @throws com.vangent.hieos.xutil.exception.HttpCodeException
     * @throws java.io.IOException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
	private String getResponse()
	throws HttpCodeException, IOException, XdsInternalException {
		try {
			String encoding = conn.getContentEncoding();
			if (encoding == null) {
				in = (FilterInputStream) conn.getInputStream();
			} else {
				Object o = conn.getContent();
				in = (FilterInputStream) o;
			}
		} catch (java.io.IOException e) {
			int code = conn.getResponseCode();
			//System.out.println("ERROR: code: " + String.valueOf(code) + " message: " + conn.getResponseMessage());
			InputStream is = conn.getErrorStream();
			if (is == null) {
				String msg = conn.getResponseMessage();
				URL url = conn.getURL();
				throw new XdsInternalException("Error retieving content of " + url.toString() + "; response was " + msg);
			} else {
				StringBuffer b = new StringBuffer();
				byte[] by = new byte[256];
				while ( is.read(by, 0, 256) > 0 )
					b.append(new String(by));  // get junk at end, should be sensitive to number of bytes read
				//System.out.println(new String(b));
				throw new HttpCodeException("ERROR: HttpClient: code: " + String.valueOf(code) + " message: " + conn.getResponseMessage() +
						"\n" + new String(b) + "\n");
			}
		}
		return conn.getHeaderField("Content-Type");
	}

    /**
     *
     * @return
     * @throws java.io.IOException
     */
	private String getReply() throws java.io.IOException {
		return Io.getStringFromInputStream(in);
	}
}
