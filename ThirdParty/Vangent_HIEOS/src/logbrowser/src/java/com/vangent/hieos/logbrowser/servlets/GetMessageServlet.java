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
package com.vangent.hieos.logbrowser.servlets;

import com.vangent.hieos.logbrowser.log.db.Log;
import com.vangent.hieos.logbrowser.log.db.LoggerException;
import com.vangent.hieos.logbrowser.log.db.Message;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMessageServlet extends HttpServlet {

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        // Do nothing.
    }

    /**
     *
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        StringBuffer s = new StringBuffer();
        res.setContentType("text/javascript");
        String messageId = req.getParameter("message");
        Log log = new Log();
        try {
            Message m = log.readMessage(messageId);
            s.append("{ \"result\" : " + m.toJSon() + " } ");
        } catch (LoggerException e) {
            s.append(" { \"result\" : {");
            s.append(" \"error\" : \"" + e.getMessage().replaceAll("\n", "\\u000a").replaceAll("\"", "'") + "\"");
            s.append("}}");
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                Logger.getLogger(GetMessageServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            res.getWriter().write(s.toString());
        } catch (IOException e) {
        }
    }
}
