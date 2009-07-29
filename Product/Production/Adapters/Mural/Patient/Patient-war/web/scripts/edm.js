var fieldNameValues="";
var fieldNames="";
var minorObjType = "";
var minorObjTypeCount = 0;
var minorArray = new Array();
var hiddenUnLockFields="";
var queryStr="";

function showdatadiv() {
    document.getElementById("advSearch").style.visibility = "hidden";
    document.getElementById("datadiv").style.visibility = "visible";
    
    var id ;
    for(var i=1;i<5;i++)  {
        id = "datadiv"+i;
        document.getElementById(id).style.visibility="hidden";
        document.getElementById("preview").style.visibility  = "hidden";
    }
    
}
function showDuplicatesdiv() {
    document.getElementById("datadiv").style.visibility = "visible";
}
function showEoDetails() {
    document.getElementById("allduprecords").style.visibility = "hidden";
    
    document.getElementById("eoDatadiv").innerHTML = document.getElementById("datadiv").innerHTML;
    document.getElementById("eoDatadiv").style.visibility = "visible";
    
}
function showBasicSearch()  {
    document.getElementById("advancedSearch").style.visibility = "hidden";
    document.getElementById("advancedSearch").style.display= "none";
    
    document.getElementById("basicSearch").style.visibility = "visible";
    document.getElementById("basicSearch").style.display = "block";
    
}
function showAdvSearch()  {
    document.getElementById("basicSearch").style.visibility = "hidden";
    document.getElementById("basicSearch").style.display= "none";
    
    document.getElementById("advancedSearch").style.visibility = "visible";
    document.getElementById("advancedSearch").style.display= "block";
}
function simple()  {
    var id ;
    for(var i=1;i<5;i++)  {
        id = "datadiv"+i;
        document.getElementById(id).style.visibility=(document.getElementById(id).style.visibility =='hidden')? 'visible' :'hidden';
        document.getElementById("preview").style.visibility  = "hidden";
    }
}

function makeDiffPerson(divId,visibility,diffPerId)   {
    document.getElementById(divId).style.background = "#efefef";
}

function makeDiffPerson1(divId,visibility)   {
    document.getElementById(divId).style.background = "#efefef";
}

function populatePreview(index,euid)   {
    var divId = "eodata"+index;
    document.getElementById("preview").innerHTML =  document.getElementById(divId).innerHTML;
    for(var i=0;i<4;i++) {
        if(index == i) {
            document.getElementById("eodata"+i).style.background = "#cecff9";
        } else {
        document.getElementById("eodata"+i).style.background = "#f7f8d5";
    }
}
document.getElementById("preview").style.background = "#cecff9";
document.getElementById("preview").style.visibility = "visible";
}
function populateDuplicatesPreview(divId,previewId)   {

    document.getElementById(previewId).innerHTML =  document.getElementById(divId).innerHTML;
    document.getElementById(previewId).style.background = "#cecff9";
}

function selectOption(num) {
    var selObj = document.getElementById('selSeaShells1');
    selObj.selectedIndex = num;
}

function showSourceDivs(divId)  {
    document.getElementById("mainDiv").innerHTML = document.getElementById(divId).innerHTML;
}

function getPosition(e) {
    e = e || window.event;
    var cursor = {x:0, y:0};
    if (e.pageX || e.pageY) {
        cursor.x = e.pageX;
        cursor.y = e.pageY;
    } 
    else {
        var de = document.documentElement;
        var b = document.body;
        cursor.x = e.clientX + 
            (de.scrollLeft || b.scrollLeft) - (de.clientLeft || 0);
        cursor.y = e.clientY + 
            (de.scrollTop || b.scrollTop) - (de.clientTop || 0);
    }
    return cursor;
}

function showConfirm(divId,thisEvent)  {
    var y;
    var x;      
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById(divId).style.top = (y-150);
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}

function showExtraDivs(divId,thisEvent)  {
    var y;
    var x;      
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById(divId).style.top = (y-150);
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}

function togglePDDivs(divId,x,y)  {
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
    document.getElementById(divId).style.top = y;
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}


function closeExtraDivs(divId,closeId)  {
       document.getElementById(divId).style.visibility = "hidden";
       document.getElementById(divId).style.display = "none";
       document.getElementById(divId).innerHTML = "";

       document.getElementById(closeId).style.visibility = "hidden";
       document.getElementById(closeId).style.display = "none";
       

}

function populateExtraDivs(sourceDivId,destnDivId,rootDiv,closeDivId)  {
       
        document.getElementById(destnDivId).innerHTML += document.getElementById(sourceDivId).innerHTML;
        document.getElementById(destnDivId).style.visibility = "visible";
        document.getElementById(destnDivId).style.display = "block";
        //close the div after populating
        document.getElementById(rootDiv).style.visibility = "hidden";
        document.getElementById(rootDiv).style.display = "none";

        document.getElementById(closeDivId).style.visibility = "visible";
        document.getElementById(closeDivId).style.display = "block";
        
}

function showReportDivs(divId)  {
    if (divId=="activityReport"){
        document.getElementById("activityReportTab").style.visibility = "hidden";
        document.getElementById("extraTab").innerHTML = document.getElementById("extraTabData").innerHTML;
        document.getElementById("extraTab").style.visibility = "visible";
    } else if(divId=="assumedMatches" || divId=="auditLog"){
    document.getElementById("extraTab").innerHTML = "";
    document.getElementById("activityReportTab").style.visibility = "visible";
    document.getElementById("extraTab").style.visibility = "hidden";
}

}
function showMoreAddr()  {
    document.getElementById("addDiv").innerHTML =   document.getElementById("addAddr").innerHTML;
}
function showMorePnos()  {
    document.getElementById("phoneDiv").innerHTML =   document.getElementById("addPnosDiv").innerHTML;
}
function showSourceEditSearch()  {
    document.getElementById("editSearch").innerHTML = document.getElementById("editSearchData").innerHTML ;
}
function showSourceEditLid() {
    document.getElementById("editLid").innerHTML = document.getElementById("editLidData").innerHTML ;
    document.getElementById("source").style.visibility = "hidden";
    document.getElementById("editSearch").style.visibility = "hidden";
}
function showSourceEditLidSearch() {
    document.getElementById("edit2").innerHTML = document.getElementById("edit2Data").innerHTML ;
}


/*
*  Function : populatePreviewDiv
*  Purpose  : Function to preview the euid fields in preview layer.
*
*
*
*/
function populatePreviewDiv(previewDivIndex)   {
    
    var mainEuidDiv;
    var contentDiv;
    var extraContentDiv;
    
    var previewEuidDiv;
    mainEuidDiv     = "mainEuidContentDiv"+previewDivIndex;
    contentDiv      = "mainEuidDataContent"+previewDivIndex;
    extraContentDiv = "dynamicMainEuidContent"+previewDivIndex;
    
    // Activate the compare screen buttons
    document.getElementById("showActiveButtonDiv").style.visibility = "visible";
    document.getElementById("showActiveButtonDiv").style.display = "block";
    
    // hide read only compare screen buttons
    document.getElementById("showReadonlyButtonDiv").style.visibility = "hidden";
    document.getElementById("showReadonlyButtonDiv").style.display = "none";
    
    
    // Change the class name for the divs
    //document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
    //document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
   
    //document.getElementById("previewEuidContentDivAbove"+previewDivIndex).innerHTML = document.getElementById(contentDiv).innerHTML;
    //document.getElementById("previewEuidContentDivExtra"+previewDivIndex).innerHTML = document.getElementById(extraContentDiv).innerHTML;
    
}

/*
*  Function : populateAssociatePreviewDiv
*  Purpose  : Function to preview the euid fields in preview layer.
*
*
*
*/
function populateAssociatePreviewDiv(assoDivIndex,previewDivIndex)   {
    
    var mainEuidDiv;
    var contentDiv;
    var extraContentDiv;
    
    var previewEuidDiv;
    mainEuidDiv     = "assEuidDataContent"+assoDivIndex;
    contentDiv      = "assEuidDataContentAbove"+assoDivIndex;
    extraContentDiv = "dynamicAssEuidContent"+assoDivIndex;
    
    // Change the class name for the divs
    document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
    document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
    document.getElementById("previewEuidContentDivAbove"+previewDivIndex).innerHTML = document.getElementById(contentDiv).innerHTML;
    document.getElementById("previewEuidContentDivExtra"+previewDivIndex).innerHTML = document.getElementById(extraContentDiv).innerHTML;
    
}



/*
*  Function : showExtraFields
*  Purpose  : Function to display or hide duplicate records extra fields.
*
*
*
*/

