package com.hadirapp.pages.Auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class ResetPasswordPage {
    private WebDriver driver;

    public ResetPasswordPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Step 1 Request Forgot Password
    @FindBy(xpath = "//button[contains(text(), 'Lupa password')]")
    private WebElement forgotPasswordButton;

    @FindBy(xpath = "//button[text()='Login']")
    private WebElement backToLoginButton;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitRequestButton;

    // Step 2 Forgot Password Field
    @FindBy(id = "password")
    private WebElement newPasswordField;

    @FindBy(id = "passwordConfirm")
    private WebElement confirmNewPasswordField;

    @FindBy(id = "otp")
    private WebElement otpField;

     @FindBy(xpath = "//button[text()='Submit']") 
    private WebElement submitNewPasswordButton;
    
    @FindBy(xpath = "//div[@role='alert']//p")
    private WebElement alertMessage;

    public void clickForgotPasswordButton() {
        WaitUtils.waitForElementClickable(driver, forgotPasswordButton, 10);
        forgotPasswordButton.click();
    }

    public void clickBackToLoginButton() {
        WaitUtils.waitForElementClickable(driver, backToLoginButton, 10);
        backToLoginButton.click();
    }

    public void requestResetPassword(String email) {
        emailField.clear();
        emailField.sendKeys(email);
        WaitUtils.waitForElementClickable(driver, submitRequestButton, 10);
        submitRequestButton.click();
    }

    public void submitNewPassword(String newPassword, String confirmPassword, String otp) {
        newPasswordField.clear();
        newPasswordField.sendKeys(newPassword);
        
        confirmNewPasswordField.clear();
        confirmNewPasswordField.sendKeys(confirmPassword);
        
        otpField.clear();
        otpField.sendKeys(otp);
        
        WaitUtils.waitForElementClickable(driver, submitNewPasswordButton, 10);
        submitNewPasswordButton.click();
    }

    public boolean isAlertMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, alertMessage, 5);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertMessage() {
        WaitUtils.waitForElementVisible(driver, alertMessage, 10);
        return alertMessage.getText();
    }

    public String getActiveValidationMessage() {
    WebElement activeElement = driver.switchTo().activeElement();
    return activeElement.getAttribute("validationMessage");
}

    
}
