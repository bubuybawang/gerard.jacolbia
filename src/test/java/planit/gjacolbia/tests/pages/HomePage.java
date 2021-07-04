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

public class HomePage extends LoadableComponent<HomePage> {
    private static final String URL = "http://jupiter.cloud.planittesting.com/";
    WebDriver driver;

    @FindBy(id = "nav-contact")
    WebElement contactPageLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigate() {
        driver.get(URL);
    }

    public void clickContact() {
        contactPageLink.click();
    }

    @Override
    protected void load() {
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-contact")));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(contactPageLink.isDisplayed(), "Home page not yet loaded");
    }
}
