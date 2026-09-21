package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Step 3: Shopping Cart - add/remove products, verify badge count and cart contents.
 */
public class CartTest extends BaseTest {

    @Test(description = "Adding two products to the cart, removing one, should leave the correct remaining item and badge count")
    public void addTwoRemoveOneLeavesCorrectRemainingItem() {
        InventoryPage inventoryPage = loginAsStandardUser();
        List<String> allProducts = inventoryPage.getProductNames();
        String firstProduct = allProducts.get(0);
        String secondProduct = allProducts.get(1);

        inventoryPage.addProductToCartByName(firstProduct);
        inventoryPage.addProductToCartByName(secondProduct);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items");

        CartPage cartPage = inventoryPage.openCart();
        List<String> cartItems = cartPage.getCartItemNames();
        Assert.assertEquals(cartItems.size(), 2, "Cart should contain 2 products");
        Assert.assertTrue(cartItems.contains(firstProduct) && cartItems.contains(secondProduct),
                "Cart should contain both added products");

        cartPage.removeItemByName(firstProduct);
        List<String> remaining = cartPage.getCartItemNames();
        Assert.assertEquals(remaining.size(), 1, "One product should remain in the cart");
        Assert.assertEquals(remaining.get(0), secondProduct, "Remaining product should be the second one added");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should reflect the removal");
    }

    @Test(description = "Cart badge should not appear when the cart is empty")
    public void emptyCartShowsNoBadge() {
        InventoryPage inventoryPage = loginAsStandardUser();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, "Cart badge should show 0 / be absent when empty");
    }

    @Test(description = "Removing the only item from the cart should leave the cart empty")
    public void removingOnlyItemLeavesCartEmpty() {
        InventoryPage inventoryPage = loginAsStandardUser();
        String product = inventoryPage.getProductNames().get(0);
        inventoryPage.addProductToCartByName(product);

        CartPage cartPage = inventoryPage.openCart();
        cartPage.removeItemByName(product);

        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty after removing the only item");
    }
}
