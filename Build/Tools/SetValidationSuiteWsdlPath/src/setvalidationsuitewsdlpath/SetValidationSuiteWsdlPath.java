package setvalidationsuitewsdlpath;

/**
 * Runs the validation suite fixer with a possible command line argument for setting a specific
 * path to the WSDLs.  (This option will be listed in a README in the Validation Suite).
 * @author Jason Smith
 */
public class SetValidationSuiteWsdlPath {

    public static void main(String[] arg){
            ValidationSuiteWsdlRunner vswRunner;
            if(arg.length>0){
                vswRunner = new ValidationSuiteWsdlRunner(arg[0]);
            }else vswRunner = new ValidationSuiteWsdlRunner();
            vswRunner.setWsdls();  
    }
}
