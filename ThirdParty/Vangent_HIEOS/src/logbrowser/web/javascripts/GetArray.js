/***** Fonctions used to get an array of messages from Postgresql Database  *****/

var timeref = 1000;
var currentMessage = "" ;
var currentColor   = "" ;
var rowsNumber          ;
var arrayMessagesToDelete     ;
var checkBoxSelected          ;
var ipOfClient    = ""        ;
var ipChanged = false ;
var reqMessageArray ;
function performAdvancedSearch()
{

    var optionsArray =new Array() ;
 
  
    optionsArray[0] = "page"       ;
    optionsArray[1] = currentPage  ;
 
    optionsArray[2] = "nbResByPage";
    optionsArray[3] = nbByPage      ;

    optionsArray[4] = "optionsNumber" ;
    optionsArray[5] = 0               ;
 
    if ( $("ip").value!="" )
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
 	
        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "ip" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] = $("ip").value ;
    }

    if ( $("pass").value!="" )
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
 	
        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "pass" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] =  $("pass").value ;
    }
    if ( trim($("test").value) !="" )
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
 	
        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "test" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] =  $("test").value ;
    }
    if (   trim ( $("company").value ) !="" )
    {
     
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
 	
        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "company" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] = $("company").value ;
    }
  
    if ( date1 != "0" )
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1)
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
        else
        {
            optionsArray[5] = 1 ;
        }

        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "date1" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] = date1 ;
  	  
    }
    if ( date2 != "0" )
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
        else
        {
            optionsArray[5] = 1 ;
        }

        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "date2" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] = date2 ;
    }
    if ( date != "0")
    {
        optionsArray[5]++ ;
        if ( optionsArray[5] > 1 )
        {
            optionsArray[optionsArray.length] = "and-or"+ optionsArray[5]  ;
            optionsArray[optionsArray.length] = "and"  ;
        }
        else
        {
            optionsArray[5] = 1 ;
        }
  
        optionsArray[optionsArray.length] = "option" + optionsArray[5] ;
        optionsArray[optionsArray.length] = "date" ;
        optionsArray[optionsArray.length] = "value"+ optionsArray[5]  ;
        optionsArray[optionsArray.length] = date ;
    }
 
    if ( ipOfClient !="" && ipChanged )
    {
        optionsArray[5] ++  ;
 	
        if  ( optionsArray[5]>1 )
        {
            optionsArray[optionsArray.length] = "and-or"+optionsArray[5] ;
            optionsArray[optionsArray.length] = "and" ;
        }
        optionsArray[optionsArray.length] = "option"+optionsArray[5] ;
        optionsArray[optionsArray.length] = "ip" ;
        optionsArray[optionsArray.length] = "value"+optionsArray[5] ;
        optionsArray[optionsArray.length] = ipOfClient ;
   
    }
 
    ptrFuncGetMessageArray(  optionsArray  ) ;
}

function getDate( )
{

    var req = newXMLHttpRequest();
    var handlerFunction = getReadyStateHandler(req, getDateResult );
    req.onreadystatechange = handlerFunction ;
  
    req.open("Get", "GetDateServlet", true);
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    req.send( null  );
}

function getDateResult( resultXML )
{
    var select =  resultXML.getElementsByTagName("select")[0]  ;
    var optionNodes = select.getElementsByTagName("option") ;
    var nodeValue  ="";
    var attributeValue = "" ;
    var innerHTMLString = "" ;
    for ( var i = 0 ; i < optionNodes.length; i++ )
    {
        if (optionNodes[i].childNodes[0]!=null)
        {
            nodeValue = optionNodes[i].childNodes[0].nodeValue   ;
        }
        else
        {
            nodeValue = "" ;
        }
        attributeValue = optionNodes[i].getAttribute("value");
		
        innerHTMLString += "<option value='"+attributeValue +"'>"+ nodeValue +"</option>" ;
    }
 
    $('select1').innerHTML = "<select id='date1'  onchange='changeDateSearchType(2);'>"+ innerHTMLString + "</select>" ;
    $('select2').innerHTML = "<select id='date2'  onchange='changeDateSearchType(2);'>"+ innerHTMLString + "</select>" ;

}

function getCompanyList( )
{

    var req = newXMLHttpRequest();
    var handlerFunction = getReadyStateHandler(req, getCompanyListResult );
    req.onreadystatechange = handlerFunction ;
  
    req.open("Get", "GetCompanyServlet?option=all", true);
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    req.send( null  );
}

