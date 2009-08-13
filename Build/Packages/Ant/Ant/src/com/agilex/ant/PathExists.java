package com.agilex.ant;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.util.FileUtils;

public class PathExists implements Condition {

   private String path;
   private File file;
   
   public String getPath() {
      return path;
   }
   public void setPath(String path) {
	  this.path = path;
   }
   
   public boolean eval() throws BuildException {
		return new File(path).exists();
   }
}
