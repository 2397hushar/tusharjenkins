package runners;

import org.testng.annotations.Test;

public class JenkinsVerificationTest {
    
    @Test
    public void verifyJenkinsSetup() {
        System.out.println("=========================================");
        System.out.println("JENKINS BUILD VERIFICATION");
        System.out.println("=========================================");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("Jenkins Home: " + System.getenv("JENKINS_HOME"));
        System.out.println("Build Number: " + System.getenv("BUILD_NUMBER"));
        System.out.println("=========================================");
    }
}