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
import org.apache.tools.ant.taskdefs.condition.Condition;

public class GreaterThanLessThan implements Condition {

   private double less;
   private double greater;
   
   public double getLess() {
      return less;
   }
   public void setLess(double less) {
      this.less = less;
   }
   
   public double getGreater() {
      return greater;
   }
   public void setGreater(double greater) {
      this.greater = greater;
   }
   
   public boolean eval() throws BuildException {
      return (getLess()<getGreater());
   }
}
