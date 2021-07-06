package planit.gjacolbia.tests.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;

public class ShopProduct extends LoadableComponent<ShopProduct> {
    WebDriver driver;

    LoadableComponent<?> parent;
    String productName;
    By pageLoadedIndicator;

    String productTitlePattern = "//h4[contains(@class, 'product-title') and text() = '%s']";
    String productBuyButtonPattern = productTitlePattern + "/following-sibling::p/a[@class='btn btn-success' and text() = 'Buy']";


    public ShopProduct(WebDriver driver, LoadableComponent<?> parent, String productName) {
        this.driver = driver;
        this.parent = parent;
        this.productName = productName;
        this.pageLoadedIndicator = By.xpath(String.format(productTitlePattern, productName));
        PageFactory.initElements(driver, this);
    }

    public void clickBuy() {
        clickBuy(1);
    }

    public void clickBuy(int times) {
        for (int i = 0; i < times; i++) {
            this.withBuyButton().click();
        }
    }

    public WebElement withBuyButton() {
        return driver.findElement(By.xpath(String.format(productBuyButtonPattern, this.productName)));
    }

    @Override
    protected void load() {
        this.parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(this.pageLoadedIndicator));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, this.pageLoadedIndicator), "Shop product " + this.productName + " not loaded.");
    }
}
