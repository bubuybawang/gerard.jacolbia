package planit.gjacolbia.tests;

import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Test;
import planit.gjacolbia.tests.models.CartItemModel;
import planit.gjacolbia.tests.pages.CartPage;
import planit.gjacolbia.tests.pages.HomePage;
import planit.gjacolbia.tests.pages.ShopPage;

import java.util.List;

/**
 * Test case 3:
 * 1. From the home page go to shop page
 * 2. Click buy button 2 times on “Funny Cow”
 * 3. Click buy button 1 time on “Fluffy Bunny”
 * 4. Click the cart menu
 * 5. Verify the items are in the cart
 */
@Log4j2
public class TestCaseThree extends BaseTest {

    @Test
    public void runTest() {
// * 1. From the home page go to shop page
        HomePage homePage = new HomePage(getDriver());
        homePage.navigate();
        ShopPage shopPage = homePage.withNavBar().clickShop();
// * 2. Click buy button 2 times on “Funny Cow”
        shopPage.withShopProducts().withProduct("Funny Cow").clickBuy(2);
// * 3. Click buy button 1 time on “Fluffy Bunny”
        shopPage.withShopProducts().withProduct("Fluffy Bunny").clickBuy();
// * 4. Click the cart menu
        CartPage cartPage = shopPage.withNavBar().clickCart();
// * 5. Verify the items are in the cart
        List<CartItemModel> actualCartItems = cartPage.withCartBasket().withCartItemModels();
        Assert.assertTrue(actualCartItems.stream().anyMatch(cartItemModel -> cartItemModel.getProductName().equals("Funny Cow")), "Funny Cow is present in the cart.");
        Assert.assertTrue(actualCartItems.stream().anyMatch(cartItemModel -> cartItemModel.getProductName().equals("Fluffy Bunny")), "Fluffy Bunny is present in the cart.");
    }
}
