
var radix = 10;
var dateLastKey;
var timeLastKey;
var ssnLastKey;
var phoneLastKey;

//Modified by Sridhar Narsingh
// Ligature Software Pvt. Ltd Bangalore http://www.ligaturesoftware.com
//Modified Date:11/19/07
//Commented the existing code to check the numbers and instead used isNaN
function isAllDigits(str) {
   if (isNaN(str))  {
       return false;    
   } else {
      return true;
   }

   /*var validChars = "0123456789";

  for (var i = 0; i < str.length; i++) {
      if (validChars.indexOf(str.charAt(i)) < 0)   {
          alert("returning false");
         return false;             
      }
      
   }
   return true;*/
}

function validate_phone(field, label) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

    var elems = field.value.split("-");  // should contains 2 components (###-#######)
    var result = (elems.length == 2);
    if (result) {
  result = (isAllDigits(elems[0]) && elems[0] != "000" && elems[0].length == 3 &&
      isAllDigits(elems[1]) && elems[1].length == 7 );
    }
    if (!result) {
  alert("Please enter a phone number in the format ###-######## for the " + label + " field.");
  field.focus();  // go to current field.
    }
}

function validate_ssn(field, label) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

    var elems = field.value.split("-");  // should contains 3 components
    var result = (elems.length == 3);
    if (result) {
  result = (isAllDigits(elems[0]) && elems[0] != "000" && elems[0].length == 3 &&
      isAllDigits(elems[1]) && elems[1].length == 2 &&
      isAllDigits(elems[2]) && elems[2].length == 4 );
    }
    if (!result) {
  alert("Please enter a Social Security Number in the format ###-##-#### for the " + label + " field.");
  field.focus();  // go to current field
    }
}
function validate_localid(field, label) {
    if (field.value == "")  // if nothing entered, do not validate
  return;
    var elems = field.value.split("-");  // should contains 3 components
    var result = (elems.length == 3);
    if (result) {
      result = (isAllDigits(elems[0]) && elems[0] != "000" && elems[0].length == 3 &&
      isAllDigits(elems[1]) && elems[1].length == 3 &&
      isAllDigits(elems[2]) && elems[2].length == 4 );
    }
    
    if (!result) {
          alert("Please enter a Local Id in the format DDD-DDD-DDDD for the " + label + " field.");
          field.focus();  // go to current field
    }
}

function validate_number(field, lower, upper, label) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

    var val = parseInt(field.value, radix);
    if (isNaN(val)) { // not numeric character
  alert("Please enter a numeric value for the " + label + " field.");
  field.focus();  // go to current field
  return;
    }
    if (val < lower || val > upper) {
  alert("Please enter a value in the range of " + lower + " to " + upper + " for the " + label + " field.");
  field.focus();  // go to current field
  return;
    }
}

function validate_time(field, label) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

    var result = true;
    var elems = field.value.split(":");
    result = (elems.length == 3);  // should be three componts
    if (result) {
  var hour = parseInt(elems[0], radix);
  var min = parseInt(elems[1], radix);
  var sec = parseInt(elems[2], radix);
  result = (!isNaN(hour) && hour >= 0 && hour <= 23 && !isNaN(min) && min >= 0 && min <= 59 && 
      !isNaN(sec) && sec >= 0 && sec <= 59);
    }
    if (!result) {
  alert("Please enter the time in the format HH:MM:SS for the " + label + " field");
  field.focus();  // go to current field
    }
}

