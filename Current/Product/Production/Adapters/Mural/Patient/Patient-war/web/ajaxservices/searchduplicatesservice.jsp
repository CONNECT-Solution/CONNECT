<%-- 
    Document   : Duplicate Record Ajax services
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Rajani Kanth M
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ArrayList"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.math.BigDecimal"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="java.util.ResourceBundle"  %>

<f:view>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />


<%
String URI_Session = request.getRequestURI();URI_Session = URI_Session.substring(1, URI_Session.lastIndexOf("/"));
//remove the app name 
URI_Session = URI_Session.replaceAll("/ajaxservices","");
boolean isSessionActive = true;

%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI_Session%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>
<%if (isSessionActive)  {%>

<%
NavigationHandler navigationHandler = new NavigationHandler();

//set the screen object of the duplicate records
session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));

ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

MasterControllerService masterControllerService = new MasterControllerService();

String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
SearchDuplicatesHandler searchDuplicatesHandler = new SearchDuplicatesHandler();

Enumeration parameterNames = request.getParameterNames();
Enumeration parameterNamesResolve = request.getParameterNames();

Operations operations = new Operations();
//Map to hold the validation Errors
HashMap valiadtions = new HashMap();

EDMValidation edmValidation = new EDMValidation();         


//List to hold the results
ArrayList finalArrayList = (ArrayList) session.getAttribute("finalArrayList");

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

ArrayList fullFieldNamesList  = new ArrayList();


//Variables required compare euids
String compareEuids = request.getParameter("compareEuids");
boolean iscompareEuids  = (null == compareEuids?false:true);

boolean isEuidValueNotEntered = false;

//Variables required for compare euids
String collectedEuids = request.getParameter("collecteuid");

//resolveDuplicate

//Variables required for resolve Duplicate
String resolveDuplicate = request.getParameter("resolveDuplicate");
boolean isResolveDuplicate = (null == resolveDuplicate?false:true);

//unresolve Duplicate fields
String unresolveDuplicate = request.getParameter("unresolveDuplicate");
boolean isUnresolveDuplicate = (null == unresolveDuplicate?false:true);


//unresolve Duplicate fields
String previewMerge = request.getParameter("previewMerge");
boolean isPreviewMerge = (null == previewMerge?false:true);

//multi Merge EO fields
String multiMergeEOs = request.getParameter("multiMergeEOs");
boolean isMultiMergeEOs = (null == multiMergeEOs?false:true);

//cancel Multi Merge EOs operation fields  (cancelMultiMergeEOs)
String cancelMultiMergeEOs = request.getParameter("cancelMultiMergeEOs");
boolean isCancelMultiMergeEOs= (null == cancelMultiMergeEOs?false:true);

String keyParam = new String();
ArrayList collectedEuidsList = new ArrayList();
HashMap previewEuidsHashMap  = new HashMap();
String previousQuery=request.getQueryString(); //added by Narahari.M on 22/08/2008 for incorporate back button
%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>
 <%
                    ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
  
					PotentialDuplicateIterator asPdIter;
                    PotentialDuplicateSummary mainDuplicateSummary = null;
                    
                    PotentialDuplicateSummary associateDuplicateSummary = null;
                    int countMain = 0;
                    int totalMainDuplicates = 0;
                    int totalAssoDuplicates = 0;
                    String mainDob = null;
                    String assoDob = null;
                    
                    String mainEuidContentDiv = null ;
                    String assoEuidContentDiv = null ;
                    String previewEuidContentDiv = null ;
                    String dupHeading = "<b>Duplicate </b>";
                    Iterator searchResultFieldsIter = searchDuplicatesHandler.getResultsConfigArray().iterator();
                    Object[] resultsConfigFeilds  = searchDuplicatesHandler.getResultsConfigArray().toArray();
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer mainEUID = new StringBuffer();
                    StringBuffer dupEUID = new StringBuffer();
                    ValueExpression finalArrayListVE = null;
					ValueExpression potDupIdValueExpression = null;
					ValueExpression duplicateSearchObjectVE = null;
                    PotentialDuplicateSearchObject potentialDuplicateSearchObject = (PotentialDuplicateSearchObject) request.getAttribute("duplicateSearchObject");                                    
				     if(potentialDuplicateSearchObject != null) {
						   duplicateSearchObjectVE  = ExpressionFactory.newInstance().createValueExpression(potentialDuplicateSearchObject, potentialDuplicateSearchObject.getClass());
					}
					HashMap eoMultiMergePreviewHashMap = null;
					HashMap previewHashMap = new HashMap();
					HashMap eoMapPreview = new HashMap();
				 
