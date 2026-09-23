package com.saucedemo.tests;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Step 6: Verify Cart State During Navigation and across login sessions.
 */
public class CartSessionTest extends BaseTest {

    @Test(description = "Cart contents should be preserved when navigating between Inventory and Cart pages")
    public void cartPersistsWhenNavigatingBetweenPages() {
        InventoryPage inventoryPage = loginAsStandardUser();
        String product = inventoryPage.getProductNames().get(0);
        inventoryPage.addProductToCartByName(product);

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartItemNames(), java.util.List.of(product), "Cart should contain the added product");

        InventoryPage backToInventory = cartPage.continueShopping();
        Assert.assertEquals(backToInventory.getCartBadgeCount(), 1, "Cart badge should still show 1 item after navigating back");

        CartPage cartAgain = backToInventory.openCart();
        Assert.assertEquals(cartAgain.getCartItemNames(), java.util.List.of(product),
                "Cart contents should be unchanged after navigating away and back");
    }

    @Test(description = "SauceDemo clears the cart on logout: verify cart/session behavior across a fresh login")
    public void cartSessionBehaviorAcrossLoginSessions() {
        InventoryPage inventoryPage = loginAsStandardUser();
        String product = inventoryPage.getProductNames().get(0);
        inventoryPage.addProductToCartByName(product);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart should show 1 item before logout");

        LoginPage loginPage = inventoryPage.logout();

        InventoryPage secondSessionInventory = loginPage.login("standard_user", "secret_sauce");

        // Document actual SauceDemo behavior: cart resets to empty on a fresh login after logout.
        Assert.assertEquals(secondSessionInventory.getCartBadgeCount(), 0,
                "Cart should be empty on a new login session after a logout (SauceDemo does not persist cart across sessions)");
    }
}