function validate_date(field, format) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

    var result = true;
    var elems;
    var symbols;

    if (format.indexOf("/") > -1) {
        elems = field.value.split("/");
        symbols = format.split("/");
    }
    if (format.indexOf(".") > -1) {
        elems = field.value.split(".");
        symbols = format.split(".");
    }
    if (format.indexOf("-") > -1) {
        elems = field.value.split("-");
        symbols = format.split("-");
    }

    result = (elems.length == 3);  // should be three componts
   /* if (!result) {
  	alert("Please enter a date in " + format + " format.");
	field.focus();  // go to current filed
	return;
    }
    */

    var month = 0;
    var day = 0;
    var year = 0;
    var yearString = null;
    
    // get day/month/year value from the supported format
    if (symbols[0] == "MM" && symbols[1] == "dd" && symbols[2] == "yyyy") {
    		month = parseInt(elems[0], radix);
    		day = parseInt(elems[1], radix);
    		year = parseInt(elems[2], radix);
    		yearString = elems[2];
    } else if (symbols[0] == "dd" && symbols[1] == "MM" && symbols[2] == "yyyy") {
    		month = parseInt(elems[1], radix);
    		day = parseInt(elems[0], radix);
    		year = parseInt(elems[2], radix);
    		yearString = elems[2];
    } else if (symbols[0] == "yyyy" && symbols[1] == "MM" && symbols[2] == "dd") {
    		month = parseInt(elems[1], radix);
    		day = parseInt(elems[2], radix);
    		year = parseInt(elems[0], radix);
    		yearString = elems[0];
    } else if (symbols[0] == "yyyy" && symbols[1] == "dd" && symbols[2] == "MM") {
    		month = parseInt(elems[2], radix);
    		day = parseInt(elems[1], radix);
    		year = parseInt(elems[0], radix)
    		yearString = elems[0];
    } else {
  	alert("Date format " + format + " is not supported.");
	field.focus();  // go to current filed
	return;
    }

    // if year, month, or day is not numeric or year is not 4 digits
    if (isNaN(month) || isNaN(day) || isNaN(year) || yearString.length != 4 ) {
        alert("Please enter a date in " + format + " format.");
        field.focus();  // go to current field
        return;
    }

    if (month <= 0 || month >= 13) {
	alert("Invalid month is entered.");
	field.focus();  // go to current filed
	return;
    }

    // calculate the correct day limit for the month
    var upperDay;
    switch (month) {
  case 1:
  case 3:
  case 5:
  case 7:
  case 8:
  case 10:
  case 12:
      upperDay = 31;
      break;
  case 4:
  case 6:
  case 9:
  case 11:
      upperDay = 30;
      break;
  case 2:
      // if leap year 29, otherwise 28
      if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
    upperDay = 29;
      else
    upperDay = 28;
      break;
  default:
      break;
    }

    if (day <= 0 || day > upperDay) { 
	alert("Invalid day of the month is entered.");
	field.focus();  // go to current field
	return;
    }
}

function validate_name(field, len, label) {
   if (field.value == "")  // if nothing is entered, do not validate
  return;
   if (field.value.length > len) {
  alert("Please enter a name less or equal to " + len + " characters in the " + label + " field.");
  field.focus();  // go to current field
  return;
   }
   var invalidChars = "*|,\":<>[]{}`\';()@&$#%"; // name field should not cntains such characters

   for (var i = 0; i < field.value.length; i++) {
      if (invalidChars.indexOf(field.value.charAt(i)) != -1) {
         alert("Invalid character is entered in the " + label + " field.");
     field.focus();  // go to current field
     return;
      }
   }

}

function add_date_slash(field) {
    if ((field.value.length == 2 || field.value.length == 5) && dateLastKey != "/") {
    field.value = field.value + "/";  // add '/' automatically
    }
    // keep the last char while BK space is pressed to wipe out the letter
    dateLastKey = field.value.substring(field.value.length - 1);
}

function add_time_colon(field) {
    if ((field.value.length == 2 || field.value.length == 5) && timeLastKey != ":") {
    field.value = field.value + ":"; // add ':' automatically
    }
    // keep the last char while BK space is pressed to wipe out the letter
    timeLastKey = field.value.substring(field.value.length - 1);
}

function add_phone_dash(field) {
    if (field.value.length == 3 && phoneLastKey != "-") {
    field.value = field.value + "-";  // add '-' automatically
    }
    // keep the last char while BK space is pressed to wipe out the letter
    phoneLastKey = field.value.substring(field.value.length - 1);
}

function add_ssn_dash(field) {
    if ((field.value.length == 3 || field.value.length == 6) && ssnLastKey != "-") {
        field.value = field.value + "-"; // add '-' automatically
    }
    // keep the last char while BK space is pressed to wipe out the letter
    ssnLastKey = field.value.substring(field.value.length - 1);

}

var global_released=true;
var global_old_value="";
var global_mask="";
var global_alerting=false;
var global_errored=false;
var global_nchar_added=0;
var global_field=0;

function qws_field_on_key_down(field, mask) {
    global_mask=mask;
    global_field=field;
    if(mask == null || mask.length == 0) return;
    if(! global_released) {
      return;
    }
    global_released=false;
    global_old_value= field.value;
    global_alerting=false;
}

var global_count=0;