%>                

<%if (iscompareEuids && request.getParameter("EUID") != null && request.getParameter("EUID").trim().length() == 0 ) {
	isEuidValueNotEntered = true;
	%>
   <div class="ajaxalert">
		   <table cellpadding="0" cellspacing="0" border="0">
			 <tr>
				 <td>
                    <b><%=bundle.getString("enter_euid_text")%> </b>
				 </td>
			 </tr>
		   </table>
   </div>
   <%}%>


<%if(isPreviewMerge) {%>  <!--if is Preview Merge-->
 <%
	 HashMap previewDuplicatesMap = new HashMap();
     String[] srcDestnEuids = request.getParameter("PREVIEW_SRC_DESTN_EUIDS").split(",");
     for(int i = 0 ; i < srcDestnEuids.length;i++ ) {
		previewEuidsHashMap.put(srcDestnEuids[i]+request.getParameter("rowCount"), srcDestnEuids[i]+request.getParameter("rowCount") );
	 }
 	  
     eoMultiMergePreviewHashMap = searchDuplicatesHandler.previewPostMultiMergedEnterpriseObject(srcDestnEuids,(String) request.getParameter("rowCount"));
    
%>
 
 <%if(eoMultiMergePreviewHashMap == null) {
	 finalArrayList = null;
	 
	 %>
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 
     <%=msgs%>
	 <script>
   		 euids="";
         euidArray = [];
         alleuidsArray = [];
		 previewEuidDivs = [];
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>
  <%} else {%>
     <table>
	   <tr>
	     <td>
  	 

	 <script>
		document.getElementById('MERGE_SRC_DESTN_EUIDS<%=request.getParameter("rowCount")%>').value="<%=request.getParameter("PREVIEW_SRC_DESTN_EUIDS")%>";
 		 euids="";
         euidArray = [];
         alleuidsArray = []; 
	 </script>
	   </td>
	   </tr>
	 <table>
 
  <%}%>


<%} else if(isCancelMultiMergeEOs) {
	//remove the final arraylist from the session
     session.removeAttribute("finalArrayList");

	%>  <!--if  Cancel Multi Merge EOs -->
 <table>
  <tr>
     <td>
	   <script>
	        getFormValues('advancedformData');
            ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','') ;
			euids="";
            euidArray = [];
            alleuidsArray = []; 
			previewEuidDivs = [];
      </script>
	 </td>
  </tr>
</table>
<%} else if(isMultiMergeEOs) {%>  <!--if MERGE EO's -->
 <%
	HashMap previewDuplicatesMap = new HashMap();
  
    String[] srcDestnEuids = request.getParameter("MERGE_SRC_DESTN_EUIDS").split(",");

 	  
    searchDuplicatesHandler.performMultiMergeEnterpriseObject(srcDestnEuids,request.getParameter("rowCount"));
   
    session.removeAttribute("finalArrayList");

    String finalEuids  = request.getParameter("MERGE_SRC_DESTN_EUIDS").replaceAll(srcDestnEuids[0]+"," , "");
    
%>
<table>
  <tr>
     <td>
	   <script>
 			euids="";
            euidArray = [];
            alleuidsArray = []; 
		    previewEuidDivs = [];

		    var messages = document.getElementById("messages");
			messages.className = "ajaxsuccess";
	        messages.innerHTML= '<font style="padding-top:100px;color:green;"><%=finalEuids%>  <%=bundle.getString("so_merge_confirm_text")%>  <%=srcDestnEuids[0]%></font>' ;		 
		

      </script>
	 </td>
  </tr>
</table>

<%} else if(isUnresolveDuplicate) {%>  <!--if Resolve Duplicate-->
  <%
	 HashMap resolveDuplicatesMap = new HashMap();
  //parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("potentialDuplicateId".equalsIgnoreCase(attributeName)) ||
		  ("resolveType".equalsIgnoreCase(attributeName)) ) { 
		   resolveDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   searchDuplicatesHandler.unresolvePotentialDuplicateAction(resolveDuplicatesMap);

   //remove the final arraylist from the session
   session.removeAttribute("finalArrayList");

    
%>
  <table>
  <tr>
     <td>
	   <script>
           euids="";
           euidArray = [];
           alleuidsArray = [];
		   previewEuidDivs = [];
      </script>
	 </td>
  </tr>
</table>
<%} else if(isResolveDuplicate) {%>  <!--if Resolve Duplicate-->
 <% HashMap resolveDuplicatesMap = new HashMap();
  
//parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("potentialDuplicateId".equalsIgnoreCase(attributeName)) ||
		  ("resolveType".equalsIgnoreCase(attributeName)) ) { 
		   resolveDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   searchDuplicatesHandler.resolvePotentialDuplicate(resolveDuplicatesMap);
  
  //remove the final arraylist from the session
   session.removeAttribute("finalArrayList");
    
%>

<script>
	 document.getElementById('resolvePopupDiv').style.visibility = 'hidden';
	 document.getElementById('resolvePopupDiv').style.display = 'none';
</script>
 
 <!-- redisplay the output here END-->
<table>
  <tr>
     <td>
	   <script>
            euids="";
           euidArray = [];
           alleuidsArray = [];
		   previewEuidDivs = [];

 		</script>
    </td>
    </tr>
</table>

<%}%>

 


