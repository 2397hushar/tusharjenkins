package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Opennewpage {
    
    // Complete user data list
    private static final List<String> USER_EMAILS = Arrays.asList(
        "archana.patil@ancstff1.com",
        "afzal.hussian@ancstff1.com",
        "ajeet.kumar@ancstff1.com",
        "ajith.kumar@ancstff1.com",
        "Alam.Shaikh@ancstff1.com",
        "Amit.Jagtap@ancstff1.com",
        "amit.kamat@ancstff1.com",
        "Amit.Pal@ancstff1.com",
        "amruta.raut@ancstff1.com",
        "anand.valge@ancstff1.com",
        "anil.das@ancstff1.com",
        "anil.yadhav@ancstff1.com",
        "anoopkunwar.k@ancstff1.com",
        "archana.patil@ancstff1.com",
        "arjunsingh.patel@ancstff1.com",
        "ashok.1772@ancstff1.com",
        "ashok.rai@ancstff1.com",
        "ashwini.barkade@ancstff1.com",
        "awnish.chaturvedi@ancstff1.com",
        "babu.bey@ancstff1.com",
        "babul.tamuli@ancstff1.com",
        "bagish.tiwari@ancstff1.com",
        "Balaram.Sahoo@ancstff1.com",
        "Bardan.Khalkho@ancstff1.com",
        "basant.das@ancstff1.com",
        "Basant.Mahara@ancstff1.com",
        "beby.kumari@ancstff1.com",
        "bharat.khare@ancstff1.com",
        "bhimrao.sawale@ancstff1.com",
        "bhivaji.sonkamble@ancstff1.com",
        "bhuri.3483@ancstff1.com",
        "bilaluddin.b@ancstff1.com",
        "binod.nagari@ancstff1.com",
        "bishal.tayi@ancstff1.com",
        "biswajit.das1@ancstff1.com",
        "Biswjit.Biswas@ancstff1.com",
        "chandrakalas.1710@ancstff1.com",
        "chandrakant.patil@ancstff1.com",
        "dadaso.maskar@ancstff1.com",
        "dashrath.1715@ancstff1.com",
        "daxa.makwana@ancstff1.com",
        "debashish.hazam@ancstff1.com",
        "deepak.mahadik@ancstff1.com",
        "deepak.tiwari@ancstff1.com",
        "Devki.Nandan@ancstff1.com",
        "Dhanjit.Das@ancstff1.com",
        "dhruba.giri@ancstff1.com",
        "dinanath.gupta@ancstff1.com",
        "Dipendra.Rimal@ancstff1.com",
        "dipu.chetry@ancstff1.com",
        "diwakar.thorat@ancstff1.com",
        "dudh.sahani@ancstff1.com",
        "fagu.marndi@ancstff1.com",
        "ganesh.kamble1@ancstff1.com",
        "gangadhar.swami@ancstff1.com",
        "ganseh.sonawane@ancstff1.com",
        "gobinda.das@ancstff1.com",
        "gorakhnath.sable@ancstff1.com",
        "govind.gaikwad@ancstff1.com",
        "hadeeya.tudu@ancstff1.com",
        "haribhau.kalbande@ancstff1.com",
        "hariom.upadhyay@ancstff1.com",
        "hariram.patil@ancstff1.com",
        "jayalakshmi.1778@ancstff1.com",
        "jayshri.jagtap@ancstff1.com",
        "jit.chetry@ancstff1.com",
        "jiten.giri@ancstff1.com",
        "k.sharma@ancstff1.com",
        "kaillas.dhawale@ancstff1.com",
        "kalimula.khan@ancstff1.com",
        "kamal.bhoj@ancstff1.com",
        "kamal.braman@ancstff1.com",
        "kamlakar.kumbhar@ancstff1.com",
        "Kawsar.Barbhuiya@ancstff1.com",
        "keerthika.1770@ancstff1.com",
        "keshav.amravati@ancstff1.com",
        "Keshor.Mansingh@ancstff1.com",
        "kiran.ghanekar@ancstff1.com",
        "kishor.manwar@ancstff1.com",
        "komal.singh@ancstff1.com",
        "krishnakant.b@ancstff1.com",
        "kumar.powar@ancstff1.com",
        "lakhan.biswas@ancstff1.com",
        "laxmi.budhe@ancstff1.com",
        "m.nagarjau@ancstff1.com",
        "mahadevi.gaikwad@ancstff1.com",
        "mahesh.nandanwar@ancstff1.com",
        "mahesh.yadhav@ancstff1.com",
        "mahiboob.shaikh@ancstff1.com",
        "mandakini.3425@ancstff1.com",
        "mangesh.pawar@ancstff1.com",
        "manish.singh@ancstff1.com",
        "manoj.gupta@ancstff1.com",
        "manoj.kumar@ancstff1.com",
        "md.hussain@ancstff1.com",
        "meenal.bobade@ancstff1.com",
        "milind.waghmare@ancstff1.com",
        "mintu.bhuyan@ancstff1.com",
        "Mohanlal.Bin@ancstff1.com",
        "mridul.doley@ancstff1.com",
        "mridul.ray@ancstff1.com",
        "mubarak.momin@ancstff1.com",
        "mukesh.ahire@ancstff1.com",
        "muktadir.bharbuiya@ancstff1.com",
        "nagesh.gn@ancstff1.com",
        "nandani.thombre@ancstff1.com",
        "narendra.kini@ancstff1.com",
        "Naresh.Shinde@ancstff1.com",
        "nasir.ansari@ancstff1.com",
        "navneet.ghote@ancstff1.com",
        "noel.christy@ancstff1.com",
        "pankaj.guwala@ancstff1.com",
        "pankaj.singh@ancstff1.com",
        "pankaj.singh1@ancstff1.com",
        "papanna.m@ancstff1.com",
        "pooja.donde@ancstff1.com",
        "pooja.puri@ancstff1.com",
        "prabhath.kumar@ancstff1.com",
        "pradeep.sawant@ancstff1.com",
        "pradhan.tudu@ancstff1.com",
        "prakash.lakra@ancstff1.com",
        "pramod.chavan@ancstff1.com",
        "prashant.pol@ancstff1.com",
        "pratap.kumar@ancstff1.com",
        "pravin.toppo@ancstff1.com",
        "premnath.1640@ancstff1.com",
        "radhamma.1643@ancstff1.com",
        "Rahul.4187@ancstff1.com",
        "raj.kumar1@ancstff1.com",
        "raja.k@ancstff1.com",
        "rajat.mishra@ancstff1.com",
        "rajesh.kumar1@ancstff1.com",
        "rajesh.singh@ancstff1.com",
        "rajesh.ujagare@ancstff1.com",
        "raju.c@ancstff1.com",
        "ram.prakash@ancstff1.com",
        "rambabu.khandelwal@ancstff1.com",
        "Ramprit.4132@ancstff1.com",
        "raosaheb.sadamast@ancstff1.com",
        "rasel.debbarma@ancstff1.com",
        "ratan.das@ancstff1.com",
        "ravi.mudliyar@ancstff1.com",
        "ray.tudu@ancstff1.com",
        "rekha.jadhav@ancstff1.com",
        "renuka.1637@ancstff1.com",
        "rutvik.thorat@ancstff1.com",
        "sachin.bhingare@ancstff1.com",
        "sachindra.munda@ancstff1.com",
        "saddam.shaikh1@ancstff1.com",
        "sakshi.dhavade@ancstff1.com",
        "samadhan.kashid@vgos.org",
        "sampada.pipmlodkar@ancstff1.com",
        "sandeep.mishra@ancstff1.com",
        "sandesh.kumbhar@ancstff1.com",
        "Sandip.Chavan@ancstff1.com",
        "sandip.paul@ancstff1.com",
        "Sangam.4220@ancstff1.com",
        "sanjay.angare@ancstff1.com",
        "sanjay.gaikwad@ancstff1.com",
        "sanjay.hiragile@ancstff1.com",
        "sanjay.khalse@ancstff1.com",
        "sanjay.mishra@ancstff1.com",
        "sanjay.shibude@ancstff1.com",
        "santosh.salunkhe@ancstff1.com",
        "santosh.tiwari@ancstff1.com",
        "sayabana.bhange@ancstff1.com",
        "seema.behera@ancstff1.com",
        "shahaji.bankar@ancstff1.com",
        "shankar.sasane@ancstff1.com",
        "shankar.vitekari@ancstff1.com",
        "shanmugam.1766@ancstff1.com",
        "sharad.wagh@ancstff1.com",
        "sheedevi.1615@ancstff1.com",
        "Shibanth.Mondal@ancstff1.com",
        "shiv.kumar@ancstff1.com",
        "shivam.bind@ancstff1.com",
        "shruthi.2719@ancstff1.com",
        "shubham.chavan@ancstff1.com",
        "siddharam.b@ancstff1.com",
        "sitaram.ahirwar@ancstff1.com",
        "sochindra.mohakud@ancstff1.com",
        "sohel.ahmed@ancstff1.com",
        "sonali.lashkar@ancstff1.com",
        "sudhir.singh@ancstff1.com",
        "Sujeet.Mishra@ancstff1.com",
        "sunil.toppo@ancstff1.com",
        "sunil.yarudaka@ancstff1.com",
        "suraj.bada@ancstff1.com",
        "Surendra.Mukhiya@ancstff1.com",
        "suresh.mule@ancstff1.com",
        "suresh.veer@ancstff1.com",
        "swamy.n@ancstff1.com",
        "tanaji.yamgar@ancstff1.com",
        "tukaram.sonawane@ancstff1.com",
        "umapathi.1602@ancstff1.com",
        "umesh.yuvne@ancstff1.com",
        "uthup.mathew@ancstff1.com",
        "uttam.hire@ancstff1.com",
        "uttam.thorat@ancstff1.com",
        "v.laxmidevi@ancstff1.com",
        "venkataramanappa.1@ancstff1.com",
        "vetti.kumar@ancstff1.com",
        "vijay.wakade@ancstff1.com",
        "Vijaya.Kumar@ancstff1.com",
        "vijaya.m@ancstff1.com",
        "vikrama.prasad@ancstff1.com",
        "vilas.sathe@ancstff1.com",
        "vinay.dhanawade@ancstff1.com",
        "vinayak.kadam@ancstff1.com",
        "vishal.gaikwad@ancstff1.com",
        "xavier.s@ancstff1.com",
        "yagnikkumar.patel@ancstff1.com",
        "yashdha.1773@ancstff1.com",
        "yesodha.1745@ancstff1.com"
    );
    
    // List of passwords to try (in order)
    private static final List<String> PASSWORDS = Arrays.asList(
        "VibG@123",      // First password
        "Vib@321$"      // Second password
    );
    
    private static final String LOGIN_URL = "https://preprod-hubbleorion-erp.hubblehox.com/";
    
    public static void main(String[] args) {
        Opennewpage test = new Opennewpage();
        test.runLoginTests();
    }
    
    public void runLoginTests() {
        int successCount = 0;
        int failureCount = 0;
        int totalUsers = USER_EMAILS.size();
        
        System.out.println("=".repeat(90));
        System.out.println("LOGIN TEST AUTOMATION RESULTS");
        System.out.println("=".repeat(90));
        System.out.printf("%-45s | %-12s | %s%n", "Email", "Status", "Message");
        System.out.println("-".repeat(90));
        
        // Test each user one by one - opening new browser for each user
        for (String email : USER_EMAILS) {
            boolean result = testLoginWithNewBrowser(email);
            if (result) {
                successCount++;
            } else {
                failureCount++;
            }
            
            // Small delay between users
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("=".repeat(90));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(90));
        System.out.println("Total Users: " + totalUsers);
        System.out.println("Successful Logins: " + successCount);
        System.out.println("Failed Logins: " + failureCount);
        System.out.println("Success Rate: " + String.format("%.2f", (successCount * 100.0 / totalUsers)) + "%");
        System.out.println("=".repeat(90));
    }
    
    private boolean testLoginWithNewBrowser(String email) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        
        try {
            // Setup ChromeDriver for new browser instance
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\tushar.sangale\\tusharjenkins\\chromedriver\\chromedriver.exe");
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-popup-blocking");
            
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Try each password until one works
            for (int i = 0; i < PASSWORDS.size(); i++) {
                String password = PASSWORDS.get(i);
                String passwordLabel = "Password " + (i + 1);
                
                try {
                    // Navigate to login page
                    driver.get(LOGIN_URL);
                    
                    // Wait for page to load completely
                    Thread.sleep(3000);
                    
                    // Wait for username field
                    WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
                    emailField.clear();
                    emailField.sendKeys(email);
                    
                    // Find password field
                    WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
                    passwordField.clear();
                    passwordField.sendKeys(password);
                    
                    // Find and click login button
                    WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("kc-login")));
                    loginButton.click();
                    
                    // Wait for login response
                    Thread.sleep(5000);
                    
                    // Check if login was successful
                    boolean loginSuccess = isLoginSuccessful(driver);
                    
                    if (loginSuccess) {
                        System.out.printf("%-45s | %-12s | %s%n", email, "✓ SUCCESS", "Login successful with " + passwordLabel);
                        return true;
                    } else {
                        // If this was the last password attempt, show failure
                        if (i == PASSWORDS.size() - 1) {
                            String errorMsg = getErrorMessage(driver);
                            System.out.printf("%-45s | %-12s | %s%n", email, "✗ FAILED", errorMsg);
                        }
                        // Otherwise, continue to next password
                    }
                    
                } catch (Exception e) {
                    if (i == PASSWORDS.size() - 1) {
                        System.out.printf("%-45s | %-12s | %s%n", email, "✗ ERROR", e.getMessage().substring(0, Math.min(e.getMessage().length(), 40)));
                    }
                }
            }
            return false;
            
        } catch (Exception e) {
            System.out.printf("%-45s | %-12s | %s%n", email, "✗ ERROR", "Browser launch failed");
            return false;
        } finally {
            // Close the browser after each user attempt
            if (driver != null) {
                driver.quit();
                System.out.println("   Browser closed for: " + email);
            }
        }
    }
    
    private boolean isLoginSuccessful(WebDriver driver) {
        try {
            // Check for error message first
            List<By> errorIndicators = Arrays.asList(
                By.className("kc-feedback-text"),
                By.cssSelector(".alert-error"),
                By.xpath("//span[contains(@class, 'kc-feedback-text')]"),
                By.xpath("//div[contains(@class, 'alert')]")
            );
            
            for (By indicator : errorIndicators) {
                List<WebElement> errorElements = driver.findElements(indicator);
                if (!errorElements.isEmpty() && errorElements.get(0).isDisplayed()) {
                    return false;
                }
            }
            
            // Check for successful login indicators (based on your dashboard screenshot)
            List<By> successIndicators = Arrays.asList(
                By.xpath("//*[contains(text(), 'Dashboard')]"),
                By.xpath("//*[contains(text(), 'Welcome')]"),
                By.xpath("//*[contains(text(), 'Academic Year')]"),
                By.xpath("//*[contains(text(), 'Powered by HubbleHox')]"),
                By.cssSelector(".navbar"),
                By.cssSelector(".sidebar"),
                By.xpath("//*[contains(@class, 'user-info')]")
            );
            
            for (By indicator : successIndicators) {
                List<WebElement> elements = driver.findElements(indicator);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return true;
                }
            }
            
            // Check if URL changed from login page
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("auth") && !currentUrl.equals(LOGIN_URL) && !currentUrl.contains("login")) {
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private String getErrorMessage(WebDriver driver) {
        try {
            List<By> errorSelectors = Arrays.asList(
                By.className("kc-feedback-text"),
                By.cssSelector(".alert-error"),
                By.xpath("//span[contains(@class, 'kc-feedback-text')]"),
                By.xpath("//div[contains(@class, 'alert-danger')]"),
                By.xpath("//div[contains(text(), 'Invalid')]"),
                By.xpath("//div[contains(text(), 'Incorrect')]")
            );
            
            for (By selector : errorSelectors) {
                List<WebElement> errorElements = driver.findElements(selector);
                if (!errorElements.isEmpty() && errorElements.get(0).isDisplayed()) {
                    String errorText = errorElements.get(0).getText();
                    if (errorText != null && !errorText.isEmpty()) {
                        return errorText.length() > 35 ? errorText.substring(0, 35) + "..." : errorText;
                    }
                }
            }
            
            return "Invalid credentials";
            
        } catch (Exception e) {
            return "Error message not available";
        }
    }
}