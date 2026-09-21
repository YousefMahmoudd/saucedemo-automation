package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutStepTwoPage extends BasePage {

    private final By itemNames = By.className("inventory_item_name");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return waitVisibleAll(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public BigDecimal getSubtotal() {
        return parseCurrency(textOf(subtotalLabel));
    }

    public BigDecimal getTax() {
        return parseCurrency(textOf(taxLabel));
    }

    public BigDecimal getTotal() {
        return parseCurrency(textOf(totalLabel));
    }

    private BigDecimal parseCurrency(String labelText) {
        // e.g. "Item total: $29.99"
        String numeric = labelText.replaceAll("[^0-9.]", "");
        return new BigDecimal(numeric);
    }

    public CheckoutCompletePage finishOrder() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public InventoryPage cancel() {
        click(cancelButton);
        return new InventoryPage(driver);
    }
}
