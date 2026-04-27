package com.hadirapp.pages.Auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.hadirapp.utlis.WaitUtils;

public class RegisterPage {

    private WebDriver driver;

    public RegisterPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//button[text()='disini']")
    private WebElement registerButton;

    @FindBy(id = "nik")
    private WebElement NIKField;

    @FindBy(id = "fullname")
    private WebElement fullNameField;
    
    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "selfie")
    private WebElement selfieUploadField;

    @FindBy(xpath = "//button[text()='Daftar']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@role='alert']//p")
    private WebElement alertMessage;


public void clickRegisterButton(){
    registerButton.click();
}

public void setNIK(String NIK){
    NIKField.clear();
    NIKField.sendKeys(NIK);
}

public void setFullName(String fullName){
    fullNameField.clear();
    fullNameField.sendKeys(fullName);
}

public void setEmail(String email){
    emailField.clear();
    emailField.sendKeys(email);
}

public void setPassword(String password){
    passwordField.clear();
    passwordField.sendKeys(password);
}

public void uploadSelfie(String filePath){
    if (filePath != null && !filePath.isEmpty()) {
        selfieUploadField.sendKeys(filePath);
    }
}

public void clickSubmitButton(){
    submitButton.click();
}

public void doRegister(String NIK, String fullName, String email, String password, String filePath){
    clickRegisterButton();
    setNIK(NIK);
    setFullName(fullName);
    setEmail(email);
    setPassword(password);
    uploadSelfie(filePath);
    clickSubmitButton();
}

public String getAlertMessage() {
    return alertMessage.getText();
}

public boolean isAlertMessageDisplayed() {
    try {
        WaitUtils.getExplicitWait(driver, 5).until(ExpectedConditions.visibilityOf(alertMessage));
        return true;
    } catch (Exception e) {
        return false;
    }
}

public String getActiveValidationMessage() {
    WebElement activeElement = driver.switchTo().activeElement();
    return activeElement.getAttribute("validationMessage");
}

}

