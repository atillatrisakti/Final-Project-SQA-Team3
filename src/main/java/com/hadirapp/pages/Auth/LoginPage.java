package com.hadirapp.pages.Auth;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final String BASE_URL = "https://magang.dikahadir.com/absen/login";
    private static final By EMAIL_INPUT = By.cssSelector("input[placeholder='Masukan Email kamu disini']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[placeholder='Masukan password kamu disini']");
    private static final By LOGIN_BUTTON = By.xpath("//button[contains(text(), 'Masuk')]");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void open() {
        driver.navigate().to(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(EMAIL_INPUT));
    }
    
    public void login(String email, String password) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT));
        emailField.clear();
        emailField.sendKeys(email);
        
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginBtn.click();
        
        wait.until(ExpectedConditions.urlContains("/absent"));
    }
}
