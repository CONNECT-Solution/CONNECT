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

package com.vangent.hieos.xwebtools.servlets.framework;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicServlet extends HttpServlet {

    PrintWriter _writer = null;
    HttpUtils _h = null;
    public HttpServletResponse response;
    public HttpServletRequest request;

    public HttpUtils h() throws ServletException {
        if (_h == null) {
            _h = new HttpUtils(get_writer());
        }
        return _h;
    }

    public void close() {
        if (_h != null) {
            _h.close();
            _h = null;
        }
        _writer = null;
    }

    private PrintWriter get_writer()
            throws ServletException {
        if (_writer == null) {
            try {
                _writer = response.getWriter();
            } catch (IOException e) {
                throw new ServletException("doGet: cannot getWriter()");
            }
        }
        return _writer;
    }
}
