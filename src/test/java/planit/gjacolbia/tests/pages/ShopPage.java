package planit.gjacolbia.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.configuration.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.components.BoughtProductsListener;
import planit.gjacolbia.tests.components.NavBar;
import planit.gjacolbia.tests.components.ShopProducts;

public class ShopPage extends LoadableComponent<ShopPage> {
    WebDriver driver;
    private static final By PAGE_LOADED_INDICATOR = By.xpath("//div[contains(@class, 'products')]//li[contains(@class, 'product')]");
    private static final String URL = "https://jupiter.cloud.planittesting.com/#/shop";

    ShopProducts shopProducts;
    NavBar navBar;

    public ShopPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        shopProducts = new ShopProducts(driver, this);
        navBar = new NavBar(driver, this);
    }

    public void navigate() {
        driver.get(URL);
        driver.manage().window().maximize();
        this.get();
    }

    public NavBar withNavBar() {
        return navBar.get();
    }

    public ShopProducts withShopProducts() {
        return this.shopProducts.get();
    }

    public ShopProducts withShopProducts(BoughtProductsListener boughtProductsListener) {
        return this.shopProducts.addBoughtProductsListener(boughtProductsListener).get();
    }

    @Override
    protected void load() {
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Shop items not yet loaded.");
    }
}
