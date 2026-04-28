package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HistoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//*[contains(text(), 'Selengkapnya')]")
    private WebElement btnSelengkapnya;

    @FindBy(xpath = "//p[contains(text(), 'Absensi')]")
    private WebElement btnAbsensi;

    public HistoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void klikSelengkapnya() {
        wait.until(ExpectedConditions.visibilityOf(btnSelengkapnya));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSelengkapnya);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSelengkapnya);
    }

    public void klikAbsensi() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        wait.until(ExpectedConditions.elementToBeClickable(btnAbsensi));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAbsensi);
    }
}