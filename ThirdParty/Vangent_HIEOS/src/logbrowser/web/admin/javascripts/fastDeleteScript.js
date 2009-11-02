var   ptrFuncGetDate   = getDate         ;	
var   isAdmin ;
 
function init()
{

    getIsAdmin() ;
    if ( isAdmin== false)
    {
        window.location.href = "../admin" ;
    }
    else if ( isAdmin == true )
    {
        $('loginScreen').style.display = 'block';
        ptrFuncGetDate( ) ;
    }
}
 
 
function DeleteMessages()
{
     
  
    if ( $('date1').value == "0" && $('date2').value == "0" )
    
    {
    //error = "Please select a date" ;
    }
    else if (    ($('date1').value == "0" && $('date2').value != "0")
        || ($('date1').value != "0" && $('date2').value == "0") )
        {
        parameterToSend = "date="+ ( $('date1').value == "0"?('date2').value:$('date1').value );
 
    }
    else if ( $('date1').value != "0" && $('date2').value != "0" )
    {
        parameterToSend = "date1="+$('date1').value+"&date2="+$('date2').value ;
	 
    }
    
   
    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();
	
    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parseDeleteResult );
    req.onreadystatechange = handlerFunction;
	  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "../DeleteMessages", true);
	
    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded") ;
	 
    req.send( parameterToSend  );
 
}
function parseDeleteResult(resultXML)
{
    window.location.href  = "../index.html" ;
}