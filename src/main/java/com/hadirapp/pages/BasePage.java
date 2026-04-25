package com.hadirapp.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hadirapp.utlis.Constants;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.SLOW_SERVER_TIMEOUT));
        this.wait.pollingEvery(Duration.ofMillis(Constants.POLLING_MILLIS));
    }

    protected WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        try {
            waitUntilClickable(locator).click();
        } catch (Exception ex) {
            WebElement element = waitUntilVisible(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected void waitUntilUrlContains(String partialUrl) {
        wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    protected void waitUntilTextVisible(String text) {
        By locator = By.xpath("//*[contains(normalize-space(.),\"" + text + "\")]");
        waitUntilVisible(locator);
    }

    protected WebElement waitAnyVisible(By... locators) {
        List<By> locatorList = Arrays.asList(locators);
        return wait.until(driver -> locatorList.stream()
                .map(locator -> {
                    try {
                        List<WebElement> elements = driver.findElements(locator);
                        return elements.stream().filter(WebElement::isDisplayed).findFirst().orElse(null);
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(element -> element != null)
                .findFirst()
                .orElse(null));
    }

    protected void clickAnyVisible(By... locators) {
        wait.until(driver -> {
            for (By locator : locators) {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    for (WebElement element : elements) {
                        try {
                            if (element.isDisplayed()) {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                                return true;
                            }
                        } catch (StaleElementReferenceException ignored) {
                            // Dicoba lagi pada polling berikutnya jika DOM berubah.
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Dicoba lagi pada polling berikutnya jika DOM berubah.
                }
            }
            return false;
        });
    }

    protected By firstVisibleLocator(By... locators) {
        wait.until((ExpectedCondition<Boolean>) driver -> Arrays.stream(locators)
                .anyMatch(this::hasVisibleElement));

        for (By locator : locators) {
            if (hasVisibleElement(locator)) {
                return locator;
            }
        }
        throw new TimeoutException("Tidak ada locator yang tampil dalam waktu tunggu.");
    }

    public boolean isAnyVisible(By... locators) {
        return Arrays.stream(locators).anyMatch(this::hasVisibleElement);
    }

    private boolean hasVisibleElement(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // DOM di halaman target cukup dinamis, jadi stale element dianggap belum stabil dan dicoba lagi pada polling berikutnya.
                }
            }
            return false;
        } catch (StaleElementReferenceException ignored) {
            return false;
        }
    }
}