function  getCompanyListResult( resultXML )
{
    var result =  resultXML.getElementsByTagName("result")[0]  ;
    var optionNodes =  result.getElementsByTagName("company") ;
    var nodeValue  ="";
    var attributeValue = "" ;
    var innerHTMLString =  "<option value=''></option>" ;;
    for ( var i = 0 ; i < optionNodes.length; i++ )
    {
        if (optionNodes[i].childNodes[0]!=null)
        {
            nodeValue = optionNodes[i].childNodes[0].nodeValue   ;
        }
        else
        {
            nodeValue = "" ;
        }
		 
		
        innerHTMLString += "<option value='"+ nodeValue +"'>"+ nodeValue   +"</option>" ;
    }
 
    $('companyDiv').innerHTML = "<select id='company' width='250' style='width: 250px'>"+ innerHTMLString + "</select>" ;

}
function getTestList( ip )
{
 
    var req = newXMLHttpRequest();
    var handlerFunction = getReadyStateHandler(req, getTestListResult );
    req.onreadystatechange = handlerFunction ;
   
    if ( ip == null || ip == "" )
        req.open("Get", "GetTestNameServlet", true);
    else
        req.open("Get", "GetTestNameServlet?ip="+ip , true);
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    req.send( null  );
}

function  getTestListResult( resultXML )
{
    var result =  resultXML.getElementsByTagName("result")[0]  ;
    var optionNodes =  result.getElementsByTagName("test") ;
    var nodeValue  ="";
    var attributeValue = "" ;
    var innerHTMLString =  "<option value=''></option>" ;;
    for ( var i = 0 ; i < optionNodes.length; i++ )
    {
        if (optionNodes[i].childNodes[0]!=null)
        {
            nodeValue = optionNodes[i].childNodes[0].nodeValue  ;
        }
        else
        {
            nodeValue = "" ;
        }
        innerHTMLString += "<option value='"+ nodeValue +"'>"+ nodeValue  +"</option>" ;
    }
 
    $('TestDiv').innerHTML = "<select id='test' width='250' style='width: 250px'>"+ innerHTMLString + "</select>" ;

}

function getAllMessages()
{


    date1 = "0" ;
    date2 = "0" ;
    date  = "0" ;


    $('date1').value = "0" ;
    $('date2').value = "0" ;
    $('date').value  = "0" ;
  
    var optionsArray = new Array() ;
 
    optionsArray[0] = "page";
    optionsArray[1] = "0";
    optionsArray[2] = "nbResByPage";
    optionsArray[3] = nbByPage ;
    optionsArray[4] = "optionsNumber" ;
    optionsArray[5] = 0            ;

 
    ptrFuncGetMessageArray(  optionsArray  ) ;

}


function getMessageArray(  optionsArray ) {

    $('loading').style.display = "block";
  
    // Obtain an XMLHttpRequest instance
    reqMessageArray = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(reqMessageArray, parseTableResultJSon );
    reqMessageArray.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    reqMessageArray.open("POST", "GetTableServlet", true);

    // Specify that the body of the request contains form data
    reqMessageArray.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
    var parameterToSend ="" ;

    if ( optionsArray != null && optionsArray.length%2==0 )
    {
        $("sqlCommand").innerHTML ="" ;
        for ( var i = 0 ; i < optionsArray.length ; i++ )
        {
            $("sqlCommand").innerHTML += "<br />" + optionsArray[i] + " : " ;
            parameterToSend += "&"+optionsArray[i++]+"="+escape( optionsArray[i] ) ;
            $("sqlCommand").innerHTML +=  optionsArray[i] + "<br />" ;
        }
    }

    reqMessageArray.send( parameterToSend  );
    $("sqlCommand").innerHTML += parameterToSend ;
 
}

function sortCol(columnNumber)
{
    // Obtain an XMLHttpRequest instance
    reqMessageArray = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(reqMessageArray, parseTableResultJSon );
    reqMessageArray.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    reqMessageArray.open("POST", "GetTableServlet", true);

    // Specify that the body of the request contains form data
    reqMessageArray.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");

    // Send form encoded data stating that I want to add the
    // specified item to the cart.
    var parameterToSend = "sort=" + columnNumber ;
    reqMessageArray.send( parameterToSend  );
}

