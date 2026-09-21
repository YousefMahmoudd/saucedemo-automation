package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By errorButton = By.cssSelector(".error-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public InventoryPage clickLoginExpectingSuccess() {
        click(loginButton);
        return new InventoryPage(driver);
    }

    public LoginPage clickLoginExpectingFailure() {
        click(loginButton);
        return this;
    }

    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginExpectingSuccess();
    }

    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginExpectingFailure();
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return textOf(errorMessage);
    }

    public void dismissError() {
        if (isDisplayed(errorButton)) {
            click(errorButton);
        }
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginButton);
    }
}
