package com.hadirapp.pages.Requests;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LemburPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final By AJUKAN_LEMBUR_BTN = By.xpath("//button[contains(text(), 'Ajukan Lembur')]");
    private static final By JAM_MASUK_INPUT = By.xpath("//input[@placeholder='dd mm yyyy, hh:mm']");
    private static final By JAM_KELUAR_INPUT = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[2]");
    private static final By LEMBUR_FORM = By.xpath(
            "(//input[@placeholder='dd mm yyyy, hh:mm'])[1]/ancestor::*[.//input[@placeholder='dd mm yyyy, hh:mm']"
                    + " and .//button[normalize-space()='Ajukan']][1]");
    private static final By CATATAN_FIELD = By.xpath(
            "//textarea[contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //input[contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //textarea[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //input[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //textarea[contains(translate(@id, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //input[contains(translate(@id, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'catatan')]"
                    + " | //textarea[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'note')]"
                    + " | //input[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'note')]"
                    + " | //textarea[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'description')]"
                    + " | //input[contains(translate(@name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'description')]"
                    + " | //*[normalize-space()='Catatan']/following::textarea[1]"
                    + " | //*[normalize-space()='Catatan']/following::input[1]"
                    + " | (//textarea)[1]");
    private static final By SUBMIT_BUTTON = By.xpath(
            "(//input[@placeholder='dd mm yyyy, hh:mm'])[1]/ancestor::*[.//input[@placeholder='dd mm yyyy, hh:mm']"
                    + " and .//button[normalize-space()='Ajukan']][1]//button[normalize-space()='Ajukan' and not(contains(normalize-space(), 'Lembur'))]");
    
    public LemburPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void clickAjukanLembur() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(AJUKAN_LEMBUR_BTN));
        button.click();
        // Wait for form to appear - wait for the first input field to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(JAM_MASUK_INPUT));
        try {
            Thread.sleep(500); // Give form time to fully render
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void fillOvertimeForm(LocalDateTime jamMasuk, LocalDateTime jamKeluar, String catatan, String keterangan) {
        fillJamMasuk(jamMasuk);
        fillJamKeluar(jamKeluar);
        fillCatatan(catatan);
    }
    
    public void fillOvertimeFormWithoutJamMasuk(LocalDateTime jamKeluar, String catatan) {
        fillJamKeluar(jamKeluar);
        fillCatatan(catatan);
    }
    
    public void fillOvertimeFormWithoutJamKeluar(LocalDateTime jamMasuk, String catatan) {
        fillJamMasuk(jamMasuk);
        fillCatatan(catatan);
    }
    
    public void fillOvertimeFormWithoutCatatan(LocalDateTime jamMasuk, LocalDateTime jamKeluar) {
        fillJamMasuk(jamMasuk);
        fillJamKeluar(jamKeluar);
    }
    
    private void fillJamMasuk(LocalDateTime jamMasuk) {
        WebElement jamMasukField = wait.until(ExpectedConditions.visibilityOfElementLocated(JAM_MASUK_INPUT));
        String dateTimeString = formatDateTimeForInput(jamMasuk);
        setDateTimeField(jamMasukField, dateTimeString);
    }
    
    private void fillJamKeluar(LocalDateTime jamKeluar) {
        WebElement jamKeluarField = wait.until(ExpectedConditions.visibilityOfElementLocated(JAM_KELUAR_INPUT));
        String dateTimeString = formatDateTimeForInput(jamKeluar);
        setDateTimeField(jamKeluarField, dateTimeString);
    }

    private void setDateTimeField(WebElement field, String value) {
        setInputValue(field, value);
    }

    private void setInputValue(WebElement field, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "const element = arguments[0];"
                        + "const value = arguments[1];"
                        + "const prototype = element.tagName === 'TEXTAREA' ? HTMLTextAreaElement.prototype : HTMLInputElement.prototype;"
                        + "const setter = Object.getOwnPropertyDescriptor(prototype, 'value').set;"
                        + "setter.call(element, value);"
                        + "element.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "element.dispatchEvent(new Event('change', { bubbles: true }));",
                field,
                value);
    }
    
    private void fillCatatan(String catatan) {
        WebElement catatanField = wait.until(ExpectedConditions.visibilityOfElementLocated(CATATAN_FIELD));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", catatanField);
        setInputValue(catatanField, catatan);
        wait.until(driver -> catatan.equals(getInputValue(catatanField)));
    }
    
    private String formatDateTimeForInput(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm");
        return dateTime.format(formatter);
    }
    
    public void submitOvertime() {
        try {
            Thread.sleep(2000); // Wait 2 seconds for form to fully stabilize after filling
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        clickSubmitButtonWithRetry();
    }

    private void clickSubmitButtonWithRetry() {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement submitBtn = wait.until(driver -> findFormSubmitButton());
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        submitBtn);
                waitForButtonToSettle();
                wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                waitForButtonToSettle();
            }
        }

        WebElement submitBtn = wait.until(driver -> findFormSubmitButton());
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
    }

    private WebElement findFormSubmitButton() {
        try {
            return driver.findElement(SUBMIT_BUTTON);
        } catch (NoSuchElementException e) {
            WebElement form = driver.findElement(LEMBUR_FORM);
            return form.findElement(By.xpath(".//button[normalize-space()='Ajukan' and not(contains(normalize-space(), 'Lembur'))]"));
        }
    }
    
    public boolean waitForSubmissionResult() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'berhasil')]|//div[contains(text(), 'Sukses')]"))
            ) != null;
        } catch (Exception e) {
            return true;
        }
    }
    
    public boolean waitForJamMasukRequiredMessage() {
        try {
            return wait.until(driver -> isValidationMessageVisible("Jam masuk harus di isi")
                    || isFieldInvalid(JAM_MASUK_INPUT));
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean waitForJamKeluarRequiredMessage() {
        try {
            return wait.until(driver -> isValidationMessageVisible("Jam Keluar harus di isi")
                    || isFieldInvalid(JAM_KELUAR_INPUT));
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean waitForCatatanMinimumCharacterMessage() {
        try {
            return wait.until(driver -> isValidationMessageVisible("Masukan minimal 5 karakter")
                    || isCatatanInvalidOrUnavailable());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidationMessageVisible(String message) {
        String lowerCaseMessage = message.toLowerCase();
        String xpath = "//*[contains(translate(normalize-space(), "
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                + lowerCaseMessage + "')]";
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private boolean isFieldInvalid(By locator) {
        try {
            WebElement field = driver.findElement(locator);
            Object valid = ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", field);
            return Boolean.FALSE.equals(valid);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isCatatanInvalidOrUnavailable() {
        try {
            WebElement field = driver.findElement(CATATAN_FIELD);
            Object valid = ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", field);
            return Boolean.FALSE.equals(valid);
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    public void clickReset() {
        WebElement resetBtn = wait.until(driver -> findFormResetButton());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", resetBtn);
        try {
            resetBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resetBtn);
        }
        waitForResetActionToFinish();
    }

    private WebElement findFormResetButton() {
        try {
            WebElement form = driver.findElement(LEMBUR_FORM);
            return form.findElement(By.xpath(".//button[normalize-space()='Reset']"));
        } catch (NoSuchElementException e) {
            WebElement jamMasukField = driver.findElement(JAM_MASUK_INPUT);
            return jamMasukField.findElement(By.xpath(
                    "./ancestor::*[.//button[normalize-space()='Ajukan']][1]//button[normalize-space()='Reset']"));
        }
    }
    
    public boolean waitForFormFieldsEmpty() {
        try {
            return wait.until(driver -> {
                WebElement jamMasukField = driver.findElement(JAM_MASUK_INPUT);
                WebElement jamKeluarField = driver.findElement(JAM_KELUAR_INPUT);

                return getInputValue(jamMasukField).isEmpty()
                        && getInputValue(jamKeluarField).isEmpty()
                        && isOptionalCatatanEmpty();
            });
        } catch (Exception e) {
            return false;
        }
    }

    public String getFormFieldValuesSummary() {
        return "Jam Masuk='" + getCurrentValueOrEmpty(JAM_MASUK_INPUT)
                + "', Jam Keluar='" + getCurrentValueOrEmpty(JAM_KELUAR_INPUT)
                + "', Catatan='" + getCurrentValueOrEmpty(CATATAN_FIELD) + "'";
    }

    private boolean isOptionalCatatanEmpty() {
        try {
            return getInputValue(driver.findElement(CATATAN_FIELD)).isEmpty();
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public boolean waitForValidatorProcessingPopup() {
        try {
            return wait.until(driver -> isValidationMessageVisible(
                    "permintaan request lembur anda sedang diproses validator"));
        } catch (Exception e) {
            return false;
        }
    }

    private String getInputValue(WebElement element) {
        String value = element.getAttribute("value");
        return value == null ? "" : value.trim();
    }

    private String getCurrentValueOrEmpty(By locator) {
        try {
            return getInputValue(driver.findElement(locator));
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private void waitForResetActionToFinish() {
        waitForButtonToSettle();
    }

    private void waitForButtonToSettle() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for button action to finish.", e);
        }
    }
}