function parseTableResultJSon(  ) 
{ 
  
    var toDisplay                    = "";
 
    if ( reqMessageArray.responseText != null && reqMessageArray.responseText!=""  )
    {
        var response = eval("(" +reqMessageArray.responseText + ")") ;
	  
        var response                       = response.result ;
        var ipColumnNumber               = -1 ;
        var ipColumnNumberStringToSearch = "ip" ;
	 
        var isAdminS                     = response.isAdmin ;
	  
        if ( isAdminS == "true" ) isAdmin = true ;
        else if ( isAdminS == "false" ) isAdmin = false ;
        if ( !isAdmin )
        {
            $('status').innerHTML = "" ;
        }
	  
        Element.hide('TableFrame');
	  
        var mytable     = response.table ;
	
        if ( mytable!=null )
        {
	
            var headerTable  = mytable[0] ;
            var columnNumber = mytable[0].length ;
            toDisplay += "<div id ='selectPage'  >" ;
	
            toDisplay += "</div><br />";
            toDisplay +="<div id='legendContainer' >Color code : " +
            "<ul id='legend'>" +
            "<li class='columnTrue'>Test passed</li>" +
            "<li class='columnFalse'>Test failed</li>" +
            "</ul>" +
            " </div>" ;
            if ( !isAdmin && ipOfClient!=null && ipOfClient!="" )
                toDisplay += "<div id=\"resultForIP\">Results for IP <a onclick='showIpBox();$(\"ipFilterInput\").focus();'>" + ipOfClient +"</a></div>" ;
	
            toDisplay +="<center>\n<div id='TableData'>\n" +
            "\t<table id='TableResult'>\n" ;
			
            toDisplay +="\t\t<tr>" ;
            for( var i = 1; i< columnNumber ; i++ )
            {
	
	
                if ( mytable[0][i].indexOf("is_secure")!=-1 )
                {
                    toDisplay +="\t\t\t<td style='width:auto;border-left : solid black 1px ;border-right : solid black 1px ;' >\n" ;
                    toDisplay +="";
                    toDisplay +="\t\t\t</td>\n" ;
                }
                else if( mytable[0][i].indexOf("ip")!=-1 )
                {
	  	
                }
                else if( mytable[0][i].indexOf("pass")!=-1 )
                {
	  	
                }
                else if( mytable[0][i].indexOf("Company Name or IP")!=-1 )
                {
	   
                    if ( isAdmin  )
                    {
                        toDisplay +="\t\t\t<td style='width:auto;border-left : solid black 1px ;border-right : solid black 1px ;' >\n" ;
                        toDisplay += "\t\t\t\t<a href=\"#\" onClick='sortCol("+ i + ")'>" + mytable[0][i] + "</a>\n";
                        toDisplay +="\t\t\t</td>\n" ;
                    }
                }
                else
                {
                    toDisplay +="\t\t\t<td style='width:auto;border-left : solid black 1px ;border-right : solid black 1px ;' >\n" ;
                    toDisplay += "\t\t\t\t<a href=\"#\" onClick='sortCol("+ i + ")'>" + mytable[0][i] + "</a>\n";
                    toDisplay +="\t\t\t</td>\n" ;
                }
	  
            }
            if ( isAdmin  )
            {
                toDisplay +="\t\t\t<td style='width:auto;border-left : solid black 1px ;border-right : solid black 1px ;' >\n" ;
                toDisplay += "\t\t\t\t<img src='images/trash.gif' alt='delete' onclick='deleteAll();' />\n";
                toDisplay +="\t\t\t</td>\n" ;
            }
            toDisplay +="</tr>\n" ;
	 
	   
            rowsNumber = mytable.length-1 ;
	
            var color ;
	
	
            for( var row = 1; row <= rowsNumber ; row++ )
            {
                if ( row%2== 0 ) color = "rowColor1" ;
                else color = "rowColor2" ;
                var currentRow = mytable[row];
                var messageNumber = currentRow[0]  ;
	
	 
	 
                var rowToDisplay ="" ;
                var fontColor ;
                for( var col = 1; col< columnNumber ; col++ )
                {
	
                    if (currentRow[col]!=null )
                    {
                        if ( (headerTable[col]) == "is_secure" )
                        {
                            rowToDisplay += "<td onclick='ptrFuncGetMessage(\""+ messageNumber +"\");'>" ;
                            if (  currentRow[col] == true )
                            {
                                rowToDisplay +="<img  src='images/icon-secure-pc2.gif'  alt='is_secure' />"
                            }
                            rowToDisplay += "</td>\n";
		   		
                        }
                        else if ( (headerTable[col]) == "pass" )
                        {
                            if ( currentRow[col]  == true )
                            {
                                fontColor = "columnTrue" ;
                            }
                            else
                            {
                                fontColor = "columnFalse" ;
                            }
		   		
                        }
                        else if ( (headerTable[col]).indexOf ( "Company Name or IP" ) != -1 )
                        {
                            if ( isAdmin  )
                            {
                                rowToDisplay += "<td onclick='ptrFuncGetMessage(\""+ messageNumber +"\");'>" ;
                                if (  currentRow[col]!="Unknown" )
                                {
                                    rowToDisplay += currentRow[col] ;
                                    // We don't display the IP address ;
                                    col++ ;
                                }
                                else
                                {   // We don't display the "Unknown" but we display the next column (the IP address)
                                    col++ ;
                                    rowToDisplay += currentRow[col] ;
                                }
                                rowToDisplay += "</td>\n";
                            }
                            else
                            {
                                col ++ ;
                            }
                        }
                        else
                        {
                            rowToDisplay += "<td onclick='ptrFuncGetMessage(\""+ messageNumber +"\");'>" ;
                            rowToDisplay += currentRow[col]  ;
                            rowToDisplay += "</td>\n";
                        }
                    }
                    else
                    {
                        rowToDisplay += "<td onclick='ptrFuncGetMessage(\""+ messageNumber +"\");'>" ;
                        rowToDisplay +=  "-"  ;
                        rowToDisplay += "</td>\n";
                    }
                }
	 
                if ( isAdmin )
                {
                    rowToDisplay +="<td >" ;
                    rowToDisplay += "<input type='checkbox' id='checkbox"+ row +"' value='"+ messageNumber +"' onclick='deleteDisplay(\""+ messageNumber + "\" , \"checkbox"+ row + "\");' />";
                    rowToDisplay +="</td>" ;
                }
                toDisplay +=   "<tr id='message"+ messageNumber  +"' onClick='highLight(\""+messageNumber+"\");' class='" + fontColor + " " + color +"' >" + rowToDisplay + "</tr>" ;
            }
            toDisplay +="</table></div></center>" ;
	
        }
        else if ( response.error !=null )
        {
            toDisplay += "<center><b>" ;
            toDisplay +=   response.error ;
            toDisplay += "</b></center>" ;
        }
	
        else
        {
            toDisplay += "<center><b>" ;
            toDisplay +=  "Unknown error";
            toDisplay += "</b></center>" ;
	
        }
        $("TableFrame").innerHTML = toDisplay ;
	
        $('loading').style.display = "none";
        new Effect.Appear('TableFrame');
	 
        getCountList() ;
    }
}

