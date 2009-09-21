/*
 * Origianl code from http://liferay.com/
 */

function ColorPicker (name,iDir) {
	var imgDir = iDir;
	var cp = $(""+name+'_p_d');
	var chooser = $(""+name+'_c');
	var chooserImg = $(""+name+'_c_i');
	var image = $(""+name+"_p_d_i");
	var textInput = $(""+name);
	var colorfield = $(""+name+'_p_d_s');
	var originalValue = "";
	var self = this;
/*	cp.style.height = "192px";
	cp.style.width = "100px";
	cp.style.position = "absolute";
	cp.style.display = "none";
	cp.style.cursor= "crosshair";
	cp.style.zIndex = 1;
	image.style.height = "192px";
	image.style.width = "100px"; */
/*
 * Public methods
 */
    this.blank = function() {
    	textInput.value = "";
    	chooserImg.src = imgDir+"/nocolor.gif";
    	chooser.style.backgroundColor = "#FFF";
    	self.hide();
    }
    
	this.hide = function () {
			cp.style.display = "none";
		};
	this.toggle = function () {
		if (cp.style.display == "none") {
            if(!cp.style.position || cp.style.position=='absolute') {
	        	cp.style.position = 'absolute';
	        	Position.clone(chooser, cp, {setHeight: false, setWidth : false, offsetTop: chooser.offsetHeight});
	        }
	        cp.style.display = "block";
	        originalValue = textInput.value;
	    } else {
	        self.hide();
	    }    
	};
	/*
	 * Private methods
	 */
 
	var getColor = function (event) {
		var nwOffset = Position.cumulativeOffset(image);
		
		//fix the event for IE
		var ev = event;
		if(typeof ev == 'undefined')
		    ev = window.event;
		
		var x = Event.pointerX(ev) - nwOffset[0];
		var y = Event.pointerY(ev) - nwOffset[1];
		var rmax = 0;
		var gmax = 0;
		var bmax = 0;
		if (y <= 32) {
		rmax = 255;
		gmax = (y / 32.0) * 255;
		bmax = 0;
		} else if (y <= 64) {
		y = y - 32;
		rmax = 255 - (y / 32.0) * 255;
		gmax = 255;
		bmax = 0;
		} else if (y <= 96) {
		y = y - 64;
		rmax = 0;
		gmax = 255;
		bmax = (y / 32.0) * 255;
		} else if (y <= 128) {
		y = y - 96;
		rmax = 0;
		gmax = 255 - (y / 32.0) * 255;
		bmax = 255;
		} else if (y <= 160) {
		y = y - 128;
		rmax = (y / 32.0) * 255;
		gmax = 0;
		bmax = 255;
		} else {
		y = y - 160;
		rmax = 255;
		gmax = 0;
		bmax = 255 - (y / 32.0) * 255;
		}
		if (x <= 50) {
		var r = Math.abs(Math.floor(rmax * x / 50.0));
		var g = Math.abs(Math.floor(gmax * x / 50.0));
		var b = Math.abs(Math.floor(bmax * x / 50.0));
		} else {
		x -= 50;
		var r = Math.abs(Math.floor(rmax + (x / 50.0) * (255 - rmax)));
		var g = Math.abs(Math.floor(gmax + (x / 50.0) * (255 - gmax)));
		var b = Math.abs(Math.floor(bmax + (x / 50.0) * (255 - bmax)));
		}
		return rgb2hex(r, g, b);
		};
		
	var rgb2hex = function (r, g, b) {
		color = '#';
		color += hex(Math.floor(r / 16));
		color += hex(r % 16);
		color += hex(Math.floor(g / 16));
		color += hex(g % 16);
		color += hex(Math.floor(b / 16));
		color += hex(b % 16);
		return color;
	};
	var hex = function (dec){
		return (dec).toString(16);
	};
	var onEnd = function (event) {
		var color = getColor(event, image);
        chooserImg.src = imgDir+'/arrowdown.gif';
		originalValue = color;
		textInput.value = color;
		chooser.style.backgroundColor = color;
		self.hide();
		if(textInput.onchange) {
			textInput.onchange();
		}
	};
	var onMove = function (event) {
		var color = getColor(event, image);
//		textInput.value = color;
		colorfield.style.backgroundColor = color;
		colorfield.firstChild.data = color;
	};
	var reset = function () {
		textInput.value = originalValue;
		colorfield.style.backgroundColor = originalValue;
	};
/*
 * Events
 */
 
image.onmousemove = onMove;
image.onclick = onEnd;
image.onmouseout = reset;
}

/*
 * Validates the ColorPicker.
 */
function validateColorPicker(id, required, msgs){
	var field = document.getElementById(id);
	if(field){
		var value = field.value;
		if(value.length == 0){
			if(required){
				setFieldErrorColor(field);
				return msgs[0];
			}
		} else if(!field.value.match(new RegExp("^#[a-fA-F0-9]{3}([a-fA-F0-9]{3})?$"))){
			setFieldErrorColor(field);
			return msgs[1];
		}
		
		setFieldValidColor(field);
		return null;
		
	} else {
		return 'Field ' + id + ' not found.';
	}
}