<% //Build the request Map  to display the duplicates here.
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
      }
   } 

%>
<%if(!isMultiMergeEOs) {%>
<table>
<tr><td>
<script>
	   
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>
<%}%>

<%
 	//set the selected search type here....
	 if(request.getParameter("selectedSearchType") != null ) {
	   searchDuplicatesHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));
	 }
 
 //Final duplicates array list here
 finalArrayList = (isPreviewMerge) ? (ArrayList) session.getAttribute("finalArrayList"):(!isEuidValueNotEntered)?searchDuplicatesHandler.performSubmit():new ArrayList();
 Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
 
%>
<% if (!iscompareEuids && finalArrayList != null)   {%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" >
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
				    <% if (operations.isPotDup_Print()) { %>
                         <a class="button" title="<%=bundle.getString("print_text")%>" href="javascript:void(0)"
                                       onclick="javascript:getFormValues('advancedformData');setRand(Math.random());openPrintWindow('/<%=URI%>/printservices/searchduplicateprint.jsf?'+queryStr)">
                                       <span><h:outputText value="#{msgs.print_text}"/></span>
                         </a>            
					<%}%>
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>

<%if (!isEuidValueNotEntered && iscompareEuids && finalArrayList != null && finalArrayList.size() == 0) {%>
   <div class="ajaxalert">
		   <table cellpadding="0" cellspacing="0" border="0">
			 <tr>
				 <td>
				     <% String messages = "EUID '" + request.getParameter("EUID") + "' " + bundle.getString("euid_not_found_text"); %>     	 <b><%=messages%> </b>
				 </td>
			 </tr>
		   </table>
   </div>
   <%}%>
   

<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
<%if(iscompareEuids) {%>
   <%String finalEuidsString = searchDuplicatesHandler.buildDuplicateEuids(finalArrayList);%> 
    <table><tr><td>
    <script>
     window.location = '/<%=URI%>/compareduplicates.jsf?fromPage=duplicaterecords&duplicateEuids=<%=finalEuidsString%>';
   </script>
   </td>
   </tr>
   </table>

<%}%>

<%if(!isMultiMergeEOs) {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>
<%}%>



                <br>   
                 <%
                    if(!iscompareEuids && finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if( finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
											   String potDupStatusText = (potDupStatus != null) ? ValidationService.getInstance().getDescription("RESOLVETYPE", potDupStatus):"";

                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   
                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }

									         keyParam = "eoMultiMergePreview" + new Integer(fac).toString();

												
											       if(eoMultiMergePreviewHashMap != null && eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }
												   

										//Compare the surviving EUID if it is previewed
                                        if( eoMultiMergePreviewHashMap != null) {         
                                           resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue)); //Compare with other values 
										   resultArrayMapMain.put(epathValue, eoMapPreview.get(epathValue));//keep the surviving EO values to compare eoMapPreview
										   
										} else { //Compare with the main EUID only
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
										}

                                        
					                
                                               
                                        %>
										<script>alleuidsArray.push('<%=fieldValuesMapSource.get("EUID")%>:<%=fac%>');</script>
                                        <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >

                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                     %>
													<% if (ifc == 0) {%>
													     <tr><td><h:outputText value="#{msgs.source_rec_status_but}"/></td></tr>
													<% } %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top"> 
										   <%if(eoMultiMergePreviewHashMap != null && 
													 eoMultiMergePreviewHashMap.get((String) fieldValuesMapSource.get("EUID")+":"+Integer.toString(fac) ) != null){ %>
                                             <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="blue">
											<%}else {%>
                                              <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="yellow">
											<%}%>
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
														<%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();%>
                                                        <%if(eoMultiMergePreviewHashMap != null ){%>
 															<span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} else {%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>','<%=arlInner.size()%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
													<% if (ifc == 0) {%>
 														 <tr><td><%=potDupStatusText%></td></tr>
													<% } %>
 
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
                                            </div>   
                                        </td>
                                        <%} else {%> <!--For duplicates here-->  

                                            <%if (j ==1 && arlInner.size() > 3 ) { %>
                                            <!--Sri-->
                                            <td>
                                                 <div style="overflow:auto;width:507px;overflow-y:hidden;">
                                                     <table>
                                                         <tr>
                                            <%}%>
                                        
                                           <td valign="top">
                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>
                                                <%if(eoMultiMergePreviewHashMap != null && 
													 eoMultiMergePreviewHashMap.get((String) fieldValuesMapSource.get("EUID")+":"+Integer.toString(fac) ) != null){ %>
                                                  <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="blue" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
											  <%} else {%>
                                                  <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%>:<%=fac%><%=j%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
 											  <%}%>
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>  
													  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();%>

                                                        <%if(eoMultiMergePreviewHashMap != null){%>

                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} else {%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>"
														  onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>','<%=arlInner.size()%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
													<% if (ifc == 0) {%>
														 <tr><td><%=potDupStatusText%></td></tr>
													<% } %>
 
                                                    <tr>                                                        
                                                       <td> 
  
                                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                                    
                                                                                <div id="highlight<%=fieldValuesMapSource.get("EUID")%>:<%=epathValue%>" style="background-color:none;">
                                                                                  <%if (eoMultiMergePreviewHashMap != null) {%> <!-- if preview is then display the links-->
                                                                                 <%if (previewEuidsHashMap.get((String) fieldValuesMapSource.get("EUID")+ new Integer(fac).toString() ) != null) {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->

                                                                                    <%if ((resultArrayMapCompare.get(epathValue) != null  && resultArrayMapMain.get(epathValue) != null)  && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
 
                                                                                        <font class="highlight">
                                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                            <%} else {%> 
                                                                                            <%=fieldValuesMapSource.get(epathValue)%>
                                                                                            <%}%>
                                                                                        </font>
                                                                                   
                                                                                    <%} else {%>
 																					<%if(resultArrayMapMain.get(epathValue) == null) { %>
 																					  <font class="highlight">
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                         <%=fieldValuesMapSource.get(epathValue)%>
                                                                                       <%}%>
                                                                                     </font>
                                                                                   <%} else {%> 
                                                                                       <%if (!operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                       <%=fieldValuesMapSource.get(epathValue)%>
                                                                                      <%}%>
                                                                                    <%}%>
																				 <%}%>
                                                                       <%} else {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->
                                                                                       <%if (!operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                                      <%}%>

                                                                       <%}%>
                                                                              <%} else {%> <!--if not [preview -->
                                                                                    <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
                                                                                     <font class="highlight">
                                                                                        <%if (!operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                          <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%} else {%> 
                                                                                          <%=fieldValuesMapSource.get(epathValue)%>
                                                                                        <%}%>
                                                                                    </font>
                                                                                    <%} else {%>

 																					<%if(resultArrayMapMain.get(epathValue) == null) { %>
 																					    <font class="highlight">
																				          <%if(!operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%> 
																					        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                          <%}else{%>
                                                                                           <%=fieldValuesMapSource.get(epathValue)%>
                                                                                        <%}%>
                                                                                       </font>
                                                                                     <%} else {%> 
                                                                                       <%if (!operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                       <%=fieldValuesMapSource.get(epathValue)%>
                                                                                      <%}%>
                                                                                    <%}%> 


                                                                                    <%}%>
                                                                                        
                                                                                    <%}%>
                                                                                        
                                                                                </div>
                                                                                    
                                                                                <%} else {%> <!-- Not null condition  -->
                                                                                    <%if (eoMultiMergePreviewHashMap != null) {%> <!-- if preview is then display the links-->
                                                                                        <%if (previewEuidsHashMap.get((String) fieldValuesMapSource.get("EUID")) != null) {%>
 		                                                                                <%if(eoMapPreview.get(epathValue) != null) { %> 
                                                                                                 <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                                 </font>
  																						<%}%>
                                                                                        <%} else {%>
                                                                                        &nbsp;
                                                                                        <%}%>
                                                                                    <%} else {%>		
                                                                                      <%if(resultArrayMapMain.get(epathValue) != null ) { %>
                                                                                       <font class="highlight">
 																						   <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                       </font>
																					 <%}else {%>
                                                                                        &nbsp; 
                                                                                     <%}%>

																					<%}%>
                                                                                <%}%>



                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
                                                  <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

												        <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                         <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
												   <%}%>

												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                            <%if (arlInner.size() > 3 && j == (arlInner.size()-1 )) { %>
                                            <!--Raj-->
                                                       </tr>
                                                     </table>
                                                 </div>
                                                </td>
                                            <%}%>
                                        
                                        <%}%>
                                        <td class="w7yelbg">&nbsp;</td><!--Separator b/n columns-->
                                        <%}%>
                                      <td  valign="top">
									  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();%>

									  <%if(eoMultiMergePreviewHashMap != null && eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1"><h:outputText value="#{msgs.preview_column_text}"/></td>
                                              </tr>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap != null && eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>
                                         			<% if (i == 0) {%>
													     <tr><td>&nbsp;</td></tr>
													<% } %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap != null && eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													  <nobr>
													     <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:getFormValues('previewForm<%=fac%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&rowCount=<%=fac%>&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>


														 <%}%>
														  <form id="previewForm<%=fac%>">
                                                            <input type="hidden" id="PREVIEW_SRC_DESTN_EUIDS<%=fac%>" title="PREVIEW_SRC_DESTN_EUIDS"/>
                                                            <input type="hidden" id="PREVIEW_DIVS<%=fac%>" title="PREVIEW_DIVS"/>
														 </form>
														 </div>

														  <form id="multiMergeFinal<%=fac%>" name="multiMergeFinal<%=fac%>">
														    <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
															  if(eoMultiMergePreviewHashMap != null && eoMultiMergePreviewHashMap.get(keyParam)  != null ) {
                                                            %>
                                                       	     <%if(operations.isEO_Merge()) {%>
                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.source_submenu_merge}"/>"
															   onclick="javascript:getDuplicateFormValues('multiMergeFinal<%=fac%>','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?multiMergeEOs=true&rowCount=<%=fac%>&random='+rand+'&'+queryStr,'outputdiv','');">
                                                       	         <span><h:outputText value="#{msgs.source_submenu_merge}"/></span>
                                                       	       </a>
                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                        	     <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
                                                       	     <%}%>

															<%}%>
															<input type="hidden" id="MERGE_SRC_DESTN_EUIDS<%=fac%>" title="MERGE_SRC_DESTN_EUIDS"/>
                                                            </form>   


													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                                <a href="compareduplicates.jsf?fromPage=duplicaterecords&duplicateEuids=<%=finalEuidsString%>&previousQuery=<%=previousQuery%>&fromUrl=duplicaterecords.jsf" 
															  class="downlink"  title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>" 
															  >   
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
                                      </div>  
                                    </div>
                                    
                           </tr>
                        </table>
                    </div> 
                    <div id="separator"  class="sep"></div>
                         <%}%> <!--final Array list count loop -->
                         <%}%> <!-- final Array list  condition in session-->
               </div> 
               <%}%>  
               
            </div>
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td> 
	 
		 <%
			  StringBuffer msgs = new StringBuffer("<ul>");	
		
			  while (messagesIter.hasNext()) {
 						 FacesMessage facesMessage = (FacesMessage) messagesIter.next();
						 msgs.append("<li>");
						 msgs.append(facesMessage.getSummary());
						 msgs.append("</li>");
			  }
			  msgs.append("</ul>");		  
		 %>     	 

		 <script>
			 var messages = document.getElementById("messages");
			 messages.innerHTML= "<%=msgs%>";
		 </script>
	    </td>
	   </tr>
	 <table>
	 </div>
 <% } %>


  <%} %>  <!-- Session check -->
</f:view>