function getCountList(  )
{
 
    reqMessageArray = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(reqMessageArray, getCountListResult );
    reqMessageArray.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    reqMessageArray.open("POST", "GetTableServlet", true);

    // Specify that the body of the request contains form data
    reqMessageArray.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
 
    reqMessageArray.send( "&option=count"  );
  
  
}
function getCountListResult( )
{
    if ( reqMessageArray.responseText != null && reqMessageArray.responseText!=""  )
    {
        var response = eval("(" +reqMessageArray.responseText + ")") ;
	  
        var response                       = response.result ;
	  
        var numberResultsByPage = response.numberResultsByPage ;
        var numberOfResults = response.numberOfResults ;
        var pageNumber = response.pageNumber ;
	  
        var toDisplay = "" ;
        if (  numberResultsByPage  )
        {
            toDisplay += "<table class='tableSelect' ><tr>"
	 
            if ( currentPage > 0 )
            {
                toDisplay += "<td class='tdSelect' ><img src='images/left_arrow.gif' alt='left' onclick='currentPage--; performAdvancedSearch();' style='width:25px;height:25px;' /></td>" ;
            }
            toDisplay += "<td class='tdSelect' ><form action=# name='page'><select name='pageNumber' onchange='currentPage=document.page.pageNumber.value ; performAdvancedSearch();' >Display : \n" ;
	 
            var nbPagesResults =  parseInt( numberOfResults / numberResultsByPage )  ;
	 
            if (  parseInt( numberOfResults ) %  parseInt(  numberResultsByPage ) != 0 ) nbPagesResults++ ;
	
	
            for ( var i = 0 ; i < nbPagesResults ; i++ )
            {
                if ( pageNumber != null && parseInt(pageNumber)==(i) )
                    toDisplay += "<option value ='"+i+"' SELECTED > Page "+(i+1) +" </option>\n" ;
                else
                    toDisplay += "<option value ='"+i+"' > Page "+(i+1) +" </option>\n" ;
            }
            toDisplay += "</select></form></td>" ;
            toDisplay += "<td class='tdSelect' >Number of results :" +  ( numberOfResults ==null?"0": numberOfResults ) + "</td>" ;
            toDisplay += "<td class='tdSelect' ><input type=\"button\" class=\"button\" value=\"Refresh now\" onclick=\"performAdvancedSearch();\"  /></td>" ;
            if ( currentPage < nbPagesResults-1 )
            {
                toDisplay += "<td class='tdSelect'><img src='images/right_arrow.gif' alt='right' onclick='currentPage++; performAdvancedSearch();' style='width:25px;height:25px;' /></td>" ;
            }
            toDisplay += "</tr></table>";
        }
	 
        $('selectPage').innerHTML = toDisplay ;
    }
 
}

