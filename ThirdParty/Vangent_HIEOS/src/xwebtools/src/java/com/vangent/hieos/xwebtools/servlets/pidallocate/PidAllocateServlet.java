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

package com.vangent.hieos.xwebtools.servlets.pidallocate;

import com.vangent.hieos.xwebtools.servlets.framework.BasicServlet;
import com.vangent.hieos.adt.db.AdtRecordBean;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.metadata.validation.CodeValidation;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;

public class PidAllocateServlet extends BasicServlet {

    public void doGet(
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws ServletException {
        this.request = request;
        this.response = response;

        CodeValidation cv = null;
        try {
            cv = new CodeValidation();
        } catch (XdsInternalException e) {
            throw new ServletException(RegistryUtility.exception_details(e));
        }
        ArrayList<String> assigning_authorities = cv.getAssigningAuthorities();

        if (request.getParameter("rest") != null) {
            // request is coming from simple rest interface
            // response should be text of new patient id and
            // content-type is text/plain

            // allocate from first AA on list
            String pid = allocate_pid(assigning_authorities.get(0));
            response.setContentType("text/plain");
            h().o(pid);
            close();
            return;
        }

        h().head("XDS Patient ID Allocation Service");
        h().h1("XDS Patient ID Allocation Service");
        h().o("Patient IDs allocated through this service will be fed to the XDS.b Registry actor");
        h().o("as part of the Patient Identity Feed transaction.  Once delivered, the XDS.b Registry actor");
        h().o("will accept Register Transactions for this Patient ID.");

        if (assigning_authorities.size() > 1) {
            h().hr();
            h().o("The XDS.b Registry is configured to support multiple Assigning Authorities. ");
            h().o("Choose the Assigning Authority you need a Patient ID for below.");

            h().post_form("/xwebtools/pidallocate", null);

            for (String aa : assigning_authorities) {
                h().br();
                h().input("submit", "aa", aa, null, "");
            }

            h().end_form();

            // Allow a user to hardwire a patient id.
            h().hr();
            h().o("Place your Patient ID here if you want to force it into the Registry");
            h().o("<form action=\"/xwebtools/pidallocate\" method=\"post\">");
            h().o("Patient Id: <input type=\"text\" name=\"pid\" />");
            h().o(" example: e38fc7671012415^^^&1.3.6.1.4.1.21367.2009.1.2.315&ISO");
            h().o("<br />");
            h().o("<input type=\"submit\" value=\"Submit\" />");
            h().o("</form>");

        } else {

            h().h2("New Patient ID is");
            h().br();

            h().o(allocate_pid(assigning_authorities.get(0)));

        }

        close();
    }

    public void doPost(
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws ServletException {

        this.response = response;
        this.request = request;
        h().head("XDS Patient ID Allocation Service");
        h().h2("New Patient ID is");
        h().br();

        String forcedPatientId = request.getParameter("pid");
        if (forcedPatientId != null) {
            this.allocate_forced_pid(forcedPatientId);
            h().o(forcedPatientId);
        } else {
            // Create based upon assigning authority.
            String aa = request.getParameter("aa");
            h().o(allocate_pid(aa));
        }

        close();
    }

    /**
     *
     * @param patientId
     * @throws javax.servlet.ServletException
     */
    private void allocate_forced_pid(String patientId)
            throws ServletException {
        try {
            AdtRecordBean arb = new AdtRecordBean();
            arb.setPatientId(patientId);
            arb.saveToDatabase();
        } catch (XdsInternalException e) {
            throw new ServletException(RegistryUtility.exception_details(e));
        } catch (SQLException e) {
            throw new ServletException(RegistryUtility.exception_details(e));
        } 
    }

    /**
     *
     * @param assigning_authority
     * @return
     * @throws javax.servlet.ServletException
     */
    private String allocate_pid(String assigning_authority)
            throws ServletException {
        AdtRecordBean arb;
        String pid;
        try {
            arb = new AdtRecordBean();
            arb.setPatientIdAutoGenerated(assigning_authority);
            pid = arb.getPatientId();
            arb.saveToDatabase();
            return pid;
        } catch (XdsInternalException e) {
            throw new ServletException(RegistryUtility.exception_details(e));
        } catch (SQLException e) {
            throw new ServletException(RegistryUtility.exception_details(e));
        } 
    }
}