function qws_field_on_key_up(field) {
    if(field != global_field) return;
    if(global_mask == null || global_mask.length == 0) return;
    global_released=true;
    if(field.value == global_old_value) {
      return;
    }
    var oldvalue=global_old_value;
	if (field.value.length == 0 )
	{
		oldvalue=""; //reset - commented by Sridhar
	} 
    var pos1=getNewCharPosL(field.value, oldvalue);
    // if too many chars entered before releasing key; take the first char that's different
    // from old value and place the others on queue
    var queue="";
    if(field.value.length > 1+oldvalue.length) {
/*
        queue=field.value.substring(pos1+1, pos1+field.value.length-oldvalue.length);
        field.value=oldvalue.substring(0, pos1) + field.value.charAt(pos1) + oldvalue.substring(pos1);
          if(queue.length>1)
          alert("q="+queue+"v="+field.value+"o="+oldvalue+"l="+queue.length);
*/
        add_if_needed(field, global_mask)==true;
				if(field.value.length>global_mask.length) {
          field.value=field.value.substring(0, global_mask.length); // enforce the max length
				}
        return;
    }

    global_errored=false;
    global_count=0;
    global_nchar_added=0;
    apply_mask(field, oldvalue);

/*
    // check if there is anything in queue
    while(queue.length>0 && global_errored==false) {
      oldvalue=field.value;
      field.value=field.value.substring(0, pos1+1+global_nchar_added) + queue.charAt(0) 
                + field.value.substring(   pos1+1+global_nchar_added);
      queue = queue.substring(1);
      global_nchar_added=0;
      apply_mask(field, oldvalue);
    }
*/

}


function apply_mask(field, oldvalue) {
    var etype=getEventType(field, oldvalue);
    var mask=global_mask;
    var pos1=getNewCharPosL(field.value, oldvalue);
    var pos2=getNewCharPosR(field.value, oldvalue);
    var cur_char=field.value.charAt(pos1);
    var cur_type=mask.charAt(pos1);
    var next_type=mask.charAt(pos1+1);
    switch(etype) {
      case 0:  // delete at end
      case 1:  // delete at other locations
        if(pos1+pos2< field.value.length) { // added 1 char after deleting selected chars
          if(isMatch(cur_char, cur_type)) {
            break;
          } else {
            global_alerting=true;
            //alert(describe(cur_type)+" required"); //Commented by Sridhar 11/20/2007
            global_errored=true;
            global_alerting=false;
            field.value=oldvalue;
            break;
          }
        }
        break;
      case 2:  // replace 1 char
        if(isMatch(cur_char, cur_type)) { // new char is valid
          break;
        } else {
          global_alerting=true;
          //alert(describe(cur_type)+" required");
          global_errored=true;
          global_alerting=false;
          field.value=oldvalue;
          break;
        }
        break;
      case 3:  // append 1 char
        if(field.value.length>mask.length) {
          field.value=field.value.substring(0, mask.length); // enforce the max length
          break;
        }
        if(isMatch(cur_char, cur_type)) { // new char is valid
          if(field.value.length<mask.length && isLiteral(next_type)) {
            field.value=field.value + next_type;  // add the next literal
            global_nchar_added++;
            global_count++;
          }
          break;
        } else { // new char invalid
          if(isLiteral(cur_type)) { // if literal missing, insert it and check again
            field.value = oldvalue + cur_type;
            var oldvalue= field.value;
            field.value += cur_char;
            global_nchar_added++;
            apply_mask(field, oldvalue);
            break;
          } else {
            global_alerting=true;
            //alert(describe(cur_type)+" required");
            global_errored=true;
            global_alerting=false;
            field.value=oldvalue;
            break;
          }
        }
        break;
      case 4:  // insert 1 char in front
      case 5:  // insert 1 char at other locations
      default:
        var head=oldvalue.substring(0, pos1);
        var tail=oldvalue.substring(pos1+1);
        if(isMatch(cur_char, cur_type)) {
          if(field.value.length>mask.length) {
            field.value = head + cur_char + tail;
          }
          break;
        } else {
            global_alerting=true;
            //alert(describe(cur_type)+" required");
            global_errored=true;
            global_alerting=false;
            field.value=oldvalue;
            break;
        }
        break;
    }
    return;
}

// return the # of chars from the left that are the same
function getNewCharPosL(value, oldvalue) {
    var i=0;
    for(i=0; i<value.length && i<oldvalue.length; i++) {
      if(value.charAt(i) != oldvalue.charAt(i)) {
        break;
      }
    }
    return i;
}

// return the # of chars from the right that are the same
function getNewCharPosR(value, oldvalue) {
    var i=value.length-1;
    var j=oldvalue.length-1;
    var k=0;
    for(k=0; i>=0 && j>=0; i--, j--, k++) {
      if(value.charAt(i) != oldvalue.charAt(j)) {
        break;
      }
    }
    return k;
}

function getEventType(field, oldvalue) {
    var value=field.value;
    pos1=getNewCharPosL(value, oldvalue);
    pos2=getNewCharPosR(value, oldvalue);

    if(value.length > 1+oldvalue.length) {
        alert("java script error");
        return;
    } else if(value.length < oldvalue.length) { // char delete; possible replacement with 1 char
        if(pos1+pos2 > oldvalue.length) {
          pos2= oldvalue.length-pos1;
        }
        if(pos1==value.length) {
          return 0; // delete at the end
        } else {
          return 1; // other delete
        }
    } else if(value.length == oldvalue.length) { // replacement of 1 char
        return 2;
    } else if(value.length == (1+oldvalue.length)) { // insert/append 1 char
        if(pos1==oldvalue.length) {
          return 3; // append 1 char
        } else if(pos1==0) {
          return 4; // insert in front 1 char
        } else {
          return 5; // other insert
        }
    }
}