function setNewInterval(newtime) // Set new auto update interval
{
    if ( isNaN(parseFloat(newtime)) )
    {
        return;
    }
    var update = parseInt(1000*60*parseFloat(newtime)); // Convert minutes to milliseconds
	
    clearInterval(timeref);
    $("refreshInterval").style.backgroundColor= "gray";
    if ( update == 0 )
    {
        $("refreshInterval").value = "";
        $("refreshInterval").style.backgroundColor= "white";
        return;
    }
   
    timeref = setInterval("performAdvancedSearch()",update);
}

function highLight( number )
{ 
    if ( currentMessage != "" )
    {
        if ( $("message" + currentMessage )!=null )
        {
            $("message" + currentMessage ).style.backgroundColor = currentColor ;
        }
    }
	
    currentMessage = number ;
    currentColor = $("message" + currentMessage ).style.backgroundColor ;
	 
    $("message" + currentMessage ).style.backgroundColor = '#cfcdcd' ;
}

function deleteDisplay(number , checkbox )
{
 
    if ( $(checkbox).checked == true )
    {
        arrayMessagesToDelete = new Array() ;
        arrayMessagesToDelete[0] = number ;
        dd.elements.modalDelete.setZ( 5 ) ;
        dd.elements.modalDelete.show(true) ;
        showWindowFixed("modalDelete") ;

    }
}
function deleteAll()
{
    for ( var i = 1 ; i < rowsNumber ; i++ )
    {
        $("checkbox"+i ).checked = true ;
    }
    arrayMessagesToDelete = new Array() ;
    for ( var i = 1 ; i < rowsNumber ; i++ )
    {
        arrayMessagesToDelete[i]= $("checkbox"+i ).value  ;
    }
    dd.elements.modalDelete.setZ( 5 ) ;
    dd.elements.modalDelete.show(true) ;
    showWindowFixed("modalDelete") ;
}
function closeModalDelete()
{
    dd.elements.modalDelete.hide(true);
    if ( $( checkBoxSelected ) != null )
    {
        $( checkBoxSelected ).checked = false ;
        checkBoxSelected = null ;
    }
    else
    {
        for ( var i = 1 ; i < rowsNumber ; i++ )
        {
            $("checkbox"+i ).checked = false ;
        }
    }
    arrayMessagesToDelete = null ;
}
function closeModalIP()
{
    dd.elements.modalCompanyName.hide(true);
}
function showIpBox()
{
 
    $('resultForIP').innerHTML = "<b>Results for IP <input type='text' id='ipFilterInput' maxlength='15' size='13' onkeydown='if(event.keyCode==13 ){changeIPFilter();}' /></b>" ;
}
function changeIPFilter()
{

    if(  verifyIP( $("ipFilterInput").value )   ) 
    {
        ipOfClient =$("ipFilterInput").value  ;
        ipChanged = true ; 
        performAdvancedSearch() ; 
    }
  
}
function verifyIP (IPvalue) {
    errorString = "";
    theName = "IPaddress";

    var ipPattern = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
    var ipArray = IPvalue.match(ipPattern);

    if (IPvalue == "0.0.0.0")
        errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
    else if (IPvalue == "255.255.255.255")
        errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
    if (ipArray == null)
        errorString = errorString + theName + ': '+IPvalue+' is not a valid IP address.';
    else {
        for (i = 0; i < 4; i++) {
            thisSegment = ipArray[i];
            if (thisSegment > 255) {
                errorString = errorString + theName + ': '+IPvalue+' is not a valid IP address.';
                i = 4;
            }
            if ((i == 0) && (thisSegment > 255)) {
                errorString = errorString + theName + ': '+IPvalue+' is a special IP address and cannot be used here.';
                i = 4;
            }
        }
    }
    extensionLength = 3;
    if (errorString != "")
    {
        alert (errorString);
        return false ;
    }
    else
    {
        return true ;
    }
}
function checkMail(emailString)
{
    var x = emailString ;
    var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (filter.test(x)) return true ;
    else
    {
        alert('Incorrect email address');
        return false ;
    }
}
