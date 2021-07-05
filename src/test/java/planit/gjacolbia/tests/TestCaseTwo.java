package planit.gjacolbia.tests;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.tests.components.ContactForm;
import planit.gjacolbia.tests.pages.ContactFormSubmittedPage;
import planit.gjacolbia.tests.pages.ContactPage;
import planit.gjacolbia.tests.pages.HomePage;

/**
 * Test case 2:
 * 1. From the home page go to contact page
 * 2. Populate mandatory fields
 * 3. Click submit button
 * 4. Validate successful submission message
 * Note: Run this test 5 times to ensure 100% pass rate
 */
public class TestCaseTwo {
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

    @DataProvider(name = "testdata")
    public Object[][] testData() {
        Faker faker = new Faker();
        return new Object[][] {
                {faker.name().firstName(), faker.internet().safeEmailAddress(), faker.lorem().sentence()},
                {faker.name().firstName(), faker.internet().safeEmailAddress(), faker.lorem().sentence()},
                {faker.name().firstName(), faker.internet().safeEmailAddress(), faker.lorem().sentence()},
                {faker.name().firstName(), faker.internet().safeEmailAddress(), faker.lorem().sentence()},
                {faker.name().firstName(), faker.internet().safeEmailAddress(), faker.lorem().sentence()},
        };
    }

    @Test(dataProvider = "testdata")
    public void runTest(String forename, String email, String message) {
// * 1. From the home page go to contact page
        HomePage homePage = new HomePage(driver);
        homePage.navigate();
        ContactPage contactPage = homePage.clickContact();
// * 2. Populate mandatory fields
        contactPage.withContactForm().fill(ContactForm.Field.FORENAME, forename);
        contactPage.withContactForm().fill(ContactForm.Field.EMAIL, email);
        contactPage.withContactForm().fill(ContactForm.Field.MESSAGE, message);
// * 3. Click submit button
        ContactFormSubmittedPage contactFormSubmittedPage = contactPage.withContactForm().clickSubmit();
// * 4. Validate successful submission message
        Assert.assertEquals(contactFormSubmittedPage.getSuccessBannerText(), String.format("Thanks %s, we appreciate your feedback.", forename), "Successful message is correct.");
    }
}
