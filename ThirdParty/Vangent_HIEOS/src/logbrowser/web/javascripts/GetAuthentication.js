

function getIsAdmin ( ) 
{

 
    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parseIsAdmin );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "AuthenticationServlet" , true );

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    var parameterToSend ="isAdmin=get" ;
                                         
    req.send( parameterToSend  );

}
function logout ()
{
    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parselogOut );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "AuthenticationServlet", true );

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    var parameterToSend ="logout=yes" ;
 
                      
    req.send( parameterToSend  );
}
function parselogOut(resultXML ) 
{
   
    window.location = 'index.html?refresh=true'; 
    isAdmin = false ;
}

function parseIsAdmin(resultXML ) 
{
    var messageResult 	=  resultXML.getElementsByTagName("response")[0] ;
    var isAuthenticated = messageResult.getAttribute("isAuthenticated");
    var page = messageResult.getAttribute("page");
    var numResByPage =  messageResult.getAttribute("numberResultByPage");
    var company      =  messageResult.getAttribute("company");
    var ip           = messageResult.getAttribute("ip") ;
	  
    if ( page != "null" ) currentPage  = page        ;
    if ( numResByPage != "null" ) nbByPage = numResByPage      ;
		
    if (  isAuthenticated == "true" )
    {
        isAdmin = true ;
    }
    else
    {
        isAdmin = false ;
    }
	  
	
    if ( ip != "" && !isAdmin )
    {
        ipOfClient = ip ;
    }
    else if ( isAdmin )
    {
        ipOfClient = "" ;
    }

    init_() ;
    if ( company == "Unknown" )
    {
        dd.elements.modalCompanyName.show(true);

    }
	  
}
 
function replaceIPEmail ( name , email )
{
  
    if ( checkMail(email) )
    {
        // Obtain an XMLHttpRequest instance
        var req = newXMLHttpRequest();
	
        // Set the handler function to receive callback notifications
        // from the request object
        var handlerFunction = getReadyStateHandler(req, replaceIPResult );
        req.onreadystatechange = handlerFunction;
	  
        // Open an HTTP POST connection to the shopping cart servlet.
        // Third parameter specifies request is asynchronous.
        req.open("Get", "AuthenticationServlet?company="+ escape(name) + "&email=" + escape(email) , true );
	
        // Specify that the body of the request contains form data
        req.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded");
        req.send( null ) ;
    }
}
function replaceIPResult ( resultXML )
{
    dd.elements.modalCompanyName.hide(true);
}
  