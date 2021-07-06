package planit.gjacolbia.tests.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.components.CartBasket;

@Log4j2
public class CartPage extends LoadableComponent<CartPage> {
    WebDriver driver;
    static final By PAGE_LOADED_INDICATOR = By.xpath("//li[@id = 'nav-cart' and @class = 'active']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public CartBasket withCartBasket() {
        try {
            return new CartBasket(driver, this).get();
        } catch (TimeoutException e) {
            log.error("Expecting to have a shopping cart with items on it but basket failed to load.");
            throw e;
        }
    }

    @Override
    protected void load() {
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Cart page not yet loaded.");
    }
}
