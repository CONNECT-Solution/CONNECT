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

   $Id: AjaxProxy.jsp,v 1.5 2009/01/09 17:42:54 veiming Exp $

--%>

<%@page import="com.sun.identity.workflow.ITask" %>
<%@page import="com.sun.identity.workflow.WorkflowException" %>
<%@page import="java.util.*" %>

<%
    request.setCharacterEncoding("UTF-8");
    String locale = request.getParameter("locale");
    Locale resLocale = null;
    if ((locale != null) && (locale.length() > 0)) {
        StringTokenizer st = new StringTokenizer(locale, "|");
        int cnt = st.countTokens();
        if (cnt == 1) {
            resLocale = new Locale(st.nextToken());
        } else if (cnt == 2) {
            resLocale = new Locale(st.nextToken(), st.nextToken());
        } else {
            resLocale = new Locale(st.nextToken(), st.nextToken(),
                st.nextToken());
        }
    } else {
        resLocale = Locale.US;
    }

    String clazzName = request.getParameter("class");
    try {
        Class clazz = Class.forName(clazzName);
        ITask task = (ITask)clazz.newInstance();

        Map map = new HashMap();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();){
            String n = (String)e.nextElement();
            if (!n.equals("class") && !n.equals("locale")) {
                map.put(n, request.getParameter(n));
            }
        }

        map.put("_servlet_context_", getServletConfig().getServletContext());
        map.put("_request_", request);
        out.println("0|" + task.execute(resLocale, map));
    } catch (WorkflowException e) {
        out.write("1|" + e.getL10NMessage(resLocale));
    } catch (IllegalAccessException e) {
        out.write("1|" + e.getMessage());
    } catch (InstantiationException e) {
        out.write("1|" + e.getMessage());
    } catch (ClassNotFoundException e) {
        out.write("1|" + e.getMessage());
    }

%>