function isMatch(ch, type) {
    switch(type) {
      case 'A':
        if(!(ch>='A' && ch<='Z' || ch>='a' && ch<='z' || ch>='0' && ch<='9')) {
      return false;
        }
        break;
      case 'L':
        if(!(ch>='A' && ch<='Z' || ch>='a' && ch<='z')) {
      return false;
        }
        break;
      case 'D':
        if(!(ch>='0' && ch<='9')) {
      return false;
        }
        break;
      default:
        if(ch!=type) {
      return false;
        }
        break;
    }
    return true;
}

function isLiteral(type) {
    return !(type=='A' || type=='L' || type=='D');
}

function qws_field_on_blur(field, mask) {
    if(mask == null || mask.length == 0) return;
    if(global_alerting || field.value.length==0) {
          return;
    }
    var i=findMismatch(field.value, mask);
    if(i<mask.length || i<field.value.length) {
      alert("Field value does not match input mask <" + mask + ">");
      field.select();
      field.focus();
    }
}

function findMismatch(value, mask) {
        var valid=true;
        for(i=0; i<value.length && i<mask.length; i++) {
            if(! isMatch(value.charAt(i), mask.charAt(i))) {
              valid=false;
              break;
            }
        }
        return i;
}

function describe(type) {
  switch(type) {
      case 'A': return "alphanumeric";
      case 'L': return "letter";
      case 'D': return "number";
      default:  return ""+type;
  }
}

function add_if_needed(field, mask) {
        var idx=findMismatch(field.value, mask);
        if(idx<field.value.length) { // find mismatch, return the index
          if(isLiteral(mask.charAt(i))) {
              field.value=field.value.substring(0, idx) + mask.charAt(idx) + field.value.substring(idx);
              if(field.value.length<=mask.length && add_if_needed(field, mask)) {
                  return true;
              } else {
                  return false;
              }
          } else {
              return false;
          }
        } else {
          return true;
        }
}
function setchanged(checkFieldName) {
       if (checkFieldName.length > 0) {
          var e = document.getElementById(checkFieldName + "id");
          e.checked = "true";
       }
       document.AuditSearchForm.fieldchanged.value = "true";
}

function validate_Integer_fields(field,  label,valueType) {
    if (field.value == "")  // if nothing entered, do not validate
  return;

   var val ;

    var s = field.value;
	var startPos = 0;

   if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
            startPos = 1;

   if(valueType == '0') 
	  {
     var avalue = s.substring(startPos, s.length);

	    if (!isInteger(s.substring(startPos, s.length))) 
	 { 
        alert("Please enter a Integer value for the '" + label + "' field.");
        field.focus();  // go to current field
        return;
	  }

    }
	else if(valueType == '4'){

     if (!isInteger(s.substring(startPos, s.length))) 
	 { 
        alert("Please enter a long value for the '" + label + "' field.");
        field.focus();  // go to current field
        return;
	  }

	}
	else if( valueType == '7' ){
		
        if(!isFloat(s.substring(startPos, s.length))){
         alert("Please enter float value for the '" + label + "' field.");
		field.focus();  // go to current field
        return;
     }



	}

}


   
    function isInteger (s)
   {
      var i;

      if (isEmpty(s))
      if (isInteger.arguments.length == 1) return 0;
      else return (isInteger.arguments[1] == true);

      for (i = 0; i < s.length; i++)
      {
       
		 var c = s.charAt(i);

         if (!isDigit(c)) return false;
      }

      return true;
   }



function isFloat(s)
 
 {
    

   var str = s.indexOf('.');

 var startPos = 0;
//   if(str != -1){
 
   var s1 = s.substring(startPos, str);
   var s2 = s.substring(str+1, s.length);


  if(s1 .length !=0){

      for (i = 0; i < s1.length; i++)
      {
    
	  var c = s1.charAt(i);
    
      if (!isDigit(c)) return false;

      }
   }

 if(s2 .length !=0){
	   for (i = 0; i < s2.length; i++)
      {
    
	  var c = s2.charAt(i);
    
      if (!isDigit(c)) return false;

      }
   }
   
//   }else
//	{
//   return false;
//   }
      return true;
   }
   




   	  function isEmpty(s)
   {
      return ((s == null) || (s.length == 0))
   }

     
	 
	 function isDigit (c)
   {
      return ((c >= "0") && (c <= "9"))
   }


function clear_masking_on_focus() {
    global_field="";
    global_old_value= "";
}