function showExtraFields(mainEuidDivId,assDupCount,optionHideDisplay,mainDupCount) {
    var height;
    var displayOption;
    
    if(optionHideDisplay == 'visible') {
        height = "100%";
        displayOption = "block";
        
        // Show/hide up/down images
        document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.visibility = "hidden";
        //document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.height = "0px";
        document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.display = "none";
        
        // Show/hide up/down images
        document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";
        //document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;
        document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
        
        document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.visibility = "visible";
        document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.display = "block";
        
        document.getElementById("previewEuidDiv"+mainEuidDivId).style.visibility = "hidden";
        document.getElementById("previewEuidDiv"+mainEuidDivId).style.display = "none";
        
        document.getElementById("previewEuidContentDiv"+mainEuidDivId).className = 'dynaprevieww169';
        
    } else {
    height = "0px";
    displayOption = "none";
    // Show/hide up/down images
    document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.visibility = "visible";
    //document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.height = "100%";
    document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.display = "block";
    
    // Show/hide up/down images
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.height = "100%";
    
    
    // Show/hide up/down images
    document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "hidden";
    //document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;
    document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
    
    // Show/hide preview
    document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.visibility = "hidden";
    document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.display = "none";
    
    document.getElementById("previewEuidDiv"+mainEuidDivId).style.visibility = "visible";
    document.getElementById("previewEuidDiv"+mainEuidDivId).style.display = "block";
    
    document.getElementById("previewEuidContentDiv"+mainEuidDivId).className = 'dynaw169';
    
}

// Show/hide Heading Div id
document.getElementById("dynamicContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicContent"+mainEuidDivId).style.display = displayOption;

var  contentDiv = "mainEuidDataContent"+mainEuidDivId;
var  contentExtraDiv = "dynamicMainEuidContent"+mainEuidDivId;

// Show/hide Main Div id
//document.getElementById(contentDiv).style.visibility = optionHideDisplay;
//document.getElementById(contentDiv).style.height = height;

document.getElementById(contentExtraDiv).style.visibility = optionHideDisplay;
//document.getElementById(contentExtraDiv).style.height = height;
document.getElementById(contentExtraDiv).style.display = displayOption;

// Show/hide Main Div id
document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.display = displayOption;

document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.display = displayOption;

//document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.height = height;

// Show/hide Associated Div Id
for(var i=0;i<assDupCount;i++) {
    document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;
    //document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.height = height;
    document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.display = displayOption;
    
    document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;
    //document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.height = height;
    document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.display = displayOption;
    
    if(optionHideDisplay == 'visible') {
        document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "hidden";
        //document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "0px";
        document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.display = "none";
    } else {
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "visible";
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "25px";
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.display = "block";
    
}

}
if(document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility == 'visible')  {
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).className = 'dynaprevieww169';
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility = optionHideDisplay;;
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.height = height;
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
}


// Hide other main divs if any of them is ex
for(var i=0;i<mainDupCount;i++) {
    if(optionHideDisplay == 'visible') {
        if(i == mainEuidDivId) {
            document.getElementById("mainEuidDiv"+mainEuidDivId).style.visibility = "visible";
            document.getElementById("mainEuidDiv"+i).style.display = "block";
            document.getElementById("separator"+i).style.display = "block";
        }  else {
        document.getElementById("mainEuidDiv"+i).style.visibility = "hidden";
        document.getElementById("mainEuidDiv"+i).style.display = "none";
        document.getElementById("separator"+i).style.display = "none";
    }
} else {
document.getElementById("mainEuidDiv"+i).style.visibility = "visible";
document.getElementById("mainEuidDiv"+i).style.display = "block";
document.getElementById("separator"+i).style.display = "block";
}
}
}


/***
// Transparent Background on the different Person
**/

function toggleDifferentPerson(divToAnimate,showHide,AssocDiv,ToggleFlagId,nRows) {
    // toggle the visibility of Different Person
    var toggleHide = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'H'," + "'assEuidDataContent" + ToggleFlagId + "','" + ToggleFlagId + "'";
    var toggleShow = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'S'," + "'assEuidDataContent" + ToggleFlagId + "','" + ToggleFlagId + "'";
    var divContent = document.getElementById(AssocDiv).innerHTML;
    
    if ( divContent.indexOf(toggleHide) > 0 )   {
        var attribShowRight = {
            width: { from: 165, to:0}
        };
        var animShowRight = new YAHOO.util.Anim(divToAnimate, attribShowRight, 2, YAHOO.util.Easing.easeNone);
        animShowRight.animate();
        
        /* Change the styling of the different person*/
        
        
        //Close Animation div
        if (nRows == 2)  {
            document.getElementById(AssocDiv).style.height= '400px';
        } else {
        document.getElementById(AssocDiv).style.height= '200px';
    }
    document.getElementById(AssocDiv).style.display='block';
    document.getElementById(divToAnimate).style.height = '0px';
    document.getElementById(divToAnimate).style.display = 'none';
    
} else {
var attribShowLeft = {
    width: { from: 2, to:165}
};
//Close Content div
var animShowLeft = new YAHOO.util.Anim(divToAnimate, attribShowLeft, 2, YAHOO.util.Easing.easeNone);
document.getElementById(AssocDiv).style.height='0px';
document.getElementById(AssocDiv).style.display='none';

document.getElementById(divToAnimate).style.visibility='visible';
if (nRows == 2)  {
    document.getElementById(divToAnimate).style.height = '400px';
}  else {
document.getElementById(divToAnimate).style.height = '200px';
}
document.getElementById(divToAnimate).style.display = 'block';
animShowLeft.animate();
}

var changeFlag = document.getElementById(AssocDiv).innerHTML;
if ( divContent.indexOf(toggleHide) > 0 )   {
    changeFlag = changeFlag.replace(toggleHide,toggleShow);
} else {
changeFlag = divContent.replace(toggleShow,toggleHide);
}
document.getElementById(AssocDiv).innerHTML = changeFlag ;
document.getElementById(divToAnimate).innerHTML = '<div class="diffperson">' + divContent+ '</div>';
}


/*
*  Function : activePreviewButtons
*  Purpose  : Function to preview the euid fields in preview layer.
*
*
*
*/
function activePreviewButtons(totalAssociates,assoDivIndex,previewDivIndex)   {
    // Change the class name for the divs
    if(document.getElementById("showCompareButtonDiv"+previewDivIndex).style.visibility == 'hidden') {
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).className = 'dynaprevieww169';
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).style.visibility = "visible";
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).style.display = "block";
    }
    // Change the class name for the divs
    document.getElementById("hidePreviewButtonDiv"+previewDivIndex).style.visibility = "hidden";
    document.getElementById("hidePreviewButtonDiv"+previewDivIndex).style.display = "none"
    for(var i=0;i<totalAssociates;i++) {
        if(previewDivIndex+i == assoDivIndex) {
            document.getElementById("assEuidDataContent"+previewDivIndex+i).className = 'dynaprevieww169';
        }
    }
    for(var i=0;i<totalAssociates;i++) {
        if(previewDivIndex+i != assoDivIndex) {
            document.getElementById("assEuidDataContent"+previewDivIndex+i).className = 'dynaw169';
        }
    }
    
}

/*
function showViewSources(mainDupSources,count,countEnt,totalColumns) {
    var divLayerMain;
    //hideOther(mainDupSources,count,countEnt);
    for(var i=0;i<count;i++) {
    divLayerMain = document.getElementById(mainDupSources+countEnt+i);
    if (divLayerMain.style.display=="none" || divLayerMain.style.display=="") {
        divLayerMain.style.visibility="visible";
        divLayerMain.style.display="block";
       if( mainDupSources == 'mainDupSources') {
            divLayerMain = document.getElementById("mainDupHistory"+countEnt+i);
            divLayerMain.style.visibility="hidden";
            divLayerMain.style.display="none";
        } else if( mainDupSources == 'mainDupHistory' ) {
           divLayerMain = document.getElementById("mainDupSources"+countEnt+i);
           divLayerMain.style.visibility="hidden";
           divLayerMain.style.display="none";
       }
       //display or hide preview pane
       document.getElementById("previewPane").style.visibility="hidden";
       document.getElementById("previewPane").style.display="none";

       //display/hide other divs
       for(var c=0;c<totalColumns;c++) {
          if(c  == countEnt) {
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
         } else {
           document.getElementById("outerMainContentDivid"+c).style.visibility = "hidden";
           document.getElementById("outerMainContentDivid"+c).style.display = "none";
         }
       }
    } else if (divLayerMain.style.display=="block") {
       divLayerMain.style.visibility="hidden";
       divLayerMain.style.display="none";
 
       //display or hide preview pane
       document.getElementById("previewPane").style.visibility="visible";
       document.getElementById("previewPane").style.display="block";
       //display main and other duplicate divs
       for(var c=0;c<totalColumns;c++) {
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
      }
       
  }
}
}
*/

function hideOther(mainDupSources,count,countEnt) {
    var divLayerMain;
    for(var i=0;i<count;i++) {
       if( showViewSources == 'mainDupSources') {
            divLayerMain = document.getElementById("mainDupHistory"+countEnt+i);
            divLayerMain.style.visibility="hidden";
            divLayerMain.style.display="none";
        } else if( showViewSources == 'mainDupHistory' ) {
           divLayerMain = document.getElementById("mainDupSources"+countEnt+i);
           divLayerMain.style.visibility="hidden";
           divLayerMain.style.display="none";
       }
    }   
}


function showAssViewSources(countAss) {
    var divLayerAssoc = document.getElementById("assDupSources"+countAss);
    if (divLayerAssoc.style.display=="none" || divLayerAssoc.style.display=="") {
        divLayerAssoc.style.visibility="visible";
        divLayerAssoc.style.display="block";
    } else if (divLayerAssoc.style.display=="block"){
    divLayerAssoc.style.visibility="hidden";
    divLayerAssoc.style.display="none";
}
}

function getDateFieldName(formName,idName)  { 
   var thisFrm = document.forms[formName];
   for(i=0; i< thisFrm.elements.length; i++)   {      
     if (thisFrm.elements[i].name.indexOf(idName) != -1)   {
          return thisFrm.elements[i].name;            
        }
    }
    return 'null';
} 


