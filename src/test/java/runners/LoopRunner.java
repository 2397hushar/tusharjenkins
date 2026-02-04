package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.FeatureWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(
    features = "src/test/resources/features/Reportcard.feature",
    glue = "stepDefinitions",
    plugin = {
        "pretty",
        "html:target/cucumber-reports-multiple.html",
        "json:target/cucumber-multiple.json"
    },
    monochrome = true
)
public class LoopRunner extends AbstractTestNGCucumberTests {

    private static final int TOTAL_EXECUTIONS = 4;

    @Test(dataProvider = "repeatedScenarios")
    public void runRepeatedScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        System.out.println("🎯 Executing scenario with next user...");
        super.runScenario(pickleWrapper, featureWrapper);
    }

    @DataProvider
    public Object[][] repeatedScenarios() {
        Object[][] originalScenarios = super.scenarios();
        Object[][] repeatedScenarios = new Object[TOTAL_EXECUTIONS][2];
        
        for (int i = 0; i < TOTAL_EXECUTIONS; i++) {
            repeatedScenarios[i] = originalScenarios[0]; // Repeat the first scenario
        }
        
        return repeatedScenarios;
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}