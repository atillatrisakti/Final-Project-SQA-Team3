package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.hadirapp.utils.WaitUtils;

public class HistoryPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[contains(text(), 'Selengkapnya')]")
    private WebElement btnSelengkapnya;

    @FindBy(xpath = "//p[contains(text(), 'Absensi')]")
    private WebElement btnAbsensi;

    public HistoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void klikSelengkapnya() {
        WaitUtils.waitForElementVisible(driver, btnSelengkapnya, 20);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSelengkapnya);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSelengkapnya);
    }

    public void klikAbsensi() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        WaitUtils.waitForElementClickable(driver, btnAbsensi, 20);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAbsensi);
    }
}