package com.hadirapp.pages.Auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.hadirapp.utils.WaitUtils;

public class LoginPage {

    private final WebDriver driver;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@role='alert']//p")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setEmail(String email){
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void setPassword(String password){
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton(){
        WaitUtils.waitForElementClickable(driver, loginButton, 10);
        loginButton.click();
    }


    public void doLogin(String email, String password){
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        WaitUtils.waitForElementVisible(driver, errorMessage, 10);
        return errorMessage.getText();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, errorMessage, 5);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
}
