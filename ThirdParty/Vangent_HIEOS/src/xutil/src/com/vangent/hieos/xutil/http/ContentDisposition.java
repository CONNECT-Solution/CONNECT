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
 * ContentDisposition.java
 *
 * Created on November 5, 2003, 2:02 PM
 */

package com.vangent.hieos.xutil.http;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author  bill
 */
public class ContentDisposition {
    HashMap map;
    
    /** Creates a new instance of ContentDisposition */
    public ContentDisposition(HttpServletRequest request) {
        String cd = request.getHeader("Content-Disposition");
        parse(cd);
    }
    
    public ContentDisposition(String cd) {
        parse(cd);
    }
    
    void parse(String contDisp) {
        map = new HashMap();
        String[] parts = contDisp.split(";");
        for (int i=0; i<parts.length; i++) {
            String part = parts[i].trim();
            String[] name_value = part.split("=");
            if (name_value.length == 1) {
                map.put("format", name_value[0]);
                continue;
            }
            if (name_value.length == 0)
                continue;
            map.put(name_value[0], name_value[1]);
        }
    }
    
    public String get(String name) {
        return (String) map.get(name);
    }
    
}
