package planit.gjacolbia.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.configuration.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.components.ContactForm;

public class ContactPage extends LoadableComponent<ContactPage> {
    WebDriver driver;
    private static final By PAGE_LOADED_INDICATOR = By.xpath("//li[@id='nav-contact' and @class='active']");

    ContactForm contactForm;
    @FindBy(xpath = "//div[@id='header-message']/div[contains(@class, 'alert ')]")
    WebElement headerMessage;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        contactForm = new ContactForm(driver, this);
    }

    public ContactForm withContactForm() {
        return this.contactForm.get();
    }

    public String getHeaderMessageText() {
        return headerMessage.getText();
    }

    public boolean isHeaderMessageAnError() {
        return this.headerMessage.getAttribute("class").contains("alert-error");
    }

    @Override
    protected void load() {
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Contact page not yet loaded.");
    }
}
