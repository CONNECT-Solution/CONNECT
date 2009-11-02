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

package com.vangent.hieos.xutil.atna;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Vincent Lewis
 */
public class DateAdapter extends XmlAdapter<String, Date> {

  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  public Date unmarshal(String date) throws Exception {
    return df.parse(date);
  }

  public String marshal(Date date) throws Exception {
    return df.format(date);
  }
}
