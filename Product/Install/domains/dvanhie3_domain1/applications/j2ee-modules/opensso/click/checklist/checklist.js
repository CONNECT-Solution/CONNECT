
/*
 * Add a function to the window.onload event
 * without removing the old event functions.
 * 
 * Example usage:
 * addLoadEvent(function () {
 *	initChecklist();
 * });
 * 
 * See Simon Willison's blog 
 * http://simon.incutio.com/archive/2004/05/26/addLoadEvent
 */
function addLoadEvent(func) {
	var oldonload = window.onload;
	
	if (typeof window.onload != "function") {
		window.onload = func;
	} else {
		window.onload = function () {
			oldonload();
			func();
		}
	}
}

/* 
 * CheckList function. 
 * See  Nicolaus Rougeux 'Check it, don't select it
 * http://c82.net/article.php?ID=25 
 */  
function initChecklist(checklistid) {
	if (document.all && document.getElementById) {
		// Get all unordered lists
		var theList = document.getElementById(checklistid);
		if(theList != null) {
			var labels = theList.getElementsByTagName("label");
			
			// Assign event handlers to labels within
			for (var j = 0; j < labels.length; j++) {
				var theLabel = labels[j];
				theLabel.onmouseover = function() { this.className += " hover"; };
				theLabel.onmouseout = function() { this.className = this.className.replace(" hover", ""); };
			}
		}
	}
}

function validateCheckList(pathName, required, msgs){
	if(required){
		for (i = 0; i < pathName.length; i++) {
			if (pathName[i].checked) {
				return null;
			}
		}
		return msgs[0];
	}
}

