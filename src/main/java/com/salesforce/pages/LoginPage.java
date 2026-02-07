package com.salesforce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameField;
    
    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;
    
    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;
    
    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMeCheckbox;
    
    @FindBy(xpath = "//div[@id='error']")
    private WebElement errorMessage;
    
    @FindBy(xpath = "//div[@class='loginError']")
    private WebElement loginErrorContainer;
    
    public LoginPage(WebDriver driver) throws IllegalArgumentException {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance cannot be null");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    public void enterUsername(String username) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            wait.until(ExpectedConditions.elementToBeClickable(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter username: " + e.getMessage(), e);
        }
    }
    
    public void enterPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            wait.until(ExpectedConditions.elementToBeClickable(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter password: " + e.getMessage(), e);
        }
    }
    
    public void clickLoginButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click login button: " + e.getMessage(), e);
        }
    }
    
    public void selectRememberMe() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
            if (!rememberMeCheckbox.isSelected()) {
                rememberMeCheckbox.click();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to select remember me: " + e.getMessage(), e);
        }
    }
    
    public void performLogin(String username, String password) throws IllegalArgumentException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }
        try {
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
    
    public void performLoginWithRememberMe(String username, String password) throws IllegalArgumentException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }
        try {
            enterUsername(username);
            enterPassword(password);
            selectRememberMe();
            clickLoginButton();
        } catch (Exception e) {
            throw new RuntimeException("Login with remember me failed: " + e.getMessage(), e);
        }
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(errorMessage),
                ExpectedConditions.visibilityOf(loginErrorContainer)
            ));
            return errorMessage.isDisplayed() || loginErrorContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getErrorMessageText() {
        try {
            if (errorMessage.isDisplayed()) {
                return errorMessage.getText();
            } else if (loginErrorContainer.isDisplayed()) {
                return loginErrorContainer.getText();
            }
            return "";
        } catch (Exception e) {
            throw new RuntimeException("Failed to get error message text: " + e.getMessage(), e);
        }
    }
    
    public boolean isUsernameFieldDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            return usernameField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPasswordFieldDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            return passwordField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isLoginButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginButton));
            return loginButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get current URL: " + e.getMessage(), e);
        }
    }
    
    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get page title: " + e.getMessage(), e);
        }
    }
}
