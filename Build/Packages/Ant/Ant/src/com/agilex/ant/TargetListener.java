package com.agilex.ant;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;

public class TargetListener implements BuildListener {

	@Override
	public void buildFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageLogged(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void targetFinished(BuildEvent event) {
		Target target = event.getTarget();
		if (event.getException() != null)
			this.forceProperty(event.getProject(), event.getProject().getName() + ".Target." + target.getName() + ".State", "Failed");
		else
			this.forceProperty(event.getProject(), event.getProject().getName() + ".Target." + target.getName() + ".State", "Success");
		this.forceProperty(event.getProject(), event.getProject().getName() + ".Target." + target.getName() + ".Executed", Boolean.toString(true));
	}

	@Override
	public void targetStarted(BuildEvent event) {
	    Target target = event.getTarget();
	    this.forceProperty(event.getProject(), "Target." + target.getName() + ".State", "Running");
	}

	@Override
	public void taskFinished(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskStarted(BuildEvent arg0) {
		// TODO Auto-generated method stub
		
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

	private void forceProperty(Project project, String name, String value) {
		try {
			Hashtable properties = (Hashtable) getValue(project, "properties");
			if ( properties == null ) {
				project.setUserProperty(name, this.parseProperty(project, value));
			}
			else {
				project.setProperty(name, this.parseProperty(project, value));
			}
		}
		catch ( Exception e ) {
			project.setUserProperty(name, this.parseProperty(project, value));
		}
	}

	@SuppressWarnings("deprecation")
	private String parseProperty(Project project, String value){
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
		         fragment = project.getProperty(propertyName);
		      }
		      sb.append( fragment );
		   }
		   return sb.toString();
		}
		return value;
	}
}
