<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2008 Sun Microsystems Inc. All Rights Reserved
  
   The contents of this file are subject to the terms
   of the Common Development and Distribution License
   (the License). You may not use this file except in
   compliance with the License.

   You can obtain a copy of the License at
   https://opensso.dev.java.net/public/CDDLv1.0.html or
   opensso/legal/CDDLv1.0.txt
   See the License for the specific language governing
   permission and limitations under the License.

   When distributing Covered Code, include this CDDL
   Header Notice in each file and include the License file
   at opensso/legal/CDDLv1.0.txt.
   If applicable, add the following below the CDDL Header,
   with the fields enclosed by brackets [] replaced by
   your own identifying information:
   "Portions Copyrighted [year] [name of copyright owner]"

   $Id: FileUpload.jsp,v 1.3 2008/06/25 05:44:35 qcheng Exp $

--%>

<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="java.util.*" %>

<%
    InputStream is = null;

    try {
        StringBuffer buff = new StringBuffer();
        is = request.getInputStream();
        BufferedReader bos = new BufferedReader(new InputStreamReader(is));
        String line = bos.readLine();
        while (line != null) {
            buff.append(line).append("\n");
            line = bos.readLine();
        }
        String data = buff.toString();
        int idx = data.indexOf("filename=\"");
        idx = data.indexOf("\n\n", idx);
        data = data.substring(idx+2);
        idx = data.lastIndexOf("\n-----------------");
        data = data.substring(0, idx);
        data = data.replace("<", "&lt;");
        data = data.replace(">", "&gt;");
        out.println("<div id=\"data\">" + data + "</div>");
    } catch (IOException e) {
    } finally {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }
%>
