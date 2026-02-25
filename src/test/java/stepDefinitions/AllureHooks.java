package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import utilities.BrowserUtils;

import java.io.ByteArrayInputStream;

public class AllureHooks {
    
    @Before(order = 0)
    public void setUpAllure(Scenario scenario) {
        System.out.println("📊 Setting up Allure for scenario: " + scenario.getName());
    }
    
    @After(order = 1)
    public void tearDownAllure(Scenario scenario) {
        // Add screenshot if failed
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = BrowserUtils.takeScreenshotForAllure("Failure Screenshot");
                if (screenshot != null && screenshot.length > 0) {
                    Allure.addAttachment("Failure Screenshot", "image/png", 
                        new ByteArrayInputStream(screenshot), "png");
                }
            } catch (Exception e) {
                System.out.println("❌ Failed to capture screenshot for Allure: " + e.getMessage());
            }
        }
    }
}