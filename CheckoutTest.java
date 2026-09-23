package com.saucedemo.tests;
import com.saucedemo.dataproviders.CsvDataProviders;
import com.saucedemo.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.util.List;

/**
 * Step 4: End-to-End Checkout scenarios - happy path totals verification plus
 */
public class CheckoutTest extends BaseTest {

    @Test(description = "Full checkout flow: add product, fill info, verify totals, finish, verify confirmation")
    public void endToEndCheckoutSucceedsWithCorrectTotals() {
        InventoryPage inventoryPage = loginAsStandardUser();
        String product = inventoryPage.getProductNames().get(0);
        BigDecimal price = inventoryPage.getProductPrices().get(0);

        inventoryPage.addProductToCartByName(product);
        CartPage cartPage = inventoryPage.openCart();

        CheckoutStepOnePage stepOne = cartPage.proceedToCheckout();
        CheckoutStepTwoPage stepTwo = stepOne.fillCustomerInfo("John", "Doe", "12345").continueToOverview();

        List<String> items = stepTwo.getItemNames();
        Assert.assertEquals(items, List.of(product), "Overview should list the product that was added to cart");

        BigDecimal subtotal = stepTwo.getSubtotal();
        BigDecimal tax = stepTwo.getTax();
        BigDecimal total = stepTwo.getTotal();

        Assert.assertEquals(subtotal, price, "Subtotal should equal the product's price for a single item");
        Assert.assertEquals(total, subtotal.add(tax), "Total should equal subtotal plus tax");

        CheckoutCompletePage completePage = stepTwo.finishOrder();
        Assert.assertTrue(completePage.isOrderComplete(), "Order confirmation should be displayed");
        Assert.assertTrue(completePage.getConfirmationMessage().toLowerCase().contains("thank you"),
                "Confirmation message should thank the customer for the order");
    }

    /**
     * Data-driven negative-case coverage: missing first name / last name / postal code,
     * sourced from checkout_data.csv.
     */
    @Test(dataProvider = "checkoutData", dataProviderClass = CsvDataProviders.class,
            description = "Checkout should reject submission when a required customer info field is missing")
    public void checkoutRequiresAllCustomerFields(String firstName, String lastName, String postalCode,
                                                   String expectedResult, String description) {
        InventoryPage inventoryPage = loginAsStandardUser();
        String product = inventoryPage.getProductNames().get(0);
        inventoryPage.addProductToCartByName(product);

        CheckoutStepOnePage stepOne = inventoryPage.openCart().proceedToCheckout();
        stepOne.fillCustomerInfo(
                firstName.isEmpty() ? null : firstName,
                lastName.isEmpty() ? null : lastName,
                postalCode.isEmpty() ? null : postalCode
        );

        if ("SUCCESS".equals(expectedResult)) {
            CheckoutStepTwoPage stepTwo = stepOne.continueToOverview();
            Assert.assertFalse(stepTwo.getItemNames().isEmpty(), description);
        } else {
            CheckoutStepOnePage result = stepOne.continueExpectingError();
            Assert.assertTrue(result.isErrorDisplayed(), description);
        }
    }
}
