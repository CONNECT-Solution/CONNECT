<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LoginHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>

<%@ page import="javax.faces.context.FacesContext"  %>

<f:view>    
   <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
    <html>
<% 
LoginHandler loginHandler = new LoginHandler();
if (request.getAttribute(LoginHandler.FAIL_INITIALIZATION) == null  && request.getAttribute("Logout") == null && request.getRemoteUser() != null && request.isUserInRole("MasterIndex.Admin")) {
    FacesContext.getCurrentInstance().getExternalContext().redirect("results.jsf");
}
%>        
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <LINK REL="STYLESHEET" HREF="./css/styles.css"  TYPE="text/css">
        </head>
<script language="JavaScript">
    function submitAction() {
		document.getElementById("loading").style.visibility = 'visible';
		document.getElementById("loading").style.display = 'block';
        document.loginform.submit();
    }

</script>        
        <body>         
            <center> 
                <div id="mainContent">
                <h:form id="localeForm">
                   <div id="localeDiv" style="padding-left:900px;">
                        <table width="300px" border="0">
                            <tr>
                                <td>
                                    <h:outputText style="font-size:10px;" value="#{msgs.locale_choose_lang}" />&nbsp;
                                    <h:selectOneMenu  id="langOption"  
                                                      value="#{LocaleHandler.langOption}"  
                                                      onchange="submit();"
                                                      valueChangeListener="#{LocaleHandler.localeChanged}"
                                                      style="font-size:10px;">
                                        <f:selectItem  itemValue="English" itemLabel="#{msgs.locale_english_text}" />
                                        <f:selectItem  itemValue="German" itemLabel="#{msgs.locale_german_text}"/>
                                        <f:selectItem  itemValue="France" itemLabel="#{msgs.locale_french_text}"/>
                                        <f:selectItem  itemValue="Japanese" itemLabel="#{msgs.locale_japanese_text}"/>
                                        <f:selectItem  itemValue="Chinese" itemLabel="#{msgs.locale_chinese_text}"/>
                                    </h:selectOneMenu>                                                       
                                    
                                </td>
                            </tr>
                        </table>
                    </div>
                </h:form>
            <div class="blueline">&nbsp;</div>
            
            <div>                         
                    <div id="log" class="loginForm">
                            <f:verbatim>
                            <form name="loginform" id ="formid" method="POST" action="j_security_check" focus="j_username">
                            </f:verbatim>
                            <table border="0" cellpadding="0" cellspacing="0">
                                
                                <tbody>
                                    <tr>
                                        <!--alt=Enterprise Data Manager-->                                    
                                        <td colspan="2"><img src='images/spacer.gif' alt="Master Index Data Manager" title="Master Index Data Manager" height='120px'></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h:outputText style="font-size:13px;" value="#{msgs.login_user_name_prompt}" />
                                        </td>
                                        
                                        <td colspan="1">
                                            
                                            <input type="text" name="j_username"  onkeyup=""  size="10"/>
                                        </td>
                                        
                                    </tr>
                                    <tr>
                                        
                                        
                                        <td>
                                            <h:outputText style="font-size:13px;" value="#{msgs.login_password_prompt}" />
                                        </td>
                                        <td colspan="1"> 
                                            
                                            <input type="password" name="j_password" onkeyup="" size="10" redisplay="false"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">                                          
                                            <a title="<h:outputText value="#{msgs.header_login_prompt}"/>" href="javascript:submitAction();" class="button"><span><h:outputText value="#{msgs.header_login_prompt}"/></span></a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <!--alt=Sun Microsystems Logo-->
                                        <td colspan="2"><img src='images/spacer.gif' alt="Sun Microsystems Logo" title="Sun Microsystems" height='75px'></td>
                                    </tr>
                            </tbody>
                        </table>
                        <f:verbatim>
                              </form>
                        </f:verbatim>
                    </div>
                    <div>
                        <table border="0">
                            <tr>
                                <td>
                               <%if (request.getAttribute("Logout") == null && request.getRemoteUser() != null && !request.isUserInRole("MasterIndex.Admin")) { %>
                                 Please check the user credentials.
							   <%}%>
                                </td>
                                <td><img src='images/spacer.gif' alt="" width='35px'></td>
                            </tr>
                        </table>
                    </div>
					<div id="loading" style="visibility:hidden;display:none;">
                         <table border="0">
                            <tr>
                                <td><img src='images/loading.gif' alt="Loading..."/> Loading.... Please Wait.</td>
                            </tr>
                    </div>
            </div>
            </div>
            </center>
    <script>
         if( document.loginform.elements[0]!=null) {
		var i;
		var max = document.loginform.length;
		for( i = 0; i < max; i++ ) {
			if( document.loginform.elements[ i ].type != "hidden" &&
				!document.loginform.elements[ i ].disabled &&
				!document.loginform.elements[ i ].readOnly ) {
				document.loginform.elements[ i ].focus();
				break;
			}
		}
      }         
         
    </script>
<script language="JavaScript">
document.onkeyup = alertkey; 
function alertkey(e) { 
if( !e ) { 
if( window.event ) { 
//DOM 
e = window.event; 
} else { 
//TOTAL FAILURE, WE HAVE NO WAY OF REFERENCING THE EVENT 
return; 
} 
} 
if( typeof( e.which ) == 'number' ) { 
//NS 4, NS 6+, Mozilla 0.9+, Opera 
e = e.which; 
} else if( typeof( e.keyCode ) == 'number' ) { 
//IE, NS 6+, Mozilla 0.9+ 
e = e.keyCode;
} else if( typeof( e.charCode ) == 'number' ) { 
//also NS 6+, Mozilla 0.9+ 
e = e.charCode; 
} else { 
//TOTAL FAILURE, WE HAVE NO WAY OF OBTAINING THE KEY CODE 
return; 
} 

 if(e=='13')
	{
    document.getElementById("loading").style.visibility = 'visible';
	document.getElementById("loading").style.display = 'block';
 	document.getElementById("formid").submit();
	}

} 
</script>        

		   
        </body>
    </html> 
</f:view>
