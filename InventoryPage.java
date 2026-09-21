package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By inventoryContainer = By.id("inventory_container");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.className("product_sort_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By appLogo = By.className("app_logo");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(inventoryContainer);
    }

    public List<String> getProductNames() {
        return waitVisibleAll(productNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<BigDecimal> getProductPrices() {
        return waitVisibleAll(productPrices).stream()
                .map(el -> el.getText().replace("$", ""))
                .map(BigDecimal::new)
                .collect(Collectors.toList());
    }

    public InventoryPage sortBy(String visibleOptionText) {
        Select select = new Select(waitVisible(sortDropdown));
        select.selectByVisibleText(visibleOptionText);
        return this;
    }

    public InventoryPage sortByValue(String value) {
        Select select = new Select(waitVisible(sortDropdown));
        select.selectByValue(value);
        return this;
    }

    public ProductDetailsPage openProduct(String productName) {
        By productLink = By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']");
        click(productLink);
        return new ProductDetailsPage(driver);
    }

    public InventoryPage addProductToCartByName(String productName) {
        By addButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[contains(@class,'btn_inventory')]");
        click(addButton);
        return this;
    }

    public InventoryPage removeProductFromCartByName(String productName) {
        By removeButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[contains(@class,'btn_inventory')]");
        click(removeButton);
        return this;
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(textOf(cartBadge));
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(appLogo);
    }
}
