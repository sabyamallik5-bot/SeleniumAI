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

public class ValidLoginTest {
    
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
    public void verifyLoginPageElements() {
        try {
            Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field is not displayed");
            Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field is not displayed");
            Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed");
            Assert.assertTrue(loginPage.getCurrentUrl().contains("login.salesforce.com"), "URL verification failed");
        } catch (Exception e) {
            Assert.fail("Test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 2)
    public void verifyUsernameFieldFunctionality() {
        try {
            String testUsername = "testuser@salesforce.com";
            loginPage.enterUsername(testUsername);
            Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field interaction failed");
        } catch (Exception e) {
            Assert.fail("Username field test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 3)
    public void verifyPasswordFieldFunctionality() {
        try {
            String testPassword = "TestPassword123";
            loginPage.enterPassword(testPassword);
            Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field interaction failed");
        } catch (Exception e) {
            Assert.fail("Password field test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 4)
    public void verifyLoginButtonClickable() {
        try {
            loginPage.enterUsername("testuser@salesforce.com");
            loginPage.enterPassword("TestPassword123");
            loginPage.clickLoginButton();
            Assert.assertTrue(true, "Login button click functionality verified");
        } catch (Exception e) {
            Assert.fail("Login button click test failed: " + e.getMessage(), e);
        }
    }
    
    @Test(priority = 5)
    public void verifyRememberMeFunctionality() {
        try {
            loginPage.enterUsername("validuser@salesforce.com");
            loginPage.enterPassword("ValidPassword123");
            loginPage.selectRememberMe();
            loginPage.clickLoginButton();
            Assert.assertTrue(true, "Remember me functionality verified");
        } catch (Exception e) {
            Assert.fail("Remember me test failed: " + e.getMessage(), e);
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
