<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.TransactionHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.TreeMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>

<script>
	function changecolor(v)   {
				 if (v.style.backgroundColor == "#696969")   {
					 v.style.backgroundColor="#e7e7d6";
				 } else {
					 v.style.backgroundColor="#696969";
				 }
	}
  
  // variable used to fix the view source/history issues
  var euidArray = [];

</script>

<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
  URI = URI.substring(1, URI.lastIndexOf("/"));
  
  TreeMap detailsArray = (session.getAttribute("transdetails") != null)?(TreeMap)session.getAttribute("transdetails"):new TreeMap();
  String [] transactionIds = (detailsArray.keySet().toString()).split(",");
  String [] functions = new String[transactionIds.length];

  for (int i =0;i<transactionIds.length;i++)   {	    
	    transactionIds[i] = transactionIds[i].trim();
		if (i == 0 ) 
			transactionIds[i] = transactionIds[i].substring(1,transactionIds[i].length());
		if (i == transactionIds.length -1 )
			transactionIds[i] = transactionIds[i].substring(0,transactionIds[i].length()-1);
		
		functions[i] = (String)detailsArray.get(transactionIds[i]);
  }
 %>

<!-- Modify By : M.Narahari on 29/07/2008
     Description : modified thisIdx to set the initial navigation
 -->

<%
 String transactionId = (String) (request.getParameter("transactionId")==null?request.getAttribute("transactionId"):request.getParameter("transactionId"));
 String function  = (request.getParameter("function") != null)? request.getParameter("function") : "Update";
%>

 <script>
var pages =[];
var functions =[];
var functionDesc =[];
var thisIdx=0;
  <%for (int i=0; i<transactionIds.length;i++)    { %>
         pages.push("<%=transactionIds[i]%>");
         functions.push("<%=functions[i]%>");
         functionDesc.push("<%=ValidationService.getInstance().getDescription(ValidationService.CONFIG_MODULE_FUNCTION, functions[i])%>");
 		 <% if(transactionIds[i].equalsIgnoreCase(transactionId)){%>
         thisIdx = <%=i%>;
		<%}%>

   <% }%>
</script>
								

<f:view> 
  
    <html>
        <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
             
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/balloontip.js"></script>
            <!-- Required for View Merge Tree -->
            <script type="text/javascript" src="scripts/yui4jsf/yahoo/yahoo-min.js"></script>           
            <!-- Additional source files go here -->
            <script type="text/javascript" src="scripts/yui4jsf/core/yui4jsf-core.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/treeview/treeview-min.js"></script>
            
            <script type="text/javascript" src="scripts/yui4jsf/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/animation/animation-min.js"></script>                        
            <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>  
			<script>
		     var euidValueArray=[];
              var editIndexid = "-1";
              function setEOEditIndex(editIndex)   {
				editIndexid = editIndex;
	    	  }
			</script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title> 
        <body>		
          <%@include file="./templates/header.jsp"%>		
            <div id="mainContent">
                    <div id="header" class="detailedresults">
			            <table>			 
                              <tr>
                                <th align="left" class="euidHeadMessage">
                                     <b> <%=bundle.getString("transaction_details_title")%></b>
                                 </th>
								 <!-- added by Narahari.M on 22/08/2008 for incorporate back button -->
								 <% 
 								    String pageName = request.getParameter("fromUrl");
								    String previousQuery = request.getQueryString();
									String URL= pageName+"?"+previousQuery+"&back=true";
								 %>
								 <td>
			               		      <a  href="<%=URL%>"  class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>">
									  <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					                  </a>
                                 </td>
                               </tr>               
                              <tr>
                                <td align="center" colspan="2" style="align:center;text-align:center;color:green;"><div id="messages"></div></td>
                               </tr>               							   
			                   <tr>
				                  <td valign="top" colspan="2">
                                      <div id="mainDupSource" class="duplicateresults"></div>                                       
				                  </td>
				               </tr>
			             </table>
                    </div>
			</div>    			

			   <%if(request.getParameter("transactionId") != null && request.getParameter("function") != null ) {%>
             	<script>
                	  ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?'+'&rand=<%=rand%>&transactionId=<%=request.getParameter("transactionId")%>&function=<%=request.getParameter("function")%>','mainDupSource','');
              	</script>
              <%}%>
                 
                       <div id="unmergePopupDiv" class="confirmPreview" style="VISIBILITY: hidden;">
                              <table cellpadding="0" cellspacing="0" border="0">
                                <form id="unmergeForm">
                                  <input type="hidden" id="unmergeTransactionId" name="unmergeTransactionId" title="unmergeTransactionId"  />
                                  <input type="hidden" id="mainEuid" name="mainEuid" title="mainEuid"  />
									<tr>
									    <th><%=bundle.getString("pop_up_confirmation_heading")%></th>
									</tr>
                                    <tr>
									  <td colspan="2">
									     <table>
										    <tr>
											   <td>
										          <h:outputText value="#{msgs.unmerge_tran_popup_content_text}" /> <%=bundle.getString("merged_euid_label")%><div id="unmergeEuid"></div>?
											  </td>
											 </tr>
										 </table>
									  </td>
									</tr>
									<tr><td colspan="2">&nbsp;</td></tr>
                                    <tr><td colspan="2" align="center">
									   <table border="0" cellpadding="0" cellspacing="0">
									      <tr>
										    <td align="right">
                                            <a href="javascript:void(0)" 
                                               onclick="javascript:getFormValues('unmergeForm');showExtraDivs('unmergePopupDiv',event);ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?'+queryStr+'&unMergeFinal=true&rand=<%=rand%>','mainDupSource','');document.getElementById('unmergePopupDiv').style.visibility='hidden';document.getElementById('unmergePopupDiv').style.display='none';" 
											   class="button" title="<h:outputText value="#{msgs.ok_text_button}" />">
                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                            </a>   
											</td>
										    <td>
                                            <a href="javascript:void(0)" onclick="javascript:showExtraDivs('unmergePopupDiv',event);ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?'+queryStr+'&cancelUnmerge=true&rand=<%=rand%>','mainDupSource','');"  class="button" title="<h:outputText value="#{msgs.cancel_but_text}" />">
                                                <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                            </a>   
											</td>
										  </tr>
										</table>
                                    </td></tr>
                                </form>
                            </table>
                        </div> 
                       
         <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>      

         <script type="text/javascript">
          makeDraggable("unmergePopupDiv");
        </script>

		</body>
    </html>
    </f:view>
    
