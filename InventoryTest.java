package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Step 2: Product Inventory and Sorting.
 */
public class InventoryTest extends BaseTest {

    @Test(description = "Inventory page should display product names and prices for every product")
    public void productsAreDisplayedWithNameAndPrice() {
        InventoryPage inventoryPage = loginAsStandardUser();
        List<String> names = inventoryPage.getProductNames();
        List<BigDecimal> prices = inventoryPage.getProductPrices();

        Assert.assertFalse(names.isEmpty(), "Product list should not be empty");
        Assert.assertEquals(prices.size(), names.size(), "Every product should have a corresponding price");
        for (BigDecimal price : prices) {
            Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price should be a positive value");
        }
    }

    @Test(description = "Sorting by Name (A to Z) should list products alphabetically")
    public void sortByNameAToZ() {
        InventoryPage inventoryPage = loginAsStandardUser().sortBy("Name (A to Z)");
        List<String> actual = inventoryPage.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());
        Assert.assertEquals(actual, expected, "Products should be sorted A to Z by name");
    }

    @Test(description = "Sorting by Name (Z to A) should list products in reverse alphabetical order")
    public void sortByNameZToA() {
        InventoryPage inventoryPage = loginAsStandardUser().sortBy("Name (Z to A)");
        List<String> actual = inventoryPage.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(actual, expected, "Products should be sorted Z to A by name");
    }

    @Test(description = "Sorting by Price (low to high) should list products in ascending price order")
    public void sortByPriceLowToHigh() {
        InventoryPage inventoryPage = loginAsStandardUser().sortBy("Price (low to high)");
        List<BigDecimal> actual = inventoryPage.getProductPrices();
        List<BigDecimal> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());
        Assert.assertEquals(actual, expected, "Products should be sorted ascending by price");
    }

    @Test(description = "Sorting by Price (high to low) should list products in descending price order")
    public void sortByPriceHighToLow() {
        InventoryPage inventoryPage = loginAsStandardUser().sortBy("Price (high to low)");
        List<BigDecimal> actual = inventoryPage.getProductPrices();
        List<BigDecimal> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(actual, expected, "Products should be sorted descending by price");
    }

    @Test(description = "Opening a product should display its name, price and description; back returns to inventory")
    public void productDetailsAreDisplayedAndBackNavigationWorks() {
        InventoryPage inventoryPage = loginAsStandardUser();
        String productName = inventoryPage.getProductNames().get(0);

        ProductDetailsPage detailsPage = inventoryPage.openProduct(productName);
        Assert.assertEquals(detailsPage.getName(), productName, "Details page should show the selected product's name");
        Assert.assertFalse(detailsPage.getPrice().isBlank(), "Product price should be displayed");
        Assert.assertFalse(detailsPage.getDescription().isBlank(), "Product description should be displayed");

        InventoryPage backOnInventory = detailsPage.backToInventory();
        Assert.assertTrue(backOnInventory.isDisplayed(), "Should return to the Inventory page");
    }
}
