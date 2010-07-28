package nhinc

class GlassFishUtils {
  GlassFishUtils() {}
  
  static stopGlassFishDomain(context, log){
    try{
      def os = System.getProperty("os.name");
      def GF_HOME = System.env['AS_HOME'];
      if (os ==~ /(?i)windows.*/){
        def proc = "${GF_HOME}/bin/asadmin.bat stop-domain domain1".execute();
        log.info( "Found text ${proc.text}");
      }else {
        def proc = "${GF_HOME}/bin/asadmin stop-domain domain1".execute();
        log.info( "Found text ${proc.text}");
      }
    }catch(Throwable e){
      e.printStackTrace(); 
    }
  }
  
  static startGlassFishDomain(context, log){
    try{
      def os = System.getProperty("os.name");
      def GF_HOME = System.env['AS_HOME'];
      if (os ==~ /(?i)windows.*/){
        def proc = "${GF_HOME}/bin/asadmin.bat start-domain domain1".execute();
        log.info("Started GF successfully");
      }else {
        def proc = "${GF_HOME}/bin/asadmin start-domain domain1".execute();
		log.info("Started GF successfully");
      }
    }catch(Throwable e){
      e.printStackTrace(); 
    }
  }
}