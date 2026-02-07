package com.salesforce.tests;

import com.salesforce.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class InvalidLoginTest {
    
    private WebDriver driver;
    private LoginPage loginPage;
    private static final String BASE_URL = "https://login.salesforce.com/?locale=in";
    
    @BeforeMethod
    public void setUp() throws Exception {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.get(BASE_URL);
            loginPage = new LoginPage(driver);
        } catch (Exception e) {
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("Setup failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 1)
    public void verifyLoginWithInvalidUsername() {
        try {
            loginPage.performLogin("invaliduser@test.com", "InvalidPassword123");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed for invalid username");
        } catch (Exception e) {
            Assert.fail("Invalid username test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 2)
    public void verifyLoginWithInvalidPassword() {
        try {
            loginPage.performLogin("validuser@salesforce.com", "WrongPassword");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed for invalid password");
        } catch (Exception e) {
            Assert.fail("Invalid password test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 3)
    public void verifyLoginWithEmptyCredentials() {
        try {
            loginPage.clickLoginButton();
            Assert.assertTrue(loginPage.getCurrentUrl().contains("login.salesforce.com"), "Page should remain on login");
        } catch (Exception e) {
            Assert.fail("Empty credentials test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 4)
    public void verifyLoginWithInvalidUsernameFormat() {
        try {
            loginPage.performLogin("invalidemail", "TestPassword123");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.getCurrentUrl().contains("login"), "Invalid format validation failed");
        } catch (Exception e) {
            Assert.fail("Invalid username format test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 5)
    public void verifyLoginWithSpecialCharacters() {
        try {
            loginPage.performLogin("user@#$%^&*()@test.com", "Pass!@#$%^&*()");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed for special characters");
        } catch (Exception e) {
            Assert.fail("Special characters test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 6)
    public void verifyLoginWithSQLInjection() {
        try {
            loginPage.performLogin("' OR '1'='1", "' OR '1'='1");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.getCurrentUrl().contains("login"), "SQL injection protection failed");
        } catch (Exception e) {
            Assert.fail("SQL injection test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 7)
    public void verifyMultipleFailedLoginAttempts() {
        try {
            for (int i = 0; i < 3; i++) {
                loginPage.performLogin("invaliduser@test.com", "WrongPassword" + i);
                Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed on attempt " + (i + 1));
                driver.navigate().refresh();
                loginPage = new LoginPage(driver);
            }
        } catch (Exception e) {
            Assert.fail("Multiple failed attempts test failed: " + e.getMessage(), e);
        }
    }
    
    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Teardown warning: " + e.getMessage());
        }
    }
}
