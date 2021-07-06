package planit.gjacolbia.tests.components;

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
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.pages.CartPage;
import planit.gjacolbia.tests.pages.ContactPage;
import planit.gjacolbia.tests.pages.ShopPage;

public class NavBar extends LoadableComponent<NavBar> {
    private static final By PAGE_LOADED_INDICATOR = By.id("nav-contact");

    WebDriver driver;
    LoadableComponent<?> parent;

    @FindBy(id = "nav-home")
    WebElement homeNav;
    @FindBy(id = "nav-shop")
    WebElement shopNav;
    @FindBy(id = "nav-contact")
    WebElement contactNav;
    @FindBy(id = "nav-cart")
    WebElement cartNav;

    public NavBar(WebDriver driver, LoadableComponent<?> parent) {
        this.driver = driver;
        this.parent = parent;
        PageFactory.initElements(driver, this);
    }

    public ContactPage clickContact() {
        contactNav.click();
        return new ContactPage(driver).get();
    }

    public ShopPage clickShop() {
        shopNav.click();
        return new ShopPage(driver);
    }

    public CartPage clickCart() {
        cartNav.click();
        return new CartPage(driver);
    }

    @Override
    protected void load() {
        parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Nav bar not loaded.");
    }
}
