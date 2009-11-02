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

package com.vangent.hieos.xutil.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {

    /**
     *
     * @param e
     * @param message
     * @return
     */
    static public String exception_details(Exception e, String message) {
        if (e == null) {
            return "";
        }
        e.printStackTrace();  // Debug:
        String emessage = e.getMessage();
        if (emessage == null) {
            emessage = "No Message";
        }
        return "Exception: " + e.getClass().getName() + "\n" +
                ((message != null) ? message + "\n" : "") +
                emessage.replaceAll("<", "&lt;");
    /*
    if (e == null)
    return "No stack trace available";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    e.printStackTrace(ps);

    String emessage = e.getMessage();
    if (emessage == null)
    emessage = "No Message";

    return "Exception thrown: " + e.getClass().getName() + "\n" +
    ((message != null) ? message + "\n" : "") +
    emessage.replaceAll("<", "&lt;") + "\n" + new String(baos.toByteArray());
     */
    }

    /**
     * 
     * @param e
     * @param message
     * @return
     */
    static public String exception_long_details(Exception e, String message) {
        if (e == null) {
            return "No stack trace available";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);

        String emessage = e.getMessage();
        if (emessage == null) {
            emessage = "No Message";
        }

        return "Exception thrown: " + e.getClass().getName() + "\n" +
                ((message != null) ? message + "\n" : "") +
                emessage.replaceAll("<", "&lt;") + "\n" + new String(baos.toByteArray());
    }

    /**
     *
     * @param e
     * @return
     */
    static public String exception_details(Exception e) {
        return exception_details(e, null);
    }

    /**
     *
     * @param e
     * @param numLines
     * @return
     */
    static public String exception_details(Exception e, int numLines) {
        return firstNLines(exception_details(e), numLines);
    }

    /**
     *
     * @param e
     * @return
     */
    static public String exception_local_stack(Exception e) {
        StringBuffer buf = new StringBuffer();

        String[] lines = exception_long_details(e, null).split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.indexOf("com.vangent") != -1) {
                buf.append(line).append("\n");
            }
        }
        return buf.toString();
    }

    /**
     *
     * @param message
     * @return
     */
    static public String here(String message) {
        try {
            throw new Exception(message);
        } catch (Exception e) {
            return exception_details(e, message);
        }
    }

    /**
     *
     * @param string
     * @param n
     * @return
     */
    static public String firstNLines(String string, int n) {
        int startingAt = 0;
        for (int i = 0; i < n; i++) {
            if (startingAt != -1) {
                startingAt = string.indexOf('\n', startingAt + 1) + 1;
            }
        }
        if (startingAt == -1) {
            return string;
        }
        return string.substring(0, startingAt);
    }
}