function ClearContents(thisForm)  { 
 thisFrm = document.forms[thisForm];
 for(i=0; i< thisFrm.elements.length; i++)   {        
    var fieldName = thisFrm.elements[i].name;
    if(thisFrm.elements[i].name != 'lidmask') {
        if(thisFrm.elements[i].name !=  'javax.faces.ViewState' && 
		   thisFrm.elements[i].name !=  'reportName' && 
		   thisFrm.elements[i].id !=  'selectedSearchType' &&
		   thisFrm.elements[i].name !=  'layer')   {
		   var elemType = thisFrm.elements[i].type.toUpperCase();
		   if (elemType.indexOf("SELECT") != -1)  {
              thisFrm.elements[i].selectedIndex = "0";
		   }  else {
              thisFrm.elements[i].value = "";
		   }
        }
    }
 }
 return;
}




function  confirmResolve(countDupId){
    
    //var confirm_action = confirm("Select the type of resolve<BR> Resolve:<BR> AutoResolve");
    countDupId = 1;
    document.getElementById("potentialDuplicate"+countDupId).style.visibility = "visible";
    document.getElementById("potentialDuplicate"+countDupId).style.display = "block";

    document.getElementById("differentPerson"+countDupId).style.visibility = "hidden";
    document.getElementById("differentPerson"+countDupId).style.display= "none";

    var newwindow=window.open('./resolvepopup.jsp','Different Person','height=200,width=300,left=1200,top=1400,resizable=no,scrollbars=no,toolbar=no,status=no,statusbar=no');
    if (window.focus) {newwindow.focus()}
}

function  markDuplicate(){
    var countDupId = 1;
    document.getElementById("differentPerson1").style.visibility = "hidden";
    document.getElementById("differentPerson1").style.display= "none";
    
    document.getElementById("potentialDuplicate1").style.visibility = "visible";
    document.getElementById("potentialDuplicate1").style.display = "block";
    
}

function disp_gnameover(divId) {
    document.getElementById(divId).style.visibility = "visible";
    document.getElementById(divId).style.display = "block";
	
}

function hide_gnameover(divId) {
    document.getElementById(divId).style.visibility = "hidden";
    document.getElementById(divId).style.display = "none";
	
}


//* AJAX Services 

var is_ie = (navigator.userAgent.indexOf('MSIE') >= 0) ? 1 : 0; 
var is_ie5 = (navigator.appVersion.indexOf("MSIE 5.5")!=-1) ? 1 : 0; 
var is_opera = ((navigator.userAgent.indexOf("Opera6")!=-1)||(navigator.userAgent.indexOf("Opera/6")!=-1)) ? 1 : 0; 

//netscape, safari, mozilla behave the same??? 
var is_netscape = (navigator.userAgent.indexOf('Netscape') >= 0) ? 1 : 0; 

//XML HttpRequest Handle
var xhr; 
// call the URL
var innerHtmlDiv = '';
var thisEvent;


// Begin Source Record
function ajaxMinorObjects(url,thisInnerHtmlDivName,isEdit)    {
    innerHtmlDiv = thisInnerHtmlDivName;
	if (isEdit == "edit") 	{
		innerHtmlDiv = "stealth";
	}
    document.getElementById(innerHtmlDiv).style.visibility='visible';
    document.getElementById(innerHtmlDiv).innerHTML =  "<table style='font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;'><tr><td><img src='./images/loading.gif' border='0'> <p>Loading ...Please Wait</p></td></tr></table>";
    xhr = getXmlHttpObject(minorObjectsStateChanged); 
    //Send the xmlHttp get to the specified url 
    xmlHttpGet(xhr, url); 
} 

    function minorObjectsStateChanged()     { 
        //readyState of 4 or 'complete' represents that data has been returned 
        if (xhr.readyState == 4 || xhr.readyState == 'complete')        { 
            //Gather the results from the callback 
           var str = xhr.responseText; 
		   //Will only generate the script to populate the form, so don't update the content
		   document.getElementById(innerHtmlDiv).innerHTML = xhr.responseText;
		   document.getElementById(innerHtmlDiv).style.display = 'block';
		   document.getElementById(innerHtmlDiv).style.visibility = 'visible';
           var divID = document.getElementById(innerHtmlDiv);
           var x = divID.getElementsByTagName("script");    
           for(var i=0;i<x.length;i++)   {       
                eval(x[i].text);   
           }
           //get values
         } else   {
                 document.getElementById(innerHtmlDiv).innerHTML =  "<table style='font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;'><tr><td><img src='./images/loading.gif' border='0'> <p>Loading ...Please Wait</p></td></tr></table>";
         }
    } 

// End Source Record


function ajaxURL(url,thisInnerHtmlDivName,e)    {
    innerHtmlDiv = thisInnerHtmlDivName;
    thisEvent = e;
    document.getElementById(innerHtmlDiv).style.visibility='visible';
    document.getElementById(innerHtmlDiv).innerHTML =  "<table style='font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;'><tr><td><img src='./images/loading.gif' border='0'> <p>Loading ...Please Wait</p></td></tr></table>";
    xhr = getXmlHttpObject(ajaxstateChangeHandler); 
    //Send the xmlHttp get to the specified url 
    xmlHttpGet(xhr, url); 
} 


    function ajaxstateChangeHandler()     { 
        //readyState of 4 or 'complete' represents that data has been returned 
        if (xhr.readyState == 4 || xhr.readyState == 'complete')        { 
            //Gather the results from the callback 
           var str = xhr.responseText; 
           document.getElementById(innerHtmlDiv).innerHTML = xhr.responseText;
           document.getElementById(innerHtmlDiv).style.display = 'block';
           document.getElementById(innerHtmlDiv).style.visibility = 'visible';
           var divID = document.getElementById(innerHtmlDiv);
           var x = divID.getElementsByTagName("script");    
           for(var i=0;i<x.length;i++)   {       
                eval(x[i].text);   
           }
           //get values
         } else   {
            document.getElementById(innerHtmlDiv).innerHTML =  "<table><tr><td><img src='./images/loading.gif' border='0'> <p>Loading ...Please Wait</p></td></tr></table>";
         }
    } 

    // XMLHttp send GET request 
    function xmlHttpGet(xmlhttp, url) { 
        xmlhttp.open('GET', url, true); 
        xmlhttp.send(null); 
    } 


    function getXmlHttpObject(handler) 
    { 
        var objXmlHttp = null;    //Holds the local xmlHTTP object instance 

        //Depending on the browser, try to create the xmlHttp object 
        if (is_ie){ 
            //The object to create depends on version of IE 
            //If it isn't ie5, then default to the Msxml2.XMLHTTP object 
            var strObjName = (is_ie5) ? 'Microsoft.XMLHTTP' : 'Msxml2.XMLHTTP'; 
             
            //Attempt to create the object 
            try{ 
                objXmlHttp = new ActiveXObject(strObjName); 
                objXmlHttp.onreadystatechange = handler; 
            } 
            catch(e){ 
            //Object creation errored 
                return; 
            } 
        } 
        else if (is_opera){ 
            //Opera has some issues with xmlHttp object functionality 
            return; 
        } 
        else{ 
            // Mozilla | Netscape | Safari 
            objXmlHttp = new XMLHttpRequest(); 
            objXmlHttp.onload = handler; 
            objXmlHttp.onerror = handler; 
        } 
         
        //Return the instantiated object 
        return objXmlHttp; 
    } 
    
    function getFormData(form) {
    var dataString = "";

    function addParam(name, value) {
        dataString += (dataString.length > 0 ? "&" : "")
            + escape(name).replace(/\+/g, "%2B") + "="
            + escape(value ? value : "").replace(/\+/g, "%2B");
    }

    var elemArray = form.elements;
    for (var i = 0; i < elemArray.length; i++) {
        var element = elemArray[i];
        var elemType = element.type.toUpperCase();
        var elemName = element.name;
        if (elemName) {
            if (elemType == "TEXT"
                    || elemType == "TEXTAREA"
                    || elemType == "PASSWORD"
                    || elemType == "HIDDEN")
                addParam(elemName, element.value);
            else if (elemType == "CHECKBOX" && element.checked)
                addParam(elemName, element.value ? element.value : "On");
            else if (elemType == "RADIO" && element.checked)
                addParam(elemName, element.value);
            else if (elemType.indexOf("SELECT") != -1)
                for (var j = 0; j < element.options.length; j++) {
                    var option = element.options[j];
                    if (option.selected)
                        addParam(elemName,
                            option.value ? option.value : option.text);
                }
        }
    }
    return dataString;
}

