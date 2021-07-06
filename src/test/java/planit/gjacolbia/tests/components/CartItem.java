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

public class CartItem extends LoadableComponent<CartItem> {
    WebDriver driver;

    LoadableComponent<?> parent;
    String productName;
    String productItemPattern = "//tr[contains(@class, 'cart-item')]/td[1 and normalize-space(text()) = '%s']/parent::tr";
    String productItemPricePattern = productItemPattern + "/td[2]";
    String productItemQuantityPattern = productItemPattern + "/td[3]/input";
    String productItemSubTotalPattern = productItemPattern + "/td[4]";
    By pageLoadedIndicator;


    public CartItem(WebDriver driver, LoadableComponent<?> parent, String productName) {
        this.driver = driver;
        this.parent = parent;
        this.productName = productName;
        PageFactory.initElements(driver, this);
        pageLoadedIndicator = By.xpath(String.format(productItemPattern, productName));
    }

    public CartItemModel getCartItemModel() {
        return new CartItemModel(this.productName, this.price(), this.quantity(), this.subTotal());
    }

    public PriceModel price() {
        return PriceModel.of(driver.findElement(By.xpath(String.format(productItemPricePattern, this.productName))).getText());
    }

    public int quantity() {
        return Integer.parseInt(withQuantity().getAttribute("value"));
    }

    public PriceModel subTotal() {
        return PriceModel.of(driver.findElement(By.xpath(String.format(productItemSubTotalPattern, this.productName))).getText());
    }

    public WebElement withQuantity() {
        return driver.findElement(By.xpath(String.format(productItemQuantityPattern, this.productName)));
    }

    @Override
    protected void load() {
        this.parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(this.pageLoadedIndicator));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, this.pageLoadedIndicator), "Cart Item " + this.productName + " not loaded.");
    }
}
