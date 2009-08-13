package com.agilex.ant;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;


public class LargeProperty extends Task {
    
	String name = "";
	
	public void setName(String name) {
		this.name = name;
	}
	
	String value = "";
	public void addText(String text) {
        this.value = text;
    }
	
	public void execute() {
		if (this.name==null) {
            throw new BuildException("No name set.");
        }
		if (this.value==null) {
            throw new BuildException("No value set.");
        }

		this.forceProperty(this.name, this.value);
	}
	
	private void forceProperty(String name, String value) {
		try {
			Hashtable properties = (Hashtable) getValue(getProject(), "properties");
			if ( properties == null ) {
				getProject().setUserProperty(name, this.parseProperty(value));
			}
			else {
				getProject().setProperty(name, this.parseProperty(value));
			}
		}
		catch ( Exception e ) {
			getProject().setUserProperty(name, this.parseProperty(value));
		}
	}
	
	private Object getValue( Object instance, String fieldName ) throws IllegalAccessException, NoSuchFieldException {
	   Field field = getField( instance.getClass(), fieldName );
	   field.setAccessible( true );
	   return field.get( instance );
	}
	
	private Field getField( Class thisClass, String fieldName ) throws NoSuchFieldException {
	   if ( thisClass == null ) {
	      throw new NoSuchFieldException( "Invalid field : " + fieldName );
	   }
	   try {
	      return thisClass.getDeclaredField( fieldName );
	   }
	   catch ( NoSuchFieldException e ) {
	      return getField( thisClass.getSuperclass(), fieldName );
	   }
}


	private String parseProperty(String value){
		Vector fragments = new Vector();
		Vector propertyRefs = new Vector();
		ProjectHelper.parsePropertyString(value, fragments, propertyRefs);

		if (propertyRefs.size() != 0) {
		   StringBuffer sb = new StringBuffer();
		   Enumeration i = fragments.elements();
		   Enumeration j = propertyRefs.elements();
		   while (i.hasMoreElements()) {
		      String fragment = (String)i.nextElement();
		      if (fragment == null) {
		         String propertyName = (String)j.nextElement();
		         fragment = getProject().getProperty(propertyName);
		      }
		      sb.append( fragment );
		   }
		   return sb.toString();
		}
		return value;
	}
    
}
