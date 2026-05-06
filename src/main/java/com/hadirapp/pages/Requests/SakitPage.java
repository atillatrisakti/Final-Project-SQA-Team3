package com.hadirapp.pages.Requests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hadirapp.utlis.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SakitPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//a[@class='user__menu__item']//p[text()='Sakit']")
    public WebElement menuSakit;

    @FindBy(xpath = "//button[contains(text(), 'Ajukan Sakit')]")
    public WebElement btnAjukanSakit;

    @FindBy(xpath = "//*[local-name()='svg' and @data-testid='AccessAlarmIcon']")
    public WebElement iconJamKalender;

    @FindBy(xpath = "//input[@type='file']")
    public WebElement inputUploadFile;

    @FindBy(xpath = "//button[text()='Simpan']")
    public WebElement btnSimpanKalender;

    @FindBy(xpath = "//button[@type='submit' and text()='Ajukan']")
    public WebElement btnSubmit;

    @FindBy(xpath = "//button[text()='Reset']")
    public WebElement btnReset;

    @FindBy(xpath = "//span[contains(text(), '.png')]")
    public WebElement labelFileName;

    @FindBy(xpath = "//input[@placeholder='Pilih Tanggal']")
    public WebElement inputTanggal;

    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]//p[text()=' / ' or text()='/']")
    public WebElement textTanggal;

    @FindBy(xpath = "//div[@role='alert']//div[contains(text(), 'sudah melakukan izin sakit')]")
    public WebElement toastErrorDuplicate;

    @FindBy(xpath = "//p[contains(@class, 'MuiFormHelperText-root')]")
    public List<WebElement> listErrorMessages;

    @FindBy(xpath = "//div[@role='alert']//div[contains(text(), 'Incorrect datetime')]")
    public WebElement toastErrorInvalidDate;

    @FindBy(xpath = "//div[contains(@class, 'MuiSnackbarContent-message')]")
    public WebElement snackbarErrorMsg;


    // Constructor
    public SakitPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Method untuk memilih rentang tanggal sakit
    public void pilihRentangTanggal(String tglMulai, String tglSelesai) {
        WaitUtils.waitForElementClickable(driver, iconJamKalender, 10).click();
        clickTanggal(tglMulai);
        clickTanggal(tglSelesai);
        WaitUtils.waitForElementClickable(driver, btnSimpanKalender, 10).click();
    }

    // method pilih tanggal
    private void clickTanggal(String tgl) {
        String xpathTgl = String.format("//button[contains(@class, 'rdrDay')]//span[text()='%s']", tgl);

        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathTgl)));
            element.click();
        } catch (Exception e) {
            WebElement element = driver.findElement(By.xpath(xpathTgl));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void klikMenuSakit(){
        WaitUtils.waitForElementClickable(driver, menuSakit, 10);
        try {
            menuSakit.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", menuSakit);
        }
    }

    public void klikTombolAjukanSakit() {
        WaitUtils.waitForElementClickable(driver, btnAjukanSakit, 10);
        try {
            btnAjukanSakit.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btnAjukanSakit);
        }
    }


    // Method untuk upload dokumen
    public void uploadDokumen(String path) {
        inputUploadFile.sendKeys(path);
    }

    // Method untuk klik tombol Ajukan
    public void klikTombolSubmit() {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnSubmit);
            Thread.sleep(500);

            WaitUtils.waitForElementClickable(driver, btnSubmit, 10).click();
        } catch (Exception e) {
            System.out.println("Klik normal gagal, mencoba JavaScript Click...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btnSubmit);
        }
    }

    // Method untuk mendapatkan teks jika tanggal kosong
    public String getTeksTanggal() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(textTanggal));
            return element.getText().trim();
        } catch (Exception e) {
            WebElement element = driver
                    .findElement(By.xpath("//label[text()='Pilih Tanggal']/following-sibling::div//p"));
            return element.getText().trim();
        }
    }

    // Method untuk klik tombol Reset
    public void klikReset() {
        WaitUtils.waitForElementClickable(driver, btnReset, 10).click();
    }

    public String getErrorMessageText() {
        try {
            WaitUtils.waitForElementVisible(driver, snackbarErrorMsg, 10);
            return snackbarErrorMsg.getText();
        } catch (Exception e) {
            return "Error message tidak muncul";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, snackbarErrorMsg, 10);
            return snackbarErrorMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Method untuk mendapatkan semua pesan error yang muncul
    public List<String> getAllErrorMessages() {
        wait.until(ExpectedConditions.visibilityOfAllElements(listErrorMessages));
        List<String> messages = new ArrayList<>();
        for (WebElement error : listErrorMessages) {
            messages.add(error.getText());
        }
        return messages;
    }
}