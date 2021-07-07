package planit.gjacolbia.tests.components;

import planit.gjacolbia.tests.models.CartItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleBoughtProductsListener implements BoughtProductsListener {
    List<CartItemModel> cartItemModels;

    public SimpleBoughtProductsListener() {
        cartItemModels = new ArrayList<>();
    }

    @Override
    public void updateProductsBought(CartItemModel cartItemModel) {
        Optional<CartItemModel> currentCartItemModel = this.cartItemModels.stream()
                .filter(cartItem -> cartItem.getProductName().equals(cartItemModel.getProductName())).findFirst();
        if (currentCartItemModel.isPresent()) {
            int currentQuantity = currentCartItemModel.get().getQuantity();
            currentCartItemModel.get().setQuantity(currentQuantity + cartItemModel.getQuantity());
        } else {
            cartItemModels.add(cartItemModel);
        }
    }

    @Override
    public List<CartItemModel> getProductsBought() {
        return this.cartItemModels;
    }
}
