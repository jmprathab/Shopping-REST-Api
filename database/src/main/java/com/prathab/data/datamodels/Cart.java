package com.prathab.data.datamodels;

import java.util.ArrayList;

public class Cart {
  private ArrayList<CartItem> cartItems;

  public ArrayList<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(ArrayList<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("Cart{");
    sb.append("cartItems=").append(cartItems);
    sb.append('}');
    return sb.toString();
  }
}
