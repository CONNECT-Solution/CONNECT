package com.agilex.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask;
import org.apache.tools.ant.types.Assertions;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

public class junitrunner extends JUnitTask {

	private CommandlineJava commandline;

	public junitrunner() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public class GoodCommandlineJava extends CommandlineJava {
		private Commandline vmCommand = new Commandline();
		private Commandline javaCommand = new Commandline();
		private SysProperties sysProperties = new SysProperties();
		private Path classpath = null;
		private Path bootclasspath = null;
		private String vmVersion;
		private String maxMemory = null;
		private Assertions assertions = null;
		private boolean executeJar = false;
		private boolean cloneVm = false;

		public GoodCommandlineJava() {
			super();
			this.setVm(JavaEnvUtils.getJreExecutable("java"));
			this.setVmversion(JavaEnvUtils.getJavaVersion());
		}

		@Override
		public Commandline.Argument createArgument() {
			return this.javaCommand.createArgument();
		}

		@Override
		public Commandline.Argument createVmArgument() {
			return this.vmCommand.createArgument();
		}

		@Override
		public void addSysproperty(Environment.Variable sysp) {
			this.sysProperties.addVariable(sysp);
		}

		@Override
		public void addSyspropertyset(PropertySet sysp) {
			this.sysProperties.addSyspropertyset(sysp);
		}

		@Override
		public void addSysproperties(SysProperties sysp) {
			this.sysProperties.addSysproperties(sysp);
		}

		@Override
		public void setVm(String vm) {
			if (this.vmCommand == null)
				this.vmCommand = new Commandline();
			this.vmCommand.setExecutable(vm);
		}

		@Override
		public void setVmversion(String value) {
			this.vmVersion = value;
		}

		@Override
		public void setCloneVm(boolean cloneVm) {
			this.cloneVm = cloneVm;
		}

		@Override
		public Assertions getAssertions() {
			return this.assertions;
		}

		@Override
		public void setAssertions(Assertions assertions) {
			this.assertions = assertions;
		}

		@Override
		public void setJar(String jarpathname) {
			this.javaCommand.setExecutable(jarpathname);
			this.executeJar = true;
		}

		@Override
		public String getJar() {
			if (this.executeJar)
				return this.javaCommand.getExecutable();

			return null;
		}

		@Override
		public void setClassname(String classname) {
			this.javaCommand.setExecutable(classname);
			this.executeJar = false;
		}

		@Override
		public String getClassname() {
			if (!(this.executeJar))
				return this.javaCommand.getExecutable();

			return null;
		}

		@Override
		public Path createClasspath(Project p) {
			if (this.classpath == null)
				this.classpath = new Path(p);

			return this.classpath;
		}

		@Override
		public Path createBootclasspath(Project p) {
			if (this.bootclasspath == null)
				this.bootclasspath = new Path(p);

			return this.bootclasspath;
		}

		@Override
		public String getVmversion() {
			return this.vmVersion;
		}

		@Override
		public String[] getCommandline() {
			List commands = new LinkedList();
			ListIterator listIterator = commands.listIterator();

			addCommandsToList(listIterator);

			return ((String[]) (String[]) commands
					.toArray(new String[commands.size()]));
		}

		private void addCommandsToList(ListIterator listIterator) {
			getActualVMCommand().addCommandToList(listIterator);

			this.sysProperties.addDefinitionsToList(listIterator);

			if (isCloneVm()) {
				SysProperties clonedSysProperties = new SysProperties();
				PropertySet ps = new PropertySet();
				PropertySet.BuiltinPropertySetName sys = new PropertySet.BuiltinPropertySetName();

				sys.setValue("system");
				ps.appendBuiltin(sys);
				clonedSysProperties.addSyspropertyset(ps);
				clonedSysProperties.addDefinitionsToList(listIterator);
			}

			Path bcp = calculateBootclasspath(true);
			if (bcp.size() > 0) {
				listIterator.add("-Xbootclasspath:" + bcp.toString());
			}

			if (haveClasspath()) {
				//listIterator.add("-classpath");
				//listIterator.add(this.classpath.concatSystemClasspath("ignore").toString());
			}

			if (getAssertions() != null) {
				getAssertions().applyAssertions(listIterator);
			}

			if (this.executeJar) {
				listIterator.add("-jar");
			}

			this.javaCommand.addCommandToList(listIterator);
		}

		@Override
		public void setMaxmemory(String max) {
			this.maxMemory = max;
		}

		@Override
		public String toString() {
			return Commandline.toString(getCommandline());
		}

		@Override
		public String describeCommand() {
			return Commandline.describeCommand(getCommandline());
		}

		@Override
		public String describeJavaCommand() {
			return Commandline.describeCommand(getJavaCommand());
		}

		@Override
		protected Commandline getActualVMCommand() {
			Commandline actualVMCommand = (Commandline) this.vmCommand
					.clone();
			if (this.maxMemory != null)
				if (this.vmVersion.startsWith("1.1"))
					actualVMCommand.createArgument().setValue(
							"-mx" + this.maxMemory);
				else
					actualVMCommand.createArgument().setValue(
							"-Xmx" + this.maxMemory);

			return actualVMCommand;
		}

		/**
		 * @deprecated
		 */
		@Override
		public int size() {
			int size = getActualVMCommand().size()
					+ this.javaCommand.size()
					+ this.sysProperties.size();

			if (isCloneVm()) {
				size += System.getProperties().size();
			}

			if (haveClasspath()) {
				size += 2;
			}

			if (calculateBootclasspath(true).size() > 0) {
				++size;
			}

			if (this.executeJar) {
				++size;
			}

			if (getAssertions() != null)
				size += getAssertions().size();

			return size;
		}

		@Override
		public Commandline getJavaCommand() {
			return this.javaCommand;
		}

		@Override
		public Commandline getVmCommand() {
			return getActualVMCommand();
		}

		@Override
		public Path getClasspath() {
			return this.classpath;
		}

		@Override
		public Path getBootclasspath() {
			return this.bootclasspath;
		}

		@Override
		public void setSystemProperties() throws BuildException {
			this.sysProperties.setSystem();
		}

		@Override
		public void restoreSystemProperties() throws BuildException {
			this.sysProperties.restoreSystem();
		}

		@Override
		public SysProperties getSystemProperties() {
			return this.sysProperties;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			try {
				GoodCommandlineJava c = (GoodCommandlineJava) super.clone();
				c.vmCommand = ((Commandline) this.vmCommand.clone());
				c.javaCommand = ((Commandline) this.javaCommand.clone());
				c.sysProperties = ((SysProperties) this.sysProperties
						.clone());
				if (this.classpath != null)
					c.classpath = ((Path) this.classpath.clone());

				if (this.bootclasspath != null)
					c.bootclasspath = ((Path) this.bootclasspath
							.clone());

				if (this.assertions != null)
					c.assertions = ((Assertions) this.assertions
							.clone());

				return c;
			} catch (CloneNotSupportedException e) {
				throw new BuildException(e);
			}
		}

		@Override
		public void clearJavaArgs() {
			this.javaCommand.clearArgs();
		}

		@Override
		public boolean haveClasspath() {
			Path fullClasspath = (this.classpath != null) ? this.classpath
					.concatSystemClasspath("ignore")
					: null;

			return ((fullClasspath != null) && (fullClasspath
					.toString().trim().length() > 0));
		}

		@Override
		protected boolean haveBootclasspath(boolean log) {
			return (calculateBootclasspath(log).size() > 0);
		}

		private Path calculateBootclasspath(boolean log) {
			if (this.vmVersion.startsWith("1.1")) {
				if ((this.bootclasspath != null) && (log))
					this.bootclasspath
							.log("Ignoring bootclasspath as the target VM doesn't support it.");
			} else {
				if (this.bootclasspath != null) {
					return this.bootclasspath
							.concatSystemBootClasspath((isCloneVm()) ? "last"
									: "ignore");
				}

				if (isCloneVm())
					return Path.systemBootClasspath;
			}

			return new Path(null);
		}

		private boolean isCloneVm() {
			return ((this.cloneVm) || ("true".equals(System
					.getProperty("ant.build.clonevm"))));
		}
	}

	@Override
	protected CommandlineJava getCommandline() {

		if (this.commandline == null)
			this.commandline = new GoodCommandlineJava();

		return this.commandline;
	}

	@Override
	public void execute() throws BuildException {
		// CommandlineJava cmd = this.getCommandline();
		// overwritePrivateField(cmd, "classpath", null, false);
		super.execute();
	}

	private static void overwritePrivateField(final Object object,
			String fieldName, Object newFieldValue, boolean useSuperClass) {

		String message = "Could not overwrite private field!";

		Field field = null;
		try {
			Class<? extends Object> className = object.getClass();

			if (useSuperClass) {

				className = className.getSuperclass();
			}

			field = className.getDeclaredField(fieldName);

		} catch (SecurityException e) {
			throw new RuntimeException(message, e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(message, e);
		}

		field.setAccessible(true);

		try {
			field.set(object, newFieldValue);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(message, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(message, e);
		}
	}
}
