
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class KeyImport {

    private String src = null;
    private String dst = null;
    private String srcAlias = null;
    private String srcStorePass = "changeit";
    private String srcKeyPass = "changeit";
    private String dstStorePass = "changeit";
    private String dstAlias = null;
    private String trustedCertEntry = null;
    private String dstKeyPass = "changeit";

    /** Creates a new instance of KeyImport */
    public KeyImport(String[] args) {
    
        int i = 0;
        while (args.length > i) {
            int eqIndx = args[i].indexOf("=");
            if (eqIndx <=0) {
                throw new RuntimeException("Illegal Argument" + args[i]);
            }
            String str = args[i].substring(eqIndx + 1);
            if (args[i].startsWith("srcalias")) {
                srcAlias = str;
            } else if (args[i].startsWith("srcstorepass")) {
                srcStorePass = str;
            } else if (args[i].startsWith("srckeypass")) {
                srcKeyPass = str;
            } else if (args[i].startsWith("dststorepass")) {
                dstStorePass = str;
            } else if (args[i].startsWith("srcstore")) {
                src = str;
            } else if (args[i].startsWith("dststore")) {
                dst = str;
            } else if (args[i].startsWith("dstalias")) {
                dstAlias = str;
            } else if (args[i].startsWith("trustedentry")) {
                trustedCertEntry=str;
            } else if (args[i].startsWith("dstkeypass")) {
                dstKeyPass = str;
            }
            i++;
        }
    }
    
    public void copyKey() throws Exception {
        KeyStore srcStore = KeyStore.getInstance("JKS");
        KeyStore dstStore = KeyStore.getInstance("JKS");
        srcStore.load(new FileInputStream(src), srcStorePass.toCharArray());
        dstStore.load(new FileInputStream(dst), dstStorePass.toCharArray());
        Key privKey = srcStore.getKey(srcAlias, srcKeyPass.toCharArray());
     
        if (privKey == null || "true".equals(this.trustedCertEntry)) {
            //this is a cert-entry
            dstStore.setCertificateEntry(dstAlias, srcStore.getCertificate(srcAlias));
            System.out.println("Added Trusted Entry  :" + dstAlias);
        } else {
              
	    Certificate cert = srcStore.getCertificate(srcAlias);
            Certificate[] chain = new Certificate[] {cert};
            dstStore.setKeyEntry(dstAlias, privKey,dstKeyPass.toCharArray(), chain);
            System.out.println("Added Key Entry  :" + dstAlias);
        }
	dstStore.store(new FileOutputStream(dst), dstStorePass.toCharArray());
       
    }
    public static void main(String[] args) throws Exception {
        KeyImport imp = new KeyImport(args);
        imp.copyKey();
    }
}

