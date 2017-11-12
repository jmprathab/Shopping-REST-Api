package com.prathab.data.datamodels;

public class CartItem {
  private String productId;
  private int quantity;

  public CartItem() {
  }

  public CartItem(String productId, int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("CartItem{");
    sb.append("productId='").append(productId).append('\'');
    sb.append(", quantity=").append(quantity);
    sb.append('}');
    return sb.toString();
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CartItem cartItem = (CartItem) o;

    return productId.equals(cartItem.productId);
  }

  @Override public int hashCode() {
    return productId.hashCode();
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
