package com.saucedemo.tests;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Step 5: Logout.
 */
public class LogoutTest extends BaseTest {

    @Test(description = "Logging out from the menu should return the user to the Login page")
    public void logoutReturnsToLoginPage() {
        InventoryPage inventoryPage = loginAsStandardUser();
        LoginPage loginPage = inventoryPage.logout();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed after logout");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("saucedemo.com/") || driver.getCurrentUrl().endsWith("index.html"),
                "URL should return to the login page after logout");
    }
}
