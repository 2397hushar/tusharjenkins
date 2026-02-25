package utilities;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseStarted;

public class AllureFilter implements EventListener {
    
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }
    
    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        
        // Filter out TestNG lifecycle methods
        if (testCase.getName().contains("setUpClass") || 
            testCase.getName().contains("tearDownClass") ||
            testCase.getName().contains("setUpMethod") ||
            testCase.getName().contains("tearDownMethod")) {
            
            System.out.println("⏭️ Skipping TestNG lifecycle method: " + testCase.getName());
            // You could also skip these in Allure by not processing them
        }
    }
}