package com.hadirapp.pages.Auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.hadirapp.utlis.WaitUtils;

public class LogoutPage {

    private WebDriver driver;

    public LogoutPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//button[.//img[@alt='menu']]")
    private WebElement menuButton;

    @FindBy(xpath = "//button[text()='Logout']")
    private WebElement logoutButton;

    public void clickMenuButton(){
        WaitUtils.waitForElementClickable(driver, menuButton, 10);
        menuButton.click();
    }
    
    public void clickLogoutButton(){
        WaitUtils.waitForElementClickable(driver, logoutButton, 10);
        logoutButton.click();
    }

    public void doLogout(){
        clickMenuButton();
        clickLogoutButton();
    }
    
}
