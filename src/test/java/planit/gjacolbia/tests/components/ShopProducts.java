package planit.gjacolbia.tests.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;

public class ShopProducts extends LoadableComponent<ShopProducts> {
    WebDriver driver;
    private static final By PAGE_LOADED_INDICATOR = By.xpath("//div[contains(@class, 'products')]//li[contains(@class, 'product')]");

    LoadableComponent<?> parent;

    public ShopProducts(WebDriver driver, LoadableComponent<?> parent) {
        this.driver = driver;
        this.parent = parent;
        PageFactory.initElements(driver, this);
    }

    public ShopProduct withProduct(String productName) {
        return new ShopProduct(driver, this, productName);
    }

    @Override
    protected void load() {
        this.parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Shop items not yet loaded.");
    }
}
