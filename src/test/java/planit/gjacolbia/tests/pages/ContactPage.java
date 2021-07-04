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
import planit.gjacolbia.framework.Configuration;

public class ContactPage extends LoadableComponent<ContactPage> {
    WebDriver driver;
    private static final By PAGE_LOADED_INDICATOR = By.xpath("//a[contains(@class, 'btn-contact') and contains(@ui-event,'onSubmit') and text()='Submit']");

    @FindBy(xpath = "//a[contains(@class, 'btn-contact') and contains(@ui-event,'onSubmit') and text()='Submit']")
    WebElement submitButton;
    @FindBy(xpath = "//div[@id='header-message']/div[contains(@class, 'alert ')]")
    WebElement headerMessage;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickSubmit() {
        submitButton.click();
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
        // TODO Use util for elementNotPresent
        Assert.assertFalse(driver.findElements(PAGE_LOADED_INDICATOR).isEmpty(), "Contact page not yet loaded.");
    }
}
