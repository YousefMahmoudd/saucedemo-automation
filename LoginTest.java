package com.saucedemo.tests;
import com.saucedemo.dataproviders.CsvDataProviders;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Login with standard_user should land on the Inventory page")
    public void standardUserCanLoginSuccessfully() {
        InventoryPage inventoryPage = loginAsStandardUser();
        Assert.assertTrue(inventoryPage.isDisplayed(), "Inventory page should be displayed after a valid login");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "URL should navigate to inventory.html after successful login");
    }

    @Test(description = "locked_out_user must be blocked with a clear error message")
    public void lockedOutUserCannotLogin() {
        LoginPage loginPage = openLoginPage().loginExpectingFailure("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should be shown for a locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"),
                "Error message should mention the account is locked out");
    }

    @Test(description = "Login with an invalid username/password should show an error and stay on the login page")
    public void invalidCredentialsShowError() {
        LoginPage loginPage = openLoginPage().loginExpectingFailure("invalid_user", "wrong_password");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should be shown for invalid credentials");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should remain on the login page after a failed login");
    }

    @Test(description = "Login with an empty username should show a required-field error")
    public void emptyUsernameShowsError() {
        LoginPage loginPage = openLoginPage().loginExpectingFailure("", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error should be shown when username is empty");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("username"),
                "Error message should reference the missing username");
    }

    @Test(description = "Login with an empty password should show a required-field error")
    public void emptyPasswordShowsError() {
        LoginPage loginPage = openLoginPage().loginExpectingFailure("standard_user", "");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error should be shown when password is empty");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("password"),
                "Error message should reference the missing password");
    }

    @Test(description = "Login with both fields empty should show a required-field error")
    public void bothFieldsEmptyShowsError() {
        LoginPage loginPage = openLoginPage().loginExpectingFailure("", "");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error should be shown when both fields are empty");
    }

  
    @Test(dataProvider = "loginData", dataProviderClass = CsvDataProviders.class,
            description = "Data-driven login coverage across all accepted users and negative cases")
    public void dataDrivenLogin(String username, String password, String expectedResult, String description) {
        LoginPage loginPage = openLoginPage();
        switch (expectedResult) {
            case "SUCCESS": {
                InventoryPage inventoryPage = loginPage.login(username, password);
                Assert.assertTrue(inventoryPage.isDisplayed(),
                        description + " -- expected successful login for user: " + username);
                break;
            }
            case "LOCKED_OUT": {
                LoginPage result = loginPage.loginExpectingFailure(username, password);
                Assert.assertTrue(result.isErrorDisplayed(), description);
                Assert.assertTrue(result.getErrorMessage().toLowerCase().contains("locked out"), description);
                break;
            }
            default: { // INVALID_CREDENTIALS, MISSING_USERNAME, MISSING_PASSWORD-Negateive testcases 
                LoginPage result = loginPage.loginExpectingFailure(username, password);
                Assert.assertTrue(result.isErrorDisplayed(), description);
                break;
            }
        }
    }
}
