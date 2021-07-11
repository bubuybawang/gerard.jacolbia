package planit.gjacolbia.tests;

import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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
public class TestCaseTwo extends BaseTest {
    @DataProvider(name = "testdata", parallel = true)
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
        HomePage homePage = new HomePage(getDriver());
        homePage.navigate();
        ContactPage contactPage = homePage.withNavBar().clickContact();
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
