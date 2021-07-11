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
import planit.gjacolbia.framework.configuration.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;
import planit.gjacolbia.tests.models.CartItemModel;

import java.util.List;
import java.util.stream.Collectors;

public class CartBasket extends LoadableComponent<CartBasket> {
    WebDriver driver;
    static final By PAGE_LOADED_INDICATOR = By.xpath("//div[@class='ng-scope' and @ui-if='cart.getCount() > 0']");

    LoadableComponent<?> parent;

    static final By ITEM_NAME_PATH = By.xpath("./td[1]");

    @FindBy(xpath = "//table[contains(@class, 'cart-items')]")
    WebElement cartItemsTable;
    @FindBy(xpath = "//table[contains(@class, 'cart-items')]//tr[contains(@class, 'cart-item')]")
    List<WebElement> cartItemRows;
    @FindBy(xpath = "//table[contains(@class, 'cart-items')]/tfoot/tr//strong[contains(@class, 'total')]")
    WebElement cartTotal;

    public CartBasket(WebDriver driver, LoadableComponent<?> parent) {
        this.driver = driver;
        this.parent = parent;
        PageFactory.initElements(driver, this);
    }

    public List<CartItemModel> withCartItemModels() {
        return cartItemRows.stream()
                .map(webElement -> new CartItem(driver, this, webElement.findElement(ITEM_NAME_PATH).getText()))
                .map(CartItem::getCartItemModel)
                .collect(Collectors.toList());
    }

    public List<CartItemModel> getCartItemModels() {
        return withCartItemModels();
    }

    public String getTotal() {
        String rawTotal = cartTotal.getText();
        return rawTotal.split(":")[1].strip();
    }

    @Override
    protected void load() {
        this.parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, PAGE_LOADED_INDICATOR), "Cart items not yet loaded.");
    }
}
