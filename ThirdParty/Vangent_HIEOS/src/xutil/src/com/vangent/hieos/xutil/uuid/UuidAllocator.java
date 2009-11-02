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

package com.vangent.hieos.xutil.uuid;

/*
import com.sun.ebxml.registry.util.UUID;
import com.sun.ebxml.registry.util.UUIDFactory;
 */
/*
import org.freebxml.omar.common.UUID;
import org.freebxml.omar.common.UUIDFactory;
 */
import java.util.UUID;

public class UuidAllocator {
    //static UUIDFactory fact = null;

    static public String allocate() {
        /*
        if (fact == null)
            fact = UUIDFactory.getInstance();
        UUID uu = fact.newUUID()
        */
        String uu = UUID.randomUUID().toString();
        return "urn:uuid:" + uu;
    }
}
