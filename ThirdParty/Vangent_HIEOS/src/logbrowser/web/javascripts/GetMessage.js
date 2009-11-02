/***** Fonctions used to get a message from Postgresql Database  *****/

var tabsName      = new Array(  "Request", "HTTP","Soap","Other","Errors"   ) ;
var tablesName    = new Array( "request", "http","soap","other","error"   ) ;
var tabsDisplayed = false ;
var currentTab    = null ;
var reqMessage    ;
function getArrayNumber ( value )
{
  for ( var i = 0 ; i< tablesName.length ; i++ ) 
  {
     if ( tablesName[i] == value ) return i ;
  }
}
function isIncludedInTablesName( value )
{
  var returnBool = false ;
  for ( var i = 0 ; i < tablesName.length ; i++ )
  {
    if ( tablesName[i] == value ) 
     {
       returnBool = true ;
       break ;
     }
  }
  return returnBool ;
}
function findTableIndex ( name , table )
{
   for ( var i = 0 ; i < table.length ; i++ )
   {
     if ( table[i].name == name ) return i ;
   }
   return -1 ;
}

function convertXMLtoHTML(message)
{
  message =message.replace(/</g,"&lt;");
  message =message.replace(/>/g,"&gt;<br/>");
  message =message.replace(/&a1;/g,"<");
  message =message.replace(/&a2;/g,">");
  message =message.replace(/\"/g,"&quot;");
  message =message.replace(/\r \n/g,"</br>");

return message ;
}
function convertHTMLtoXML(message)
{
  message =message.replace(/&lt;/g,"<");
  message =message.replace(/&gt;/g,">");

return message ;
}

function parseMessageResult( ) {

var messageResult 	=   eval("(" + reqMessage.responseText + ")") ; 

if ( !tabsDisplayed )
{
	var info = "" ;
	if ( tabsName.length != tablesName.length  )
	{
		throw "tabsName.length != tablesName.length " ;
	}
	
	 info= "<div id='tabs'><ul id='tablist'>" ;
	 
	 for ( var i = 0 ; i < tablesName.length ; i++  )
	 {
	 	info += "<li><a onclick=\" showTab('"+ tablesName[i] + "')\" id=\""+ tablesName[i] +"tab\"> " + tabsName[i] +"</a></li>";
	 }
     
	 info += "</ul></div><div id='tabbody'>" ;
	 for ( var i = 0 ; i < tablesName.length ; i++  )
	 {
	 	info += "<div id='" + tablesName[i] +"'class='tabbody'  ></div>" ;
	 } 					
	 info += "</div>" ;

	 $('info').innerHTML = info ;
     tabsDisplayed = true ;
} 
if ( messageResult != null )
{
 if ( messageResult.result.error == null  )
 { 
    var number =   messageResult.result.message.number ;
	var firstTabAssigned = false ;
	 for ( var i = 0 ; i < tablesName.length ; i++ )
	 {   
	      $( tablesName[i] + "tab" ).style.display = 'block' ;
	 	  $( tablesName[i]  ).style.display = 'block'        ;
	 }
	
	 for ( var i = 0 ; i < tablesName.length ; i++ )
	 { 
	   var tableIndex = findTableIndex(  tablesName[i] , messageResult.result.message.table ) ;
	   if ( tableIndex != -1 )
	   {
	   var message = "" ;
	  
	      
	 	 var tableOfValues = messageResult.result.message.table[tableIndex].values    ;
		 
		  for ( var j=0 ; j < tableOfValues.length ; j++ )
		  {
		  if ( tableOfValues[j]!=null )
		  {
		  	var values = tableOfValues[j] ;
		  	
		     message  += "<h3>" +  values[0] +"<img src='images/copy.gif' alt='copy'  onclick = 'copy(convertHTMLtoXML($(\""+ values[0]+j+"\").innerHTML).replace(/<br>/ig,\"\"));'></h3>";
		  	if ( isUrl( values[1]) )
		  	{
		  		message  += "<p><a href='"+  values[1].escapeHTML() +"' target='_new'>" + values[1].escapeHTML() + "</a></p>";	
		  	}
		  	else
		  	{
		  		message  += "<p id='"+values[0]+j+"'>"+  values[1].escapeHTML()   +"</p>";	
		  	}
		  	
		   }
		 }
		 
	 		
	 	   if ( message !=null && $( tablesName[i] )!=null ) 
	 		{ 		
	 		
	 			 $( tablesName[i] ).innerHTML =  message    ;
	 			 
	 			  
	 			  if ( message == "")
	 			  {
	 			    
	 			   $( tablesName[i] + "tab" ).style.display = 'none' ;
	 		       $( tablesName[i]  ).style.display = 'none' ;
	 			  }
	 			  
	 		}
	 		
 		}
	 	else
	 	{
	 
	 		 $( tablesName[i] + "tab" ).style.display = 'none' ;
	 		 $( tablesName[i]  ).style.display = 'none' ;
	 	}
 	
  	} 
  
   for ( var i = 0 ; i < tablesName.length ; i++ )
	 {   
       if (   $( tablesName[i] ).innerHTML  != "" )
 			  {
 			 	 
            if ( currentTab != null && $( tablesName[getArrayNumber(currentTab) ] )!=null && $( tablesName[getArrayNumber(currentTab) ] ).innerHTML != ""  )
                {
                 showTab(currentTab) ;
                }
                else
                { 
 
                 showTab(tablesName[i]) ;
                }
               
 			  }
 	 }
 	 
	$("messageId").innerHTML = "<b>Message ID:</b> <a href=\"message.html?message="+number+"\" target='_new'>" + number + "</a><img src='images/copy.gif' alt='copy'  onclick = 'copy(\""+number+"\");'>" + "<b>Download EVS</b> <a href=\"/logbrowser/GetEVSServlet?message="+number+"\" target='_new'>" + number + "</a></br>"

   }
   else
   {
    $("messageId").innerHTML = "<b>" +  messageResult.error + "</b>" ;
   }
}
else 
 {
   alert("message is null") ;
 }
Element.show('info');
}


/* This script and many more are available free online at
The JavaScript Source!! http://javascript.internet.com
Created by: Mark O'Sullivan :: http://lussumo.com/
 Jeff Larson :: http://www.jeffothy.com/
 Mark Percival :: http://webchicanery.com/ */
function copy(text2copy) {
  if (window.clipboardData) {
    window.clipboardData.setData("Text",text2copy);
  } else {
    var flashcopier = 'flashcopier';
    if(!document.getElementById(flashcopier)) {
      var divholder = document.createElement('div');
      divholder.id = flashcopier;
      document.body.appendChild(divholder);
    }
    document.getElementById(flashcopier).innerHTML = '';
    var divinfo = '<embed src="others/_clipboard.swf" FlashVars="clipboard='+escape(text2copy)+'" width="0" height="0" type="application/x-shockwave-flash"></embed>';
    document.getElementById(flashcopier).innerHTML = divinfo;
  }
}

 
function getMessage(number ) {

Element.hide('info'); 
$("messageId").innerHTML = "<b>Getting message...</b>" ;
reqMessage = newXMLHttpRequest();

var handlerFunction = getReadyStateHandler(reqMessage, parseMessageResult );
reqMessage.onreadystatechange = handlerFunction;

var parameterToSend ="GetMessageServlet?" +
	"message=" + encodeURIComponent(number) ;

$("sqlCommand").innerHTML =  parameterToSend  ;

reqMessage.open("Get", parameterToSend , true);
reqMessage.send( null ) ;


}
function deleteMessage(arrayOfMessage)
{
  
// Obtain an XMLHttpRequest instance
  var req = newXMLHttpRequest();

  // Set the handler function to receive callback notifications
  // from the request object
  var handlerFunction = getReadyStateHandler(req, parseDeleteResult );
  req.onreadystatechange = handlerFunction;
  
  // Open an HTTP POST connection to the shopping cart servlet.
  // Third parameter specifies request is asynchronous.
  req.open("POST", "DeleteMessages", true);

  // Specify that the body of the request contains form data
  req.setRequestHeader("Content-Type", 
                       "application/x-www-form-urlencoded") ;

  var parameterToSend ="" ;
                      
  parameterToSend += "number="+ arrayOfMessage.length ;

   
    for ( var i = 0 ; i <  arrayOfMessage.length ; i++  )
      {
           parameterToSend += "&message"+i+"="+ arrayOfMessage[i] ;
      }

 req.send( parameterToSend  );
 
}

function parseDeleteResult(resultXML)
{
   performAdvancedSearch()         ;
   closeModalDelete()              ;
   arrayMessagesToDelete = null    ;
   currentPage = 0                 ;
   performAdvancedSearch()         ;
}

function getContentAsStringIE(node){
var s="";
for (var i = 0; i < node.childNodes.length; i++)
  s += node.childNodes[i].xml;
  return s;
}
function isUrl(s) {
	var regexp = /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	return regexp.test(s);
}

