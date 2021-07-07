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
import planit.gjacolbia.tests.models.CartItemModel;

import java.util.List;
import java.util.Optional;

public class ShopProducts extends LoadableComponent<ShopProducts> {
    WebDriver driver;
    private static final By PAGE_LOADED_INDICATOR = By.xpath("//div[contains(@class, 'products')]//li[contains(@class, 'product')]");

    LoadableComponent<?> parent;
    Optional<BoughtProductsListener> boughtProductsListener;

    public ShopProducts(WebDriver driver, LoadableComponent<?> parent) {
        this.driver = driver;
        this.parent = parent;
        boughtProductsListener = Optional.empty();
        PageFactory.initElements(driver, this);
    }

    public ShopProduct withProduct(String productName) {
        if (boughtProductsListener.isPresent()) {
            return new ShopProduct(driver, this, productName, boughtProductsListener.get());
        } else {
            return new ShopProduct(driver, this, productName);
        }
    }

    public ShopProduct andWithProduct(String productName) {
        return withProduct(productName);
    }

    public ShopProducts addBoughtProductsListener(BoughtProductsListener boughtProductsListener) {
        this.boughtProductsListener = Optional.of(boughtProductsListener);
        return this;
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

    public Optional<List<CartItemModel>> getBoughtItems() {
        if (boughtProductsListener.isPresent()) {
            return Optional.of(boughtProductsListener.get().getProductsBought());
        }
        return Optional.empty();
    }
}
