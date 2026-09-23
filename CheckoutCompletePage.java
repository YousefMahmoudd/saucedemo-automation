package com.saucedemo.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderComplete() {
        return isDisplayed(completeHeader);
    }

    public String getConfirmationMessage() {
        return textOf(completeHeader);
    }

    public InventoryPage backToProducts() {
        click(backHomeButton);
        return new InventoryPage(driver);
    }
}
