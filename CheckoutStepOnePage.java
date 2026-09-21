package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepOnePage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepOnePage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        if (firstName != null) type(firstNameInput, firstName);
        if (lastName != null) type(lastNameInput, lastName);
        if (postalCode != null) type(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutStepTwoPage continueToOverview() {
        click(continueButton);
        return new CheckoutStepTwoPage(driver);
    }

    public CheckoutStepOnePage continueExpectingError() {
        click(continueButton);
        return this;
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return textOf(errorMessage);
    }
}
