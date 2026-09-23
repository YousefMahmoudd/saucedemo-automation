package com.saucedemo.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getCartItemNames() {
        return waitVisibleAll(cartItemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public CartPage removeItemByName(String productName) {
        By removeButton = By.xpath("//div[@class='inventory_item_name' and text()='" + productName
                + "']/ancestor::div[@class='cart_item']//button[contains(@class,'cart_button')]");
        click(removeButton);
        return this;
    }

    public CheckoutStepOnePage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }
}
