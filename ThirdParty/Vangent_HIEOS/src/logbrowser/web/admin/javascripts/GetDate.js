function getDate( )
{

    var req = newXMLHttpRequest();
    var handlerFunction = getReadyStateHandler(req, getDateResult );
    req.onreadystatechange = handlerFunction ;
  
    req.open("Get", "../GetDateServlet?systemType=new&formatDisplayed=yyyy%20MMMMMM&formatValue=yyyy-MM", false );
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    req.send( null  );
}
function getDateOld( )
{

    var req = newXMLHttpRequest();
    var handlerFunction = getReadyStateHandler(req, getDateResult );
    req.onreadystatechange = handlerFunction ;
  
    req.open("Get", "../GetDateServlet?systemType=old&formatDisplayed=yyyy%20MMMMMM", false );
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
    $('select1').innerHTML = "<select id='date1'  >"+ innerHTMLString + "</select>" ;
    $('select2').innerHTML = "<select id='date2'  >"+ innerHTMLString + "</select>" ;
}