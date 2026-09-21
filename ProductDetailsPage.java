package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.className("inventory_details_name");
    private final By productPrice = By.className("inventory_details_price");
    private final By productDescription = By.className("inventory_details_desc");
    private final By addToCartButton = By.cssSelector("button.btn_inventory");
    private final By backButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getName() {
        return textOf(productName);
    }

    public String getPrice() {
        return textOf(productPrice);
    }

    public String getDescription() {
        return textOf(productDescription);
    }

    public ProductDetailsPage addToCart() {
        click(addToCartButton);
        return this;
    }

    public InventoryPage backToInventory() {
        click(backButton);
        return new InventoryPage(driver);
    }
}
