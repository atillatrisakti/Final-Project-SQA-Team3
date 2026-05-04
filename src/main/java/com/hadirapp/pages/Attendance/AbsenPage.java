package com.hadirapp.pages.Attendance;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbsenPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final String ABSENT_PAGE_URL = "https://magang.dikahadir.com/apps/absent";
    private static final By LEMBUR_MENU = By.xpath("//a[@class='user__menu__item']//p[text()='Lembur']");
    private static final By LEMBUR_MENU_LINK = By.xpath("//a[contains(@class, 'user__menu__item') and .//p[text()='Lembur']]");
    private static final By AJUKAN_LEMBUR_BUTTON = By.xpath("//button[contains(text(), 'Ajukan Lembur')]");
    
    public AbsenPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void openAbsentModule() {
        driver.navigate().to(ABSENT_PAGE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(LEMBUR_MENU));
    }
    
    public void ensureAbsentPage() {
        driver.navigate().to(ABSENT_PAGE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(LEMBUR_MENU));
    }
    
    public void clickLemburButton() {
        clickLemburMenuWithRetry();
        wait.until(ExpectedConditions.presenceOfElementLocated(AJUKAN_LEMBUR_BUTTON));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AJUKAN_LEMBUR_BUTTON));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void clickLemburMenuWithRetry() {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement lemburLink = wait.until(ExpectedConditions.presenceOfElementLocated(LEMBUR_MENU_LINK));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        lemburLink);
                wait.until(ExpectedConditions.elementToBeClickable(LEMBUR_MENU_LINK)).click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                sleepBriefly();
            }
        }

        WebElement lemburLink = wait.until(ExpectedConditions.presenceOfElementLocated(LEMBUR_MENU_LINK));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", lemburLink);
    }

    private void sleepBriefly() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for Lembur menu to stabilize.", e);
        }
    }
    
    public boolean isAjukanLemburVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(AJUKAN_LEMBUR_BUTTON)) != null;
        } catch (Exception e) {
            return false;
        }
    }
}