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

package com.vangent.hieos.xtest.framework;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;

import java.io.File;
import java.io.IOException;

public class SourceIdAllocator extends IdAllocator {

	public String allocate() throws XdsInternalException {
		try {
			return getFromFile();
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}

	}
	
	String getFromFile() throws IOException {
		return Io.stringFromFile(new File(sourceId_file)).trim();
	}
}
