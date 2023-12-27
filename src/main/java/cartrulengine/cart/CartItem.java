package cartrulengine.cart;

public class CartItem {

    private String productId;

    private String category;

    private int quantity;

    public CartItem() {}

    public CartItem(final String productId, final String category, final int quantity) {
        this.productId = productId;
        this.category = category;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}
