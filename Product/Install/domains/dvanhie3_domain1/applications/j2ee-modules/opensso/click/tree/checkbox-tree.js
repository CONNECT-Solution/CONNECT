/*
 * This script provides select/deselect highlighting functionality to the Click 
 * CheckboxTree control. So when a checkbox is clicked the node's value 
 * must also be selected (highlighted) or deselected (not highlighted).
 * Clicking on a checkbox, the stylesheet class of the node's value must be 
 * dynamically swapped. So the class 'unselected' must be swapped with 
 * 'selected' and vice versa, so that the node value can be highlighted or not.
 *
 * Also if a node's value is clicked the checkbox must be selected or 
 * deselected.
 */

/* Defines the select/deselect defined in Tree.java */
var selectionArray = ["selected","unselected"];

/*
 * Replaces class1 with class2 on specified object. 
 * If the class1 is not a class of the object, class2 
 * is simply added
 */
function replaceOrAddClass(object, class1 ,class2) {
    if(classExists(object,class1)) {
         object.className = object.className.replace(class1,class2);
    } else if (!classExists(object,class2)) {
        addClass(object, class2);
    }
}

/*
 * Handles node selection events. This function
 * finds the specified selectId element, and checks if that
 * element's class value is "selected" or "unselected", before
 * swapping the value.
 */
function handleNodeSelection(objectArg, event, selectId, checkboxId) {
    stopPropagation(event);
    
    var span = document.getElementById(selectId);

    index = 0;
    if(classExists(span,selectionArray[1])) {
        index = 1;
    }
    removeClass(span, selectionArray[index]);
    addClass(span, selectionArray[1 - index]);

    handleCheckboxSelection(checkboxId, index);
}

/*
 * Handles checkbox selection events. This function
 * retrieves the specified checkbox with checkboxId
 * and check/unchecks based on the index value.
 */
function handleCheckboxSelection(checkboxId, index) {
    var checkbox = document.getElementById(checkboxId);    
    checkbox.checked = (index == 1) ? true : false;
     return;
}

/*
 * Called when user clicks on the checkbox. This function 
 * finds the <span> holding the selection state and swaps
 * the classes
 */
function checkboxClicked(objectArg, event, selectId) {
    stopPropagation(event);
    
    var span = document.getElementById(selectId);

    if(objectArg.checked) {
        replaceOrAddClass(span, selectionArray[1], selectionArray[0]);
    } else {
        replaceOrAddClass(span, selectionArray[0], selectionArray[1]);
    }
}