function submitFormData(form, thisInnerHtmlDivName) {
    innerHtmlDiv = thisInnerHtmlDivName;
    if (window.ActiveXObject)
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest)
        xhr = new XMLHttpRequest();
    else
        return null;
    getXmlHttpObject(handler);    
    var method = form.method ? form.method.toUpperCase() : "GET";
    var action = form.action ? form.action : document.URL;
    var data = getFormData(form);

    var url = action;
    function submitCallback() {
        if (xhr.readyState == 4 && xhr.status != 200) {
            alert("Auto-Save Error: "
                + xhr.status + " " + xhr.statusText);
        }
    }
    xhr.onreadystatechange = submitCallback;

    xhr.setRequestHeader("Ajax-Request", "Auto-Save");
    if (method == "POST") {
        xhr.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded");
        xhr.send(data);
    } else
        xhr.send(null);
    
    return xhr;
}



    function collectLid(euid,localIdDesignation) {
        var found = "";
        var allLIds = [];
        var allLIdsCheck = [];
        var disableArray = [];
        for (var i=0; i<alllidsArray.length; i++) { 
            allLIds[i] = alllidsArray[i];
            allLIdsCheck[i] = alllidsArray[i];
            disableArray[i] = alllidsArray[i];
        }

       for (var i=0; i<lidArray.length; i++) {            
            if (lidArray[i] == euid)   {
                lidArray.splice(i,1);
                found = "true";
            }
        }

        if (found != "true")    {
            lidArray.push(euid);
        }  
       

        var tab = document.getElementById('mainEuidContent'+euid);
        if (tab.className == 'yellow')   {
            tab.className = 'blue';
        } else {
            tab.className = 'yellow';
        }

     
        var tab = document.getElementById('personEuidDataContent'+euid);
        if (tab.className == 'yellow')   {
            tab.className = 'blue';
        } else {
            tab.className = 'yellow';
        }

        if (lidArray.length == 2)    {
            
            //showAction ButtonDiv
            document.getElementById("previewActionButton").style.visibility = "visible";
            document.getElementById("previewActionButton").style.display = "block";
            var one;
            var two;
            for (var i=0; i<allLIdsCheck.length; i++) {            
                if (allLIdsCheck[i] == lidArray[0])   {
                    one = localIdDesignation + " " + (i+1);
                }
                if (allLIdsCheck[i] == lidArray[1])   {
                    two = localIdDesignation + " " + (i+1);
                }
            }
            var system = document.getElementById("basicMergeformData:sourceOption").options[document.getElementById("basicMergeformData:sourceOption").selectedIndex].value;

            document.getElementById("LID1").innerHTML = one;
            document.getElementById("LID2").innerHTML = two;           
            
			//Set the title for the links here
            document.getElementById("previewlid1Form:lid1Link").title = one;
			document.getElementById("previewlid2Form:lid2Link").title = two;           

            document.getElementById("previewlid1Form:previewhiddenLid1").value = lidArray[1] + ':' + lidArray[0];
            document.getElementById("previewlid2Form:previewhiddenLid2").value = lidArray[0] + ':'+ lidArray[1];
            //Set System
            document.getElementById("previewlid1Form:previewhiddenLid1source").value = system;
            document.getElementById("previewlid2Form:previewhiddenLid2source").value = system;

            document.getElementById("mergeFinalForm:previewhiddenLid1source").value = system;
    
            for (var i=0; i< lidArray.length; i++) { 
                 for (var j=0; j<disableArray.length; j++) {          
                     if (disableArray[j] == lidArray[i])   {
                         disableArray.splice(j,1);
                     }
                 }
            }


            for (var i=0; i<disableArray.length; i++) {
                for (var j=0; j<alllidsactionText.length; j++) {
                    if (alllidsactionText[j].lastIndexOf(disableArray[i]) != -1 )  {
                       document.getElementById('curve' + disableArray[i]).innerHTML = disableArray[i];
                       document.getElementById('curve' + disableArray[i]).style.cursor= 'not-allowed';
                    }
                }
            }  
        }  else {
            for (var i=0; i< disableArray.length; i++) { 
                found = false;
                 for (var j=0; j<lidArray.length; j++) { 
                     if (disableArray[i] == lidArray[j])   {
                         disableArray.splice(i,1);
                     }
                 }
            }

        
            for (var i=0; i<disableArray.length; i++) {
                for (var j=0; j<alllidsactionText.length; j++) {
                    if (alllidsactionText[j].lastIndexOf(disableArray[i]) != -1 )  {
                       document.getElementById('curve' + disableArray[i]).innerHTML = alllidsactionText[j];
                       document.getElementById('curve' + disableArray[i]).style.cursor= 'hand';
                    }
                }
            }

            document.getElementById("previewActionButton").style.visibility = "hidden";
            document.getElementById("previewActionButton").style.display = "none";                        
            document.getElementById('confirmationButton').style.visibility = 'hidden';
        }
    }

   
function showSearchType(searchId) {
    document.getElementById(searchId).style.visibility = "visible";
    document.getElementById(searchId).style.display = "block";
//    return searchId;
}

function pickSearchType(selectOption) {
    var searchId = selectOption.options[selectOption.selectedIndex].value;
    
    for(var i=0;i<selectOption.options.length;i++) {
      if(selectOption.options[i].value == searchId) {
         document.getElementById(searchId).style.visibility = "visible";
         document.getElementById(searchId).style.display = "block";
      } else {
         document.getElementById(selectOption.options[i].value).style.visibility = "hidden";
         document.getElementById(selectOption.options[i].value).style.display = "none";
      }
   }
}

function showLIDDiv(divId,thisEvent)  {
    var y;
    var x;      
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}
}

function showExtraLinkDivs(thisEvent,displayName,fieldName)  {
    var y;
    var x;      
    
    if(document.getElementById('linkSoDiv').style.visibility == 'hidden') {
        document.getElementById('linkSoDiv').style.visibility = "visible";
        document.getElementById('linkSoDiv').style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById('linkSoDiv').style.top = (y-150);
    document.getElementById('linkSoDiv').style.left = x;
} else {
   document.getElementById('linkSoDiv').style.visibility = "hidden";
   document.getElementById('linkSoDiv').style.display = "none";
}
document.getElementById('linkedValueDiv').innerHTML = fieldName;
document.getElementById('linkedDisplayValueDiv').innerHTML = displayName;

//store the full field name here
document.linkForm.sbrfullfieldname.value = fieldName;
document.linkForm.fieldDisplayName.value = displayName;


}

var linkValues="";
function populateLinkFields() {
    var sysLidCode = document.linkForm.systemCodeWithLid.options[document.linkForm.systemCodeWithLid.selectedIndex].value;
    var fieldName = document.getElementById('linkedValueDiv').innerHTML+'>>';

    var linkId  = document.getElementById('linkedValueDiv').innerHTML;
    
    document.getElementById('linkSourceDiv:'+linkId).style.visibility = 'hidden';
    document.getElementById('linkSourceDiv:'+linkId).style.display = 'none';
    
    //Person.PersonCatCode:HOSPITAL:1238990001
    document.getElementById(linkId+':'+sysLidCode).style.visibility = 'visible';
    document.getElementById(linkId+':'+sysLidCode).style.display = 'block';    
    
    linkValues+=fieldName+sysLidCode+'##';         
    
    var hiddenLinkFieldsVar = 'basicAddformData:hiddenLinkFields';    
    document.getElementById('basicAddformData:hiddenLinkFields').value = linkValues;
    document.getElementById('linkSoDiv').style.visibility = "hidden";
    document.getElementById('linkSoDiv').style.display = "none";
}

/**************START UNLINK RELATED METHODS**************/

function showExtraUnLinkDivs(thisEvent,displayName,fieldName,fullFieldName)  {
    var y;
    var x;      
    if(document.getElementById('unLinkSoDiv').style.visibility == 'hidden') {
        document.getElementById('unLinkSoDiv').style.visibility = "visible";
        document.getElementById('unLinkSoDiv').style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById('unLinkSoDiv').style.top = (y-150);
    document.getElementById('unLinkSoDiv').style.left = x;
} else {
   document.getElementById('unLinkSoDiv').style.visibility = "hidden";
   document.getElementById('unLinkSoDiv').style.display = "none";
}
document.getElementById('unLinkedValueDiv').innerHTML = fieldName;
document.getElementById('unLinkedDisplayValueDiv').innerHTML = displayName;
document.getElementById('unLinkedFullFieldDiv').innerHTML = fullFieldName;

//store the full field name here
document.unlinkForm.sbrfullfieldname.value = fieldName;
document.unlinkForm.fieldDisplayName.value = displayName;

}

var unLinkValues="";
function populateUnLinkFields() {
    var fieldName = document.getElementById('unLinkedValueDiv').innerHTML;
    fieldName = fieldName.replace("&gt;&gt;",">>");
    
    var fullFieldName = document.getElementById('unLinkedFullFieldDiv').innerHTML;
    document.getElementById('linkSourceDivData:'+fullFieldName ).style.visibility = 'visible';
    document.getElementById('linkSourceDivData:'+fullFieldName ).style.display = 'block';
    
    unLinkValues+=fieldName+'##';     
    
    var hiddenUnLinkFieldsVar = 'basicAddformData:hiddenUnLinkFields';
    
    document.getElementById('basicAddformData:hiddenUnLinkFields').value = unLinkValues;
    
    document.getElementById('unLinkSoDiv').style.visibility = "hidden";
    document.getElementById('unLinkSoDiv').style.display = "none";
}
/**************END UNLINK RELATED METHODS**************/

var mergeEuids="";
var sourceEuids="";

//		   var euids="";
 //          var euidArray = [];
 //          var alleuidsArray = [];

