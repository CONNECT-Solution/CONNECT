var YUI4JSF = {};

YUI4JSF.HtmlParser = function() {
	
	YUI4JSF.HtmlParser.prototype.getFormContent = function(formId, htmlContent) {
		var formStartIndex = htmlContent.indexOf("<form id=\"" + formId + "\"");
		var	formEndIndex = htmlContent.indexOf("</form>");

		var rawContent = htmlContent.substring(formStartIndex, formEndIndex);	//contains form attributes
		var formStartTagEnd = rawContent.indexOf(">");		

		return rawContent.substring(formStartTagEnd + 1);
	}
}

YUI4JSF.AjaxRequestCallback = function(elements) {
	
	this.argument = elements;
	
	this.success = function(o) {
		var formId = o.argument[0];
		document.getElementById(formId).innerHTML = new YUI4JSF.HtmlParser().getFormContent(formId, o.responseText);
	}
	
	this.failure = function(o) {
		window.alert('Exception occured in YUI4JSF Ajax Transaction:' + o.statusText);
	}
	
	this.upload = function(o) {
		alert("File Uploaded");
		var formId = o.argument[0];
		document.getElementById(formId).innerHTML = new YUI4JSF.HtmlParser().getFormContent(formId, o.responseText);
	}
}

YUI4JSF.AjaxFileUploadCallback = function(elements) {
	
	this.argument = elements;
	
	this.upload = function(o) {
		var formId = o.argument[0];
		document.getElementById(formId).innerHTML = new YUI4JSF.HtmlParser().getFormContent(formId, o.responseText);
	}
}