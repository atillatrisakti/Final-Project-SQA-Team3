package com.hadirapp.pages.Auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.hadirapp.pages.BasePage;
import com.hadirapp.utlis.Constants;

public class LoginPage extends BasePage {

    private final By emailInput = By.xpath("//input[@type='email' or contains(@placeholder,'Email') or @name='email']");
    private final By passwordInput = By.xpath("//input[@type='password' or contains(@placeholder,'Password') or @name='password']");
    private final By loginButton = By.xpath("//button[contains(normalize-space(.),'Masuk')]");
    private final By postLoginMarker = By.xpath("//img[@alt='Absensi'] | //*[contains(normalize-space(.),'Absensi')] | //*[contains(normalize-space(.),'Lembur')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(Constants.LOGIN_URL);
        waitUntilVisible(emailInput);
    }

    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
        waitAnyVisible(postLoginMarker);
    }
}