function accumilateMultiMergeEuids(mergeEuidVar) {
        var found = "";
        var allEUIDs = [];
        var allEUIDsCheck = [];
        var disableArray = [];
        for (var i=0; i<alleuidsArray.length; i++) { 
            allEUIDs[i] = alleuidsArray[i];
            allEUIDsCheck[i] = alleuidsArray[i];
         }

         //If the destination EO is removed from the selection remove all selections and reset the array of euids
        if (euidArray.length > 1 && euidArray[0] == mergeEuidVar)   {
            for (var i=0; i<euidArray.length; i++) {            
               document.getElementById('mainEuidContent'+euidArray[i]).className = 'yellow';
               document.getElementById('personEuidDataContent'+euidArray[i]).className = 'yellow'; 		  
	        }
		    //Deselect and reset the array size here
             euidArray = [];
        } else {
          for (var i=0; i<euidArray.length; i++) {            
            if (euidArray[i] == mergeEuidVar)   {
                euidArray.splice(i,1);
                found = "true";
               }
           }
 
            if (found != "true")    {
               euidArray.push(mergeEuidVar);
            }  
             var tab = document.getElementById('mainEuidContent'+mergeEuidVar);
             if (tab.className == 'yellow')   {
               tab.className = 'blue'; 
             } else {
               tab.className = 'yellow';
             }

             var tab = document.getElementById('personEuidDataContent'+mergeEuidVar);
             if (tab.className == 'yellow')   {
                 tab.className = 'blue';
             } else {
                tab.className = 'yellow';
             }        
		}
        mergeEuids+=mergeEuidVar+'##';     

        var mainEuidArray = mergeEuids.split("##");
        var mainEuid = mainEuidArray[0];

        //document.getElementById('previewForm:destinationEO').value = mainEuid;
        //document.getElementById('mergeFinalForm:destinationEO').value = mainEuid;
        //document.getElementById('clickButton' + mainEuid).style.cursor= 'not-allowed';

         if(euidArray.length >= 2) {
            document.getElementById('previewForm:previewhiddenMergeEuids').value = euidArray;
            document.getElementById('mergeFinalForm:srcDestnEuids').value = euidArray; 
            document.getElementById('mergeEuidsDiv').style.visibility = "visible";
            document.getElementById('mergeEuidsDiv').style.display = "block";
         } else {
            document.getElementById('mergeEuidsDiv').style.visibility = "hidden";
            document.getElementById('mergeEuidsDiv').style.display = "none";
		 }
}

