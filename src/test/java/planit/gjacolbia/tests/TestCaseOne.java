package planit.gjacolbia.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.components.ContactForm;
import planit.gjacolbia.tests.pages.ContactPage;
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
        ContactPage contactPage = homePage.clickContact();
        // 2. Click submit button
        contactPage.withContactForm().clickSubmit();
        // 3. Validate errors
        // Header error
        Assert.assertTrue(contactPage.isHeaderMessageAnError(), "Contact form Header Error is present.");
        Assert.assertEquals(contactPage.getHeaderMessageText(), "We welcome your feedback - but we won't get it unless you complete the form correctly.", "Header Error message is correct.");
        // Forename error
        Assert.assertTrue(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.FORENAME), "Forename field is marked invalid.");
        Assert.assertEquals(contactPage.withContactForm().getFieldErrorMessage(ContactForm.Field.FORENAME), "Forename is required", "Forename field error message is displayed and correct");
        // Email error
        Assert.assertTrue(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.EMAIL), "Email field is marked invalid.");
        Assert.assertEquals(contactPage.withContactForm().getFieldErrorMessage(ContactForm.Field.EMAIL), "Email is required", "Email field error message is displayed and correct");
        // Message error
        Assert.assertTrue(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.MESSAGE), "Message field is marked invalid.");
        Assert.assertEquals(contactPage.withContactForm().getFieldErrorMessage(ContactForm.Field.MESSAGE), "Message is required", "Message field error message is displayed and correct");
        // 4. Populate Mandatory Fields
        contactPage.withContactForm().fill(ContactForm.Field.FORENAME, "Gerard");
        contactPage.withContactForm().fill(ContactForm.Field.EMAIL, "gerard@example.com");
        contactPage.withContactForm().fill(ContactForm.Field.MESSAGE, "The quick brown fox jumped over the lazy dog.");
        // 5. Validate errors are gone
        // Header
        Assert.assertFalse(contactPage.isHeaderMessageAnError(), "Header Message is no longer an error message.");
        Assert.assertEquals(contactPage.getHeaderMessageText(), "We welcome your feedback - tell it how it is.", "Header message is no longer an error message.");
        // Forename
        Assert.assertFalse(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.FORENAME), "Forename field is no longer marked as invalid.");
        Assert.assertFalse(ElementHelper.isElementPresent(contactPage.withContactForm().getFormFieldWebElement(ContactForm.Field.FORENAME, ContactForm.FieldType.ERROR_MESSAGE)), "Forename field error message is no longer displayed.");
        // Email
        Assert.assertFalse(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.EMAIL), "Email field is no longer marked as invalid.");
        Assert.assertFalse(ElementHelper.isElementPresent(contactPage.withContactForm().getFormFieldWebElement(ContactForm.Field.EMAIL, ContactForm.FieldType.ERROR_MESSAGE)), "Email field error message is no longer displayed.");
        // Message
        Assert.assertFalse(contactPage.withContactForm().isFieldInvalid(ContactForm.Field.MESSAGE), "Message control group is no longer marked as invalid.");
        Assert.assertFalse(ElementHelper.isElementPresent(contactPage.withContactForm().getFormFieldWebElement(ContactForm.Field.MESSAGE, ContactForm.FieldType.ERROR_MESSAGE)), "Message field error message is no longer displayed.");

    }

}
