/*
 * This script provides cookie manipulation functionality to the Click Tree control.
 * Two cookies are used to track the expanded nodes and collapsed nodes.
 * 
 * When node expands, the node's id is added to the first cookie and
 * removed form the second cookie. When a node is collapsed the id
 * is removed from the first cookie and added to the second.
 */

/*
 * Find and return the cookie with specified name
 */
function getCookie(name) {
    if (document.cookie.length>0) {
        var start=document.cookie.indexOf(name + "=");
        if (start!=-1) {
            start=start + name.length+1;
            end=document.cookie.indexOf(";",start);
            if (end==-1) {
                end=document.cookie.length;
            }
            return unescape(document.cookie.substring(start,end));
        } 
    }
    return "";
}

/*
 * Sets a new cookie with the specified parameters
 */
function setCookie(name, value, expires, path, domain, secure ) {
    var today = new Date();
    today.setTime(today.getTime());

    if (expires) {
        expires = expires * 1000 * 60 * 60 * 24;
    }

    var expireDate = new Date( today.getTime() + (expires) );
    document.cookie = name+'='+escape(value) +
    ((expires) ? ';expires='+expireDate.toGMTString() : '') + 
    ((path) ? ';path=' + path : '') +
    ((domain) ? ';domain=' + domain : '') +
    ((secure) ? ';secure' : '');
}

/*
 * This method is called from html. Add or remove nodeId
 * from the cookie values.
 */
function handleCookie(object, event, expandId, nodeId, expandedCookieName, collapsedCookieName) {
    stopPropagation(event);
    var span = document.getElementById(expandId);

    var modified=false;
    if(isClassExpanded(span)) {
        addCookieValue(expandedCookieName, nodeId);
        removeCookieValue(collapsedCookieName, nodeId);
    } else {
        removeCookieValue(expandedCookieName, nodeId);
        addCookieValue(collapsedCookieName, nodeId);
    }
}

/*
 * Add cookie value to the specified cookie. Returns
 * true if the cookie was modified, false otherwise
 */
function addCookieValue(expandedCookieName, value) {
    var cookie = getCookie(expandedCookieName);
    var result='';

    if(cookie == null || cookie.length == 0 || cookie.indexOf('""') == 0) {
        result='"'+value+'"';
    } else {
        //remove any quotes
        cookie=cookie.replace(/"/g,'');

        var ar = cookie.split(',');

        //check incase of duplicates
        if(arrayContains(ar,value)){
            return false;
        }
        result='"'+cookie+','+value+'"';
    }

    setCookie(expandedCookieName, result, 0, '/');
    return true;
}

/*
 * Remove cookie value from the specified cookie. Returns
 * true if the cookie was modified, false otherwise
 */
function removeCookieValue(expandedCookieName, value) {
    var cookie = getCookie(expandedCookieName);
    if(cookie == null || cookie.length <= 0 || cookie.indexOf('""') == 0) {
        return false;
    }
    //remove any quotes
    cookie=cookie.replace(/"/g,'');
    var ar=cookie.split(',');

    //remove all values even duplicates
    ar=removeValues(ar, value);
    var result='"'+ar.join(',')+'"';

    setCookie(expandedCookieName, result, 0, '/');
    return true;
}

/*
 * Returns true if the array contains the value, false otherwise
 */
function arrayContains(ar, value) {
    var i=0;
    for(var i=0;i<ar.length;i++) {
        if(ar[i]==value) {
            return true;
        }
    }
    return false;
}

/*
 * Remove values from the array
 */
function removeValues(ar, value) {
    var i=0;
    for(var i=0;i<ar.length;i++) {
        if (ar[i]==value) {
            ar.splice(i, 1);
        }
    }
    return ar;
}