function finalMultiMergeEuids(mergeDivId,thisEvent)  {
    var y;
    var x;      
    if(document.getElementById(mergeDivId).style.visibility == 'hidden') {
        document.getElementById(mergeDivId).style.visibility = "visible";
        document.getElementById(mergeDivId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById(mergeDivId).style.top = (y-150);
    document.getElementById(mergeDivId).style.left = x;
} else {
   document.getElementById(mergeDivId).style.visibility = "hidden";
   document.getElementById(mergeDivId).style.display = "none";
}

}
 
  var euids=""
   /*Accumulate EUID for the Patient details screen*/
   function getEUIDS(euid) {
       euids += euid + "##";
       document.getElementById('yuiform:compareEuids').value = euids;
   }


/**
*
*  URL encode / decode
**/
function _utf8_encode (string) {
	    var str = string;
		//string = str.replace(/\r\n/g,"\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    }


var mergePreEuids="";

function populateMergeFields(fieldName,value,displayValue,highLightId) {
	   if( displayValue == "null") {
         document.getElementById(fieldName).innerHTML = '<img src="./images/calup.gif" border="0" alt="Blank Value"/>';
	   } else {
		 document.getElementById(fieldName).innerHTML = displayValue;
 	   }
	   document.getElementById(fieldName).className='highlight';
	   mergePreEuids+=fieldName+"##"+value+'>>';     
       //document.getElementById('mergeFinalForm:selectedMergeFields').value = value;
        //document.getElementById('mergeFinalForm:selectedMergeFields').value = mergePreEuids;

	   //document.getElementById('highlight'+highLightId).style.visibility = 'hidden';
	   //document.getElementById('highlight'+highLightId).style.display = 'none';

	  // document.getElementById('unHighlight'+highLightId).style.visibility = 'visible';
	  // document.getElementById('unHighlight'+highLightId).style.display = 'block';
       
}

function showResolveDivs(divId,thisEvent,potDupId)  {
    var y;
    var x;      
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById(divId).style.top = (y-150);
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}
   //For duplicate preview screen
   if(document.getElementById('potentialDuplicateId') != null ) {
       document.getElementById('potentialDuplicateId').value = potDupId;
   }
   //For compare duplicate screen
   if(document.getElementById('reportYUISearch:potentialDuplicateId') != null ) {
     document.getElementById('reportYUISearch:potentialDuplicateId').value = potDupId;
   }

   if(document.getElementById('resolvePotentialDuplicateId') != null ) {
	 
     document.getElementById('resolvePotentialDuplicateId').value = potDupId;
     }

}

function accumilateFieldsOnBlur(field,fullFieldName) {
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+field.value+">>";
    document.getElementById("advancedformData:enteredFieldValues").value = fieldNameValues;

  //set the search type here
    if(document.getElementById("searchTypeForm:searchType") != null) {
       var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
       document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
    }
}

function accumilateSelectFieldsOnBlur(field,fullFieldName) {
    var selectedValue = field.options[field.selectedIndex].value;
    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    document.getElementById("advancedformData:enteredFieldValues").value = fieldNameValues;
    if(document.getElementById("searchTypeForm:searchType") != null) {
     var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
     document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
    }
}

function getLidMask(systemCode,systemCodes,lidMasks) {
    for(var i=0;i<systemCodes.length;i++) {
      if(systemCode == systemCodes[i]) {
         return lidMasks[i];
      }
    }    
}
function unlockFields(fieldName) {
        document.getElementById('editableSBR:'+fieldName).style.visibility = 'visible';
        document.getElementById('editableSBR:'+fieldName).style.display = 'block';

        document.getElementById('readOnlySBR:'+fieldName).style.visibility = 'hidden';
        document.getElementById('readOnlySBR:'+fieldName).style.display = 'none';

        document.getElementById('lockSourceDiv:'+fieldName).style.visibility = 'visible';
        document.getElementById('lockSourceDiv:'+fieldName).style.display = 'block';

        document.getElementById('unlockSourceDiv:'+fieldName).style.visibility = 'hidden';
        document.getElementById('unlockSourceDiv:'+fieldName).style.display = 'none';
        
       //hiddenUnLockFields += fieldName + "##";
       //document.getElementById("basicAddformData:hiddenUnLockFields").value = hiddenUnLockFields;      
    }


function accumilatePersonFieldsOnBlur(field,fullFieldName) {
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+field.value+">>";
    document.getElementById("basicAddformData:enteredFieldValues").value = fieldNameValues;

}

function accumilatePersonSelectFieldsOnBlur(field,fullFieldName) {
    var selectedValue = field.options[field.selectedIndex].value;
    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    document.getElementById("basicAddformData:enteredFieldValues").value = fieldNameValues;
 
}



function accumilateMinorObjectFieldsOnBlur(objectType,field,fullFieldName) {
   minorObjType =   objectType;
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+field.value+">>";
    //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValues;
    
        fieldNameValues = fieldNameValues  + minorObjType;
        minorArray.push(fieldNameValues);
        //RESET THE FIELD VALUES HERE
        fieldNameValues = "";      
        document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArray;

}

function accumilateMinorObjectSelectFieldsOnBlur(objectType,field,fullFieldName) {
    var selectedValue = field.options[field.selectedIndex].value;
    minorObjType =   objectType;
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValues;

        fieldNameValues = fieldNameValues  + minorObjType;
        minorArray.push(fieldNameValues);
        //RESET THE FIELD VALUES HERE
        fieldNameValues = "";        
        document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArray;

}



function populateMinorObjectsExtraDivs(sourceDivId,destnDivId,rootDiv,closeDivId,minorArrayLocal)  {
        document.getElementById(destnDivId).innerHTML +=  document.getElementById(sourceDivId).innerHTML;
        document.getElementById(destnDivId).style.visibility = "visible";
        document.getElementById(destnDivId).style.display = "block";
        //close the div after populating
        document.getElementById(rootDiv).style.visibility = "hidden";
        document.getElementById(rootDiv).style.display = "none";

        document.getElementById(closeDivId).style.visibility = "visible";
        document.getElementById(closeDivId).style.display = "block";

        //fieldNameValues +=  minorObjType + minorArrayLocal +"$$$";
        
        //populate the minor object values here.
        minorArray.push(minorArrayLocal);
        
        var localArray = minorArray[minorArray.length-1];
//        for(var i=0;i<minorArray.length;i++) {
//            
//            for(var j=0;j<localArray.length;j++) {
//               if(j > 0 ) {
//                   alert( j + "INDEX" + localArray[j] +" ------ minorArray final in loop ------"+localArray[j-1]);      
//               }
//               if(j < localArray.length && localArray[j] == localArray[j+1]) {
//                   alert(localArray[j] +" ------ ===== ------"+localArray[j+1]);      
//               }
//            }    
//        }
        document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = localArray;
        document.getElementById("basicAddformData:minorObjectTotal").value = minorArray.length;        
}

          
function accumilateNewPersonFieldsOnBlur(field,fullFieldName,mask,valueType) {
    var maskChars = new Array();
    var str = mask;
    str  = str.replace(/D/g,"");
    
    maskChars = str.split('');
    
    var valueEntered =  "";
    var valueTypeLocal =  "";
    valueEntered =  field.value;
    valueTypeLocal =  valueType;
    
    //valueEntered  = valueEntered.replace(/\)/g,"");
    //valueEntered  = valueEntered.replace(/\(/g,"");
    
    if(valueTypeLocal == '6' || valueTypeLocal == '8' ) {
      valueEntered =  field.value;
    } else {
      for(var i=0;i<maskChars.length;i++) {
        valueEntered  = valueEntered.replace(maskChars[i],'');
      }    
    }

    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+valueEntered+">>";
    document.getElementById("basicAddformData:newSOEnteredFieldValues").value = fieldNameValues;

}

function accumilateNewPersonSelectFieldsOnBlur(field,fullFieldName) {
    var selectedValue = field.options[field.selectedIndex].value;
    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    document.getElementById("basicAddformData:newSOEnteredFieldValues").value = fieldNameValues;
 
}

function accumilateEOMinorFieldsOnBlur(field,fullFieldName,minorId,minorobjectType) {
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+field.value+">>";
    document.getElementById("basicAddformData:minorFieldsEO").value = fieldNameValues + "++" + minorobjectType+"," +minorId + "";
}

function accumilateEOMinorFieldsSelectOnBlur(field,fullFieldName,minorId,minorobjectType) {
    var selectedValue = field.options[field.selectedIndex].value;
    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    document.getElementById("basicAddformData:minorFieldsEO").value = fieldNameValues + "++" + minorobjectType +"," +minorId+ "";

}


function accumilateEOMinorObjectsRemove(minorId,minorobjectType) {
    fieldNameValues += "MINOR_OBJECT_ID :: " + minorId + "##"+ "MINOR_OBJECT_TYPE ::" + minorobjectType+">>";
    var remArray = new Array();
    remArray.push(fieldNameValues);
    document.getElementById("basicAddformData:removeEOMinorObjectsValues").value = remArray;

}


function toggleDupDivClass(fac,count,euid) {
	    var tab = document.getElementById('mainEuidContentDiv'+fac+count+euid);
        if (tab.className == 'yellow')   {
           tab.className = 'blue';
        } else {
            tab.className = 'yellow';
        }

		var preview  = document.getElementById('previewEuidDiv'+fac);
        preview.className = 'blue';

        var buttonsDiv = document.getElementById('buttonsDiv'+fac);
		buttonsDiv.style.visibility = 'visible';
		buttonsDiv.style.display = 'block';
}

var mergeEuidsPreview="";

function accumilateMultiMergeEuidsPreview(fac,count,mergeEuidVar) {
	     var tab = document.getElementById('mainEuidContentDiv'+fac+count+mergeEuidVar);
         if (tab.className == 'yellow')   {
            tab.className = 'blue';
         } else {
             tab.className = 'yellow';
         }
         var mainEuidDataDiv = document.getElementById('mainEuidDataDiv'+fac+count+mergeEuidVar);
		 var preview  = document.getElementById('previewEuidDiv'+fac);
         preview.className = 'blue';
         mergeEuidsPreview+=mergeEuidVar+'##';     

         var mainEuidArray = mergeEuidsPreview.split("##");
         var mainEuid = mainEuidArray[0];

         document.getElementById('mergeFinalForm:destinationEO').value = mainEuid;
	     document.getElementById('mergeFinalForm:rowCount').value = fac;

         var query = "";

         if(mainEuidArray.length > 2) {
            var buttonsDiv = document.getElementById('buttonsDiv'+fac);
	    	buttonsDiv.style.visibility = 'visible';
		    buttonsDiv.style.display = 'block';

   	        document.getElementById('mergeFinalForm:previewhiddenMergeEuids').value = mergeEuidsPreview;
            //query +="&destnEuidValue="+ mainEuid;
	        //query +="&sourceEuids="+ mergeEuidsPreview;
	        //query +="&rowcount="+ fac;
        }


}

//Functions for view sources/history in euid details pages
function showViewSources(mainDupSources,count,countEnt,totalColumns,historySize,euidsArray) {
  
		var divLayerMain;
		var divLayerHistory;
		//Hide/show extra source divs spacer<%=euid%><%=sCount%><%=countEnt%>
		   for(var s=0;s<count;s++) {
			for(var i=0;i<totalColumns;i++) {
			// var spacerDiv = document.getElementById('spacer'+euidsArray[e]+s+i);    
			 var spacerDiv = document.getElementById('spacer'+s+i);    
			 if(spacerDiv != null) {
			   if(countEnt  == i ) {
				 if (spacerDiv.style.display=="block") {
				   spacerDiv.style.visibility="hidden";
				   spacerDiv.style.display="none";
				 } else {
				   spacerDiv.style.visibility="visible";
				   spacerDiv.style.display="block";
				} 
			  } //if euids array condition
			} //if spacer div condition
		   } //Total number of euid columns
		  }//Total numbe of src records column
		var showSources = "true";
		for(var i=0;i<count;i++) {
		  divLayerMain = document.getElementById(mainDupSources+countEnt+i);
		  if (divLayerMain.style.display=="none" || divLayerMain.style.display=="") {
			 divLayerMain.style.visibility="visible";
			 divLayerMain.style.display="block";
			 showSources = "true";
  			 if( mainDupSources == 'mainDupSources') {
				divLayerMain = document.getElementById("mainDupSources"+countEnt+i);
				divLayerMain.style.visibility="visible";
				divLayerMain.style.display="block";
			 }
			 
			 //display or hide preview pane
			 if(document.getElementById("previewPane") != null ) {
				document.getElementById("previewPane").style.visibility="hidden";
				document.getElementById("previewPane").style.display="none";
			 }
			 //display/hide other divs
			 for(var c=0;c<totalColumns;c++) {
				//HIDE history divs by default
				 for(var j=0;j<historySize;j++) {
				  divLayerHistory = document.getElementById("mainDupHistory"+c+j);
				  if(divLayerHistory != null) {  
					divLayerHistory.style.visibility="hidden";
					divLayerHistory.style.display="none";
				  }
				}
			  
		   }
		} else if (divLayerMain.style.display=="block" || divLayerMain.style.display!="") {
		   divLayerMain.style.visibility="hidden";
		   divLayerMain.style.display="none";
		   showSources = "false";


		   //display main and other duplicate divs
		   for(var c=0;c<totalColumns;c++) {
				document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
				document.getElementById("outerMainContentDivid"+c).style.display = "block";
				if (document.getElementById("dynamicMainEuidButtonContent"+c) != null) 			{
				document.getElementById("dynamicMainEuidButtonContent"+c).style.visibility = "visible";
				document.getElementById("dynamicMainEuidButtonContent"+c).style.display = "block";
				}
			  }
	 }
	}
 	 
	 for(var c=0;c<totalColumns;c++) {
 				 for(var j=0;j<count;j++) {
				  divLayerMain = document.getElementById("mainDupSources"+c+j);
				  if(divLayerMain != null ) {
					  if(divLayerMain.style.visibility == 'visible') {
 						showSources = "true";
					  }
				  }
					
 				}
			  
		   }

  		//show/hiden merge buttons in compare duplicates screen
		if(showSources == "true"   ) {
  				if(document.getElementById("mergeFinalEuidsDiv") != null ) {
						document.getElementById("mergeFinalEuidsDiv").style.visibility="hidden";
						document.getElementById("mergeFinalEuidsDiv").style.display="none";
				 }
		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="hidden";
			 document.getElementById("previewPane").style.display="none";
		   }

 		} else {
				if(document.getElementById("mergeFinalEuidsDiv") != null ) {
						document.getElementById("mergeFinalEuidsDiv").style.visibility="visible";
						document.getElementById("mergeFinalEuidsDiv").style.display="block";
				 }

		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="visible";
			 document.getElementById("previewPane").style.display="block";
		   }
 
		}

 	if(euidArray.length > 1 ) {
		if(showSources == "true"   ) {
   				 if(document.getElementById("mergeEuidsDiv") != null ) {
						document.getElementById("mergeEuidsDiv").style.visibility="hidden";
						document.getElementById("mergeEuidsDiv").style.display="none"; 
				}
		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="hidden";
			 document.getElementById("previewPane").style.display="none";
		   }
		} else {
	  			 if(document.getElementById("mergeEuidsDiv") != null ) {
						document.getElementById("mergeEuidsDiv").style.visibility="visible";
						document.getElementById("mergeEuidsDiv").style.display="block"; 
				}

		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="visible";
			 document.getElementById("previewPane").style.display="block";
		   }
		}
	}
}            
//View History for the EOS
function showViewHistory(mainDupHistory,count,countEnt,totalColumns,sourceSize,mergeflag,euidsArray) {
    var divLayerHist;
    var divLayerSources;
	var showHistory = "true";
   	
	//Hide/show extra source divs 
    for(var s=0;s<count;s++) {
        for(var i=0;i<totalColumns;i++) {
        // var spacerDiv = document.getElementById('spacer'+euidsArray[e]+s+i);    
         var spacerDiv = document.getElementById('spacer'+s+i);    
  	     if(spacerDiv != null) {
            spacerDiv.style.visibility="hidden";
            spacerDiv.style.display="none";
 	    } //if spacer div condition
	   } //Total number of euid columns
	}//Total numbe of src records column
 


	for(var i=0;i<count;i++) {
    divLayerHist = document.getElementById(mainDupHistory+countEnt+i);
    
    if (divLayerHist.style.display=="none" || divLayerHist.style.display=="") {
         divLayerHist.style.visibility="visible";
         divLayerHist.style.display="block";
 		 showHistory = "true";
          
       if( mainDupHistory == 'mainDupHistory') {
            divLayerHist = document.getElementById("mainDupHistory"+countEnt+i);
            divLayerHist.style.visibility="visible";
            divLayerHist.style.display="block";
        }
        //display/hide other divs
       for(var c=0;c<totalColumns;c++) {
	   //hide sources by default
	   for(var j=0;j<sourceSize;j++) {  
	       divLayerSources = document.getElementById("mainDupSources"+c+j);
		   if(divLayerSources != null) {
		      divLayerSources.style.visibility="hidden";
	          divLayerSources.style.display="none";
		   }
		}

          if(c  == countEnt) {
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
			if(document.getElementById("dynamicMainEuidButtonContent"+c) != null) {
            document.getElementById("dynamicMainEuidButtonContent"+c).style.visibility = "visible";
            document.getElementById("dynamicMainEuidButtonContent"+c).style.display = "block";
			}
           } else {
           document.getElementById("outerMainContentDivid"+c).style.visibility = "hidden";
           document.getElementById("outerMainContentDivid"+c).style.display = "none";
		   if(document.getElementById("dynamicMainEuidButtonContent"+c) != null) {
           document.getElementById("dynamicMainEuidButtonContent"+c).style.visibility = "hidden";
           document.getElementById("dynamicMainEuidButtonContent"+c).style.display = "none";
		   }
          }
       }
	   if(mergeflag == 'true') {
	    //viewMergeRecords
         if(document.getElementById("viewMergeRecords") != null) {
	       //viewMergeRecords
           document.getElementById("viewMergeRecords").style.visibility="visible";
           document.getElementById("viewMergeRecords").style.display="block";
         }
       }
    } else if (divLayerHist.style.display=="block") {
       divLayerHist.style.visibility="hidden";
       divLayerHist.style.display="none";
 		 showHistory = "false";
 
  

	   if(mergeflag == 'true') {
	    //viewMergeRecords
         if(document.getElementById("viewMergeRecords") != null) {
           document.getElementById("viewMergeRecords").style.visibility="hidden";
           document.getElementById("viewMergeRecords").style.display="none";
		 }
       }


       //display main and other duplicate divs
       for(var c=0;c<totalColumns;c++) {
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
			if(document.getElementById("dynamicMainEuidButtonContent"+c) != null) {
            document.getElementById("dynamicMainEuidButtonContent"+c).style.visibility = "visible";
            document.getElementById("dynamicMainEuidButtonContent"+c).style.display = "block";
			}
          }

 }
}


      //check for all the history divs
	  for(var c=0;c<totalColumns;c++) {
		 for(var j=0;j<count;j++) {
		  divLayerHist = document.getElementById("mainDupHistory"+c+j);
		  if(divLayerHist != null ) {
			  if(divLayerHist.style.visibility == 'visible') {
				showHistory = "true";
			  }
		  }			
		}
       }

 
		//show/hiden merge buttons in compare duplicates screen
		if(showHistory == "true"   ) {
  				if(document.getElementById("mergeFinalEuidsDiv") != null ) {
						document.getElementById("mergeFinalEuidsDiv").style.visibility="hidden";
						document.getElementById("mergeFinalEuidsDiv").style.display="none";
				 }
		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="hidden";
			 document.getElementById("previewPane").style.display="none";
		   }

 		} else {
				if(document.getElementById("mergeFinalEuidsDiv") != null ) {
						document.getElementById("mergeFinalEuidsDiv").style.visibility="visible";
						document.getElementById("mergeFinalEuidsDiv").style.display="block";
				 }

		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="visible";
			 document.getElementById("previewPane").style.display="block";
		   }
 
		}

 	if(euidArray.length > 1 ) {
		if(showHistory == "true"   ) {
			if(document.getElementById("mergeEuidsDiv") != null ) {
					document.getElementById("mergeEuidsDiv").style.visibility="hidden";
					document.getElementById("mergeEuidsDiv").style.display="none"; 
			}
		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="hidden";
			 document.getElementById("previewPane").style.display="none";
		   }
		} else {
	  			 if(document.getElementById("mergeEuidsDiv") != null ) {
					document.getElementById("mergeEuidsDiv").style.visibility="visible";
					document.getElementById("mergeEuidsDiv").style.display="block"; 
				 }

		   //display or hide preview pane
		   if(document.getElementById("previewPane") != null ) {
			 document.getElementById("previewPane").style.visibility="visible";
			 document.getElementById("previewPane").style.display="block";
		   }
		}
	}


} 

