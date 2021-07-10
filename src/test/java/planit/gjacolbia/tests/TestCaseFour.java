package planit.gjacolbia.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import planit.gjacolbia.framework.models.PriceModel;
import planit.gjacolbia.tests.components.SimpleBoughtProductsListener;
import planit.gjacolbia.tests.models.CartItemModel;
import planit.gjacolbia.tests.pages.CartPage;
import planit.gjacolbia.tests.pages.ShopPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Test case 4: Advanced
 * 1. Buy 2 Stuffed Frog, 5 Fluffy Bunny, 3 Valentine Bear
 * 2. Go to the cart page
 * 3. Verify the price for each product
 * 4. Verify that each product’s sub total = product price * quantity
 * 5. Verify that total = sum(sub totals)
 */
public class TestCaseFour extends BaseTest {

    @Test
    public void runTest() {
// * 1. Buy 2 Stuffed Frog, 5 Fluffy Bunny, 3 Valentine Bear
        ShopPage shopPage = new ShopPage(getDriver());
        shopPage.navigate();
        SimpleBoughtProductsListener boughtProductsListener = new SimpleBoughtProductsListener();
        shopPage.withShopProducts(boughtProductsListener).withProduct("Stuffed Frog").clickBuy(2);
        shopPage.withShopProducts(boughtProductsListener).withProduct("Fluffy Bunny").clickBuy(5);
        shopPage.withShopProducts(boughtProductsListener).withProduct("Valentine Bear").clickBuy(3);
// * 2. Go to the cart page
        Optional<List<CartItemModel>> boughtItems = shopPage.withShopProducts().getBoughtItems();
        CartPage cartPage = shopPage.withNavBar().clickCart();
// * 3. Verify the price for each product
// * 4. Verify that each product’s sub total = product price * quantity
        if (boughtItems.isPresent()) {
            List<CartItemModel> actualCartItemModels = cartPage.withCartBasket().getCartItemModels();
            List<CartItemModel> expectedCartItemModels = boughtItems.get();
            Assert.assertEquals(actualCartItemModels.size(), expectedCartItemModels.size(), "All bought items are in cart.");
            actualCartItemModels.forEach(cartItemModel -> {
                CartItemModel expectedCartItem = getFilteredCartItemModel(cartItemModel, actualCartItemModels);
                Assert.assertEquals(expectedCartItem.getPrice(), cartItemModel.getPrice(), String.format("%s price from the cart is correct. Expected is %s, actual is %s", cartItemModel.getProductName(), expectedCartItem.getPrice(), cartItemModel.getPrice()));
                PriceModel expectedSubTotal = expectedCartItem.getPrice().multiply(expectedCartItem.getQuantity());
                PriceModel actualSubTotal = cartItemModel.getSubTotal();
                Assert.assertEquals(actualSubTotal, expectedSubTotal, String.format("%s subtotal price is correct. Expected is %s, actual is %s", cartItemModel.getProductName(), expectedSubTotal, actualSubTotal));
            });
        } else {
            Assert.fail("this should not happen since we are sure that we have products bought.");
        }
// * 5. Verify that total = sum(sub totals)
        String totalString = cartPage.withCartBasket().getTotal();
        String expectedTotalString = getTotalFromCartItemModels(cartPage.withCartBasket().getCartItemModels()).toString();
        Assert.assertEquals(totalString, expectedTotalString, String.format("The total amount displayed on cart page is correct. Expected is %s, actual is %s", expectedTotalString, totalString));
    }

    private CartItemModel getFilteredCartItemModel(CartItemModel cartItemModel, List<CartItemModel> cartItemModels) {
        return cartItemModels.stream()
                .filter(cartItem -> cartItem.getProductName().equals(cartItemModel.getProductName()))
                .findFirst().orElseThrow(() -> new NoSuchElementException(String.format("Unable to find %s from the expected cart items. Was this really bought?", cartItemModel.getProductName())));
    }

    private BigDecimal getTotalFromCartItemModels(List<CartItemModel> cartItemModels) {
        return cartItemModels.stream()
                .map(cartItemModel -> cartItemModel.getSubTotal().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
