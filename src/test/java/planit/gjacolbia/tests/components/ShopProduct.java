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
import planit.gjacolbia.framework.models.PriceModel;
import planit.gjacolbia.tests.models.CartItemModel;

import java.util.ArrayList;
import java.util.List;

public class ShopProduct extends LoadableComponent<ShopProduct> {
    WebDriver driver;

    LoadableComponent<?> parent;
    String productName;
    By pageLoadedIndicator;

    String productTitlePattern = "//h4[contains(@class, 'product-title') and text() = '%s']";
    String productBuyButtonPattern = productTitlePattern + "/following-sibling::p/a[@class='btn btn-success' and text() = 'Buy']";
    String productPricePattern = productTitlePattern + "/following-sibling::p/span[contains(@class, 'product-price')]";


    public ShopProduct(WebDriver driver, LoadableComponent<?> parent, String productName) {
        this.driver = driver;
        this.parent = parent;
        this.productName = productName;
        this.pageLoadedIndicator = By.xpath(String.format(productTitlePattern, productName));
        PageFactory.initElements(driver, this);
    }

    public CartItemModel clickBuy() {
        return clickBuy(1);
    }

    public CartItemModel clickBuy(int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.withBuyButton().click();
        }
        return new CartItemModel(productName, price(), quantity);
    }

    public WebElement withBuyButton() {
        return driver.findElement(By.xpath(String.format(productBuyButtonPattern, this.productName)));
    }

    public PriceModel price() {
        return PriceModel.of(driver.findElement(By.xpath(String.format(productPricePattern, this.productName))).getText());
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