function accumilateLidSearchFieldsOnBlur(field,fullFieldName,mask,valueType,formName) {
    var maskChars = new Array();
    var str = mask;
    str  = str.replace(/D/g,"");
    
    maskChars = str.split('');
    
    var valueEntered =  "";
    var valueTypeLocal =  "";
    valueEntered =  field.value;
    valueTypeLocal =  valueType;
    
    //valueEntered  = valueEntered.replace(/\)/g,"");
    //valueEntered  = valueEntered.replace(/\(/g,"");
    
    if(valueTypeLocal == '6' || valueTypeLocal == '8' ) {
      valueEntered =  field.value;
    } else {
      for(var i=0;i<maskChars.length;i++) {
        valueEntered  = valueEntered.replace(maskChars[i],'');
      }    
    }

    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+valueEntered+">>";
    document.getElementById(formName+":newSOEnteredFieldValues").value = fieldNameValues;

}


function accumilateFormFieldsOnBlur(field,fullFieldName,mask,valueType,formName) {
    var maskChars = new Array();
    var str = mask;
    str  = str.replace(/D/g,"");
    
    maskChars = str.split('');
    
    var valueEntered =  "";
    var valueTypeLocal =  "";
    valueEntered =  field.value;
    valueTypeLocal =  valueType;
    
    //valueEntered  = valueEntered.replace(/\)/g,"");
    //valueEntered  = valueEntered.replace(/\(/g,"");
    
    if(valueTypeLocal == '6' || valueTypeLocal == '8' ) {
      valueEntered =  field.value;
    } else {
      for(var i=0;i<maskChars.length;i++) {
        valueEntered  = valueEntered.replace(maskChars[i],'');
      }    
    }

    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+valueEntered+">>";
    document.getElementById(formName+":enteredFieldValues").value = fieldNameValues;
}

function accumilateFormSelectFieldsOnBlur(formName,field,fullFieldName) {
    var selectedValue = field.options[field.selectedIndex].value;
    
    if(fieldNames != fullFieldName+':') {
       fieldNames+=fullFieldName+':';
    }
    fieldNameValues += fullFieldName + "##"+selectedValue+">>";
    document.getElementById(formName+":enteredFieldValues").value = fieldNameValues;
}


function ClearMinorObjectContents(divElement)  { 
	 //var divElement = document.getElementById('Address')
	var forms = divElement.forms;
	for (var i=0;i<forms.length;i++)   {
		var thisForm = forms[i]
	    for (var j = 0; j<thisForm.elements();j++)    {
		   //checkthisField here
			if(!thisFrm.elements[i].name != 'lidmask') {
			   thisFrm.elements[i].value = "";
	    	}
		}
	}
   /*
	for(var j=0;j<minorObjectDiv.forms.length;j++) {
    var thisFrm = minorObjectDiv.forms[thisForm];

   for(i=0; i< thisFrm.elements.length; i++)   {      
        if(!thisFrm.elements[i].name != 'lidmask') {
           thisFrm.elements[i].value = "";
	}
    }
	}
	*/
    return;
} 

function getFormValues(formName)   {
  var thisFrm = document.getElementById(formName);
  var query = "";
   for(i=0; i< thisFrm.elements.length; i++)   {      
			if(thisFrm.elements[i].title.length != 0 ) {
				query +="&"+thisFrm.elements[i].title +"="+  thisFrm.elements[i].value;

				//fix for issue # 14 for handlling '+' to  standardization
				 if(query .indexOf('+') != -1) {
   		          query  = query.replace("+","%2B");
  	            }
	    	}
    }

	query +="&editThisID="+ editIndexid;
    queryStr  = query;

}

function getDuplicateFormValues(formName1,formName2)   {
  var thisFrm1 = document.getElementById(formName1);
  var thisFrm2 = document.getElementById(formName2);
  

  var query = "";
   for(i=0; i< thisFrm1.elements.length; i++)   {      
			if(thisFrm1.elements[i].title.length != 0 ) {
				query +="&"+thisFrm1.elements[i].title +"="+  thisFrm1.elements[i].value;
	    	}
    }

   for(i=0; i< thisFrm2.elements.length; i++)   {      
			if(thisFrm2.elements[i].title.length != 0 ) {
				query +="&"+thisFrm2.elements[i].title +"="+  thisFrm2.elements[i].value;
	    	}
    }

	query +="&editThisID="+ editIndexid;

    queryStr  = query;
 }


function getFormValuesCancelMerge(formName)   {
    rowCountMerge = "";
    destinationEOFinalMerge ="";
    previewhiddenMergeEuidsFinalMerge ="";
    mergeEuidsPreview="";

  var thisFrm = document.getElementById(formName);
  

  var query = "";
   for(i=0; i< thisFrm.elements.length; i++)   {      
			if(thisFrm.elements[i].title.length != 0 ) {
				query +="&"+thisFrm.elements[i].title +"="+  thisFrm.elements[i].value;
	    	}
    }

	query +="&editThisID="+ editIndexid;
    queryStr  = query;
}

