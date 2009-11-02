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

package com.vangent.hieos.xutil.metadata.validation;

import com.vangent.hieos.xutil.exception.XdsValidationException;

abstract public class ValidationEngine {
	StringBuffer errorBuffer = null;
	
	abstract public void run();
	
	// expected to return \n delimited list of TF references to quote in the error message
	abstract protected String getTFReferences();
	
	public void runWithException() throws XdsValidationException {
		run();
		if (hasErrors()) 
			throw new XdsValidationException(getResults());
	}

	protected ValidationEngine newError(Object msg) {
		if (errorBuffer == null) 
			errorBuffer = new StringBuffer();
		else
			finishError();
		
		errorBuffer
		.append("Error: ")
		.append(msg);
		return this;
	}
	
	public ValidationEngine appendError(Object msg) {
		if (errorBuffer == null) 
			errorBuffer = new StringBuffer();
		errorBuffer.append(msg);
		return this;
	}
	
	private void finishError() {
		if (errorBuffer != null)
			errorBuffer.append("\n");
	}
	
	public String getResults() {
		if (errorBuffer == null)
			return null;
		String refs = getTFReferences();
		if (refs != null && !refs.equals(""))
			errorBuffer
			.append("Technical Framework References:\n")
			.append(refs);
		return errorBuffer.toString();
	}
	
	protected boolean hasErrors() {
		return errorBuffer != null;
	}
}
