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
import java.io.PrintStream;

public class UniqueIdAllocator extends IdAllocator {

	String getUniqueIdBase() throws IOException {
		return Io.stringFromFile(new File(uniqueid_base_file)).trim();
	}

	String getUniqueIdIndex() throws IOException {
		return Io.stringFromFile(new File(uniqueid_index_file)).trim();
	}

	void putUniqueIdIndex(String index) throws IOException {
		PrintStream ps = new PrintStream(new File(uniqueid_index_file));
		ps.print(index);
		ps.close();
	}

	String allocateUniqueId() throws IOException {
		String uniqueid_base = getUniqueIdBase();
		int uniqueid_index = Integer.parseInt(getUniqueIdIndex()) + 1;
		putUniqueIdIndex(String.valueOf(uniqueid_index));
		return uniqueid_base + uniqueid_index;
	}

	String allocate() throws XdsInternalException {
		try {
		return allocateUniqueId();
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}
	}



}
