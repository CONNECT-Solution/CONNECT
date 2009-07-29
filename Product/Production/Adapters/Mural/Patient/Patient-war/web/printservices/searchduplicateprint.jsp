<%-- 
    Document   : Duplicate Records Print From Print services
    Created on : July 28, 2008, 7:59:17 PM
    Author     : Narahari M
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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

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
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/printservices","");
boolean isSessionActive = true;
%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>
<%if (isSessionActive)  {%>

<%
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");%>
<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
%>
<html>
<head>
 <title> <%=screenObject.getDisplayTitle()%> </title>
 <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen, print">
</head>
<body>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   


<%
MasterControllerService masterControllerService = new MasterControllerService();
SearchDuplicatesHandler searchDuplicatesHandler = new SearchDuplicatesHandler();

Enumeration parameterNames = request.getParameterNames();
Enumeration parameterNamesResolve = request.getParameterNames();

Operations operations = new Operations();
//Map to hold the validation Errors
HashMap valiadtions = new HashMap();

EDMValidation edmValidation = new EDMValidation();         


//List to hold the results ArrayList results = new ArrayList();
ArrayList finalArrayList = new ArrayList();

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

ArrayList fullFieldNamesList  = new ArrayList();


//Variables required compare euids
String compareEuids = request.getParameter("compareEuids");
boolean iscompareEuids = (null == compareEuids?false:true);

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
%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
  HashMap newHashMap = new HashMap();
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
    HashMap eoMultiMergePreviewHashMap = new HashMap();

%>                
 

<% //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("lidmask".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);		
			 newHashMap.put(attributeName,attributeValue);
      }
   } 
	  

%>

<table>
<tr>
<%
	
  if(searchDuplicatesHandler.getParametersMap().get("selectedSearchType") != null) {
	//set the selected search type here....
	 searchDuplicatesHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));
%>
<td>
	  <span><%=bundle.getString("search_Type")%>:&nbsp;<b><%=searchDuplicatesHandler.getParametersMap().get("selectedSearchType")%></b></span>
</td>
<%}%>

<%
 
 //Final duplicates array list here
 finalArrayList = searchDuplicatesHandler.performSubmit();

%>

<% if (finalArrayList != null)   {
%>

<!------Sridhar -->

		
<%
	
  ArrayList screenConfigArrayLocal = searchDuplicatesHandler.getScreenConfigArray();
	 	  
%>

 <%
String strVal = new String();
  for (Iterator it = screenConfigArrayLocal.iterator(); it.hasNext();) {
	  	 

	  %>
	  
	      <td>

	  <%
		 
     FieldConfig fieldConfig = (FieldConfig) it.next();
 	   
 	 String   value = (fieldConfig.isRange()) ? (String) newHashMap.get(fieldConfig.getDisplayName()):(String) newHashMap.get(fieldConfig.getName());
	  
	 %>

	 <%
     if(value != null && value.length() > 0) {
	
       if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0)) {
    
        //SET THE VALUES WITH USER CODES AND VALUE LIST 
        if (fieldConfig.getUserCode() != null) {
	
               strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
        } else {
	
              strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
        }
    %>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=strVal%></b>&nbsp;</span>
       
   <% } else {%>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=value%></b>&nbsp;</span>
   <%}%>
   <%}%> 
   </td>
  <% }%>
  </tr>
  </table>
        <table align="right">
		 <tr>
		   <td>
			 <em>
               <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
			   &nbsp;
			   <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
               &nbsp;			   
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
			  </em>
           </td>
		 </tr>
	   </table>

<!-- Sridhar -->

  
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="../images/spacer.gif" width="15"></td>
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
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
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
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                             <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
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
													     <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(potDupStatusText)%> </font></td></tr>
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
     												<tr><td>&nbsp;</td></tr>
                                              </table>

											  </div>
                                            </div>   
                                        </td>
									
                                        <%} else {%> <!--For duplicates here-->  

                                            <%if (j ==1 && arlInner.size() > 3 ) { %>
                                            <!--Sri-->
                                            <td>
                                                 <div>
                                                     <table>
                                                         <tr>
                                            <%}%>
                                        
                                           <td valign="top">

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;width:169px;">
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
                                                              <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
	 
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
                                                        epathValue = fieldConfigMap.getFullFieldName();    
														%>
														<% if (ifc == 0) {%>
													     <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(potDupStatusText)%> </font></td></tr>
													<% } %>

                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
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

                                        <%}%>
                                      <td  valign="top">
									  <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>

									  <%} else {%>

									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();
											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
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

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
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

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
														 <%}%>
														 </div>

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                        

                                            </table>
                                      </div>  
                                    </div>
                                    
                           </tr>
                        </table>
                    </div> 

                         <%}%> <!--final Array list count loop -->
                         <%}%> <!-- final Array list  condition in session-->
               </div> 
               <%}%>  
               <%
                   if (finalArrayList != null && finalArrayList.size() == 0) {
               %>
               <div class="printClass">
                       <table cellpadding="0" cellspacing="0" border="0">
                         <tr>
                             <td>
                                 <h:outputText value="#{msgs.total_records_text}"/><%=finalArrayList.size()%>&nbsp;
                             </td>
                         </tr>
                       </table>
               </div>
               <%}%>
               
            </div>
            
         <table align="right">
		 <tr>
		   <td>
			 <em>
               <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
			   &nbsp;
			   <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
               &nbsp;			   
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
			  </em>
           </td>
		 </tr>
	   </table>


<% } else { %> <!-- End results!= null -->
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

	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

 
  <%} %>  <!-- Session check -->
</body>
</html>
</f:view>
