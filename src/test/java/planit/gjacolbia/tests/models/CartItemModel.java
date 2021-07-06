package planit.gjacolbia.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import planit.gjacolbia.framework.models.PriceModel;

@Data
@AllArgsConstructor
public class CartItemModel {
    String productName;
    PriceModel price;
    int quantity;
    PriceModel subTotal;

    public CartItemModel(String productName, PriceModel price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
