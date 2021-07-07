package planit.gjacolbia.tests.components;

import planit.gjacolbia.tests.models.CartItemModel;

import java.util.List;

public interface BoughtProductsListener {
    void updateProductsBought(CartItemModel cartItemModel);
    List<CartItemModel> getProductsBought();
}
