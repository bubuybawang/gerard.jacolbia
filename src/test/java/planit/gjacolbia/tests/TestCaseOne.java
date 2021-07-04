package planit.gjacolbia.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.tests.pages.HomePage;

/**
 * Test case 1:
 * 1. From the home page go to contact page
 * 2. Click submit button
 * 3. Validate errors
 * 4. Populate mandatory fields
 * 5. Validate errors are gone
 */
public class TestCaseOne {
    WebDriver driver;

    @BeforeTest
    public void setUp() {
        // TODO: Have a driver manager class
        switch (Configuration.get("browser").toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "opera":
                WebDriverManager.operadriver().setup();
                driver = new OperaDriver();
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void runTest() {
        // 1. From the home page go to contact page
        HomePage homePage = new HomePage(driver);
        homePage.navigate();
        homePage.clickContact();
        // TODO Change to pageobject, loadablecomponent?
        // 2. Click submit button
        WebElement submitButton = driver.findElement(By.xpath("//a[contains(@class, 'btn-contact') and contains(@ui-event,'onSubmit') and text()='Submit']"));
        submitButton.click();
        // 3. Validate errors
        // Header error
        WebElement headerMessage = driver.findElement(By.xpath("//div[@id='header-message']/div[contains(@class, 'alert ')]"));
        Assert.assertTrue(headerMessage.getAttribute("class").contains("alert-error"), "Contact form Header Error is present.");
        Assert.assertEquals(headerMessage.getText(), "We welcome your feedback - but we won't get it unless you complete the form correctly.", "Header Error message is correct.");
        // Forename error
        // TODO: Good candidate for component
        WebElement forenameField = driver.findElement(By.id("forename"));
        WebElement forenameControlGroup = driver.findElement(By.xpath("//input[@id='forename']/ancestor::div[contains(@class, 'control-group')]"));
        WebElement forenameErrorMessage = driver.findElement(By.id("forename-err"));
        Assert.assertTrue(forenameControlGroup.getAttribute("class").contains("error"), "Forename control-group is marked invalid");
        Assert.assertTrue(forenameField.getAttribute("class").contains("ng-invalid"), "Forename field is marked invalid.");
        Assert.assertEquals(forenameErrorMessage.getText(), "Forename is required", "Forename field error message is displayed and correct");
        // Email error
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement emailControlGroup = driver.findElement(By.xpath("//input[@id='email']/ancestor::div[contains(@class, 'control-group')]"));
        WebElement emailErrorMessage = driver.findElement(By.id("email-err"));
        Assert.assertTrue(emailControlGroup.getAttribute("class").contains("error"), "Email control-group is marked invalid");
        Assert.assertTrue(emailField.getAttribute("class").contains("ng-invalid"), "Email field is marked invalid.");
        Assert.assertEquals(emailErrorMessage.getText(), "Email is required", "Email field error message is displayed and correct");
        // Message error
        WebElement messageField = driver.findElement(By.id("message"));
        WebElement messageControlGroup = driver.findElement(By.xpath("//textarea[@id='message']/ancestor::div[contains(@class, 'control-group')]"));
        WebElement messageErrorMessage = driver.findElement(By.id("message-err"));
        Assert.assertTrue(messageControlGroup.getAttribute("class").contains("error"), "Message control-group is marked invalid");
        Assert.assertTrue(messageField.getAttribute("class").contains("ng-invalid"), "Message field is marked invalid.");
        Assert.assertEquals(messageErrorMessage.getText(), "Message is required", "Message field error message is displayed and correct");
        // 4. Populate Mandatory Fields
        // TODO Repeating pattern...
        forenameField.click();
        forenameField.clear();
        forenameField.sendKeys("Gerard");

        emailField.click();
        emailField.clear();
        emailField.sendKeys("gerard@example.com");

        messageField.click();
        messageField.clear();
        messageField.sendKeys("The quick brown fox jumped over the lazy dog.");
        // 5. Validate errors are gone
        // Header
        headerMessage = driver.findElement(By.xpath("//div[@id='header-message']/div[contains(@class, 'alert ')]"));
        Assert.assertTrue(headerMessage.getAttribute("class").contains("alert-info"), "Header Message is no longer an error message.");
        Assert.assertEquals(headerMessage.getText(), "We welcome your feedback - tell it how it is.", "Header message is no longer an error message.");
        // Forename
        Assert.assertFalse(forenameControlGroup.getAttribute("class").contains("error"), "Forename control group is no longer marked as invalid.");
        Assert.assertTrue(driver.findElements(By.id("forename-err")).isEmpty(), "Forename field error message is no longer displayed.");
        // Email
        Assert.assertFalse(emailControlGroup.getAttribute("class").contains("error"), "Email control group is no longer marked as invalid.");
        Assert.assertTrue(driver.findElements(By.id("email-err")).isEmpty(), "Email field error message is no longer displayed.");
        // Message
        Assert.assertFalse(messageControlGroup.getAttribute("class").contains("error"), "Message control group is no longer marked as invalid.");
        Assert.assertTrue(driver.findElements(By.id("message-err")).isEmpty(), "Message field error message is no longer displayed.");

    }

}
