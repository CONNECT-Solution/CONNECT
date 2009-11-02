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

package com.vangent.hieos.xutil.client;

import com.vangent.hieos.xutil.response.RegistryResponseParser;

import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class RetContext {
	HashMap<String, RetInfo> request_info, response_info;
	OMElement request, result;
	RegistryResponseParser rrp;
	String expectedError;
	
	public RetContext() {
		request_info = null;
		response_info = null;
		result = null;
		request = null;
		rrp = null;
		expectedError = null;
	}
	
	public HashMap<String, RetInfo> getRequestInfo() {
		return request_info;
	}
	public void setRequestInfo(HashMap<String, RetInfo> request_info) {
		this.request_info = request_info;
	}
	public void addRequestInfo(String uid, RetInfo ri) {
		if (request_info == null)
			request_info = new HashMap<String, RetInfo>();
		request_info.put(uid, ri);
	}
	public HashMap<String, RetInfo> getResponseInfo() {
		return response_info;
	}
	
	public void setExpectedError(String msg) {
		expectedError = msg;
	}
	public String getExpectedError() { return expectedError; }
	public void setResponseInfo(HashMap<String, RetInfo> response_info) {
		this.response_info = response_info;
	}
	public OMElement getResult() {
		return result;
	}
	public void setResult(OMElement result) {
		this.result = result;
	}
	public RegistryResponseParser getRrp() {
		return rrp;
	}
	public void setRrp(RegistryResponseParser rrp) {
		this.rrp = rrp;
	}

	public OMElement getRequest() {
		return request;
	}

	public void setRequest(OMElement request) {
		this.request = request;
	}
}

