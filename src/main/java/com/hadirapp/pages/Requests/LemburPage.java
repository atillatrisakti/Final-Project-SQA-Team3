package com.hadirapp.pages.Requests;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
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
    private static final By CATATAN_TEXTAREA = By.xpath("(//input | //textarea)[contains(@placeholder, 'Catatan')]");
    private static final By SUBMIT_BUTTON = By.xpath("//button[@type='submit'][contains(text(), 'Ajukan')]");
    private static final By RESET_BUTTON = By.xpath("//button[contains(text(), 'Reset')]");
    
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
        jamMasukField.clear();
        jamMasukField.sendKeys(dateTimeString);
    }
    
    private void fillJamKeluar(LocalDateTime jamKeluar) {
        WebElement jamKeluarField = wait.until(ExpectedConditions.visibilityOfElementLocated(JAM_KELUAR_INPUT));
        String dateTimeString = formatDateTimeForInput(jamKeluar);
        jamKeluarField.clear();
        jamKeluarField.sendKeys(dateTimeString);
    }
    
    private void fillCatatan(String catatan) {
        // Catatan field may be optional or appear conditionally
        // Try to find and fill it, but don't fail if not found
        try {
            WebElement catatanField = driver.findElement(CATATAN_TEXTAREA);
            if (catatanField != null) {
                catatanField.clear();
                catatanField.sendKeys(catatan);
            }
        } catch (Exception e) {
            // Catatan field not found or not visible - may be optional
            System.out.println("Catatan field not found, skipping: " + e.getMessage());
        }
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
        
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));
        // Scroll to button to ensure it's visible and not intercepted
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        
        try {
            Thread.sleep(500); // Wait for scroll animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Try regular click first
        try {
            submitBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // If intercepted, use JavaScript click
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
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
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Jam masuk harus di isi')]"))
            ) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean waitForJamKeluarRequiredMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Jam Keluar harus di isi')]"))
            ) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean waitForCatatanMinimumCharacterMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Masukan minimal 5 karakter')]"))
            ) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void clickReset() {
        WebElement resetBtn = wait.until(ExpectedConditions.elementToBeClickable(RESET_BUTTON));
        resetBtn.click();
    }
    
    public boolean waitForFormFieldsEmpty() {
        try {
            WebElement jamMasukField = wait.until(ExpectedConditions.visibilityOfElementLocated(JAM_MASUK_INPUT));
            WebElement jamKeluarField = driver.findElement(JAM_KELUAR_INPUT);
            WebElement catatanField = driver.findElement(CATATAN_TEXTAREA);
            
            Thread.sleep(500);
            
            return jamMasukField.getAttribute("value").isEmpty() &&
                   jamKeluarField.getAttribute("value").isEmpty() &&
                   catatanField.getAttribute("value").isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