function getFormValuesMerge(formName)   {
  var thisFrm = document.getElementById(formName);
  

  var query = "";
   for(i=0; i< thisFrm.elements.length; i++)   {      
			if(thisFrm.elements[i].title.length != 0 ) {
				query +="&"+thisFrm.elements[i].title +"="+  thisFrm.elements[i].value;
	    	}
    }

	query +="&editThisID="+ editIndexid;
    
	query +="&destnEuidValue="+ destinationEOFinalMerge;
	query +="&sourceEuids="+ previewhiddenMergeEuidsFinalMerge;
	query +="&rowcount="+ rowCountMerge;
    queryStr  = query;

rowCountMerge = "";
destinationEOFinalMerge ="";
previewhiddenMergeEuidsFinalMerge="";
mergeEuidsPreview="";

}
function accumilateMultiMergeEuidsPreviewDuplicates(fac,count,mergeEuidVar,arrlInnerSize) {
var preview  = document.getElementById('previewEuidDiv'+fac);

 var found = "";
        var allEUIDs = [];
        var allEUIDsCheck = [];
        var disableArray = [];
        for (var i=0; i<alleuidsArray.length; i++) { 
            allEUIDs[i] = alleuidsArray[i];
            allEUIDsCheck[i] = alleuidsArray[i];
         }

         //If the destination EO is removed from the selection remove all selections and reset the array of euids
        if (euidArray.length > 1 && euidArray[0] == mergeEuidVar+':'+fac)   {
			for(var i=0;i<arrlInnerSize;i++) {
             for (var j=0; j<euidArray.length; j++) {            
 				if(document.getElementById('mainEuidContentDiv'+euidArray[j]+i) != null) {
                   document.getElementById('mainEuidContentDiv'+euidArray[j]+i).className = 'yellow';			   
				}
  	          }
			}
		    //Deselect and reset the array size here
             euidArray = [];
			 preview.className = 'yellow';
        } else {
          for (var i=0; i<euidArray.length; i++) {            
            if (euidArray[i] == mergeEuidVar+':'+fac)   {
                euidArray.splice(i,1);
                found = "true";
               }
           }
 
            if (found != "true")    {
               euidArray.push(mergeEuidVar+':'+fac);
            }  
 		    
			var tab = document.getElementById('mainEuidContentDiv'+mergeEuidVar+":"+fac+count);
            if (tab.className == 'yellow')   {
               tab.className = 'blue'; 
             } else {
               tab.className = 'yellow';
             }
 
             preview.className = 'blue';

 		}
  
  
        mergeEuidsPreview+=mergeEuidVar+'##';     

       var mainEuidArray = mergeEuidsPreview.split("##");
       var mainEuid = mainEuidArray[0];
		 var finalEuidsArray = [];

		 for(var i = 0 ; i< euidArray.length ; i++) {
			  var valueArray  = euidArray[i].split(":");
			  if(valueArray[1] == fac) {
                finalEuidsArray.push(valueArray[0]);
			  }
		 }
   
       if(finalEuidsArray.length > 1) {
         rowCountMerge  = fac;
         destinationEOFinalMerge = mainEuid;
         previewhiddenMergeEuidsFinalMerge = mergeEuidsPreview;
 
		 document.getElementById('PREVIEW_SRC_DESTN_EUIDS'+fac).value = finalEuidsArray;
		 document.getElementById('MERGE_SRC_DESTN_EUIDS'+fac).value = finalEuidsArray;
		 document.getElementById('PREVIEW_DIVS'+fac).value = previewEuidDivs;
         
		 //buttonsDiv<%=fac%>
         document.getElementById('buttonsDiv'+ fac).style.visibility = "visible";
         document.getElementById('buttonsDiv'+ fac).style.display = "block";

     } else {
         document.getElementById('buttonsDiv'+ fac).style.visibility = "hidden";
         document.getElementById('buttonsDiv'+ fac).style.display = "none";
	 }

}
//multiMergeEuidsPreview
function multiMergeEuidsPreview(formName)   {
  var thisFrm = document.getElementById(formName);
  var query = "";
   for(i=0; i< thisFrm.elements.length; i++)   {      
			if(thisFrm.elements[i].title.length != 0 ) {
				query +="&"+thisFrm.elements[i].title +"="+  thisFrm.elements[i].value;
	    	}
    }

	query +="&editThisID="+ editIndexid;
    
	query +="&destnEuidValue="+ destinationEOFinalMerge;
	query +="&sourceEuids="+ previewhiddenMergeEuidsFinalMerge;
	query +="&rowcount="+ rowCountMerge;
    queryStr  = query;

 
}




function showMinorObjectsDiv(divId) {
	//animateDiv(divId,document.getElementById(divId).style.height)
    if (document.getElementById(divId).style.visibility == 'visible')    {
        document.getElementById(divId).style.visibility = "hidden";
        document.getElementById(divId).style.display = "none";
    } else {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
	}
}

  function animateDiv(divId,thisHeight) {
      var attributes = { 
	                height : { to: 500 },
	                width : { to: 1025 }
                   };
	  var anim = new YAHOO.util.Anim(divId,attributes);
	  anim.animate();
}

/* Used for edit EO on Edit main EUID*/
  var editEOIndexid = "-1";
  function setEOEditIndex(editIndex)   {
	editEOIndexid = editIndex;
  }


function getRecordDetailsFormValues(formName)   {
  var thisFrm = document.getElementById(formName);
 
  var query = "";
   for(i=0; i< thisFrm.elements.length; i++)   {      
		if(thisFrm.elements[i].title.length != 0 ) {
				query +="&"+thisFrm.elements[i].title +"="+  thisFrm.elements[i].value;
	            if(query .indexOf('%') != -1) {
   		          query  = query.replace("%","~~");
  	            }
 	    }
    }

	query +="&editThisID="+ editIndexid;
    queryStr  = query;
  }

 /* Opens a new window for the print*/
function openPrintWindow(url)    {
   var newwindow=window.open(url,"_blank","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes");
   newwindow.focus();
}

function checkDuplicateFileds(formName,field,message) {
	var thisFrm = document.getElementById(formName);
    for(i=0; i< thisFrm.elements.length; i++)   {      
		thisFrm.elements[i].style.backgroundColor ="#ffffff";
		thisFrm.elements[i].style.border  ="1px solid #7f9db9";
		document.getElementById("duplicateIdsDiv").innerHTML = "";
        if(document.getElementById("duplicateLid") != null ) {
		  document.getElementById("duplicateLid").value="";
		}
    }
    for(i=0; i< thisFrm.elements.length; i++)   {      
	  if(thisFrm.elements[i].value.length !=0 &&  field.title  != thisFrm.elements[i].title ) {
		if(field.value == thisFrm.elements[i].value) {
 	     document.getElementById("duplicateIdsDiv").innerHTML = field.title + " '" + field.value + "' "+ message;
 		 field.value = "";
		 field.focus(); 
		 field.style.backgroundColor ="#f7fbd6";
		 field.style.border  ="1px solid #d75e33"; 
		 //duplicateLid = true;duplicateLidMessage
         if(document.getElementById("duplicateLid") != null ) {
		   document.getElementById("duplicateLid").value = field.title + " '" + field.value + "' "+ message;
		 }
  	    }
	  }
  }

}

function populateContents(thisForm,thistitle,thisvalue)  { 
 thisFrm = document.forms[thisForm];
 for(i=0; i< thisFrm.elements.length; i++)   {        
	if (thisFrm.elements[i].title == thistitle) 		   {
			   thisFrm.elements[i].value = thisvalue;
	}
 }
 return;
}

function makeDraggable(divId) {
 	    var dd;
        YAHOO.util.Event.onDOMReady(function() {
            dd = new YAHOO.util.DD(divId);
         }); 
}


//enable all the fields in the form 
function enableallfields(thisForm)  { 
 thisFrm = document.forms[thisForm];
 for(i=0; i< thisFrm.elements.length; i++)   {        
     thisFrm.elements[i].readOnly = false;
     thisFrm.elements[i].disabled = false;

  }
 return;
}
 

 function showUnmergeViewSources(totalSources)  {
	var divId;
	for(var i = 0 ; i <totalSources ; i ++) {
		divId = 'unmergepreviewmainDupSources'+i;
	 	if (document.getElementById(divId).style.visibility == 'visible')    {
			document.getElementById(divId).style.visibility = "hidden";
			document.getElementById(divId).style.display = "none";
		} else {
			document.getElementById(divId).style.visibility = "visible";
			document.getElementById(divId).style.display = "block";
		}
	}

}
 

 function showUnmergeViewSources(totalSources)  {
	var divId;
	for(var i = 0 ; i <totalSources ; i ++) {
		divId = 'unmergepreviewmainDupSources'+i;
	 	if (document.getElementById(divId).style.visibility == 'visible')    {
			document.getElementById(divId).style.visibility = "hidden";
			document.getElementById(divId).style.display = "none";
		} else {
			document.getElementById(divId).style.visibility = "visible";
			document.getElementById(divId).style.display = "block";
		}
	}

}
